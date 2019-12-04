package nhapmon.nhom15.sbshoppingcart.model;

import nhapmon.nhom15.sbshoppingcart.entity.Account;

public class AccountInfo {
	private String userName;
	private String encrytedPassword;
	private boolean active;
	private String userRole;

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

	public AccountInfo(String userName, String encrytedPassword, boolean active, String userRole) {
		this.userName = userName;
		this.encrytedPassword = encrytedPassword;
		this.active = active;
		this.userRole = userRole;
	}
	
	

	public AccountInfo(String userName, boolean active, String userRole) {
		super();
		this.userName = userName;
		this.active = active;
		this.userRole = userRole;
	}

	public AccountInfo(Account account) {
		this.active = account.isActive();
		this.encrytedPassword = account.getEncrytedPassword();
		this.userName = account.getUserName();
		this.userRole = account.getUserRole();
	}
}
