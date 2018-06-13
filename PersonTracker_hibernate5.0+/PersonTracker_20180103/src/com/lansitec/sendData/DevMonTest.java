package com.lansitec.sendData;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.servlets.DevMsgHandler;
import com.lansitec.systemconfig.dao.ORMFactory;


import net.sf.json.JSONObject;

public class DevMonTest extends TimerTask{
	private Logger logger = LoggerFactory.getLogger(DevMonTest.class);
    public static int a = 0;
    public static int b = 0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String[] deveui = {"004a770211030001","004a770211030002","004a770211030003","004a770211030004","004a770211030005","004a770211030006","004a770211030007","004a770211030008","004a770211030009","004a77021103000a",
				"004a77021103000b","004a77021103000c","004a77021103000d","004a77021103000e","004a77021103000f","004a770211030010","004a770211030011","004a770211030012","004a770211030013","004a770211030014",
				"004a770211030015","004a770211030016","004a770211030017","004a770211030018","004a770211030019","004a77021103001a","004a77021103001b","004a77021103001c","004a77021103001d","004a77021103001e",
				"004a77021103002b","004a77021103002c","004a77021103002d","004a77021103002e","004a77021103002f","004a770211030030","004a770211030031","004a770211030032","004a770211030033","004a770211030034",
				"004a77021103001f","004a770211030020","004a770211030021","004a770211030022","004a770211030023","004a770211030024","004a770211030025","004a770211030026","004a770211030027","004a770211030028",
				"004a770211030029","004a77021103002a","004a77021103002b","004a77021103002c","004a77021103002d","004a77021103002e","004a77021103002f","004a770211030030","004a770211030031","004a770211030032",
				"004a770211030033","004a770211030034","004a770211030035","004a770211030036","004a770211030037","004a770211030038","004a770211030039","004a77021103003a","004a77021103003b","004a77021103003c",
				"004a77021103003d","004a77021103003e","004a77021103003f","004a770211030040","004a770211030041","004a770211030042","004a770211030043","004a770211030044","004a770211030045","004a770211030046",
				"004a770211030047","004a770211030048","004a770211030049","004a77021103004a","004a77021103004b","004a77021103004c","004a77021103004d","004a77021103004e","004a77021103004f","004a770211030050",
				"004a770211030051","004a770211030052","004a770211030053","004a770211030054","004a770211030055","004a770211030056","004a770211030057","004a770211030058","004a770211030059","004a77021103005a",
				"004a77021103005b","004a77021103005c","004a77021103005d","004a77021103005e","004a77021103005f","004a770211030060","004a770211030061","004a770211030062","004a770211030063","004a770211030064",
				"004a770211030065","004a770211030066","004a770211030067","004a770211030068","004a770211030069","004a77021103006a","004a77021103006b","004a77021103006c","004a77021103006d","004a77021103006e",
				"004a77021103006f","004a770211030070","004a770211030071","004a770211030072","004a770211030073","004a770211030074","004a770211030075","004a770211030076","004a770211030077","004a770211030078",
				"004a770211030079","004a77021103007a","004a77021103007b","004a77021103007c","004a77021103007d","004a77021103007e","004a77021103007f","004a770211030080","004a770211030081","004a770211030082",
				"004a770211030083","004a770211030084","004a770211030085","004a770211030086","004a770211030087","004a770211030088","004a770211030089","004a77021103008a","004a77021103008b","004a77021103008c",
				"004a77021103008d","004a77021103008e","004a77021103008f","004a770211030090","004a770211030091","004a770211030092","004a770211030093","004a770211030094","004a770211030095","004a770211030096",
				"004a770211030097","004a770211030098","004a770211030099","004a77021103009a","004a77021103009b","004a77021103009c","004a77021103009d","004a77021103009e","004a77021103009f","004a7702110300a0",
				"004a7702110300a1","004a7702110300a2","004a7702110300a3","004a7702110300a4","004a7702110300a5","004a7702110300a6","004a7702110300a7","004a7702110300a8","004a7702110300a9","004a7702110300aa",
				"004a7702110300ab","004a7702110300ac","004a7702110300ad","004a7702110300ae","004a7702110300af","004a7702110300b0","004a7702110300b1","004a7702110300b2","004a7702110300b3","004a7702110300b4",
				"004a7702110300b5","004a7702110300b6","004a7702110300b7","004a7702110300b8","004a7702110300b9","004a7702110300ba","004a7702110300bb","004a7702110300bc","004a7702110300bd","004a7702110300be",
				"004a7702110300bf","004a7702110300c0","004a7702110300c1","004a7702110300c2","004a7702110300c3","004a7702110300c4","004a7702110300c5","004a7702110300c6","004a7702110300c7","004a7702110300c8"
				};
		
		JSONObject jsonMsg = new JSONObject();
		if(a >=200 ){
			a = 0;
		}
		/*String dev = "004a77021103";
		String dara = Integer.toHexString(a);
		String finaldev = "";*/
		
		deveui[a] = "004a77021103";
		//for(int c = a; c<f; c++){
			PositionTest test = respData();
			String battery = "80%";
			logger.info("deveui {}",deveui[a]);
			jsonMsg.element("DevEUI", deveui[a]);
	    	jsonMsg.element("msgType", "REG");
	    	jsonMsg.element("battery", battery);
	    	String vibt = null;
	    	byte vib = 0;
	    	if(vib == 0){
	    		vibt = "静止";
	    	}else{
	    		vibt = "运动强度："+Byte.toString(vib);
	    	}
	    	
	    	jsonMsg.element("vib", vibt);
	    	jsonMsg.element("rssi", "32");
	    	jsonMsg.element("x", test.getX());
	    	jsonMsg.element("y", test.getY());
	    	jsonMsg.element("time", "2018-04-26");
		//}
		
		try {
			DevMsgHandler.updateToMapId(deveui[a], jsonMsg);
			//DevMsgHandler.updateToObserver(deveui[a], jsonMsg);
			/*DevMsgHandler.updateToObserver(deveui[a+1], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+2], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+3], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+4], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+5], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+6], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+7], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+8], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+9], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+10], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+11], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+12], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+13], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+14], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+15], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+16], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+17], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+18], jsonMsg);
			DevMsgHandler.updateToObserver(deveui[a+19], jsonMsg);*/
			a++;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public PositionTest respData(){
		
		/*float[] x = {(float) 108.23,(float) 125.23,(float) 134.20,(float) 152.23,(float) 168.30,(float) 175.20,(float) 184.32,(float) 197.23,(float) 206.23,(float) 218.20,(float) 229.20,(float) 233.20,(float) 245.26,(float) 258.56,(float) 266.11,
				     (float) 275.30,(float) 288.20,(float) 297.32,(float) 308.20,(float) 319.30,(float) 325.44,(float) 338.20,(float) 346.20,(float) 354.30,(float) 368.20,(float) 374.20,(float) 381.10,(float) 399.30,(float) 405.20,(float) 419.22,
				     (float) 428.23,(float) 435.23,(float) 444.20,(float) 452.23,(float) 468.30,(float) 475.20,(float) 484.32,(float) 497.23,(float) 506.23,(float) 518.20,(float) 529.20,(float) 533.20,(float) 545.26,(float) 558.56,(float) 566.11,
				     (float) 108.23,(float) 125.23,(float) 134.20,(float) 152.23,(float) 168.30,(float) 175.20,(float) 184.32,(float) 197.23,(float) 206.23,(float) 218.20,(float) 229.20,(float) 233.20,(float) 245.26,(float) 258.56,(float) 266.11,
				     (float) 275.30,(float) 288.20,(float) 297.32,(float) 308.20,(float) 319.30,(float) 325.44,(float) 338.20,(float) 346.20,(float) 354.30,(float) 368.20,(float) 374.20,(float) 381.10,(float) 399.30,(float) 405.20,(float) 419.22,
				     (float) 428.23,(float) 435.23,(float) 444.20,(float) 452.23,(float) 468.30,(float) 475.20,(float) 484.32,(float) 497.23,(float) 506.23,(float) 518.20,(float) 529.20,(float) 533.20,(float) 545.26,(float) 558.56,(float) 566.11,
				     (float) 108.23,(float) 125.23,(float) 134.20,(float) 152.23,(float) 168.30,(float) 175.20,(float) 184.32,(float) 197.23,(float) 206.23,(float) 218.20,(float) 229.20,(float) 233.20,(float) 245.26,(float) 258.56,(float) 266.11,
				     (float) 275.30,(float) 288.20,(float) 297.32,(float) 308.20,(float) 319.30,(float) 325.44,(float) 338.20,(float) 346.20,(float) 354.30,(float) 368.20,(float) 374.20,(float) 381.10,(float) 399.30,(float) 405.20,(float) 419.22,
				     (float) 428.23,(float) 435.23,(float) 444.20,(float) 452.23,(float) 468.30,(float) 475.20,(float) 484.32,(float) 497.23,(float) 506.23,(float) 518.20,(float) 529.20,(float) 533.20,(float) 545.26,(float) 558.56,(float) 566.11,
				     (float) 108.23,(float) 125.23,(float) 134.20,(float) 152.23,(float) 168.30,(float) 175.20,(float) 184.32,(float) 197.23,(float) 206.23,(float) 218.20,(float) 229.20,(float) 233.20,(float) 245.26,(float) 258.56,(float) 266.11,
				     (float) 275.30,(float) 288.20,(float) 297.32,(float) 308.20,(float) 319.30,(float) 325.44,(float) 338.20,(float) 346.20,(float) 354.30,(float) 368.20,(float) 374.20,(float) 381.10,(float) 399.30,(float) 405.20,(float) 419.22,
				     (float) 428.23,(float) 435.23,(float) 444.20,(float) 452.23,(float) 468.30,(float) 475.20,(float) 484.32,(float) 497.23,(float) 506.23,(float) 518.20,(float) 529.20,(float) 533.20,(float) 545.26,(float) 558.56,(float) 566.11,
				     (float) 108.23,(float) 125.23,(float) 134.20,(float) 152.23,(float) 168.30,(float) 175.20,(float) 184.32,(float) 197.23,(float) 206.23,(float) 218.20,(float) 229.20,(float) 233.20,(float) 245.26,(float) 258.56,(float) 266.11,
				     (float) 275.30,(float) 288.20,(float) 297.32,(float) 308.20,(float) 319.30,(float) 325.44,(float) 338.20,(float) 346.20,(float) 354.30,(float) 368.20,(float) 374.20,(float) 381.10,(float) 399.30,(float) 405.20,(float) 419.22,
				     (float) 428.23,(float) 435.23,(float) 444.20,(float) 452.23,(float) 468.30,(float) 475.20,(float) 484.32,(float) 497.23,(float) 506.23,(float) 518.20,(float) 529.20,(float) 533.20,(float) 545.26,(float) 558.56,(float) 566.11
		             };
		
		float[] y = {(float) 508.20,(float) 614.30,(float) 725.20,(float) 764.30,(float) 228.20,(float) 366.25,(float) 428.60,(float) 569.50,(float) 123.10,(float) 368.10,(float) 419.30,(float) 628.30,(float) 769.10,(float) 523.20,(float) 168.50,
				     (float) 622.30,(float) 765.30,(float) 829.30,(float) 968.20,(float) 127.10,(float) 562.30,(float) 729.30,(float) 468.20,(float) 825.60,(float) 167.30,(float) 721.20,(float) 369.60,(float) 825.20,(float) 268.20,(float) 526.70,
				     (float) 528.20,(float) 164.30,(float) 325.20,(float) 864.30,(float) 428.20,(float) 966.25,(float) 128.60,(float) 769.50,(float) 223.10,(float) 368.10,(float) 819.30,(float) 328.30,(float) 769.10,(float) 123.20,(float) 968.50,
				     (float) 508.20,(float) 614.30,(float) 725.20,(float) 764.30,(float) 228.20,(float) 366.25,(float) 428.60,(float) 569.50,(float) 123.10,(float) 368.10,(float) 419.30,(float) 628.30,(float) 769.10,(float) 523.20,(float) 168.50,
				     (float) 622.30,(float) 765.30,(float) 829.30,(float) 968.20,(float) 127.10,(float) 562.30,(float) 729.30,(float) 468.20,(float) 825.60,(float) 167.30,(float) 721.20,(float) 369.60,(float) 825.20,(float) 268.20,(float) 526.70,
				     (float) 528.20,(float) 164.30,(float) 325.20,(float) 864.30,(float) 428.20,(float) 966.25,(float) 128.60,(float) 769.50,(float) 223.10,(float) 368.10,(float) 819.30,(float) 328.30,(float) 769.10,(float) 123.20,(float) 968.50,
				     (float) 508.20,(float) 614.30,(float) 725.20,(float) 764.30,(float) 228.20,(float) 366.25,(float) 428.60,(float) 569.50,(float) 123.10,(float) 368.10,(float) 419.30,(float) 628.30,(float) 769.10,(float) 523.20,(float) 168.50,
				     (float) 622.30,(float) 765.30,(float) 829.30,(float) 968.20,(float) 127.10,(float) 562.30,(float) 729.30,(float) 468.20,(float) 825.60,(float) 167.30,(float) 721.20,(float) 369.60,(float) 825.20,(float) 268.20,(float) 526.70,
				     (float) 528.20,(float) 164.30,(float) 325.20,(float) 864.30,(float) 428.20,(float) 966.25,(float) 128.60,(float) 769.50,(float) 223.10,(float) 368.10,(float) 819.30,(float) 328.30,(float) 769.10,(float) 123.20,(float) 968.50,
				     (float) 508.20,(float) 614.30,(float) 725.20,(float) 764.30,(float) 228.20,(float) 366.25,(float) 428.60,(float) 569.50,(float) 123.10,(float) 368.10,(float) 419.30,(float) 628.30,(float) 769.10,(float) 523.20,(float) 168.50,
				     (float) 622.30,(float) 765.30,(float) 829.30,(float) 968.20,(float) 127.10,(float) 562.30,(float) 729.30,(float) 468.20,(float) 825.60,(float) 167.30,(float) 721.20,(float) 369.60,(float) 825.20,(float) 268.20,(float) 526.70,
				     (float) 528.20,(float) 164.30,(float) 325.20,(float) 864.30,(float) 428.20,(float) 966.25,(float) 128.60,(float) 769.50,(float) 223.10,(float) 368.10,(float) 819.30,(float) 328.30,(float) 769.10,(float) 123.20,(float) 968.50,
				     (float) 508.20,(float) 614.30,(float) 725.20,(float) 764.30,(float) 228.20,(float) 366.25,(float) 428.60,(float) 569.50,(float) 123.10,(float) 368.10,(float) 419.30,(float) 628.30,(float) 769.10,(float) 523.20,(float) 168.50,
				     (float) 622.30,(float) 765.30,(float) 829.30,(float) 968.20,(float) 127.10,(float) 562.30,(float) 729.30,(float) 468.20,(float) 825.60,(float) 167.30,(float) 721.20,(float) 369.60,(float) 825.20,(float) 268.20,(float) 526.70,
				     (float) 528.20,(float) 164.30,(float) 325.20,(float) 864.30,(float) 428.20,(float) 966.25,(float) 128.60,(float) 769.50,(float) 223.10,(float) 368.10,(float) 819.30,(float) 328.30,(float) 769.10,(float) 123.20,(float) 968.50
		             };*/
		float x = (float) (Math.random()*1080+1);
		float y = (float) (Math.random()*1170+1);
		PositionTest potest = new PositionTest(x, y);
		return potest;
	}
	
	@Test
	public void pp(){
		ORMFactory.initialize();
		String deveui = "004a770211030003";
		String deveui1 = "004a770211030004";
		String deveui2 = "004a770211030006";;
		String sql = "select * from dev_info_tbl where deveui = "+deveui;
		String dateTimeOne = "2018-03-27 11:11:39";
		String dateTimeTwo = "2018-03-27 11:15:21";
		
		String sqla = "from DevInfo dev where dev.deveui = :deveui ORDER BY deveui desc";
		try {
			/*DevInfo devInfo = DevInfoDAO.getDevInfoByDeveui(deveui);
			if(null != devInfo){
				String owner = devInfo.getOwner();
				logger.info("owner {}",owner);
			}*/
			/*List<DevInfo> devInfoList = DevInfoDAO.getDevInfoBySQLParament(sqla,"deveui",deveui,0,2);
			logger.info("DevInfoList size {}",devInfoList.size());*/
			/*List<DevInfo> devInfoLista = DevInfoDAO.getAllDevInfo();
			logger.info("DevInfoList size {}",devInfoLista.size());
			List<DevInfo> devInfoListb = DevInfoDAO.getDevInfoByDevtype("工人");
			logger.info("DevInfoList size {}",devInfoListb.size());*/
		    List<PositionRecord> postList = PositionRecordDAO.test(deveui, deveui1,deveui2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
