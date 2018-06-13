package com.lansitec.controller.networkgw.msghandler;

import java.util.Date;

public class NodePosition {
	double x;
	double y;
	Date time;
	
	public NodePosition(double x, double y, Date time) {
		this.x = x;
		this.y = y;
		this.time = time;
	}

	public double getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
