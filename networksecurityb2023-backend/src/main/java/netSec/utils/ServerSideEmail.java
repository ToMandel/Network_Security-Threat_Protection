package netSec.utils;

import java.util.Properties;

public class ServerSideEmail {

	// Sender's email and password
	private String senderEmail;
	private String senderPassword;

	// Email subject and content
	private String emailSubject;
	private int code = 0;
	private String emailContent = "Hello!\n Your 6 digit code to recover your password in Communications LTD is  "
			+ code + "\nEnter it to reset your password";

	// Set up mail server properties
	private Properties properties = new Properties();

	public ServerSideEmail() {
		super();
		initProperties();
	}

	public ServerSideEmail(String senderEmail, String senderPassword, String emailSubject, int code,
			String emailContent, Properties properties) {
		super();
		this.senderEmail = senderEmail;
		this.senderPassword = senderPassword;
		this.emailSubject = emailSubject;
		this.code = code;
		this.emailContent = emailContent;
		this.properties = properties;
		initProperties();
	}

	private void initProperties() {
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderPassword() {
		return senderPassword;
	}

	public void setSenderPassword(String senderPassword) {
		this.senderPassword = senderPassword;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
