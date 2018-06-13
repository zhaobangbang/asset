package com.lansitec.controller.networkgw.msghandler;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;
import com.lansitec.beans.DevParamSetting;
import com.lansitec.beans.GpsNodeStatusBean;
import com.lansitec.controller.networkgw.TVMsgDefs;
import com.lansitec.controller.networkgw.msghandler.DescribingCircle.Point;
import com.lansitec.controller.networkgw.tvmessages.DLDevLoraConfig;
import com.lansitec.controller.networkgw.tvmessages.DLDevModeConfig;
import com.lansitec.controller.networkgw.tvmessages.EndDevIndoorPos;
import com.lansitec.controller.networkgw.tvmessages.ScannedBeacon;
import com.lansitec.dao.BeaconsDAO;
import com.lansitec.dao.DevConfigDAO;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.StatusRecordDAO;
import com.lansitec.dao.WarnRecordDAO;
import com.lansitec.dao.beans.Beacons;
import com.lansitec.dao.beans.DevConfig;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.dao.beans.StatusRecord;
import com.lansitec.dao.beans.WarnRecord;
import com.lansitec.enumlist.WarnType;
import com.lansitec.servlets.DevMsgHandler;

import net.sf.json.JSONObject;

public class EndDevIndoorPosHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevIndoorPosHandler.class);
	IGateWayConnLayer3 l3;
	public int entertime = 0;
	public int existtime = 0;
	public EndDevIndoorPosHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if (upMsg.getType() == TVMsgDefs.UL_DEV_INDOOR_POS) {
			return true;
		}

		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		EndDevIndoorPos devIndoorPos = (EndDevIndoorPos)upMsg;
		String deveui = DevMsgHandler.toDevEui(devInfo.getEui());
		float endDevX = 0;
		float endDevY = 0;
		String mapid = null;
		String battery = null;
		short rssi = 0;
	    byte vib = 0;
	    int hb = 0;
	    LocalDateTime statutime = null;
	    DevInfo devsInfos = null;
		//PersonTrackerBeaconChooser beaconChooser = new PersonTrackerBeaconChooser();
		List<PersonTrackerBeanconNode> chooseList = null;
		//根据信号强度排序
		Collections.sort(devIndoorPos.beaconList, new Comparator<ScannedBeacon>() {
            public int compare(ScannedBeacon a, ScannedBeacon b) {
            	int one = a.getRssi();
            	int two = b.getRssi();
            	return new Integer(two).compareTo(new Integer(one));
            }
        });
		devIndoorPos.showTV();
		try {
			DevConfig devConfig = DevConfigDAO.getDevConfigByDevice(deveui);
			if(null == devConfig){
				logger.error("Fail to get the devConfigInfo {} by the deveui {}",devConfig,deveui);
			}else{
				hb = devConfig.getHB();
			}
			devsInfos = DevInfoDAO.getDevInfoByDeveui(deveui);
			if(null == devsInfos){
				logger.error("Fail to get the devInfos {} by the deveui {}",devsInfos,deveui);
			}else{
				statutime = devsInfos.getStatustime();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Date nowtime = new Date();
		//virtual out of rang warning and three times scance the same beacon of enter or exist
		for(ScannedBeacon scannedBeacon : devIndoorPos.beaconList){
			if((scannedBeacon.getMajor() == 88) && (scannedBeacon.getMinor() == 99)){
				WarnRecord warnRecord = new WarnRecord(deveui, WarnType.valueOf("越界"), "所在位置超出范围", LocalDateTime.now(), false);
				WarnRecordDAO.create(warnRecord);
				logger.info("get the warnInfo of the deveui {} warnType {}",deveui,WarnType.valueOf("越界"));
			}else if((scannedBeacon.getMajor() == 11) && (scannedBeacon.getMinor() == 00)){
				// Scaning the beacon three times continuously that shows worker enters construction site
				if(((nowtime.getTime() - statutime.toInstant(ZoneOffset.of("+8")).toEpochMilli())/hb) >=2){
					logger.info("(nowtime {} - latestStatutime {} of the deveui {} ) / hb {} more than 2*hb {} "
							+ "so that scanning the beacon {} - {} time beyond rang",nowtime,statutime,hb,2*hb,scannedBeacon.getMajor(),scannedBeacon.getMinor());
				}else{
					entertime++;
				}
				if(entertime == 3){
					logger.info("the deveui {} enters construction site!",deveui);
					devsInfos.setEntertime(LocalDateTime.now());
					DevInfoDAO.update(devsInfos);
					
				}
			}else if((scannedBeacon.getMajor() == 22) && (scannedBeacon.getMinor() == 00)){
				// Scaning the beacon three times continuously that shows worker exist construction site
				if((nowtime.getTime() - statutime.toInstant(ZoneOffset.of("+8")).toEpochMilli())/hb >=2){
					logger.info("(nowtime {} - latestStatutime {} of the deveui {} ) / hb {} more than 2*hb {} "
							+ "so that scanning the beacon {} - {} time beyond rang",nowtime,statutime,hb,2*hb,scannedBeacon.getMajor(),scannedBeacon.getMinor());
				}else{
					existtime++;
				}
				if(existtime == 3){
					logger.info("the deveui {} exsits construction site!",deveui);
					devsInfos.setExittime(LocalDateTime.now());
					DevInfoDAO.update(devsInfos);
				}
			}
			else{
				try {
					WarnRecord warnRecord = WarnRecordDAO.getWarnRecordByDeveui(deveui);
					if(null != warnRecord){
						if(!warnRecord.isWarn_on()){
							warnRecord.setWarn_on(true);
							warnRecord.setDescription("所在位置符合范围要求，警告取消！");
							warnRecord.setWarn_etime(LocalDateTime.now());
							WarnRecordDAO.update(warnRecord);
							logger.info("Canceling the warnInfo of the deveui {}",deveui);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		chooseList = new LinkedList<PersonTrackerBeanconNode>();
		ScannedBeacon sBeacon = devIndoorPos.getRSSIBiggest();
		try {
			List<Beacons> beaconList = BeaconsDAO.getAllBeacons();
			if((null == beaconList) || (beaconList.size() == 0)){
				logger.error("Fail to get the beacons {}",beaconList);
			}else{
				for(Beacons beacons : beaconList){
					String beaconsn = beacons.getSn();
					int major = Integer.parseInt(beaconsn.substring(4, beaconsn.length()-2));
					int minor = Integer.parseInt(beaconsn.substring(beaconsn.length()-2, beaconsn.length()));
					mapid = beacons.getMapid();
					if((sBeacon.getMajor() == major) && (sBeacon.getMinor() == minor)){
						chooseList.add(new PersonTrackerBeanconNode(beacons, sBeacon.getRssi()));
						break;
					}
				}
				
				//walk normally to get the position(x,y)
				DescribingCircle describingCircle = new DescribingCircle();
				if(!chooseList.isEmpty()){
					PersonTrackerBeanconNode personTrackerBeanconNode = chooseList.get(0);
					float x = personTrackerBeanconNode.getBeacons().getX();
					float y = personTrackerBeanconNode.getBeacons().getY();
					float R = 7; 
				    mapid = personTrackerBeanconNode.getBeacons().getMapid();
					MapInfo mapInfo = MapInfoDAO.getMapInfoByMapid(mapid);
					if((null == mapInfo) || (mapInfo.equals(""))){
						logger.error("Fail to get the mapInfo {} by the mapId {}",mapInfo,mapid);
					}else{
						int width = mapInfo.getWidth();
						int length = mapInfo.getLength();
						describingCircle.initGetPointsCircular(x, y, R, length, width);
					}
					
				}
				if(!describingCircle.points.isEmpty()){
					Point point = describingCircle.points.get(0);
					endDevX = point.x;
					endDevY = point.y;
				}
				DevInfo devInfos = DevInfoDAO.getDevInfoByDeveui(deveui);
				if(null == devInfos){
					logger.error("Fail to get the devInfos {} by the deveui {}",devInfos,deveui);
					battery = "";
				}else{
					battery = devInfos.getBattery();
				}
				
				StatusRecord statusRecord = StatusRecordDAO.getStatusRecordDataByDev(deveui);
				if(null == statusRecord){
					logger.error("Fail to get the statusRecord {} by the deveui {}",statusRecord,deveui);
				}else{
					vib = devIndoorPos.move;//scan beacon get the devIndoor's move value
					if(vib != 0){
						statusRecord.setVib(vib);
						StatusRecordDAO.update(statusRecord);
					}
					rssi = statusRecord.getRssi();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		//inform X,Y of the enddev to observer.
		JSONObject jsonMsg = new JSONObject();
    	
		java.util.Date date;
		if(0 != devIndoorPos.time)
			date = new java.util.Date((long)devIndoorPos.time * 1000);
		else
			date = new Date();
		
	    SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String dateTime = shortDF.format(date);
		
		//battery vib rssi
    	jsonMsg.element("DevEUI", deveui);
    	jsonMsg.element("msgType", "REG");
    	jsonMsg.element("battery", battery);
    	String vibt = null;
    	if(vib == 0){
    		vibt = "静止";
    	}else{
    		vibt = "运动强度："+Byte.toString(vib);
    	}
    	jsonMsg.element("vib", vibt);
    	jsonMsg.element("rssi", rssi);
    	jsonMsg.element("x", endDevX);
    	jsonMsg.element("y", endDevY);
    	jsonMsg.element("time", dateTime);
    			
    	GpsNodeStatusBean posBean = GpsNodeStatusBean.getInstance();
    	posBean.addDevicePosition(deveui, (float)endDevX, (float)endDevY, date);

   		//检查参数是否有更新  				
		DevParamSetting devParam = new DevParamSetting();
		devParam.readDevParams(deveui);
		
		if(EndDevHBHandler.getEndDevParamStatus(deveui).paramSent == false)
		{		
			EndDevHBHandler.getEndDevParamStatus(deveui).count1++;
			if(EndDevHBHandler.getEndDevParamStatus(deveui).count1 % 10 == 1)
			{
			byte[] params = new byte[20];
			int len = 0;		

			DLDevLoraConfig devLora = new DLDevLoraConfig(devParam.ADR, devParam.CLAAMODE, devParam.DATARATE,
														(byte)0, devParam.DRSCHEME, devParam.POWER);
			byte[] bDevLora = devLora.getBytes();
			System.arraycopy(bDevLora, 0, params, len, bDevLora.length);
			len += bDevLora.length;		

			DLDevModeConfig devMode = new DLDevModeConfig(devParam.LOSTPOINT, devParam.SELFADAPT, devParam.ONEOFF,
					                                    devParam.ALREPORT,devParam.GPS, (byte)devParam.HEARTBEAT);
			byte[] bDevMode = devMode.getBytes();
			System.arraycopy(bDevMode, 0, params, len, bDevMode.length);
			len += bDevMode.length;
			
			byte[] sentParams = new byte[len];
			System.arraycopy(params, 0, sentParams, 0, len);
			l3.sendRawBytesToEndDev(devInfo.getEui(), (byte) 21, sentParams);
			
			if(EndDevHBHandler.getEndDevParamStatus(deveui).count1 > 10)
			    EndDevHBHandler.getEndDevParamStatus(deveui).count1 = 0;
			}
		}
		if(EndDevHBHandler.getEndDevParamStatus(deveui).paramSent)
		{
			EndDevHBHandler.getEndDevParamStatus(deveui).count1 = 0;
		}
		
    	try {
    		//create position_record_tbl's data           
    		PositionRecord positionRecord = new PositionRecord(deveui, mapid, endDevX, endDevY, LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    		PositionRecordDAO.create(positionRecord);
    		DevMsgHandler.updateToMapId(deveui, jsonMsg);
			//DevMsgHandler.updateToObserver(deveui, jsonMsg);
			DevMsgHandler.updateDevToObserver(deveui, jsonMsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
