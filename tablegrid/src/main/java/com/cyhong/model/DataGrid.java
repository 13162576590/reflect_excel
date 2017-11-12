package com.cyhong.model;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyUI DataGrid模型
 * 
 * @author wangyanze
 * 
 */
@SuppressWarnings("serial")
public class DataGrid implements java.io.Serializable {

	private Long total = 0L;

	@SuppressWarnings("unchecked")
	private List rows = new ArrayList();

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@SuppressWarnings("unchecked")
	public List getRows() {
		return rows;
	}

	@SuppressWarnings("unchecked")
	public void setRows(List rows) {
		this.rows = rows;
	}

}
