package twb.ftp.client;

import java.util.List;

import org.apache.log4j.Logger;

import twb.ftp.bean.FtpClientConfig;

public interface IFtpClient {
	void init(FtpClientConfig ftpConfig,Logger logger);
	
	public void upLoadFile(String localPath, String remotePath) ;
	
	
	public void upLoadFile(String localPath, String remotePath,String fileName) ;

	public void upLoadFile(String localPath, String remotePath,String localFileName,String remoteFileName) ;


	public void downLoadFile(String localPath, String remotePath) ;

	public boolean downLoadFileIfExisted(String localPath, String remotePath) ;

	public boolean downLoadFileIfExisted(String localDirPath, String remoteDirPath,String fileName) ;
	
	public boolean downLoadFileIfExisted(String localDirPath, String remoteDirPath,String localFileName,String remoteFileName) ;
	

	public void reName(String from, String to);

	public void deleteFile(String remotePath);

	public void mkdir(String remotePath);

	public List<String> getFileNames(String remotePath);//获取目录下所有文件的文件名

	public boolean isFileExisted(String remotePath);

	public void connect();

	public void disconnect() ;
	public Logger getLog();

}
