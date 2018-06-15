package com.lans.dao.beans;

public class AcquiredemandInfo {
    private String devid = "test";
    private short cfgHB;
    private short sqlHB;
    private String mgsTimeBefore;
    public AcquiredemandInfo(){
    	
    }
    public AcquiredemandInfo(String devid,short cfgHB,short sqlHB,String mgsTimeBefore){
    	this.devid = devid;
    	this.cfgHB = cfgHB;
    	this.sqlHB = sqlHB;
    	this.mgsTimeBefore = mgsTimeBefore;
    }
	public String getDevid() {
		return devid;
	}
	public short getCfgHB() {
		return cfgHB;
	}
	public short getSqlHB() {
		return sqlHB;
	}
	public String getMgsTimeBefore() {
		return mgsTimeBefore;
	}
	public void setDevid(String devid) {
		this.devid = devid;
	}
	public void setCfgHB(short cfgHB) {
		this.cfgHB = cfgHB;
	}
	public void setSqlHB(short sqlHB) {
		this.sqlHB = sqlHB;
	}
	public void setMgsTimeBefore(String mgsTimeBefore) {
		this.mgsTimeBefore = mgsTimeBefore;
	}
	
}
