package com.lansitec.springmvc.beans;

public class ProjectManagerBean {
	private String sn;
	private String company;
	private String fieldName;
	private String name;
	public String getSn() {
		return sn;
	}
	public String getCompany() {
		return company;
	}
	public String getFieldName() {
		return fieldName;
	}
	public String getName() {
		return name;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public void setName(String name) {
		this.name = name;
	}
}
