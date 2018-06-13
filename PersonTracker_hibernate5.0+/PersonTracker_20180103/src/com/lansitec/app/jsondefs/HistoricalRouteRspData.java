package com.lansitec.app.jsondefs;

import java.util.ArrayList;
import java.util.List;

public class HistoricalRouteRspData {
	private String datetime;
	private List<HistoricalRouteDataList> positionesList = new ArrayList<HistoricalRouteDataList>();
	public HistoricalRouteRspData(){
		
	}
    public HistoricalRouteRspData(String datetime,List<HistoricalRouteDataList> positionesList){
		this.datetime = datetime;
		this.positionesList = positionesList;
	}
	public String getDatetime() {
		return datetime;
	}
	public List<HistoricalRouteDataList> getPositionesList() {
		return positionesList;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public void setPositionesList(List<HistoricalRouteDataList> positionesList) {
		this.positionesList = positionesList;
	}
	@Override
	public String toString() {
		return "HistoricalRouteRspData [datetime=" + datetime + ", positionesList=" + positionesList + "]";
	}
}
