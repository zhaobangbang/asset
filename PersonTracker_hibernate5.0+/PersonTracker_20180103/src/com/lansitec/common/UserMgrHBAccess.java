package com.lansitec.common;

import com.lansitec.dao.ProjectmanagersDAO;
import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.beans.ProjectManagers;
import com.lansitec.dao.beans.SystemManagers;

public class UserMgrHBAccess {
	public static String userSystemExist(String username){
		if(null == username)
	   	   	return "";
		
		String bExists = "";
		try {
			SystemManagers managers = SystemManagersDAO.getMangersInfoByUsername(username);
			if(managers != null){
				bExists="系统管理员";
			}else{
				ProjectManagers proManagers = ProjectmanagersDAO.getUsersManagersByUN(username);
				if(proManagers != null){
					bExists="项目管理员";
				}else{
					bExists = "";
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bExists;
	}
	
	public static boolean userSystemValid(String username,String usermail){
		if(null == username){
			return false;
		}
		String smail = null;
		boolean bExists = false;
		SystemManagers sysManagers = null;
		ProjectManagers proManagers = null;
		try {
			sysManagers = SystemManagersDAO.getMangersInfoByUsername(username);
			if(sysManagers != null){
				smail = sysManagers.getUsermail();
				if(usermail.equals(smail)){
					bExists = true;
				}
			}else{
				proManagers = ProjectmanagersDAO.getUsersManagersByUN(username);
				if(proManagers != null){
					smail = proManagers.getUsermail();
					if(usermail.equals(smail)){
						bExists = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bExists;
	}
}
