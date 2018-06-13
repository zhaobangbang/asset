package com.lansitec.thirdparty.app.infoHandler;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansitec.util.DecriptUtil;
import com.lansitec.util.HttpConnWapper;



public class ZwwlAuth {
	static Logger logger = LoggerFactory.getLogger(ZwwlAuth.class);
	public static String addDevUrl = "/zwwl/addDevice";
	public static String delDevUrl ="/zwwl/delDevice";
	public static String controlDevUrl="/zwwl/controlDevice";
	
	public static String retWholeSchemaUrl="/zwwl/retrieveWholeSchema";
	public static String retDataSchemaUrl="/zwwl/retrieveDataSchema";
	public static String retEventSchemaUrl="/zwwl/retrieveEventSchema"; 
	public static String retFuncSchemaUrl="/zwwl/retrieveFuncSchema";
	
	public static String retNewDataUrl="/zwwl/retrieveNewestData";
	public static String batRetNewDataUrl="/zwwl/batchRetrieveNewestData";
	public static String retNewFuncStatusUrl="/zwwl/retrieveNewestFuncStatus";
	public static String batRetNewFuncStatus="/zwwl/batchRetrieveNewestFuncStatus";
	
	
	static String token = "9d9145d0da3d0a07b8f8bdbab375e7cc853ac665";
	static String tenantId = "e73696c616";
	static String tenantKey = "70D29624772AAF8FF98DA7C0B896B5C8";
	
	public ZwwlAuth(String id, String key) {
		tenantId = id;
		tenantKey = key;
	}
	
	public void fillHeader(HttpConnWapper inst, String url, String body) {
		Date now = new Date();
		String ts = Long.toString(now.getTime());
		
		inst.setHeaderParaAfterOpen("tenantId", tenantId);
		inst.setHeaderParaAfterOpen("ts", ts);
		inst.setHeaderParaAfterOpen("apiVer", "1.0");
		inst.setHeaderParaAfterOpen("encryptVer", "0");
		
		String sha1Str = "";
		if (body == null) {
			sha1Str = tenantId + url + tenantKey + ts;
		} else {
			sha1Str = token + url + body + tenantKey + ts;
		}
		
		String siginfo = DecriptUtil.SHA1(sha1Str);
		inst.setHeaderParaAfterOpen("signInfo", siginfo);
	}
	
    public static boolean check(String tenantIdreq,String signInforeq,String requestBody,String ts,String signUrl){
	   
	   logger.info("ZwwlAuth check id {} uri {}", tenantIdreq, signUrl);
	   if(tenantId.equals(tenantIdreq)){
	       String sha1Str = token + signUrl + requestBody + tenantKey + ts;
	       
	       logger.info("before SHA1 {}", sha1Str);
		   String lansiginfo = DecriptUtil.SHA1(sha1Str);
		   
		   logger.info("after SHA1 {}-{}", lansiginfo, signInforeq);
		   if(lansiginfo.equals(signInforeq)){
		        return true;
		   }
	   }
	   return false;
   }
    
    @Test
    public static void test(){
    	String signUrl = "/asset/zwwl/controlDevice";
    	String requestBody = "{\"devId\":\"004A77021103002E\",\"control\":{\"movethr\":\"1\",\"cfgmode\":\"0\",\"energyCycleValue\":\"50\",\"lowbatterythr\":\"20\"}}";
    	String ts = "1516341828880";
    	String signInforeq = "4401b04f815b32b5287abe39a3466505";
    	String sha1Str = token + signUrl + requestBody + tenantKey + ts;
    	logger.info("before SHA1 {}", sha1Str);
		String lansiginfo = DecriptUtil.SHA1(sha1Str);
    	logger.info("after SHA1 {}-{}", lansiginfo, signInforeq);
    }
}
