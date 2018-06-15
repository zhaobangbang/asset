package com.lans.asset.thirdparty.zwwl;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.common.QueryResult;
import com.lans.dao.GpsDataDAO;
import com.lans.dao.ZwwldevicesDAO;
import com.lans.dao.beans.DevAndPositionInfo;
import com.lans.dao.beans.GpsData;
import com.lans.dao.beans.Zwwldevices;
import com.lans.listener.ResourseMgrListener;
import com.lansi.thirdparty.zwwl.jsondefs.BatchUploadData;
import com.lansi.thirdparty.zwwl.jsondefs.BatchUploadDataReq;
import com.lansi.thirdparty.zwwl.jsondefs.BatchUploadList;
import com.lansi.thirdparty.zwwl.jsondefs.BatchUploadPosAndBattery;
import com.lansi.thirdparty.zwwl.jsondefs.BatterythrData;
import com.lansi.thirdparty.zwwl.jsondefs.Calories;
import com.lansi.thirdparty.zwwl.jsondefs.CfgMetaData;
import com.lansi.thirdparty.zwwl.jsondefs.DevLocation;
import com.lansi.thirdparty.zwwl.jsondefs.GWPosAndDistance;
import com.lansi.thirdparty.zwwl.jsondefs.HeartBeatAndBloodPre;
import com.lansi.thirdparty.zwwl.jsondefs.StepsData;
import com.lansi.thirdparty.zwwl.jsondefs.UpEventReq;
import com.lansi.thirdparty.zwwl.jsondefs.UpLoadDataReq;
import com.lansi.thirdparty.zwwl.jsondefs.UploadBatteryThrReq;
import com.lansi.thirdparty.zwwl.jsondefs.UploadBatteryThrReqData;
import com.lansi.thirdparty.zwwl.jsondefs.UploadData;
import com.lansi.thirdparty.zwwl.jsondefs.UploadDevCowDataReq;
import com.lansi.thirdparty.zwwl.jsondefs.UploadDevCowReqData;
import com.lansi.thirdparty.zwwl.jsondefs.UploadFuncReq;
import com.lansi.thirdparty.zwwl.jsondefs.UploadGWPosAndDistance;
import com.lansi.thirdparty.zwwl.jsondefs.UploadGWPosAndDistanceData;
import com.lansi.thirdparty.zwwl.jsondefs.UploadHBAndBloodPre;
import com.lansi.thirdparty.zwwl.jsondefs.UploadHBAndBloodPreData;
import com.lansi.thirdparty.zwwl.jsondefs.UploadStepsAndCalories;
import com.lansi.thirdparty.zwwl.jsondefs.UploadStepsAndCaloriesData;
import com.lansi.thirdparty.zwwl.jsondefs.UploadStepsData;
import com.lansi.thirdparty.zwwl.jsondefs.UploadStepsDataReq;
import com.lansi.thirdparty.zwwl.msgSender.DevAndTenantId;
import com.lansi.thirdparty.zwwl.msgSender.ZwwlAuth;
import com.lansi.thirdparty.zwwl.msgSender.ZwwlMsgSender;

public class ZwwlSender {
	static Logger logger = LoggerFactory.getLogger(ZwwlSender.class);
	public static final String EVENT_MOVE = "1001";
	public static final String EVENT_WARNING = "1002";
	static List<DevAndPositionInfo> devids = new LinkedList<DevAndPositionInfo>();
	private static String CowDevModel = "100-00036";
	private static String trackerDevModel = "100-00035";
	private static String xtDevModelName = "100-02372";
	private static Set<DevAndTenantId> devAndTenantId = new HashSet<DevAndTenantId>();
	public static void sendPositionData(String devid, String longi, String lati,String tenantId) {
		String mapid = null;
		mapid = QueryResult.queryMapId(devid);
		logger.info("sendPositionData deveui {},mapid {},longi {},lati {}",devid, mapid,longi, lati);
		
		DevLocation loc = new DevLocation(mapid,longi, lati);
		UploadData data = new UploadData(loc);
		
		Date now = new Date();
		String time = Long.toString(now.getTime());
		UpLoadDataReq req = new UpLoadDataReq(devid.toUpperCase(), time, data);
		boolean addData = judgeAddSetData(devid);
		if(addData){
			devAndTenantId.add(new DevAndTenantId(devid,tenantId));
		}
		ZwwlMsgSender.devAndtenid = devAndTenantId;
		ResourseMgrListener.thirdparty.sendMessage(req);
	}
	
	public static void sendGPSPositionData(String devid, String xGPS, String yGPS,String tenantId){
		String mapid = null;
		String deveui = null;
		if(devid.length() == 12){
			try {
				List<Zwwldevices> zwwldevices = ZwwldevicesDAO.getZwwldeviceByDevid(devid);
				if((null != zwwldevices) && (zwwldevices.size() != 0)){
					deveui = zwwldevices.get(0).getGateway();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			deveui = devid;
		}
		mapid = QueryResult.queryMapId(deveui);
		logger.info("sendGPSPositionData deveui {},mapid {},longi {},lati {}",devid, mapid,xGPS, yGPS);
		Date now = new Date();
		String time = Long.toString(now.getTime());
		
		DevLocation location = new DevLocation(mapid, xGPS, yGPS);
		UploadDevCowReqData data = new UploadDevCowReqData(location);
		UploadDevCowDataReq req = new UploadDevCowDataReq(devid, time, data);
		boolean addData = judgeAddSetData(devid);
		if(addData){
			devAndTenantId.add(new DevAndTenantId(devid,tenantId));
		}
		ZwwlMsgSender.devAndtenid = devAndTenantId;
		ResourseMgrListener.thirdparty.sendMessage(req);
		
	}
	
	public static void sendBatterythrData(String devid,String batteryThr,String tenantId){
		String battery = "";
		if(batteryThr.contains("%")){
			battery = batteryThr;
		}else{
			battery = batteryThr+"%";
		}
		logger.info("sendBatterythrData deveui {} battery {}",devid,battery);
		Date now = new Date();
		String time = Long.toString(now.getTime());
		
		BatterythrData batterythr = new BatterythrData(battery);
		UploadBatteryThrReqData data = new UploadBatteryThrReqData(batterythr);
		UploadBatteryThrReq req = new UploadBatteryThrReq(devid.toUpperCase(), time, data);
		boolean addData = judgeAddSetData(devid);
		if(addData){
			devAndTenantId.add(new DevAndTenantId(devid,tenantId));
		}
		ZwwlMsgSender.devAndtenid = devAndTenantId;
		ResourseMgrListener.thirdparty.sendMessage(req);	
			
	}
	
	public static void sendListDevsAndPositionData(String devid, String longi, String lati){
		DevAndPositionInfo devsPosition = new DevAndPositionInfo(devid, longi, lati);
		devids.add(devsPosition);
		String deveui = null;
		String gpsLong = null;
		String gpsLat = null;
		String battery = null;
		DevLocation loc = null;
		BatchUploadPosAndBattery data = null;
		BatterythrData batterythr = null;
		BatchUploadList datas = null;
		List<BatchUploadList> listData = null;
		BatchUploadData uploadData = null;
		BatchUploadDataReq req = new BatchUploadDataReq();
		for(DevAndPositionInfo dev : devids){
			deveui = dev.getDevId();
			gpsLong = dev.getGpsLong();
			gpsLat = dev.getGpsLat();
			String mapid = null;
			mapid = QueryResult.queryMapId(devid);
			battery = QueryResult.queryBattery(devid);
		    loc = new DevLocation(mapid,gpsLong, gpsLat);
		    batterythr = new BatterythrData(battery);
		    data  = new BatchUploadPosAndBattery(loc,batterythr);
			Date now = new Date();
			String datetime = Long.toString(now.getTime());
			datas = new BatchUploadList(datetime, data);
			listData = new LinkedList<BatchUploadList>();
			listData.add(datas);
			uploadData = new BatchUploadData(deveui.toUpperCase(), listData);
			req.getDevs().add(uploadData);
		}
			//String reqData = JSON.toJSONString(req);
		   // System.out.println(reqData);
		ResourseMgrListener.thirdparty.sendMessage(req);
				
	}
	
	
	public static void sendFuncData(String devId, String movethr){
		List<CfgMetaData> funcList = new LinkedList<CfgMetaData>();
		funcList.add(new CfgMetaData("cfgmode", "enum", "配置模式", "0", "position,shake", "RW"));
		funcList.add(new CfgMetaData("movethr", "int", "移动报警阈值", "1", "1,9", "RW"));
		
		Date now = new Date();
	    String datetime = Long.toString(now.getTime());
		UploadFuncReq req = new UploadFuncReq(devId.toUpperCase(), datetime, funcList);
//		String reqData = JSON.toJSONString(req);
//		System.out.println(reqData);
		ResourseMgrListener.thirdparty.sendMessage(req);
		
	}
	
	
	
	
	@Test
	public void test(){
		String devt = "one"; 
//		String mo = "8";
//		sendFuncData(mo,devt);
		String devo = "two";
		List<String> dev = new LinkedList<String>();
		dev.add(devt);
		dev.add(devo);
		String longi = "118.02";
//		String lati = "30.58";
//		for(String devs :dev){
//				sendListDevsAndPositionData(devs,longi,lati);
//		}
		sendStepsEvent(devo,longi,"");
	}
	
	
	public static void sendEvent(String devid,String event,String tenantId) {
		Date now = new Date();
		String time = Long.toString(now.getTime());
		UpEventReq req = null;
		boolean addData = judgeAddSetData(devid);
		if(addData){
			devAndTenantId.add(new DevAndTenantId(devid,tenantId));
		}
		ZwwlMsgSender.devAndtenid = devAndTenantId;
		//XT doesn't have move event!
		if((tenantId.equals(ZwwlAuth.tenantIdZWWL)) && (event.equalsIgnoreCase(EVENT_MOVE))){
			req = new UpEventReq(devid.toUpperCase(), time, event);
		}else{
			req = new UpEventReq(devid.toUpperCase(), time, event);
		}
		ResourseMgrListener.thirdparty.sendMessage(req);
	}
	
	public static void sendStatusEvent(String deveui,String device_staus,String tenantId) {
		Date now = new Date();
		String time = Long.toString(now.getTime());
		
		logger.info("sendStatusEvent deveui {} status {} ",deveui,device_staus);
		UpEventReq req = new UpEventReq(deveui.toUpperCase(), time, device_staus);
		boolean addData = judgeAddSetData(deveui);
		if(addData){
			devAndTenantId.add(new DevAndTenantId(deveui,tenantId));
		}
		ZwwlMsgSender.devAndtenid = devAndTenantId;
		ResourseMgrListener.thirdparty.sendMessage(req);
	}
	
	public static void sendStepsEvent(String devid,String steps,String tenantId){
		Date now = new Date();
		String time = Long.toString(now.getTime());
		
		logger.info("sendStepsEvent deveui {} steps {} ",devid.toUpperCase(),steps);
		StepsData stepData = new StepsData(steps);
		UploadStepsData data = new UploadStepsData(stepData);
		UploadStepsDataReq req = new UploadStepsDataReq(devid.toUpperCase(), time, data);
		//String rsp = JSON.toJSONString(req);
		//System.out.println(rsp);
		boolean addData = judgeAddSetData(devid);
		if(addData){
			devAndTenantId.add(new DevAndTenantId(devid,tenantId));
		}
		ZwwlMsgSender.devAndtenid = devAndTenantId;
		ResourseMgrListener.thirdparty.sendMessage(req);
		
	}
	
	public static void pushPositionData(String deveui,String longi, String lati,int move) throws Exception{
		   List<Zwwldevices> list= ZwwldevicesDAO.getZwwldeviceByDevid(deveui);
		   if(0 == list.size()){
			   logger.info("the deveui {} doesn't belong to ZWWL ",deveui);
			   return;
		   }else{
		       for(Zwwldevices devices : list){
				    if(devices.getCfgtype().equals(ZwwlReqListener.CFGMODE_POSITION)){
				    	logger.info("sendPositionData deveui {} ,longi {} ,lati {} tenanId {} ",deveui, longi, lati,devices.getTenantId());
					    ZwwlSender.sendPositionData(deveui, longi, lati,devices.getTenantId());
					    //ZwwlSender.sendListDevsAndPositionData(deveui, longi, lati);
			        }else if(devices.getCfgtype().equals(ZwwlReqListener.CFGMODE_SHAKE)){
			        	int cfgdevicemovethr = devices.getMovthr();
			        	if(move >= cfgdevicemovethr){
			        		logger.info("sendMoveEvent deveui {} move {} ,longi {} ,lati {} ",deveui,move,longi, lati);
				            ZwwlSender.sendEvent(deveui,EVENT_MOVE,devices.getTenantId());
				            ZwwlSender.sendPositionData(deveui, longi, lati,devices.getTenantId());
			        	}else{
			        		logger.info("real move{} less than cfgdevicemovethr{},needn't sendMoveEvent",move,cfgdevicemovethr);
			        	}
			      }
		     }
		 }
	}
	
	public static void pushSteps(String devid,int steps) throws Exception{
		List<Zwwldevices> list= ZwwldevicesDAO.getZwwldeviceByDevid(devid);
		if(0 == list.size()){
		   logger.info("the deveui {} doesn't belong to ZWWL ",devid);
		   return;
		}else{
		   ZwwlSender.sendStepsEvent(devid,Integer.toString(steps),list.get(0).getTenantId());
		}
	}
	
	public static void pushOutDoor(String deveui,String xGPS, String yGPS) throws Exception{
		List<Zwwldevices> list= ZwwldevicesDAO.getZwwldeviceByDevid(deveui);
		if((0 == list.size()) || (list.isEmpty())){
		    logger.info("the deveui {} doesn't belong to ZWWL ",deveui);
			return;
		}else{
			ZwwlSender.sendGPSPositionData(deveui, xGPS, yGPS,list.get(0).getTenantId());
	    }
	}
	
	public static void pushBatterythr(List<String> devid,int count) throws Exception{
		int battery = 0;
		int energycyclevalue = 0;
		int lowbatterythr = 0;
		List<Zwwldevices> list = null;
		
	     for(String deveui : devid){
	    	list = ZwwldevicesDAO.getZwwldeviceByDevid(deveui);
		    Zwwldevices zwwldev = list.get(0);
			logger.info("deveui {} in ZwwlDevices",zwwldev.getDevid());
			battery = QueryResult.querydevBattery(zwwldev.getDevid());
	    	energycyclevalue = zwwldev.getEnergycyclevalue()*2;
			lowbatterythr = zwwldev.getLowbatterythr();
			logger.info("battery {}  lowbatterythr {}",battery,lowbatterythr);
			// battery warning
			if(battery < lowbatterythr){
				 logger.info("battery {} less than lowbatterythr {}",battery,lowbatterythr);
				 ZwwlSender.sendEvent(deveui, EVENT_WARNING,zwwldev.getTenantId());
			}
			//send battery data
			logger.info("count {}  energycyclevalue {} ",count,energycyclevalue);
			if(count % energycyclevalue == 0){
		          logger.info("the deveui's {} battery {}",deveui,Integer.toString(battery));
				  ZwwlSender.sendBatterythrData(deveui, Integer.toString(battery),zwwldev.getTenantId());
			}else{
				logger.info("the deveui {} needn't to sendBatterythrData",deveui);
			}
			
	     }
	    
	}
	
	public static void pushDevFinalLocalData(List<String> onlinedev) throws Exception{
		  List<GpsData> gpsData = null;
		  String latitude = null;
		  String longitude = null;
		  String devType = null;
		  List<Zwwldevices> zwwldevicesList = null;
		  for(String zwwlonlineDev : onlinedev){
			zwwldevicesList = ZwwldevicesDAO.getZwwldeviceByDevid(zwwlonlineDev);
		    if(!(zwwldevicesList.isEmpty())){
		    	Zwwldevices zwwlDev = zwwldevicesList.get(0);
		    	String tenantId = zwwlDev.getTenantId();
		    	if((tenantId.equalsIgnoreCase(ZwwlAuth.tenantIdZWWL)) || (tenantId.equalsIgnoreCase(ZwwlAuth.tenantIdXT))){
		    		 gpsData = GpsDataDAO.getGpsDataByDevid(zwwlonlineDev);
					    if((null == gpsData) || (gpsData.size() == 0)){
					    	logger.info("the device {} doesn't have gpsData",zwwlonlineDev);
					    	continue;
					    }
					    for(GpsData gps : gpsData){
							   latitude = String.valueOf(gps.getLatitude());
							   longitude = String.valueOf(gps.getLongitude());
						}
					    devType = QueryResult.querydevType(zwwlonlineDev);
					    if(null == devType ){
					    	logger.info("devType {}",devType);
					    	return;
					    }
					    if((devType.equalsIgnoreCase(CowDevModel)) || (devType.equalsIgnoreCase(xtDevModelName))){
						  ZwwlSender.sendGPSPositionData(zwwlonlineDev,longitude, latitude,zwwlDev.getTenantId());
					   }else if(devType.equals(trackerDevModel)){
						  ZwwlSender.sendPositionData(zwwlonlineDev,longitude, latitude,zwwlDev.getTenantId());
					    }
		    	 }
			  }
		  }
	}
	
	//Bracelet data
	public static void sendGWPosAndDistanceData(String devId,String gatewayX,String gatewayY,String distance,String tenantId){
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		GWPosAndDistance gwPosAndDistance = new GWPosAndDistance(gatewayX, gatewayY, distance);
		UploadGWPosAndDistanceData  data = new UploadGWPosAndDistanceData(gwPosAndDistance);
		UploadGWPosAndDistance req = new UploadGWPosAndDistance(devId, datetime, data);
		boolean addData = judgeAddSetData(devId);
		if(addData){
			devAndTenantId.add(new DevAndTenantId(devId,tenantId));
		}
		ZwwlMsgSender.devAndtenid = devAndTenantId;
		ResourseMgrListener.thirdparty.sendMessage(req);
	}
	
	public static void sendHBAndBloodPreData(String devId,String heartBeat,String systolicPressure,String diastolicPressure,String tenantId){
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		HeartBeatAndBloodPre heartBeatAndBloodPre = new HeartBeatAndBloodPre(heartBeat+"bpm", systolicPressure+"mmHg", diastolicPressure+"mmHg");
		UploadHBAndBloodPreData data = new UploadHBAndBloodPreData(heartBeatAndBloodPre);
		UploadHBAndBloodPre req = new UploadHBAndBloodPre(devId, datetime, data);
		boolean addData = judgeAddSetData(devId);
		if(addData){
			devAndTenantId.add(new DevAndTenantId(devId,tenantId));
		}
		ZwwlMsgSender.devAndtenid = devAndTenantId;
		ResourseMgrListener.thirdparty.sendMessage(req);
	}
	
	public static void sendStepsAndCaloriesData(String devId,String step,String caloric,String tenantId){
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		StepsData steps = new StepsData(step);
		Calories calories = new Calories(caloric);
		UploadStepsAndCaloriesData data = new UploadStepsAndCaloriesData(steps, calories);
		UploadStepsAndCalories req = new UploadStepsAndCalories(devId, datetime, data);
		boolean addData = judgeAddSetData(devId);
		if(addData){
			devAndTenantId.add(new DevAndTenantId(devId,tenantId));
		}
		ZwwlMsgSender.devAndtenid = devAndTenantId;
		ResourseMgrListener.thirdparty.sendMessage(req);
	}
	
	public static boolean judgeAddSetData(String deveui){
		for(DevAndTenantId devAndTenid : devAndTenantId){
			String devid = devAndTenid.getDeveui();
			if(devid.equalsIgnoreCase(deveui)){
				return false;
			}
		}
		return true;
	}
}
