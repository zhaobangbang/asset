package com.lansitec.springmvc.beans;

public class BeaconsTblBean {
	private String Major;
    private String Minor;
    private String sn;
    private String mapid;
    private String x;
    private String y;
    private String alias;
    private String alarmtype;
    private String rssi1;
    private String rssi2;
    private String a;
    private String n;
    private String oper;
	public String getMajor() {
		return Major;
	}
	public String getMinor() {
		return Minor;
	}
	public String getSn() {
		return sn;
	}
	public String getMapid() {
		return mapid;
	}
	public String getX() {
		return x;
	}
	public String getY() {
		return y;
	}
	public String getAlias() {
		return alias;
	}
	public String getAlarmtype() {
		return alarmtype;
	}
	public String getRssi1() {
		return rssi1;
	}
	public String getRssi2() {
		return rssi2;
	}
	public String getA() {
		return a;
	}
	public String getN() {
		return n;
	}
	public String getOper() {
		return oper;
	}
	public void setMajor(String major) {
		Major = major;
	}
	public void setMinor(String minor) {
		Minor = minor;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public void setMapid(String mapid) {
		this.mapid = mapid;
	}
	public void setX(String x) {
		this.x = x;
	}
	public void setY(String y) {
		this.y = y;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setAlarmtype(String alarmtype) {
		this.alarmtype = alarmtype;
	}
	public void setRssi1(String rssi1) {
		this.rssi1 = rssi1;
	}
	public void setRssi2(String rssi2) {
		this.rssi2 = rssi2;
	}
	public void setA(String a) {
		this.a = a;
	}
	public void setN(String n) {
		this.n = n;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
}
