package com.lans.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.DevicesOperateBean;
import com.lans.dao.DevConfigDAO;
import com.lans.dao.ZwwldevicesDAO;
import com.lans.dao.beans.AcquiredemandInfo;
import com.lans.dao.beans.DevConfig;
import com.lans.dao.beans.SaveInfoSet;
import com.lans.dao.beans.Zwwldevices;


public class QueryResult {
	
	static DataBaseMgr db = DataBaseMgr.getInstance();
	static Logger logger = LoggerFactory.getLogger(QueryResult.class);
	
	public static String queryMapId(String devid){
		String mapid = null;
		String sql = "select * from dev_list_tbl where deveui=\""+devid+"\"";
		ResultSet rs = db.executeQuery(sql);
		try {
			rs.beforeFirst();
			if(rs.next()){
		    	mapid  = rs.getString("map_id");
		    }
			rs.close();
		} catch (SQLException e) {
			logger.info("query mapId error in dev_list_tbl by devid {} ",devid);
			e.printStackTrace();
		}
		if(null == mapid || mapid.equals("")){	
			mapid = "outdoor";
		}
	    return mapid;
	}
	
    public static String querySteps(String devid){
    	String steps = null;
    	
    	String sql = "select * from status_record_tbl where deveui =\""+devid+"\"";
    	ResultSet rs = db.executeQuery(sql);
		try {
			rs.beforeFirst();
			if(rs.next()){
				steps  = rs.getString("gps");
		    }
			rs.close();
		} catch (SQLException e) {
			logger.info("query steps error in status_record_tbl by devid {} ",devid);
			e.printStackTrace();
		}
	   return steps;
    }
    
    public static String queryBattery(String devid){
    	String battery = null;
    	String sql = "select * from dev_list_tbl where deveui =\""+devid+"\"";
    	ResultSet rs = db.executeQuery(sql);
    	try {
			rs.beforeFirst();
			if(rs.next()){
				battery = rs.getString("battery");
			}
			rs.close();
		} catch (SQLException e) {
			logger.info("query battery error in status_record_tbl by devid {} ",devid);
			e.printStackTrace();
		}
    	return battery;
    }
    //dev Type
    public static String querydevType(String devid){
		String sql = "select * from dev_list_tbl where deveui=\""+devid+"\"";
		ResultSet rs = db.executeQuery(sql);
		String devType = null;
		
		try{
			rs.beforeFirst();
		    if(rs.next()){
		    	devType = rs.getString("devType");
		  }
                rs.close();
		}catch (Exception e) {
			logger.info("query devType error in dev_list_tbl by devid {} ",devid);
			e.printStackTrace();
		}
		
		return devType;
	}
    
    public static int querydevBattery(String devid){
    	String sql = "select * from dev_list_tbl where deveui=\""+devid+"\"";
    	ResultSet rs = db.executeQuery(sql);
    	int battery = 0;
    	
    	try {
			rs.beforeFirst();
			if(rs.next()){
				battery = Integer.parseInt(cutStrAchieveNumber(rs.getString("battery")));
			}
		} catch (SQLException e) {
			logger.info("query battery error in status_record_tbl by devid {} ",devid);
			e.printStackTrace();
		}
    	return battery;
    }
    
//    @org.junit.Test
//    public static void Test(){
//    	String battery ="80%";
//    	int a = Integer.parseInt(cutStrAchieveNumber(battery));
//    	logger.info("a {}",a);
//    }
    
    public static String cutStrAchieveNumber(String str){
    	String number = "0";
    	Pattern p = Pattern.compile("[\\d]+");
	    Matcher m = p.matcher(str);
	     
	    if(m.find()){
	       number = m.group(0);
	    }
	   return number;
    }
    
    
    public static short querySQLHeartBeat(String dev){
		List<DevConfig> devlist = null;
		short sqlHB = 0;
		try {
			devlist = DevConfigDAO.getDevConfigByDevice(dev);
			if((devlist == null) || (devlist.size() == 0)){
				logger.error("Fail to query configInfo of the deveui {}",dev);
			}
			else{
			    DevConfig devConfig = devlist.get(0);
			    sqlHB = devConfig.getHB();
			   
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return sqlHB;
		
	}
	
	public static String queryLatestMsgTime(String dev){
		String sql = "select * from dev_list_tbl where deveui=\""+dev+"\"";
		ResultSet rs = db.executeQuery(sql);
		String latestMsgTime = null;
		try {
			rs.beforeFirst();
			if(rs.next()){
				latestMsgTime = rs.getString("statustime");
			}
		} catch (SQLException e) {
			logger.error("try/catch error in queryLatestMsgTime");
			e.printStackTrace();
		}
		
		return latestMsgTime;
	}
	
	public static String queryStatus(String dev){
		String status = null;
		try {
			List<Zwwldevices> zwwldev = ZwwldevicesDAO.getZwwldeviceByDevid(dev);
			Zwwldevices zwwldevice = zwwldev.get(0);
			status = zwwldevice.getStatus();
		} catch (Exception e) {
			logger.error("try/catch error in queryStatus");
			e.printStackTrace();
		}
		return status;
	}
	
	public static long getValidConfigHeartBeat(String dev,String latMsgTime,AcquiredemandInfo acqInfo){
		String latestMsgTime = latMsgTime;
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long diffMsgTime = 0;
		try {
			//dev上次心跳的时间
			Date msgTime = sdf.parse(latestMsgTime);
			Date newTime = new Date();
			//dev上次心跳的时间转换得到秒数
			long msgTimeBefore = msgTime.getTime()/1000;
			//下次心跳的秒数
			logger.info("acqInfo.getSqlHB {}",acqInfo.getSqlHB());
			long mgsTimeAfter = msgTimeBefore + (acqInfo.getSqlHB()*30);
			long nowTime = newTime.getTime()/1000;
			//下次心跳的秒数与当前时间的差值(/minutes)
			diffMsgTime = (mgsTimeAfter - nowTime)/30;
		    logger.info("latestMsgTime {} msgTimeBefore {} mgsTimeAfter {} nowTime {} diffMsgTime {}",
		    		                        latestMsgTime,msgTimeBefore,mgsTimeAfter,nowTime,diffMsgTime);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return diffMsgTime;
		
	}
	
	public static void OnlineDevCfg(String DevEUI,short iHB){
		//for(String dev : DevicesOperateBean.offlineDevList){
			if(!DevicesOperateBean.offlineDevList.contains(DevEUI)){
				//查询上线终端配置之前的心跳值
				short sqlHB = QueryResult.querySQLHeartBeat(DevEUI);
				if(sqlHB == 0){
					logger.info("the dev {} doesn't have configInfo",DevEUI);
				}
				else {
					//上线终端配置之前的心跳值与配置心跳的商
					short num = (short) (sqlHB/(short)iHB);
					if(num >= 3 ){
						 String latestMsgTime = QueryResult.queryLatestMsgTime(DevEUI);
						 AcquiredemandInfo acquInfo = new AcquiredemandInfo(DevEUI, (short)iHB, sqlHB, latestMsgTime);
						 SaveInfoSet.judge(acquInfo);
						logger.info("config AcquiredemandInfo!");
					 }else{
						 logger.info("num {} less than 3 ",num);
					 }
				}
			}
		//}
	}
	
	public static String repGatewaysn(String devid){
		String gatewaysn = null;
		String[] zwwlTrackDevidArry = {"004A77021103002E","004A77021103002F","004A770211030030"};
		String[] cowDevidArry = {"004A77021103004E","004A77021103004F"};
		String[] storageDevidArry = {"004A770211030043","004A770211030044","004A770211030045","004A770211030046","004A770211030047",
				                     "004A770211030048","004A770211030049","004A77021103004A","004A77021103004B","004A77021103004C"};
		
		for(String devids : zwwlTrackDevidArry){
			if(devids.equals(devid)){
				//中物办公室
				gatewaysn = "19155318";
				return gatewaysn;
			}
		}
		for(String devids : cowDevidArry){
			if(devids.equals(devid)){
				//钦州牛场
				gatewaysn = "19155319";
				return gatewaysn;
			}
		}
		for(String devids : storageDevidArry){
			if(devids.equals(devid)){
				//钦州蔗糖仓库
				gatewaysn = "18965022";
				break;
			}
		}
	 return gatewaysn;
	}
	
}
