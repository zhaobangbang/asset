package com.lans.fingerprint.learnmodel;

import java.util.LinkedList;
import java.util.List;


public class FpKnownPos {
	double x;
	double y;
	
	int learnCnt;
	List<FpScannedBeacon> scanList;
	
	public FpKnownPos(double x, double y) {
		this.x = x;
		this.y = y;
		
		learnCnt = 0;
		scanList = new LinkedList<FpScannedBeacon>();
	}
	
	public void incLearnCnt() {
		learnCnt++;
	}
	
	public int getLearnCnt() {
		return learnCnt;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public List<FpScannedBeacon> getScanList() {
		return scanList;
	}

	public void addScannedBeacon(int minor, int power) {
		for (FpScannedBeacon beacon : scanList) {
			if (beacon.getMinor() == minor) {
				beacon.addOnce(power);
				return;
			}
		}
		
		//add a new instance here
		FpScannedBeacon newBeacon = new FpScannedBeacon(minor);
		newBeacon.addOnce(power);
		scanList.add(newBeacon);
	}
}
