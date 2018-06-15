package com.lans.beans;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.asset.thirdparty.zwwl.ZwwlSender;
import com.lans.dao.ZwwldevicesDAO;
import com.lans.dao.beans.Zwwldevices;

public class UploadDevFinalLocation {
   public static UploadDevFinalLocation insitance = null;
   private Logger logger = LoggerFactory.getLogger(UploadDevFinalLocation.class);
   private List<Zwwldevices> zwwlDevLsit = null;
   private ArrayList<String> onlinedev = null;
   private String status = null;
   
   public UploadDevFinalLocation(){
	   onlinedev = new ArrayList<String>();
   }
   
   public static UploadDevFinalLocation getInsitance(){
	   if(null == insitance){
		   synchronized (UploadDevFinalLocation.class) {
			if(null == insitance){
				insitance = new UploadDevFinalLocation();
			}
		}
	   }
	   return insitance;
   }
   
   public synchronized void pushDevFinalLocaltion(){
	   try {
		   zwwlDevLsit = ZwwldevicesDAO.getAllZwwldevices();
		   for(Zwwldevices zwwldev : zwwlDevLsit){
			   if(zwwldev.getDevid().length() != 12){
				   status = zwwldev.getStatus();
				   if(status.equals("1")){
					   logger.info("the device's {} status {} is online",zwwldev.getDevid(),status);
					   onlinedev.add(zwwldev.getDevid());  
				   }else{
					   logger.info("the device's {} status {} is offline",zwwldev.getDevid(),status);
					   continue;	
				   } 
			   }
		   }
		  ZwwlSender.pushDevFinalLocalData(onlinedev);
	   } catch (Exception e) {
		   logger.error("try/catch error in pushDevFinalLocaltion ");
		   e.printStackTrace();
	   }	   
   }
   
   
   
   
}
