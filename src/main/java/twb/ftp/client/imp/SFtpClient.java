package twb.ftp.client.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import twb.ftp.client.FtpClientAbstract;

public class SFtpClient extends FtpClientAbstract {

	private ChannelSftp channelSftp = null;
	private Session session = null;

	public void upLoadFile(String localPath, String remotePath) {
		try {
			SFTPMonitor MyProgressMonitor = new SFTPMonitor(log);
			channelSftp.put(localPath, remotePath, MyProgressMonitor, ChannelSftp.OVERWRITE);
		} catch (Exception e) {
			throw new RuntimeException("SFtp上传文件失败,失败原因:" + e.getMessage());
		}
	}

	public void downLoadFile(String localPath, String remotePath) {
		try {
			SFTPMonitor MyProgressMonitor = new SFTPMonitor(log);
			channelSftp.get(remotePath, localPath, MyProgressMonitor, ChannelSftp.OVERWRITE);
		} catch (Exception e) {
			throw new RuntimeException("SFtp下载文件失败,失败原因:" + e.getMessage());
		}

	}

	public void reName(String from, String to) {
		try {
			channelSftp.rename(from, to);
		} catch (Exception e) {
			throw new RuntimeException("SFtp重命名文件失败,失败原因:" + e.getMessage());
		}
	}

	public void deleteFile(String ftpPath) {
		try {
			channelSftp.rm(ftpPath);
		} catch (Exception e) {
			throw new RuntimeException("SFtp删除文件失败,失败原因:" + e.getMessage());
		}
	}

	public void mkdir(String ftpPath) {
		if (StringUtils.isNotBlank(ftpPath)) {
			ftpPath.replace("//", "/");
			ftpPath.replace("\\", "/");
			try {
				channelSftp.mkdir(ftpPath);
			} catch (Exception e) {
				throw new RuntimeException("SFtp创建文件夹失败,失败原因:" + e.getMessage());
			}
		}
	}

	public List<String> getFileNames(String ftpPath) {
		List list = new ArrayList();
		try {
			Vector<LsEntry> vector = channelSftp.ls(ftpPath);
			for (LsEntry lsEntry : vector) {
				String fileName = lsEntry.getFilename();
				if (!fileName.equals(".") && !fileName.equals("..") && lsEntry.getAttrs().isReg()) {
					list.add(fileName);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("SFtp获取文件夹文件数据失败,失败原因:" + e.getMessage());
		}
		return list;
	}

	public boolean isFileExisted(String filePath) {
		try {
			channelSftp.ls(filePath);
			return true;
		} catch (SftpException e) {
			return false;
		}
	}

	/**
	 * @description 连接Ftp服务器
	 * @throws RuntimeException
	 */
	public void connect() {
		channelSftp = null;
		session = null;
		try {
			if (session == null || !session.isConnected()) {
				JSch jsch = new JSch();
				session = jsch.getSession(ftpConfig.getUserName(), ftpConfig.getIp(),
						Integer.parseInt(ftpConfig.getPort()));
				session.setConfig("StrictHostKeyChecking", "no");
				session.setPassword(ftpConfig.getPassword());
				session.connect();
			}

			if (channelSftp == null || !channelSftp.isConnected()) {
				channelSftp = (ChannelSftp) session.openChannel("sftp");
				channelSftp.connect();
			}

		} catch (JSchException e) {
			throw new RuntimeException("SFTP登录失败 ", e);
		}

	}

	/**
	 * @description 断开Ftp连接
	 * @throws RuntimeException
	 */
	public void disconnect() throws RuntimeException {
		try {
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
			if (channelSftp != null && channelSftp.isConnected()) {
				channelSftp.disconnect();
			}
		} catch (Exception e) {
			throw new RuntimeException("断开SFTP服务器连接异常:" + e.getMessage());
		}
	}

}
