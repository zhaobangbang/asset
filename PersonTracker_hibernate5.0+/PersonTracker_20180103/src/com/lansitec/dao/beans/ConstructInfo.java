package com.lansitec.dao.beans;

public class ConstructInfo implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String name;
    private String sn;
    private String captain;
    private String tel;
    private String field;
    private String memo;
    public ConstructInfo(){
    	
    }
    public ConstructInfo(String name,String sn,String captain,String tel,String field,String memo){
    	this.name = name;
    	this.sn = sn;
    	this.captain = captain;
    	this.tel = tel;
    	this.field = field;
    	this.memo = memo;
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
	public String getCaptain() {
		return captain;
	}
	public String getTel() {
		return tel;
	}
	public String getField() {
		return field;
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
	public void setCaptain(String captain) {
		this.captain = captain;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setField(String field) {
		this.field = field;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
  
}
