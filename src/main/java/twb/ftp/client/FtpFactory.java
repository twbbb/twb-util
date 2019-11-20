package twb.ftp.client;

import java.lang.reflect.Proxy;
import java.util.Map;

import org.apache.log4j.Logger;

import twb.ftp.bean.FtpClientConfig;
import twb.ftp.client.imp.FtpClient;
import twb.ftp.client.imp.SFtpClient;
import twb.utils.string.StringConvertUtils;

public class FtpFactory {
	
	public static IFtpClient getFtpClient(FtpClientConfig ftpConfig, Logger logger) {
		
		IFtpClient ftpClient = new FtpClient();
		if ("1".equals(ftpConfig.getFtpType())) {
			ftpClient = new SFtpClient();
		}
		ftpClient.init(ftpConfig, logger);
		
		IFtpClient ftpClientProxy = (IFtpClient) Proxy.newProxyInstance(ftpClient.getClass().getClassLoader(), new Class[]{ IFtpClient.class},
				new FtpProxy(ftpClient));
		return ftpClientProxy;
	}
	
	public static IFtpClient getFtpClient(Map ftpConfigMap, Logger logger) {
		
		FtpClientConfig ftpClientConfig = new FtpClientConfig();
		ftpClientConfig.setIp(StringConvertUtils.toString(ftpConfigMap.get("FTP_DEST_HOST")));
		ftpClientConfig.setPort(StringConvertUtils.toString(ftpConfigMap.get("FTP_DEST_PORT")));
		ftpClientConfig.setUserName(StringConvertUtils.toString(ftpConfigMap.get("FTP_DEST_UID")));
		ftpClientConfig.setPassword(StringConvertUtils.toString(ftpConfigMap.get("FTP_DEST_PASSWORD")));
		ftpClientConfig.setPassiveOn(StringConvertUtils.toString(ftpConfigMap.get("PASSIVE_ON")));
		ftpClientConfig.setFtpType(StringConvertUtils.toString(ftpConfigMap.get("FTP_TYPE")));
		
		return getFtpClient(ftpClientConfig, logger);
	}
	
	
}
