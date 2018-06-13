package com.lansitec.app.jsondefs;

import java.util.ArrayList;
import java.util.List;

public class DevsRspData {
	private String datetime;
	private List<DeveuiName> devsList = new ArrayList<DeveuiName>();
    
	public DevsRspData(){
		
	}
	
    public DevsRspData(String datetime,List<DeveuiName> devsList){
		this.datetime = datetime;
		this.devsList = devsList;
	}

	public String getDatetime() {
		return datetime;
	}

	public List<DeveuiName> getDevsList() {
		return devsList;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public void setDevsList(List<DeveuiName> devsList) {
		this.devsList = devsList;
	}

	@Override
	public String toString() {
		return "DevsRspData [datetime=" + datetime + ", devsList=" + devsList + "]";
	}
    
}
