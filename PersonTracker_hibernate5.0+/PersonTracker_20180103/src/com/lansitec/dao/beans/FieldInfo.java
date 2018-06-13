package com.lansitec.dao.beans;

public class FieldInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String sn;
	private String city;
	private byte gwnumber;
	private short motenumber;
	public FieldInfo(String name,String sn,String city){
		this.name = name;
		this.sn = sn;
		this.city = city;
	}
    public FieldInfo(){
		
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
	public String getCity() {
		return city;
	}
	public byte getGwnumber() {
		return gwnumber;
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
	public void setCity(String city) {
		this.city = city;
	}
	public void setGwnumber(byte gwnumber) {
		this.gwnumber = gwnumber;
	}
	public void setMotenumber(short motenumber) {
		this.motenumber = motenumber;
	}
	@Override
	public String toString() {
		return "FieldInfo [id=" + id + ", name=" + name + ", sn=" + sn + ", city=" + city + ", gwnumber=" + gwnumber
				+ ", motenumber=" + motenumber + "]";
	}
}
