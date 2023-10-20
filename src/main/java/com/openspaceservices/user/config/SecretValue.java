package com.openspaceservices.user.config;

public class SecretValue {
	private String username;
	private String password;
	private String admin_username;
	private String admin_password;
	private String multithreading_rds_username;
	private String multithreading_rds_password;
	
	public SecretValue() {
		
	}

	public SecretValue(String username, String password, String admin_username, String admin_password,
			String multithreading_rds_username, String multithreading_rds_password) {
		super();
		this.username = username;
		this.password = password;
		this.admin_username = admin_username;
		this.admin_password = admin_password;
		this.multithreading_rds_username = multithreading_rds_username;
		this.multithreading_rds_password = multithreading_rds_password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdmin_username() {
		return admin_username;
	}

	public void setAdmin_username(String admin_username) {
		this.admin_username = admin_username;
	}

	public String getAdmin_password() {
		return admin_password;
	}

	public void setAdmin_password(String admin_password) {
		this.admin_password = admin_password;
	}

	public String getMultithreading_rds_username() {
		return multithreading_rds_username;
	}

	public void setMultithreading_rds_username(String multithreading_rds_username) {
		this.multithreading_rds_username = multithreading_rds_username;
	}

	public String getMultithreading_rds_password() {
		return multithreading_rds_password;
	}

	public void setMultithreading_rds_password(String multithreading_rds_password) {
		this.multithreading_rds_password = multithreading_rds_password;
	}

	@Override
	public String toString() {
		return "SecretValue [username=" + username + ", password=" + password + ", admin_username=" + admin_username
				+ ", admin_password=" + admin_password + ", multithreading_rds_username=" + multithreading_rds_username
				+ ", multithreading_rds_password=" + multithreading_rds_password + "]";
	}
	
	
}
