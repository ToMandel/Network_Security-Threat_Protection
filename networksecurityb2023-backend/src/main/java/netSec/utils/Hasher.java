package netSec.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.Random;

// Java program to calculate SHA hash value
public class Hasher {

	private static String hashAlgorithmSHA256 = "SHA-256";
	private static String hashAlgorithmSHA1 = "SHA-1";
//	private static final Random RANDOM = new SecureRandom();
//	private static final int ITERATIONS = 10000;
//	private static final int KEY_LENGTH = 256;
	private static final int SALT_LENGTH = 16; // Length of the salt in bytes

	public Hasher() {
	}

//	public String hashString(String input, String salt, String hashAlgorithm) {
//		try {
//			if (hashAlgorithm.equals(this.hashAlgorithmSHA256)) {
//				return toHexString(getSHA(input, hashAlgorithm));
//			} else if (hashAlgorithm.equals(this.hashAlgorithmSHA1)) {
//				return toHexString(getSHA(input, hashAlgorithm));
//			}
//		} catch (NoSuchAlgorithmException e) {
//			System.out.println("Exception thrown for incorrect algorithm: " + e);
//		}
//		return null;
//	}

	public static int get6Digits() {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(hashAlgorithmSHA1);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (md == null) {
			throw new RuntimeException();
		}
		long seed = new Random().nextLong();
		String seedStr = Long.toString(seed);
		md.update(seedStr.getBytes());
		byte[] digest = md.digest();

		// Convert the first 4 bytes of the digest to an integer
		double randInt = ((digest[0] & 0xff) << 24) | ((digest[1] & 0xff) << 16) | ((digest[2] & 0xff) << 8)
				| (digest[3] & 0xff);

		// Ensure the random number is positive and has 6 digits
		randInt = Math.abs(randInt) % 1000000;
		while (randInt < 100000)
			randInt *= randInt % 10;
		return (int) randInt;
	}


	public static String hashPassword(String password, byte[] salt) {
		// Generate a random salt
//		byte[] salt = generateSalt();

		// Combine the password and salt
		byte[] saltedPassword = concatenateByteArrays(password.getBytes(), salt);

		try {
			// Compute the SHA-256 hash
			MessageDigest md = MessageDigest.getInstance(hashAlgorithmSHA256);
			byte[] hashedBytes = md.digest(saltedPassword);

			// Convert the hashed bytes to a hexadecimal string
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean verifyPassword(String password, String hashedPassword, byte[] salt) {
		// Convert the hashed password string to bytes
		byte[] hashedBytes = hexStringToByteArray(hashedPassword);

		// Extract the salt from the hashed password bytes
//		byte[] salt = new byte[SALT_LENGTH];
//		System.arraycopy(hashedBytes, 0, salt, 0, SALT_LENGTH);

		// Combine the provided password and extracted salt
		byte[] saltedPassword = concatenateByteArrays(password.getBytes(), salt);

		try {
			// Compute the SHA-256 hash
			MessageDigest md = MessageDigest.getInstance(hashAlgorithmSHA256);
			byte[] computedHash = md.digest(saltedPassword);

			// Compare the computed hash with the provided hashed password bytes
			return MessageDigest.isEqual(hashedBytes, computedHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static byte[] generateSalt() {
		byte[] salt = new byte[SALT_LENGTH];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		return salt;
	}

	private static byte[] concatenateByteArrays(byte[] a, byte[] b) {
		byte[] result = new byte[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

}
