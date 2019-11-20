package twb.utils.db.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import twb.utils.string.StringConvertUtils;

public class OracleJdbc {
	/**
	 * Created by 10412 on 2016/12/27. JDBC的六大步骤 JAVA连接Oracle的三种方式
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String sql = "";
		String url = "jdbc:oracle:thin:@134.176.123.25:XXXX/bssupdb";
		String user = "";
		String password = "";

		queryDB(sql, url, user, password);
	}

	public static List<Map<String, String>> queryDB(String sql, String url, String user, String password)
			throws Exception {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			// 第一步：注册驱动
			Class.forName("oracle.jdbc.OracleDriver");

			// 第二步：获取连接
			// 第一种方式：利用DriverManager（常用）
			connect = DriverManager.getConnection(url, user, password);

			// 测试connect正确与否
			// System.out.println(connect);
			statement = connect.createStatement();
			// 第四步：执行sql语句
			resultSet = statement.executeQuery(sql);
			statement.setFetchSize(100);
			statement.setMaxRows(100000);
			// 第二种方式：PreStatement
			// PreparedStatement preState = connect.prepareStatement("select *
			// from tb1_dept where id = ?");
			// preState.setInt(1, 2);//1是指sql语句中第一个？, 2是指第一个？的values值
			// resultSet = preState.executeQuery();
			// boolean execute = preState.execute();
			// 查询任何语句，如果有结果集，返回true，没有的话返回false,注意如果是插入一条数据的话，虽然是没有结果集，返回false，但是却能成功的插入一条数据
			// System.out.println(execute);

			// 第五步：处理结果集
			return getResultMap(resultSet);
			// while (resultSet.next()) {
			// int id = resultSet.getInt("id");
			// String name = resultSet.getString("name");
			// String city = resultSet.getString("city");
			// System.out.println(id + " " + name + " " + city); // 打印输出结果集
			// }
		} finally {
			// 第六步：关闭资源
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<Map<String, String>> insertDB(String sql,List param, String url, String user, String password) throws Exception {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			// 第一步：注册驱动
			Class.forName("oracle.jdbc.OracleDriver");

			// 第二步：获取连接
			// 第一种方式：利用DriverManager（常用）
			connect = DriverManager.getConnection(url, user,password);

			// 测试connect正确与否
//			System.out.println(connect);
			// 第四步：执行sql语句
			// 第二种方式：PreStatement
			 PreparedStatement preState = connect.prepareStatement(sql);
			 if(param!=null){
				 for(int i=0;i<param.size();i++){
					 preState.setObject(i+1, param.get(i));//1是指sql语句中第一个？, 2是指第一个？的values值
				 }
			 }

			 boolean execute = preState.execute();
			// 查询任何语句，如果有结果集，返回true，没有的话返回false,注意如果是插入一条数据的话，虽然是没有结果集，返回false，但是却能成功的插入一条数据
			 System.out.println(execute);

			// 第五步：处理结果集
			return  getResultMap(resultSet) ;
//			while (resultSet.next()) {
//				int id = resultSet.getInt("id");
//				String name = resultSet.getString("name");
//				String city = resultSet.getString("city");
//				System.out.println(id + "   " + name + "   " + city); // 打印输出结果集
//			}
		}  finally {
			// 第六步：关闭资源
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<Map<String, String>> getResultMap(ResultSet rs) throws SQLException {
		List list = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();

		while (rs.next()) {
			Map<String, String> hm = new CaseInsensitiveMap<String, String>();
			for (int i = 1; i <= count; i++) {
				String key = rsmd.getColumnLabel(i);
				String value = StringConvertUtils.toString(rs.getObject(i));
				hm.put(key, value);

			}
			list.add(hm);

		}

		// System.out.println(list);
		return list;
	}
}
