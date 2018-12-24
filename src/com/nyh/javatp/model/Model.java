package com.nyh.javatp.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nyh.javatpmodel.model.ConditionModel;
import com.nyh.javatpmodel.model.ConnectModel;
import com.nyh.javatpmodel.model.PageModel;
import com.nyh.javatpmodel.service.DbService;
import com.nyh.javatpmodel.service.impl.OracleServiceImpl;

/**
 * ����th5���ݿ�model����jdbc��װ��java������
 * */
public class Model {
	private DbService db;//���ݿ�ӿ�
	private ConditionModel condition = new ConditionModel();//��ѯ����
	private Map<String, String> tables = new HashMap<String, String>();//��ѯ�ı��Լ�����
	private String name;//���롢���¡�ɾ���ı������
	private ConnectModel connect = new ConnectModel();//���ݿ�������Ϣ
	private String seqn_="";//����������
	private char seq_='A';
	
	/**����ע�����ݿ�ʵ��*/
	public void setDb(DbService db){
		this.db = db;
		db.setConnect(connect);
	}
	
	/**����ע�������ַ���*/
	public void setUrl(String url){
		connect.setUrl(url);
	}
	
	/**����ע�����ݿ��û���*/
	public void setName(String name){
		connect.setName(name);
	}
	
	/**����ע�����ݿ�����*/
	public void setPass(String pass){
		connect.setPass(pass);
	}
	
	/**�������ݿ�����
	 * @param url �����ַ���
	 * @param name �û���
	 * @param pass ����
	 * */
	public Model setConn(String url,String name,String pass){
		connect.setUrl(url);
		connect.setName(name);
		connect.setPass(pass);
		return this;
	}
	/**����Ϊoracleģʽ*/
	public Model OracleModel(){
		db = new OracleServiceImpl();
		db.setConnect(connect);
		return this;
	}
	
	/**����Ϊmysqlģʽ*/
	public Model MySqlModel(){
		new Exception("��δ��дmysqlģʽ").printStackTrace();;
		return this;
	}
	/**���ò�ѯ,���²���ɾ�������ı��,Ĭ�ϱ���A,��������ϲ�ѯ��ӵı���*/
	public Model From(String name){
		this.name = name;
		tables = new HashMap<String,String>();
		tables.put("A", name);
		seq_='B';
		seqn_="";
		return this;
	}
	
	/**@˵�� ���ò�ѯ����������ñ���,��������ϲ�ѯ��ӵı���
	 * @name ����
	 * @oname ����
	 * */
	public Model From(String name,String oname){
		tables = new HashMap<String,String>();
		tables.put(oname, name);
		return this;
	}
	
	/**������ϲ�ѯ���,������ĸ��˳�������*/
	public Model join(String name){
		if(tables==null){
			tables=new HashMap<String,String>();
		}
		if(seq_!='Z'){
			tables.put(seqn_+seq_, name);
			seq_++;
		}else{
			tables.put(seqn_+seq_, name);
			if("".equals(seqn_)){
				seqn_="A";
			}else{
				String s = "";
				int now = 1;
				int next = 0;
				for(int i=seqn_.length()-1;i>=0;i--){
					char n = seqn_.toCharArray()[i];
					if(i!=seqn_.length()-1){
						now = next;
						next = 0;
					}
					if(n+now<='Z'){
						n+=now;
						s=n+s;
					}else{
						next++;
						s=s+'A';
						if(i==0){
							s="A"+s;
						}
					}
				}
				seqn_=s;
			}
			seq_='A';
		}
		return this;
	}
	
	/**������ϲ�ѯ���,����������*/
	public Model join(String name,String oname){
		if(tables==null){
			tables=new HashMap<String,String>();
		}
		tables.put(oname, name);
		return this;
	}
	
	/**������ϲ�ѯ����,��And�����ȼ�*/
	public Model on(String and){
		if(condition.getWhere()==null||"".equals(condition.getWhere())){
			condition.setWhere(and);
		}else{
			condition.setWhere(condition.getWhere()+" and "+and);
		}
		return this;
	}
	
	/**
	 * @˵�� ��Ӳ�ѯҪ��ѯ���� field1,field2,field3....
	 * @ע�� ����ж�� �뱸ע���� ��:a.field1
	 * */
	public Model field(String field){
		List<String> fields = new ArrayList<String>();
		for(String f : field.split(",")){
			fields.add(f);
		}
		condition.setField(fields);
		return this;
	}
	
	/**��ѯ����*/
	public Model where(String where){
		condition.setWhere(where);
		return this;
	}
	
	/**׷�Ӳ�ѯ����,���ڶ�̬��ѯ,Ĭ������ǰ��ƴ�� and �����ظ�ƴ��*/
	public Model and(String and){
		if(condition.getWhere()==null||"".equals(condition.getWhere())){
			condition.setWhere(and);
		}else{
			condition.setWhere(condition.getWhere()+" and "+and);
		}
		return this;
	}
	
	/**׷�Ӳ�ѯ����,���ڶ�̬��ѯ,Ĭ������ǰ��ƴ��or �����ظ�ƴ��*/
	public Model or(String or){
		if(condition.getWhere()==null||"".equals(condition.getWhere())){
			condition.setWhere(or);
		}else{
			condition.setWhere(condition.getWhere()+" or "+or);
		}
		return this;
	}
	
	/**�������� ����ʹ�ö��Ÿ���,Ĭ��ƴ��group by ���*/
	public Model GroupBy(String group){
		condition.setGroup(group);
		return this;
	}
	
	/**����group by ���ɸѡ���,Ĭ��ƴ��having �����ڶ�̬��ѯ,��ƴ���߼��ؼ���,��д�� and or �ȹؼ���*/
	public Model Having(String having){
		if(condition.getHaving()==null||"".equals(condition.getHaving())){
			condition.setHaving(having);
		}else{
			condition.setHaving(condition.getHaving()+" "+having);
		}
		return this;
	}
	
	/**�������� ����ע�� asc desc,Ĭ��ƴ�� order by*/
	public Model OrderBy(String order){
		condition.setOrder(order);
		return this;
	}
	
	/**���ò���*/
	public Model put(String key,Object value){
		if(condition.getParams()==null){
			condition.setParams(new HashMap<String,Object>());
		}
		Map<String, Object> parms = condition.getParams();
		parms.put(key, value);
		condition.setParams(parms);
		return this;
	}
	
	/**���ò��� �����ػḲ��ԭ���Ĳ�������*/
	public Model put(Map<String, Object> parms){
		condition.setParams(parms);
		return this;
	}
	
	/**���������õ��������е�һ�����ѯ*/
	public Map<String, Object> find(){
		return db.find(tables, condition);
	}
	
	/**���������õ��������в�ѯ,�޷�ҳ*/
	public List<Map<String, Object>> select(){
		return db.select(tables, condition);
	}
	
	/**���������õ�������ҳ��ѯ
	 * @param page ��ǰҳ
	 * @param row ÿҳ��ʾ����*/
	public PageModel<Map<String, Object>> page(int page,int row){
		PageModel<Map<String, Object>> p = new PageModel<Map<String, Object>>();
		p.setPage(page);
		p.setRows(row);
		return db.select(tables, condition, p);
	}
	
	/**���������õ��������и��� ������Ӱ������*/
	public int update(){
		return db.update(name, condition);
	}
	
	/**���������õ��������в��� ������Ӱ������*/
	public int insert(){
		return db.insert(name, condition.getParams());
	}
	
	/**��������������ɾ�� ������Ӱ������*/
	public int delete(){
		return db.delete(name, condition);
	}
}
