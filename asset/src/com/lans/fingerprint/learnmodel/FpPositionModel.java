package com.lans.fingerprint.learnmodel;

import java.util.LinkedList;
import java.util.List;

public class FpPositionModel {
	double x;
	double y;
	
	List<FpBeaconModel> bModelList;

	public FpPositionModel() {
		super();
	}
	
	public FpPositionModel(double x, double y) {
		this.x = x;
		this.y = y;
		bModelList = new LinkedList<FpBeaconModel>();
	}
	
	public void addBeaconModel(FpBeaconModel model) {
		bModelList.add(model);
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

	public List<FpBeaconModel> getbModelList() {
		return bModelList;
	}

	public void setbModelList(List<FpBeaconModel> bModelList) {
		this.bModelList = bModelList;
	}	
	
}
