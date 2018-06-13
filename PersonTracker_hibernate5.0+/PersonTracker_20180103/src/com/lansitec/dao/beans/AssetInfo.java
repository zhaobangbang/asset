package com.lansitec.dao.beans;

import java.time.LocalDate;
import com.lansitec.enumlist.AssetStatus;
import com.lansitec.enumlist.AssetType;

public class AssetInfo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;//Ä¬ÈÏÖµÊÇnull
	private String name;
	private String sn;
	private AssetType type;
	private LocalDate purchase;
	private AssetStatus status;
	private String memo;
	private String deveui;
    public AssetInfo(String name,String sn,AssetType type,LocalDate purchase,AssetStatus status,String memo){
		this.name = name;
		this.sn = sn;
		this.type = type;
		this.purchase = purchase;
		this.status = status;
		this.memo = memo;
	}
	public AssetInfo(){
		
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
	public AssetType getType() {
		return type;
	}
	public LocalDate getPurchase() {
		return purchase;
	}
	public AssetStatus getStatus() {
		return status;
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
	public void setType(AssetType type) {
		this.type = type;
	}
	public void setPurchase(LocalDate purchase) {
		this.purchase = purchase;
	}
	public void setStatus(AssetStatus status) {
		this.status = status;
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
		return "AssetInfo [id=" + id + ", name=" + name + ", sn=" + sn + ", type=" + type + ", purchase=" + purchase
				+ ", status=" + status + ", memo=" + memo + ", deveui=" + deveui + "]";
	}
}
