package com.lansitec.app.jsondefs;

public class SingleCheckInfoRsp {
	private String code;
	private SingleCheckInfoRspData data;
	public SingleCheckInfoRsp(String code,SingleCheckInfoRspData data){
		this.code = code;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public SingleCheckInfoRspData getData() {
		return data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setData(SingleCheckInfoRspData data) {
		this.data = data;
	}
}
