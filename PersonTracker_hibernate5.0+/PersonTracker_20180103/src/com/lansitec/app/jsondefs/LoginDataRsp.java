package com.lansitec.app.jsondefs;

public class LoginDataRsp {
	private String code;
	private String data;
	
	public LoginDataRsp(){
		
	}
	
    public LoginDataRsp(String code,String data){
		this.code  = code;
		this.data = data;
	}
	
	public String getCode() {
		return code;
	}
	public String getData() {
		return data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setData(String data) {
		this.data = data;
	}

}
