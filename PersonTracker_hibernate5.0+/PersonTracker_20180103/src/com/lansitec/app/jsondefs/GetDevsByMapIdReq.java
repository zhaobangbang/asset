package com.lansitec.app.jsondefs;

public class GetDevsByMapIdReq {
	private String MapId;
	private String data;
	public GetDevsByMapIdReq(){
		
	}
	public String getMapId() {
		return MapId;
	}
	public String getData() {
		return data;
	}
	public void setMapId(String mapId) {
		MapId = mapId;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "GetDevsByMapIdReq [MapId=" + MapId + ", data=" + data + "]";
	}
}
