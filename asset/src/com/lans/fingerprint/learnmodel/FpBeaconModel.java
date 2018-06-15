package com.lans.fingerprint.learnmodel;

public class FpBeaconModel {
	int minor;
	double scanPercent;
	
	double pwr1Percent;
	double pwr2Percent;
	double pwr3Percent;
	double pwr4Percent;

	public FpBeaconModel() {
		super();
	}
	
	public FpBeaconModel(int minor) {
		this.minor = minor;
		scanPercent = 0;
		pwr1Percent = 0;
		pwr2Percent = 0;
		pwr3Percent = 0;
		pwr4Percent = 0;
	}
	
	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public double getPwr1Percent() {
		return pwr1Percent;
	}

	public void setPwr1Percent(double pwr1Percent) {
		this.pwr1Percent = pwr1Percent;
	}

	public double getPwr2Percent() {
		return pwr2Percent;
	}

	public void setPwr2Percent(double pwr2Percent) {
		this.pwr2Percent = pwr2Percent;
	}

	public double getPwr3Percent() {
		return pwr3Percent;
	}

	public void setPwr3Percent(double pwr3Percent) {
		this.pwr3Percent = pwr3Percent;
	}

	public double getPwr4Percent() {
		return pwr4Percent;
	}

	public void setPwr4Percent(double pwr4Percent) {
		this.pwr4Percent = pwr4Percent;
	}

	public double getScanPercent() {
		return scanPercent;
	}

	public void setScanPercent(double scanPercent) {
		this.scanPercent = scanPercent;
	}	
}
