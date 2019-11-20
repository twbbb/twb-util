package twb.utils.file.imp;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import twb.utils.file.IFileLineFilter;



public class FileLineEmptyFilter implements IFileLineFilter {


	@Override
	public boolean check(String str,List list,File file) {
		if(StringUtils.isEmpty(str)){
			return false;
		}
		return true;
	}


}
