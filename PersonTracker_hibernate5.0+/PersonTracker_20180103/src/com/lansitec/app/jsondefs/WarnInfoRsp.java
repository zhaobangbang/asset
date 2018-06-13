package com.lansitec.app.jsondefs;

public class WarnInfoRsp {
	private String code;
	private WarnInfoRspData data;
	public WarnInfoRsp(){
		
	}
    public WarnInfoRsp(String code,WarnInfoRspData data){
		this.code = code;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public WarnInfoRspData getData() {
		return data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setData(WarnInfoRspData data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "WarnInfoRsp [code=" + code + ", data=" + data + "]";
	}
}
