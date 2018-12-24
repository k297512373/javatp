package com.nyh.javatpmodel.service;

import java.util.List;
import java.util.Map;

import com.nyh.javatpmodel.model.ConditionModel;
import com.nyh.javatpmodel.model.ConnectModel;
import com.nyh.javatpmodel.model.PageModel;

/**
 * 数据库接口
 * @作者 nyh
 * */
public interface DbService {
	public void setConnect(ConnectModel conn);
	public Map<String, Object> find(Map<String, String> tables,ConditionModel condition);
	public List<Map<String, Object>> select(Map<String, String> tables,ConditionModel condition);
	public PageModel<Map<String, Object>> select(Map<String, String> tables,ConditionModel condition,PageModel<Map<String, Object>> page);
	public int insert(String name,Map<String, Object> parms);
	public int update(String name,ConditionModel condition);
	public int delete(String name,ConditionModel condition);
}
