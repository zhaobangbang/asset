package com.lans.controller.networkgw.tvmessages;

public class ScannedBeacon {
	int major;
	int minor;
	int rssi;
	
	public ScannedBeacon(int major, int minor, int rssi) {
		this.major = major;
		this.minor = minor;
		this.rssi = rssi;
	}
	
	public int getMajor() {
		return major;
	}
	
	public int getMinor() {
		return minor;
	}
	
	public int getRssi() {
		return rssi;
	}
}
