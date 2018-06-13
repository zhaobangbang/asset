package com.lansitec.dao.beans;

public class MapInfo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String sn;
	private String building;
	private String ground;
	private Integer width;
	private Integer length;
	private String memo;
	public MapInfo(String name,String sn,String building,String ground,Integer width,Integer length,String memo){
		this.name = name;
		this.sn = sn;
		this.building = building;
		this.ground = ground;
		this.width = width;
		this.length = length;
		this.memo = memo;
	}
    public MapInfo(){
		
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
	public String getBuilding() {
		return building;
	}
	public String getGround() {
		return ground;
	}
	public Integer getWidth() {
		return width;
	}
	public Integer getLength() {
		return length;
	}
	public String getMemo() {
		return memo;
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
	public void setBuilding(String building) {
		this.building = building;
	}
	public void setGround(String ground) {
		this.ground = ground;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
