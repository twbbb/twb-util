package twb.ftp.bean;

import java.io.File;

public class FtpClientConfig {

	private String userName;
	private String password;
	private String ip;
	private String port;
	private String passiveOn;
	private String ftpType;
	private String fileSeparator=File.separator;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getPassiveOn() {
		return passiveOn;
	}
	public void setPassiveOn(String passiveOn) {
		this.passiveOn = passiveOn;
	}
	public String getFtpType() {
		return ftpType;
	}
	public void setFtpType(String ftpType) {
		this.ftpType = ftpType;
	}
	public String getFileSeparator() {
		return fileSeparator;
	}
	public void setFileSeparator(String fileSeparator) {
		this.fileSeparator = fileSeparator;
	}
	
	
	
	
	
}
