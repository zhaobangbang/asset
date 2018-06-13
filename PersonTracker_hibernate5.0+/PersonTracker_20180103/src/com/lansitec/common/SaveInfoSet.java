package com.lansitec.common;

import java.util.HashSet;
import java.util.Set;

import com.lansitec.dao.beans.AcquiredemandInfo;
import com.sun.media.jfxmedia.logging.Logger;

public class SaveInfoSet {
    public static Set<AcquiredemandInfo> acqmandSet = new HashSet<AcquiredemandInfo>();


	public static void judge(AcquiredemandInfo acqmand){
		Set<AcquiredemandInfo> acqmandInfo = new HashSet<AcquiredemandInfo>();
		System.out.println("acqmand.getDevid() "+acqmand.getDevid());
		if(acqmandSet.isEmpty()){
			acqmandSet.add(acqmand);
			return;
		}
		else if((acqmand.getDevid() == null) || (acqmand.getDevid().equals("test"))){
			acqmandSet.remove(acqmand);
			return;
		}
		else{
			for(AcquiredemandInfo acqMand : acqmandSet){
				if(acqMand.getDevid().equalsIgnoreCase(acqmand.getDevid()) || acqMand.getDevid() == null ||
						                                                      acqMand.getDevid().equals("test"))
				{
					acqmandInfo.add(acqMand);
				}
				
			}
			for(AcquiredemandInfo acq : acqmandInfo){
				acqmandSet.remove(acq);
			}
			acqmandSet.add(acqmand);
		}
		
	}
    
}
