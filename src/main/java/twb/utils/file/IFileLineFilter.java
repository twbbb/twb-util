package twb.utils.file;

import java.io.File;
import java.util.List;

public interface IFileLineFilter {

	boolean check(String line,List list,File file);
}
