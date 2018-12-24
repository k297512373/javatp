package com.nyh.javatpmodel.model;

import java.util.List;

public class PageModel<T>{
	//��ҳģ��
	private int page = -1;//ҳ��
	private int rows = 10;//ÿҳ��ʾ����
	private int total;//�ܼ�¼��
	private int totalpage;//��ҳ��
	private List<T> result;//��ѯ���
	
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
			totalpage = total%rows==0?total/rows:total/rows+1;//����ҳ��
		}
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
		if(rows>0){
			totalpage = total%rows==0?total/rows:total/rows+1;//����ҳ��
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
