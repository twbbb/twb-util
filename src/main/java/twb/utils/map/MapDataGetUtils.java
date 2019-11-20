package twb.utils.map;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

//map数据获取
public class MapDataGetUtils {
	
	public static String getStringVal(Map map,String key) {
		if(map==null||map.isEmpty()){
			return "";
		}
		Object t = map.get(key);
		if (t == null)
			return "";
		if (t instanceof BigDecimal) {
			return t.toString();
		} else if (t instanceof Timestamp) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			return df.format(t);
		} else {
			return t.toString().trim();
		}
	}
	
	/**
	 * 根据路径获取对应位置的对象
	 * @param paramMap
	 * @param path
	 * @return
	 */
	public static Object selectPathObj(Map paramMap,String path){
		if(paramMap==null){
			return null;
		}
		String[] pathNodes = path.split("/");
		Object resultObject = paramMap.get(pathNodes[0]) ;
		
		for(int i=1;i<pathNodes.length;i++){
			
			 if(resultObject==null || resultObject instanceof String ){
				 return null;
			 }
			 if(resultObject instanceof List){
				 List tempResultList = (List)resultObject;
				 if(tempResultList.size()==0){
					 return null;
				 }
				 resultObject=null;
				 for(Object tempItem : tempResultList ){
					 if(tempItem instanceof Map ){
						 resultObject = tempItem;
						 break;
					 }
				 }
			 }
			 if(resultObject==null){
				 return null;
			 }
			 resultObject = ((Map)resultObject).get(pathNodes[i]);  
		} 
		
		return resultObject;
	}
	
}
