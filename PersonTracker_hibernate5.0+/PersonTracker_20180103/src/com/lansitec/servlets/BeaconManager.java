package com.lansitec.servlets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lansitec.dao.BeaconsDAO;
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.beans.Beacons;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.enumlist.Alarmtype;
import com.lansitec.handle.data.QueryDiffDataTblInfo;
import com.lansitec.springmvc.beans.BeaconsTblBean;

@Controller
@RequestMapping("/BeaconManager")
public class BeaconManager {
	private Logger logger = LoggerFactory.getLogger(BeaconManager.class);
	
	@RequestMapping("doGet")
	@ResponseBody
	protected List<Beacons> doGet(@RequestParam String mapid){
		List<Beacons> beaconsTblList = null;
		try {
			beaconsTblList = BeaconsDAO.getBeaconsByMapid(mapid);
			if((null == beaconsTblList) || (beaconsTblList.size() == 0)){
				logger.error("Fail to get beaconsTblList {} in beacons_tbl!",beaconsTblList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return beaconsTblList;
	}
	
	@RequestMapping("doPost")
	@ResponseBody
	protected String doPost(BeaconsTblBean beaconsTblBase) throws Exception{
		
		String sn = beaconsTblBase.getSn();
		String mapid = beaconsTblBase.getMapid();
		MapInfo mapInfo = MapInfoDAO.getMapInfoByMapid(mapid);
		int width = mapInfo.getWidth();
		int length = mapInfo.getLength();
		String x = beaconsTblBase.getX();
		String y = beaconsTblBase.getY();
		String alias = beaconsTblBase.getAlias();
		String oper = beaconsTblBase.getOper();
		String result = "";
		
		switch(oper){
			case "add":{
				logger.info("request add beancons!");
				//x beyond width
				if((Float.parseFloat(x) < 0) || (Float.parseFloat(x) > width) ){
					logger.error("add the beacon'x {}  out the map's width rang ( 0 - {} ) £¡so that Fail to add the beacon! Major {} Minor {}",x,width,beaconsTblBase.getMajor(),beaconsTblBase.getMinor());
					return "2";
				}
				//y beyond length
				if((Float.parseFloat(y) < 0) || (Float.parseFloat(y) > length)){
					logger.error("add the beacon'y {}  out the map's length rang ( 0 - {}) £¡so that Fail to add the beacon! Major {} Minor {}",y,length,beaconsTblBase.getMajor(),beaconsTblBase.getMinor());
					return "2";
				}
				String major = beaconsTblBase.getMajor();
				String minor = beaconsTblBase.getMinor();
				String alarmtype = beaconsTblBase.getAlarmtype();
				String rssi1 = beaconsTblBase.getRssi1();
				String rssi2 = beaconsTblBase.getRssi2();
				String a = beaconsTblBase.getA();
				String n = beaconsTblBase.getN();
				if((null == a)|| (a.equals(""))){
					a = "0";
				}
				if((null == n)|| (n.equals(""))){
					n = "0";
				}
				if((null == rssi1) || (rssi1.equals(""))){
					rssi1 = "1";
				}
				if((null == rssi2) || (rssi2.equals(""))){
					rssi2 = "0";
				}
				String companysn = QueryDiffDataTblInfo.getCompanySNByBeaconsMapidSN(mapid);
				if(null == companysn){
					logger.error("Fail to get the companysn {} by mapid {}",companysn,mapid);
					return "1";
				}else{
					String newsn = companysn+major+minor;
					Beacons beaconsTbl = new Beacons(newsn, alias, mapid, Float.parseFloat(x), Float.parseFloat(y), 
							                Byte.parseByte(rssi1), Byte.parseByte(rssi2), Float.parseFloat(a), Float.parseFloat(n), Alarmtype.valueOf(alarmtype));
					logger.info("add beansInfo sn {} alias {} x {} y {} rssi1 {} rssi2 {} a {} n {} alarmtype {}",newsn,alias,x,y,rssi1,rssi2,a,n,alarmtype);
					BeaconsDAO.create(beaconsTbl);
					result="0";
				}
				break;
			}
			case "del":{
				logger.info("request delete the beancons !");
				try {
					Beacons beaconsTbl = BeaconsDAO.getBeaconsBySN(sn);
					if(null == beaconsTbl){
						logger.error("Fail to get the beaconsTblList {} and can't delete the beacon {}",beaconsTbl,sn);
						return "1";
					}else{
						BeaconsDAO.delete(beaconsTbl);
						logger.info("sucess to delete the beacon {}",sn);
						result="0";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			case "edit":{
				logger.info("request editor the beancons information that has not x and y!");
				Beacons beaconsTbl = null;
				try {
					beaconsTbl = BeaconsDAO.getBeaconsBySN(sn);
					if(null == beaconsTbl){
						logger.error("Fail to get the beaconsTblList {} and can't update to the beacon {}",beaconsTbl,sn);
						return "1";
					}else{
						String rssi1 = beaconsTblBase.getRssi1();
						String rssi2 = beaconsTblBase.getRssi2();
						String alarmtype = beaconsTblBase.getAlarmtype();
						if((null == alias) || (alias.equals(""))){
							alias = beaconsTbl.getAlias();
						}
						if((null == rssi1) || (rssi1.equals(""))){
							rssi1 = Byte.toString(beaconsTbl.getRssi1());
						}
						if((null == rssi2) || (rssi2.equals(""))){
							rssi2 = Byte.toString(beaconsTbl.getRssi2());
						}
						beaconsTbl.setAlias(alias);
						beaconsTbl.setAlarmtype(Alarmtype.valueOf(alarmtype));
						beaconsTbl.setRssi1(Byte.parseByte(rssi1));
						beaconsTbl.setRssi2(Byte.parseByte(rssi2));
						BeaconsDAO.update(beaconsTbl);
						logger.info("update beacons {} alias {} rssi1 {} rssi2 {}",sn,alias, rssi1, rssi2);
						result="0";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			   }
				break;
			}
		  case "posEdit":{
			  logger.info("request posEdit the beancons!");
			  //x beyond width
			  if((Float.parseFloat(x) < 0) || (Float.parseFloat(x) > width) ){
					logger.error("editing the beacon'x {}  out the map's width rang ( 0 - {} )£¡",x,width);
					return "2";
			  }
			  //y beyond length
			  if((Float.parseFloat(y) < 0) || (Float.parseFloat(y) > length)){
					logger.error("editing the beacon'y {}  out the map's length rang( 0 - {} )  £¡",y,length);
					return "2";
			  }
			  Beacons beaconsTbl = null;
			  try {
				 beaconsTbl = BeaconsDAO.getBeaconsBySN(sn);
				 if(null == beaconsTbl){
					logger.error("Fail to get the beaconsTblList {} and can't update to the beacon {}",beaconsTbl,sn);
					return "1";
				 }else{
					beaconsTbl.setX(Float.parseFloat(x));
					beaconsTbl.setY(Float.parseFloat(y));
					BeaconsDAO.update(beaconsTbl);
					logger.info("update beacons {} x {} y {}",sn,x,y);
					result="0";
				 }
			  } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    }
		  break;
		  }
		}
	  return result;
	}
}
