package com.nyh.javatpmodel.model;

import java.util.List;
import java.util.Map;

public class ConditionModel {
	//��ѯ����ģ��
	private List<String> field;//����
	private String group;//����
	private String order;//����
	private Map<String, Object> params;//����
	private String having;//����ɸѡ����
	private String where;
	public List<String> getField() {
		return field;
	}
	public void setField(List<String> field) {
		this.field = field;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public String getHaving() {
		return having;
	}
	public void setHaving(String having) {
		this.having = having;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	
}
