package com.lansitec.app.jsondefs;

public class WarnInfoAllData {
	private WarnInfomation warnInfomation;
	public WarnInfoAllData (){
	}
	public WarnInfoAllData(WarnInfomation warnInfomation){
		this.warnInfomation = warnInfomation;
	}
	public WarnInfomation getWarnInfomation() {
		return warnInfomation;
	}
	public void setWarnInfomation(WarnInfomation warnInfomation) {
		this.warnInfomation = warnInfomation;
	}
	@Override
	public String toString() {
		return "WarnInfoAllData [warnInfomation=" + warnInfomation + "]";
	}
	
}
