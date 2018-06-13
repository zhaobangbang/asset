package com.lansitec.app.jsondefs;

public class HistoricalRouteDataList {
	private String mapid;
	private String gpsLat;
	private String gpsLong;
	private String time;
	public HistoricalRouteDataList(){
		
	}
    public HistoricalRouteDataList(String mapid,String gpsLat,String gpsLong,String time){
		this.mapid = mapid;
		this.gpsLat = gpsLat;
		this.gpsLong = gpsLong;
		this.time = time;
	}
	public String getMapid() {
		return mapid;
	}
	public String getGpsLat() {
		return gpsLat;
	}
	public String getGpsLong() {
		return gpsLong;
	}
	public String getTime() {
		return time;
	}
	public void setMapid(String mapid) {
		this.mapid = mapid;
	}
	public void setGpsLat(String gpsLat) {
		this.gpsLat = gpsLat;
	}
	public void setGpsLong(String gpsLong) {
		this.gpsLong = gpsLong;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "HistoricalRouteDataList [mapid=" + mapid + ", gpsLat=" + gpsLat + ", gpsLong=" + gpsLong + ", time="
				+ time + "]";
	}
}
