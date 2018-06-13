package com.lansitec.app.openitf;

import com.lansitec.app.interfaces.init.LansiAppItfInit;
import com.lansitec.app.thirdparty.ILansiAppReqListener;
import com.lansitec.app.thirdparty.ILansiAppThirdpartyInst;

public class ThirdpartyProxy {
	ILansiAppThirdpartyInst init;
  
  public ThirdpartyProxy(){
	  init = null;
  }
  
  public void addLansitecReqListener(ILansiAppReqListener listener){
	   init = new LansiAppItfInit();
	   init.setLansiReq(listener);
 }
}
