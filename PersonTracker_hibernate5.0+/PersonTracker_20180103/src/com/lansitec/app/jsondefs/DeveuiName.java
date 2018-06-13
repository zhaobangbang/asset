package com.lansitec.app.jsondefs;

public class DeveuiName {
	private String deveui;
	
	public DeveuiName(){
		
	}
	
    public DeveuiName(String deveui){
		this.deveui = deveui;
	}

	public String getDeveui() {
		return deveui;
	}

	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}

	@Override
	public String toString() {
		return "DeveuiName [deveui=" + deveui + "]";
	}
}
