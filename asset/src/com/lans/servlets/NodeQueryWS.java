package com.lans.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.*;
import com.lans.listener.ResourseMgrListener;



/**
 * websocket implementation class NodeQueryWS
 */
@ServerEndpoint("/NodeQueryWS.do")
public class NodeQueryWS {
	private Logger logger = LoggerFactory.getLogger(NodeQueryWS.class);
    @OnMessage 
    public void onMessage(String name, Session session)   
    					throws IOException, InterruptedException { 
        //logger.info("receive connection of user "+name);
    	String msgType = name.substring(0,3);
    	String msgBody = name.substring(3, name.length());
    	
    	if(msgType.equals("NME"))
    	{
    		logger.info(msgType + " " + msgBody);
    		ServletContext ctx = ResourseMgrListener.getGlobalContext();
    		OnlineUserBean userBean = (OnlineUserBean)ctx.getAttribute("OnlineUser");
    		//将用户名或地图和session绑定在一起，同一用户或地图可建立多个session
    		userBean.addUser(msgBody, session);
    	}
    	else if(msgType.equals("DEV"))
    	{
    		ServletContext ctx = ResourseMgrListener.getGlobalContext();
    		OnlineUserBean userBean = (OnlineUserBean)ctx.getAttribute("OnlineUser");
    		userBean.addDev(msgBody, session, false);
    	}
    	else if(msgType.equals("DBG"))
    	{
    		ServletContext ctx = ResourseMgrListener.getGlobalContext();
    		OnlineUserBean userBean = (OnlineUserBean)ctx.getAttribute("OnlineUser");
    		userBean.addDev(msgBody, session, true);
    	}
    	else {
			//do nothing
		}
    }  

    @OnOpen 
    public void onOpen (Session session, EndpointConfig config) throws IOException {
    	logger.info("Client connected");
    }
   
    @OnClose 
    public void onClose(Session session) throws IOException{  
    	ServletContext ctx = ResourseMgrListener.getGlobalContext();
        OnlineUserBean userBean = (OnlineUserBean)ctx.getAttribute("OnlineUser");
        userBean.delSession(session);
        logger.info("Client disconnected");
    }
    
    @OnError
    public void onError(Throwable T) throws IOException{
    	logger.info("node query websocket onError");
    }
   
}
