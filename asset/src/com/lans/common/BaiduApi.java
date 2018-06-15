package com.lans.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.BaiduTrackPoint;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * 百度地图，百度鹰眼相关API
 * Creator: Zhang Chao
 * Date: 2016/08/20
 */
public class BaiduApi {
    private static final String baiduGPS = "http://api.map.baidu.com/geoconv/v1/"; 
    private static final String baiduGPSParam = "&from=1&to=5&ak=T3esAl5i0NHA9SAEHF3dRyauqTazR2bf";
    
    private static final String baiduEye_addEntity = "http://yingyan.baidu.com/api/v3/entity/add";
    private static final String baiduEye_updateEntity = "http://yingyan.baidu.com/api/v3/entity/update";
    private static final String baiduEye_deleteEntity = "http://yingyan.baidu.com/api/v3/entity/delete";
    private static final String baiduEye_addColumn = "http://api.map.baidu.com/trace/v2/entity/addcolumn";
    private static final String baiduEye_deleteColumn = "http://api.map.baidu.com/trace/v2/entity/deletecolumn";
    
    private static final String baiduEye_addTrack  = "http://api.map.baidu.com/trace/v2/track/addpoint";
    private static final String baiduEye_addTracks  = "http://yingyan.baidu.com/api/v3/track/addpoints";
    
    private static final String baiduEye_addFence  = "http://api.map.baidu.com/trace/v2/fence/create";
    private static final String baiduEye_updateFence = "http://api.map.baidu.com/trace/v2/fence/update";
    private static final String baiduEye_deleteFence = "http://api.map.baidu.com/trace/v2/fence/delete";
    
    private static final String baiduEye_queryAlarm = "http://api.map.baidu.com/trace/v2/fence/querystatus";
    
    private static final String baiduEye_akserviceId = "ak=SLlCUHnkdc580INpOiI9wRsbi8KYRKUW&service_id=118042";
    
    /*
     * 单位：米。每个轨迹点都有一个定位误差半径radius，这个值越大，代表定位越不准确，可能是噪点。
     * 围栏计算时，如果噪点也参与计算，会造成误报的情况。设置precision可控制，当轨迹点的定位误差半径
     * 大于设置的precision值时，就会把该轨迹点当做噪点，不参与围栏计算。presision默认值为0，不去噪。
     */
    private static final int  baiduEye_precision = 30;
    static Logger logger = LoggerFactory.getLogger(BaiduApi.class);
    
    /*
     * GSP坐标转百度坐标
     */
    static public String GPSTransfer(double xGPS, double yGPS)
    {
    	return HttpUtil.sendGet(baiduGPS, "coords=" + xGPS + "," + yGPS + baiduGPSParam);
    }

    static public String GPSTransfer(double[] xGPSA, double[] yGPSA, int number)
    {
    	if(number < 1)
    		return null;
    	
    	String gpString = "";
    	for(int i=0; i<number; i++)
    	{
    		gpString += xGPSA[i] + "," + yGPSA[i];
    		if(i != number-1)
    			gpString += ";";
    	}
    	return HttpUtil.sendGet(baiduGPS, "coords="+ gpString + baiduGPSParam);
    }
    /*
     * 为每个实体增加属性
     */
    static private boolean AddColumn(String columnName)
    {
    	//为Entity创建属性	
    	String colResult = HttpUtil.sendPost(baiduEye_addColumn, baiduEye_akserviceId + "&column_key=" + columnName);
    	if(colResult.equals(""))
    	{
    		logger.error("Fail to create entity column in Baidu eye.");
    		return false;
    	}
        JSONObject addjsonObj = JSONObject.fromObject(colResult);
        String status = addjsonObj.get("status").toString();
        //0 means success
        if(status.equals("0"))
        {
        	logger.info("Succeed to create entity column in Baidu eye.");
        }
        else
        {
        	logger.error("Baidu API error, message:" + addjsonObj.get("message").toString());
        	return false;
        }
        
        return true;
    }
    /*
     * 为每个实体删除属性
     */
    @SuppressWarnings("unused")
    static private boolean DeleteColumn(String columnName)
    {
    	//为Entity创建属性
    	String colResult = HttpUtil.sendPost(baiduEye_deleteColumn, baiduEye_akserviceId + "&column_key=" + columnName);
    	if(colResult.equals(""))
    	{
    		logger.error("Fail to delete entity column from Baidu eye.");
    		return false;
    	}
        JSONObject addjsonObj = JSONObject.fromObject(colResult);
        String status = addjsonObj.get("status").toString();
        //0 means success
        if(status.equals("0"))
        {
        	logger.info("Succeed to delete entity column in Baidu eye.");
        }
        else
        {
        	logger.error("Baidu API error, message:" + addjsonObj.get("message").toString());
        	return false;
        }
        
        return true;
    }
    /*
     * 管理员每增加一个设备，调用此函数增加一个实体
     */
    static public boolean AddEntity(String DevEUI, String alias,String owner)
    {
    	if(null == DevEUI)
    		return false;
    	     
    	String addResult = HttpUtil.sendPost(baiduEye_addEntity, baiduEye_akserviceId + "&entity_name=" + DevEUI + "&alias=" + alias+"&owner="+owner);
    	if(addResult.equals(""))
    	{
    		logger.error("Fail to create entity in Baidu eye.");
    		return false;
    	}
        JSONObject addjsonObj = JSONObject.fromObject(addResult);
        String status = addjsonObj.get("status").toString();

        //0 means success, 3005 means 设备已存在
        if(status.equals("0") || status.equals("3005"))
        {
        	logger.info("Succeed to create entity in Baidu eye.");
        }
        else
        {
        	logger.error("Baidu API error, message:" + addjsonObj.get("message").toString());
        	if(status.equals("7002"))
        	{
        		if(BaiduApi.AddColumn("alias"))
        		{
        			logger.info("Try again");
        		    HttpUtil.sendPost(baiduEye_updateEntity, baiduEye_akserviceId + "&entity_name=" + DevEUI + "&alias=" + alias+"&owner="+owner);       		
        		}
        		else
        		{
        			return false;
        		}
        	}
        	else
        	{
        		return false;
        	}
        }
        
        return true;
    }
    
    static public boolean UpdateEntity(String DevEUI, String alias,String owner)
    {
    	if(null == DevEUI || (null == alias && null == owner))
    	{
    		logger.error("Fail to update entity in Baidu eye. DevEUI {}, alias {}, owner {}", DevEUI, alias, owner);
    		return false;
    	}
    	
    	String updatedStr = "";
    	if(null == alias)
    		updatedStr = "&owner=" + owner;
    	else{
    		if(null == owner)
    			updatedStr = "&alias=" + alias;
    		else
    			updatedStr = "&alias=" + alias + "&owner=" + owner;
    	}
     	String addResult = HttpUtil.sendPost(baiduEye_updateEntity, baiduEye_akserviceId + "&entity_name=" + DevEUI + updatedStr);
    	if(addResult.equals(""))
    	{
    		logger.error("Fail to update entity in Baidu eye.");
    		return false;
    	}
        JSONObject addjsonObj = JSONObject.fromObject(addResult);
        String status = addjsonObj.get("status").toString();
        //0 means success, 3005 means 设备已存在
        if(status.equals("0"))
        {
        	logger.info("Succeed to update entity in Baidu eye.");
        }
        else
        {
        	logger.error("Baidu API error, message:" + addjsonObj.get("message").toString());
        	if(status.equals("7002"))
        	{
        		if(BaiduApi.AddColumn("alias"))
        		{
        			logger.info("Try again");
        		    HttpUtil.sendPost(baiduEye_updateEntity, baiduEye_akserviceId + "&entity_name=" + DevEUI + "&alias=" + alias+"&owner"+owner);       		
        		}
        		else
        		{
        			return false;
        		}
        	}
        	else
        	{
        		return false;
        	}
        }
        
        return true;
    }
    
    /*
     * 删除设备时，将相应实体也删除。
     */
    static public boolean DeleteEntity(String DevEUI)
    {
    	String addResult = HttpUtil.sendPost(baiduEye_deleteEntity, baiduEye_akserviceId + "&entity_name=" + DevEUI);
    	if(addResult.equals(""))
    	{
    		logger.error("Fail to delete entity from Baidu eye.");
    		return false;
    	}
        JSONObject addjsonObj = JSONObject.fromObject(addResult);
        String status = addjsonObj.get("status").toString();
        //0 means success, 3003 means 要删除的实体不存在，同样认为成功
        if(status.equals("0") || status.equals("3003"))
        {
        	logger.info("Succeed to delete entity from Baidu eye.");
        }
        else
        {
        	logger.error("Baidu API error, message:" + addjsonObj.get("message").toString());
        	return false;
        }
        
        return true;
    }
    
    /*
     * 删除设备时，将相应实体也删除，同时检查是否有相应围栏，如果有的话，同时删除
     */
    static public boolean DeleteEntityFence(HttpServletRequest request,String DevEUI)
    {
    	String addResult = HttpUtil.sendPost(baiduEye_deleteEntity, baiduEye_akserviceId + "&entity_name=" + DevEUI);
    	if(addResult.equals(""))
    	{
    		logger.error("Fail to delete entity from Baidu eye.");
    		return false;
    	}
        JSONObject addjsonObj = JSONObject.fromObject(addResult);
        String status = addjsonObj.get("status").toString();
        //0 means success
        if(status.equals("0"))
        {
        	logger.info("Succeed to delete entity from Baidu eye.");
        }
        else
        {
        	logger.error("Baidu API error, message:" + addjsonObj.get("message").toString());
        	return false;
        }
        
        //删除实体时，同时检查是否有相应围栏，如果有的话，同时删除
        return DeleteFence(request, DevEUI);
    }
  
    /*********************轨迹相关*********************/
    /*
     * 添加轨迹，目前百度地图上轨迹无法删除
     */
    static public boolean AddTrack(String DevEUI, double xGPS, double yGPS, String trackTime)
    {
    	String trackResult = HttpUtil.sendPost(baiduEye_addTrack, baiduEye_akserviceId + "&entity_name=" + DevEUI + "&longitude=" + xGPS +
    								"&latitude=" + yGPS + "&coord_type=1" + "&loc_time=" + trackTime);
    	
    	if(trackResult.equals(""))
    	{
    		logger.error(DevEUI + ":Fail to add track point to Baidu.");
    		return false;
    	}
        JSONObject trackjsonObj = JSONObject.fromObject(trackResult);
        String trackstatus = trackjsonObj.get("status").toString();
        if(!trackstatus.equals("0"))
        {
        	logger.error("Baidu API error, message:" + trackjsonObj.get("message").toString());
        	return false;
        }
        
        return true;
    }
    
    static public boolean AddTracks(String DevEUI, double[] xGPS, double[] yGPS, String[] trackTime)
    {
    	int len = xGPS.length;
    	
    	if(len <=0 )
    		return false;
    	
		BaiduTrackPoint  baiduGps[] = new BaiduTrackPoint[len];
       	for(int i=0;i<len;i++){
       		baiduGps[i] = new BaiduTrackPoint(DevEUI, yGPS[i],xGPS[i],trackTime[i],"wgs84");
       	}
       	JSONArray jsonRsp = JSONArray.fromObject(baiduGps);
       	
    	String trackResult = HttpUtil.sendPost(baiduEye_addTracks, baiduEye_akserviceId + "&point_list=" + jsonRsp.toString());
    	
    	if(trackResult.equals(""))
    	{
    		logger.error(DevEUI + ":Fail to add track point to Baidu.");
    		return false;
    	}
        JSONObject trackjsonObj = JSONObject.fromObject(trackResult);
        String trackstatus = trackjsonObj.get("status").toString();
        if(!trackstatus.equals("0"))
        {
        	logger.error("Baidu API error, message:" + trackjsonObj.get("message").toString());
        	return false;
        }
        
        return true;
    }
    /*添加电子围栏，具体含义参考：http://lbsyun.baidu.com/index.php?title=yingyan/api/fence
     * DevEUI: 监控对象
     * stime:  监控开始时间
     * etime:  监控结束时间
     * */
    static public int AddFence(String DevEUI, String stime, String etime, int validday, float xGPS, float yGPS, int radius)
    {
    	if(null == DevEUI || null == stime || null == etime)
    		return -1;
    	if(DevEUI.equals("") || stime.equals("") || etime.equals(""))
    		return -1;
    	
    	//Baidu时间格式为hhmm, 如8点20， 0820
    	stime = stime.replace(":", "");
    	etime = etime.replace(":", "");
    	
    	stime = stime.substring(0,4);
    	etime = etime.substring(0,4);
    	
    	if(stime.equals(etime))
    	{
    		stime="0000";
    		etime="2359";
    	}
    	String valid_days="";
    	for(int i=0; i<7; i++)
    	{
    		if(((validday >> i) & 1) == 1)
    		{
    			valid_days += i+1 + ",";
    		}  		
    	}
    	valid_days = valid_days.substring(0, valid_days.length() - 1);
    	
    	String param = "&name=" + DevEUI + "&creator="+DevEUI+"&monitored_persons="+DevEUI+"&observers="+DevEUI+
    			"&valid_times=" + stime + "," + etime + "&valid_cycle=5&valid_days=" + valid_days + "&shape=1&coord_type=3&center=" + 
    			xGPS + "," + yGPS + "&radius=" + radius + "&alarm_condition=2&precision=" + baiduEye_precision; 
    	
    	String fenceResult = HttpUtil.sendPost(baiduEye_addFence, baiduEye_akserviceId + param);
    	
    	if(fenceResult.equals(""))
    	{
    		logger.error(DevEUI + ":Fail to add track fence to Baidu.");
    		return -1;
    	}
        JSONObject fencejsonObj = JSONObject.fromObject(fenceResult);
        String trackstatus = fencejsonObj.get("status").toString();
        if(trackstatus.equals("0"))
        {
        	logger.info(DevEUI + ":Succeed to add fence to Baidu eye.");
        	String fence_id = fencejsonObj.get("fence_id").toString();
        	return Integer.parseInt(fence_id);
        }
        else
        {
        	logger.error("Baidu API error, message:" + fencejsonObj.get("message").toString());
        	return 0;	
        }
    }
    
    /*更新围栏信息*/
    static public boolean UpdateFence(int fence_id, String DevEUI, String stime, String etime, int validday, float xGPS, float yGPS, int radius)
    {
    	if(DevEUI.equals("") || stime.equals("") || etime.equals(""))
    		return false;
    	
    	stime = stime.replace(":", "");
    	etime = etime.replace(":", "");
    	
    	stime = stime.substring(0,4);
    	etime = etime.substring(0,4);
    	
    	if(stime.equals(etime))
    	{
    		stime="0000";
    		etime="2359";
    	}
    	
    	String valid_days="";
    	for(int i=0; i<7; i++)
    	{
    		if(((validday >> i) & 1) == 1)
    		{
    			valid_days += i+1 + ",";
    		}
    	}
		valid_days = valid_days.substring(0, valid_days.length() - 1);
		
    	String param = "&fence_id=" + fence_id + "&name=" + DevEUI + "&creator="+DevEUI+"&monitored_persons="+DevEUI+"&observers="+DevEUI+
    			"&valid_times=" + stime + "," + etime + "&valid_cycle=5&valid_days=" + valid_days + "&shape=1&coord_type=3&center=" + 
    			xGPS + "," + yGPS + "&radius=" + radius + "&alarm_condition=2"; 
    	
    	String fenceResult = HttpUtil.sendPost(baiduEye_updateFence, baiduEye_akserviceId + param);
    	
    	if(fenceResult.equals(""))
    	{
    		logger.error(DevEUI + ":Fail to update track fence to Baidu.");
    		return false;
    	}
        JSONObject fencejsonObj = JSONObject.fromObject(fenceResult);
        String trackstatus = fencejsonObj.get("status").toString();
        if(trackstatus.equals("0"))
        {
        	logger.info(DevEUI + ":Succeed to update fence to Baidu eye.");
        	return true;
        }
        else
        {
        	logger.error("Baidu API error, message:" + fencejsonObj.get("message").toString());
        	return false;	
        }
    }
    
    /*根据设备ID删除围栏*/
    static public boolean DeleteFence(HttpServletRequest request, String DevEUI)
    {
    	String sql = "select * from dev_fence_tbl where deveui=\""+DevEUI+"\"";
    	int fence_id = 0;
    	
		ServletContext ctx = request.getServletContext();
	   	DataBaseMgr db = (DataBaseMgr)ctx.getAttribute("DataBaseMgr");
	   	
    	ResultSet rs = db.executeQuery(sql);
   		try{
   			rs.beforeFirst();
   			if (rs.next()) {
   				fence_id = rs.getInt("fence_id");
   			}
   		  }catch(SQLException ex) {
   				logger.warn("DeleteFence(),无此设备号：" + DevEUI);
   				return false;
   			}
   		
   		if(DeleteFence(fence_id))
   		{
   			logger.info(DevEUI + ":Succeed to delete fence from Baidu eye.");
   			return true;
   		}
   		else
   		{
   			logger.info(DevEUI + ":Fail to delete fence from Baidu eye.");
   			return false;
   		}
    }
    
    /*根据围栏ID删除围栏*/
    static public boolean DeleteFence(int fence_id)
    {
    	if(fence_id <= 0)
    		return false;
    	
    	String param = "&fence_id=" + fence_id; 
    	
    	String fenceResult = HttpUtil.sendPost(baiduEye_deleteFence, baiduEye_akserviceId + param);
    	
    	if(fenceResult.equals(""))
    	{
    		logger.error(fence_id + ":Fail to delete track fence Baidu.");
    		return false;
    	}
        JSONObject fencejsonObj = JSONObject.fromObject(fenceResult);
        String trackstatus = fencejsonObj.get("status").toString();
        //2 means 围栏不存在
        if(trackstatus.equals("0") || trackstatus.equals("2") || trackstatus.equals("5104"))
        {
        	logger.info(fence_id + ":Succeed to delete fence from Baidu eye.");
        	return true;
        }
        else
        {
        	logger.error("Baidu API error, message:" + fencejsonObj.get("message").toString());
        	return false;	
        }
    }   
    
    /*告警查询*/
    static public boolean queryAlarm(String DevEUI)
    {
    	String sql = "select * from dev_fence_tbl where deveui=\""+DevEUI+"\"";
    	int fence_id = 0;
    	String owner = null;

	   	DataBaseMgr db = DataBaseMgr.getInstance();
	   	
    	ResultSet rs = db.executeQuery(sql);
   		try{
   			rs.beforeFirst();
   			if (rs.next()) {
   				fence_id = rs.getInt("fence_id");
   				owner = rs.getString("owner");
   			}
   		  }catch(SQLException ex) {
   				logger.warn("queryAlarm(),无此设备号：" + DevEUI);
   				return false;
   			}
   		
   		if(fence_id <= 0)
   			return false;
   		
    	String fenceResult = HttpUtil.sendGet(baiduEye_queryAlarm, baiduEye_akserviceId + "&fence_id=" + fence_id + "&monitored_persons="+DevEUI);
    	
    	if(fenceResult.equals(""))
    	{
    		logger.error(DevEUI + ":Fail to query alarm from Baidu.");
    		return false;
    	}
        JSONObject fencejsonObj = JSONObject.fromObject(fenceResult);
        String trackstatus = fencejsonObj.get("status").toString();

        if(trackstatus.equals("0"))
        {
        	logger.info(fence_id + ":Succeed to query alarm from Baidu eye.");
        	String monitored_person_statuses = fencejsonObj.get("monitored_person_statuses").toString(); 
        	
        	if(monitored_person_statuses.equals("0"))
        		return false;
        	else
        	{
                JSONArray alarmjsonArray = JSONArray.fromObject(monitored_person_statuses);
                JSONObject alarmjsonObj = alarmjsonArray.getJSONObject(0);
                
                String monitored_person = alarmjsonObj.get("monitored_person").toString();
                String monitored_status = alarmjsonObj.get("monitored_status").toString();
                String ALARMTYPE = "越界";
            	//查看此设备告警是否依然存在
            	sql = "select * from warning_record_tbl where deveui=\""+DevEUI+
            			       "\" and warn_desc=\"" + ALARMTYPE + "\" and warn_on='1'";
        	   	boolean alarmOn = false;
            	rs = db.executeQuery(sql);
           		try{
           			rs.beforeFirst();
           			if (rs.next()) {
           				alarmOn = true;
           			}
           		  }catch(SQLException ex) {
           				System.err.println("queryAlarm(),无此设备号：" + DevEUI);
           				return false;
           			}
           		
                if(monitored_person.equals(DevEUI) && monitored_status.equals("2"))
                {
                	logger.warn(DevEUI + ":Succeed to query alarm from Baidu eye.");             	
                	              		
               		//如果告警在数据库中不存在则添加
               		if(!alarmOn)
               		{
               	    	java.util.Date date = new java.util.Date();
               	    	SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    	sql = "insert into warning_record_tbl(usrname,deveui,warn_desc, warn_stime,warn_on) values('"+owner + "','" + DevEUI+
             			       "','" + ALARMTYPE + "','" + shortDF.format(date) + "','1')";
                    	int affectedRow = db.executeUpdate(sql);

                    	if(affectedRow == 0)
                    	{
                    		logger.warn(DevEUI + ":Fail to add alarm in database."); 
                    	}
               		}
                	return true;
                }
                else//当前DevEUI无告警
                {
                	//如果告警在数据库中存在则将其置为false
                	if(alarmOn)
                	{
               	    	java.util.Date date = new java.util.Date();
               	    	SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    	sql = "update warning_record_tbl set warn_etime='" +shortDF.format(date) +"', warn_on='0' where deveui=\""+DevEUI+
            			       "\" and warn_desc=\"" + ALARMTYPE + "\" and warn_on='1'";
                    	
                     	int affectedRow = db.executeUpdate(sql);

                     	if(affectedRow == 0)
                     	{
                     		logger.warn(DevEUI + ":Fail to add alarm in database."); 
                     	}              		
                	}
                }
        	}
        }
        else
        {
        	logger.error("Baidu API error, message:" + fencejsonObj.get("message").toString());
        }
        return false;
    }
}
