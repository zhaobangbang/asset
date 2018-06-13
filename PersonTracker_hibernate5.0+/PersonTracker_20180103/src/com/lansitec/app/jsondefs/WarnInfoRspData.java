package com.lansitec.app.jsondefs;

import java.util.ArrayList;
import java.util.List;

public class WarnInfoRspData {
	private List<WarnInfoLittleData> devs = new ArrayList<WarnInfoLittleData>();
	public WarnInfoRspData(){
		
	}
	public WarnInfoRspData(List<WarnInfoLittleData> devs){
		this.devs = devs;
	}
	public List<WarnInfoLittleData> getDevs() {
		return devs;
	}
	public void setDevs(List<WarnInfoLittleData> devs) {
		this.devs = devs;
	}
	@Override
	public String toString() {
		return "WarnInfoRspData [devs=" + devs + "]";
	}
	
}
