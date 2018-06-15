package com.lans.fingerprint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lans.fingerprint.learnmodel.FpBeaconModel;
import com.lans.fingerprint.learnmodel.FpKnownPos;
import com.lans.fingerprint.learnmodel.FpPosSetModel;
import com.lans.fingerprint.learnmodel.FpPositionModel;
import com.lans.fingerprint.learnmodel.FpScannedBeacon;

public class FpModels {
	private Logger logger = LoggerFactory.getLogger(FpModels.class);
	FpPosSetModel model;
	String fileBase;
	
	public FpModels(String base) {
		model = new FpPosSetModel();
		this.fileBase = base;
	}
	
	public void saveToFile() {
		String modelStr = JSON.toJSONString(model);
		
		File file;
		BufferedWriter out;
		try {
			file = new File(fileBase + "fpmodel.txt");
	        if(!file.exists()) {
	            file.createNewFile();
	        }
	        
	        out = new BufferedWriter(new FileWriter(file));
	        out.write(modelStr);
	        out.close();
	        
	        logger.info("save to file {}", modelStr);
		} catch (Exception e) {
			logger.error("save to file failed "+e.getMessage());
		}
	}
	
	public void loadFromFile() {
		String fileName = fileBase + "fpmodel.txt";
    	String result = "";
		
    	File file = new File(fileName);
    	if(!file.exists()||file.isDirectory()) {
    		logger.info("file {} not exist or directory", fileName);
    		return;
    	}
    	
        BufferedReader reader = null;  
        try {
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;
            
            while ((tempString = reader.readLine()) != null) {
            	result += tempString;
            }  
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {  
                try {
                    reader.close();  
                } catch (IOException e1) {
                	e1.printStackTrace(); 
                }
            }
        }
    	
        model = JSON.parseObject(result, FpPosSetModel.class);
	}
	
	public FpPosSetModel getModelList() {
		return model;
	}
	
	public void addKnownPos(FpKnownPos pos) {
		
		FpPositionModel posModel = new FpPositionModel(pos.getX(), pos.getY());
		int totalCnt = pos.getLearnCnt();
		
		List<FpScannedBeacon> scanList = pos.getScanList();
		for (FpScannedBeacon beacon : scanList) {
			FpBeaconModel bModel = new FpBeaconModel(beacon.getMinor());
			
			bModel.setScanPercent(((double)beacon.getCount())/((double)totalCnt));
			int pwrTotalCnt = beacon.getPwrRange1Cnt()+beacon.getPwrRange2Cnt()
								+ beacon.getPwrRange3Cnt()+beacon.getPwrRange4Cnt();
			
			bModel.setPwr1Percent(((double)beacon.getPwrRange1Cnt())/((double)pwrTotalCnt));
			bModel.setPwr2Percent(((double)beacon.getPwrRange2Cnt())/((double)pwrTotalCnt));
			bModel.setPwr3Percent(((double)beacon.getPwrRange3Cnt())/((double)pwrTotalCnt));
			bModel.setPwr4Percent(((double)beacon.getPwrRange4Cnt())/((double)pwrTotalCnt));
			
			posModel.addBeaconModel(bModel);
		}
		
		model.addPositionModel(posModel);
	}
}
