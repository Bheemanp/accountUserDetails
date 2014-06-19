package user.account.classes;

public class AccountDetails {

	private String token;
	private String emailId;
	
	public AccountDetails(String token, String emailId) {
		super();
		this.token = token;
		this.emailId = emailId;
	}
	
	public AccountDetails(String emailId) {
		super();
		this.emailId = emailId;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	
}
