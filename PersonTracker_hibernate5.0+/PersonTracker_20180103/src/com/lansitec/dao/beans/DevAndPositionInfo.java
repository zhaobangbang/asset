package com.lansitec.dao.beans;

public class DevAndPositionInfo implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String devId;
    String gpsLong;
	String gpsLat;
	public DevAndPositionInfo(){
		
	}
	public DevAndPositionInfo(String devId,String gpsLong,String gpsLat){
		this.devId = devId;
		this.gpsLong = gpsLong;
		this.gpsLat = gpsLat;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getGpsLong() {
		return gpsLong;
	}
	public void setGpsLong(String gpsLong) {
		this.gpsLong = gpsLong;
	}
	public String getGpsLat() {
		return gpsLat;
	}
	public void setGpsLat(String gpsLat) {
		this.gpsLat = gpsLat;
	}
	
}
