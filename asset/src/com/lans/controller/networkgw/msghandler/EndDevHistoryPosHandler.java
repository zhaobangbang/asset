package com.lans.controller.networkgw.msghandler;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.BaiduApi;
import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.controller.networkgw.tvmessages.EndDevHistoryPosList;
import com.lans.servlets.DevMsgHandler;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EndDevHistoryPosHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevHistoryPosHandler.class);
	IGateWayConnLayer3 l3;
	
	public EndDevHistoryPosHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_DEV_LOST_POS) {
			return true;
		}

		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		EndDevHistoryPosList devHistoryPosList = (EndDevHistoryPosList)upMsg;
		String deveui = DevMsgHandler.toDevEui(devInfo.getEui());
		int gpsN = devHistoryPosList.locList.size() + 1;
		
		double[] xGPSA = new double[gpsN];
		double[] yGPSA = new double[gpsN];	
		int[] timeA = new int[gpsN];
		String[] unixTime = new String[gpsN];
		
		float[] speedA = new float[gpsN];
		
		 String sql = "select * from dev_list_tbl where deveui=\""+deveui+"\"";
     	String owner = null;

 	   	DataBaseMgr db = DataBaseMgr.getInstance();
 	   	
     	ResultSet rs = db.executeQuery(sql);
    		try{
    			rs.beforeFirst();
    			if (rs.next()) {
    				owner = rs.getString("owner");
    			}
    		  }catch(SQLException ex) {
    				logger.error("enddevRealTime(),无此设备号：" + deveui);
    				return;
    			}
		for(int i = 0; i < gpsN; i++)
		{
			double xGPS = 0; 
			double yGPS = 0; 
			int time = 0;
			float speed = -1;
			
			if(i==0)
			{
				xGPS = devHistoryPosList.longitude;
				yGPS = devHistoryPosList.latitude;
				time = devHistoryPosList.time;
				speed = devHistoryPosList.speed;
			}
			else
			{
				xGPS = devHistoryPosList.locList.get(i-1).getLongi();
				yGPS = devHistoryPosList.locList.get(i-1).getLati();
				time = devHistoryPosList.locList.get(i-1).getTime();
				speed = devHistoryPosList.locList.get(i-1).getSpeed();
			}
			xGPSA[i] = xGPS;
			yGPSA[i] = yGPS;
			timeA[i] = time;
			unixTime[i] = Integer.toString(time);
			speedA[i] = speed;
			/*
            String unixTime = Integer.toString(time);
            //BaiduApi.AddTrack(deveui, xGPS, yGPS, unixTime);
            //BaiduApi.queryAlarm(deveui);
            
            if(i <= devHistoryPosList.locList.size())
            {
            	try   
            	{   
            	Thread.currentThread();
    			Thread.sleep(50);//毫秒   
            	}   
            	catch(Exception e){} 
            }
            */
		}	

		String xyresult = BaiduApi.GPSTransfer(xGPSA, yGPSA, gpsN);
	
    	if(null == xyresult || xyresult.equals(""))
    	{
    		logger.warn("EndDevOneOffPosHandler：Fail to parse GPS data from Baidu");
    		return;
    	}

        
        BaiduApi.AddTracks(deveui, xGPSA, yGPSA, unixTime);
        BaiduApi.queryAlarm(deveui);
        
        JSONObject xyjsonObj = JSONObject.fromObject(xyresult);
        String status = xyjsonObj.get("status").toString();
        if(status.equals("0"))
        {
        	String xyBaidu = xyjsonObj.get("result").toString();
        	//xyBaidu = xyBaidu.replace("[","");
        	//xyBaidu = xyBaidu.replace("]", "");
        	JSONArray xyArray = JSONArray.fromObject(xyBaidu);
        	
        	JSONArray newArray = new JSONArray();
        	JSONObject newObj = new JSONObject();
        	newObj.put("DevEUI", deveui);
        	newObj.put("msgType", "HIS");
        	
        	for(int i=0; i < xyArray.size(); i++)
        	{
        		JSONObject xy = xyArray.getJSONObject(i);

        		JSONObject newXY = new JSONObject();
        		String xGPS = xy.getString("x");
        		String yGPS = xy.getString("y");
        		newXY.put("xGPS", xGPS);
        		newXY.put("yGPS", yGPS);
        		newXY.put("time", timeA[i]);
        		newXY.put("speed", speedA[i]);
        		newArray.add(newXY);
        		
        		java.util.Date date = new java.util.Date((long)timeA[i]*1000);
        	    SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	sql = "insert into gps_tbl(owner,deveui,latitude,longitude,baidulati,baidulong, type, time) values('"+owner + "','" + deveui+
        			       "','" + yGPSA[i] + "','" + xGPSA[i] + "','" + yGPS + "','" + xGPS + "','历史','" + shortDF.format(date) + "')";
            	int affectedRow = db.executeUpdate(sql);

            	if(affectedRow == 0)
            	{
            		logger.warn("{},Fail to add realtime GPS in database.", deveui); 
            	}
        	}

        	newObj.put("GPS", newArray.toString());

        	try {
    			DevMsgHandler.updateToObserver(deveui, newObj);
    			DevMsgHandler.updateDevToObserver(deveui, newObj);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        else
        {
        	logger.info("EndDevOneOffPosHandler：Baidu API error, message: {}", xyjsonObj.get("message"));
        	return;
        }    	           	            	          
	}
}
