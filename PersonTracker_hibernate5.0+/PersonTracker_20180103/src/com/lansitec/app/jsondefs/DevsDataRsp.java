package com.lansitec.app.jsondefs;

public class DevsDataRsp {
	private String code;
	private DevsRspData data;
	
	public DevsDataRsp(){
		
	}
    public DevsDataRsp(String code,DevsRspData data){
		this.code = code;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public DevsRspData getData() {
		return data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setData(DevsRspData data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "DevsDataRsp [code=" + code + ", data=" + data + "]";
	}
}
