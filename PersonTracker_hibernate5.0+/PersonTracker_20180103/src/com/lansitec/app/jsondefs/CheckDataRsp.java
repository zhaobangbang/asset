package com.lansitec.app.jsondefs;

public class CheckDataRsp {
	private String code;
	private CheckInfoRspData data;
	public CheckDataRsp(){
		
	}
	public CheckDataRsp(String code,CheckInfoRspData data){
		this.code = code;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public CheckInfoRspData getData() {
		return data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setData(CheckInfoRspData data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "CheckDataRsp [code=" + code + ", data=" + data + "]";
	}
}
