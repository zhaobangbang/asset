package com.lansitec.app.jsondefs;

import java.util.ArrayList;
import java.util.List;

public class CheckInfoRspData {
	private List<CheckIncludLittleData> devs  = new ArrayList<CheckIncludLittleData>();
	public CheckInfoRspData(){
		
	}
    public CheckInfoRspData(List<CheckIncludLittleData> devs){
		this.devs = devs;
	}

	public List<CheckIncludLittleData> getDevs() {
		return devs;
	}

	public void setDevs(List<CheckIncludLittleData> devs) {
		this.devs = devs;
	}

	@Override
	public String toString() {
		return "CheckInfoRspData [devs=" + devs + "]";
	}

}
