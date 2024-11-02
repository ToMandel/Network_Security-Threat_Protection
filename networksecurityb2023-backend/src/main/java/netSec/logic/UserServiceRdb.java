package netSec.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import netSec.boundaries.UserBoundary;
import netSec.dal.Crud;
import netSec.data.UserEntity;
import netSec.utils.Converter;
import netSec.utils.Hasher;
import netSec.utils.ServerSideEmail;

@Service
public class UserServiceRdb implements UserService {
	private Crud userCrud;
	private Converter converter;
	private String nameFromSpringConfig;
	private String userNotFound = "User not found.";
	Properties config = new Properties();
	private static final String PASS_CONFIG_FILE = "src/main/resources/passwordConfig.properties";
	private static final String EMAIL_CONFIG_FILE = "src/main/resources/Email.properties";
	private static final int DEFAULT_PASSWORD_HISTORY_LENGTH = 3;
	private static final Map<String, Integer> loginAttempts = new HashMap<>();
	private String sqlUrl;
	private boolean safetyFlag;

	@Autowired
	public void setUserCrud(Crud userCrud) {
		this.userCrud = userCrud;
	}

	@Autowired
	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	// have spring inject a configuration to this method
	@Value("${spring.application.name:defaultAfekaDemoValue}")
	public void setNameFromSpringConfig(String nameFromSpringConfig) {
		this.nameFromSpringConfig = nameFromSpringConfig;
	}

	@Value("${spring.datasource.url:jdbc:mysql://localhost:3306/netsec?user=root}")
	public void sqlUrlFromSpringConfig(String sqlUrl) {
		this.sqlUrl = sqlUrl;
	}

	@Value("${safety.flag:true}")
	public void setSafetyFlagFromSpringConfig(boolean safetyFlag) {
		this.safetyFlag = safetyFlag;
	}

	@PostConstruct
	public void init() {
		System.err.println("**** spring.application.name=" + this.nameFromSpringConfig);
		System.err.println("**** spring.datasource.ur=" + this.sqlUrl);
		System.err.println("**** safety.flag=" + this.safetyFlag);
	}

	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		if (user == null) {
			throw new BadRequestException("Bad input, User is null");
		}

		String internalUserId = UUID.randomUUID().toString();
		user.setId(internalUserId);

		if (user.getPassword() == null || user.getPassword().isBlank()) {
			throw new BadRequestException("Bad input, User passowrd is blank!");
		}

		UserEntity existingUserName = null;
		if (safetyFlag) {
			// Connection to your database
			existingUserName = queryUserByUsername(user, existingUserName);
		} else {
			// To check if there is a user with the same username already
			existingUserName = this.userCrud.findByUserName(user.getUserName());
		}
		if (existingUserName != null) {
			throw new BadRequestException("User with same Username already exists");
		}

		UserEntity existingEmail = null;
		if (safetyFlag) {
			// Connection to your database
			existingEmail = queryUserByEmail(user, existingEmail);

		} else {
			// To check if there is a user with the same email already
			existingEmail = this.userCrud.findByEmail(user.getEmail());
		}
		if (existingEmail != null) {
			throw new BadRequestException("User with same email already exists");
		}

		if (!isEmailValid(user.getEmail())) {
			throw new BadRequestException("Bad input, Email is not valid format");
		}
		if (!checkPass(user.getPassword())) {
			throw new BadRequestException("Bad input, please enter a password according to format");
		}
		byte[] salt = Hasher.generateSalt();
		String hashedPass = Hasher.hashPassword(user.getPassword(), salt);
//		System.err.println(hashedPass);

		UserEntity entity = this.converter.toEntity(user);
		entity.setPassword(hashedPass);
		entity.setSalt(salt);
		entity.setPasswordHistory(hashedPass);
		entity = this.userCrud.save(entity);
		UserBoundary rv = this.converter.toBoundary(entity);
		return rv;
	}

	@Override
	public UserBoundary login(UserBoundary user) {
		if (user == null) {
			throw new BadRequestException(userNotFound);
		}

		try (FileInputStream fis = new FileInputStream(PASS_CONFIG_FILE)) {
			config.load(fis);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load configuration file: " + PASS_CONFIG_FILE);
		}

		int maxLoginTries = Integer.parseInt(config.getProperty("login.tries.max"));
		String username = user.getUserName();

		if (username == null) {
			throw new UnauthorizedException("there is no such user");
		}
		UserEntity entity = null;
		if (safetyFlag) {
			// Connection to your database
			queryUserByUsername(user, entity);
		} else {
			entity = this.userCrud.findByUserName(username);
		}
		
		if (entity == null) {
			throw new RuntimeException("Could not init object.");
		}
		// add prep statement

		int loginAttemptCount = loginAttempts.getOrDefault(username, 0);

		if (user.getPassword() == null ||
		/* hashedPass.toString().equals(entity.getPassword()) */
				!Hasher.verifyPassword(user.getPassword(), entity.getPassword(), entity.getSalt())) {
			// Increment login attempt count for the user
			loginAttemptCount++;
			loginAttempts.put(username, loginAttemptCount);
			if (loginAttemptCount >= maxLoginTries) {
				throw new UnauthorizedException("You have exceeded the maximum login attempts");
			} else {
				// Still have login attempts left
				throw new UnauthorizedException("You entered the wrong password");
			}
		}

		// if the user enters a correct password after he exceeded the number of login
		// tries
		if (loginAttemptCount >= maxLoginTries) {
			throw new UnauthorizedException("You have exceeded the maximum login attempts");
		} else {
			// Successful login, reset the login attempts for the user
			loginAttempts.remove(username);
		}
		return this.converter.toBoundary(entity);
	}

	@Override
	@Transactional
	public void updateUser(UserBoundary update) {

		if (update == null) {
			throw new BadRequestException("User is null");
		}
		String username = update.getUserName();
		if (username == null || username.isBlank()) {
			throw new BadRequestException("You have to enter a username");
		}
		
		UserEntity entity = null;
		if (safetyFlag) {
			// Connection to your database
			queryUserByUsername(update, entity);
		} else {
			entity = this.userCrud.findByUserName(username);
		}

		// update entity
		String newPassword = update.getNewPass();
		if (newPassword == null) {
			throw new BadRequestException("You have to enter a password");
		}

		if (!checkPass(newPassword)) {
			throw new BadRequestException("Bad input, please enter a password according to format");
		}
		byte[] salt = entity.getSalt();
		String hashedPass = Hasher.hashPassword(newPassword, salt);

		update.setPassword(hashedPass);
		if (!checkPassHistory(entity, update)) {
			throw new BadRequestException("Bad input, the password was already used before try a new one");
		}
		entity.setPassword(hashedPass);
		entity.setPasswordHistory(hashedPass);

		// save (UPDATE) entity to database if message was indeed updated
		this.userCrud.save(entity);
	}

	@Override
	@Transactional
	public void updatePass(UserBoundary user) {
		UserBoundary boundary = (login(user));
		boundary.setNewPass(user.getNewPass());
		updateUser(boundary);
	}

	@Override
	@Transactional
	public int forgotPass(UserBoundary boundary) throws NoSuchAlgorithmException {
		UserEntity user = null;
		if (safetyFlag) {
			// Connection to your database
			queryUserByUsername(boundary, user);
		} else {
			user = this.userCrud.findByUserName(boundary.getUserName());
		}
		if (user == null) {
			throw new NotFoundException(userNotFound);
		}

		int rv = this.sendEmail(user.getEmail());
		return rv;
	}

	private int sendEmail(String email) throws NoSuchAlgorithmException {

		try (FileInputStream fis = new FileInputStream(EMAIL_CONFIG_FILE)) {
			config.load(fis);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load configuration file: " + EMAIL_CONFIG_FILE);
		}
		// Email subject and content
		int otp = Hasher.get6Digits();
		String senderEmail = config.getProperty("email.user");
		String senderPassword = config.getProperty("email.pass");
		String emailRecipient = email;
		String emailSubject = config.getProperty("email.subject");
		String emailContent = "Hello!\nYour 6 digit code to recover your password in Communications LTD is " + otp
				+ "\nEnter it to reset your password";
		ServerSideEmail emailObj = new ServerSideEmail(senderEmail, senderPassword, emailSubject, otp, emailContent,
				new Properties());

		// Create a session with an authenticator
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		};
		Session session = Session.getInstance(emailObj.getProperties(), auth);

		try {
			// Create a MimeMessage object
			MimeMessage message = new MimeMessage(session);

			// Set sender and recipient addresses
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipient));

			// Set email subject and content
			message.setSubject(emailSubject);
			message.setText(emailContent);

			// Send the email
			Transport.send(message);

		} catch (MessagingException e) {
			System.out.println("Failed to send email. Error: " + e.getMessage());
			throw new RuntimeException();
		}
		return otp;
	}

	@Override
	@Transactional
	public void deleteAllUsers() {
		this.userCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers() {
		List<UserEntity> entities = this.userCrud.findAll();
		List<UserBoundary> rv = new ArrayList<>();
		for (UserEntity e : entities) {
			rv.add(this.converter.toBoundary(e));
		}
		return rv;
	}

	private UserEntity queryUserByUsername(UserBoundary user, UserEntity existingUserName) {
		Connection con;
		try {
			con = DriverManager.getConnection(this.sqlUrl);
			// Query which needs parameters
			String query = "Select * from USERS where userName = ?";
			// Prepare Statement
			PreparedStatement myStmt = con.prepareStatement(query);

			// Set Parameters
			myStmt.setString(1, user.getUserName());

			// Execute SQL query
			ResultSet rs = myStmt.executeQuery();

			existingUserName = new UserEntity();
			// Display function to show the Resultset
			while (rs.next()) {
				String username = rs.getString("userName");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String id = rs.getString("id");
				byte[] salt = rs.getString("salt").getBytes();

				System.out.println(username + "     " + password);
				existingUserName.setPassword(password);
				existingUserName.setUserName(username);
				existingUserName.setEmail(email);
				existingUserName.setId(id);
				existingUserName.setSalt(salt);
			}
			// Close the connection
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return existingUserName;
	}

	private UserEntity queryUserByEmail(UserBoundary user, UserEntity existingEmail) {
		Connection con;
		try {
			con = DriverManager.getConnection(this.sqlUrl);
			// Query which needs parameters
			String query = "Select * from USERS where userName = ?";
			// Prepare Statement
			PreparedStatement myStmt = con.prepareStatement(query);

			// Set Parameters
			myStmt.setString(1, user.getUserName());
			myStmt.setString(2, user.getPassword());

			// Execute SQL query
			ResultSet rs = myStmt.executeQuery();

			existingEmail = new UserEntity();
			// Display function to show the Resultset
			while (rs.next()) {
				String username = rs.getString("userName");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String id = rs.getString("id");
				byte[] salt = rs.getString("salt").getBytes();

				System.out.println(username + "     " + password);
				existingEmail.setPassword(password);
				existingEmail.setUserName(username);
				existingEmail.setEmail(email);
				existingEmail.setId(id);
				existingEmail.setSalt(salt);
			}
			// Close the connection
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
		return existingEmail;
	}
	
	private boolean checkPassHistory(UserEntity entity, UserBoundary boundary) {
		// Load the password history length from the configuration file
		int passwordHistoryLength = getPasswordHistoryLength();

		// Check password history
		if (isPasswordInHistory(boundary.getPassword(), entity.getPasswordHistory(), passwordHistoryLength)) {
			return false;
		}
		return true;
	}

	private boolean checkPass(String password) {

		try (FileInputStream fis = new FileInputStream(PASS_CONFIG_FILE)) {
			config.load(fis);
		} catch (IOException e) {
			System.err.println("Failed to load configuration file: " + PASS_CONFIG_FILE);
			return false;
		}

		int minPasswordLength = Integer.parseInt(config.getProperty("password.length.min"));
		String requirements = config.getProperty("password.requirements");
		boolean dictionaryCheck = Boolean.parseBoolean(config.getProperty("dictionary.check"));

		// Check password length
		if (password.length() < minPasswordLength) {
			return false;
		}

		// Check password requirements
		for (char req : requirements.toCharArray()) {

			switch (req) {
			case 'U':
				if (!hasUppercase(password)) {
					throw new BadRequestException("Your password doesn't include uppercase letters");
				}
				break;
			case 'L':
				if (!hasLowercase(password)) {
					throw new BadRequestException("Your password doesn't include Lowercase letters");
				}
				break;
			case 'D':
				if (!hasDigit(password)) {
					throw new BadRequestException("Your password doesn't include Digits");
				}
				break;
			case 'S':
				if (!hasSpecialChar(password)) {
					throw new BadRequestException("Your password doesn't include Special Char");
				}
				break;
			default:
				System.err.println("Invalid password requirement: " + req);
				throw new BadRequestException("Invalid password requirement: " + req);
			}
		}

//		 Check dictionary words
		if (dictionaryCheck && isPasswordInDictionary(password)) {
			throw new BadRequestException("Your password should not include Strings from dictionaries");
		}

		// Password passed all checks, so it is strong
		return true;
	}

	private static boolean hasUppercase(String password) {
		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasLowercase(String password) {
		for (char c : password.toCharArray()) {
			if (Character.isLowerCase(c)) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasDigit(String password) {
		for (char c : password.toCharArray()) {
			if (Character.isDigit(c)) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasSpecialChar(String password) {
		for (char c : password.toCharArray()) {
			if (!Character.isLetterOrDigit(c)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isPasswordInDictionary(String password) {
		// Load the dictionary word list from a file or any external source
		List<String> dictionaryWords = loadDictionaryWords();

		// Convert the password to lowercase for case-insensitive comparison
		String lowercasePassword = password.toLowerCase();

		// Check if the password is found in the dictionary word list
		return dictionaryWords.contains(lowercasePassword);
	}

	private int getPasswordHistoryLength() {
		try (FileInputStream fis = new FileInputStream(PASS_CONFIG_FILE)) {
			config.load(fis);
		} catch (IOException e) {
			System.err.println("Failed to load configuration file: " + PASS_CONFIG_FILE);
			return DEFAULT_PASSWORD_HISTORY_LENGTH;
		}

		return Integer.parseInt(
				config.getProperty("password.history.amount", String.valueOf(DEFAULT_PASSWORD_HISTORY_LENGTH)));
	}

	private boolean isPasswordInHistory(String password, List<String> passwordHistory, int passwordHistoryLength) {
		if (passwordHistory == null || passwordHistory.isEmpty()) {
			return false;
		}
		int startIndex = Math.max(0, passwordHistory.size() - passwordHistoryLength);
		for (int i = startIndex; i < passwordHistory.size(); i++) {
			if (password.equals(passwordHistory.get(i))) {
				return true;
			}
		}
		return false;
	}

	private boolean isEmailValid(String email) {
		String emailRegex = "([a-zA-Z0-9_+&.-]+)@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	private static List<String> loadDictionaryWords() {
		List<String> dictionaryWords = new ArrayList<>();

		// Load the word list from a file
		try (Scanner scanner = new Scanner(new File("C:\\javaenv2023b\\" + "dictionary.txt"))) {
			while (scanner.hasNextLine()) {
				String word = scanner.nextLine().trim();
				dictionaryWords.add(word);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return dictionaryWords;
	}
}
