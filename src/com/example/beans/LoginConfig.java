package com.example.beans;
/**
 * �û���¼��Ϣ
 * �û���
 * �û�id
 * ������ip
 */
public class LoginConfig {
	String username = null;
	String userid=null;
	String serverip=null;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getServerip() {
		return serverip;
	}

	public void setServerip(String serverip) {
		this.serverip = serverip;
	}

	private LoginConfig() {
	}

	private static LoginConfig lcf = null;

	public static LoginConfig getLoginConfig() {
		if (lcf == null) {
			lcf = new LoginConfig();
		}
		return lcf;
	}

	public void printInfo() {
	}
}
