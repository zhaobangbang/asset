package com.lansitec.app.jsondefs;

public class SingleWarnInfoRsp {
	private String code;
	private SingleWarnInfoRspData data;
	public SingleWarnInfoRsp(String code,SingleWarnInfoRspData data){
		this.code = code;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public SingleWarnInfoRspData getData() {
		return data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setData(SingleWarnInfoRspData data) {
		this.data = data;
	}
}
