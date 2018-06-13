package com.lansitec.app.thirdparty;

public class ReqProcRslt {
	Object resp;

	public ReqProcRslt() {
		resp = null;
	}
	
	public Object getResp() {
		return resp;
	}

	public void setResp(Object resp) {
		this.resp = resp;
	}
	
	public String getRespStr(){
		return (String)resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}
}
