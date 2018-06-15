package com.lans.fingerprint.learnmodel;

public class FpPosMatchRslt {
	double x;
	double y;
	double possibility;
	
	public FpPosMatchRslt() {
		this.x = 0;
		this.y = 0;
		this.possibility = 0;
	}

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getPossibility() {
		return possibility;
	}
	
	public void setPossibility(double possibility) {
		this.possibility = possibility;
	}
	
	
}
