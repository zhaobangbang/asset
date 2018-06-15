package com.lans.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.beans.GpsNodeStatusBean;
import com.lans.common.DataBaseMgr;
import com.lans.common.LansUtil;
import com.lans.controller.networkgw.msghandler.BeaconChooser;
import com.lans.controller.networkgw.msghandler.BeaconNode;
import com.lans.controller.networkgw.msghandler.PositionFilter;
import com.lans.controller.networkgw.tvmessages.EndDevIndoorPos;
import com.lans.controller.networkgw.tvmessages.ScannedBeacon;
import com.lans.dao.BeaconsDAO;
import com.lans.dao.beans.Beacons;
import com.lans.infrastructure.util.Trilateration;
import com.lans.servlets.DevMsgHandler;
import com.lansi.assetp2p.init.IAppliCallback;
import com.lansi.assetp2p.msgdefs.EndPosition;

import net.sf.json.JSONObject;



public class DevAssetP2PHandler implements IAppliCallback {
	Logger logger = LoggerFactory.getLogger(DevAssetP2PHandler.class);
	private byte lastPos[][] = {
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0}
	};
	
	static private byte pos3Array[][] = {
			{63,64,89,90},
			{64,65,88,89},
			{65,66,87,88},
			{66,67,86,87},
			{67,68,85,86},
			{68,111,84,85},
			{111,70,83,84},
			{70,71,82,83},
			{71,72,81,82},
			{72,73,102,81},
			{73,74,101,102},
			{74,98,100,101},
			{98,75,77,100},
			{98,76,77,100},
			{98,75,76,100}	
/*			{63,64,65},
			{64,65,66},
			{65,66,67},
			{66,67,68},
			{67,68,111},
			{68,111,70},
			{111,70,71},
			{70,71,72},
			{71,72,73},
			{72,73,74},
			{73,74,98},
			{74,98,75}*/
	};	
	static private byte pos2Array[][] = {
			{63,90},
			{64,89},
			{65,88},
			{66,87},
			{67,86},
			{68,85},
			{111,84},
			{70,83},
			{71,82},
			{72,81},
			{73,102},
			{74,101},
			{98,100},
			{98,76},
			{75,76},
			{76,77},
			{76,100},
			{75,77},
			{98,77},
			{75,100}
			/*{63,64},
			{64,65},
			{65,66},
			{66,67},
			{67,68},
			{68,111},
			{111,70},
			{70,71},
			{71,72},
			{72,73},
			{73,74},
			{74,98},
			{98,75}*/
			///////////
/*			{63,89},
			{64,90},
			{64,88},
			{65,89},
			{65,87},
			{66,88},
			{66,86},
			{67,87},
			{67,85},
			{68,86},
			{68,84},
			{111,85},
			{111,83},
			{70,84},
			{70,82},
			{71,83},
			{71,81},
			{72,82},
			{72,102},
			{73,81},
			{73,101},
			{74,102},
			{74,100},
			{98,101},
			{63,64},
			{64,65},
			{65,66},
			{66,67},
			{67,68},
			{68,111},
			{111,70},
			{70,71},
			{71,72},
			{72,73},
			{73,74},
			{74,98},
			{98,75},
			{77,100},
			{100,101},
			{101,102},
			{102,81},
			{81,82},
			{82,83},
			{83,84},
			{84,85},
			{85,86},
			{86,87},
			{87,88},
			{88,89},
			{89,90}*/
	};
/*	static private byte pos3Array[][] = {
			{104,80,106,109},
			{80,69,107,106},
			{69,79,97,107}		
	};	
	static private byte pos2Array[][] = {
			{104,109},
			{80,106},
			{69,107},
			{79,97},
			{104,106},
			{80,109},
			{80,107},
			{69,106},
			{69,97},
			{79,107}
	};*/
	public DevAssetP2PHandler() {
		// TODO Auto-generated constructor stub
	}
    
	private boolean isValidPos3(byte b1, byte b2, byte b3)
	{
		byte posTimes = 0;
		for(int i=0; i< pos3Array.length; i++)
		{
			posTimes = 0;
			for(int j=0; j<pos3Array[i].length; j++)
			{
				if(b1 == pos3Array[i][j])
				{
					posTimes++;
					break;
				}
			}
			for(int j=0; j<pos3Array[i].length; j++)
			{
				if(b2 == pos3Array[i][j])
				{
					posTimes++;
					break;
				}
			}
			for(int j=0; j<pos3Array[i].length; j++)
			{
				if(b3 == pos3Array[i][j])
				{
					posTimes++;
					break;
				}
			}
			
			if(posTimes == 3)
				return true;
		}

		
		return false;
	}
	
	private boolean isValidPos2(byte b1, byte b2)
	{
		byte posTimes = 0;
		for(int i=0; i< pos2Array.length; i++)
		{
			posTimes = 0;
			for(int j=0; j<pos2Array[i].length; j++)
			{
				if(b1 == pos2Array[i][j])
				{
					posTimes++;
					break;
				}
			}
			for(int j=0; j<pos2Array[i].length; j++)
			{
				if(b2 == pos2Array[i][j])
				{
					posTimes++;
					break;
				}
			}
			if(posTimes == 2)
				return true;
		}

		
		return false;
	}
	public void onPositionReport(String dev, short vol, short rssi, byte length, byte move, EndPosition[] endP) {
		//logger.info("onTempHumReport - dev {} temp1 {} temp2 {} rssi {}", arg0, arg1, arg2, arg3);
		String sql = "select * from dev_list_tbl where deveui=\""+dev+"\"";
    	String owner = null;

	   	DataBaseMgr db = DataBaseMgr.getInstance();
	   	
    	ResultSet rs = db.executeQuery(sql);
   		try{
   			rs.beforeFirst();
   			if (rs.next()) {
   				owner = rs.getString("owner");
   			}
   			else
   				return;
   		  }catch(SQLException ex) {
   				logger.error("queryAlarm(),无此设备号：" + dev);
   				return;
   			}
		java.util.Date date = new java.util.Date();
	    SimpleDateFormat shortDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    
		sql = "update dev_list_tbl set battery=\""+ vol +"\", rssi=\"-"+ rssi+ "\", statustime=\""+ shortDF.format(date)+"\" where deveui=\""+dev+"\"";

    	int affectedRow = db.executeUpdate(sql);

    	if(affectedRow == 0)
    	{
    		LansUtil.LogWarning(dev + "(p2p):Fail to add hb info in database."); 
    	}
    	
	    sql = "insert into status_record_tbl(owner,deveui,battery,rssi,snr, gps,vib,time) values('"+owner + "','" + dev+
			       "','" + vol + "','-" + rssi + "','" + 0 + "','" + "Indoor" + 
					"','"+ move + "','" + shortDF.format(date) + "')";
	    affectedRow = db.executeUpdate(sql);

        if(affectedRow == 0)
        {
 	        logger.warn("{} (p2p) :Fail to add status record in database.", dev); 
        }
        
        if(length <= 0)
        	return;
       
        byte lastPosIndex = 0;
        if(dev.equals("ef010001"))
        	lastPosIndex = 0;
        else if(dev.equals("ef010002"))
        	lastPosIndex = 1;
        else if(dev.equals("ef010003"))
        	lastPosIndex = 2;
        else if(dev.equals("ef010004"))
        	lastPosIndex = 3;
        else if(dev.equals("ef010005"))
        	lastPosIndex = 4;
        
        List<ScannedBeacon> list = new LinkedList<ScannedBeacon>();
		for (byte idx = 0; idx < length; idx++) {
			int major = (int)endP[idx].getMajor();
			int minor = (int)endP[idx].getMinor();
			int _rssi = endP[idx].getRssi();
			
			ScannedBeacon beacon = new ScannedBeacon(major, minor, _rssi);
			list.add(beacon);
		}
		
		EndDevIndoorPos devIndoorPos = new EndDevIndoorPos(length, move, 0, list);
		BeaconChooser bChooser = new BeaconChooser();
		List<BeaconNode> chooseList = null;
		
		devIndoorPos.showTV();
		
		//check the move status, if not freeze or very slow use only the max power beacon
		if (move > 9) {
			chooseList = new LinkedList<BeaconNode>();
			ScannedBeacon sBeacon = devIndoorPos.getRSSIBiggest();
			try {
				List<Beacons> beaconList = BeaconsDAO.getBeaconByMajorMinor(sBeacon.getMajor(), sBeacon.getMinor());
				if (!beaconList.isEmpty()) {
					chooseList.add(new BeaconNode(beaconList.get(0), sBeacon.getRssi()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		} else {
			//read scanned beacon to beacon chooser
			List<ScannedBeacon> bList = devIndoorPos.beaconList;
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
			//use beacon chooser to select beacon
			chooseList = bChooser.choose();
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
		byte endPath = 0;
		
		//if only 1 choice, use it
		//if 2 points, use the average x and y
		//else search points by trilateration
		if (chooseList.size() == 1) {
/*			endDevX = chooseList.get(0).getBeacon().getX();
			endDevY = chooseList.get(0).getBeacon().getY();
			endDevFloor = chooseList.get(0).getBeacon().getFlor();*/
			return;
		} else if (chooseList.size() == 2) {
			byte b1 = (byte) chooseList.get(0).getBeacon().getMinor();
			byte b2 = (byte) chooseList.get(1).getBeacon().getMinor();
			/*if((b1 == 63 && b2 == 90) || (b1 == 90 && b2 == 63))
				endPath = 2;
			else if((b1 == 75 && b2 == 76) || (b1 == 76 && b2 == 75))
				endPath = 1;*/
			if(!isValidPos2(b1, b2))
				return;
			
			if(lastPos[lastPosIndex][0] != 2)
			{
				lastPos[lastPosIndex][0] = 2;
				lastPos[lastPosIndex][1] = b1;
				lastPos[lastPosIndex][2] = b2;
				lastPos[lastPosIndex][3] = 0;
			}
			else
			{
				byte b11 = lastPos[lastPosIndex][1];
				byte b22 = lastPos[lastPosIndex][2]; 

				if((b1  == b11 && b2 == b22) || (b1 == b22 && b2 == b11))
				{	
				lastPos[lastPosIndex][0] = 0;
				lastPos[lastPosIndex][1] = 0;
				lastPos[lastPosIndex][2] = 0;
				lastPos[lastPosIndex][3] = 0;
				logger.info("the same: {},{}", b1, b2);
				return;
				}
				else
				{
					lastPos[lastPosIndex][1] = b1;
					lastPos[lastPosIndex][2] = b2;
					lastPos[lastPosIndex][3] = 0;
				}
			}
			endDevX = ((chooseList.get(0).getBeacon().getX() + chooseList.get(1).getBeacon().getX())/2);
			endDevY = ((chooseList.get(0).getBeacon().getY() + chooseList.get(1).getBeacon().getY())/2);
			endDevFloor = chooseList.get(0).getBeacon().getFlor();
		} else {
			BeaconNode node1 = chooseList.get(0);
			BeaconNode node2 = chooseList.get(1);
			BeaconNode node3 = chooseList.get(2);
			
			byte b1 = (byte) node1.getBeacon().getMinor();
			byte b2 = (byte) node2.getBeacon().getMinor();
			byte b3 = (byte) node3.getBeacon().getMinor();
			
/*			if((b1==63 && (b2 == 90 || b3 == 90)) ||
					(b2==63 && (b1 == 90 || b3 == 90)) ||
					   (b3==63 && (b1 == 90 || b2 == 90)))
				endPath = 2;
			else if((b1==75 && (b2 == 76 || b3 == 76)) ||
					(b2==75 && (b1 == 76 || b3 == 76)) ||
					   (b3==75 && (b1 == 76 || b2 == 76)))
				endPath = 1;*/
			
			if(!isValidPos3(b1, b2, b3))
				return;
			
			if(lastPos[lastPosIndex][0] != 3)
			{
				lastPos[lastPosIndex][0] = 3;
				lastPos[lastPosIndex][1] = b1;
				lastPos[lastPosIndex][2] = b2;
				lastPos[lastPosIndex][3] = b3;
			}
			else
			{
				byte b11 = lastPos[lastPosIndex][1];
				byte b22 = lastPos[lastPosIndex][2];
				byte b33 = lastPos[lastPosIndex][3];
				if((b1  == b11 && b2 == b22 && b3 == b33) || (b1 == b22 && b2 == b11 && b3 == b33) ||
						(b1 == b11 && b2 == b33 && b3 == b22) || (b1 == b22 && b2 == b33 && b3 == b11) ||
						   (b1 == b33 && b2 == b11 && b3 == b22) || (b1 == b33 && b2 == b22 && b3 == b11))
				{	
					lastPos[lastPosIndex][0] = 0;
						lastPos[lastPosIndex][1] = 0;
							lastPos[lastPosIndex][2] = 0;
							lastPos[lastPosIndex][3] = 0;
					logger.info("the same: {},{},{}", b1, b2,b3);
				return;
				}
				else
				{
					lastPos[lastPosIndex][1] = b1;
					lastPos[lastPosIndex][2] = b2;
					lastPos[lastPosIndex][3] = b3;
				}
			}
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
			
			if(endDevX < 1 && endDevY < 1)
				return;
			
			endDevFloor = node1.getBeacon().getFlor();			
		}
		
		//filter the position calculated
		PositionFilter.getInstance().filterPosition(dev, endDevX, endDevY, new Date(), PositionFilter.POS_TYPE_INDOOR, move);
		endDevX = PositionFilter.getInstance().getLatestX(dev);
		endDevY = PositionFilter.getInstance().getLatestY(dev);
		
		//inform X,Y of the enddev to observer.
		JSONObject jsonMsg = new JSONObject();
    	
    	jsonMsg.element("DevEUI", dev);
    	jsonMsg.element("msgType", "INDOOR");
    	jsonMsg.element("xGPS", endDevX);
    	jsonMsg.element("yGPS", endDevY);
    	jsonMsg.element("endPath", endPath);
    	jsonMsg.element("floor", endDevFloor);
    	jsonMsg.element("time", devIndoorPos.time);
    	GpsNodeStatusBean posBean = GpsNodeStatusBean.getInstance();
    	posBean.addDevicePosition(dev, (float)endDevX, (float)endDevY, new Date((long)devIndoorPos.time*1000));

		if(0 != devIndoorPos.time)
			date = new java.util.Date((long)devIndoorPos.time * 1000);
		else
			date = new Date();
		
    	sql = "insert into gps_tbl(owner,deveui,latitude,longitude,baidulati,baidulong, type, time) values('"+owner + "','" + dev+
			       "','" + endDevY + "','" + endDevX + "','" + endDevY + "','" + endDevX + "','室内','" + shortDF.format(date) + "')";
    	
    	affectedRow = db.executeUpdate(sql);

    	if(affectedRow == 0)
    	{
    		logger.warn("{}(p2p) :Fail to add Indoor position in database.", dev); 
    	}
    	
    	try {
			DevMsgHandler.updateToObserver(dev, jsonMsg);
			DevMsgHandler.updateDevToObserver(dev, jsonMsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
