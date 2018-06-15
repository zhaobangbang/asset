package com.lans.listener;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lans.controller.networkgw.msghandler.BeaconChooser;
import com.lans.controller.networkgw.msghandler.BeaconNode;
import com.lans.controller.networkgw.tvmessages.DLAck;
import com.lans.controller.networkgw.tvmessages.DLDevLoraConfig;
import com.lans.controller.networkgw.tvmessages.DLDevModeConfig;
import com.lans.controller.networkgw.tvmessages.DLDevRegResult;
import com.lans.controller.networkgw.tvmessages.EndDevAck;
import com.lans.controller.networkgw.tvmessages.EndDevAlarm;
import com.lans.controller.networkgw.tvmessages.EndDevHB;
import com.lans.controller.networkgw.tvmessages.EndDevHistoryPosList;
import com.lans.controller.networkgw.tvmessages.EndDevIndoorPos;
import com.lans.controller.networkgw.tvmessages.EndDevRealTimePos;
import com.lans.controller.networkgw.tvmessages.EndDevReg;
import com.lans.controller.networkgw.tvmessages.ScannedBeacon;
import com.lans.dao.BeaconsDAO;
import com.lans.dao.beans.Beacons;
import com.lans.fingerprint.FpLearnMachine;
import com.lans.infrastructure.util.BytesDump;
import com.lans.infrastructure.util.IntBytesConverter;
import com.lans.infrastructure.util.Trilateration;
import com.lansi.networkgw.p2p.P2pController;

class A {
	int a;
	public A() {
		
	}
	public A(int a) {
		this.a = a;
	}
	
	public void setA(int a) {
		this.a = a;
	}
	public int getA() {
		return a;
	}
}

class B {
	int b;
	public B() {
		
	}
	public B(int b) {
		this.b = b;
	}
	
	public void setB(int b) {
		this.b = b;
	}
	public int getB() {
		return b;
	}
}

public class Tests {
	
	static Logger logger = LoggerFactory.getLogger(Tests.class);
	public static void testDLMessage() {
		DLAck d1 = new DLAck((byte)0, (byte)12);
		d1.showTV();
		logger.info("DLAck {}", BytesDump.toString(d1.getBytes()));
		
		DLDevLoraConfig d2 = new DLDevLoraConfig((byte)1, (byte)3, (byte)8, (byte)0, (byte)3, (byte)25);
		d2.showTV();
		logger.info("DLDevLoraConfig {}", BytesDump.toString(d2.getBytes()));
		
		DLDevModeConfig d3 = new DLDevModeConfig((byte)1, (byte)1, (byte)1, (byte)1,(short)123, (byte)22);
		d3.showTV();
		logger.info("DLDevModeConfig {}", BytesDump.toString(d3.getBytes()));
		
		DLDevRegResult d4 = new DLDevRegResult((byte)2);
		d4.showTV();
		logger.info("DLDevRegResult {}", BytesDump.toString(d4.getBytes()));
		
	}
	
	public static void testULMessage() {
		byte[] devAck = new byte[2];
		devAck[0] = (byte) 0xF2;
		devAck[1] = (byte)123;
		
		EndDevAck e1 = new EndDevAck();
		e1 = (EndDevAck) e1.fromBytes(devAck, 0);
		e1.showTV();
		
		byte[] devalarm = new byte[2];
		devalarm[0] = (byte)0x51;
		devalarm[1] = (byte)111;
		
		EndDevAlarm e2 = new EndDevAlarm();
		e2 = (EndDevAlarm) e2.fromBytes(devalarm, 0);
		e2.showTV();
		
		byte[] devhb = new byte[7];
		devhb[0] = (byte)0x22;
		devhb[1] = (byte)99;
		devhb[2] = (byte)88;
		devhb[3] = (byte)0x33;
		devhb[4] = (byte)0x10;
		devhb[5] = (byte)0x11;
		devhb[6] = (byte)0x22;
		
		EndDevHB e3 = new EndDevHB();
		e3 = (EndDevHB) e3.fromBytes(devhb, 0);
		e3.showTV();
		
		byte[] devreg = new byte[9];
		devreg[0] = (byte)0x19;
		devreg[1] = (byte)8;
		devreg[2] = (byte)0x79;
		devreg[3] = (byte)0x3C;
		devreg[4] = 0x11;
		devreg[5] = 0x22;
		devreg[6] = 0x9;
		devreg[7] = 0x11;
		devreg[8] = 0x22;
		
		EndDevReg e4 = new EndDevReg();
		e4 = (EndDevReg) e4.fromBytes(devreg, 0);
		e4.showTV();
		
		byte[] devpos = new byte[14];
		devpos[0] = 0x38;
		devpos[1] = (byte)66;
		int lati = Float.floatToIntBits((float) 1.2345);
		byte[] bLati = IntBytesConverter.int2Bytes(lati);
		System.arraycopy(bLati, 0, devpos, 2, 4);
		int longi = Float.floatToIntBits((float) 2.2345);
		byte[] bLongi = IntBytesConverter.int2Bytes(longi);
		System.arraycopy(bLongi, 0, devpos, 6, 4);
		Date now = new Date();
		int iNow = (int) (now.getTime()/1000);
		logger.info("utc ms {} s {}", now.getTime(), iNow);
		byte[] bTime = IntBytesConverter.int2Bytes(iNow);
		System.arraycopy(bTime, 0, devpos, 10, 4);
		
		EndDevRealTimePos e5 = new EndDevRealTimePos();
		e5 = (EndDevRealTimePos) e5.fromBytes(devpos, 0);
		e5.showTV();
		
		byte[] devhis = new byte[25];
		devhis[0] = 0x42;
		int lati2 = Float.floatToIntBits((float) 11.2345);
		byte[] bLati2 = IntBytesConverter.int2Bytes(lati2);
		System.arraycopy(bLati2, 0, devhis, 1, 4);
		int longi2 = Float.floatToIntBits((float) 12.2345);
		byte[] bLongi2 = IntBytesConverter.int2Bytes(longi2);
		System.arraycopy(bLongi2, 0, devhis, 5, 4);
		Date now2 = new Date();
		int iNow2 = (int) (now2.getTime()/1000);
		logger.info("utc ms {} s {}", now2.getTime(), iNow2);
		byte[] bTime2 = IntBytesConverter.int2Bytes(iNow2);
		System.arraycopy(bTime2, 0, devhis, 9, 4);
		
		int latidiff1 = Float.floatToIntBits((float) 11.2135);
		int difflati1 = (latidiff1 - lati2);
		logger.info("diff latitude {}", Integer.toHexString(difflati1));
		devhis[13] = (byte) ((difflati1 >> 8) & 0xFF);
		devhis[14] = (byte) (difflati1 & 0xFF);
		int longidiff1 = Float.floatToIntBits((float) 12.2455);
		int difflongi1 = (longidiff1 - longi2);
		logger.info("diff longitude {}", Integer.toHexString(difflongi1));
		devhis[15] = (byte) ((difflongi1 >> 8) & 0xFF);
		devhis[16] = (byte) (difflongi1 & 0xFF);
		devhis[17] = (byte)0;
		devhis[18] = (byte)60;
		
		int latidiff2 = Float.floatToIntBits((float) 11.2005);
		int difflati2 = (latidiff2 - latidiff1);
		logger.info("diff2 latitude {}", Integer.toHexString(difflati2));
		devhis[19] = (byte) ((difflati2 >> 8) & 0xFF);
		devhis[20] = (byte) (difflati2 & 0xFF);
		int longidiff2 = Float.floatToIntBits((float) 12.2565);
		int difflongi2 = (longidiff2 - longidiff1);
		logger.info("diff2 longitude {}", Integer.toHexString(difflongi2));
		devhis[21] = (byte) ((difflongi2 >> 8) & 0xFF);
		devhis[22] = (byte) (difflongi2 & 0xFF);
		devhis[23] = (byte)0;
		devhis[24] = (byte)60;
		
		EndDevHistoryPosList e6 = new EndDevHistoryPosList();
		e6 = (EndDevHistoryPosList) e6.fromBytes(devhis, 0);
		e6.showTV();
	}
	
	public static void testBeacon() {
		BeaconChooser bc = new BeaconChooser();
		Beacons b1 = new Beacons();
		b1.setA(61.0);
		b1.setN(2.32535);
		b1.setX(1.32241284E7);
		b1.setY(3737994.38);
		b1.setMajor(11);
		b1.setMinor(2);
		b1.setPostype("ÊÒÄÚ");
		b1.setFlor("1");
		Beacons b2 = new Beacons();
		b2.setA(63.0);
		b2.setN(2.99658);
		b2.setX(1.322412439E7);
		b2.setY(3737948.41);
		b2.setMajor(11);
		b2.setMinor(4);
		b2.setPostype("ÊÒÄÚ");
		b2.setFlor("1");
		Beacons b3 = new Beacons();
		b3.setA(67.0);
		b3.setN(3.18974);
		b3.setX(1.322407541E7);
		b3.setY(3737955.99);
		b3.setMajor(11);
		b3.setMinor(3);
		b3.setPostype("ÊÒÄÚ");
		b3.setFlor("1");
		
		bc.addBeacon(b1, -64).addBeacon(b2, -71).addBeacon(b3, -79);
		List<BeaconNode> chooseList = bc.choose();
		for (BeaconNode bn : chooseList) {
			logger.info("beacon {} A {} N {} RSSI {} x {} y {}", bn.getBeacon().getMinor(),
					bn.getBeacon().getA(), bn.getBeacon().getN(), bn.getRssi(),
					bn.getBeacon().getX(), bn.getBeacon().getY());
		}
		
		BeaconNode node1 = chooseList.get(0);
		BeaconNode node2 = chooseList.get(1);
		BeaconNode node3 = chooseList.get(2);
					
		Trilateration tri = new Trilateration();
		tri.setBeacon1(node1.getBeacon().getX(), node1.getBeacon().getY(), 
					node1.getBeacon().getA(), node1.getBeacon().getN(), node1.getRssi(),node1.getBeacon().getMinor());
		tri.setBeacon2(node2.getBeacon().getX(), node2.getBeacon().getY(), 
					node2.getBeacon().getA(), node2.getBeacon().getN(), node2.getRssi(),node2.getBeacon().getMinor());
		tri.setBeacon3(node3.getBeacon().getX(), node3.getBeacon().getY(), 
					node3.getBeacon().getA(), node3.getBeacon().getN(), node3.getRssi(),node3.getBeacon().getMinor());
		tri.searchPoint();
		
	}
	
	public static void testP2pParser() {
		P2pController test = new P2pController(2001);
		
		byte[] rcv = new byte[10];
		rcv[0] = '1';
		rcv[1] = '2';
		rcv[2] = '3';
		rcv[3] = '4';
		rcv[4] = '5';
		rcv[5] = '6';
		rcv[6] = '7';
		rcv[7] = '8';
		rcv[8] = 0x1;
		rcv[9] = '9';
		
		test.onGateWayMsg(rcv, 10, 0);
	}
	
	public static void testHibernate() {
		try {
			List<Beacons> beaconList = BeaconsDAO.getAllBeacons();
			
			for (Beacons beacon : beaconList) {
				logger.info("beacon {} {} A {} n {}", beacon.getMajor(), beacon.getMinor(), 
						beacon.getA(), beacon.getN());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testFastJson() {
		List<Object> list = new LinkedList<Object>();
		list.add(new A(1));
		list.add(new B(2));
		
		String rspStr = JSON.toJSONString(list);
		logger.info("testFastJson {}", rspStr);
	}
	
	public static void testFingerprintAlgo() {
		FpLearnMachine machine = new FpLearnMachine("E:/map/");
		
		machine.startlearning("testeui", 12.345, 34.212);
		
		List<ScannedBeacon> bList = new LinkedList<ScannedBeacon>();
		bList.add(new ScannedBeacon(12, 1, -90));
		bList.add(new ScannedBeacon(12, 2, -60));
		bList.add(new ScannedBeacon(12, 3, -70));
		bList.add(new ScannedBeacon(12, 4, -20));
		EndDevIndoorPos msg = new EndDevIndoorPos();
		msg.beaconList = bList;
		machine.learnOnce("testeui", msg);
		
		bList = new LinkedList<ScannedBeacon>();
		bList.add(new ScannedBeacon(12, 1, -40));
		bList.add(new ScannedBeacon(12, 2, -80));
		bList.add(new ScannedBeacon(12, 3, -80));
		bList.add(new ScannedBeacon(12, 4, -100));
		msg = new EndDevIndoorPos();
		msg.beaconList = bList;
		machine.learnOnce("testeui", msg);
		
		bList = new LinkedList<ScannedBeacon>();
		bList.add(new ScannedBeacon(12, 1, -40));
		bList.add(new ScannedBeacon(12, 2, -80));
		bList.add(new ScannedBeacon(12, 3, -80));
		bList.add(new ScannedBeacon(12, 4, -98));
		msg = new EndDevIndoorPos();
		msg.beaconList = bList;
		machine.learnOnce("testeui", msg);
		
		bList = new LinkedList<ScannedBeacon>();
		bList.add(new ScannedBeacon(12, 1, -40));
		bList.add(new ScannedBeacon(12, 2, -80));
		bList.add(new ScannedBeacon(12, 3, -80));
		bList.add(new ScannedBeacon(12, 4, -98));
		msg = new EndDevIndoorPos();
		msg.beaconList = bList;
		machine.learnOnce("testeui", msg);
		
		machine.startlearning("testeui2", 66.345, 77.212);
		
		bList = new LinkedList<ScannedBeacon>();
		bList.add(new ScannedBeacon(12, 4, -90));
		bList.add(new ScannedBeacon(12, 3, -60));
		bList.add(new ScannedBeacon(12, 2, -70));
		bList.add(new ScannedBeacon(12, 5, -20));
		msg = new EndDevIndoorPos();
		msg.beaconList = bList;
		machine.learnOnce("testeui2", msg);
		
		bList = new LinkedList<ScannedBeacon>();
		bList.add(new ScannedBeacon(12, 4, -40));
		bList.add(new ScannedBeacon(12, 3, -80));
		bList.add(new ScannedBeacon(12, 2, -80));
		bList.add(new ScannedBeacon(12, 1, -100));
		msg = new EndDevIndoorPos();
		msg.beaconList = bList;
		machine.learnOnce("testeui2", msg);
		
		bList = new LinkedList<ScannedBeacon>();
		bList.add(new ScannedBeacon(12, 3, -40));
		bList.add(new ScannedBeacon(12, 4, -80));
		bList.add(new ScannedBeacon(12, 2, -80));
		bList.add(new ScannedBeacon(12, 5, -98));
		msg = new EndDevIndoorPos();
		msg.beaconList = bList;
		machine.learnOnce("testeui2", msg);
		
		bList = new LinkedList<ScannedBeacon>();
		bList.add(new ScannedBeacon(12, 5, -40));
		bList.add(new ScannedBeacon(12, 3, -80));
		bList.add(new ScannedBeacon(12, 4, -80));
		bList.add(new ScannedBeacon(12, 1, -98));
		msg = new EndDevIndoorPos();
		msg.beaconList = bList;
		machine.learnOnce("testeui2", msg);

		machine.saveLearning();
		
		bList = new LinkedList<ScannedBeacon>();
		bList.add(new ScannedBeacon(12, 1, -40));
		bList.add(new ScannedBeacon(12, 2, -80));
		bList.add(new ScannedBeacon(12, 3, -80));
		bList.add(new ScannedBeacon(12, 4, -100));
		msg = new EndDevIndoorPos();
		msg.beaconList = bList;
		
		machine.findBestMatch(msg);
	}
	
	public static void runTests() {
		testFingerprintAlgo();
	}
}
