package nhapmon.nhom15.sbshoppingcart.form;

import org.springframework.web.multipart.MultipartFile;

import nhapmon.nhom15.sbshoppingcart.entity.Account;

public class AccountForm {
	private String userName;
	private String encrytedPassword;
	private boolean active;
	private String userRole;

	private boolean newAccount = false;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEncrytedPassword() {
		return encrytedPassword;
	}

	public void setEncrytedPassword(String encrytedPassword) {
		this.encrytedPassword = encrytedPassword;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public AccountForm() {
		this.newAccount = true;
	}
	
	public AccountForm(Account account) {
		this.active = account.isActive();
		this.encrytedPassword = account.getEncrytedPassword();
		this.userName = account.getUserName();
		this.userRole = account.getUserRole();
	}

	public boolean isNewAccount() {
		return newAccount;
	}

	public void setNewAccount(boolean newAccount) {
		this.newAccount = newAccount;
	}
	
}
