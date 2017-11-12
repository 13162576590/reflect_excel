package com.cyhong.model;

public class TableHead {
	
	private int colNo;
	
	private String visiable;
	
	private String fieldName;

	private String fieldProperty;
	
	private String key;

	private String nullable;

	//联合主键
	private String cId;

	public int getColNo() {
		return colNo;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	public String getVisiable() {
		return visiable;
	}

	public void setVisiable(String visiable) {
		this.visiable = visiable;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldProperty() {
		return fieldProperty;
	}

	public void setFieldProperty(String fieldProperty) {
		this.fieldProperty = fieldProperty;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	@Override
	public String toString() {
		return "TableHead [colNo=" + colNo + ", visiable=" + visiable + ", fieldName=" + fieldName + ", fieldProperty="
				+ fieldProperty + ", key=" + key + ", nullable=" + nullable + ", cId=" + cId + "]";
	}

}
