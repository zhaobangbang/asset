package com.lans.dao.beans;

public class ZwwlDevsUseInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	String username;
	String deveui;
	String alias;
	public ZwwlDevsUseInfo(){
		
	}
	
    public ZwwlDevsUseInfo(String username,String deveui,String alias){
		this.username = username;
		this.deveui = deveui;
		this.alias = alias;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeveui() {
		return deveui;
	}

	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
    
}
