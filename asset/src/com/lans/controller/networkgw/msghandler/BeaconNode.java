package com.lans.controller.networkgw.msghandler;

import com.lans.dao.beans.Beacons;

public class BeaconNode {
	Beacons beacon;
	int rssi;
		
	public BeaconNode(Beacons beacon, int rssi) {
		this.beacon = beacon;
		this.rssi = rssi;
	}
		
	public Beacons getBeacon() {
		return beacon;
	}
		
	public int getRssi() {
		return rssi;
	}
		
	public boolean isMidFloor() {    
	    //Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    //return !pattern.matcher(beacon.getFlor()).matches();
		if (beacon.getPostype().equals("Â¥µÀ")) {
			return true;
		}
		return false;
	}
}
