package com.lansitec.controller.networkgw.msghandler;

import com.lansitec.dao.beans.Beacons;

public class PersonTrackerBeanconNode {
	Beacons beacons;
	int rssi;
	
	public PersonTrackerBeanconNode(Beacons beacons,int rssi){
		this.beacons = beacons;
		this.rssi = rssi;
	}

	public Beacons getBeacons() {
		return beacons;
	}

	public int getRssi() {
		return rssi;
	}
	
}
