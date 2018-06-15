package com.lans.controller.networkgw.msghandler;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionFilter {
	Logger logger = LoggerFactory.getLogger(PositionFilter.class);
	Map<String, NodePosition> lastPosMap = null;
	public static PositionFilter instance = null;
	
	public static int POS_TYPE_INDOOR = 0;
	public static int POS_TYPE_OUTDOOR = 1;
	
	public PositionFilter() {
		lastPosMap = new ConcurrentHashMap<String, NodePosition>();
	}
	
	public static PositionFilter getInstance() {
		if (instance == null) {
			instance = new PositionFilter();
		}
		
		return instance;
	}
	
	public double calCoeffi(byte move, long timeDiff) {
		double coeffi = 1.0;
		
		logger.info("calCoeffi move {} timediff {}", (int)move, timeDiff);
		
		//if timediff > 30s, then believe the new position
		if (timeDiff > 30 * 1000) {
			return coeffi;
		}
		
		//otherwise, if move < 2 consider the dev is not moving fast
		//believe last position mostly
/*		if (move <= 1) {
			coeffi = 0.6;
		} else if (move <= 2) {
			coeffi = 0.7;
		} else if (move <= 3) {
			coeffi = 0.8;
		} else {
			coeffi = 0.9;
		}*/
		
		return coeffi;
	}
	
	public void filterPosition(String eui, double devX, double devY, Date time, int posType, byte move) {
		NodePosition np = lastPosMap.get(eui);
		
		//the first position does not need to be filtered
		if (np == null) {
			lastPosMap.put(eui, new NodePosition(devX, devY, time));
			return;
		} 
		
		//otherwise, filter the position based on move and time
		Date lastTime = np.getTime();
		double lastX = np.getX();
		double lastY = np.getY();
		
		//coeffi need to be defined based on move and time
		double coeffi = calCoeffi(move, time.getTime()-lastTime.getTime());
		
		double storeX = (1 - coeffi) * lastX + coeffi * devX;
		double storeY = (1 - coeffi) * lastY + coeffi * devY;

		logger.info("filterPosition eui {} coeffi {} old X-{}, Y-{} new X-{}, Y-{}", eui, coeffi, devX, devY, storeX, storeY);
		lastPosMap.put(eui, new NodePosition(storeX, storeY, time));
	}
	
	public double getLatestX(String eui) {
		NodePosition np = lastPosMap.get(eui);
		
		if (np == null) {
			return 0.0;
		} else {
			return np.getX();
		}
	}
	
	public double getLatestY(String eui) {
		NodePosition np = lastPosMap.get(eui);
		
		if (np == null) {
			return 0.0;
		} else {
			return np.getY();
		}
	}
}
