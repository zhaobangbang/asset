package com.lansitec.dao.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.lansitec.enumlist.Devtype;
public class DevInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String deveui;
	private Devtype devtype;
	private String field;
	private LocalDate recv;
	private String owner;
	private String alias;
	private String battery;
	private short rssi;
	private float snr;
	private LocalDateTime statustime;
	private String mapid;
	private LocalDateTime entertime;
	private LocalDateTime exittime;
	private String memo;
	public DevInfo(){
		
	}
    public DevInfo(String deveui,Devtype devtype,String field,LocalDate recv,String owner,String mapid,String memo){
		this.deveui = deveui;
		this.devtype = devtype;
		this.field = field;
		this.recv = recv;
		this.owner = owner;
		this.mapid = mapid;
		this.memo = memo;
	}
	public Integer getId() {
		return id;
	}
	public String getDeveui() {
		return deveui;
	}
	public Devtype getDevtype() {
		return devtype;
	}
	public String getField() {
		return field;
	}
	public LocalDate getRecv() {
		return recv;
	}
	public String getOwner() {
		return owner;
	}
	public String getAlias() {
		return alias;
	}
	public String getBattery() {
		return battery;
	}
	public short getRssi() {
		return rssi;
	}
	public float getSnr() {
		return snr;
	}
	public LocalDateTime getStatustime() {
		return statustime;
	}
	public String getMapid() {
		return mapid;
	}
	public LocalDateTime getEntertime() {
		return entertime;
	}
	public LocalDateTime getExittime() {
		return exittime;
	}
	public String getMemo() {
		return memo;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	public void setDevtype(Devtype devtype) {
		this.devtype = devtype;
	}
	public void setField(String field) {
		this.field = field;
	}
	public void setRecv(LocalDate recv) {
		this.recv = recv;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setBattery(String battery) {
		this.battery = battery;
	}
	public void setRssi(short rssi) {
		this.rssi = rssi;
	}
	public void setSnr(float snr) {
		this.snr = snr;
	}
	public void setStatustime(LocalDateTime statustime) {
		this.statustime = statustime;
	}
	public void setMapid(String mapid) {
		this.mapid = mapid;
	}
	public void setEntertime(LocalDateTime entertime) {
		this.entertime = entertime;
	}
	public void setExittime(LocalDateTime exittime) {
		this.exittime = exittime;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Override
	public String toString() {
		return "DevInfo [id=" + id + ", deveui=" + deveui + ", devtype=" + devtype + ", field=" + field + ", recv="
				+ recv + ", owner=" + owner + ", alias=" + alias + ", battery=" + battery + ", rssi=" + rssi + ", snr="
				+ snr + ", statustime=" + statustime + ", mapid=" + mapid + ", entertime=" + entertime + ", exittime="
				+ exittime + ", memo=" + memo + "]";
	}
}
