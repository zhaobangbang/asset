package com.lans.servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.OnlineUserBean;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class NodeStatusUpdate
 */
@WebServlet("/NodeStatusUpdate.do")
public class NodeStatusUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(NodeStatusUpdate.class);
   /**
     * @see HttpServlet#HttpServlet()
     */
    public NodeStatusUpdate() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    
    /*Inform the observe who want to see this device*/
    public void updateDevToObserver(HttpServletRequest request, 
			String dev, JSONObject jsonMsg) throws IOException{
    	ServletContext ctx = request.getServletContext();
    	OnlineUserBean onlineBean = (OnlineUserBean)ctx.getAttribute("OnlineUser");

    	ArrayList<Session> sessList = onlineBean.getDevSession(dev);
    	for(Session sess : sessList)
    	{
    		if ((null != sess) && (sess.isOpen())){
    			sess.getBasicRemote().sendText(jsonMsg.toString()); 
    			logger.info("send to "+dev+" with "+jsonMsg.toString());
    		}
    	}
    }
  
    public void updateDebugToObserver(HttpServletRequest request, 
			String dev, JSONObject jsonMsg) throws IOException{
    	ServletContext ctx = request.getServletContext();
    	OnlineUserBean onlineBean = (OnlineUserBean)ctx.getAttribute("OnlineUser");

    	ArrayList<Session> sessList = onlineBean.getDevSession(dev);
    	for(Session sess : sessList)
    	{
    		if ((null != sess) && (sess.isOpen()) && onlineBean.isDebugSession(sess)){
    			sess.getBasicRemote().sendText(jsonMsg.toString()); 
    			logger.info("send to "+dev+" with "+jsonMsg.toString());
    		}
    	}
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
