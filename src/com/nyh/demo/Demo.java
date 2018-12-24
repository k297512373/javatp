package com.nyh.demo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nyh.demo.consoletable.ConsoleTable;
import com.nyh.javatp.model.Model;


public class Demo {

	public static void main(String[] args) {
		//ʵ����
		Model m = new Model();
		m.setConn("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "train", "train");//�������ݿ�����
		m.OracleModel();//�趨Ϊoracleģʽ
		
		//���в�ѯ
		show(m.From("user_tables").find(),"���в�ѯ");
		
		//����ָ���ֶβ�ѯ ע�����û������ʹ��From���ò�ѯ�ı��,�������Ĳ�ѯ�����ѯ���ű��
		show(m.field("table_name,tablespace_name").find(),"����ָ���ֶ�");
		
		//����������ѯ
		show(m.where("instr(table_name,:name)>0").put("name","A").find(),"����������ѯ");//���ұ����а���A�ļ�¼�ĵ�һ��
		
		//�������� and����where�Ļ������������,���û��ʹ��whereֱ��ʹ��and ��ͬ��where
		show(m.and("instr(tablespace_name,:name)>0").put("name","U").find(),"����������ѯ");
		
		//���в�ѯ ע��ʹ��where �����ǰ����and��or��ӵĲ�ѯ����
		show(m.where("rownum<10").select(),"���в�ѯ");
		
		//�����ѯ  ע��field�����»ᱣ��ԭ�����趨 ��ʹ�� where("")�����ѯ����,�������ᱻ����
		show(m.field("count(1) c,substr(table_name,0,1) first").where("").GroupBy("substr(table_name,0,1)").select(),"�����ѯ");
		
		//�����ѯ����ɸѡ ɸѡ������һ��д��� ���� a>2 and b>3 or c<0
		show(m.Having("count(1)>1").select(),"�����ѯ����ɸѡ");
		
		//��ѯ����
		show(m.field("table_name,tablespace_name").where("rownum<11").GroupBy("").Having("").OrderBy("table_name desc").select(),"��ѯ����");
		
		//���ϲ�ѯ ע�� where�����on������ on�ȼ���and
		show(m.field("A.table_name,A.column_name,comments").From("user_tab_cols").join("user_col_comments").where("rownum<11").on("A.table_name=B.table_name").on("A.column_name=B.column_name").select(),"���ϲ�ѯ");
		
		//��ҳ��ѯ
		show(m.where("").page(2, 10).getResult(),"��ҳ��ѯ");
		
		//����
		Map<String, Object> parms = new  HashMap<String, Object>();
		parms.put("k", "k");
		parms.put("v","v");
		m.From("tp_test").put(parms).insert();
		show(m.select(),"������");
		
		//����
		parms.put("v", "a");
		m.From("tp_test").where("k=:k").put(parms).update();
		show(m.select(),"���º���");
		
		//ɾ��
		m.delete();
		show(m.select(),"ɾ������");
	}
	
	private static void show(Map<String, Object> map,String title){
		if(map!=null){
			System.out.println(title);
			ConsoleTable t = new ConsoleTable(map.keySet().size(), true);
			t.appendRow();
			for(String key : map.keySet()){
				t.appendColum(key);
			}
			t.appendRow();
			for(String key : map.keySet()){
				t.appendColum(map.get(key));
			}
			System.out.println(t.toString());
		}
	}
	
	private static void show(List<Map<String, Object>> list,String title){
		if(list!=null&&list.size()!=0){
			System.out.println(title);
			ConsoleTable t = new ConsoleTable(list.get(0).keySet().size(), true);
			t.appendRow();
			for(String key : list.get(0).keySet()){
				t.appendColum(key);
			}
			for(Map<String, Object> row : list){
				t.appendRow();
				for(String key : row.keySet()){
					t.appendColum(row.get(key));
				}
			}
			System.out.println(t.toString());
		}
	}
}
