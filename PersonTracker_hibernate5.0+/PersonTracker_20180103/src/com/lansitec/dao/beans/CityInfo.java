package com.lansitec.dao.beans;

public class CityInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String city;
	public CityInfo(){
		
	}
    public CityInfo(String city){
		this.city = city;
	}
	public Integer getId() {
		return id;
	}
	public String getCity() {
		return city;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "CityInfo [id=" + id + ", city=" + city + "]";
	}
}
