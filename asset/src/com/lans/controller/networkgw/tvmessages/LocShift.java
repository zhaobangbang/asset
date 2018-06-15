package com.lans.controller.networkgw.tvmessages;

public class LocShift {
	double lati;//纬度
	double longi;//经度
	int time;
	float speed;//速度
	LocShift(double nextLongi, double nextLati, int time) {
		this.longi = nextLongi;
		this.lati = nextLati;
		this.time = time;
		speed = -1;
	}
	
	LocShift(double nextLongi, double nextLati, int time, float speed) {
		this.longi = nextLongi;
		this.lati = nextLati;
		this.time = time;
		this.speed = speed;
	}
	
	public double getLati() {
		return lati;
	}
	
	public double getLongi() {
		return longi;
	}
	
	public int getTime() {
		return time;
	}
	
	public float getSpeed() {
		return speed;
	}
}
