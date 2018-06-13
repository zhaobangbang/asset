package com.lansitec.app.interfaces.init;

import com.lansitec.app.thirdparty.ILansiAppReqListener;
import com.lansitec.app.thirdparty.ILansiAppThirdpartyInst;

public class LansiAppItfInit implements ILansiAppThirdpartyInst {
	
   private static ILansiAppReqListener reqHandler;
   public LansiAppItfInit(){
	   
    }
 
	public static ILansiAppReqListener getReqHandler() {
	   return reqHandler;
   }

	@Override
	public void setLansiReq(ILansiAppReqListener listener) {
		reqHandler = listener;
	}


}
