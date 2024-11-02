package netSec.boundaries;

public class EmailMessage {
	private int oneTimePassword;
	private String senderEmail;
	private String senderPassword;
	private UserBoundary recipientEmail;
	private String emailSubject;
	private String emailContent;

	public EmailMessage() {

	}

	public EmailMessage(int oneTimePassword, String senderEmail, String senderPassword, UserBoundary recipientEmail,
			String emailSubject, String emailContent) {
		this.oneTimePassword = oneTimePassword;
		this.senderEmail = senderEmail;
		this.senderPassword = senderPassword;
		this.recipientEmail = recipientEmail;
		this.emailSubject = emailSubject;
		this.emailContent = emailContent;

	}

	public int getOneTimePassword() {
		return oneTimePassword;
	}

	public void setOneTimePassword(int oneTimePassword) {
		this.oneTimePassword = oneTimePassword;
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

	public UserBoundary getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(UserBoundary recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

}
