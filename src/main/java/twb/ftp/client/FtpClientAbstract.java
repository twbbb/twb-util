package twb.ftp.client;

import org.apache.log4j.Logger;

import twb.ftp.bean.FtpClientConfig;

public abstract class FtpClientAbstract implements IFtpClient {
	protected FtpClientConfig ftpConfig;
	protected Logger log;

	@Override
	public void init(FtpClientConfig ftpConfig, Logger log) {
		this.ftpConfig = ftpConfig;
		this.log = log;
	}

	public Logger getLog() {
		return log;
	}
	public void upLoadFile(String localPath, String remotePath,String fileName) {
		this.upLoadFile(getFilePath(localPath, fileName), getFilePath(remotePath, fileName));
	}

	public void upLoadFile(String localPath, String remotePath,String localFileName,String remoteFileName) {
		this.upLoadFile(getFilePath(localPath, localFileName), getFilePath(remotePath, remoteFileName));

	}
	
	public boolean downLoadFileIfExisted(String localDirPath, String remoteDirPath) {
		if(this.isFileExisted(remoteDirPath)){
			 this.downLoadFile(localDirPath, remoteDirPath);
			 return true;
		}
		return false;
	}
	
	public boolean downLoadFileIfExisted(String localDirPath, String remoteDirPath,String fileName) {
		return this.downLoadFileIfExisted(getFilePath(localDirPath, fileName), getFilePath(remoteDirPath, fileName));
	}
	
	public boolean downLoadFileIfExisted(String localDirPath, String remoteDirPath,String localFileName,String remoteFileName) {
		return this.downLoadFileIfExisted(getFilePath(localDirPath, localFileName), getFilePath(remoteDirPath, remoteFileName));

	}
	
	

	public String getFilePath(String ftpPath, String fileName) {
		if (ftpPath == null) {
			return fileName;
		}
		if (ftpPath.endsWith(ftpConfig.getFileSeparator())) {
			return ftpPath + fileName;
		} else {
			return ftpPath + ftpConfig.getFileSeparator() + fileName;
		}
	}

}
