package com.lans.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class OnlineUser {
	private String userName;
	private Session session;
	
	public OnlineUser(String name, Session sess) {
		userName = name;
		session = sess;
	}
	
	public String getName() {
		return userName;
	}
	
	public Session getSession() {
		return session;
	}
}

class OnlineDev {
	private String deveui;
	private Session session;
	private boolean isDebug;
	
	public OnlineDev(String devname, Session sess, boolean debug) {
		deveui = devname;
		session = sess;
		isDebug = debug;
	}
	
	public String getDevName() {
		return deveui;
	}
	
	public Session getSession() {
		return session;
	}
	
	public boolean getIsDebug(){
		return isDebug;
	}
}

public class OnlineUserBean {
	private Set<OnlineUser> onlineUsers;
	private Set<OnlineDev>  onlineDevs;
	static Logger logger = LoggerFactory.getLogger(OnlineUserBean.class);
	public static OnlineUserBean instance;
	
	public OnlineUserBean() {
		onlineUsers = new HashSet<OnlineUser>();
		onlineDevs  = new HashSet<OnlineDev>();
	}
	
	public static OnlineUserBean getInstance() {
		if (instance == null) {
			instance = new OnlineUserBean();
		}
		
		return instance;
	}
	
	/*用户名和session邦定,属于此用户的设备将发动到对应的session上去*/
	public synchronized void addUser(String name, Session sess) {
		OnlineUser newUser = new OnlineUser(name, sess);
		
		onlineUsers.add(newUser);
		logger.info("addUser name: "+name);
	}
	
	/*设备和session邦定,此设备将发动到对应的session上去*/
	public synchronized void addDev(String dev, Session sess, boolean debug) {
		OnlineDev newDev = new OnlineDev(dev, sess, debug);
		onlineDevs.add(newDev);
		logger.info("addDev name: "+dev);	
	}
	
	public synchronized void delUser(String name) {
		for (OnlineUser curUser: onlineUsers) {
			if (curUser.getName().equals(name)) {
				onlineUsers.remove(curUser);
				return;      // return here suppose otherwise the for will crash
			}
		}
	}
	
	public synchronized void delSession(Session sess) {
		//Suppose it's user related session.
		for (OnlineUser curUser: onlineUsers) {
			if (curUser.getSession().equals(sess)) {
				//System.out.println("delSessionUser found it");
				onlineUsers.remove(curUser);
				return;      // return here suppose otherwise the for will crash
			}
		}
		//It must be dev related session.
		for (OnlineDev curDev: onlineDevs) {
			if (curDev.getSession().equals(sess)) {
				//System.out.println("delSessionUser found it");
				onlineDevs.remove(curDev);
				return;      // return here suppose otherwise the for will crash
			}
		}
	}
	
	public synchronized boolean isDebugSession(Session sess){
		for (OnlineDev curDev: onlineDevs) {
			if (curDev.getSession().equals(sess)) {
				if(curDev.getIsDebug())
					return true;
				else
					return false;
			}
		}
		
		return false;
	}
		
	public synchronized ArrayList<Session> getUserSession(String name) {
		//System.out.println("getUserSession name: "+name);
		ArrayList<Session> sessList = new ArrayList<Session>();
		
		for (OnlineUser curUser: onlineUsers) {
			if (curUser.getName().equals(name)) {
				logger.info("succeed getUserSession name: "+name);
				sessList.add(curUser.getSession());
				//System.out.println("getUserSession name: "+name+" "+curUser.getName());
			}
		}
		return sessList;
	}
	
	public synchronized ArrayList<Session> getDevSession(String dev) {
		//System.out.println("getUserSession name: "+name);
		ArrayList<Session> sessList = new ArrayList<Session>();
		
		for (OnlineDev curDev: onlineDevs) {
			if (curDev.getDevName().equals(dev)) {
				logger.info("succeed getDevSession name: "+dev);
				sessList.add(curDev.getSession());
				//System.out.println("getUserSession name: "+name+" "+curUser.getName());
			}
		}
		return sessList;
	}
}
