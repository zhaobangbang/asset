package com.lans.beans;
/*
 * 添加多个轨迹点到百度服务器时，需要构造json
 * 
 */
public class BaiduTrackPoint {
	String entity_name;
	double latitude;
	double longitude;
	String loc_time;
    String coord_type;
	
	public BaiduTrackPoint(String entity_name, double yGPS, double xGPS,String loc_time, String coord_type ){
		this.entity_name = entity_name;
		this.latitude = yGPS;
		this.longitude = xGPS;
		this.loc_time = loc_time;
		this.coord_type = coord_type;
	}
	
	public BaiduTrackPoint(double baidulati, double baidulong){
		this.latitude = baidulati;
		this.longitude = baidulong;
		
	}

	public String getEntity_name() {
		return entity_name;
	}

	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLoc_time() {
		return loc_time;
	}

	public void setLoc_time(String loc_time) {
		this.loc_time = loc_time;
	}

	public String getCoord_type() {
		return coord_type;
	}

	public void setCoord_type(String coord_type) {
		this.coord_type = coord_type;
	}
}
