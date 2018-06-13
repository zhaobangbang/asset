package com.lansitec.app.jsondefs;

public class HistoricalRouteRsp {
	private String code;
	private HistoricalRouteRspData data;
	public HistoricalRouteRsp(){
		
	}
    public HistoricalRouteRsp(String code,HistoricalRouteRspData data){
		this.code = code;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public HistoricalRouteRspData getData() {
		return data;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setData(HistoricalRouteRspData data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "HistoricalRouteRsp [code=" + code + ", data=" + data + "]";
	}
}
