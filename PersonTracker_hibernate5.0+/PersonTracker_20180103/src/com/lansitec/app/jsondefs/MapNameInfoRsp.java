package com.lansitec.app.jsondefs;

public class MapNameInfoRsp {
	private String code;
	private MapNameInfoRspData data;
	public MapNameInfoRsp(String code,MapNameInfoRspData data){
		this.code = code;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public MapNameInfoRspData getData() {
		return data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setData(MapNameInfoRspData data) {
		this.data = data;
	}
}
