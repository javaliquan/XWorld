package com.founderdpt.comm.xworld.orm.page;

import java.io.Serializable;
import java.util.List;



public class Page<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7164938910033841409L;
	// 传参或指定
	protected int pageNo = 1; // 当前页号, 采用自然数计数 1,2,3,...
	protected int pageSize = 10; // 页面大小:一个页面显示多少个数据

	// 需要从数据库中查找出
	protected long rowCount;// 数据总数：一共有多少个数据
	
	protected List<T> result;

	public Page(){
		
	}
	
	
	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}


	public List<T> getResult() {
		return result;
	}


	public void setResult(List<T> result) {
		this.result = result;
	}
 
 

	
	 

}