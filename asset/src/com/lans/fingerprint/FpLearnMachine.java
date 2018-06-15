package com.lans.fingerprint;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.tvmessages.EndDevIndoorPos;
import com.lans.controller.networkgw.tvmessages.ScannedBeacon;
import com.lans.fingerprint.learnmodel.FpBeaconModel;
import com.lans.fingerprint.learnmodel.FpKnownPos;
import com.lans.fingerprint.learnmodel.FpPosMatchRslt;
import com.lans.fingerprint.learnmodel.FpPosSetModel;
import com.lans.fingerprint.learnmodel.FpPositionModel;

public class FpLearnMachine {
	private Logger logger = LoggerFactory.getLogger(FpLearnMachine.class);
	Map<String, FpKnownPos> learnMap;
	FpModels fpModel;
	
	public FpLearnMachine(String baseDir) {
		learnMap = new ConcurrentHashMap<String, FpKnownPos>();
		fpModel = new FpModels(baseDir);
		
		fpModel.loadFromFile();
	}
	
	public void startlearning(String deveui, double x, double y) {
		FpKnownPos pos = new FpKnownPos(x, y);
		
		learnMap.put(deveui, pos);
	}
	
	public boolean learnOnce(String deveui, EndDevIndoorPos msg) {
		FpKnownPos pos = learnMap.get(deveui);
		
		//in case pos is null it means the deveui is not set as learner
		if (pos == null) {
			return false;
		}
		
		pos.incLearnCnt();
		
		List<ScannedBeacon> bList = msg.beaconList;
		for (ScannedBeacon beacon : bList) {
			pos.addScannedBeacon(beacon.getMinor(), beacon.getRssi());
		}

		if (pos.getLearnCnt() > FpDefs.LEARN_MIN_CNT) {
			return true;
		}
		
		return false;
	}
	
	public double calPosMatchValue(FpPositionModel pos, EndDevIndoorPos msg) {
		List<FpBeaconModel> knownBList = pos.getbModelList();
		List<ScannedBeacon> scannBList = msg.beaconList;
		double possibility = 0;
		int knowSize = knownBList.size();
		
		for (FpBeaconModel knownB : knownBList) {
			int minor = knownB.getMinor();
			
			for (ScannedBeacon scannB : scannBList) {
				if (scannB.getMinor() == minor) {
					//check power value
					int power = scannB.getRssi();
					double pwrPoss;
					if (power <= FpDefs.POWER_THRE_1) {
						pwrPoss = knownB.getPwr1Percent();
					} else if (power <= FpDefs.POWER_THRE_2) {
						pwrPoss = knownB.getPwr2Percent();
					} else if (power <= FpDefs.POWER_THRE_3) {
						pwrPoss = knownB.getPwr3Percent();
					} else {
						pwrPoss = knownB.getPwr4Percent();
					}
					
					possibility += knownB.getScanPercent() * pwrPoss;
					break;
				}
			}
		}
		
		return possibility/knowSize;
	}
	
	public FpPosMatchRslt findBestMatch(EndDevIndoorPos msg) {
		FpPosSetModel posSet = fpModel.getModelList();
		List<FpPositionModel> posList = posSet.getModelList();
		FpPosMatchRslt rslt = new FpPosMatchRslt();
		
		for (FpPositionModel position : posList) {
			
			double match = calPosMatchValue(position, msg);
			if (match > rslt.getPossibility()) {
				rslt.setPossibility(match);
				rslt.setX(position.getX());
				rslt.setY(position.getY());
				
				logger.info("x-{} y-{} match better {}.", position.getX(), position.getY(), match);
			}
		}
		
		if (rslt.getPossibility() == 0) {
			return null;
		}
		
		return rslt;
	}
	
	public boolean saveLearning() {
		Iterator<Entry<String, FpKnownPos>> itor = learnMap.entrySet().iterator();
		Entry<String, FpKnownPos> entry = null;

		FpKnownPos val = null;
		while (itor.hasNext()) {
			entry = itor.next();
			val = entry.getValue();
			
			fpModel.addKnownPos(val);
		}
		
		fpModel.saveToFile();
		return true;
	}
}
