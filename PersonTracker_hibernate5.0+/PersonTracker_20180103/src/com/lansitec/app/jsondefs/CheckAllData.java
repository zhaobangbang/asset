package com.lansitec.app.jsondefs;

public class CheckAllData {
	private ChekDetailInfomation checkInformation;
	public CheckAllData(){
		
	}
    public CheckAllData(ChekDetailInfomation checkInformation){
		this.checkInformation = checkInformation;
	}
	public ChekDetailInfomation getCheckInformation() {
		return checkInformation;
	}
	public void setCheckInformation(ChekDetailInfomation checkInformation) {
		this.checkInformation = checkInformation;
	}
	@Override
	public String toString() {
		return "CheckAllData [checkInformation=" + checkInformation + "]";
	}
}
