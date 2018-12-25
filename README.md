//实例化
		Model m = new Model();
		m.setConn("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "train", "train");//设置数据库连接
		m.OracleModel();//设定为oracle模式
		
		//单行查询
		show(m.From("user_tables").find(),"单行查询");
		
		//单行指定字段查询 注意如果没有重新使用From重置查询的表格,接下来的查询都会查询这张表格
		show(m.field("table_name,tablespace_name").find(),"单行指定字段");
		
		//单行条件查询
		show(m.where("instr(table_name,:name)>0").put("name","A").find(),"单行条件查询");//查找表名中包含A的记录的第一行
		
		//复合条件 and会在where的基础上添加条件,如果没有使用where直接使用and 等同于where
		show(m.and("instr(tablespace_name,:name)>0").put("name","U").find(),"复合条件查询");
		
		//多行查询 注意使用where 会清除前面用and或or添加的查询条件
		show(m.where("rownum<10").select(),"多行查询");
		
		//分组查询  注意field不更新会保持原来的设定 不使用 where("")清除查询条件,条件将会被保留
		show(m.field("count(1) c,substr(table_name,0,1) first").where("").GroupBy("substr(table_name,0,1)").select(),"分组查询");
		
		//分组查询二次筛选 筛选条件请一次写清除 例如 a>2 and b>3 or c<0
		show(m.Having("count(1)>1").select(),"分组查询二次筛选");
		
		//查询排序
		show(m.field("table_name,tablespace_name").where("rownum<11").GroupBy("").Having("").OrderBy("table_name desc").select(),"查询排序");
		
		//联合查询 注意 where会清除on的条件 on等价于and
		show(m.field("A.table_name,A.column_name,comments").From("user_tab_cols").join("user_col_comments").where("rownum<11").on("A.table_name=B.table_name").on("A.column_name=B.column_name").select(),"联合查询");
		
		//分页查询
		show(m.where("").page(2, 10).getResult(),"分页查询");
		
		//插入
		Map<String, Object> parms = new  HashMap<String, Object>();
		parms.put("k", "k");
		parms.put("v","v");
		m.From("tp_test").put(parms).insert();
		show(m.select(),"插入结果");
		
		//更新
		parms.put("v", "a");
		m.From("tp_test").where("k=:k").put(parms).update();
		show(m.select(),"更新后结果");
		
		//删除
		m.delete();
		show(m.select(),"删除后结果");*/
		
		Model m = new Model();
		m.setConn("jdbc:mysql://localhost:3306/pic?useSSL=false", "root", "root");//设置数据库连接
		m.MySqlModel();
		//show(m.From("pic_baidu_face_task").select(),"mysql模式");
		Map<String, Object> parms = new  HashMap<String, Object>();
		parms.put("k", "k");
		parms.put("v","13");
		m.From("test").where("k=:k").put(parms).update();
		show(m.select(),"修改测试");
