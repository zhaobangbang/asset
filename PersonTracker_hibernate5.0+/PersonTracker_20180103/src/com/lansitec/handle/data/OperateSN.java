package com.lansitec.handle.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperateSN {
	private static Logger logger = LoggerFactory.getLogger(OperateSN.class);
	public static String newestData =null;
	public static String changeData =null;
	
	//select old(sn,city) and get new(sn,city) After updating table's(sn,city)
	public static String getOldInfo(String tbName,String idValue,String rowName,String rowValue){
		String oldInfo = null;
		
		oldInfo = QueryDiffDataTblInfo.selectOldSN(tbName,idValue);
		
		if((rowName.equals("sn")) || (((tbName.equals("field_info_tbl")) || (tbName.equals("users_tbl")))&&(rowName.equals("company"))) || 
			     (((tbName.equals("map_info_tbl")) || tbName.equals("asset_info_tbl"))&&(rowName.equals("field"))) || 
			     ((tbName.equals("worker_info_tbl"))&&(rowName.equals("c_sn")))){
			newestData = rowValue;
			
		}
		//can't open!
		if(oldInfo == null){
			logger.info("Fail to tblName {} oldsn {}  in id {}",tbName,oldInfo,newestData);
			return null;
			
		}else{
			// update company_info_tbl'sn:newestSN == oldSN,   
			/*if((newestSN.equals(oldSN))||(newestSN.equals(oldSN.substring(0, 4)))){
				//Needn't response newSN to update to mapping tbl
				if(tbName.equals("company_info_tbl")){
					logger.info("oldSN {} equal to newSN {},Don't update any data of table",oldSN,newestSN);
					return null;
				}else{
					// sn doesn't change,but Need response newSN to update to mapping tbl
					logger.info("Need to response sn {}",newestSN);
					// users_tbl、field_info_tbl 
					newestSN = oldSN;
					return newestSN;
				}
				
			}*/
			//update other tbl'sn:newestSN == oldSN.substring(0, 4)
			//(newestData.equals(oldInfo)) || 
			if((newestData.equals(oldInfo.substring(0, 4))) || (newestData.equals(oldInfo.substring(0, 8)))){
					// sn doesn't change,but Need response newSN to update to mapping tbl
					logger.info("Need to response sn {}",newestData);
					// users_tbl、field_info_tbl 
					newestData = oldInfo;
					return newestData;
			}else if((tbName.equals("field_info_tbl")) || (tbName.equals("users_tbl")) || (tbName.equals("worker_info_tbl"))){
			
				String fourBytesAftField = oldInfo.substring(4, oldInfo.length());
				changeData = newestData+fourBytesAftField;
			}else if((tbName.equals("map_info_tbl")) || (tbName.equals("asset_info_tbl"))){
				String fourBytesAftField = oldInfo.substring(8, oldInfo.length());
				changeData = newestData+fourBytesAftField;
			}
			
		}
		
		return oldInfo;
	}
	
	//add and delete deveui in dev_info_tbl then add devnumber in gateway_info_tbl and field_info_tbl(无效)
	/*public static void addAndDelAndUpdateDevNuberByFieldSN(String fieldsn,String fieldsnNow,String oper){
		List<GatewayInfo> gatewayInfoList = null;
		
		try {
			gatewayInfoList = GatewayInfoDAO.getGatewayInfoByFieldSN(fieldsn);
			if((null == gatewayInfoList) || (gatewayInfoList.size() == 0)){
				logger.error("Fail to get the gatewayInfoList {} by the fielsn {}",gatewayInfoList,fieldsn);
			}else{
				for(GatewayInfo gatewayInfo : gatewayInfoList){
					int devNumber = gatewayInfo.getMotenumber();
					if(oper.equals("add")){
						devNumber++;
						gatewayInfo.setMotenumber((short)devNumber);
						GatewayInfoDAO.update(gatewayInfo);
						logger.info("Gateway'devNumber add to devNumber {} in gateway_info_tbl",devNumber);
					}else if(oper.equals("del")){
						devNumber--;
						gatewayInfo.setMotenumber((short)devNumber);
					    GatewayInfoDAO.update(gatewayInfo);
						logger.info("Gateway'devNumber delete to devNumber {} in gateway_info_tbl",devNumber);
					}else if(oper.equals("edit")){
						//field'sn gateway devNumber -- bofore
						devNumber--;
						gatewayInfo.setMotenumber((short)devNumber);
					    GatewayInfoDAO.update(gatewayInfo);
					    logger.info("Gateway'devNumber delete to devNumber {} in gateway_info_tbl",devNumber);
					    //field'sn gateway devNuvber ++ now
					    gatewayInfoList = GatewayInfoDAO.getGatewayInfoByFieldSN(fieldsnNow);
						if((null == gatewayInfoList) || (gatewayInfoList.size() == 0)){
							logger.error("Fail to get the gatewayInfoList {} by the fieldsnNow {}",gatewayInfoList,fieldsnNow);
							continue;
						}else{
							for(GatewayInfo gatewayInfos : gatewayInfoList){
								int devsNumber = gatewayInfos.getMotenumber();
								devsNumber++;
								gatewayInfos.setMotenumber((short)devsNumber);
								GatewayInfoDAO.update(gatewayInfos);
								logger.info("Gateway'devNumber add to devsNumber {} in gateway_info_tbl",devsNumber);
							}
							
						}
					}
					
				}
			}
			FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoBySN(fieldsn);
			if(null == fieldInfo){
				logger.error("Fail to get the fieldInfo {} by the fielsn {}",fieldInfo,fieldsn);
				return;
			}else{
				int devNumber = fieldInfo.getMotenumber();
				if(oper.equals("add")){
					devNumber++;
					fieldInfo.setMotenumber((short)devNumber);
					FieldInfoDAO.update(fieldInfo);
					logger.info("Field'devNumber add to devNumber {} in field_info_tbl",devNumber);
					return;
				}else if(oper.equals("del")){
					devNumber--;
					fieldInfo.setMotenumber((short)devNumber);
					FieldInfoDAO.update(fieldInfo);
					logger.info("Field'devNumber delete to devNumber {} in field_info_tbl",devNumber);
					return;
				}else if(oper.equals("edit")){
					if(fieldsn.equals(fieldsnNow)){
						logger.info("the fieldsnTbl {} eq to the fieldsnNow {} , so that update the devNum in fieldsn {}",fieldsn,fieldsnNow,fieldsn);
						return;
					}else{
						//field'sn devNumber -- bofore
						devNumber--;
						fieldInfo.setMotenumber((short)devNumber);
						FieldInfoDAO.update(fieldInfo);
						logger.info("Field'devNumber delete to devNumber {} in field_info_tbl",devNumber);
						//field'sn devNuvber ++ now
						FieldInfo fieldInfos = FieldInfoDAO.getFieldInfoBySN(fieldsnNow);
						int devsNumber = fieldInfos.getMotenumber();
						devsNumber++;
						fieldInfos.setMotenumber((short)devsNumber);
						FieldInfoDAO.update(fieldInfos);
						logger.info("Field'devNumber add to devsNumber {} in field_info_tbl",devsNumber);
						return;
					}
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	
	//add and delete gateway in gateway_info_tbl then add gatewaynumber in field_info_tbl(无效)
	/*public static void addAndDelAndUpdateGatewayNumByField(String fieldsn,String fieldsnNow,String oper){
		try {
			FieldInfo fieldInfo = FieldInfoDAO.getFieldInfoBySN(fieldsn);
			if(null == fieldInfo){
				logger.error("Fail to get the fieldInfo {} by the field {}",fieldInfo,fieldsn);
				return;
			}else{
				int gatewayNumber = fieldInfo.getGwnumber();
				if(oper.equals("add")){
					gatewayNumber++;
					fieldInfo.setGwnumber((byte)gatewayNumber);
					FieldInfoDAO.update(fieldInfo);
					logger.info("Field'gatewayNumber add to gatewayNumber {} in field_info_tbl",gatewayNumber);
					return;
				}else if(oper.equals("del")){
					gatewayNumber--;
					fieldInfo.setGwnumber((byte)gatewayNumber);
					FieldInfoDAO.update(fieldInfo);
					logger.info("Field'gatewayNumber delete to gatewayNumber {} in field_info_tbl",gatewayNumber);
					return;
				}else if(oper.equals("edit")){
					//field'sn gatewayNumber -- bofore
					gatewayNumber--;
					fieldInfo.setGwnumber((byte)gatewayNumber);
					FieldInfoDAO.update(fieldInfo);
					logger.info("Field'gatewayNumber delete to gatewayNumber {} in field_info_tbl",gatewayNumber);
					//field'sn gatewayNumber ++ now
					FieldInfo fieldInfos = FieldInfoDAO.getFieldInfoBySN(fieldsnNow);
					if(null == fieldInfos){
						logger.error("Fail to get the fieldInfos {} by the fieldsnNow {}",fieldInfos,fieldsnNow);
						return;
					}else{
						int gatewaysNumber = fieldInfos.getGwnumber();
						gatewaysNumber++;
						fieldInfos.setGwnumber((byte)gatewaysNumber);
						FieldInfoDAO.update(fieldInfos);
						logger.info("Field'gatewayNumber add to gatewaysNumber {} in field_info_tbl",gatewaysNumber);
						return;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	
	
	//changing sn byte of association table
	public static void updatediffTblJointSN(String tbName,String beforeInfo){
		HanlerTblAssoSN hanlerTblAssoSN = new HanlerTblAssoSN();
		//company'sn can update then uses it
		/*if(tbName.equals("company_info_tbl")){
			hanlerTblAssoSN.handleProjMgAndFieldTblUpSN(beforeSN);
			return;
		}else */
		if(tbName.equals("field_info_tbl")){
		   hanlerTblAssoSN.handleMapTblUpdateSN(beforeInfo);
		   hanlerTblAssoSN.handleAssetInfoUpdateSN(beforeInfo);
		   return;
		}//construct'sn can update then uses it
		 /*else if(tbName.equals("construct_info_tbl")){
			hanlerTblAssoSN.handleWorkerTblUpdateSN(beforeSN);
			return;
		}*/
		else if((tbName.equals("users_tbl")) || (tbName.equals("worker_info_tbl")) || (tbName.equals("asset_info_tbl"))){
			hanlerTblAssoSN.handleDevInfoTblUpOwner(tbName,beforeInfo);
			return;
		}

	}
	
	//deleting sn byte of association table
	public static void deletediffTblAssociateSN(String tbName,String idInfo){
		HanlerTblAssoSN hanlerTblAssoSN = new HanlerTblAssoSN();
		//company'sn can update then uses it
		/*if(tbName.equals("company_info_tbl")){
			hanlerTblAssoSN.handleProjMgAndFieldTblUpSN(beforeSN);
			return;
		}else */
		if(tbName.equals("field_info_tbl")){
		   hanlerTblAssoSN.handleMapTblDeleteSN(idInfo);
		   hanlerTblAssoSN.handleAssetInfoDeleteSN(idInfo);
		   return;
		}//construct'sn can update then uses it
		 /*else if(tbName.equals("construct_info_tbl")){
			hanlerTblAssoSN.handleWorkerTblUpdateSN(beforeSN);
			return;
		}*/
		else if((tbName.equals("users_tbl")) || (tbName.equals("worker_info_tbl")) || (tbName.equals("asset_info_tbl"))){
			hanlerTblAssoSN.handleDevInfoTblDeleteOwner(tbName, idInfo);
			return;
		}
	}
}
