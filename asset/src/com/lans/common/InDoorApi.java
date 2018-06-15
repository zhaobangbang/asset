package com.lans.common;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/*
 * 室内地图，寅时相关API
 * Creator: Zhang Chao
 * Date: 2017/04/30
 */
public class InDoorApi {
    private static final String tokenReq = "http://res.innsmap.com/res/authJS/auth"; 
    private static long tokenTime = 0;
    private static String lastToken = "";
    private static final String token_akserviceId = "appId=411c1ef2ecd34f01a5a9c9632ffeb32e&url=www.lansitec.com";
    static Logger logger = LoggerFactory.getLogger(InDoorApi.class);

    /*
     * 请求token
     */
    static public String ReqToken()
    {
    	Date now = new Date();
    	if(0 == tokenTime || (now.getTime() - tokenTime)/1000 >= 7000)
        {
    		String colResult = HttpUtil.sendPost(tokenReq, token_akserviceId);
    		if(colResult.equals(""))
    		{
    			logger.error("Fail to request token.");
    			return null;
    		}
    		JSONObject addjsonObj = JSONObject.fromObject(colResult);
    		lastToken = addjsonObj.get("data").toString();
    		tokenTime = now.getTime();
    		return lastToken;
        }
    	else
    	{
    		return lastToken;
    	}
    }
}