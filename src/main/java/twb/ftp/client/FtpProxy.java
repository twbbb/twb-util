package twb.ftp.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class FtpProxy implements InvocationHandler {

	private IFtpClient target = null;

	FtpProxy(IFtpClient target) {
		super();
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Logger logger = target.getLog();
		try {
			target.connect();
			if(logger!=null){
				logger.info(target.getClass().getSimpleName()+"."+method.getName()+" begin,"+Arrays.toString(args));
			}
			Object result = method.invoke(target, args);
			if(logger!=null){
				logger.info(target.getClass().getSimpleName()+"."+method.getName()+" end,"+Arrays.toString(args)+result);
			}
			return result;
		} catch (Throwable e) {
			if(logger!=null){
				logger.error(target.getClass().getSimpleName()+"."+method.getName()+" 异常,"+Arrays.toString(args),e);
			}
			throw e;
		} finally {
			target.disconnect();
		}
	}
	
	

}