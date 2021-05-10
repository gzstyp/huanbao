package com.fwtai.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作工具类
 * @功能 本工具类封装直接对数据库的添加、删除、更新、查询；<br />
 * 即增删改放在一个方法里；查询放在一个方法里
 * @作者 田应平
 * @创建时间 2013年12月21日14:52:08
*/
public final class ToolDao{

    private final String URL = "jdbc:mysql://192.168.3.66:3306/db1?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";

    private Connection connection;

    //使用默认的数据库连接
    public ToolDao(){}

    //指定数据库连接
    public ToolDao(final Connection connection){
        this.connection = connection;
    }

    /**
	 * 获取数据库连接
	*/
	private Connection getConn(){
	    if(connection == null){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(URL,"root","rootFwtai");
            } catch (Exception e){
                System.out.println("获取连接数据库失败");
                e.printStackTrace();
            }
        }
		return connection;
	}

	/**
	 * 本方法对数据库的增加、删除、修改的直接操作
	 * @param sql 本sql参数是操作数据库的[含有占位符?]sql语句
	 * @param list params参数是List集合类型<br/>
	 * 它是使用占位符拼写合拼起来的sql语句，用List参数作为占位符，如没有占位符参数则写null
	 * @return 返回值类型 boolean
	 * @throws SQLException 数据库操作异常
	 * @作者 田应平
	 * @创建时间 2013年12月21日15:03:03
	*/
	public boolean executeSql(final String sql,final List<Object> list){
        final Connection conn = getConn();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            int index = 1 ;//表示List参数占位符的第一个位置
            //在for循环之前得判断List参数占位符，所以for的外面加了if语句判断,它是集合里的判断的标准方式
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    //获取List参数listParams里每一个占位符参数，记得占位符是第1个位置开始的哦,而index++表示叠加占位符参数;
                    pstmt.setObject(index++,list.get(i));
                }
            }
            return pstmt.executeUpdate() > 0;//执行sql语句对数据库表的影响行数并返回给result储存
	    } catch (final Exception e) {
	        e.printStackTrace();
	        closeFinally(conn,pstmt,null);
	        return false;
	    }
	}
	
	/**
	 * 查询返回多条记录,当然也可以把‘查询返回多条记录’转成json对象格式,方便返回到页面
	 * @param sql 本sql参数是操作数据库的[含有占位符?]sql语句
	 * @param list 参数listParams为List集合对象，而不是实体类对象；
	 * @return 调用该方法后返回值的类型为 List<Map<String, Object>>；List<Map>对象
	 * @创建时间 2013年12月21日22:13:13
	 * @throws Exception
	*/
	public List<Map<String,Object>> queryList(final String sql,final List<Object> list){
        final Connection conn = getConn();
	    PreparedStatement pstmt = null;
        ResultSet rs = null;
	    try {
            final List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
            pstmt = conn.prepareStatement(sql);
            int index = 1 ;//表示List参数占位符的第一个位置
            //在for循环之前得判断List参数占位符，所以for的外面加了if语句判断,它是集合里的判断的标准方式
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    //获取List参数listParams里每一个占位符参数，记得占位符是第1个位置开始的哦,而index++表示叠加占位符参数;
                    pstmt.setObject(index++, list.get(i));
                }
            }
            rs = pstmt.executeQuery();//执行sql语句后便返回结果[集];
            /*
             * 接口 ResultSetMetaData，用于获取关于 ResultSet 对象中列的类型和属性信息的对象。
             * 其方法 int getColumnCount()返回此一条记录中的列的个数。即，该表所有的字段个数。
             * 其方法 String getColumnName(int column[索引位置])获取指定列的名称。
             */
            final ResultSetMetaData metaData = rs.getMetaData();
            // 通过metaData.getColumnCount()表的列的长度个数;
            final int clos_len = metaData.getColumnCount();
            while (rs.next()) {
                //如果rs.next()为真或者说还有下一条数据记录，则new创建Map<String, Object> map 来存储进行for循环存储添加下一条数据记录
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < clos_len; i++) {
                    //获取每列每字段的名字,下行的i+1是因为起先定义int i = 0;
                    //又因为数据库的索引是从下标1开始的，所以i+1 = 1。
                    String clos_name = metaData.getColumnName(i+1);//起先定义int i = 0;因为数据库的索引是从下标1开始的，所以i+1 = 1。因为每循环一次，就自动增加1
                    //定义获取每列的值
                    Object clos_value = rs.getObject(clos_name);
                    if (clos_value == null) {
                        //即当数据库表的某一个为非空约束时，即表示该字段的值为空时，则赋值为""；
                        clos_value = "" ;
                    }
                    map.put(clos_name, clos_value);
                }
                //别忘了在for循环的外面把map值添加到list里面
                listMap.add(map);//循环一次就添加一次map对象
            }
            return listMap;
	    } catch (final Exception e) {
            e.printStackTrace();
	        closeFinally(conn,pstmt,rs);
	        return null;
	    }
	}

    /**
     * 查询返回ResultSet单1条记录；当返回单一条记录时用Map作为返回值[当且仅当返回1记录时可用]
     * @param sql 本sql参数是操作数据库的[含有占位符?]sql语句
     * @param list 参数listParams为List集合对象，而不是实体类对象；
     * @return 调用该方法后返回值的类型为 Map<String, Object>；Map对象
     * @创建时间 2013年12月21日22:13:13
     * @throws SQLException
    */
	public HashMap<String,Object> queryMap(final String sql,final List<Object> list){
        final Connection conn = getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //定义了sql语句执行的对象;
            //定义了执行sql语句后返回了结果[集];
            int index = 1 ;//表示List参数占位符的第一个位置
            pstmt = conn.prepareStatement(sql);
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    //获取List参数listParams里每一个占位符参数，记得占位符是第1个位置开始的哦,而index++表示叠加占位符参数;
                    pstmt.setObject(index++, list.get(i));
                }
            }
            rs = pstmt.executeQuery();//执行sql语句后便返回结果[集];
            /*
             * 接口 ResultSetMetaData，用于获取关于 ResultSet 对象中列的类型和属性信息的对象。
             * 其方法 int getColumnCount()返回此一条记录中的列的个数。即，该表所有的字段个数。
             * 其方法 String getColumnName(int column[索引位置])获取指定列的名称。
             */
            final ResultSetMetaData metaData = rs.getMetaData();
            // 通过metaData.getColumnCount()表的列的长度个数;声明长度;
            final int clos_len = metaData.getColumnCount();
            final HashMap<String,Object> map = new HashMap<>();
            while (rs.next()) {
                //如果rs.next()为真或者说有该条数据记录，则给resultObject = cls.newInstance()赋值，
                //它是通过反射机制来创建一个实例来存储,然后进行for循环存储添加下一条数据记录
                for (int i = 0; i < clos_len; i++) {//clos_len列的长度
                    //获取每列每字段的名字,下行的i+1是因为起先定义int i = 0;
                    //又因为数据库的索引是从下标1开始的，所以i+1 = 1。
                    final String clos_name = metaData.getColumnName(i+1);//起先定义int i = 0;因为数据库的索引是从下标1开始的，所以i+1 = 1。因为每循环一次，就自动增加1
                    //定义获取每列的值
                    Object clos_value = rs.getObject(clos_name);
                    if (clos_value == null) {
                        //即当数据库表的某一个为非空约束时，即表示该字段的值为空时，则赋值为""；
                        clos_value = "";
                    }
                    map.put(clos_name,clos_value);
                }
            }
            return map;
	    } catch (final Exception e) {
	        e.printStackTrace();
	        closeFinally(conn,pstmt,rs);
	        return null;
	    }
	}
	
	/**
	 * 关闭数据库，释放内存资源,正规的关闭JDBC,较为规范
	 * @param rs
	 * @param pstmt
	 * @param conn
	 * @创建时间 2013年12月21日22:13:13
	*/
	private void closeFinally(final Connection conn,final PreparedStatement pstmt,final ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}