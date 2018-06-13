package com.lansitec.dao.beans;

import java.time.LocalDateTime;

public class StatusRecord implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Integer id;
    private String deveui;
    private byte battery;
    private short rssi;
    private byte snr;
    private byte vib;
    private LocalDateTime time;
    public StatusRecord(){
    	
    }
    public StatusRecord(String deveui,byte battery,short rssi,byte snr,byte vib,LocalDateTime time){
    	this.deveui = deveui;
    	this.battery = battery;
    	this.rssi = rssi;
    	this.snr = snr;
    	this.vib = vib;
    	this.time = time;
    }
	public Integer getId() {
		return id;
	}
	public String getDeveui() {
		return deveui;
	}
	public byte getBattery() {
		return battery;
	}
	public short getRssi() {
		return rssi;
	}
	public byte getSnr() {
		return snr;
	}
	public byte getVib() {
		return vib;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	public void setBattery(byte battery) {
		this.battery = battery;
	}
	public void setRssi(short rssi) {
		this.rssi = rssi;
	}
	public void setSnr(byte snr) {
		this.snr = snr;
	}
	public void setVib(byte vib) {
		this.vib = vib;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "StatusRecord [id=" + id + ", deveui=" + deveui + ", battery=" + battery + ", rssi=" + rssi + ", snr="
				+ snr + ", vib=" + vib + ", time=" + time + "]";
	}
}
