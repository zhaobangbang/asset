package com.lansitec.app.httpReq;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.lansitec.app.interfaces.init.LansiAppItfInit;
import com.lansitec.app.jsondefs.SingleWarnInfoReq;
import com.lansitec.app.thirdparty.ILansiAppReqListener;
import com.lansitec.app.thirdparty.ReqProcRslt;
import com.lansitec.enumlist.ReqProcRsltType;
import com.lansitec.util.RequestBodyReader;

@Controller
@RequestMapping("/tracker")
public class SingleWarnInfoHandler {
	private Logger logger = LoggerFactory.getLogger(SingleWarnInfoHandler.class);
	@RequestMapping(value="SingleWarnInfo",method=RequestMethod.POST)
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		SingleWarnInfoReq msg = null;
		String inputStr = null;
		try {
			inputStr = RequestBodyReader.read(request);
			logger.info("read body inputStr {}",inputStr);
			msg = JSON.parseObject(inputStr, SingleWarnInfoReq.class);
			String deveui = msg.getDevId();
			if((null == deveui) || (deveui.equals(""))){
				logger.error("fail to get the deveui {}",deveui);
				LansiAppHttpRspDefs.sendErrorRsp(response, Integer.toString(LansiAppHttpRspDefs.ERROR_INVALID_PARA));
        		return;
			}
			ILansiAppReqListener listener = LansiAppItfInit.getReqHandler();
    		if(null == listener){
				logger.error("no listener of app, ignored");
				LansiAppHttpRspDefs.sendErrorRsp(response, Integer.toString(LansiAppHttpRspDefs.ERROR_ACCESS_DENY));
				return;
			}
			logger.info("WarnInfomation by the deveui {}",deveui);
			ReqProcRslt rslt = new ReqProcRslt();
			ReqProcRsltType type = listener.handlerReq(msg, rslt);
			LansiAppHttpRspDefs.sendProcRsltRsp(response, type, rslt);
		} catch (Exception e) {
			logger.error("read app body error {} - {}", e.getMessage(),inputStr);
			LansiAppHttpRspDefs.sendErrorRsp(response, Integer.toString(LansiAppHttpRspDefs.ERROR_INTERNAL));
			return;
		}
	}

}
