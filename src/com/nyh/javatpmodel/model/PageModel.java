package com.nyh.javatpmodel.model;

import java.util.List;

public class PageModel<T>{
	//分页模型
	private int page = -1;//页码
	private int rows = 10;//每页显示行数
	private int total;//总记录数
	private int totalpage;//总页数
	private List<T> result;//查询结果
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
		if(total>0){
			totalpage = total%rows==0?total/rows:total/rows+1;//计算页数
		}
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
		if(rows>0){
			totalpage = total%rows==0?total/rows:total/rows+1;//计算页数
		}
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public int getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
}
