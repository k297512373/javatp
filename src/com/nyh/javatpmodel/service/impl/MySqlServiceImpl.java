package com.nyh.javatpmodel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nyh.javatpmodel.dao.MySqlDao;
import com.nyh.javatpmodel.model.ConditionModel;
import com.nyh.javatpmodel.model.ConnectModel;
import com.nyh.javatpmodel.model.PageModel;
import com.nyh.javatpmodel.service.DbService;

public class MySqlServiceImpl implements DbService {
	private MySqlDao dao;
	private ConnectModel conn;
	
	@Override
	public void setConnect(ConnectModel conn) {
		this.conn = conn;
	}

	@Override
	public List<Map<String, Object>> select(Map<String, String> tables,ConditionModel condition){
		//����ҳ��ѯ
		Connect();
		String sql = getSelectSql(tables, condition);//��װ��ѯ���
		List<Map<String, Object>> list = dao.query(sql, condition.getParams());//��ȡ��ѯ���
		return list;
	}

	@Override
	public PageModel<Map<String, Object>> select(Map<String, String> tables,ConditionModel condition, PageModel<Map<String, Object>> page){
		//��ҳ��ѯ
		Connect();
		String sql = getSelectSql(tables, condition);//��װ��ѯ���
		Map<String, Object> parms = condition.getParams();//��ȡ����
		String total = dao.query(String.format("select count(1) c from (%s)", sql), parms).get(0).get("c").toString();
		page.setTotal(Integer.parseInt(total));//�����ܼ�¼��
		int start = (page.getPage()-1)*page.getRows();//��ʼ��
		int end = start + page.getRows();//������
		List<Map<String, Object>> list = dao.query(String.format("select * from (%s) limt %d,%d",sql,end,start), parms);
		page.setResult(list);
		return page;
	}

	@Override
	public int insert(String name, Map<String, Object> parms){
		//����
		Connect();
		StringBuffer sql = new StringBuffer(String.format("insert into %s(", name));
		List<String> cols = getCols(name);
		StringBuffer values=new StringBuffer(")values(");
		for(String key : parms.keySet()){
			if(cols.contains(key.toUpperCase())){
				sql.append(key+",");
				values.append(":"+key+",");
			}
		}
		sql.deleteCharAt(sql.length()-1);
		values.deleteCharAt(values.length()-1);
		int effect = dao.update(sql.toString()+values.toString()+")", parms);
		return effect;
	}

	@Override
	public int update(String name, ConditionModel condition){
		//�޸�
		Connect();
		List<String> cols = getCols(name);
		StringBuffer sql = new StringBuffer(String.format("update %s set ", name));
		for(String key : condition.getParams().keySet()){
			if(cols.contains(key.toUpperCase())){
				sql.append(String.format("%s=:%s,", key,key));
			}
		}
		sql.deleteCharAt(sql.length()-1);
		if(condition.getWhere()!=null&&!"".equals(condition.getWhere())){
			sql.append(String.format(" where %s", condition.getWhere()));
		}
		int effect = dao.update(sql.toString(),condition.getParams());
		return effect;
	}

	@Override
	public int delete(String name, ConditionModel condition) {
		//ɾ��
		Connect();
		StringBuffer sql = new StringBuffer(String.format("delete from %s ", name));
		if(condition.getWhere()!=null&&!"".equals(condition.getWhere())){
			sql.append(String.format(" where %s", condition.getWhere()));
		}
		int effect = dao.update(sql.toString(),condition.getParams());
		return effect;
	}
	
	@Override
	public Map<String, Object> find(Map<String, String> tables,ConditionModel condition){
		//������¼����
		Connect();
		String sql = getSelectSql(tables, condition);
		List<Map<String, Object>> list = dao.query(sql, condition.getParams());
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	private void Connect(){
		//�������ݿ�
		if(this.conn!=null&&dao==null){
			dao = new MySqlDao(conn.getUrl(),conn.getName(),conn.getPass());
		}
	}
	
	private String getSelectSql(Map<String, String> tables,ConditionModel condition){
		//��װsql
		StringBuffer sql =new StringBuffer("select ");
		if(condition.getField()==null||condition.getField().size()==0){
			sql.append("* ");//û���ֶ�Ҫ�����������
		}else{
			for (String field : condition.getField()) {
				sql.append(field+",");//ƴ�Ӳ�ѯ�ֶ�
			}
			sql.deleteCharAt(sql.length()-1);//ȥ��ĩβ������ַ�
		}
		if(tables==null||tables.size()==0) new Exception("����Ϊ��").printStackTrace();
		sql.append(" from ");
		for (String key : tables.keySet()) {
			sql.append(String.format("%s %s,", tables.get(key),key));//ƴ�ӱ����ͱ���
		}
		sql.deleteCharAt(sql.length()-1);
		if(condition.getWhere()!=null&&!"".equals(condition.getWhere())){
			sql.append(String.format(" where %s", condition.getWhere()));
		}
		if(condition.getGroup()!=null&&!"".equals(condition.getGroup())){
			sql.append(" group by "+condition.getGroup());//ƴ�ӷ������
			if(condition.getHaving()!=null&&!"".equals(condition.getHaving())){
				sql.append(String.format(" having %s", condition.getHaving()));
			}
		}
		if(condition.getOrder()!=null){
			sql.append(" order by "+condition.getOrder());//ƾ������
		}
		return sql.toString();
	}
	
	private List<String> getCols(String name){
		//��ȡ��������
		List<Map<String, Object>> col = dao.query("desc "+name);
		List<String> cols = new ArrayList<String>();
		if(col==null||col.size()==0) new Exception(String.format("��%s��������", name)).printStackTrace();
		for(Map<String, Object> row : col){
			cols.add(row.get("column_name").toString().toUpperCase());
		}
		return cols;
	}
	
}
