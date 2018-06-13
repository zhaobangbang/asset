package com.lansitec.app.httpReq;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.lansi.util.RequestBodyReader;
import com.lansitec.app.interfaces.init.LansiAppItfInit;
import com.lansitec.app.jsondefs.HistoricalRouteReq;
import com.lansitec.app.thirdparty.ILansiAppReqListener;
import com.lansitec.app.thirdparty.ReqProcRslt;
import com.lansitec.enumlist.ReqProcRsltType;

@Controller
@RequestMapping("/tracker")
public class HistoricalRouteHandler {
	private Logger logger = LoggerFactory.getLogger(HistoricalRouteHandler.class);
	@RequestMapping("/HistoricalRoute")
	private void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String inputStr = null;
		HistoricalRouteReq msg = null;
		try {
			inputStr = RequestBodyReader.read(request);
			logger.info("read body iputStr {} in HistoricalRouteHandler",inputStr);
			msg = JSON.parseObject(inputStr,HistoricalRouteReq.class);
			String devId = msg.getDevId();
			if(null == (msg.getDevId()) || (msg.getDevId().equals(""))){
				logger.error("fail to get the devId {}",msg.getDevId());
				LansiAppHttpRspDefs.sendErrorRsp(response, Integer.toString(LansiAppHttpRspDefs.ERROR_INVALID_PARA));
        		return;
			}
			ILansiAppReqListener listener = LansiAppItfInit.getReqHandler();
    		if(null == listener){
				logger.error("no listener of app, ignored");
				LansiAppHttpRspDefs.sendErrorRsp(response, Integer.toString(LansiAppHttpRspDefs.ERROR_ACCESS_DENY));
				return;
			}
			logger.info("CheckInfomation by the devId {}",devId);
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
