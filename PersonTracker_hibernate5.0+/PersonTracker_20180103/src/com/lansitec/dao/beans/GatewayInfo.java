package com.lansitec.dao.beans;

public class GatewayInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String sn;
	private String type;
	private String field;
	private short motenumber;
	public GatewayInfo(){
		
	}
    public GatewayInfo(String name,String sn,String type,String field){
		this.name = name;
		this.sn = sn;
		this.type = type;
		this.field = field;
	}
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSn() {
		return sn;
	}
	public String getType() {
		return type;
	}
	public String getField() {
		return field;
	}
	public short getMotenumber() {
		return motenumber;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setField(String field) {
		this.field = field;
	}
	public void setMotenumber(short motenumber) {
		this.motenumber = motenumber;
	}
}
