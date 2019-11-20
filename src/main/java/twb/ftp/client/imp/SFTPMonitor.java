package twb.ftp.client.imp;

import org.apache.log4j.Logger;

import com.jcraft.jsch.SftpProgressMonitor;

public class SFTPMonitor implements SftpProgressMonitor {
	long count = 0;
	long max = 0;
	Logger log;

	public void init(int op, String src, String dest, long max) {
		this.max = max;
		count = 0;
	}

	public boolean count(long count) {
		this.count += count;

		long percent = this.count * 100 / max;

		log.info("Completed " + this.count + "(" + percent + "%) out of " + max + ".");

		return true;
	}

	public void end() {
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public SFTPMonitor(Logger log) {
		super();
		this.log = log;
	}

	public SFTPMonitor() {
		super();
	}
	
}
