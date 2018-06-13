package com.lansitec.app.jsondefs;

public class MapNameAndId {
	private String mapName;
	private String mapId;
	public MapNameAndId(String mapName,String mapId){
		this.mapName = mapName;
		this.mapId = mapId;
	}
	public String getMapName() {
		return mapName;
	}
	public String getMapId() {
		return mapId;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
}
