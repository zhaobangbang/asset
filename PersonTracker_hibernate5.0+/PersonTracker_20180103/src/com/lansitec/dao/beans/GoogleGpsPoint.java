package com.lansitec.dao.beans;


public class GoogleGpsPoint {
	private String latitude;
	private String longitude;
	private String googletime;
	
	public GoogleGpsPoint(String latitude,String longitude,String googletime){
		this.latitude = latitude;
		this.longitude = longitude;
		this.googletime = googletime;
		
	}
	public GoogleGpsPoint(String latitude,String longitude){
		this.latitude = latitude;
		this.longitude = longitude;
		
	}
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getGoogletime() {
		return googletime;
	}

	public void setGoogletime(String googletime) {
		this.googletime = googletime;
	}

}
