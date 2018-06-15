package com.lans.controller.networkgw.msghandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.asset.thirdparty.zwwl.ZwwlSender;
import com.lans.beans.DevOprStatus;
import com.lans.beans.DevParamSetting;
import com.lans.beans.DevicesOperateBean;
import com.lans.beans.GpsNodeStatusBean;
import com.lans.common.DataBaseMgr;
import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.controller.networkgw.tvmessages.DLCommandReq;
import com.lans.controller.networkgw.tvmessages.DLDevLoraConfig;
import com.lans.controller.networkgw.tvmessages.DLDevModeConfig;
import com.lans.controller.networkgw.tvmessages.EndDevIndoorPos;
import com.lans.controller.networkgw.tvmessages.ScannedBeacon;
import com.lans.dao.BeaconsDAO;
import com.lans.dao.beans.Beacons;
import com.lans.infrastructure.util.Trilateration;
import com.lans.servlets.DevMsgHandler;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

import net.sf.json.JSONObject;

public class EndDevIndoorPosHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevIndoorPosHandler.class);
	IGateWayConnLayer3 l3;
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
		BeaconChooser bChooser = new BeaconChooser();
		List<BeaconNode> chooseList = null;
		//�����ź�ǿ������
		Collections.sort(devIndoorPos.beaconList, new Comparator<ScannedBeacon>() {
            public int compare(ScannedBeacon a, ScannedBeacon b) {
            	int one = a.getRssi();
            	int two = b.getRssi();
            	return new Integer(two).compareTo(new Integer(one));
            }
        });
		devIndoorPos.showTV();
		
		//check the move status, if not freeze or very slow use only the max power beacon
		byte move = devIndoorPos.move;
		String mapid = "";

		DevicesOperateBean devicesOperateBean = DevicesOperateBean.getInstance();
		DevOprStatus status = devicesOperateBean.getDevOprList().get(deveui);
		if ((move > 9) || (status.getDevWorkType().toString().contains("����"))) {//���ǿ�ȼ�Ϊ9���˴���ʱ���Ը�ǿ���˶������⴦��
			logger.warn("Device {} move too fast, {}", deveui, move);
			chooseList = new LinkedList<BeaconNode>();
			ScannedBeacon sBeacon = devIndoorPos.getRSSIBiggest();
			try {
				List<Beacons> beaconList = BeaconsDAO.getBeaconByMajorMinor(sBeacon.getMajor(), sBeacon.getMinor());
				if (!beaconList.isEmpty()) {
					chooseList.add(new BeaconNode(beaconList.get(0), sBeacon.getRssi()));
					mapid = beaconList.get(0).getFlor();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		} else if(status.getDevWorkType().toString().contains("����")){ 
			//Get the mapid of the beacon, �ն˸���ɨ����beaconȷ����ǰ������λ��
			ScannedBeacon mBeacon = devIndoorPos.beaconList.get(0);//�õ��ź���ǿ��beacon
			try {
				List<Beacons> beaconList = BeaconsDAO.getBeaconByMajorMinor(mBeacon.getMajor(), mBeacon.getMinor());
				if (!beaconList.isEmpty()) {
					mapid = beaconList.get(0).getFlor();
                   //Update the database
					String mapSql = "update dev_list_tbl set map_id=\""+ mapid +"\" where deveui=\""+deveui+"\"";
					
					DataBaseMgr db = DataBaseMgr.getInstance();
					int rowAffected = db.executeUpdate(mapSql);

			    	if(rowAffected == 0)
			    	{
			    		logger.warn("{} :Fail to update mapid in database.", deveui); 
			    	}
			    	else
			    	{
			    		logger.info("{}: map changed, {}", deveui, mapid);
			    	}
			    //Step0: ���¹���ʵʱ��Ϣ
			    //mapid��ʽ����ַ_"¥����"_"¥���"������¥��¥�����Ϣ��Ҫ�����������Ϣ
			    	if(Pattern.matches(".*_.*_.*", mapid))
			    	{
			    		String pattern = "(.*)_(.*)_(.*)";
			    		Pattern r=Pattern.compile(pattern);
			    		Matcher m=r.matcher(mapid);
			    		if(m.find())
			    		{
			    			//address = m.group(1);
			    			
					    	//�ж��ն����볡���ǳ���
					    	//Step1: �жϵ�ǰ�����ڻ�������
					    	boolean outDoor = true;
					    	for(Beacons scanedBeacon: beaconList)
					    	{   //ɨ����beacon,��һ��Ϊ�������ն˴�������
					    		if(!scanedBeacon.getPostype().equals("����"))
					    		{
					    			outDoor = false;
					    			break;
					    		}
					    	}
					    	//Step2:�����һ��λ����Ϣ
					    	String lastLocation = DevicesOperateBean.getInstance().getDevicePos(deveui);
					    	//Step3:���볡�ж�
					    	if((lastLocation.equals("δ֪") || lastLocation.equals("����")) && !outDoor)
					    	{
					    		//�볡 ����->����
					    		logger.info("dev {} enter {}", deveui, mapid);
					    		DevicesOperateBean.getInstance().setDevicePos(deveui, "����");
					    		SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    		mapSql = "update dev_list_tbl set entertime='" + shortDF.format(new Date())+ "' where deveui=\""+deveui+"\"";
								
								rowAffected = db.executeUpdate(mapSql);

						    	if(rowAffected == 0)
						    	{
						    		logger.warn("{} :Fail to update({}) entertime dev_list_tbl in database.", deveui, mapid); 
						    	}			    		
					    	}
					    	else if(lastLocation.equals("����") && outDoor)
					    	{
					    		//���� ����->����
					    		logger.info("dev {} exit {}", deveui, mapid);
					    		DevicesOperateBean.getInstance().setDevicePos(deveui, "����");
					    		SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    		mapSql = "update dev_list_tbl set exittime='" + shortDF.format(new Date())+ "' where deveui=\""+deveui+"\"";
								
								rowAffected = db.executeUpdate(mapSql);

						    	if(rowAffected == 0)
						    	{
						    		logger.warn("{} :Fail to update({}) exittime dev_list_tbl in database.", deveui, mapid); 
						    	}
					    	}
					    	else if(lastLocation.equals("����") && !outDoor && !mapid.equals(DevicesOperateBean.getInstance().getMapId(deveui)))
					    	{
					    		//�볡 ���ͼ
					    		logger.info("dev {} enter {}", deveui, mapid);
					    		SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    		mapSql = "update dev_list_tbl set entertime='" + shortDF.format(new Date())+ "' where deveui=\""+deveui+"\"";
								
								rowAffected = db.executeUpdate(mapSql);

						    	if(rowAffected == 0)
						    	{
						    		logger.warn("{} :Fail to update({}) entertime dev_list_tbl in database.", deveui, mapid); 
						    	}
					    	}
			    		}			    		
			    	}
					DevicesOperateBean.getInstance().setMapId(deveui, mapid);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			//read scanned beacon to beacon chooser
			List<ScannedBeacon> bList = devIndoorPos.beaconList;
			//Select the valid points, no more than 3 beacons are selected after this step.
			byte[] beaconsAll = new byte[bList.size()];
			byte i = 0;
			for (ScannedBeacon sBeacon : bList) {
			  beaconsAll[i] = (byte) sBeacon.getMinor();
			  i++;
			}
			
			byte[] selBeacons = DevicesOperateBean.getInstance().searchValidPoints(deveui, beaconsAll);
			if(null == selBeacons)
			{
				logger.info("No valid points searched for {}", deveui);
				//if fail to find, go on searching as old method.
				for (ScannedBeacon sBeacon : bList) {
							try {
								List<Beacons> beaconList = BeaconsDAO.getBeaconByMajorMinor(sBeacon.getMajor(), sBeacon.getMinor());
								if (!beaconList.isEmpty()) {
									bChooser.addBeacon(beaconList.get(0), sBeacon.getRssi());
								}
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
					}
			}
			else {//Will be removed, once all the maps be ready.
				for (ScannedBeacon sBeacon : bList) {
					  for(byte beacon: selBeacons)
					  {
						 if(sBeacon.getMinor() == beacon)
						 {
							try {
								List<Beacons> beaconList = BeaconsDAO.getBeaconByMajorMinor(sBeacon.getMajor(), sBeacon.getMinor());
								if (!beaconList.isEmpty()) {
									bChooser.addBeacon(beaconList.get(0), sBeacon.getRssi());
								}
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
							break;
						 }
					  }
					}				
			}

			//use beacon chooser to select beacon
			chooseList = bChooser.choose();
		}
		else{
			logger.error("{}, �������豸���������", deveui);
			return;
		}
		
		for (BeaconNode node : chooseList) {
			logger.info("choose node {}-{} x {} y {} rssi {}", node.getBeacon().getMajor(),
					node.getBeacon().getMinor(), node.getBeacon().getX(), node.getBeacon().getY(),
					node.getRssi());
		}
		
		if (chooseList.isEmpty()) {
			return;
		}
		
		//check chooseList to determine the enddev position
		double endDevX = 0;
		double endDevY = 0;
		String endDevFloor;
		short beaconNumber = 0;//��ʱΪ�豸4e demo�ã�����ɾ��
		//if only 1 choice, use it
		//if 2 points, use the average x and y
		//else search points by trilateration
		if (chooseList.size() == 1) {
			endDevX = chooseList.get(0).getBeacon().getX();
			endDevY = chooseList.get(0).getBeacon().getY();
			endDevX = endDevX + 0.05 *(int)(1 + Math.random()*10);//��ֹ��ͬ���豸ɨ��ͬһ���ű�ʱλ���ص�
			endDevY = endDevY + 0.05*(int)(1 + Math.random()*10);
			endDevFloor = chooseList.get(0).getBeacon().getFlor();
			beaconNumber = (short) chooseList.get(0).getBeacon().getMinor();//��ʱΪ�豸4e demo�ã�����ɾ��
		} else if (chooseList.size() == 2) {
			int rssi1 = Math.abs(chooseList.get(0).getRssi());
			int rssi2 = Math.abs(chooseList.get(1).getRssi());
			endDevX = (chooseList.get(0).getBeacon().getX()*rssi2 + chooseList.get(1).getBeacon().getX()*rssi1)/(rssi1 + rssi2);
			endDevY = (chooseList.get(0).getBeacon().getY()*rssi2 + chooseList.get(1).getBeacon().getY()*rssi1)/(rssi1 + rssi2);
			endDevFloor = chooseList.get(0).getBeacon().getFlor();
		} else {
			BeaconNode node1 = chooseList.get(0);
			BeaconNode node2 = chooseList.get(1);
			BeaconNode node3 = chooseList.get(2);
			
			Trilateration tri = new Trilateration();
			tri.setBeacon1(node1.getBeacon().getX(), node1.getBeacon().getY(), 
						node1.getBeacon().getA(), node1.getBeacon().getN(), node1.getRssi(), node1.getBeacon().getMinor());
			tri.setBeacon2(node2.getBeacon().getX(), node2.getBeacon().getY(), 
						node2.getBeacon().getA(), node2.getBeacon().getN(), node2.getRssi(), node2.getBeacon().getMinor());
			tri.setBeacon3(node3.getBeacon().getX(), node3.getBeacon().getY(), 
						node3.getBeacon().getA(), node3.getBeacon().getN(), node3.getRssi(), node3.getBeacon().getMinor());
			
			logger.info("Distance: beacon{}:A-{},N-{},DIS-{}; beacon{}:A-{},N-{},DIS-{};beacon{}:A-{},N-{},DIS-{}; ",node1.getBeacon().getMinor(),node1.getBeacon().getA(),node1.getBeacon().getN(),tri.getBeacon1Dist(),
					node2.getBeacon().getMinor(),node2.getBeacon().getA(),node2.getBeacon().getN(),tri.getBeacon2Dist(), 
					node3.getBeacon().getMinor(),node3.getBeacon().getA(),node3.getBeacon().getN(),tri.getBeacon3Dist());
			tri.searchPoint();
			endDevX =  tri.getXPos();
			endDevY =  tri.getYPos();
			
			if(endDevX < 1.0 && endDevY < 1.0)
				return;
			
			endDevFloor = node1.getBeacon().getFlor();
		}
		
		//filter the position calculated
		PositionFilter.getInstance().filterPosition(deveui, endDevX, endDevY, new Date(), PositionFilter.POS_TYPE_INDOOR, move);
		endDevX = PositionFilter.getInstance().getLatestX(deveui);
		endDevY = PositionFilter.getInstance().getLatestY(deveui);
		
		//inform X,Y of the enddev to observer.
		JSONObject jsonMsg = new JSONObject();
    	
    	jsonMsg.element("DevEUI", deveui);
    	jsonMsg.element("msgType", "INDOOR");
    	jsonMsg.element("xGPS", endDevX);
    	jsonMsg.element("yGPS", endDevY);
    	jsonMsg.element("mapid", endDevFloor);
    	jsonMsg.element("time", devIndoorPos.time);
    	if(deveui.equals("004a770211030061") || deveui.equals("004a770211030062"))//��ʱΪ�豸4e demo�ã�����ɾ��
    	{
    		jsonMsg.element("minor", beaconNumber);
    	}
    			
    	GpsNodeStatusBean posBean = GpsNodeStatusBean.getInstance();
    	posBean.addDevicePosition(deveui, (float)endDevX, (float)endDevY, new Date((long)devIndoorPos.time*1000));

    	String sql = "select * from dev_list_tbl where deveui=\""+deveui+"\"";
    	String owner = null;
        String alias = null;
        String worktype = null;
	   	DataBaseMgr db = DataBaseMgr.getInstance();
	   	
    	ResultSet rs = db.executeQuery(sql);
   		try{
   			rs.beforeFirst();
   			if (rs.next()) {
   				owner = rs.getString("owner");
   				alias = rs.getString("alias");
   				worktype = rs.getString("worktype");
   			}
   			else
   			{
   				logger.error("enddevRealTime(),�޴��豸�ţ�{}", deveui);
   				return;
   			}
   			rs.close();
   		  }catch(SQLException ex) {
   			logger.error("EndDevRealTimePosHandler:"+ ex.getMessage());
			return;
   			}
    	jsonMsg.element("alias", alias);
    	jsonMsg.element("worktype", worktype);
   		//�������Ƿ��и���  				
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
		
        //check if pending command request buffered
        String dev = devInfo.getEui(); //NOTES: dev and deveui may be different 
        if (CmdBufService.getInstance().cmdOfDevExist(deveui)) {
        	IEndDevItfTV command = CmdBufService.getInstance().getHeadCmdOfDev(deveui);
        	
        	l3.sendTVMsgToEndDev(dev, (byte)21, command);
        	
        	if(((DLCommandReq)command).getCommand() == 3)//�����ն�
        	    CmdBufService.getInstance().rmHeadCmdOfDev(dev);
        }
        
		java.util.Date date;
		if(0 != devIndoorPos.time)
			date = new java.util.Date((long)devIndoorPos.time * 1000);
		else
			date = new Date();
		
	    SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	sql = "insert into gps_tbl(owner,alias,deveui,latitude,longitude,baidulati,baidulong, type, time) values('"+owner + "','" + alias + "','" + deveui+
			       "','" + endDevY + "','" + endDevX + "','" + endDevY + "','" + endDevX + "','����','" +  shortDF.format(date) + "')";
    	int affectedRow = db.executeUpdate(sql);

    	if(affectedRow == 0)
    	{
    		logger.warn("{} :Fail to add Indoor position in database.", deveui); 
    	}
    	
    	try {
    		String zwwlxGPS = String.valueOf(endDevX);
    		String zwwlyGPS = String.valueOf(endDevY);
    		int zwwldevicemove = move;
    		ZwwlSender.pushPositionData(deveui.toUpperCase(), zwwlxGPS, zwwlyGPS,zwwldevicemove);
    		DevMsgHandler.updateToMap(mapid, jsonMsg);
			//DevMsgHandler.updateToObserver(deveui, jsonMsg);
			//DevMsgHandler.updateDevToObserver(deveui, jsonMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
