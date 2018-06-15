package com.lans.dao.beans;

import java.io.Serializable;
import java.util.Date;

public class BlueToothBraceletData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String deveui;
	private String step;
	private String caloric;
	private float gpsLat;
	private float gpsLong;
	private short heartBeat;
	private short systolicPressure;
	private short  diastolicPressure;
	private String distance;
	private Date time;
	private String battery;
	private short rssi;
	private Date batteryTime;
	private Date distanceTime;
	public BlueToothBraceletData(){
		
	}
	
    public BlueToothBraceletData(String deveui,String step,String caloric,float gpsLat,float gpsLong,short heartBeat,
                     short systolicPressure,short  diastolicPressure,String distance,Date time,String battery,short rssi,
                     Date batteryTime,Date distanceTime){
		this.deveui = deveui;
		this.step = step;
    	this.caloric = caloric;
		this.gpsLat = gpsLat;
		this.gpsLong = gpsLong;
		this.heartBeat = heartBeat;
    	this.systolicPressure = systolicPressure;
    	this.diastolicPressure = diastolicPressure;
		this.distance = distance;
		this.time = time;
		this.battery = battery;
		this.rssi = rssi;
		this.battery = battery;
		this.distance = distance;
    }

	public Integer getId() {
		return id;
	}

	public String getDeveui() {
		return deveui;
	}

	public String getStep() {
		return step;
	}

	public String getCaloric() {
		return caloric;
	}

	public float getGpsLat() {
		return gpsLat;
	}

	public float getGpsLong() {
		return gpsLong;
	}

	public short getHeartBeat() {
		return heartBeat;
	}

	public short getSystolicPressure() {
		return systolicPressure;
	}

	public short getDiastolicPressure() {
		return diastolicPressure;
	}

	public String getDistance() {
		return distance;
	}

	public Date getTime() {
		return time;
	}

	public String getBattery() {
		return battery;
	}

	public short getRssi() {
		return rssi;
	}

	public Date getBatteryTime() {
		return batteryTime;
	}

	public Date getDistanceTime() {
		return distanceTime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public void setCaloric(String caloric) {
		this.caloric = caloric;
	}

	public void setGpsLat(float gpsLat) {
		this.gpsLat = gpsLat;
	}

	public void setGpsLong(float gpsLong) {
		this.gpsLong = gpsLong;
	}

	public void setHeartBeat(short heartBeat) {
		this.heartBeat = heartBeat;
	}

	public void setSystolicPressure(short systolicPressure) {
		this.systolicPressure = systolicPressure;
	}

	public void setDiastolicPressure(short diastolicPressure) {
		this.diastolicPressure = diastolicPressure;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public void setRssi(short rssi) {
		this.rssi = rssi;
	}

	public void setBatteryTime(Date batteryTime) {
		this.batteryTime = batteryTime;
	}

	public void setDistanceTime(Date distanceTime) {
		this.distanceTime = distanceTime;
	}
    
}
