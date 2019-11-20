package twb.ftp.client.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

import twb.ftp.client.FtpClientAbstract;

public class FtpClient extends FtpClientAbstract {
	private FTPClient ftpConn = null;

	@Override
	public void upLoadFile(String localPath, String remotePath) {
		try {

			ftpConn.configure(new FTPClientConfig());
			File file = new File(localPath);
			FileInputStream fis = new FileInputStream(file);
			ftpConn.storeFile(remotePath, fis);
			if (ftpConn.getReplyCode() != 226) {
				throw new RuntimeException("FTP上传失败,失败原因:" + localPath + ftpConn.getReplyString());
			}
			fis.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FTP上传FileNotFound异常,异常原因:" + localPath + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("FTP上传IO异常,异常原因:" + localPath + e.getMessage());
		}
	}

	@Override
	public void downLoadFile(String localPath, String remotePath) {

		try {
			ftpConn.configure(new FTPClientConfig());
			FileOutputStream fos = null;
			try {
				File file = new File(localPath);
				fos = new FileOutputStream(file);
				boolean rFalg = ftpConn.retrieveFile(remotePath, fos);
				fos.close();
				if (ftpConn.getReplyCode() != 226) {
					if (file.length() == 0) {
						try {
							file.delete();
						} catch (Exception e) {
						}
					}
					log.info(localPath + "下载失败,失败原因:" + ftpConn.getReplyString());
					throw new RuntimeException(localPath + "下载失败,失败原因:" + ftpConn.getReplyString());
				}

				log.info("------file.length()----------" + file.length());

			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (Exception e) {
						log.error(e);
					}
				}
				this.disconnect();
			}

		} catch (FileNotFoundException e) {
			throw new RuntimeException("FTP下载FileNotFound异常,异常原因:" + localPath + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("FTP下载IO异常,异常原因:" + localPath + e.getMessage());
		}

	}

	@Override
	public void reName(String from, String to) {
		try {
			ftpConn.rename(from, to);
		} catch (IOException e) {
			throw new RuntimeException("修改FTP服务器文件名IO异常,请检查网络:" + e.getMessage());
		}
	}

	@Override
	public void deleteFile(String ftpPath) {

		try {
			ftpConn.deleteFile(ftpPath);
			if (ftpConn.getReplyCode() != 250) {
				throw new RuntimeException("删除Ftp文件出错,错误原因:" + ftpConn.getReplyString());
			}
		} catch (IOException e) {
			throw new RuntimeException("删除FTP服务器文件IO异常,请检查网络:" + e.getMessage());
		}
	}

	@Override
	public void mkdir(String ftpPath) {
		if (StringUtils.isNotBlank(ftpPath)) {
			ftpPath.replace("//", "/");
			ftpPath.replace("\\", "/");
			try {
				ftpConn.makeDirectory(ftpPath);
			} catch (Exception e) {
				throw new RuntimeException("SFtp创建文件夹失败,失败原因:" + e.getMessage());
			}

		}
	}

	@Override
	public List<String> getFileNames(String ftpPath) {
		List<String> fileNames = new ArrayList<String>();
		try {
			FTPFile[] files = ftpConn.listFiles(ftpPath);
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i] != null) {
						if (files[i].getType() == 0) {
							fileNames.add(files[i].getName());
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FTP下载FileNotFound异常,异常原因:" + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("FTP下载IO异常,异常原因:" + e.getMessage());
		}

		return fileNames;
	}

	@Override
	public boolean isFileExisted(String filePath) {
		try {
			FTPFile[] files = ftpConn.listFiles(filePath);
			if (files != null && files.length != 0) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void connect() {
		if (ftpConn == null) {
			ftpConn = new FTPClient();
		}
		if (!ftpConn.isConnected()) {
			try {
				ftpConn.connect(ftpConfig.getIp(), Integer.parseInt(ftpConfig.getPort()));
				ftpConn.setDefaultTimeout(10 * 60 * 1000);
				if (ftpConn.getReplyCode() != 220) {
					throw new RuntimeException("FTP无法连接,请检查网络:" + ftpConn.getReplyString());
				}
				ftpConn.login(ftpConfig.getUserName(), ftpConfig.getPassword());

				if (ftpConn.getReplyCode() != 230) {
					throw new RuntimeException("FTP登录失败,请核实帐号密码:" + ftpConn.getReplyString());
				}
				ftpConn.setFileType(FTP.BINARY_FILE_TYPE);

			} catch (SocketException e) {
				throw new RuntimeException("FTP服务器Socket异常,请检查网络:" + e.getMessage());
			} catch (IOException e) {
				throw new RuntimeException("FTP服务器IO异常,请检查网络:" + e.getMessage());
			}

			if ("Y".equals(ftpConfig.getPassiveOn())) {
				ftpConn.enterLocalPassiveMode();
			}
		}
	}

	@Override
	public void disconnect() {
		try {
			if (ftpConn != null && ftpConn.isConnected()) {
				ftpConn.disconnect();
			}
		} catch (IOException e) {
			throw new RuntimeException("断开FTP服务器连接异常:" + e.getMessage());
		}
	}

}
