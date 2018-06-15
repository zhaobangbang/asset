package com.lans.controller.networkgw.msghandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.asset.thirdparty.zwwl.ZwwlSender;
import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.controller.networkgw.tvmessages.EndDevBracelet;
import com.lans.controller.networkgw.tvmessages.ScannedBracelet;
import com.lans.dao.BlueToothBraceletDataDAO;
import com.lans.dao.DevConfigDAO;
import com.lans.dao.GpsDataDAO;
import com.lans.dao.ZwwldevicesDAO;
import com.lans.dao.beans.BlueToothBraceletData;
import com.lans.dao.beans.BraceletData;
import com.lans.dao.beans.DevConfig;
import com.lans.dao.beans.GpsData;
import com.lans.dao.beans.Zwwldevices;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

public class EndDevBraceletHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevBraceletHandler.class);
	IGateWayConnLayer3 l3;
	private static int batteryCycleValue = 1;
	public static Map<String,BraceletData> saveData = new HashMap<String,BraceletData>();
	private Timer timer = new Timer();
	public EndDevBraceletHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}
    
	class Calculate extends TimerTask{

		@Override
		public void run() {
			boolean judValue = false;
			Map<String,BraceletData> saveFinalData = new HashMap<String,BraceletData>();
			Map<String,BraceletData> mapData = new HashMap<String,BraceletData>();
			BraceletData baBraceletData = null;
			saveFinalData = saveData;
			for(Map.Entry<String, BraceletData> entry : saveFinalData.entrySet()){
				if(mapData.isEmpty()){
				    baBraceletData = new BraceletData(entry.getValue().getDeveui(), entry.getValue().getbTime());
					mapData.put(entry.getKey(), baBraceletData);
				}else{
					for(Map.Entry<String, BraceletData> mapEntry : mapData.entrySet()){
						// mapEntry's gateway not eq entry's gateway
						if(!mapEntry.getKey().equalsIgnoreCase(entry.getKey())){
							judValue = false;
							if(mapEntry.getValue().getDeveui().equalsIgnoreCase(entry.getValue().getDeveui())){
							
								if(mapEntry.getValue().getbTime() - entry.getValue().getbTime() <= 2000){
									//conform
									
									break;
								}
							}else{
								// all entry'dev not mapEntry's dev
								judValue = true;
							}
							
						}else{
							
							// mapEntry's gateway  eq entry's gateway,put entry's dev insert mapEntry
							if(!mapEntry.getValue().getDeveui().equalsIgnoreCase(entry.getValue().getDeveui())){
								baBraceletData = new BraceletData(entry.getValue().getDeveui(), entry.getValue().getbTime());
								//error 
								mapData.put(entry.getKey(), baBraceletData);
							}
							
						}
					}
					if(judValue){
						// 遍历mapData all data,Don't including the dev
						baBraceletData = new BraceletData(entry.getValue().getDeveui(), entry.getValue().getbTime());
						mapData.put(entry.getKey(), baBraceletData);
					}
					
					
				}
			}
			
			//clear saveFinalData and mapData all data
			saveFinalData.clear();
			mapData.clear();
			
		}
		
	}
	
	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if(upMsg.getType() == TVMsgDefs.UL_DEV_BRACELET){
			return true;
		}
		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		 EndDevBracelet braceletDevid = (EndDevBracelet) upMsg;
		 braceletDevid.showTV();
		 DataBaseMgr db = DataBaseMgr.getInstance();
		 String gateway = devInfo.getEui();
		 byte bTime =0;
		 int num = 1;
		 long getDatatime = new Date().getTime();
		 for(ScannedBracelet braceletDev : braceletDevid.braceletList){
			 
			 String deveui = Integer.toHexString(braceletDev.macone).toUpperCase() + Integer.toHexString((braceletDev.mactwo & 0xffff) << 16).substring(0,4).toUpperCase();
			 if(braceletDevid.length > 1){
				 if(braceletDevid.TMOFF != null){
					 bTime = getBtime(num,braceletDevid.TMOFF);
					 num++;
				 } 
				 saveSetData(gateway,deveui,getDatatime,bTime);
			 }
			 
			 String owner = "手环";
			 String battery = Byte.toString(braceletDev.bat)+"%";
			 String rssi = Byte.toString(braceletDev.rssi);
			 Date time = new Date();
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String datetime = sdf.format(time);
			
			 String step = Short.toString(braceletDev.steps);
			 
			 String caloric = Short.toString(braceletDev.calo)+"cal";
			 
			 String heartBeat = Byte.toString(braceletDev.hb);
			 
			 String systolicPressure = Byte.toString(braceletDev.sysp);
			 
			 String diastolicPressure = Byte.toString(braceletDev.diap);
			 
			 String sos = Byte.toString(braceletDev.sos);
			 
			 //calculate Bracelet to bluetooth's distance by rssi
		 	 double d = calculateDistance(braceletDev.bat);
		     String distance = String.format("%.1f", d) + "m";
			 
		     ResultSet rs = null;
			 boolean warn_status = false;
			 String warnContent = "SOS";
			 if(!sos.equals("0")){
				 //get the sos
				 String sql = "insert into warning_record_tbl (usrname,deveui,warn_desc,warn_stime,warn_on) values ('"+owner+"','"+deveui+"','"+warnContent+"','"+datetime+"',true)";
				 int updateAffect = db.executeUpdate(sql);
				 if(updateAffect == 0)
				{
					 logger.warn("{} :Fail to insert data in database.", deveui); 
				}
			 }else{
				 // the sos is finished
				 String sql= "select * from warning_record_tbl where deveui = '"+deveui+"' ORDER BY warn_stime desc limit 0,1";
				 rs = db.executeQuery(sql);
				 try {	
					rs.beforeFirst();
					if(rs.next()){
						warn_status = rs.getBoolean("warn_on");
					}
					if(warn_status){
						warn_status = false;
						sql = "update warning_record_tbl set warn_etime = '"+datetime+"', warn_on = "+warn_status+" where deveui = '"+deveui+"' ORDER BY warn_stime desc limit 1";
						int updateAffect = db.executeUpdate(sql);
						if(updateAffect == 0)
						{
						   logger.warn("{} :Fail to update data in database.", deveui); 
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			 }
			 
			 //send data to XT
			 try {
				String gatewayX = "";
				String gatewayY = "";
				String sql = "select * from dev_list_tbl where deveui = '"+gateway+"'";
				rs = db.executeQuery(sql);
				rs.beforeFirst();
				if(rs.next()){
					gatewayX = Float.toString(rs.getFloat("lastx"));
					gatewayY = Float.toString(rs.getFloat("lasty"));
				}
						
				List<Zwwldevices> zwwldevicesList = ZwwldevicesDAO.getZwwldeviceByDevid(deveui);
				if((null != zwwldevicesList) && (!zwwldevicesList.isEmpty())){
					//update deveui's gateway information  in zwwlDeveui
					Zwwldevices zwwldevices = zwwldevicesList.get(0);
					zwwldevices.setGateway(gateway);
					ZwwldevicesDAO.update(zwwldevices);
					GpsData gpsData = null;
					String tenantId = zwwldevicesList.get(0).getTenantId();
					ZwwlSender.sendHBAndBloodPreData(deveui, heartBeat, systolicPressure, diastolicPressure, tenantId);
					ZwwlSender.sendStepsAndCaloriesData(deveui, step, caloric, tenantId);
			        
			        int lowbatterythr = zwwldevices.getLowbatterythr();
			        if(braceletDev.bat < lowbatterythr){
			        	logger.info("battery {} less than lowbatterythr {}",braceletDev.bat,lowbatterythr);
						ZwwlSender.sendEvent(deveui, ZwwlSender.EVENT_WARNING,zwwldevices.getTenantId());
			        }
					BlueToothBraceletData blueToothBraceletData = BlueToothBraceletDataDAO.getBlueToothBraceletDataByDev(deveui);
					if(null == blueToothBraceletData){
						blueToothBraceletData = new BlueToothBraceletData(deveui, step, caloric, Float.parseFloat(gatewayX),Float.parseFloat(gatewayY),
								                                           Short.parseShort(heartBeat), Short.parseShort(systolicPressure), Short.parseShort(diastolicPressure), distance,new Date(),battery+"%",Short.parseShort(rssi));
					    BlueToothBraceletDataDAO.create(blueToothBraceletData);
					    //BlueTooth Bracelet just online
					    ZwwlSender.sendGPSPositionData(deveui,gatewayY,gatewayX,tenantId);
					    ZwwlSender.sendBatterythrData(deveui, battery,zwwldevices.getTenantId());
					    ZwwlSender.sendGWPosAndDistanceData(deveui,gatewayX,gatewayY,distance,tenantId);
					    gpsData = new GpsData(owner, deveui, blueToothBraceletData.getGpsLat(), blueToothBraceletData.getGpsLong(), "实时", new Date());
					    GpsDataDAO.create(gpsData);
					}else{
						List<DevConfig> devConfigList = DevConfigDAO.getDevConfigByDevice(gateway);
						int gpsCycleValue = 0;
						if(!devConfigList.isEmpty()){
							DevConfig devConfig = devConfigList.get(0);
							gpsCycleValue  = devConfig.getGPS();
						}else{
							gpsCycleValue = 2;
						}
						Date datime = new Date();
						long nowtime = datime.getTime();
						
						if((blueToothBraceletData.getGpsLat() != Float.parseFloat(gatewayX)) && (blueToothBraceletData.getGpsLong() != Float.parseFloat(gatewayY))){
							blueToothBraceletData.setGpsLat(Float.parseFloat(gatewayX));
							blueToothBraceletData.setGpsLong(Float.parseFloat(gatewayY));
							//blueTooth Bracelet's position  different from before position
							ZwwlSender.sendGPSPositionData(deveui,gatewayY,gatewayX,tenantId);
							gpsData = new GpsData(owner, deveui, blueToothBraceletData.getGpsLat(), blueToothBraceletData.getGpsLong(), "实时", new Date());
							GpsDataDAO.create(gpsData);
						}else{
							List<GpsData> gpsDataList = GpsDataDAO.getGpsDataByDevid(deveui);
							if(!gpsDataList.isEmpty()){
								gpsData = gpsDataList.get(0);
								long gpstime = gpsData.getTime().getTime();
								int number = (int) ((nowtime - gpstime)/(gpsCycleValue * 5 * 1000));
								if((number % zwwldevices.getEnergycyclevalue()) == 0){
									ZwwlSender.sendGPSPositionData(deveui,gatewayY,gatewayX,tenantId);
								}
							}
						}
						
						//upload battery doesn't eq to battery before
						if(!blueToothBraceletData.getBattery().equals(battery)){
							ZwwlSender.sendBatterythrData(deveui, battery,zwwldevices.getTenantId());
							blueToothBraceletData.setBattery(battery);
						}else{
							//have a problem
							if((batteryCycleValue % zwwldevices.getEnergycyclevalue()) == 0){
								ZwwlSender.sendBatterythrData(deveui, battery,zwwldevices.getTenantId());
								batteryCycleValue = 0;
							}
							batteryCycleValue ++;
						}
						blueToothBraceletData.setStep(step);
						blueToothBraceletData.setCaloric(caloric);
						blueToothBraceletData.setHeartBeat(Short.parseShort(heartBeat));
						blueToothBraceletData.setSystolicPressure(Short.parseShort(systolicPressure));
						blueToothBraceletData.setDiastolicPressure(Short.parseShort(diastolicPressure));
						blueToothBraceletData.setRssi(Short.parseShort(rssi));
						blueToothBraceletData.setTime(new Date());
						BlueToothBraceletDataDAO.update(blueToothBraceletData);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		}
		 timer.schedule(new Calculate(), 0, 5*60*1000);
	}
		 
	// method about calculating distance
	private double calculateDistance(int rssi) {
	        if(0 == rssi) {
	            return -1.00;
	        }
	        double ratio = (rssi * 1.0) / (double) -63;
	        if(ratio < 1.0) {
	            return Math.pow(ratio, 10);
	        } else {
	            double distance = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
	            return distance;
	        }
	 }
	//get mapping bracelet's time
	private byte getBtime(int num,byte[] time){
		byte bTime = 0;
		if(num % 2 != 0){
			int length = (num / 2);
			bTime = (byte) (time[length] >> 4 & 0x0f);
		}else if(num % 2 == 0){
			int length = (num / 2);
			bTime = (byte) (time[length-1] >> 4 & 0x0f);
		}
		return bTime;
	}
	
	private void saveSetData(String gateway,String bDeveui,long getDataTime,byte bTime){
		
		long braTime = getDataTime - bTime * 1000;
		if(saveData.isEmpty()){
			BraceletData braceletData = new BraceletData(bDeveui, braTime);
			saveData.put(gateway, braceletData);
		}else{
			for(Map.Entry<String,BraceletData> entry : saveData.entrySet()){
				if(entry.getValue().getDeveui().equalsIgnoreCase(bDeveui)){
					return;
				}
			}
			BraceletData braceletData = new BraceletData(bDeveui, braTime);
			saveData.put(gateway, braceletData);
			logger.info("deveui {} getDatatiem {} btime {} braTime {}",bDeveui,getDataTime,bTime,braTime);
		}
	}
}
