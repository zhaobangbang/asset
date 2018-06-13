package com.lansitec.handle.data;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansitec.app.jsondefs.MapNameAndId;
import com.lansitec.dao.AssetInfoDAO;
import com.lansitec.dao.CompanyInfoDAO;
import com.lansitec.dao.ConstructInfoDAO;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.FieldInfoDAO;
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.WorkerInfoDAO;
import com.lansitec.dao.beans.AssetInfo;
import com.lansitec.dao.beans.CompanyInfo;
import com.lansitec.dao.beans.ConstructInfo;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.FieldInfo;
import com.lansitec.dao.beans.GatewayInfo;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.dao.beans.SystemManagers;
import com.lansitec.dao.beans.WorkerInfo;
import com.lansitec.enumlist.Prio;
import com.lansitec.servlets.DeviceListMgr;

public class QueryDiffDataTblInfo {
	  private static Logger logger = LoggerFactory.getLogger(QueryDiffDataTblInfo.class);
	  public static String getTblNMByDiffsn(String columnName,String value){
		  String companyName = null;
		  String fieldNM = null;
		  String result = null;
		  String mapid = null;
		  try {
			    if(columnName.equals("company")){
			    CompanyInfo companyInfo = CompanyInfoDAO.getCompanyInfoBySN(value);
				if(null == companyInfo){
					logger.info("Fail to query companyInfo {} by companysn {}",companyInfo,value);
					return "fail";
				}else{
					companyName = companyInfo.getName();
					result = companyName;
				}
		       
	         }else if(columnName.equals("field")){
		        FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoBySN(value);
		        if(null == fieldInfo){
		        	logger.info("Fail to query fieldInfo {} by fieldsn {}",fieldInfo,value);
					return "fail";
			        }else{
			        	fieldNM = fieldInfo.getName();
			        	result = fieldNM;
			        }
		    }else if(columnName.equals("mapid")){
		    	MapInfo mapInfo = MapInfoDAO.getMapInfoByMapid(value);
		    	if(null == mapInfo){
		    		logger.error("Fail to query mapInfo {} by mapid {}",mapInfo,value);
		    		return "fail";
		    	}else{
		    		mapid = mapInfo.getName();
		    		result = mapid;
		    	}
		    }else if(columnName.equals("company")){
		    	CompanyInfo companyInfo = CompanyInfoDAO.getCompanyInfoBySN(value);
		    	if(null == companyInfo){
		    		logger.error("Fail to query companyInfo {} by mapid {}",companyInfo,value);
		    		return "fail";
		    	}else{
		    		result = companyInfo.getName();;
		    	}
		    }else if(columnName.equals("owner")){
		    	SystemManagers systemManagers = SystemManagersDAO.getMangersInfoByUsername(value);
		    	if(null == systemManagers){
		    		WorkerInfo workerInfo = WorkerInfoDAO.getWorkInfoBySN(value);
			    	if(null == workerInfo){
			    		ProjectManagers projectManagers = ProjectmanagersDAO.getUsersManagersBySN(value);
			    		if(null == projectManagers){
			    			AssetInfo assetInfo = AssetInfoDAO.getAssetInfoBySN(value);
			    			if(null == assetInfo){
			    				logger.error("Fail to get the assetInfo {} projectManagers {} workInfo {}",assetInfo,projectManagers,workerInfo);
			    				return "fail";
			    			}else{
			    				result = assetInfo.getName();
			    			}
			    		}else{
			    			result = projectManagers.getUsername();
			    		}
			    	}else{
			    		result = workerInfo.getName();
			    	}
		    	}else{
		    		result = systemManagers.getUsername();
		    	}
		    	
		    }
		} catch (Exception e) {
		    e.printStackTrace();
	   }
	   return result;  
	  }
      
	  //update information of tables to judge if not exist the table
	  /*public static String editSNTblName(String tableName){
		  String[] tablegroup ={"users_tbl","field_info_tbl","worker_info_tbl","asset_info_tbl","map_info_tbl"}; 
		  String result = null;
		  for(String tblName : tablegroup){
			  if(tblName.equals(tableName)){
				  result = tblName;
			  }
		  }
		  return result;
	  }*/
	  
	  //add information of tables to reponse newsn
	  public static String responseSN(String tableName,String rowName,String rowValue){
		  String number =Integer.toString((int) ((Math.random()*9+1)*1000));
		  String sn = null;
		  if((tableName.equals("users_tbl") || tableName.equals("field_info_tbl")) && (rowName.equals("company"))){
			  sn = rowValue+number;
		  }
		  else if((tableName.equals("worker_info_tbl")) && (rowName.equals("c_sn"))){
			  sn = rowValue+number;
		  }
		  else if((tableName.equals("asset_info_tbl") || (tableName.equals("map_info_tbl"))) && (rowName.equals("field"))){
			  sn = rowValue+number;
		  }
		  return sn;
	  }
	  
	  
	  /*//edit information of tables to judge,if exit,update other jointsn of tables
	  public static String updateJointSNTblName(String tableName){
		  String[] tablegroup ={"company_info_tbl"}; 
		  String result = null;
		  for(String tblName : tablegroup){
			  if(tblName.equals(tableName)){
				  result = tblName;
			  }
		  }
		  return result;
	  }
	  */
	  // query old sn by id
	  public static String selectOldSN(String tableName,String rowValue){
		  String oldInform = null;
		  if(tableName.equals("company_info_tbl")){
			  CompanyInfo company = CompanyInfoDAO.get(Integer.parseInt(rowValue));
			  if(company == null ){
				  logger.info("Fail to query to company {}",company);
				  return null;
			  }else{
				  oldInform = company.getSn();
			  }
		  }else if(tableName.equals("field_info_tbl")){
			  FieldInfo fieldInfo = FieldInfoDAO.get(Integer.parseInt(rowValue));
			  if(null == fieldInfo){
				  logger.info("Fail to query to fieldInfo {}",fieldInfo);
				  return null;
			  }else{
				  oldInform = fieldInfo.getSn();
			  }
		  }else if(tableName.equals("construct_info_tbl")){
			  ConstructInfo constructInfo = ConstructInfoDAO.get(Integer.parseInt(rowValue));
			  if(null == constructInfo){
				  logger.info("Fail to query constructInfo {}",constructInfo);
				  return null;
			  }else{
				  oldInform = constructInfo.getSn();
			  }
		  }else if(tableName.equals("users_tbl")){
			  ProjectManagers projectManagers = ProjectmanagersDAO.get(Integer.parseInt(rowValue));
			  if(null == projectManagers){
				  logger.info("Fail to query projectManagers {}",projectManagers);
				  return null;
			  }else{
				  oldInform = projectManagers.getSn();
			  }
		  }else if(tableName.equals("map_info_tbl")){
			  MapInfo mapInfo = MapInfoDAO.get(Integer.parseInt(rowValue));
			  if(null == mapInfo){
				  logger.info("Fail to query mapInfo {}",mapInfo);
				  return null;
			  }else{
				  oldInform = mapInfo.getSn();
			  }
		  }else if(tableName.equals("worker_info_tbl")){
			  WorkerInfo workerInfo = WorkerInfoDAO.get(Integer.parseInt(rowValue));
			  if(null == workerInfo){
				  logger.info("Fail to query workerInfo {}",workerInfo);
				  return null;
			  }else{
				  oldInform = workerInfo.getSn();
			  }
		  }else if(tableName.equals("asset_info_tbl")){
			  AssetInfo assetInfo = AssetInfoDAO.get(Integer.parseInt(rowValue));
			  if(null == assetInfo){
				  logger.info("Fail to query asset_info_tbl {}",assetInfo);
				  return null;
			  }else{
				  oldInform = assetInfo.getSn();
			  }
		  }
		  return oldInform;
	  }
	  
	  //choose diff sn of tbl by devtype
	  /*public static String getDiffSNByDevtype(String field,String devtype){
		  List<DevInfo> devInfoList = null;
		  String owner = null;
		  boolean sameValue = true;
		 if(devtype.equals("工人")){
  			try {
  				List<ConstructInfo> constructInfoList = ConstructInfoDAO.getConstructInfoByField(field);
  				List<WorkerInfo> workerInfoList = WorkerInfoDAO.getAllWorkInfo();
  				devInfoList = DevInfoDAO.getDevInfoByDevtype(devtype);
  				if((null == constructInfoList) || (constructInfoList.size() == 0)){
  					logger.error("Fail to get the constructInfoList {} bt field!",constructInfoList);
  					return "";
  				}else{
  					if((null == workerInfoList) || (workerInfoList.size() == 0)){
  						logger.info("Fail to get workerInfoList {} in worker_info_tbl!",workerInfoList);
  						return "";
  					}else{
  						Set<WorkerInfo> saveDiffSN = new HashSet<WorkerInfo>();
  						Set<WorkerInfo> saveFianlSN = new HashSet<WorkerInfo>();
  						for(ConstructInfo constructInfo : constructInfoList){
  							for(WorkerInfo workerInfo : workerInfoList){
  								if(constructInfo.getSn().equals(workerInfo.getSn().substring(0, 4))){
  									saveDiffSN.add(workerInfo);
  								}
  							}
  						}
  						if((null == saveDiffSN) || (saveDiffSN.size() == 0)){
  							logger.error("Don't have the worker that belongs to the construct that is searched by the field {}",field);
  							return "";
  						}
  						if((null == devInfoList) || (devInfoList.size() == 0)){
  							//Don't distribute to any device in devtype,choose any workersn to the deveui's owner
  							for(WorkerInfo worker : saveDiffSN){
  								owner = worker.getSn();
  								return owner;
  							}
  							
  						}else{
  							saveFianlSN.addAll(saveDiffSN);
  							for(DevInfo devInfo : devInfoList){
  								String devInfoOwner = devInfo.getOwner();
  								for(WorkerInfo worker : saveDiffSN){
  									String workersn = worker.getSn();
  									if(devInfoOwner.equals(workersn)){
  										sameValue = false;
  										//removing the worker'sn that alreay be distributed to the deveui
  										saveFianlSN.remove(worker);
  										break;
  									}
  								}
  							}
  						    //saveFinalSN  is distributed to workowner
  							if(!sameValue){
  								if((null == saveFianlSN) || (saveFianlSN.size() == 0)){
  									logger.info("All wokersn already has distributed finally! so that Fail to get sn {}",saveFianlSN);
  									return "";
  								}
  								for(WorkerInfo noUseworker : saveFianlSN){
  									owner = noUseworker.getSn();
  									return owner;
  								}
  							}
  							//saveFinalSN  doesn't be distributed to owner 
  							else{
  								for(WorkerInfo worker : saveDiffSN){
  	  								owner = worker.getSn();
  	  								return owner;
  	  							}
  							}
  						}
  					}
  				}
		    } catch (Exception e) {
			  e.printStackTrace();
		   }
  		 }else if(devtype.equals("管理者")){
  			List<ProjectManagers> projectManager = null;
  			 try {
				 projectManager = ProjectmanagersDAO.getUsersManagersByField(field);
				 devInfoList = DevInfoDAO.getDevInfoByDevtype(devtype);
				 Set<ProjectManagers> saveDiffSN = new HashSet<ProjectManagers>();
				 if((null == projectManager) || (projectManager.size() == 0)){
					 logger.info("Fail to get projectManager {} in users_tbl!",projectManager);
					 return "";
				 }else{
					 if((null == devInfoList) || (devInfoList.size() == 0)){
						//Don't distribute to any device in devtype,choose any projectManager'sn to the deveui's owner
						 for(ProjectManagers promanagers : projectManager){
							 owner = promanagers.getSn();
							 return owner;
						 }
					 }else{
						 // save all projectManager'sn that is conform to condition of the field
						 saveDiffSN.addAll(projectManager);
						 for(DevInfo devInfos : devInfoList){
							 String devInfoOwner = devInfos.getOwner();
							 for(ProjectManagers promanage : projectManager){
								 String projmgsn = promanage.getSn();
								 if(devInfoOwner.equals(projmgsn)){
									 sameValue = false;
									//removing the projectManager'sn that alreay be distributed to the deveui
									 saveDiffSN.remove(promanage);
									 break;
								 }
							 }
						 }
						 if(!sameValue){
							 if((null == saveDiffSN) || (saveDiffSN.size() == 0)){
									logger.info("All projectManagersn already has distributed finally! so that Fail to get sn {}",saveDiffSN);
									return "";
							 }
							 for(ProjectManagers promanage : saveDiffSN){
								 owner = promanage.getSn();
								 return owner;
							 }
						 }else{
							//projectManager'sn  doesn't be distributed to owner but the projectManager has the devType
							 for(ProjectManagers promanagers : projectManager){
								 owner = promanagers.getSn();
								 return owner;
							 }
						 }
					 }
				 }
			} catch (Exception e) {
				e.printStackTrace();
			} 
  			
  		 }else if(devtype.equals("资产")){
  			List<AssetInfo> assetInfoList = null;
  			try {
				assetInfoList = AssetInfoDAO.getAllAssetInfo();
				devInfoList = DevInfoDAO.getDevInfoByDevtype(devtype);
				Set<AssetInfo> saveDiffSN = new HashSet<AssetInfo>();
				Set<AssetInfo> saveFianlSN = new HashSet<AssetInfo>();
				if((null == assetInfoList) || (assetInfoList.size() == 0)){
					logger.info("Fail to get assetInfo {} in asset_info_tbl!",assetInfoList);
					 return "";
				}else{
					  for(AssetInfo assetInfo : assetInfoList){
						  if(field.equals(assetInfo.getSn().substring(0, 8))){
							  saveDiffSN.add(assetInfo);
						  }
					  }
					 if((null == saveDiffSN) || (saveDiffSN.size() == 0)){
						 logger.error("Fail to get the assetinfo'sn eight bytes before eq to the field {}",field);
						 return "";
					 }
					 if((null == devInfoList) || (devInfoList.size() == 0)){
						for(AssetInfo assetInfo : saveDiffSN){
							owner = assetInfo.getSn();
							return owner;
						}
					}else{
						saveFianlSN.addAll(saveDiffSN);
						for(DevInfo devInfo : devInfoList){
							String devInfoOwner = devInfo.getOwner();
							for(AssetInfo assetInfo : saveDiffSN){
								String assetInfosn = assetInfo.getSn();
								if(devInfoOwner.equals(assetInfosn)){
									sameValue = false;
									saveFianlSN.remove(assetInfo);
									break;
								}
							}
						}
						if(!sameValue){
							if((null == saveFianlSN) || (saveFianlSN.size() == 0)){
								logger.info("All assetInfosn already has distributed finally! so that Fail to get sn {}",saveDiffSN);
								return "";
							}
							for(AssetInfo assetInfo : saveFianlSN){
								owner = assetInfo.getSn();
								return owner;
							}
						}else{
							for(AssetInfo assetInfo : saveDiffSN){
								owner = assetInfo.getSn();
								return owner;
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  			
  		 }else if((devtype.equals("访客")) || (devtype.equals("未分配"))){
  			 owner = null;
  			return owner;
  		 }
		 return owner;
	  }*/
	  
	  public static boolean judgeCityField(String city,String fieldsn){
	    	List<FieldInfo> fieldInfoList = null;
	    	boolean judgeValue = false;
	    	try {
				fieldInfoList = FieldInfoDAO.getFieldInfoByCity(city);
				if((null == fieldInfoList) || (fieldInfoList.size() == 0)){
					logger.error("Fail to get fieldInfo {} data by city {}",fieldInfoList,city);
				}else{
					for(FieldInfo fieldInfo : fieldInfoList){
						if(fieldInfo.getSn().equals(fieldsn)){
							judgeValue = true;
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return judgeValue;
	    }
	  
	public static String getConstructInfoByNM(String username,String oper){
		List<ConstructInfo> constructList = null;
		String constructNM= null;
		String result = null;
		
		ProjectManagers projectManagers = null;
		try {
			projectManagers = ProjectmanagersDAO.getUsersManagersByUN(username);
			if(null == projectManagers){
				logger.info("Fail to query projectManagers {} by username {}",projectManagers,username);
				return "['fail']";
			}else{
				constructList = ConstructInfoDAO.getAllConstrut();
				if(null == constructList || constructList.size() == 0){
					logger.info("Fail to query constructList {}",constructList);
					return "['fail']";
				}else{
					boolean judgeValue = false;
					for(ConstructInfo construt : constructList){
						if(projectManagers.getField().equals(construt.getField())){
							if((null == oper) || (oper.equals(""))){
								constructNM = construt.getName();
								result+="<option value=\""+constructNM+"\">"+constructNM+"</option>";
								judgeValue = true;
							}else{
								//load ConstructInfo by promanager's name 
								DeviceListMgr.fieldList.add(construt.getSn());
								result = "";
								judgeValue = true;
							}
						}
					}
					if(!judgeValue){
						logger.info("ConstructInfo's field doesn't eq to projectManagers's field {}",projectManagers.getField());
						return "['fail']";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	public static Boolean getWorkerInfoByNM(List<String> construtInfoSN){
        List<WorkerInfo> workerInfos = null;
        List<String> construtsn = new LinkedList<String>();
        construtsn.addAll(construtInfoSN);
        String result = null;
        DeviceListMgr.fieldList.clear();
        boolean judgeValue = false;
		try {
			workerInfos = WorkerInfoDAO.getAllWorkInfo();
			if((null == workerInfos) || (workerInfos.size() == 0)){
				logger.error("Fail to get workerInfos {}",workerInfos);
				return judgeValue;
			}else{
				for(WorkerInfo workerInfo : workerInfos){
					for(String construtInfosn : construtsn){
						if(workerInfo.getSn().substring(0, 4).equals(construtInfosn)){
							DeviceListMgr.fieldList.add(workerInfo.getSn());
							judgeValue = true;
						}
					}
					
				}
				if(!judgeValue){
					logger.error("All workerInfo'sn four bytes before doesn't eq to all construtInfosn!");
					return false;
				}
			}
		}catch (Exception e) {
			 e.printStackTrace();
		}
		return judgeValue;
	}
	
	//get company by the beancons' mapname
	public static String getCompanySNByBeaconsMapidSN(String mapidsn){
		MapInfo mapInfo = null;
		String result = null; 
		try {
			mapInfo = MapInfoDAO.getMapInfoByMapid(mapidsn);
			if(null == mapInfo){
				logger.error("Fail to get the mapInfo {} by mapid {}",mapInfo,mapidsn);
				return result;
			}else{
				String mapsn = mapidsn.substring(0,8);
				FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoBySN(mapsn);
				if(null == fieldInfo){
					logger.error("Fail to get the fieldInfo {} by mapsn {}",fieldInfo,mapsn);
					return result;
				}else{
					String fieldsn = fieldInfo.getSn().substring(0, 4);
					CompanyInfo companyInfo = CompanyInfoDAO.getCompanyInfoBySN(fieldsn);
					if(null == companyInfo){
						logger.error("Fail to get the companyInfo {} by fieldsn {}",companyInfo,fieldsn);
						return result;
					}else{
						result = companyInfo.getSn();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//get deveuis by project Manager'name
	public static String getDevsByPriojectManagerName(String projectManagName){
		String result = "";
		try {
			List<MapNameAndId> mapsList = getMapidsAndMapNameByProjManageNM(projectManagName);
			if((null == mapsList) || (mapsList.size() == 0)){
				logger.error("Fail to get the mapsList {} by the projectManagName {}",mapsList,projectManagName);
				result = "fail";
			}else{
				for(MapNameAndId maps : mapsList){
					List<DevInfo> devInfosList = DevInfoDAO.getDevInfoByMapid(maps.getMapId());
					if((null == devInfosList) || (devInfosList.size() == 0)){
						logger.error("Fail to get the devInfosList {} by the mapsid {}",devInfosList,maps.getMapId());
					}else{
						for(DevInfo devInfo : devInfosList){
							String deveui = devInfo.getDeveui();
							result += deveui + ",";
						}
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	//get maps by the username
	public static List<MapNameAndId> getMapidsAndMapNameByProjManageNM(String projectManagName){
		ProjectManagers projectManagers = null;
		List<MapNameAndId> mapsList = new LinkedList<MapNameAndId>();
		try {
			projectManagers = ProjectmanagersDAO.getUsersManagersByUN(projectManagName);
			if(null == projectManagers){
				logger.error("Fail to get the projectManagers {} by the projectManagerName {}",projectManagers,projectManagName);
				return null;
			}else{
				String field = projectManagers.getField();
				List<MapInfo> mapInfoList = MapInfoDAO.getAllMapInfo();
				if((null == mapInfoList) || (mapInfoList.size() == 0)){
					logger.error("Fail to get the mapInfoList {}",mapInfoList);
					return null;
				}else{
					for(MapInfo mapInfo : mapInfoList){
						String mapEightBytesBefore = mapInfo.getSn().substring(0, 8);
						if(mapEightBytesBefore.equals(field)){
							MapNameAndId map = new MapNameAndId(mapInfo.getName(), mapInfo.getSn());
							mapsList.add(map);
						}
					}
				}
		   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapsList;
	}
	
	//get devs by the 
	public static List<String> getDevsByfieldOfProjManagers(List<String> fieldList){
		List<String> mapsnList = new LinkedList<String>();
    	List<String> devsList = new LinkedList<String>();
		
		try{
			List<MapInfo> mapInfoList = MapInfoDAO.getAllMapInfo();
			if(mapInfoList.isEmpty()){
				logger.error("Fail to get mapInfoList {} by getAllMaoInfo",mapInfoList);
				return null;
			}else{
				for(MapInfo mapInfo : mapInfoList){
					String mapEightBytesBefore = mapInfo.getSn().substring(0, 8);
					for(String fieldsn : fieldList){
						if(mapEightBytesBefore.equals(fieldsn)){
							mapsnList.add(mapInfo.getSn());
						}
					}
				}
				if(mapsnList.isEmpty()){
					logger.error("Fail to get mapsnList {} by getAllMaoInfo",mapsnList);
					return null;
				}else{
					for(String mapsn : mapsnList){
						List<DevInfo> devInfoList = DevInfoDAO.getDevInfoByMapid(mapsn);
						if(!devInfoList.isEmpty()){
							for(DevInfo devInfo : devInfoList){
								devsList.add(devInfo.getDeveui());
							}
						}
					}
					if(devsList.isEmpty()){
						logger.error("Fail to get devsList {}",devsList);
						return null;
					}
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return devsList;
	}
	
	//get fieldList by projectManager's name
	public static List<String> getFieldListByProjMangName(String username){
		List<String> fieldList = new LinkedList<String>();
	
		ProjectManagers projectManagers = null;
		try {
			projectManagers = ProjectmanagersDAO.getUsersManagersByUN(username);
			if(null != projectManagers){
				//Prio.公司级 can search all devid
				if(projectManagers.getPrio().equals(Prio.公司级)){
					String companysn = projectManagers.getCompany();
					List<FieldInfo> fieldlist = FieldInfoDAO.getAllField();
					if((null == fieldlist) || (fieldlist.size() == 0)){
						logger.error("Fail to get fieldlist {} by getAllField,so that Faill to response information!",fieldlist);
						return fieldList;
					}else{
						for(FieldInfo field : fieldlist){
							String fieldsn = field.getSn();
							if(fieldsn.substring(0,4).equals(companysn)){
								fieldList.add(fieldsn);
							}
						}
					}
				}
				else if(projectManagers.getPrio().equals(Prio.城市级)){
					String city = projectManagers.getCity();
					List<FieldInfo> field = FieldInfoDAO.getFieldInfoByCity(city);
					if((null == field) || (field.size() == 0)){
						logger.error("Fail to get field {} by city",field);
						return fieldList;
					}else{
						for(FieldInfo fieldInfo : field){
							// hava so many fieldInfo
							String fieldsn = fieldInfo.getSn();
							if(projectManagers.getField().equals(fieldsn)){
								fieldList.add(fieldsn);
							}
							
						}
					}
				}else if(projectManagers.getPrio().equals(Prio.工地级)){
					String fieldsn = projectManagers.getField();
					fieldList.add(fieldsn);
			    }
		   }else{
			   logger.info("Fail to get the projManager data by the username {}!",username);
				return fieldList;
		   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 return fieldList;
    }
	
	
	public static List<GatewayInfo> getFieldListByDiffListSN(List<GatewayInfo> gatewayInfoList){
		List<GatewayInfo> gatewaysList = new LinkedList<GatewayInfo>();
		try {
			for(GatewayInfo gatewayInfos : gatewayInfoList){
				String fieldsn = gatewayInfos.getField();
				FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoBySN(fieldsn);
				if(null == fieldInfo){
					continue;
				}
				gatewayInfos.setField(fieldInfo.getName());
				gatewaysList.add(gatewayInfos);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gatewaysList;
	}
}
