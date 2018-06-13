package com.lansitec.app.jsondefs;

import java.util.ArrayList;
import java.util.List;

public class MapNameInfoRspData {
	private String datetime;
	private List<MapNameAndId> mapsList = new ArrayList<MapNameAndId>();
	public MapNameInfoRspData(){
		
	}
    public MapNameInfoRspData(String datetime,List<MapNameAndId> mapsList){
    	this.datetime = datetime;
    	this.mapsList = mapsList;
    }
	public String getDatetime() {
		return datetime;
	}
	public List<MapNameAndId> getMapsList() {
		return mapsList;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public void setMapsList(List<MapNameAndId> mapsList) {
		this.mapsList = mapsList;
	}
	@Override
	public String toString() {
		return "MapNameInfoRspData [datetime=" + datetime + ", mapsList=" + mapsList + "]";
	}

}
