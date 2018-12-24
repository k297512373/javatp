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

public class OracleDao {
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";//�����ַ���
	private String name;//�û���
	private String pass;//����
	private Connection conn;
	private PreparedStatement pstm;
	private ResultSet rs;
	
	/**@���� �޲ι���<br>Ĭ�������ַ���:jdbc:oracle:thin:@127.0.0.1:1521:orcl<br>Ĭ���û������� :peixun/peixun_585 
	 * */
	public OracleDao(){}
	
	/**@˵�� Ĭ�������ַ������޸��û�������Ĺ���
	 * @name �û���
	 * @pass ����*/
	public OracleDao(String name,String pass){
		this.name=name;
		this.pass=pass;
	}
	
	/**@˵�� �޸������ַ���,�û���������Ĺ���
	 * @url �����ַ���
	 * @name �û���
	 * @pass ����*/
	public OracleDao(String url,String name,String pass){
		this.url=url;
		this.name=name;
		this.pass=pass;
	}
	
	private void connect(){//��������
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			if(conn==null||conn.isClosed()){
				conn=DriverManager.getConnection(url, name, pass);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	private void close(){//�رղ���
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
	 * @���� �����
	 * @ʱ�� 2018��9��5��
	 * @˵�� �޲β�ѯ
	 * */
	public List<Map<String, Object>> query(String sql){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		connect();//��������
		try{
			pstm = conn.prepareStatement(sql);//Ԥ����sql
			rs = pstm.executeQuery();
			ResultSetMetaData md = rs.getMetaData();// ��ý�����ṹ��Ϣ��Ԫ���ݣ�
	        int columnCount = md.getColumnCount();
			while(rs.next()){
				Map<String, Object> map = new HashMap<String,Object>();
				for (int i = 1; i <= columnCount; i++) {// ������ȡ�Ե�ǰ�е�ÿһ�еļ�ֵ�ԣ�put��map��
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
	 * @���� �����
	 * @ʱ�� 2018��9��5��
	 * @˵�� ���ݲ�������list�Ĳ�ѯ���
	 * */
	public List<Map<String, Object>> query(String sql,Object...prams){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		connect();//��������
		try{
			pstm = conn.prepareStatement(sql);//Ԥ����sql
			if(prams!=null&&prams.length>0){
				for(int i = 0;i<prams.length;i++){//���ò���
					pstm.setObject(i+1, prams[i]);
				}
			}
			rs = pstm.executeQuery();
			ResultSetMetaData md = rs.getMetaData();// ��ý�����ṹ��Ϣ��Ԫ���ݣ�
	        int columnCount = md.getColumnCount();
			while(rs.next()){
				Map<String, Object> map = new HashMap<String,Object>();
				for (int i = 1; i <= columnCount; i++) {// ������ȡ�Ե�ǰ�е�ÿһ�еļ�ֵ�ԣ�put��map��
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
	 * @�����
	 * @ʱ�� 2018��9��5��
	 * @˵�� �޲θ���
	 * */
	public int update(String sql){
		int result = 0;
		connect();
		try{
			pstm=conn.prepareStatement(sql);//Ԥ����sql,��ע��
			result=pstm.executeUpdate();//ִ�����,��ý��
		}catch(Exception e){
			showerror(sql, e);
		}finally{
			close();
		}
		return result;
	}
	
	/**
	 * @���� �����
	 * @ʱ�� 2018��9��5��
	 * @˵�� ִ�и������
	 * **/
	public int update(String sql,Object...prams){
		int result = 0;
		connect();
		try{
			pstm=conn.prepareStatement(sql);//Ԥ����sql,��ע��
			if(prams!=null&&prams.length>0){//���ò���
				for(int i=0;i<prams.length;i++){
					pstm.setObject(i+1, prams[i]);
				}
			}
			result=pstm.executeUpdate();//ִ�����,��ý��
		}catch(Exception e){
			showerror(sql, e);
		}finally{
			close();
		}
		return result;
	}
	
	/**
	 * @���� �����
	 * @ʱ�� 2018��9��5��
	 * @˵�� ʹ��map����
	 * */
	public List<Map<String, Object>> query(String sql,Map<String, Object> map){
		List<Map<String, Object>> list = null;
		Object[] prams = dealMap(sql, map);
		list = query(sql,prams);
		return list;
	}
	
	/**
	 * @���� �����
	 * @ʱ��	2018��9��5��
	 * @˵�� map���θ������
	 * */
	public int update(String sql,Map<String, Object> map){
		Object[] prams = dealMap(sql, map);
		int result = update(sql,prams);
		return result;
	}
	
	/**
	 * @�����
	 * @ʱ�� 2018��9��5��
	 * @˵�� ��������
	 * */
	public void update(String sql,List<Map<String,Object>> list){
		for(Map<String, Object> map : list){
			Object[] prams = dealMap(sql, map);
			update(sql,prams);
		}
	}
	
	/**
	 * @���� �����
	 * @ʱ��	2018��9��5��
	 * @˵�� ������䴦��mapΪObjec[]
	 * */
	private Object[] dealMap(String sql,Map<String, Object> map){
		List<Object> prams = new ArrayList<Object>();
		int start = 0;
		sql = sql.toLowerCase();//ת��Сд����
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
	
	private void showerror(String sql,Exception e){
		System.out.println("[:sql]:error".replace(":sql", sql).replace(":error", e.getMessage()));
	}
}
