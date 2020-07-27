package twb.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	/*
	 * 获取项目路径
	 */
	public static String getProjectDir() {
		return System.getProperty("user.dir") ;
	}

	/*
	 * 获取项目路径下文件
	 */
	public static File getProjectFile(String fileName) {
		return new File(getProjectDir() +"\\"+ fileName);
	}

	/*
	 * 获取当前class路径
	 */
	public static String getClassResource(Class cla) {
		return cla.getResource("/").getPath();
	}
	


	/*
	 * 获取当前class下文件
	 */
	public static File getClassResFile(Class cla, String name) throws URISyntaxException {
		return new File(cla.getResource(name).toURI());
	}

	public static List<String> getFileData(File file,IFileLineFilter fileLineFilter) throws Exception {
		List<String> list;
		BufferedReader br=null;
		try {
			list = new ArrayList();
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				if(fileLineFilter==null){
					list.add(line);
				}else{
					if(fileLineFilter.check(line,list,file)){
						list.add(line);
					}
				}
				line = br.readLine();
				
			}
		} finally{
			if(br!=null){
				br.close();
			}
			
		}
		
		return list;
	}
	public static String getFileAllData(File file) throws Exception {
//		ByteArrayOutputStream bos=null;
//		InputStream fileInputStream=null;
//		try {
//			bos = new ByteArrayOutputStream();
//			fileInputStream = new BufferedInputStream(new FileInputStream(file));   
//			byte[] buf = new byte[1024];  
//			int length = 0;
//			while((length = fileInputStream.read(buf)) != -1){   
//				bos.write(buf, 0, length);
//			}
//		} finally{
//			if(bos!=null){
//				bos.close();
//			}
//			if(fileInputStream!=null){
//				fileInputStream.close();
//			}
//		}
//		
//		return bos.toString();
		
		 StringBuffer strBuf = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        int tempchar;
        while ((tempchar = bufferedReader.read()) != -1) {
            strBuf.append((char) tempchar);
        }
        bufferedReader.close();
        return strBuf.toString();
	}
	public static String getFileAllDataProDir(String fileName) throws Exception {

		return getFileAllData(getProjectFile(fileName));
	}

	public static List<String> getFileDataProDir(String fileName) throws Exception {

		return getFileData(getProjectFile(fileName),null);
	}

	public static List<String> getFileData( Class cla,String fileName) throws Exception {
		return getFileData(getClassResFile(cla, fileName),null);
	}
	
	public static List<String> getFileDataProDir(String fileName,IFileLineFilter fileLineFilter) throws Exception {

		return getFileData(getProjectFile(fileName),fileLineFilter);
	}

	public static List<String> getFileData( Class cla,String fileName,IFileLineFilter fileLineFilter) throws Exception {
		return getFileData(getClassResFile(cla, fileName),fileLineFilter);
	}
	
	
	
}
