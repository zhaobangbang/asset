package com.lans.controller.networkgw.msghandler;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lans.controller.networkgw.TVMsgDefs;
import com.lans.controller.networkgw.tvmessages.EndDevCoordinate;
import com.lans.controller.networkgw.tvmessages.ScannedBeacon;
import com.lans.servlets.DevMsgHandler;
import com.lansi.msghandle.itftv.IEndDevItfTV;
import com.lansi.msghandle.itftv.IEndDevTVMsgHandler;
import com.lansi.networkgw.EndDevEnvInfo;
import com.lansi.networkgw.IGateWayConnLayer3;

import net.sf.json.JSONObject;

public class EndDevCoordinateHandler implements IEndDevTVMsgHandler {
	Logger logger = LoggerFactory.getLogger(EndDevCoordinateHandler.class);
	IGateWayConnLayer3 l3;
	
	public EndDevCoordinateHandler(IGateWayConnLayer3 connL3) {
		l3 = connL3;
	}

	@Override
	public boolean isHandlerOfMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		if(upMsg.getType() == TVMsgDefs.UL_DEV_COORDINATE){
			return true;
		}
		return false;
	}

	@Override
	public void processMsg(EndDevEnvInfo devInfo, IEndDevItfTV upMsg) {
		EndDevCoordinate devcCoordinate = (EndDevCoordinate) upMsg;
		String deveui = devInfo.getEui();

		//根据信号强度排序
		Collections.sort(devcCoordinate.beaconList, new Comparator<ScannedBeacon>() {

			@Override
			public int compare(ScannedBeacon a, ScannedBeacon b) {
				int one = a.getRssi();
				int two = a.getRssi();
				return new Integer(two).compareTo(one);
			}
		});
		
		devcCoordinate.showTV();
		
		String minorList="";
		for (ScannedBeacon node : devcCoordinate.beaconList) {
			minorList += node.getMinor() + ",";
		}
		
		JSONObject jObject = new JSONObject();
		jObject.element("msgType", "indoor");
		jObject.element("deveui", deveui);
		jObject.element("num", devcCoordinate.beaconList.size());
		jObject.element("beacon", minorList);
		try {
			DevMsgHandler.updateToObserver(deveui, jObject);
			//DevMsgHandler.updateDevToObserver(deveui, jObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
