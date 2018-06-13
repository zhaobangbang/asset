package com.lansitec.dao.beans;

import com.lansitec.enumlist.WorkStatus;
import com.lansitec.enumlist.WorkerType;

public class WorkerInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String sn;
	private String tel;
	private WorkerType type;
	private WorkStatus status;
	private String picture;
	private String memo;
	private String deveui;
	public WorkerInfo(String name,String sn,String tel,WorkerType type,WorkStatus status,String picture,String memo){
		this.name = name;
		this.sn = sn;
		this.tel = tel;
		this.type = type;
		this.status = status;
		this.picture = picture;
		this.memo = memo;
	}
    public WorkerInfo(){
		
	}
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSn() {
		return sn;
	}
	public String getTel() {
		return tel;
	}
	public WorkerType getType() {
		return type;
	}
	public WorkStatus getStatus() {
		return status;
	}
	public String getPicture() {
		return picture;
	}
	public String getMemo() {
		return memo;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public void setType(WorkerType type) {
		this.type = type;
	}
	public void setStatus(WorkStatus status) {
		this.status = status;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDeveui() {
		return deveui;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	@Override
	public String toString() {
		return "WorkerInfo [id=" + id + ", name=" + name + ", sn=" + sn + ", tel=" + tel + ", type=" + type
				+ ", status=" + status + ", picture=" + picture + ", memo=" + memo + ", deveui=" + deveui + "]";
	}
}
