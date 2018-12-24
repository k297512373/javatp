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
 * 仿照th5数据库model基于jdbc封装的java工具类
 * */
public class Model {
	private DbService db;//数据库接口
	private ConditionModel condition = new ConditionModel();//查询条件
	private Map<String, String> tables = new HashMap<String, String>();//查询的表以及别名
	private String name;//插入、更新、删除的表格名称
	private ConnectModel connect = new ConnectModel();//数据库链接信息
	private String seqn_="";//表格别名计数
	private char seq_='A';
	
	/**用于注入数据库实现*/
	public void setDb(DbService db){
		this.db = db;
		db.setConnect(connect);
	}
	
	/**用于注入连接字符串*/
	public void setUrl(String url){
		connect.setUrl(url);
	}
	
	/**用于注入数据库用户名*/
	public void setName(String name){
		connect.setName(name);
	}
	
	/**用于注入数据库密码*/
	public void setPass(String pass){
		connect.setPass(pass);
	}
	
	/**设置数据库连接
	 * @param url 连接字符串
	 * @param name 用户名
	 * @param pass 密码
	 * */
	public Model setConn(String url,String name,String pass){
		connect.setUrl(url);
		connect.setName(name);
		connect.setPass(pass);
		return this;
	}
	/**设置为oracle模式*/
	public Model OracleModel(){
		db = new OracleServiceImpl();
		db.setConnect(connect);
		return this;
	}
	
	/**设置为mysql模式*/
	public Model MySqlModel(){
		new Exception("暂未编写mysql模式").printStackTrace();;
		return this;
	}
	/**设置查询,更新插入删除操作的表格,默认别名A,会清除联合查询添加的表名*/
	public Model From(String name){
		this.name = name;
		tables = new HashMap<String,String>();
		tables.put("A", name);
		seq_='B';
		seqn_="";
		return this;
	}
	
	/**@说明 设置查询操作表格并设置别名,会清除联合查询添加的表名
	 * @name 表名
	 * @oname 别名
	 * */
	public Model From(String name,String oname){
		tables = new HashMap<String,String>();
		tables.put(oname, name);
		return this;
	}
	
	/**添加联合查询表格,并以字母表顺序起别名*/
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
	
	/**添加联合查询表格,并命名别名*/
	public Model join(String name,String oname){
		if(tables==null){
			tables=new HashMap<String,String>();
		}
		tables.put(oname, name);
		return this;
	}
	
	/**添加联合查询条件,和And方法等价*/
	public Model on(String and){
		if(condition.getWhere()==null||"".equals(condition.getWhere())){
			condition.setWhere(and);
		}else{
			condition.setWhere(condition.getWhere()+" and "+and);
		}
		return this;
	}
	
	/**
	 * @说明 添加查询要查询的列 field1,field2,field3....
	 * @注意 如果有多表 请备注别名 例:a.field1
	 * */
	public Model field(String field){
		List<String> fields = new ArrayList<String>();
		for(String f : field.split(",")){
			fields.add(f);
		}
		condition.setField(fields);
		return this;
	}
	
	/**查询条件*/
	public Model where(String where){
		condition.setWhere(where);
		return this;
	}
	
	/**追加查询条件,用于动态查询,默认条件前面拼接 and 请勿重复拼接*/
	public Model and(String and){
		if(condition.getWhere()==null||"".equals(condition.getWhere())){
			condition.setWhere(and);
		}else{
			condition.setWhere(condition.getWhere()+" and "+and);
		}
		return this;
	}
	
	/**追加查询条件,用于动态查询,默认条件前面拼接or 请勿重复拼接*/
	public Model or(String or){
		if(condition.getWhere()==null||"".equals(condition.getWhere())){
			condition.setWhere(or);
		}else{
			condition.setWhere(condition.getWhere()+" or "+or);
		}
		return this;
	}
	
	/**分组条件 多列使用逗号隔开,默认拼接group by 语句*/
	public Model GroupBy(String group){
		condition.setGroup(group);
		return this;
	}
	
	/**设置group by 后的筛选语句,默认拼接having 可用于动态查询,不拼接逻辑关键字,请写明 and or 等关键字*/
	public Model Having(String having){
		if(condition.getHaving()==null||"".equals(condition.getHaving())){
			condition.setHaving(having);
		}else{
			condition.setHaving(condition.getHaving()+" "+having);
		}
		return this;
	}
	
	/**设置排序 可以注明 asc desc,默认拼接 order by*/
	public Model OrderBy(String order){
		condition.setOrder(order);
		return this;
	}
	
	/**设置参数*/
	public Model put(String key,Object value){
		if(condition.getParams()==null){
			condition.setParams(new HashMap<String,Object>());
		}
		Map<String, Object> parms = condition.getParams();
		parms.put(key, value);
		condition.setParams(parms);
		return this;
	}
	
	/**设置参数 此重载会覆盖原来的参数设置*/
	public Model put(Map<String, Object> parms){
		condition.setParams(parms);
		return this;
	}
	
	/**根据已设置的条件进行单一结果查询*/
	public Map<String, Object> find(){
		return db.find(tables, condition);
	}
	
	/**根据已设置的条件进行查询,无分页*/
	public List<Map<String, Object>> select(){
		return db.select(tables, condition);
	}
	
	/**根据已设置的条件分页查询
	 * @param page 当前页
	 * @param row 每页显示行数*/
	public PageModel<Map<String, Object>> page(int page,int row){
		PageModel<Map<String, Object>> p = new PageModel<Map<String, Object>>();
		p.setPage(page);
		p.setRows(row);
		return db.select(tables, condition, p);
	}
	
	/**根据已设置的条件进行更新 返回受影响行数*/
	public int update(){
		return db.update(name, condition);
	}
	
	/**根据已设置的条件进行插入 返回受影响行数*/
	public int insert(){
		return db.insert(name, condition.getParams());
	}
	
	/**根据已设置条件删除 返回受影响行数*/
	public int delete(){
		return db.delete(name, condition);
	}
}
