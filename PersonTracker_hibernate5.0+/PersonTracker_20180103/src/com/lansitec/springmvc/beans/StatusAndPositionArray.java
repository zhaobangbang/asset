package com.lansitec.springmvc.beans;

import java.util.ArrayList;
import java.util.List;

public class StatusAndPositionArray {
	private List<DevStatusAndpositionData> devData = new ArrayList<DevStatusAndpositionData>();
	public StatusAndPositionArray(){
		
	}
	public StatusAndPositionArray(List<DevStatusAndpositionData> devData){
		this.devData = devData;
	}
	public List<DevStatusAndpositionData> getDevData() {
		return devData;
	}
	public void setDevData(List<DevStatusAndpositionData> devData) {
		this.devData = devData;
	}

}
