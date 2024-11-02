package netSec.utils;

public class PasswordConfiguration {

	private int minimalPasswordLength = 10;
	private boolean upperCaseLetters = true;
	private boolean lowerCaseLetters = true;
	private boolean numerals = true;
	private boolean specialCharacters = true;
	private int passwordHistoryCheck = 3;
	private String blackListChars = "";
	private int loginAttemptsAllowed = 3;

	public int getMinimalPasswordLength() {
		return minimalPasswordLength;
	}

	public void setMinimalPasswordLength(int minimalPasswordLength) {
		this.minimalPasswordLength = minimalPasswordLength;
	}

	public boolean isUpperCaseLetters() {
		return upperCaseLetters;
	}

	public void setUpperCaseLetters(boolean upperCaseLetters) {
		this.upperCaseLetters = upperCaseLetters;
	}

	public boolean isLowerCaseLetters() {
		return lowerCaseLetters;
	}

	public void setLowerCaseLetters(boolean lowerCaseLetters) {
		this.lowerCaseLetters = lowerCaseLetters;
	}

	public boolean isNumerals() {
		return numerals;
	}

	public void setNumerals(boolean numerals) {
		this.numerals = numerals;
	}

	public boolean isSpecialCharacters() {
		return specialCharacters;
	}

	public void setSpecialCharacters(boolean specialCharacters) {
		this.specialCharacters = specialCharacters;
	}

	public int getPasswordHistoryCheck() {
		return passwordHistoryCheck;
	}

	public void setPasswordHistoryCheck(int passwordHistoryCheck) {
		this.passwordHistoryCheck = passwordHistoryCheck;
	}

	public String getBlackListChars() {
		return blackListChars;
	}

	public void setBlackListChars(String blackListChars) {
		this.blackListChars = blackListChars;
	}

	public int getLoginAttemptsAllowed() {
		return loginAttemptsAllowed;
	}

	public void setLoginAttemptsAllowed(int loginAttemptsAllowed) {
		this.loginAttemptsAllowed = loginAttemptsAllowed;
	}

}
