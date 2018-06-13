package com.lansitec.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.assetp2p.msgdefs.EndDevASSET;
import com.lansi.networkgw.GateWayLayer3Controller;
import com.lansi.openitf.JsonpSvcProxy;
import com.lansi.openitf.LansiProxy;
import com.lansitec.app.openitf.ThirdpartyProxy;
import com.lansitec.beans.*;
import com.lansitec.common.InitUploadContext;
import com.lansitec.controller.networkgw.msghandler.EndDevAckHandler;
import com.lansitec.controller.networkgw.msghandler.EndDevAlarmHandler;
import com.lansitec.controller.networkgw.msghandler.EndDevHBHandler;
import com.lansitec.controller.networkgw.msghandler.EndDevIndoorPosHandler;
import com.lansitec.controller.networkgw.msghandler.EndDevRegHandler;
import com.lansitec.controller.networkgw.tvmessages.EndDevAck;
import com.lansitec.controller.networkgw.tvmessages.EndDevAlarm;
import com.lansitec.controller.networkgw.tvmessages.EndDevHB;
import com.lansitec.controller.networkgw.tvmessages.EndDevHistoryPosList;
import com.lansitec.controller.networkgw.tvmessages.EndDevIndoorPos;
import com.lansitec.controller.networkgw.tvmessages.EndDevOneOffPos;
import com.lansitec.controller.networkgw.tvmessages.EndDevRealTimePos;
import com.lansitec.controller.networkgw.tvmessages.EndDevReg;
import com.lansitec.infrastructure.util.DevMonitor;
import com.lansitec.sendData.DevMonTest;
import com.lansitec.systemconfig.dao.ORMFactory;
import com.lansitec.thirdparty.app.infoHandler.LansitecAppReqListener;


/**
 * Application Lifecycle Listener implementation class ResourseMgrListener
 *
 */
@WebListener
public class ResourseMgrListener implements ServletContextListener, HttpSessionListener, ServletRequestListener {
	static Logger logger = LoggerFactory.getLogger(ResourseMgrListener.class);
	public static ServletContext gServletCtx;
	private java.util.Timer timer = null;
	public static GateWayLayer3Controller gGwConnL3 = null;
	public static ThirdpartyProxy thirdparty = null;
    /**
     * Default constructor. 
     */
    public ResourseMgrListener() {
        // TODO Auto-generated constructor stub
    	gServletCtx = null;
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
     */
    public void requestDestroyed(ServletRequestEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
     */
    public void requestInitialized(ServletRequestEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	timer.cancel();
    	LansiProxy proxy = LansiProxy.GetProxy();
    	proxy.CloseConns();
    }
    
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	ServletContext application = arg0.getServletContext();
    	gServletCtx = application;
    	
    	ORMFactory.initialize();
    	
    	ObserverInfoBean obsInfo = new ObserverInfoBean();
    	application.setAttribute("obsInfo", obsInfo);
    	
    	GpsNodeStatusBean gpsNodeStatus = new GpsNodeStatusBean();
    	application.setAttribute("GpsNodeStatus", gpsNodeStatus);
    	
    	OnlineUserBean onlineUser = OnlineUserBean.getInstance();
    	application.setAttribute("OnlineUser", onlineUser);
    	
    	DevicesOperateBean devOper = DevicesOperateBean.getInstance();
    	application.setAttribute("DevOper", devOper);   	
    	
    	InitUploadContext context = new InitUploadContext();
    	//Tests.runTests();
    	
    	// init of gateway sub-system
    	JsonpSvcProxy jp = new JsonpSvcProxy();
    	LansiProxy proxy = LansiProxy.GetProxy();
    	if(ORMFactory.localDebug)
    	{
    		//proxy.AddClaaConn("msp02.claaiot.com", 30002, "2c26c50211000003", "00112233445566778899aabbccddeeff");
    		//proxy.AddActilityConn(arg0, "/actility/recv", "https://api-dev1.thingpark.com/thingpark/lrc/rest/downlink");
      		proxy.AddMultitecConn(arg0, "/multitech/recv");
    		//proxy.AddP2PConn(12345);

    		jp.init(arg0, "d:/map/", "/jsonp");
    	}
    	else
    	{
    		if(ORMFactory.officialEnv)
    		{
    			proxy.AddClaaConn("msp02.claaiot.com", 30002, "2c26c50211000003", "00112233445566778899aabbccddeeff");
    			proxy.AddClaaConn("msp03.claaiot.com", 30002, "2c26c5048b010199", "00112233445566778899aabbccddeeff");
    		    //proxy.AddManthinkConn("iot.manthink.cn", 6001);
    		    //proxy.AddP2PConn(2016); 
    			//https://api-dev1.thingpark.com/thingpark/lrc/rest/downlink
    		    proxy.AddActilityConn(arg0, "/actility/recv", "https://api-dev1.thingpark.com/thingpark/lrc/rest/downlink");
    		    proxy.AddMultitecConn(arg0, "/multitech/recv");
    		    jp.init(arg0, "/usr/java/tomcat/apache-tomcat-8.0.33/webapps/map/", "/jsonp");
    		}
    		else
    		{
    			//proxy.AddClaaConn("msp01.claaiot.com", 30002, "2c26c50211000003", "00112233445566778899aabbccddeeff");
    			proxy.AddMultitecConn(arg0, "/multitech/recv");
    		    jp.init(arg0, "/usr/apache-tomcat-8.0.46/webapps/map/", "/jsonp");
    		}
    	}
    	gGwConnL3 = (GateWayLayer3Controller) proxy.PrepareConns();

    	proxy.AddSupportMsg(new EndDevReg());
    	//proxy.AddSupportMsg(new EndDevRealTimePos());
    	proxy.AddSupportMsg(new EndDevAck());
    	proxy.AddSupportMsg(new EndDevAlarm());
    	proxy.AddSupportMsg(new EndDevHB());
    	//proxy.AddSupportMsg(new EndDevHistoryPosList());
    	proxy.AddSupportMsg(new EndDevIndoorPos());
    	//proxy.AddSupportMsg(new EndDevOneOffPos());
    	proxy.AddSupportMsg(new EndDevASSET());
    	
    	proxy.AddMsgHandler(new EndDevRegHandler(gGwConnL3));
    	//proxy.AddMsgHandler(new EndDevRealTimePosHandler(gGwConnL3));
    	//proxy.AddMsgHandler(new EndDevHistoryPosHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevHBHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevAlarmHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevAckHandler(gGwConnL3));
    	//proxy.AddMsgHandler(new EndDevOneOffPosHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevIndoorPosHandler(gGwConnL3));
    	//p2pproxy = new ASSETP2PProxy(2016, 5, (short) 10, new DevAssetP2PHandler());
    	//application.setAttribute("ASSETP2PProxy", p2pproxy);
    	//proxy.AddMsgHandler(new EndDevASSETHandler(gGwConnL3, p2pproxy));
    	
    	DevMsgValidator validator = new DevMsgValidator();
    	gGwConnL3.setMsgPreValidator(validator);
    	
    	proxy.OpenConns();
    	
 
    	thirdparty = new ThirdpartyProxy();   	
    	thirdparty.addLansitecReqListener(new LansitecAppReqListener());
    	
    	timer = new java.util.Timer(true);  
    	DevMonitor dm = new DevMonitor(application);
    	
    	timer.schedule(dm, 0, 30 * 1000);  
    	
    	//excutor.scheduleAtFixedRate(dm, 0, 30*1000, TimeUnit.SECONDS);
    	// test send data to app
    	//DevMonTest dmt = new DevMonTest();
    	//timer.schedule(dmt, 0, 5 * 10);  
    }
    
    public static ServletContext getGlobalContext() {
    	return gServletCtx;
    }
	
}
