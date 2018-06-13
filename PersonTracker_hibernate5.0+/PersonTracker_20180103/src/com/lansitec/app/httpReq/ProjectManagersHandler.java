package com.lansitec.app.httpReq;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lansitec.app.interfaces.init.LansiAppItfInit;
import com.lansitec.app.jsondefs.LoginReq;
import com.lansitec.app.thirdparty.ILansiAppReqListener;
import com.lansitec.app.thirdparty.ReqProcRslt;
import com.lansitec.enumlist.ReqProcRsltType;

@Controller
@RequestMapping("/tracker")
public class ProjectManagersHandler{
    private Logger logger = LoggerFactory.getLogger(ProjectManagersHandler.class);
    @RequestMapping(value="projectManagersLogin",method=RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    	//String inputStr = null;
    	LoginReq systemManagerReq = null;
    	try {
			/*inputStr = RequestBodyReader.read(request);
			logger.info("read app body inputStr {}",inputStr);
			systemManagerReq = JSON.parseObject(inputStr, LoginReq.class);*/
    		String projUsername = request.getParameter("username");
    		//String projUsername = request.getHeader("username");
    		if((null == projUsername) || (projUsername.equals(""))){
        		logger.error("fail to get the projectManagerName {}",projUsername);
        		LansiAppHttpRspDefs.sendErrorRsp(response, Integer.toString(LansiAppHttpRspDefs.ERROR_INVALID_PARA));
        		return;
        	}
    		String password = request.getParameter("password");
    		//String password =request.getHeader("password");
        	if((null == password) || (password.equals(""))){
        		logger.error("fail to get the password {}",password);
        		LansiAppHttpRspDefs.sendErrorRsp(response, Integer.toString(LansiAppHttpRspDefs.ERROR_INVALID_PARA));
        		return;
        	}
    		systemManagerReq = new LoginReq(projUsername, password);
			ILansiAppReqListener listener = LansiAppItfInit.getReqHandler();
			if(null == listener){
				logger.error("no listener of app, ignored");
				LansiAppHttpRspDefs.sendErrorRsp(response, Integer.toString(LansiAppHttpRspDefs.ERROR_ACCESS_DENY));
				return;
			}
			logger.info("login projectManagerName {} password {}",projUsername,password);
			ReqProcRslt rslt = new ReqProcRslt();
			ReqProcRsltType type = listener.handlerReq(systemManagerReq, rslt);
			LansiAppHttpRspDefs.sendProcRsltRsp(response, type, rslt);
		} catch (Exception e) {
			logger.error("read app parament error {} - {}", e.getMessage());
			LansiAppHttpRspDefs.sendErrorRsp(response, Integer.toString(LansiAppHttpRspDefs.ERROR_INTERNAL));
			return;
		}

	}

	
    @RequestMapping(value="ProjectManagersHandler",method=RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	doGet(request,response);
	   
	}

}
