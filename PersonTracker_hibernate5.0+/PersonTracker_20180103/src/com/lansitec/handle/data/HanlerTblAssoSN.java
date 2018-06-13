package com.lansitec.handle.data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansitec.dao.AssetInfoDAO;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.FieldInfoDAO;
import com.lansitec.dao.MapInfoDAO;
import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.WorkerInfoDAO;
import com.lansitec.dao.beans.AssetInfo;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.FieldInfo;
import com.lansitec.dao.beans.MapInfo;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.dao.beans.WorkerInfo;

public class HanlerTblAssoSN {
   private  Logger logger = LoggerFactory.getLogger(HanlerTblAssoSN.class);
   
   /*public void handleProjMgAndFieldTblUpSN(String beforeSN){
	   List<ProjectManagers> projManagerList =null;
		List<FieldInfo> fieldList = null;
		String number = null;
		String newsn = null;
		try {
			projManagerList = ProjectmanagersDAO.getUsersManagersByCompany(beforeSN);
			fieldList = FieldInfoDAO.getAllField();
			if((null == projManagerList) || (projManagerList.size() == 0)){
				logger.info("Fail to query PorjrctManagers {},Neendn't to update any date of users_tbl",projManagerList);
			}else{
				for(ProjectManagers projMg : projManagerList){
					String projmangSN = projMg.getSn();
					number = projmangSN.substring(4,projmangSN.length());
					newsn = OperateSN.newestSN+number;
					projMg.setSn(newsn);
				
					ProjectmanagersDAO.update(projMg);
					logger.info("Users_tbl update newsn {}",newsn);
				}
				
			}
			if((null == fieldList) || (fieldList.size() == 0)){
				logger.info("Fail to query FieldInfo {},Neendn't to update any date of field_info_tbl",fieldList);
			}
			else{
				for(FieldInfo field : fieldList){
					String fieldSN =field.getSn();
					String fourbytesbefore = fieldSN.substring(0, 4);
					if(fourbytesbefore .equals(beforeSN) ){
						number = fieldSN.substring(4, fieldSN.length());
						newsn = OperateSN.newestSN+number;
						field.setSn(newsn);
						FieldInfoDAO.update(field);
						logger.info("field_tbl update newsn {}",newsn);
						//fieldsn equal to eight bytes befort of mapsn
						handleMapTblUpdateSN(fieldSN);
						continue;
					}
					
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }*/
   
    //get FieldSN to update mapsn
	public synchronized void handleMapTblUpdateSN(String beforeSN){
		String number = null;
		String newsn = null;
		List<MapInfo> mapInfoList = null;
		try {
			mapInfoList = MapInfoDAO.getAllMapInfo();
			if((null == mapInfoList) || (mapInfoList.size() == 0)){
				logger.info("Fail to query mapInfoList {},Neendn't to update any date of map_info_tbl",mapInfoList);
				return ;
			}else{
				for(MapInfo map : mapInfoList){
					String mapsn = map.getSn();
					String mapFieldsn = mapsn.substring(0, 8);
					if(mapFieldsn.equals(beforeSN)){
						//String mapCenterSn = beforeSN.substring(4, beforeSN.length());
						number = mapsn.substring(8, mapsn.length());
						//company'sn can update then uses it
						/*if(null == OperateSN.changeSN){
							newsn = OperateSN.newestSN+mapCenterSn+number;
						}else{
							newsn = OperateSN.changeSN+number;
						}*/
						newsn = OperateSN.changeData+number;
						map.setSn(newsn);
						MapInfoDAO.update(map);
						logger.info("map_info_tbl updates the sn {}",newsn);
						continue;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//del field'sn to delete the all data of mapsn
	public synchronized void handleMapTblDeleteSN(String id){
		FieldInfo fieldInfo = FieldInfoDAO.get(Integer.parseInt(id));
		String beforeSN = fieldInfo.getSn();
		List<MapInfo> mapInfoList = null;
		try {
			mapInfoList = MapInfoDAO.getAllMapInfo();
			if((null == mapInfoList) || (mapInfoList.size() == 0)){
				logger.info("Fail to query mapInfoList {},Neendn't to update any date of map_info_tbl",mapInfoList);
				return ;
			}else{
				for(MapInfo map : mapInfoList){
					String mapFieldsn = map.getSn().substring(0, 8);
					if(mapFieldsn.equals(beforeSN)){
						MapInfoDAO.delete(map);
						logger.info("map_info_tbl deletes the mapsn {}",mapFieldsn);
						continue;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get FieldSN to update assetsn
	public synchronized void handleAssetInfoUpdateSN(String beforesn){
		String newsn = null;
		String number = null;
		List<AssetInfo> assetInfoList = null;
		try {
			assetInfoList = AssetInfoDAO.getAllAssetInfo();
			if((null == assetInfoList) || (assetInfoList.size() == 0)){
				logger.info("Fail to query assetInfoList {},Neendn't to update any date of asset_info_tbl!",assetInfoList);
				return;
			}else{
				for(AssetInfo assetInfo : assetInfoList){
					String oldsn = assetInfo.getSn();
					//assetsn's eight bytes before is fieldsn
					if(beforesn.equals(oldsn.substring(0, 8))){
						number = oldsn.substring(8, oldsn.length());
						newsn = OperateSN.changeData + number; 
						assetInfo.setSn(newsn);
						AssetInfoDAO.update(assetInfo);
						logger.info("asset_info_tbl updates the sn {}",newsn);
						continue;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//delete field'sn to delete the all data about the assetsn
	public synchronized void handleAssetInfoDeleteSN(String id){
		FieldInfo fieldInfo = FieldInfoDAO.get(Integer.parseInt(id));
		String beforesn = fieldInfo.getSn();
		List<AssetInfo> assetInfoList = null;
		try {
			assetInfoList = AssetInfoDAO.getAllAssetInfo();
			if((null == assetInfoList) || (assetInfoList.size() == 0)){
				logger.info("Fail to query assetInfoList {},Neendn't to update any date of asset_info_tbl!",assetInfoList);
				return;
			}else{
				for(AssetInfo assetInfo : assetInfoList){
					String oldsn = assetInfo.getSn();
					//assetsn's eight bytes before is fieldsn
					if(beforesn.equals(oldsn.substring(0, 8))){
						AssetInfoDAO.delete(assetInfo);
						logger.info("asset_info_tbl deletes the assetsn {}",oldsn);
						return;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get constructSN to update workSN
	/*public void handleWorkerTblUpdateSN(String beforeSN){
		String number = null;
		String newWokerSN = null;
		List<WorkerInfo> workerList = null;
		try {
			workerList = WorkerInfoDAO.getAllWorkInfo();
			if((null == workerList) || (workerList.size() == 0)){
				logger.info("Fail to query workerList {},Neendn't to update any date of worker_info_tbl",workerList);
				return ;
			}else{
				for(WorkerInfo work : workerList){
					String worksn = work.getSn();
					// worksn's four bytes before is constructsn
					String fourBytesBefore = worksn.substring(0, 4);
					if(fourBytesBefore.equals(beforeSN)){
						number = worksn.substring(4, worksn.length());
						newWokerSN = OperateSN.newestSN+number;
						work.setSn(newWokerSN);
						
						WorkerInfoDAO.update(work);
						logger.info("update worker_info_tbl's  sn {}",newWokerSN);
						
						OperateSN.changeSN = newWokerSN;
						handleDevInfoTblUpOwner("worker_info_tbl",worksn);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	//get usersSN to update DevInfoOwner
	public synchronized void handleDevInfoTblUpOwner(String tblName ,String beforeUsersSN){
		String newDevOwner = null;
		List<DevInfo> devinfoList = null;
		try {
			devinfoList = DevInfoDAO.getAllDevInfo();
			if((null == devinfoList) || (devinfoList.size() == 0)){
				logger.info("Fail to query devinfoList {},Needn't to update the dev_info_tbl",devinfoList);
			}else{
				for(DevInfo devInfo : devinfoList){
					String owner = devInfo.getOwner();
					if((owner.equals(beforeUsersSN)) && ((tblName.equals("users_tbl")) || (tblName.equals("worker_info_tbl")) || (tblName.equals("asset_info_tbl")))){
						newDevOwner = OperateSN.changeData;
						devInfo.setOwner(newDevOwner);
						DevInfoDAO.update(devInfo);
						logger.info("update dev_info_tbl's owner {}",newDevOwner);
						continue;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get usersSN to delete DevInfoOwner
	public synchronized void handleDevInfoTblDeleteOwner(String tblName,String id){
		String beforeOwner = null;
		if(tblName.equals("users_tbl")){
			ProjectManagers projectManagers = ProjectmanagersDAO.get(Integer.parseInt(id));
			if(null == projectManagers){
				logger.error("Fail to get the projectManagers {} by the id {} ! so that Needn't update any data !",projectManagers,id);
				return;
			}else{
				beforeOwner = projectManagers.getSn();
			}
		}else if(tblName.equals("worker_info_tbl")){
			WorkerInfo workerInfo = WorkerInfoDAO.get(Integer.parseInt(id));
			if(null == workerInfo){
				logger.error("Fail to get the workerInfo {} by the id {} !  so that Needn't update any data !",workerInfo,id);
				return;
			}else{
				beforeOwner = workerInfo.getSn();
			}
		}else if(tblName.equals("asset_info_tbl")){
			AssetInfo assetInfo = AssetInfoDAO.get(Integer.parseInt(id));
			if(null == assetInfo){
				logger.error("Fail to get the assetInfo {} by the id {} !  so that Needn't update any data !",assetInfo,id);
				return;
			}else{
				beforeOwner = assetInfo.getSn();
			}
		}
		
		List<DevInfo> devinfoList = null;
		try {
			devinfoList = DevInfoDAO.getAllDevInfo();
			if((null == devinfoList) || (devinfoList.size() == 0)){
				logger.info("Fail to query devinfoList {},Needn't to update the dev_info_tbl",devinfoList);
			}else{
				for(DevInfo devInfo : devinfoList){
					String owner = devInfo.getOwner();
					if(owner.equals(beforeOwner)){
						DevInfoDAO.delete(devInfo);
						logger.info("delete dev_info_tbl's owner {}",owner);
						return;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
