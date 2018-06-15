package com.lans.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.asset.thirdparty.zwwl.ZwwlSender;
import com.lans.common.DataBaseMgr;
import com.lans.common.QueryResult;
import com.lans.dao.DevConfigDAO;
import com.lans.dao.ZwwldevicesDAO;
import com.lans.dao.beans.AcquiredemandInfo;
import com.lans.dao.beans.DevConfig;
import com.lans.dao.beans.SaveInfoSet;
import com.lans.dao.beans.Zwwldevices;
import com.lans.infrastructure.util.DevMonitor;
import com.lans.listener.ResourseMgrListener;
public class DevicesOperateBean{
	private HashMap<String, DevOprStatus> DevOprList;
	private long diff;
	private int cycleCount = 0;
	public static DevicesOperateBean instance;
	public static LinkedList <String> offlineDevList;
	private Logger logger = LoggerFactory.getLogger(DevicesOperateBean.class);
	private static final byte search5Pool[][] = {{1,2,3},{1,2,4},{1,2,5},{1,3,4},
			                                    {1,3,5},{1,4,5},{2,3,4},{2,3,5},
			                                    {2,4,5},{3,4,5}};
	private static final byte search4Pool[][] = {{1,2,3},{1,2,4},{1,3,4},{2,3,4}};
	
	public DevicesOperateBean(){
		DevOprList = new HashMap<String, DevOprStatus>();
		List<Zwwldevices> zwwldev = new LinkedList<Zwwldevices>();
		
		try {
			zwwldev = ZwwldevicesDAO.getAllZwwldevices();
			for(Zwwldevices dev : zwwldev){
				String deveui = dev.getDevid().toLowerCase();
				//blueThooth Baracelet can't update devType
				if(deveui.length() != 12){
					if(dev.getStatus().equals("0")){
						//offline dev neen't add DevOprList
						updateLastMsgTime(deveui, queryDevStatusTime(deveui));
						DevMonitor.offlineDevs.add(deveui);
					}else{
						updateOpr(deveui, DevOpr.ONLINE);
						updateLastMsgTime(deveui, new Date());
					}
			  }
			}
		} catch (Exception e) {
			logger.error("Fail to query the device of zwwldevice in DevicesOperateBean");
			e.printStackTrace();
		} 
	}
	
	public HashMap<String, DevOprStatus> getDevOprList() {
		return DevOprList;
	}

	public static DevicesOperateBean getInstance() {
		if (instance == null) {
			instance = new DevicesOperateBean();
		}
		
		return instance;
	}
	//使用synchronized块或者synchronized方法就可以标志一个监视区域
	public synchronized void updateOpr(String deveui, DevOpr devopr){
		logger.info(" update deveui {} status {}",deveui,devopr);
		//If fail to find the dev, add it.
		DevOprStatus newOpr = DevOprList.get(deveui);
		if(null == newOpr)	{
			newOpr = new DevOprStatus(devopr);
			DevOprList.put(deveui, newOpr);
		}
		else{
		    newOpr.setOpr(devopr);	
		}					
	}
	
	public synchronized void  assingToDevOprList(HashMap<String, DevOprStatus> DevOprStatusList){
		DevOprList = DevOprStatusList;
	}
	
	public synchronized DevOprStatus getDevOprStatus(String deveui)
	{
		return DevOprList.get(deveui);
	}

	public synchronized String getRegisterDev(String usrname)
	{
    	ServletContext ctx = ResourseMgrListener.getGlobalContext();
    	ObserverInfoBean userBean = (ObserverInfoBean)ctx.getAttribute("obsInfo");
    	
		String result = "[";
		for(Entry<String, DevOprStatus> entry: DevOprList.entrySet())
  		{
			if(userBean.devObservBySb(entry.getKey(), usrname) &&
					entry.getValue().getOpr() != DevOpr.OFFLINE)
			{
	  			result += "'" + entry.getKey() + "',";
			}
  		}
  		result += "]";
  		//logger.info("--getRegisterDev: " + result);
  		return result;
	}
	
	public synchronized DevOpr getDevOpr(String deveui)
	{
		DevOprStatus devOprStatus = DevOprList.get(deveui);
		if(null == devOprStatus)
		    return DevOpr.OFFLINE;
		else
			return devOprStatus.getOpr();
	}
	
	public synchronized String getDevOprStr(String deveui)
	{
		DevOprStatus devOprStatus = DevOprList.get(deveui);
		DevOpr DevState;
		if(null == devOprStatus)
			DevState = DevOpr.OFFLINE;
		else
			DevState = devOprStatus.getOpr();

		if(DevState == DevOpr.REG)
			return "REG";
		else if(DevState == DevOpr.DATARCV)
			return "DATARCV";
		else
			return "OFFLINE";
	}
	public synchronized DevOpr getDevPreOpr(String deveui)
	{
		DevOprStatus devOprStatus = DevOprList.get(deveui);
		if(null == devOprStatus)
		    return DevOpr.OFFLINE;
		else
			return devOprStatus.getPreOpr();
	}
	
	public synchronized void updateLastMsgTime(String deveui, Date dt) {
		DevOprStatus newOpr = DevOprList.get(deveui);
		String devTypeWoker = null;
		ResultSet rs;
		String sql = "select * from dev_list_tbl where deveui='" + deveui + "'";
		//dev online update devStatus
		if(null == newOpr)		{
			// deveui doesn't have the postype in DevOprList before,Need get postype in Database then set deveui's postype
			DataBaseMgr dbm = DataBaseMgr.getInstance();
			rs = dbm.executeQuery(sql);
			try {
				while(rs.next()) {
					devTypeWoker = rs.getString("postype");
				}
				rs.close();
				if((null == devTypeWoker) || (devTypeWoker.equals(""))){
					//default 三点  woker type 
					devTypeWoker = "室内三点";
				}
				newOpr = new DevOprStatus();
				newOpr.setLastMsgTime(dt);
				newOpr.setDevWorkType(DevWorkType.valueOf(devTypeWoker));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else{
		    newOpr.setLastMsgTime(dt);
		    //server start add zwwl deveui
		    if((null == newOpr.getDevWorkType()) || (newOpr.getDevWorkType().equals(""))){
		    	//dev exists in postype before,Need set dev's postType
		    	DataBaseMgr dbm = DataBaseMgr.getInstance();
				rs = dbm.executeQuery(sql);
				try {
					while(rs.next()) {
						devTypeWoker = rs.getString("postype");
					}
					rs.close();
					if(null == devTypeWoker){
						//default 三点  woker type 
						devTypeWoker = "室内三点";
					}
					newOpr.setDevWorkType(DevWorkType.valueOf(devTypeWoker));
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }

		}
	}	
	
	public synchronized Date queryDevStatusTime(String deveui){
		String statustime = null;
		Date time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String sql = "select * from dev_list_tbl where deveui=\""+deveui+"\"";
		DataBaseMgr db = DataBaseMgr.getInstance();
		ResultSet rs = db.executeQuery(sql);
		
		try{
			rs.beforeFirst();
		    if(rs.next()){
			  statustime  = rs.getString("statustime");
		  }
		  if((null == statustime) || (statustime.equals(""))){
			  statustime = "2017-09-14 13:36:17";
		  }
		  time = sdf.parse(statustime);
		}catch (Exception e) {
			logger.error("Fail to query the deveui {} statustime ：" + deveui);
			e.printStackTrace();
		}
		return time;
	}
	
	public synchronized void pushZwwlCfgDeviceHeartBeat(String devid,int stepsCycleValue){
		List<DevConfig> devConfig = null;
		try {
			devConfig = DevConfigDAO.getDevConfigByDevice(devid);
			if((null == devConfig) || (devConfig.isEmpty())){
				logger.error("Fail to query the devid {}",devid);
			}
			logger.info("stepsCycleValue {} compare to HB {} ",stepsCycleValue,devConfig.get(0).getHB());
			//stepsCycleValue(计步周期值)若和数据库中的HB值不一致，说明有改动
			if(stepsCycleValue != devConfig.get(0).getHB()){
				//把被配置得devid最新时间信息时间更新为当前时间
				updateLastMsgTime(devid,new Date());
			}else{
				logger.info("stepsCycleValue {} equal to HB {}",stepsCycleValue,devConfig.get(0).getHB());
			}
		} catch (Exception e) {
			logger.error("pushZwwlCfgDeviceHeartBeat try/catch error in DevicesOperateBean ");
			e.printStackTrace();
		}
	
	}
	
	public synchronized void scanTimeOutDev(LinkedList <String> offlineDevs) {
		Date now = new Date();
		List<DevConfig> devconfig = null;
		//Set<String> devSet = HashSet<String>();
		List<Zwwldevices> zwwldev = null;
		List<String> zwwldevlist = new LinkedList<String>();
		String status = null;
		HashMap<String, DevOprStatus> saveOfflineDev = null;
		saveOfflineDev = DevOprList;
		for(Entry<String, DevOprStatus> entry: DevOprList.entrySet()){
			if (null == entry.getValue().getLastMsgTime()) {
				 entry.getValue().setLastMsgTime(queryDevStatusTime(entry.getKey()));
				continue;
			}
			
			diff = now.getTime()-entry.getValue().getLastMsgTime().getTime();
			
			try {
				devconfig = DevConfigDAO.getDevConfigByDevice(entry.getKey());
				if(null == devconfig || devconfig.isEmpty())
					continue;
				
				DevConfig devc = devconfig.get(0);
				if(null != devc)
				{//30 is the heartbeat interval unit, means the least interval is 30s.
					for(AcquiredemandInfo acquInfo: SaveInfoSet.acqmandSet){
						if(acquInfo == null){
							logger.info("acquInfo {}",acquInfo);
							break;
						}
						if(entry.getKey().equalsIgnoreCase(acquInfo.getDevid())){
							//logger.info("curDev.getName {}  acquInfo.getDevid {}",curDev.getName(),acquInfo.getDevid());
							long diffTimeNum = QueryResult.getValidConfigHeartBeat(entry.getKey(),acquInfo.getMgsTimeBefore(),acquInfo);
							logger.info("diffTimeNum {}  2*acquInfo.getCfgHB {}  diff {}",diffTimeNum,
									     2*acquInfo.getCfgHB(),(diffTimeNum / (2*acquInfo.getCfgHB()))); 
							//确保差值在2*cfgHB的时间内可以上报下次心跳
							if((diffTimeNum / (2*acquInfo.getCfgHB())) > 1){
								updateLastMsgTime(entry.getKey(),new Date());
								continue;
							}
							acquInfo.setDevid(null);
							SaveInfoSet.judge(acquInfo);
						}
					}
					if(diff > 3*devc.getHB()*30*1000){
				        if (DevOpr.OFFLINE != entry.getValue().getOpr()) {
					      logger.info("devices {} time out {} ", entry.getKey(), diff);
					      entry.getValue().setOpr(DevOpr.OFFLINE);
					      offlineDevs.add(entry.getKey());
					      saveOfflineDev.remove(entry.getKey());
				        }
					}else{
                       if(null != offlineDevs && offlineDevs.size() > 0)
						  offlineDevs.remove(entry.getKey());
						//logger.info("dev {} diff {} less than HBtime {}",curDev.getName(), diff,3*devc.getHB()*30*1000);
			        }
		        }
				if(entry.getValue().getDevWorkType() != null){
					String workType = entry.getValue().getDevWorkType().toString();
					if(null != workType && workType.equals("网关")){
						//deveui eq to gateway
						logger.info("the deveui {} 's workType {} ,Needn't to send data !",entry.getKey(),workType);
						continue;
					}
				}
				zwwldev = ZwwldevicesDAO.getZwwldeviceByDevid(entry.getKey());
				if((null == zwwldev) || (zwwldev.size() == 0)){
					logger.debug("the deveui {} doesn't exist in zwwldevices",entry.getKey());
				}else{
					Zwwldevices devzwwl = zwwldev.get(0);
					status = devzwwl.getStatus();
					//终端位为上线状态
					if(status.equals("1")){
						//将上线终端加入zwwldevlist中,最终传入到上报电量事件中
						zwwldevlist.add(entry.getKey());
						logger.info("zwwldevlist add the device {}",entry.getKey());
					}else{
						for(String offDev : zwwldevlist){
							if(offDev.equals(entry.getKey())){
								zwwldevlist.remove(entry.getKey());
								logger.info("reomve the devid {} from zwwldevlist",entry.getKey());
							}
						}
					}
				}
		
		} catch (Exception e) {
				logger.error("scanTimeOutDev try/catch error in DevicesOperateBean");
			e.printStackTrace();
		}
			
		}
		DevOprList = saveOfflineDev;
		try {
			if(!zwwldevlist.isEmpty()){
				ZwwlSender.pushBatterythr(zwwldevlist,cycleCount);
				//battery周期累加值
				cycleCount++;
				if(cycleCount == 2880){
					cycleCount = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		offlineDevList = offlineDevs;
	}
	public synchronized boolean scanTimeOutByDev(String dev) {
	    for(String device : offlineDevList){				    	
		      if(dev.equalsIgnoreCase(device)){
			          return true;  
		        }				      
		    }
	    return false;
	}
	
	public synchronized boolean setMapId(String deveui, String mapid)
	{
		DevOprStatus curDev = DevOprList.get(deveui);
		if(null != curDev)
			return curDev.setMapId(deveui, mapid);

		return false;
	}
	
	public synchronized String getMapId(String deveui)
	{
		DevOprStatus curDev = DevOprList.get(deveui);
		if(null != curDev)
			return curDev.getMapId();

		return "";
	}
	
	public synchronized String getDevicePos(String deveui)
	{
		DevOprStatus curDev = DevOprList.get(deveui);
		if(null != curDev)
			return curDev.getPosition();
		
		return "";
	}
	
	public synchronized boolean setDevicePos(String deveui, String location)
	{
		DevOprStatus curDev = DevOprList.get(deveui);
		if(null != curDev){
			curDev.setPosition(location);
		    return true;
		}
		return false;
	}
	//search2point: 如果没有找到三点的合法点，继续寻找两点的合法点
	private byte[] search3Points(DevOprStatus curDev, byte[]input, boolean search2point)
	{
		PositionValid3Points[] p3Array = curDev.getValid3points();
		if(null == p3Array)
			return null;
		
    	for(PositionValid3Points p3: p3Array)
    	{
    		byte[] valid3Points = p3.getValid3points();
    		if((input[0] == valid3Points[0] && input[1] == valid3Points[1] && input[2] == valid3Points[2]) ||
    		   (input[0] == valid3Points[0] && input[1] == valid3Points[2] && input[2] == valid3Points[1]) ||
    		   (input[0] == valid3Points[1] && input[1] == valid3Points[0] && input[2] == valid3Points[2]) ||
    		   (input[0] == valid3Points[1] && input[1] == valid3Points[2] && input[2] == valid3Points[0]) ||
    		   (input[0] == valid3Points[2] && input[1] == valid3Points[0] && input[2] == valid3Points[1]) ||
    		   (input[0] == valid3Points[2] && input[1] == valid3Points[1] && input[2] == valid3Points[0]))
    		{
    			logger.info("valid 3 points found, {},{},{}", input[0],input[1],input[2]);
				return input;
    		}
    	}
    	
    	if(search2point)
    	{
	    	PositionValid2Points[] p2Array = curDev.getValid2points();
	    	if(null == p2Array)
	    		return null;
	    	
	    	for(PositionValid2Points p2: p2Array)
	    	{
	    		byte[] valid2Points = p2.getValid2points();
	    		byte[] selBytes = new byte[2];
	    		if((input[0] == valid2Points[0] && input[1] == valid2Points[1]) ||
	    				(input[0] == valid2Points[1] && input[1] == valid2Points[0]))
	    		{				
					selBytes[0] = input[0];
					selBytes[1] = input[1];
					return selBytes;
	    		}
	    		else if((input[0] == valid2Points[0] && input[2] == valid2Points[1]) ||
	    				(input[0] == valid2Points[1] && input[2] == valid2Points[0]))
	    		{
					selBytes[0] = input[0];
					selBytes[1] = input[2];
					return selBytes;
	    		}
	    		else if((input[1] == valid2Points[0] && input[2] == valid2Points[1]) ||
	    				(input[2] == valid2Points[1] && input[1] == valid2Points[0]))
	    		{
					selBytes[0] = input[1];
					selBytes[1] = input[2];
					return selBytes;
	    		}
	    	}
    	}
    	return null;
	}
	
	//从三点中寻找合法的两点
	private byte[] search2Points(DevOprStatus curDev, byte[]input)
	{		
    	PositionValid2Points[] p2Array = curDev.getValid2points();
    	if(null == p2Array)
    		return null;
    	
    	for(PositionValid2Points p2: p2Array)
    	{
    		byte[] valid2Points = p2.getValid2points();
    		byte[] selBytes = new byte[2];
    		if((input[0] == valid2Points[0] && input[1] == valid2Points[1]) ||
    				(input[0] == valid2Points[1] && input[1] == valid2Points[0]))
    		{				
				selBytes[0] = input[0];
				selBytes[1] = input[1];
				return selBytes;
    		}
    		else if((input[0] == valid2Points[0] && input[2] == valid2Points[1]) ||
    				(input[0] == valid2Points[1] && input[2] == valid2Points[0]))
    		{
				selBytes[0] = input[0];
				selBytes[1] = input[2];
				return selBytes;
    		}
    		else if((input[1] == valid2Points[0] && input[2] == valid2Points[1]) ||
    				(input[2] == valid2Points[1] && input[1] == valid2Points[0]))
    		{
				selBytes[0] = input[1];
				selBytes[1] = input[2];
				return selBytes;
    		}
    	}
    	return null;
	}
	
	public synchronized byte[] searchValidPoints(String deveui, byte input[])
	{
		DevOprStatus curDev = DevOprList.get(deveui);
		if(null != curDev){
				if(input.length == 1)
				{
					return input;
				}
				else if(input.length == 2)
			    {
			    	PositionValid2Points[] p2Array = curDev.getValid2points();
			    	if(null == p2Array)
			    		return null;
			    	
			    	for(PositionValid2Points p2: p2Array)
			    	{
			    		byte[] valid2Points = p2.getValid2points();
			    		if((input[0] == valid2Points[0] && input[1] == valid2Points[1]) ||
			    				(input[0] == valid2Points[1] && input[1] == valid2Points[0]))
			    		{
			    			logger.info("deveui {}, valid 2 points found, {}, {}",deveui, input[0],input[1]);
							return input;
			    		}
			    	}
			    }	
			    else if(input.length == 3)
			    {
			    	return search3Points(curDev, input, true);
			    }
			    else if(input.length == 4)
			    {
			    	byte[] selBytes = null;
			    	for(byte[] s3p: search4Pool)
			    	{
			    		byte[] newInputs = new byte[3];
			    		newInputs[0] = input[s3p[0]-1];
			    		newInputs[1] = input[s3p[1]-1];
			    		newInputs[2] = input[s3p[2]-1];
			    		
			    		selBytes = search3Points(curDev, newInputs, false);
			    		if(null != selBytes)
			    			return selBytes;
			    	}

			    	for(byte[] s3p: search4Pool)
			    	{
			    		byte[] newInputs = new byte[3];
			    		newInputs[0] = input[s3p[0]-1];
			    		newInputs[1] = input[s3p[1]-1];
			    		newInputs[2] = input[s3p[2]-1];

                        selBytes = search2Points(curDev, newInputs);
			    		if(null != selBytes)
			    			return selBytes;
			    	}	    			    			
			    }
			    else if(input.length >= 5)
			    {
			    	for(byte[] s3p: search5Pool)
			    	{
			    		byte[] newInputs = new byte[3];
			    		newInputs[0] = input[s3p[0]-1];
			    		newInputs[1] = input[s3p[1]-1];
			    		newInputs[2] = input[s3p[2]-1];
			    		
			    		byte[] selBytes = search3Points(curDev, newInputs, false);
			    		if(null != selBytes)
			    			return selBytes;
			    	}
			    	for(byte[] s3p: search5Pool)
			    	{
			    		byte[] newInputs = new byte[3];
			    		newInputs[0] = input[s3p[0]-1];
			    		newInputs[1] = input[s3p[1]-1];
			    		newInputs[2] = input[s3p[2]-1];
			    		
			    		byte[] selBytes = search2Points(curDev, newInputs);
			    		if(null != selBytes)
			    			return selBytes;
			    	}	
			    }
			}
		return null;
	}

	
}
