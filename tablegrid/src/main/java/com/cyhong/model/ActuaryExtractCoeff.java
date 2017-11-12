package com.cyhong.model;

public class ActuaryExtractCoeff {

	private String sunName;
	
	private String resType;
	
	private String accAccont;

	public String getSunName() {
		return sunName;
	}

	public void setSunName(String sunName) {
		this.sunName = sunName;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getAccAccont() {
		return accAccont;
	}

	public void setAccAccont(String accAccont) {
		this.accAccont = accAccont;
	}

	@Override
	public String toString() {
		return "ActuaryExtractCoeff [sunName=" + sunName + ", resType=" + resType + ", accAccont=" + accAccont + "]";
	}
	
}
