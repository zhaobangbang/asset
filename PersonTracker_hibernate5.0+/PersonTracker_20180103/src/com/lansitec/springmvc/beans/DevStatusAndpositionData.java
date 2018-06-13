package com.lansitec.springmvc.beans;

public class DevStatusAndpositionData {
	private String deveui;
	//status
	private String battery;
	private String rssi;
	private String snr;
	private String vib;
	//position
	private String mapid;
	private String x;
	private String y;
	//devInfo lastTime
	private String time;
	private String devType;
	//dev status 
	private String status;
	public DevStatusAndpositionData(){
		
	}
	
    public DevStatusAndpositionData(String deveui,String battery,String rssi,String snr,String vib,String mapid,String x,String y,String time,String devType,String status){
		this.deveui = deveui;
		this.battery = battery;
		this.rssi = rssi;
		this.snr = snr;
		this.vib = vib;
		this.mapid = mapid;
		this.x = x;
		this.y = y;
		this.time = time;
		this.devType = devType;
		this.status = status;
	}

	public String getDeveui() {
		return deveui;
	}

	public String getBattery() {
		return battery;
	}

	public String getRssi() {
		return rssi;
	}

	public String getSnr() {
		return snr;
	}

	public String getVib() {
		return vib;
	}

	public String getMapid() {
		return mapid;
	}

	public String getX() {
		return x;
	}

	public String getY() {
		return y;
	}

	public String getTime() {
		return time;
	}

	public String getDevType() {
		return devType;
	}

	public String getStatus() {
		return status;
	}

	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public void setRssi(String rssi) {
		this.rssi = rssi;
	}

	public void setSnr(String snr) {
		this.snr = snr;
	}

	public void setVib(String vib) {
		this.vib = vib;
	}

	public void setMapid(String mapid) {
		this.mapid = mapid;
	}

	public void setX(String x) {
		this.x = x;
	}

	public void setY(String y) {
		this.y = y;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
