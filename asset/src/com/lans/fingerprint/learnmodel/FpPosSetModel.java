package com.lans.fingerprint.learnmodel;

import java.util.LinkedList;
import java.util.List;

public class FpPosSetModel {
	List<FpPositionModel> modelList;

	public FpPosSetModel() {
		modelList = new LinkedList<FpPositionModel>();
	}
	
	public List<FpPositionModel> getModelList() {
		return modelList;
	}

	public void setModelList(List<FpPositionModel> modelList) {
		this.modelList = modelList;
	}
	
	public void addPositionModel(FpPositionModel posModel) {
		for (FpPositionModel posm : modelList) {
			if ((posm.getX() == posModel.getX()) && (posm.getY() == posModel.getY())) {
				modelList.remove(posm);
				break;
			}
		}
		
		modelList.add(posModel);
	}
}
