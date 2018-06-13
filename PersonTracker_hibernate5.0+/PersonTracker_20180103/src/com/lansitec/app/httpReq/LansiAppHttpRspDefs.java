package com.lansitec.app.httpReq;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lansi.thirdparty.zwwl.jsondefs.RspNoData;
import com.lansi.thirdparty.zwwl.jsondefs.RspOkData;
import com.lansitec.app.thirdparty.ReqProcRslt;
import com.lansitec.enumlist.ReqProcRsltType;


public class LansiAppHttpRspDefs {
	private static Logger logger = LoggerFactory.getLogger(LansiAppHttpRspDefs.class);
	public static int ERROR_AUTH = 2001;
	public static int ERROR_TOKEN_EXPIRE = 2002;
	public static int ERROR_ACCESS_DENY = 2003;
	public static int ERROR_API_PATH = 2004;
	public static int ERROR_INVALID_PARA = 2005;//无效参数
	public static int ERROR_INTERNAL = 2006;//系统内部错误
	public static int NOTADD_NO_RESP = 2007;//设备未添加
	public static int NOEXIST_NO_RESP = 20101;//用户不存在
	public static void responseWrite(HttpServletResponse response, String rspStr) throws IOException {
		logger.info("http response: {}", rspStr);
		response.getWriter().write(rspStr);
	}
	
	public static void sendOkRsp(HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		/*StringBuffer sb = new StringBuffer();  
		sb.append("{");
		sb.append("\"code\" : \"0\",");
		sb.append("\"data\" : {");
		sb.append("}");
		sb.append("}");
		response.getWriter().write(sb.toString());*/
		RspOkData okRsp = new RspOkData("0","");
		String rspStr = JSON.toJSONString(okRsp);
		responseWrite(response, rspStr);
	}
	
	public static void sendErrorRsp(HttpServletResponse response, String err) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		RspNoData errRsp = new RspNoData(err);
		String rspStr = JSON.toJSONString(errRsp);
		responseWrite(response, rspStr);
	}
	
	public static void sendInternalErrorRsp(HttpServletResponse response, String Internalerr) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		RspNoData errRsp = new RspNoData(Internalerr);
		String rspStr = JSON.toJSONString(errRsp);
		responseWrite(response, rspStr);
	}
	
	public static void sendRsp(HttpServletResponse response, Object rspObj) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		String rspStr = JSON.toJSONString(rspObj);
		responseWrite(response, rspStr);
	}
	
	public static void sendStrRsp(HttpServletResponse response, String rspStr) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		String rsp = JSON.toJSONString(rspStr);
		responseWrite(response, rsp);
	}
	
	public static void sendProcRsltRsp(HttpServletResponse response, ReqProcRsltType type, ReqProcRslt rslt) throws Exception {
		switch (type) {
			case SUCC_NO_RESP:
				sendOkRsp(response);
				break;
			case FAIL_NO_RESP:
				sendErrorRsp(response, Integer.toString(ERROR_INVALID_PARA));
				break;
			case NOTADD_NO_RESP:
				sendErrorRsp(response, Integer.toString(NOTADD_NO_RESP));
			    break;
			case NOEXIST_NO_RESP:
				sendErrorRsp(response, Integer.toString(NOEXIST_NO_RESP));
			    break;
			case ERROR_RESP_OBJ:
				sendInternalErrorRsp(response, Integer.toString(ERROR_INTERNAL));
				break;
			case SUCC_RESP_OBJ:
				sendRsp(response, rslt.getResp());
				break;
			case SUCC_RESP_STR:
				sendStrRsp(response, rslt.getRespStr());
				break;
			case FAIL_RESP_STR:
				sendStrRsp(response, rslt.getRespStr());
				break;
			default:
				sendErrorRsp(response, Integer.toString(ERROR_INTERNAL));
				break;
		}
	}
}
