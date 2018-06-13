package com.lansitec.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansitec.beans.DevicesOperateBean;
import com.lansitec.dao.DevConfigDAO;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.beans.AcquiredemandInfo;
import com.lansitec.dao.beans.DevConfig;
import com.lansitec.dao.beans.DevInfo;


public class QueryResult {
	
	private static Logger logger = LoggerFactory.getLogger(QueryResult.class);
    
    public static int querydevBattery(String devid){
    	int battery = 0;
    	DevInfo devInfo = null;
		try{
			devInfo = DevInfoDAO.getDevInfoByDeveui(devid);
			
			battery = Integer.parseInt(cutStrAchieveNumber(devInfo.getBattery()));
		}catch (Exception e) {
			logger.error("query battery error in status_record_tbl by devid {} ：" + devid);
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
		DevConfig devcfg = null;
		short sqlHB = 0;
		try {
			devcfg = DevConfigDAO.getDevConfigByDevice(dev);
			if(null == devcfg){
				logger.error("Fail to query configInfo of the deveui {}",dev);
			}
			else{
			    sqlHB = devcfg.getHB();
			   
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return sqlHB;
		
	}
	
	public static String queryLatestMsgTime(String dev){
		String latestMsgTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DevInfo devInfo = null;
	
		try{
			devInfo = DevInfoDAO.getDevInfoByDeveui(dev);
			if(null == devInfo){
				logger.error("Fail to get the devInfo by the dev {}",dev);
				
			}else{
				latestMsgTime = sdf.format(devInfo.getStatustime());
			}
			
		}catch (Exception e) {
			logger.error("try/catch error in queryLatestMsgTime by the devid {} ：" + dev);
			e.printStackTrace();
		}
		return latestMsgTime;
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
}
