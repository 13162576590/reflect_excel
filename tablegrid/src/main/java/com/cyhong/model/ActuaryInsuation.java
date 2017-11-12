package com.cyhong.model;

public class ActuaryInsuation {

	private String belong;
	
	private String comname;
	
	private String anaT9;

	private String dbCode;

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

	public String getAnaT9() {
		return anaT9;
	}

	public void setAnaT9(String anaT9) {
		this.anaT9 = anaT9;
	}

	public String getDbCode() {
		return dbCode;
	}

	public void setDbCode(String dbCode) {
		this.dbCode = dbCode;
	}

	@Override
	public String toString() {
		return "ActuaryInsuation [belong=" + belong + ", comname=" + comname + ", anaT9=" + anaT9 + ", dbCode=" + dbCode
				+ "]";
	}

}
