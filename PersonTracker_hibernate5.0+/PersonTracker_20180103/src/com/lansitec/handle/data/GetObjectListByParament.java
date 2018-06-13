package com.lansitec.handle.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import com.lansitec.dao.AssetInfoDAO;
import com.lansitec.dao.CityInfoDAO;
import com.lansitec.dao.CompanyInfoDAO;
import com.lansitec.dao.ConstructInfoDAO;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.FieldInfoDAO;
import com.lansitec.dao.GatewayInfoDAO;
import com.lansitec.dao.LogRecordDAO;
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.StatusRecordDAO;
import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.WarnRecordDAO;
import com.lansitec.dao.WorkerInfoDAO;
import com.lansitec.dao.beans.AssetInfo;
import com.lansitec.dao.beans.CityInfo;
import com.lansitec.dao.beans.CompanyInfo;
import com.lansitec.dao.beans.ConstructInfo;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.FieldInfo;
import com.lansitec.dao.beans.GatewayInfo;
import com.lansitec.dao.beans.LogRecord;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.dao.beans.StatusRecord;
import com.lansitec.dao.beans.SystemManagers;
import com.lansitec.dao.beans.WarnRecord;
import com.lansitec.dao.beans.WorkerInfo;

public class GetObjectListByParament {
	  public static List<SystemManagers> getSystemManagerListByParament(int page,int row,String hql,int paramentNum,String paramentNameOne,String paramentValueOne,String paramentNameTwo,String paramentValueTwo){
		   List<SystemManagers> managersList = new LinkedList<SystemManagers>();
		   try {
			   managersList = SystemManagersDAO.getSystemManagInfoByHQL(hql,page,paramentNum,paramentNameOne,paramentValueOne,paramentNameTwo,paramentValueTwo,0,0);
			   if(!managersList.isEmpty()){
				 ParamentSave.records = managersList.size();
			   }
			   if(page != 1){
				   int start = ParamentSave.getParamentStart(page, row);
				   managersList = SystemManagersDAO.getSystemManagInfoByHQL(hql,page,paramentNum,paramentNameOne,paramentValueOne,paramentNameTwo,paramentValueTwo,start,row);
				}
			   if(!managersList.isEmpty()){
				   for(SystemManagers systemManagers : managersList){
					   if((null == systemManagers.getTel()) || (systemManagers.getTel().equals(""))){
						   systemManagers.setTel("NA");
					   }
				   }
			   }
		   } catch (Exception e) {
			  e.printStackTrace();
		   }
		   
		  return managersList;
	   }
	  
	  public static List<CityInfo> getCityInfoListByParament(int page,int row,String hql){
		  List<CityInfo> cityInfosList = new LinkedList<CityInfo>();
		  try{
			cityInfosList = CityInfoDAO.getCityInfoByHQL(page,hql,0,0);
      		if(!cityInfosList.isEmpty()){
      			ParamentSave.records = cityInfosList.size();
      		}
		    if(page != 1){
      		  int start = ParamentSave.getParamentStart(page, row);
  		 	  cityInfosList = CityInfoDAO.getCityInfoByHQL(page,hql,start,row);
	        }
		  }catch (Exception e) {
			e.printStackTrace();
		}
		return cityInfosList;
	  }
	  
	  public static List<CompanyInfo> getCompanyInfoListByParament(int page,int row,String hql){
		  List<CompanyInfo> companyInfosList = new LinkedList<CompanyInfo>();
		  try{
			  companyInfosList = CompanyInfoDAO.getCompanyInfoByHQLParament(page,hql,0,0);
			  if(!companyInfosList.isEmpty()){
				  ParamentSave.records =companyInfosList.size();
			  }
			  if(page != 1){
				  int start = ParamentSave.getParamentStart(page, row);
				  companyInfosList = CompanyInfoDAO.getCompanyInfoByHQLParament(page,hql,start,row);
			  }
			  if(!companyInfosList.isEmpty()){
				  for(CompanyInfo companyInfo : companyInfosList){
					  if((null == companyInfo.getMemo()) || (companyInfo.getMemo().equals(""))){
						  companyInfo.setMemo("NA");
					  }
				  }
			  }
		  }catch (Exception e) {
			e.printStackTrace();
		}
		  return companyInfosList;
	  }
     public static List<GatewayInfo> getGatewayInfoListByParamnet(int page,int row,String hql){
    	 List<GatewayInfo> gatewayInfosList = new LinkedList<GatewayInfo>();
    	 try {
			gatewayInfosList = GatewayInfoDAO.getGatewayInfoByHQLParament(page,hql,0,0);
			if(!gatewayInfosList.isEmpty()){
				ParamentSave.records = gatewayInfosList.size();
			}
			if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
		  		gatewayInfosList = GatewayInfoDAO.getGatewayInfoByHQLParament(page,hql,start,row);
			}
			if(!gatewayInfosList.isEmpty()){
				for(GatewayInfo gatewayInfo : gatewayInfosList){
		    		 String field = gatewayInfo.getField();
		    		 String fieldName = QueryDiffDataTblInfo.getTblNMByDiffsn("field", field);
		    		 gatewayInfo.setField(fieldName);
		    	 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	 
    	 return gatewayInfosList;
     }
       
     public static List<DevInfo> getDevInfoListByParament(int page,int row,int paramentNum,String paramentNameOne,String paramentValueOne,String hql){
    	 List<DevInfo> devInfosList = new LinkedList<DevInfo>();
    	 try {
			devInfosList = DevInfoDAO.getDevInfoByHQLParament(page,paramentNum,paramentNameOne,paramentValueOne,hql,0,0);
			if(!devInfosList.isEmpty()){
				ParamentSave.records = devInfosList.size();
			}
			if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
		  		devInfosList = DevInfoDAO.getDevInfoByHQLParament(page,paramentNum,paramentNameOne,paramentValueOne,hql,start,row);
			}
			if(!devInfosList.isEmpty()){
				for(DevInfo devInfo : devInfosList){
					String field = devInfo.getField();
					String fieldName = QueryDiffDataTblInfo.getTblNMByDiffsn("field", field);
					String mapid = devInfo.getMapid();
					String mapName = QueryDiffDataTblInfo.getTblNMByDiffsn("mapid", mapid);
					devInfo.setField(fieldName);
					devInfo.setMapid(mapName);
					String devType = devInfo.getDevtype().toString();
					if(!devType.equals("Œ¥∑÷≈‰")){
						String owner = devInfo.getOwner();
						String ownerName = QueryDiffDataTblInfo.getTblNMByDiffsn("owner", owner);
						devInfo.setOwner(ownerName);
					}
					if((null == devInfo.getAlias()) || (devInfo.getAlias().equals(""))){
						devInfo.setAlias("NA");
					}
					if((null == devInfo.getBattery()) || (devInfo.getBattery().equals(""))){
						devInfo.setBattery("NA");
					}
					if((null == devInfo.getMemo()) || (devInfo.getMemo().equals(""))){
						devInfo.setMemo("NA");
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	 return devInfosList;
     }
     
     public static List<StatusRecord> getStatusRecordsListByParament(int page,int row,int paramentNum,String paramentNameOne,String paramentValueOne,String hql){
    	 List<StatusRecord> statusRecordsList = new LinkedList<StatusRecord>();
    	 try {
			  statusRecordsList = StatusRecordDAO.getStatusRecordByDiffParament(page,paramentNum,paramentNameOne,paramentValueOne,hql,0,0);
			  if(!statusRecordsList.isEmpty()){
				  ParamentSave.records = statusRecordsList.size();
			  }
    		  if(page != 1){
    			  int start = ParamentSave.getParamentStart(page, row);
  		  		  statusRecordsList = StatusRecordDAO.getStatusRecordByDiffParament(page,paramentNum,paramentNameOne,paramentValueOne,hql,start,row);
    		  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return statusRecordsList;
     }
                                                                              
     public static List<PositionRecord> getPositionRecordsListByParament(int page,int row,int paramentNum,String paramentNameOne,String paramentValueOne,String hql){
    	 List<PositionRecord> positionRecordsList = new LinkedList<PositionRecord>();
    	 try {
			positionRecordsList = PositionRecordDAO.getPositionDataByHQLParament(page,paramentNum,paramentNameOne,paramentValueOne,hql,0,0);
			if(!positionRecordsList.isEmpty()){
				ParamentSave.records =  positionRecordsList.size();
			}
			if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
		  		positionRecordsList = PositionRecordDAO.getPositionDataByHQLParament(page,paramentNum,paramentNameOne,paramentValueOne,hql,start,row);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return positionRecordsList;
     }
     
     public static List<WarnRecord> getWarnRecordsListByParament(int page,int row,String hql){
    	 List<WarnRecord> warnRecordsList = new LinkedList<WarnRecord>();
    	 try {
			warnRecordsList = WarnRecordDAO.getWarnRecordsListByHQLParament(page,hql,0,0);
			if(!warnRecordsList.isEmpty()){
				ParamentSave.records = warnRecordsList.size();
			}
			if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
		  		warnRecordsList = WarnRecordDAO.getWarnRecordsListByHQLParament(page,hql,start,row);
			}
			if(!warnRecordsList.isEmpty()){
				DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String date = "2016-10-01 00:00:00";
				for(WarnRecord warnRecord : warnRecordsList){
					if((null == warnRecord.getDescription()) || (warnRecord.getDescription().equals(""))){
						warnRecord.setDescription("NA");
					}
					if((null == warnRecord.getWarn_etime()) || (warnRecord.getWarn_etime().equals(""))){
						warnRecord.setWarn_etime(LocalDateTime.parse(date, df));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return warnRecordsList;
     }
     
     public static List<LogRecord> getLogRecordsListByParament(int page,int row,String hql){
    	 List<LogRecord> logRecordsList = new LinkedList<LogRecord>();
    	 try {
			logRecordsList = LogRecordDAO.getLogRecordsByHQLParament(page,hql,0,0);
		    if(!logRecordsList.isEmpty()){
			   ParamentSave.records = logRecordsList.size();
		    }
			if(page != 1){
	    		 int start = ParamentSave.getParamentStart(page, row);
			  	 logRecordsList = LogRecordDAO.getLogRecordsByHQLParament(page,hql,start,row);
	    	 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return logRecordsList;
     }
     
     public static List<ConstructInfo> getConstructInfosByParament(int page,int row,String hql){
    	 List<ConstructInfo> constructInfosList = new LinkedList<ConstructInfo>();
    	 try {
			constructInfosList = ConstructInfoDAO.getConstructInfosByHQLParament(page,hql,0,0);
			if(!constructInfosList.isEmpty()){
				ParamentSave.records = constructInfosList.size();
			}
			if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
			  	constructInfosList = ConstructInfoDAO.getConstructInfosByHQLParament(page,hql,start,row);
			}
			if(!constructInfosList.isEmpty()){
				for(ConstructInfo constructInfo : constructInfosList){
					if((null == constructInfo.getMemo()) || (constructInfo.getMemo().equals(""))){
						constructInfo.setMemo("NA");
					}
					FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoBySN(constructInfo.getField());
					if(null != fieldInfo){
						constructInfo.setField(fieldInfo.getName());
					}
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return constructInfosList;
     }                                               
     public static List<ProjectManagers> getProjectManagersByParament(int page,int row,int paramentNum,String paramentNameOne,String paramentValueOne,String paramentNameTwo,String paramentValueTwo,String hql){
    	 List<ProjectManagers> projectManagersList = new LinkedList<ProjectManagers>();
    	 try {
    		 projectManagersList = ProjectmanagersDAO.getUsersManagersByHQLParament(page,paramentNum,paramentNameOne,paramentValueOne,paramentNameTwo,paramentValueTwo,hql,0,0);
			 if(!projectManagersList.isEmpty()){
				ParamentSave.records = projectManagersList.size();
			 }
			 if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
			  	projectManagersList = ProjectmanagersDAO.getUsersManagersByHQLParament(page,paramentNum,paramentNameOne,paramentValueOne,paramentNameTwo,paramentValueTwo,hql,start,row);
			}
			if(!projectManagersList.isEmpty()){
				for(ProjectManagers projectManagers :projectManagersList){
					String companysn = projectManagers.getCompany();
					String companyName = QueryDiffDataTblInfo.getTblNMByDiffsn("company", companysn);
					String fieldsn = projectManagers.getField();
					String fieldName = QueryDiffDataTblInfo.getTblNMByDiffsn("field", fieldsn);
					projectManagers.setCompany(companyName);
					projectManagers.setField(fieldName);
					if((null == projectManagers.getTel()) || (projectManagers.getTel().equals(""))){
						projectManagers.setTel("NA");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return projectManagersList;
     }
     
     public static List<WorkerInfo> getWorkerInfosByParament(int page,int row,String hql){
    	 List<WorkerInfo> workerInfosList = new LinkedList<WorkerInfo>();
    	 try {
			workerInfosList = WorkerInfoDAO.getWorkerInfosByHQLParament(page,hql,0,0);
			if(!workerInfosList.isEmpty()){
				ParamentSave.records = workerInfosList.size();
			}
			if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
			  	workerInfosList = WorkerInfoDAO.getWorkerInfosByHQLParament(page,hql,start,row);
			}
			if(!workerInfosList.isEmpty()){
				for(WorkerInfo workerInfo : workerInfosList){
					if((null == workerInfo.getMemo()) || (workerInfo.getMemo().equals(""))){
						workerInfo.setMemo("NA");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return workerInfosList;
     }
    public static List<AssetInfo> getAssetInfoByParament(int page,int row,String hql){
    	List<AssetInfo> assetInfosList = new LinkedList<AssetInfo>();
    	try {
			assetInfosList = AssetInfoDAO.getAssetInfoByHQLParament(page,hql,0,0);
			if(!assetInfosList.isEmpty()){
				ParamentSave.records = assetInfosList.size();
			}
			if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
			  	assetInfosList = AssetInfoDAO.getAssetInfoByHQLParament(page,hql,start,row);
			}
			if(!assetInfosList.isEmpty()){
				for(AssetInfo assetInfo : assetInfosList){
					if((null == assetInfo.getMemo()) || (assetInfo.getMemo().equals(""))){
						assetInfo.setMemo("NA");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return assetInfosList;
    }
    public static List<FieldInfo> getFieldInfosByParament(int page,int row,String hql){
    	List<FieldInfo> fieldInfosList = new LinkedList<FieldInfo>();
    	try {
			fieldInfosList = FieldInfoDAO.getFieldInfoByHQLParament(page,hql,0,0);
			if(!fieldInfosList.isEmpty()){
				ParamentSave.records = fieldInfosList.size();
			}
			if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
			  	fieldInfosList = FieldInfoDAO.getFieldInfoByHQLParament(page,hql,start,row);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      return fieldInfosList;	
    }
    public static List<MapInfo> getMapInfosByParament(int page,int row,String hql){
    	List<MapInfo> mapInfosList = new LinkedList<MapInfo>();
    	try {
			mapInfosList = MapInfoDAO.getMapInfosByHQLParament(page,hql,0,0);
			if(!mapInfosList.isEmpty()){
				ParamentSave.records = mapInfosList.size();
			}
			if(page != 1){
				int start = ParamentSave.getParamentStart(page, row);
			  	mapInfosList = MapInfoDAO.getMapInfosByHQLParament(page,hql,start,row);
			}
			if(!mapInfosList.isEmpty()){
				for(MapInfo mapInfo : mapInfosList){
					if((null == mapInfo.getMemo()) || (mapInfo.getMemo().equals(""))){
						mapInfo.setMemo("NA");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return mapInfosList;
    }
} 
