package com.nyh.javatpmodel.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MySqlDao {

	private String url = "jdbc:mysql://localhost:3306/pic?useSSL=false";//链接字符串
	private String name;//用户名
	private String pass;//密码
	private Connection conn;
	private PreparedStatement pstm;
	private ResultSet rs;
	
	public MySqlDao(){}
	
	/**@说明 默认连接字符串，修改用户名密码的构造
	 * @name 用户名
	 * @pass 密码*/
	public MySqlDao(String name,String pass){
		this.name=name;
		this.pass=pass;
	}
	
	/**@说明 修改连接字符串,用户名和密码的构造
	 * @url 连接字符串
	 * @name 用户名
	 * @pass 密码*/
	public MySqlDao(String url,String name,String pass){
		this.url=url;
		this.name=name;
		this.pass=pass;
	}
	
	private void connect(){//建立连接
		try{
			Class.forName("com.mysql.jdbc.Driver");
			if(conn==null||conn.isClosed()){
				conn=DriverManager.getConnection(url, name, pass);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	private void close(){//关闭操作
		try{
			if(rs!=null&&!rs.isClosed()){
				rs.close();
			}
			if(pstm!=null&&!pstm.isClosed()){
				pstm.close();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * @作者 聂宇豪
	 * @时间 2018年9月5日
	 * @说明 无参查询
	 * */
	public List<Map<String, Object>> query(String sql){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		connect();//建立连接
		try{
			pstm = conn.prepareStatement(sql);//预加载sql
			rs = pstm.executeQuery();
			ResultSetMetaData md = rs.getMetaData();// 获得结果集结构信息（元数据）
	        int columnCount = md.getColumnCount();
			while(rs.next()){
				Map<String, Object> map = new HashMap<String,Object>();
				for (int i = 1; i <= columnCount; i++) {// 遍历获取对当前行的每一列的键值对，put到map中
	                map.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
	            }
				list.add(map);
			}
		}catch(Exception e){
			showerror(sql, e);
		}finally{
			close();
		}
		return list;
	}
	
	/**
	 * @作者 聂宇豪
	 * @时间 2018年9月5日
	 * @说明 根据参数返回list的查询结果
	 * */
	public List<Map<String, Object>> query(String sql,Object...prams){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		connect();//建立连接
		try{
			pstm = conn.prepareStatement(sql);//预加载sql
			if(prams!=null&&prams.length>0){
				for(int i = 0;i<prams.length;i++){//设置参数
					pstm.setObject(i+1, prams[i]);
				}
			}
			rs = pstm.executeQuery();
			ResultSetMetaData md = rs.getMetaData();// 获得结果集结构信息（元数据）
	        int columnCount = md.getColumnCount();
			while(rs.next()){
				Map<String, Object> map = new HashMap<String,Object>();
				for (int i = 1; i <= columnCount; i++) {// 遍历获取对当前行的每一列的键值对，put到map中
	                map.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
	            }
				list.add(map);
			}
		}catch(Exception e){
			showerror(sql, e);
		}finally{
			close();
		}
		return list;
	}
	
	/**
	 * @聂宇豪
	 * @时间 2018年9月5日
	 * @说明 无参更新
	 * */
	public int update(String sql){
		int result = 0;
		connect();
		try{
			pstm=conn.prepareStatement(sql);//预加载sql,防注入
			result=pstm.executeUpdate();//执行语句,获得结果
		}catch(Exception e){
			showerror(sql, e);
		}finally{
			close();
		}
		return result;
	}
	
	/**
	 * @作者 聂宇豪
	 * @时间 2018年9月5日
	 * @说明 执行更新语句
	 * **/
	public int update(String sql,Object...prams){
		int result = 0;
		connect();
		try{
			pstm=conn.prepareStatement(sql);//预加载sql,防注入
			if(prams!=null&&prams.length>0){//设置参数
				for(int i=0;i<prams.length;i++){
					pstm.setObject(i+1, prams[i]);
				}
			}
			result=pstm.executeUpdate();//执行语句,获得结果
		}catch(Exception e){
			showerror(sql, e);
		}finally{
			close();
		}
		return result;
	}
	
	/**
	 * @作者 聂宇豪
	 * @时间 2018年9月5日
	 * @说明 使用map传参
	 * */
	public List<Map<String, Object>> query(String sql,Map<String, Object> map){
		List<Map<String, Object>> list = null;
		Object[] prams = dealMap(sql, map);
		sql = dealSql(sql, map.keySet());
		list = query(sql,prams);
		return list;
	}
	
	/**
	 * @作者 聂宇豪
	 * @时间	2018年9月5日
	 * @说明 map传参更新语句
	 * */
	public int update(String sql,Map<String, Object> map){
		Object[] prams = dealMap(sql, map);
		sql = dealSql(sql, map.keySet());
		int result = update(sql,prams);
		return result;
	}
	
	/**
	 * @聂宇豪
	 * @时间 2018年9月5日
	 * @说明 批量更新
	 * */
	public void update(String sql,List<Map<String,Object>> list){
		for(Map<String, Object> map : list){
			Object[] prams = dealMap(sql, map);
			sql = dealSql(sql, map.keySet());
			update(sql,prams);
		}
	}
	
	/**
	 * @作者 聂宇豪
	 * @时间	2018年9月5日
	 * @说明 根据语句处理map为Objec[]
	 * */
	private Object[] dealMap(String sql,Map<String, Object> map){
		List<Object> prams = new ArrayList<Object>();
		int start = 0;
		sql = sql.toLowerCase();//转换小写处理
		while(map!=null){
			Object o = null;
			int min = 0;
			for(String key : map.keySet()){
				if(sql.indexOf(":"+key.toLowerCase(),start)==-1) continue;
				if(sql.indexOf(":"+key.toLowerCase(),start)<min||o==null){
					min=sql.indexOf(":"+key.toLowerCase(),start);
					o=map.get(key);
				}
			}
			if(o==null) break;
			start = min+1;
			prams.add(o);
		}
		return prams.toArray();
	}
	
	private String dealSql(String sql,Set<String> k){
		for(String key : k){
			sql = sql.replace(":"+key, "?");
		}
		return sql;
	}
	
	private void showerror(String sql,Exception e){
		System.out.println("[:sql]:error".replace(":sql", sql).replace(":error", e.getMessage()));
	}
}
