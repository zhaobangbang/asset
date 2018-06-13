package com.lansitec.thirdparty.app.infoHandler;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansitec.app.jsondefs.CheckAllData;
import com.lansitec.app.jsondefs.CheckDataRsp;
import com.lansitec.app.jsondefs.CheckIncludLittleData;
import com.lansitec.app.jsondefs.CheckInfoRspData;
import com.lansitec.app.jsondefs.ChekDetailInfomation;
import com.lansitec.app.jsondefs.DeveuiName;
import com.lansitec.app.jsondefs.CheckDataReq;
import com.lansitec.app.jsondefs.DevsDataReq;
import com.lansitec.app.jsondefs.DevsDataRsp;
import com.lansitec.app.jsondefs.DevsRspData;
import com.lansitec.app.jsondefs.GetDevsByMapIdReq;
import com.lansitec.app.jsondefs.HistoricalRouteDataList;
import com.lansitec.app.jsondefs.HistoricalRouteReq;
import com.lansitec.app.jsondefs.HistoricalRouteRsp;
import com.lansitec.app.jsondefs.HistoricalRouteRspData;
import com.lansitec.app.jsondefs.LoginDataRsp;
import com.lansitec.app.jsondefs.LoginReq;
import com.lansitec.app.jsondefs.MapNameAndId;
import com.lansitec.app.jsondefs.MapNameInfoReq;
import com.lansitec.app.jsondefs.MapNameInfoRsp;
import com.lansitec.app.jsondefs.MapNameInfoRspData;
import com.lansitec.app.jsondefs.SingleWarnInfoReq;
import com.lansitec.app.jsondefs.SingleWarnInfoRsp;
import com.lansitec.app.jsondefs.SingleWarnInfoRspData;
import com.lansitec.app.jsondefs.WarnInfoAllData;
import com.lansitec.app.jsondefs.WarnInfoLittleData;
import com.lansitec.app.jsondefs.WarnInfoReq;
import com.lansitec.app.jsondefs.WarnInfoRsp;
import com.lansitec.app.jsondefs.WarnInfoRspData;
import com.lansitec.app.jsondefs.WarnInfomation;
import com.lansitec.app.thirdparty.ILansiAppReqListener;
import com.lansitec.app.thirdparty.ReqProcRslt;
import com.lansitec.dao.ConstructInfoDAO;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.WarnRecordDAO;
import com.lansitec.dao.WorkerInfoDAO;
import com.lansitec.dao.beans.ConstructInfo;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.dao.beans.WarnRecord;
import com.lansitec.dao.beans.WorkerInfo;
import com.lansitec.enumlist.ReqProcRsltType;
import com.lansitec.handle.data.QueryDiffDataTblInfo;

public class LansitecAppReqListener implements ILansiAppReqListener {
    private Logger logger = LoggerFactory.getLogger(LansitecAppReqListener.class);
    private DevsDataRsp genGetDevsDataRsp(String username){
    	List<String> fieldList = new LinkedList<String>();
    	List<String> devsList = new LinkedList<String>();
    	DevsRspData data = new DevsRspData();
    	DevsDataRsp rsp = null;
		fieldList = QueryDiffDataTblInfo.getFieldListByProjMangName(username);
		if(!fieldList.isEmpty()){
		 devsList = QueryDiffDataTblInfo.getDevsByfieldOfProjManagers(fieldList);
		 if(null == devsList){
			 logger.error("Fail to get devsList {} by the fieldList {}",devsList,fieldList);
			 return null;
		 }
		 for(String deveui : devsList){
			DeveuiName deveuis = new DeveuiName(deveui);
			data.getDevsList().add(deveuis);
		 }
		}else{
			logger.error("Fail to get fieldList {} because Don't get any field by the username {}",fieldList,username);
			return null;
		}
    	Date now = new Date();
		String datetime = Long.toString(now.getTime());
    	data.setDatetime(datetime);
		rsp = new DevsDataRsp("0", data);
		return rsp;
    }
    private CheckDataRsp genCheckDataRsp(String username){
    	List<String> fieldList = new LinkedList<String>();
    	List<WorkerInfo> workerAllList = new LinkedList<WorkerInfo>();
    	List<String> constructSNList = new LinkedList<String>();
    	fieldList = QueryDiffDataTblInfo.getFieldListByProjMangName(username);
    	List<ConstructInfo> constructInfoList = null;
    	CheckDataRsp rsp = null;
    	CheckInfoRspData data = new CheckInfoRspData();
    	String workingTime = null;
    	String getOffTime = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	CheckAllData all = null;
    	try{
    		if(!fieldList.isEmpty()){
    			for(String fieldsn : fieldList){
    				constructInfoList = ConstructInfoDAO.getConstructInfoByField(fieldsn);
    			}
    			if(!constructInfoList.isEmpty()){
    				for(ConstructInfo constructInfo : constructInfoList){
    					constructSNList.add(constructInfo.getSn());
    				}
    				List<WorkerInfo> workerInfosList = WorkerInfoDAO.getAllWorkInfo();
    				if(!workerInfosList.isEmpty()){
    					for(WorkerInfo workerInfo : workerInfosList){
        					String workersnFourBytesBefore = workerInfo.getSn().substring(0,4);
        					for(String constructsn : constructSNList){
        						if(workersnFourBytesBefore.equals(constructsn)){
        							workerAllList.add(workerInfo);
        							continue;
        						}
        					}
        				}
    					if(!workerAllList.isEmpty()){
    						for(WorkerInfo workerInfo : workerAllList){
    							DevInfo devInfo = DevInfoDAO.getDevInfoByOwener(workerInfo.getSn());
    							if(null != devInfo){
    								if((null == devInfo.getEntertime()) || (devInfo.getEntertime().equals(""))){
    									workingTime = "2016-10-01 00:00:00";
    								}else{
    									workingTime = sdf.format(devInfo.getEntertime());
    								}
    								if((null == devInfo.getExittime()) || (devInfo.getExittime().equals(""))){
    									getOffTime = "2016-10-01 00:00:00";
    								}else{
    									getOffTime = sdf.format(devInfo.getExittime());
    								}
    							}else{
    								workingTime = "2016-10-01 00:00:00";
    								getOffTime = "2016-10-01 00:00:00";
    							}
    							ChekDetailInfomation checInfomation = new ChekDetailInfomation(workerInfo.getName(),workerInfo.getType().toString(), 
                                        workerInfo.getStatus().toString(), workingTime, getOffTime);
                                all  = new CheckAllData(checInfomation);
    							Date now = new Date();
                                String datetime = Long.toString(now.getTime());
                                CheckIncludLittleData info = new CheckIncludLittleData(workerInfo.getSn(), datetime, all);
                                data.getDevs().add(info);
    						}
    					}else{
    						logger.error("Fail to get the workerAllList {}",workerAllList);
    						return null;
    					}
    				}else{
    					logger.error("Fail to get all workerInfo {}",workerInfosList);
    					return null;
    				}
    				
    			}else{
    				logger.error("Fail to get the constructInfoList {}",constructInfoList);
    				return null;
    			}
    		}else{
    			logger.error("Fail to get the fieldList {} by the username {}",fieldList,username);
    			return null;
    		}
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
		rsp = new CheckDataRsp("", data);
		return rsp;
    }
    
    private WarnInfoRsp genWarnInfoRsp(String username){
    	List<String> fieldList = new LinkedList<String>();
    	List<String> devsList = new LinkedList<String>();
    	WarnInfoRspData data = new WarnInfoRspData();
    	WarnInfoRsp rsp = null;
    	WarnInfoAllData all = null;
		fieldList = QueryDiffDataTblInfo.getFieldListByProjMangName(username);
		try {
			if(!fieldList.isEmpty()){
			 devsList = QueryDiffDataTblInfo.getDevsByfieldOfProjManagers(fieldList);
			 if(null == devsList){
				 logger.error("Fail to get devsList {} by the fieldList {}",devsList,fieldList);
				 return null;
			 }
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 for(String deveui : devsList){
					WarnRecord warnRecord = WarnRecordDAO.getWarnRecordByDeveui(deveui);
					if(null != warnRecord){
						WarnInfomation warnInfomation = new WarnInfomation(warnRecord.getType().toString(), warnRecord.getDescription(), sdf.format(warnRecord.getWarn_stime()), sdf.format(warnRecord.getWarn_etime()), warnRecord.isWarn_on());
					    all = new WarnInfoAllData(warnInfomation);
					    Date now = new Date();
		                String datetime = Long.toString(now.getTime());
					    WarnInfoLittleData warnAllData = new WarnInfoLittleData(deveui, datetime, all);
					    data.getDevs().add(warnAllData);
					}
			 }
			}else{
				logger.error("Fail to get fieldList {} because Don't get any field by the username {}",fieldList,username);
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rsp = new WarnInfoRsp("0", data);
		return rsp;
    }
    
	private HistoricalRouteRsp genHistoricalRouteRsp(String devId,String timeBefore,String timeAfter){
    	List<PositionRecord> positionRecordsList = new LinkedList<PositionRecord>();
    	HistoricalRouteRspData data = new HistoricalRouteRspData();
    	HistoricalRouteRsp rsp = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
	    	if(((null == timeBefore) || (timeBefore.equals(""))) && ((null == timeAfter) || (timeAfter.equals("")))){
				positionRecordsList = PositionRecordDAO.getPositionBySQLdeveui(devId);
				if(!positionRecordsList.isEmpty()){
					for(PositionRecord positionRecord : positionRecordsList)
					{
						String mapid = positionRecord.getMapid();
						String gpsLat = Float.toString(positionRecord.getX());
						String gpsLong = Float.toString(positionRecord.getY());
						String time = sdf.format(positionRecord.getTime());
						HistoricalRouteDataList positionesList = new HistoricalRouteDataList(mapid, gpsLat, gpsLong, time);
						data.getPositionesList().add(positionesList);
					}
	                
				}else{
					logger.error("Fail to get the positionRecordsList {} by the devId {}",positionRecordsList,devId);
					return null;
				}
				
	    	}else{
	    		positionRecordsList = PositionRecordDAO.getPositionBydeveuiAndTime(devId, timeBefore, timeAfter);
	    		if(!positionRecordsList.isEmpty()){
					for(PositionRecord positionRecord : positionRecordsList)
					{
						String mapid = positionRecord.getMapid();
						String gpsLat = Float.toString(positionRecord.getX());
						String gpsLong = Float.toString(positionRecord.getY());
						String time = sdf.format(positionRecord.getTime());
						HistoricalRouteDataList positionesList = new HistoricalRouteDataList(mapid, gpsLat, gpsLong, time);
						data.getPositionesList().add(positionesList);
					}
	                
				}else{
					logger.error("Fail to get the positionRecordsList {} by the devId {} timeBefore {} timeAfter {}",positionRecordsList,devId,timeBefore,timeAfter);
					return null;
				}
	    	}
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Date now = new Date();
        String datetime = Long.toString(now.getTime());
        data.setDatetime(datetime);
        rsp = new HistoricalRouteRsp("0", data);
        return rsp;
    }
    /*private SingleCheckInfoRsp genSingleCheckInfoRsp(String worksn){
    	
    }*/
    private SingleWarnInfoRsp genSingleWarnInfoRsp(String devId){
    	SingleWarnInfoRsp rsp = null;
    	try {
			WarnRecord warnRecord = WarnRecordDAO.getWarnRecordByDeveui(devId);
			if(null == warnRecord){
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(null == warnRecord.getDescription()){
				warnRecord.setDescription("");
			}
			WarnInfomation warnInfomation = new WarnInfomation(warnRecord.getType().toString(), warnRecord.getDescription(), sdf.format(warnRecord.getWarn_stime()),sdf.format(warnRecord.getWarn_etime()), warnRecord.isWarn_on());
			Date now = new Date();
	        String datetime = Long.toString(now.getTime());
			SingleWarnInfoRspData data = new SingleWarnInfoRspData(datetime, warnInfomation);
			rsp = new SingleWarnInfoRsp("0", data);
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return rsp;
    }
    
    private WarnInfoRsp genSingleWarnInfoRspByDevAndTime(String devId,String warn_stime,String warn_etime){
    	WarnInfoRspData data = new WarnInfoRspData();
    	WarnInfoRsp rsp = null;
    	WarnInfoAllData all = null;
		try {
			String sql = "select * from warn_record_tbl where deveui='"+devId+"' and warn_stime BETWEEN '"+warn_stime+"' and '"+warn_etime+"' ORDER BY warn_stime ";
	    	List<WarnRecord> warnRecordsList = new LinkedList<WarnRecord>();
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			warnRecordsList = WarnRecordDAO.getWarnRecordsListByHQL(sql,"");
			if(!warnRecordsList.isEmpty()){
				logger.error("Fail to get the warnRecordsList {} by the devId {} warn_stime {} warn_etime {}",warnRecordsList,devId,warn_stime,warn_etime);
				return null;
			}
			for(WarnRecord warnRecord : warnRecordsList){
				if(null == warnRecord.getDescription()){
					warnRecord.setDescription("");
				}
				WarnInfomation warnInfomation = new WarnInfomation(warnRecord.getType().toString(), warnRecord.getDescription(), sdf.format(warnRecord.getWarn_stime()), sdf.format(warnRecord.getWarn_etime()), warnRecord.isWarn_on());
			    all = new WarnInfoAllData(warnInfomation);
			    Date now = new Date();
                String datetime = Long.toString(now.getTime());
			    WarnInfoLittleData warnAllData = new WarnInfoLittleData(devId, datetime, all);
			    data.getDevs().add(warnAllData);
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rsp = new WarnInfoRsp("0", data);
    	return rsp;
    }
    
    private MapNameInfoRsp genMapNameInfoRsp(String username){
    	MapNameInfoRspData data = new MapNameInfoRspData(); 
    	try {
			ProjectManagers projectManagers = ProjectmanagersDAO.getUsersManagersByUN(username);
			if(null == projectManagers){
				logger.error("the username doesn't exist in the projectManagers",username);
				return null;
			}
			List<MapNameAndId> mapsList = QueryDiffDataTblInfo.getMapidsAndMapNameByProjManageNM(username);
			if((null == mapsList) || (mapsList.isEmpty())){
				logger.error("Fail to get the mapList {} by the username {}",mapsList,username);
				return null;
			}
			for(MapNameAndId maps : mapsList){
				data.getMapsList().add(maps);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Date now = new Date();
        String datetime = Long.toString(now.getTime());
        data.setDatetime(datetime);
    	MapNameInfoRsp rsp = new MapNameInfoRsp("0", data);
    	return rsp;
    }
    
    public DevsDataRsp genDevsDataRsp(String MapId){
    	DevsRspData data = new DevsRspData();
    	MapInfo mapInfo = null;
		try {
			mapInfo = MapInfoDAO.getMapInfoByMapid(MapId);
			if(null == mapInfo){
	    		logger.error("Fail to get the mapInfo {} by the mapId {}",mapInfo,MapId);
	    		return null;
	    	}
			List<DevInfo> devInfosList = DevInfoDAO.getAllDevInfo();
			if((null == devInfosList) || (devInfosList.size() == 0)){
				logger.error("Fail to the devInfosList {}",devInfosList);
				return null;
			}
			for(DevInfo devInfo : devInfosList){
				String fielsn = devInfo.getField();
				//map sn eight bytes before is field'sn
				if(fielsn.equals(MapId.substring(0,8))){
					DeveuiName devName = new DeveuiName(devInfo.getDeveui());
					data.getDevsList().add(devName);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	DevsDataRsp rsp = new DevsDataRsp("0", data);
    	return rsp;
    }
    
    private ReqProcRsltType loginInfoReq(LoginReq projrctManagerReq,ReqProcRslt rslt){
    	String projUsername = projrctManagerReq.getUsername();
    	/*if((null == sysUsername) || (sysUsername.equals(""))){
    		logger.error("fail to get the systemManagerName {}",sysUsername);
    		return ReqProcRsltType.FAIL_NO_RESP;
    	}*/
    	String password = projrctManagerReq.getPassword();
    	/*if((null == password) || (password.equals(""))){
    		logger.error("fail to get the password {}",password);
    		return ReqProcRsltType.FAIL_NO_RESP;
    	}*/
    	LoginDataRsp rsp = null;
    	try {
			ProjectManagers projectManagers = ProjectmanagersDAO.getUsersManagersByUK(projUsername, password);
			if(null == projectManagers){
				logger.error("the sysUsername {} and password {} doesn't exist in systemManagers!",projectManagers,projUsername,password);
				rsp = new LoginDataRsp("20101", "µÇÂ¼Ê§°Ü£¡");
			}else{
				rsp = new LoginDataRsp("0", "µÇÂ¼³É¹¦£¡");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	rslt.setResp(rsp);
    	return ReqProcRsltType.SUCC_RESP_OBJ;
    }
    //get devs by the username
    private ReqProcRsltType getDevDataByUserName(DevsDataReq devsDataReq,ReqProcRslt rslt){
    	String username  = devsDataReq.getUsername();
    	DevsDataRsp rsp = genGetDevsDataRsp(username);
    	if((null == rsp) || (rsp.equals(""))){
    		return ReqProcRsltType.ERROR_RESP_OBJ;
    	}
    	rslt.setResp(rsp);
    	return ReqProcRsltType.SUCC_RESP_OBJ;
    }
    //get checkInfo by username
    private ReqProcRsltType getCheckInfoByUserName(CheckDataReq checkDataReq,ReqProcRslt rslt){
    	String username = checkDataReq.getUsername();
    	CheckDataRsp rsp = genCheckDataRsp(username);
    	if(null == rsp){
    		return ReqProcRsltType.ERROR_RESP_OBJ;
    	}
    	rslt.setResp(rsp);
    	return ReqProcRsltType.SUCC_RESP_OBJ;
    }
    
    private ReqProcRsltType getWarnInfoByUserName(WarnInfoReq warnInfoReq,ReqProcRslt rslt){
    	String username  = warnInfoReq.getUsername();
    	WarnInfoRsp rsp = genWarnInfoRsp(username);
    	if(null == rsp){
    		return ReqProcRsltType.ERROR_RESP_OBJ;
    	}
    	rslt.setResp(rsp);
    	return ReqProcRsltType.SUCC_RESP_OBJ;
    	
    }
    
    private ReqProcRsltType getHistoricalRouteInfo(HistoricalRouteReq historicalRouteReq,ReqProcRslt rslt){
    	String devId = historicalRouteReq.getDevId();
    	String timeBefore = historicalRouteReq.getTimeBefore();
    	String timeAfter = historicalRouteReq.getTimeAfter();
    	HistoricalRouteRsp rsp = genHistoricalRouteRsp(devId,timeBefore,timeAfter);
    	if(null == rsp){
    		return ReqProcRsltType.ERROR_RESP_OBJ;
    	}
    	rslt.setResp(rsp);
    	return ReqProcRsltType.SUCC_RESP_OBJ;
    }
    
    /*private ReqProcRsltType getSingleCheckInfo(SingleCheckInfoReq singleCheckInfoReq,ReqProcRslt rslt){
    	String workDay = singleCheckInfoReq.getWorkingDay();
    	String worksn = singleCheckInfoReq.getWorkerSN();
    	if((null == workDay) || (workDay.equals(""))){
    		logger.info("teh workDay is {},so that Olny searching latest check data!");
    		SingleCheckInfoRsp rsp = genSingleCheckInfoRsp(worksn);
    	}else{
    	
    	}
    }*/
    @SuppressWarnings("null")
	private ReqProcRsltType getSingleWarnInfo(SingleWarnInfoReq singleWarnInfoReq,ReqProcRslt rslt){
    	String warn_stime  = singleWarnInfoReq.getWarn_stime();
    	String warn_etime  = singleWarnInfoReq.getWarn_etime();
    	String devId = singleWarnInfoReq.getDevId();
    	Object rsp = null;
    	if(((null == warn_stime) || (warn_stime.equals(""))) && ((null == warn_etime) || (warn_etime.equals("")))){
    		rsp = genSingleWarnInfoRsp(devId);
    	}else if(((null != warn_stime) || (!warn_stime.equals(""))) && ((null != warn_etime) || (!warn_etime.equals("")))){
    		rsp = genSingleWarnInfoRspByDevAndTime(devId,warn_stime,warn_etime);
    	}else{
			return ReqProcRsltType.FAIL_NO_RESP;
    	}
    	if(null == rsp){
			return ReqProcRsltType.ERROR_RESP_OBJ;
    	}
    	rslt.setResp(rsp);	
    	return ReqProcRsltType.SUCC_RESP_OBJ;
    }
    
    private ReqProcRsltType getMapNameInfo(MapNameInfoReq mapNameInfoReq,ReqProcRslt rslt){
    	String username = mapNameInfoReq.getUsername();
    	MapNameInfoRsp rsp = genMapNameInfoRsp(username);
    	if(null == rsp){
			return ReqProcRsltType.ERROR_RESP_OBJ;
    	}
    	rslt.setResp(rsp);	
    	return ReqProcRsltType.SUCC_RESP_OBJ;
    }
    
    private ReqProcRsltType getGetDevsByMapId(GetDevsByMapIdReq getDevsByMapIdReq,ReqProcRslt rslt){
    	String mapId = getDevsByMapIdReq.getMapId();
    	DevsDataRsp rsp = genDevsDataRsp(mapId);
    	if(null == rsp){
			return ReqProcRsltType.ERROR_RESP_OBJ;
    	}
    	rslt.setResp(rsp);	
    	return ReqProcRsltType.SUCC_RESP_OBJ;
    }
	@Override
	public ReqProcRsltType handlerReq(Object obj, ReqProcRslt rslt) {
		if(obj instanceof LoginReq){
			return loginInfoReq((LoginReq)obj,rslt);
		}else if(obj instanceof DevsDataReq){
			return getDevDataByUserName((DevsDataReq)obj,rslt);
		}else if(obj instanceof CheckDataReq){
			return getCheckInfoByUserName((CheckDataReq)obj,rslt);
		}else if(obj instanceof WarnInfoReq){
			return getWarnInfoByUserName((WarnInfoReq)obj,rslt);
		}else if(obj instanceof HistoricalRouteReq){
			return getHistoricalRouteInfo((HistoricalRouteReq)obj,rslt);
		}else if(obj instanceof SingleWarnInfoReq){
			return getSingleWarnInfo((SingleWarnInfoReq)obj,rslt);
		}else if(obj instanceof MapNameInfoReq){
			return getMapNameInfo((MapNameInfoReq)obj,rslt);
		}else if(obj instanceof GetDevsByMapIdReq){
			return getGetDevsByMapId((GetDevsByMapIdReq)obj,rslt);
		}
		/*else if(obj instanceof SingleCheckInfoReq){
			//return getSingleCheckInfo((SingleCheckInfoReq)obj,rslt);
		}*/
		else{
			return ReqProcRsltType.FAIL_NO_RESP;
		}
	}
}
