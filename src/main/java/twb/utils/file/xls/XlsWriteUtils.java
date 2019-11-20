package twb.utils.file.xls;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twb.utils.string.StringConvertUtils;

public class XlsWriteUtils {

	public static void writeMapXls(List<Map> list, String filePath) throws Exception {
		List xlsList = new ArrayList();
		Map titleMap = new HashMap();
		String[] keyStrs = null;

		for (int i = 0; i < list.size(); i++) {
			int j = 0;

			Map dataMap = list.get(i);


			if (i == 0) {
				keyStrs = (String[]) dataMap.keySet().toArray(new String[dataMap.keySet().size()]);
				
				Map headMap = new HashMap();
				int a = 0;
				for (String field : keyStrs) {
					// 获取属性

					headMap.put(a++, field);

				}
				xlsList.add(headMap);
			}

			Map paramMap = new HashMap();
			for (String key : keyStrs) {
				// 获取属性值
				String value = StringConvertUtils.toString(dataMap.get(key));
				paramMap.put(j++, value);
			}

			xlsList.add(paramMap);
		}



		XLSUtils.testWrite(filePath, xlsList);
		System.out.println("生成xls文件:"+filePath);

	}

	public static void writeBeanXls(List<Object> list, String dir, String fileName) throws Exception {

		List xlsList = new ArrayList();
		Map titleMap = new HashMap();
		Field[] fields = null;

		for (int i = 0; i < list.size(); i++) {
			int j = 0;

			Object apiRegisterInfoBean = list.get(i);


			if (i == 0) {
				fields = apiRegisterInfoBean.getClass().getDeclaredFields();
				
				Map headMap = new HashMap();
				int a = 0;
				for (Field field : fields) {
					// 获取属性
					String name = field.getName();

					headMap.put(a++, name);

				}
				xlsList.add(headMap);
			}

			Map paramMap = new HashMap();
			for (Field field : fields) {
				field.setAccessible(true);
				// 获取属性值
				String value = (String) field.get(apiRegisterInfoBean);
				paramMap.put(j++, value);
			}

			xlsList.add(paramMap);
		}


		File file = new File(dir);
		if (!file.exists()) {
			file.mkdir();
		}

		XLSUtils.testWrite(dir + "\\" + fileName + ".xlsx", xlsList);

	}

}
