package com.lans.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.DevOprStatus;
import com.lans.beans.DevWorkType;
import com.lans.beans.DevicesOperateBean;
import com.lans.beans.ObserverInfoBean;
import com.lans.common.BaiduApi;
import com.lans.common.DataBaseMgr;
import com.lans.common.JqueryGridDBAccess;
import com.lans.common.JqueryGridParser;



/**
 * Servlet implementation class DeviceListMgr
 * ��ӦDevListManager.html���豸��������Ա�ɼ�
 * 
 */
@WebServlet("/DeviceListMgr.do")
public class DeviceListMgr extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(DeviceListMgr.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeviceListMgr() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("receive do get in DeviceListManager.do");
	
        ServletContext ctx = request.getServletContext();
       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
       	
        String cityName = request.getParameter("city");
        String areaName = request.getParameter("area");
        String devName  = request.getParameter("deveui");
        
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String sql = "select * from dev_list_tbl";
		ResultSet rs = null;
		if(devName == null)
		{
			if(null != cityName && null == areaName)
				sql = "select * from dev_list_tbl where city = \"" + cityName + "\"";
			else if(null != cityName && null != areaName)
				sql = "select * from dev_list_tbl where city = \"" + cityName + "\" and area = \"" + areaName + "\"";
		
			
			ArrayList<String> devEuiList = new ArrayList<String>();
		
			try{
				rs = dbm.executeQuery(sql);
				rs.beforeFirst();
				while(rs.next()) {
	       			devEuiList.add(rs.getString("deveui"));
	       		}
				rs.close();
				} catch(SQLException ex) {
					logger.error("executeQuery1:" + ex.getMessage());
				}
			//this is the special response for JQGrid select column
			String resp = "";
			for (String eui:devEuiList) {
				resp += "<option value=\""+eui+"\">"+eui+"</option>";
			}
			//System.out.println("respond do get in DeviceListManager.do "+resp);
			response.getWriter().write(resp);
		}
		else
		{
			sql = "select * from dev_list_tbl where deveui=\"" + devName + "\"";
			String city = null;
			String area = null;
			try{
				rs = dbm.executeQuery(sql);
				if(rs.next())
				{
					city = rs.getString("city");
					area = rs.getString("area");
				}
				rs.close();
			}catch(SQLException ex) {
				logger.error("executeQuery1:" + ex.getMessage());
			}
			
			if(null != city && null != area)
				response.getWriter().write(city+"��"+area);
			else if(null != city)
				response.getWriter().write(city+"��");
			else
				response.getWriter().write("");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //��ֹ��������
        logger.info("receive do post in DeviceListManager.do "+inStr);
        
        String cityName = request.getParameter("city");
        String areaName = request.getParameter("area");
        String usrName  = request.getParameter("usrname");
        String deveui1 = request.getParameter("deveui");
        String posType = request.getParameter("posType");
        
        ServletContext ctx = request.getServletContext();
       	DataBaseMgr dbm = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
       	
        Map<String, String> postMap = new HashMap<String, String>();
        String oper = JqueryGridParser.parserGridString(inStr, postMap);
        String resp = "";
                
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		if(null == oper)
		{
			return;
		}
		HttpSession reqSession = request.getSession(false);
		if(reqSession != null)
		{
			String usrname = (String) reqSession.getAttribute("usrname");
			if(usrname != null)
			{
				if(usrname.equals("guest") && !oper.equals("load"))		
					return;
			}
			else
			{
				return;
			}
		}
		else
		{
			return;
		}
		
		//�õ�Ҫɾ���ļ�¼��DevEUI,����Ҫ�����豸��DevEUI/owner
		String devTypeWork = null;
		String id = postMap.get("id");
		String delSql = "select * from dev_list_tbl where id=\"" + id + "\"";
		String delEUI = null;
		String delOwner = null;

		try{
			ResultSet rs = dbm.executeQuery(delSql);
			if(rs.next())
			{
				delEUI = rs.getString("deveui");
				delOwner = rs.getString("owner");
				devTypeWork = rs.getString("postype");
			}			
		}catch(SQLException ex) {
			logger.error("executeQuery1:" + ex.getMessage());
		}
		DevicesOperateBean devicesOperateBean = null;
        switch (oper){
        case "load": {
        	if(null != cityName)
        	{
        		if(null == areaName)
        		{//���ĳ�������µ������豸
        			postMap.put("whereField", "city");
        			postMap.put("whereString", cityName);
        		}
        		else
        		{//���ĳ������ĳ�������µ������豸
        			postMap.put("whereField", "city");
        			postMap.put("whereString", cityName);
        			postMap.put("condString", "area=\""+areaName+"\"");       			
        		}
        	}
        	
        	if(null != usrName && deveui1!=null)
        	{
        		String selSql = "select prio,city from users_tbl where username='"+usrName+"'";
        		String prio = null;
        		String city = null;
        		try{
        			ResultSet rs = dbm.executeQuery(selSql);
        			while(rs.next()){
        			prio = rs.getString("prio");
        			city = rs.getString("city");
        			}
        			if(prio.equals("��������Ա")){
        			  postMap.put("condString", "deveui=\""+deveui1+"\"");
        			}
        			else if(prio.equals("һ���������Ա")){
        		      postMap.put("condString", "city='"+city+"' and deveui=\""+deveui1+"\"");	
        				
        			}
        			else if(prio.equals("�����������Ա")){
        			   selSql = "select area from users_tbl where city='"+city+"'";
        			   String area = null;
        			   try{
        				    rs = dbm.executeQuery(selSql);
        				    while(rs.next()){
        				    	area = rs.getString("area");
        				    }
        				    postMap.put("condString", "city='"+city+"' and area='"+area+"' and deveui=\""+deveui1+"\"");
        				   
        			   }catch (Exception e) {
        				   logger.error("executeQuery3:" + e.getMessage());
					}
        				
        			}
        		}catch(Exception ex) {
        			logger.error("executeQuery2:" + ex.getMessage());
        		}
        	}
        	else if(null != usrName)
        	{
        		String selSql = "select prio,city from users_tbl where username='"+usrName+"'";
        		String prio = null;
        		String city = null;
        		String posSearchStr = "";
        		
        		if(null != posType){
        			if(posType.equals("outdoor"))
        			    posSearchStr = " and (postype = '����' or  postype = '����������' or postype = '����������')";
        			else if(posType.equals("indoor"))
        				posSearchStr = " and (postype = '��������' or postype = '��������')";
        		}
        		try{
        			ResultSet rs = dbm.executeQuery(selSql);
        			while(rs.next()){
        			prio = rs.getString("prio");
        			city = rs.getString("city");
        			}
        			 if(prio.equals("һ���������Ա")){
        		      postMap.put("condString", "city='"+city+"'" + posSearchStr);	
        				
        			}
        			else if(prio.equals("�����������Ա")){
        			   selSql = "select area from users_tbl where city='"+city+"'" + posSearchStr;
        			   String area = null;
        			   try{
        				    rs = dbm.executeQuery(selSql);
        				    while(rs.next()){
        				    	area = rs.getString("area");
        				    }
        				    postMap.put("condString", "city='"+city+"' and area='"+area+"'" + posSearchStr);
        				   
        			   }catch (Exception e) {
        				   logger.error("executeQuery3:" + e.getMessage());
					}
        				
        			}
        			else if(prio.equals("��ͨ�û�")){
         				postMap.put("condString", "owner='"+usrName+"' and city='" + city + "'"+ posSearchStr);
        			}
        		}catch(Exception ex) {
        			logger.error("executeQuery2:" + ex.getMessage());
        		}
        	}
        	resp = JqueryGridDBAccess.load(dbm, "dev_list_tbl", postMap);
        	logger.debug("respond to post in DevicesListMgr.do "+resp.toString());
		    response.getWriter().write(resp);
		    break;
        }
        
        case "add": {
        	String now=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        	if(null != cityName)
        	{
        		if(null == areaName)
        		{//���ĳ�������µ������û�,ϵͳ����Ա/һ���������Ա����
        			postMap.put("city", cityName);
        		}
        		else
        		{//���ĳ������ĳ�������µ�������ͨ
        			postMap.put("city", cityName);
        			postMap.put("area", areaName); 			
        		}
        	}
        	
        	postMap.put("recv", now);
        	String deveui2 = postMap.get("deveui");
    		String[] strs = deveui2.split(",");
    		
    		for(int i = 0; i < strs.length; i++) {
    			String deveui3 = strs[i];
    			postMap.put("deveui", deveui3);
    			postMap.put("alias", deveui3);
        	if(JqueryGridDBAccess.insert(dbm, "dev_list_tbl", postMap) > 0)
        	{
        		String deveui = postMap.get("deveui");
        		String owner = postMap.get("owner");
        		String postype = postMap.get("postype");
        		if("����������".equals(postype) || "����������".equals(postype) || "����".equals(postype))
        		{
        		BaiduApi.AddEntity(deveui, deveui,owner);
        		}
        		
        		Map<String, String>updateValue = new HashMap<String, String>();
        		
        		updateValue.put("newvalue", postMap.get("deveui"));
        		logger.info("add: " + postMap.get("deveui"));

               	ObserverInfoBean obInfo = (ObserverInfoBean)ctx.getAttribute("obsInfo");
               	
               	if(null != owner){
               		//���¶�̬�ڴ�
               		devicesOperateBean = DevicesOperateBean.getInstance();
				    DevOprStatus status = devicesOperateBean.getDevOprList().get(deveui3);
               		if(null == status){
               			// System restart
               			status = new DevOprStatus();
               			status.setDevWorkType(DevWorkType.valueOf(postMap.get("postype")));
               		}else if(!status.getDevWorkType().toString().equals(postMap.get("postype"))){
               			// run system,add dev that is deleted not long ago
               			status.setDevWorkType(DevWorkType.valueOf(postMap.get("postype")));
               		}
               		devicesOperateBean.getDevOprList().put(deveui3, status);
               		obInfo.updateObserverInfoBean(owner, "add", updateValue);
               	  }	
        		}
        	}
        	break;
        }
        
        case "del":{
        	// del : only get the value of id
        	//After del dev, fail to get the dev; so that del entityFence and get owner firstly.
        	//ɾ�����豸�µľɵ�ʵ���Χ��
           	BaiduApi.DeleteEntityFence(request, delEUI);
        	if(JqueryGridDBAccess.delete(dbm, "dev_list_tbl", postMap) > 0)
        	{
        		Map<String, String>updateValue = new HashMap<String, String>();
        		updateValue.put("newvalue", delEUI);
				ObserverInfoBean obInfo = (ObserverInfoBean)ctx.getAttribute("obsInfo");
            	obInfo.updateObserverInfoBean(delOwner, "del", updateValue);
            	logger.info("del oper was on owner: " + delOwner + " dev:" + delEUI);
            	
        	}
        	
        	break;
        }
        
        case "edit":{
 			String newEUI = postMap.get("deveui");
 			String postype = postMap.get("postype");
 			if(null == newEUI){
 				newEUI = delEUI;
 			}
 			
			if(null != devTypeWork && null !=postype  && !devTypeWork.equals(postype)){
				devicesOperateBean = DevicesOperateBean.getInstance();
				DevOprStatus status = devicesOperateBean.getDevOprList().get(newEUI);
				if(null != status){
					status.setDevWorkType(DevWorkType.valueOf(postMap.get("postype")));
				}
			}
        	if(JqueryGridDBAccess.update(dbm, "dev_list_tbl", postMap) > 0)
        	{
        		String owner = postMap.get("owner");
        		
        		if(owner == null)
        		{
        			owner = delOwner;
        		}
        		
        		BaiduApi.UpdateEntity(newEUI, newEUI, owner);
        		if(newEUI != null && !delEUI.equals(newEUI))
        		{//ֻ���豸EUI���޸��˲���Ҫ����Χ��
        			//ɾ�����豸�µľɵ�ʵ���Χ��
        			BaiduApi.DeleteEntityFence(request, delEUI);
        			//�����µ�ʵ�壬Χ����Ҫ�û���������
        			if(null == postype){
        				postype = devTypeWork;
        			}
        			if("����������".equals(postype) || "����������".equals(postype) || "����".equals(postype))
        			{
        			BaiduApi.AddEntity(newEUI, newEUI,owner);
        		}
        		}
    			
        		Map<String, String>updateValue = new HashMap<String, String>();

        		updateValue.put("oldvalue", delEUI);
        		updateValue.put("newvalue", newEUI);
        		

        		logger.info("edit oper was on");
        		ObserverInfoBean obInfo = (ObserverInfoBean)ctx.getAttribute("obsInfo");

        		if(delOwner.equals(owner))
        		{
        			if(newEUI != null && !delEUI.equals(newEUI))
        				obInfo.updateObserverInfoBean(delOwner, "edit", updateValue);
        		}
        		else
        		{
        			if(newEUI == null)
        			{
        				updateValue.put("newvalue", delEUI);
        				obInfo.updateObserverInfoBean(delOwner, "del", updateValue);
        				updateValue.put("newvalue", delEUI);
        				obInfo.updateObserverInfoBean(postMap.get("owner"), "add", updateValue);
        			}
        			else
        			{
        				updateValue.put("newvalue", delEUI);
        				obInfo.updateObserverInfoBean(delOwner, "del", updateValue);
        				updateValue.put("newvalue", newEUI);
        				obInfo.updateObserverInfoBean(postMap.get("owner"), "add", updateValue);
        			}
        		}
        	}
        	
        	break;
        }
        
        case "query":{
        	break;
         }
        
        default:{
         }
       }//switch
	}

}
