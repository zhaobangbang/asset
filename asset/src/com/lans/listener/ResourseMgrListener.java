package com.lans.listener;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.asset.thirdparty.zwwl.ZwwlReqListener;
import com.lans.beans.*;
import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.msghandler.EndDevAckHandler;
import com.lans.controller.networkgw.msghandler.EndDevAlarmHandler;
import com.lans.controller.networkgw.msghandler.EndDevBraceletHandler;
import com.lans.controller.networkgw.msghandler.EndDevCoordinateHandler;
import com.lans.controller.networkgw.msghandler.EndDevHBHandler;
import com.lans.controller.networkgw.msghandler.EndDevHistoryPosHandler;
import com.lans.controller.networkgw.msghandler.EndDevIndoorPosHandler;
import com.lans.controller.networkgw.msghandler.EndDevOneOffPosHandler;
import com.lans.controller.networkgw.msghandler.EndDevRealTimePosHandler;
import com.lans.controller.networkgw.msghandler.EndDevRegHandler;
import com.lans.controller.networkgw.msghandler.EndGwHBHandler;
import com.lans.controller.networkgw.tvmessages.EndDevAck;
import com.lans.controller.networkgw.tvmessages.EndDevAlarm;
import com.lans.controller.networkgw.tvmessages.EndDevBracelet;
import com.lans.controller.networkgw.tvmessages.EndDevCoordinate;
import com.lans.controller.networkgw.tvmessages.EndDevHB;
import com.lans.controller.networkgw.tvmessages.EndDevHistoryPosList;
import com.lans.controller.networkgw.tvmessages.EndDevIndoorPos;
import com.lans.controller.networkgw.tvmessages.EndDevOneOffPos;
import com.lans.controller.networkgw.tvmessages.EndDevRealTimePos;
import com.lans.controller.networkgw.tvmessages.EndDevReg;
import com.lans.controller.networkgw.tvmessages.EndGwHB;
import com.lans.infrastructure.util.DevMonitor;
import com.lans.infrastructure.util.RSAUtils;
import com.lans.systemconfig.dao.ORMFactory;
import com.lansi.networkgw.GateWayLayer3Controller;
import com.lansi.openitf.JsonpSvcProxy;
import com.lansi.openitf.LansiProxy;
import com.lansi.openitf.ThirdpartyProxy;
import com.lansi.thirdparty.zwwl.msgSender.MqttclientPublish;
import com.lansi.thirdparty.zwwl.msgSender.ZwwlMsgSender;


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
    	
    	DataBaseMgr dbMgr = DataBaseMgr.getInstance();
    	application.setAttribute("DataBaseMgr", dbMgr);
    	
    	ObserverInfoBean obsInfo = new ObserverInfoBean(dbMgr);
    	application.setAttribute("obsInfo", obsInfo);
    	
    	GpsNodeStatusBean gpsNodeStatus = new GpsNodeStatusBean();
    	application.setAttribute("GpsNodeStatus", gpsNodeStatus);
    	
    	OnlineUserBean onlineUser = OnlineUserBean.getInstance();
    	application.setAttribute("OnlineUser", onlineUser);
    	
    	DevicesOperateBean devOper = DevicesOperateBean.getInstance();
    	application.setAttribute("DevOper", devOper);   	
    	
    	//创建公钥私钥对，用于前端修改用户密码时的加密解密
    	HashMap<String, Object> rsaKeys = null;
		try {
			rsaKeys = RSAUtils.getKeys();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	application.setAttribute("RsaKeys", rsaKeys);
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
      		//test use
      		//proxy.AddManthinkConn("iotcn02.manthink.cn", 6001);
        	proxy.SetDefaultLayer2(proxy.AddMQTTConn("tcp://dev.lansitec.com:1883", "lansitecLocalDebugAsset", "admin", "lansi#1234"));
    		jp.init(arg0, "c:/map/", "/jsonp");
    	}
    	else
    	{
    		if(ORMFactory.officialEnv)
    		{
    			proxy.AddClaaConn("msp02.claaiot.com", 30002, "2c26c50211000003", "00112233445566778899aabbccddeeff");
    			proxy.AddClaaConn("msp03.claaiot.com", 30002, "2c26c5048b010199", "00112233445566778899aabbccddeeff");
    		    //proxy.AddP2PConn(2016); 
    			//https://api-dev1.thingpark.com/thingpark/lrc/rest/downlink
    		    proxy.AddActilityConn(arg0, "/actility/recv", "https://api-dev1.thingpark.com/thingpark/lrc/rest/downlink");
    		    proxy.SetDefaultLayer2(proxy.AddMultitecConn(arg0, "/multitech/recv"));	
    		    jp.init(arg0, "/usr/java/tomcat/apache-tomcat-8.0.33/webapps/map/", "/jsonp");
    		}
    		else
    		{
    			//proxy.AddClaaConn("msp01.claaiot.com", 30002, "2c26c50211000003", "00112233445566778899aabbccddeeff");
    			proxy.AddManthinkConn("iotcn02.manthink.cn", 6001);
    			proxy.AddMultitecConn(arg0, "/multitech/recv");
    	    	proxy.SetDefaultLayer2(proxy.AddMQTTConn("tcp://dev.lansitec.com:1883", "lansitecDevAsset", "admin", "lansi#1234"));
    		    jp.init(arg0, "/usr/apache-tomcat-8.0.46/webapps/map/", "/jsonp");
    		}
    	}
    	gGwConnL3 = (GateWayLayer3Controller) proxy.PrepareConns();

    	proxy.AddSupportMsg(new EndDevReg());
    	proxy.AddSupportMsg(new EndDevRealTimePos());
    	proxy.AddSupportMsg(new EndDevAck());
    	proxy.AddSupportMsg(new EndDevAlarm());
    	proxy.AddSupportMsg(new EndDevHB());
    	proxy.AddSupportMsg(new EndDevHistoryPosList());
    	proxy.AddSupportMsg(new EndDevIndoorPos());
    	proxy.AddSupportMsg(new EndDevOneOffPos());
    	//proxy.AddSupportMsg(new EndDevASSET());
    	proxy.AddSupportMsg(new EndGwHB());
    	proxy.AddSupportMsg(new EndDevCoordinate());
    	proxy.AddSupportMsg(new EndDevBracelet());
    	
    	proxy.AddMsgHandler(new EndDevRegHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevRealTimePosHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevHistoryPosHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevHBHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevAlarmHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevAckHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevOneOffPosHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevIndoorPosHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndGwHBHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevCoordinateHandler(gGwConnL3));
    	proxy.AddMsgHandler(new EndDevBraceletHandler(gGwConnL3));
    	
    	//p2pproxy = new ASSETP2PProxy(2016, 5, (short) 10, new DevAssetP2PHandler());
    	//application.setAttribute("ASSETP2PProxy", p2pproxy);
    	//proxy.AddMsgHandler(new EndDevASSETHandler(gGwConnL3, p2pproxy));
    	
    	DevMsgValidator validator = new DevMsgValidator();
    	gGwConnL3.setMsgPreValidator(validator);
    	
    	proxy.OpenConns();
 
    	thirdparty = new ThirdpartyProxy();   	
    	thirdparty.initZwwlConn(arg0, "", "e73696c616", "70D29624772AAF8FF98DA7C0B896B5C8");
    	thirdparty.addReqListener(new ZwwlReqListener());
    	thirdparty.openConn();
    	
    	timer = new java.util.Timer(true);  
    	DevMonitor dm = new DevMonitor(application);
    	timer.schedule(dm, 0, 30* 1000);  
    	
    	MqttclientPublish pub = new MqttclientPublish();
    	try {
    		MqttclientPublish.mqttClient = pub.connect("admin", "password","lansitec3");
    		ZwwlMsgSender.mqtt = MqttclientPublish.mqttClient;
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//push online dev's position
    	UploadDevFinalLocation uploadDevLocaData = UploadDevFinalLocation.getInsitance();
    	//application.setAttribute("uploadDevLocaData", uploadDevLocaData);
    	uploadDevLocaData.pushDevFinalLocaltion();
    	
    	//excutor.scheduleAtFixedRate(dm, 0, 30*1000, TimeUnit.SECONDS);
    }
    
    public static ServletContext getGlobalContext() {
    	return gServletCtx;
    }
	
}
