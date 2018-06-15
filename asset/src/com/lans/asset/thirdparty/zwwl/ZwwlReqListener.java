package com.lans.asset.thirdparty.zwwl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lans.beans.DevOpr;
import com.lans.beans.DevicesOperateBean;
import com.lans.common.DataBaseMgr;
import com.lans.common.QueryResult;
import com.lans.dao.BlueToothBraceletDataDAO;
import com.lans.dao.DevConfigDAO;
import com.lans.dao.GpsDataDAO;
import com.lans.dao.ZwwlDevsUseInfoDAO;
import com.lans.dao.ZwwldevicesDAO;
import com.lans.dao.beans.BlueToothBraceletData;
import com.lans.dao.beans.DevConfig;
import com.lans.dao.beans.GpsData;
import com.lans.dao.beans.ZwwlDevsUseInfo;
import com.lans.dao.beans.Zwwldevices;
import com.lansi.thirdparty.IReqListener;
import com.lansi.thirdparty.ReqProcRslt;
import com.lansi.thirdparty.ReqProcRsltType;
import com.lansi.thirdparty.zwwl.jsondefs.AddDeviceReq;
import com.lansi.thirdparty.zwwl.jsondefs.AddDeviceRsp;
import com.lansi.thirdparty.zwwl.jsondefs.AddDeviceRspData;
import com.lansi.thirdparty.zwwl.jsondefs.BatRetCowNewFuncStatusRsp;
import com.lansi.thirdparty.zwwl.jsondefs.BatRetCowNewFuncStatusRspData;
import com.lansi.thirdparty.zwwl.jsondefs.BatRetNewFuncStatusDevsData;
import com.lansi.thirdparty.zwwl.jsondefs.BatRetNewFuncStatusReq;
import com.lansi.thirdparty.zwwl.jsondefs.BatRetNewFuncStatusRsp;
import com.lansi.thirdparty.zwwl.jsondefs.BatRetNewFuncStatusRspData;
import com.lansi.thirdparty.zwwl.jsondefs.BatchLocationAndSteps;
import com.lansi.thirdparty.zwwl.jsondefs.BatchRetNewCowDevDataRsp;
import com.lansi.thirdparty.zwwl.jsondefs.BatchRetNewCowDevDataRspData;
import com.lansi.thirdparty.zwwl.jsondefs.BatchRetNewDataDevidsReq;
import com.lansi.thirdparty.zwwl.jsondefs.BatchRetNewDataRsp;
import com.lansi.thirdparty.zwwl.jsondefs.BatchRetNewDataRspData;
import com.lansi.thirdparty.zwwl.jsondefs.BatteryListData;
import com.lansi.thirdparty.zwwl.jsondefs.BatterythrData;
import com.lansi.thirdparty.zwwl.jsondefs.Calories;
import com.lansi.thirdparty.zwwl.jsondefs.CaloriesData;
import com.lansi.thirdparty.zwwl.jsondefs.CfgDeviceReq;
import com.lansi.thirdparty.zwwl.jsondefs.CfgMetaData;
import com.lansi.thirdparty.zwwl.jsondefs.CompanyName;
import com.lansi.thirdparty.zwwl.jsondefs.CowDevAndCfgData;
import com.lansi.thirdparty.zwwl.jsondefs.DataMetaData;
import com.lansi.thirdparty.zwwl.jsondefs.DelDeviceReq;
import com.lansi.thirdparty.zwwl.jsondefs.DevAndCfgData;
import com.lansi.thirdparty.zwwl.jsondefs.DevAndGpsData;
import com.lansi.thirdparty.zwwl.jsondefs.DevId;
import com.lansi.thirdparty.zwwl.jsondefs.DevLocation;
import com.lansi.thirdparty.zwwl.jsondefs.DevLocationAndBattery;
import com.lansi.thirdparty.zwwl.jsondefs.DevType;
import com.lansi.thirdparty.zwwl.jsondefs.DevsData;
import com.lansi.thirdparty.zwwl.jsondefs.EventMetaData;
import com.lansi.thirdparty.zwwl.jsondefs.GWPosAndDistance;
import com.lansi.thirdparty.zwwl.jsondefs.HBandBloodPreData;
import com.lansi.thirdparty.zwwl.jsondefs.HeartBeatAndBloodPre;
import com.lansi.thirdparty.zwwl.jsondefs.LocationAndStepsAndBatteryData;
import com.lansi.thirdparty.zwwl.jsondefs.LocationListData;
import com.lansi.thirdparty.zwwl.jsondefs.ModelName;
import com.lansi.thirdparty.zwwl.jsondefs.OnlineStatusData;
import com.lansi.thirdparty.zwwl.jsondefs.ProductName;
import com.lansi.thirdparty.zwwl.jsondefs.QueryCfgData;
import com.lansi.thirdparty.zwwl.jsondefs.QueryCowCfgData;
import com.lansi.thirdparty.zwwl.jsondefs.RetCowNewFuncStatusRsp;
import com.lansi.thirdparty.zwwl.jsondefs.RetCowNewFuncStatusRspData;
import com.lansi.thirdparty.zwwl.jsondefs.RetEventSchemaReq;
import com.lansi.thirdparty.zwwl.jsondefs.RetEventSchemaRsp;
import com.lansi.thirdparty.zwwl.jsondefs.RetEventSchemaRspData;
import com.lansi.thirdparty.zwwl.jsondefs.RetFuncSchemaDataReq;
import com.lansi.thirdparty.zwwl.jsondefs.RetFuncSchemaDataRsp;
import com.lansi.thirdparty.zwwl.jsondefs.RetFuncSchemaDataRspData;
import com.lansi.thirdparty.zwwl.jsondefs.RetNewFuncStatusReq;
import com.lansi.thirdparty.zwwl.jsondefs.RetNewFuncStatusRsp;
import com.lansi.thirdparty.zwwl.jsondefs.RetNewFuncStatusRspData;
import com.lansi.thirdparty.zwwl.jsondefs.RetWholeDevSchemaReq;
import com.lansi.thirdparty.zwwl.jsondefs.RetWholeDevSchemaRsp;
import com.lansi.thirdparty.zwwl.jsondefs.RetWholeDevSchemaRspData;
import com.lansi.thirdparty.zwwl.jsondefs.RetrieveDataSchemaReq;
import com.lansi.thirdparty.zwwl.jsondefs.RetrieveDataSchemaRsp;
import com.lansi.thirdparty.zwwl.jsondefs.RetrieveDataSchemaRspData;
import com.lansi.thirdparty.zwwl.jsondefs.RetrieveNewestCowDevDataRsp;
import com.lansi.thirdparty.zwwl.jsondefs.RetrieveNewestCowDevDataRspData;
import com.lansi.thirdparty.zwwl.jsondefs.RetrieveNewestDataReq;
import com.lansi.thirdparty.zwwl.jsondefs.RetrieveNewestDataRsp;
import com.lansi.thirdparty.zwwl.jsondefs.RetrieveNewestDataRspData;
import com.lansi.thirdparty.zwwl.jsondefs.StepsData;
import com.lansi.thirdparty.zwwl.jsondefs.StepsListData;
import com.lansi.thirdparty.zwwl.jsondefs.SupportDataList;
import com.lansi.thirdparty.zwwl.jsondefs.XYandDistanceData;
import com.lansi.thirdparty.zwwl.msgSender.ZwwlAuth;

public class ZwwlReqListener implements IReqListener {
	
	public static String CFGMODE_POSITION = "0";
	public static String CFGMODE_SHAKE = "1";
	private String devModelName = "100-00035";
	private String cowDevModelName = "100-00036";
	private String handDevModelName ="100-10001";
	private String xtDevModelName = "100-02372";
	private Logger logger = LoggerFactory.getLogger(ZwwlReqListener.class);
	
	@Test
	public void testa(){
		//genAddDeviceRsp("004a770211030002","0","100-02372");
		//genRetWholeDevSchemaReq("004a770211030002","0","100-02372");
		//genRetrieveDataSchemaRsp();
		//genRetFuncSchemaDataRsp("100-00035");
		//genRetFuncSchemaDataRsp("100-02372");
		//genRetEventSchemaRsp("100-00035");
		//genRetEventSchemaRsp("100-02372");
		
		//genAddCowDeviceRsp("004a770211030002","0",handDevModelName);
		//genAddCowDeviceRsp("004a770211030003","0",cowDevModelName);
		//genRetWholeCowDevSchemaReq("004a770211030002","0",handDevModelName);
		//genRetrieveCowDevDataSchemaRsp(handDevModelName);
		//genRetCowDevFuncSchemaDataRsp(handDevModelName);
		genAddCowDeviceRsp("004a7702aa","0",cowDevModelName);
	}
	
	public AddDeviceRsp genAddDeviceRsp(String devId,String status,String devModel) {
		List<Object> attrList = new LinkedList<Object>();
        List<CfgMetaData> funcList = new LinkedList<CfgMetaData>();
		List<EventMetaData> eventList = new LinkedList<EventMetaData>();
		
		if(devModel.equalsIgnoreCase(devModelName)){
			attrList.add(new CompanyName("蓝思科技", "厂商名称"));
			attrList.add(new ProductName("资产管理终端", "产品名称"));
			attrList.add(new ModelName(devModel,"型号名称"));
			attrList.add(new DevId(devId.toUpperCase(), "设备标识"));
			attrList.add(new DevType("资产监控终端", "设备类型"));
			
			funcList.add(new CfgMetaData("cfgmode", "enum", "配置模式", "0", "0:定位模式,1:震动模式", "RW"));
			funcList.add(new CfgMetaData("movethr", "int", "移动报警阈值", "1", "1,9", "RW"));
			funcList.add(new CfgMetaData("energyCycleValue", "int", "电量周期值", "120", "10,1440", "RW"));
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阔值", "20", "1,90", "RW"));
			
			eventList.add(new EventMetaData("1001", "设备移动"));
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1","设备上线"));
			eventList.add(new EventMetaData("0","设备下线"));
		}else if(devModel.equalsIgnoreCase(xtDevModelName)){
			attrList.add(new CompanyName("蓝思科技", "厂商名称"));
			attrList.add(new ProductName("工卡型资产定位终端", "产品名称"));
			attrList.add(new ModelName(devModel,"型号名称"));
			attrList.add(new DevId(devId.toUpperCase(), "设备标识"));
			attrList.add(new DevType("室内外资产监控终端", "设备类型"));
			
			funcList.add(new CfgMetaData("energyCycleValue", "int", "电量周期值", "120", "10,1440", "RW"));
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
			
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1","设备上线"));
			eventList.add(new EventMetaData("0","设备下线"));
		}
		
		List<Object> location = new LinkedList<Object>();
		location.add(new LocationListData("终端位置"));
		location.add(new DataMetaData("地图ID", "mapId", "string"));
		location.add(new DataMetaData("经度", "gpsLong", "string"));
		location.add(new DataMetaData("纬度", "gpsLat", "string"));
		
		List<Object> batterythr = new LinkedList<Object>();
		batterythr.add(new BatteryListData("电量值"));
		batterythr.add(new DataMetaData("电量", "battery", "string"));
		
		SupportDataList dataList = new SupportDataList(location,batterythr);
		
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		OnlineStatusData onlineStatus = new OnlineStatusData(datetime, status);
		
		AddDeviceRspData data = new AddDeviceRspData(attrList, funcList, eventList, dataList,onlineStatus);
		AddDeviceRsp rsp = new AddDeviceRsp("0", data);
		
	    //String rep = JSON.toJSONString(rsp);
	    //System.out.println(rep);
	    
		return rsp;
	}
	
	public AddDeviceRsp genAddCowDeviceRsp(String devid,String status,String devModel){
		List<Object> attrList = new LinkedList<Object>();
		List<CfgMetaData> funcList = new LinkedList<CfgMetaData>();
		List<EventMetaData> eventList = new LinkedList<EventMetaData>();
		List<Object> location = new LinkedList<Object>();
		List<Object> steps = new LinkedList<Object>();
		steps.add(new StepsListData("运动步数"));
		steps.add(new DataMetaData("步数", "step", "string"));
		
		List<Object> batterythr = new LinkedList<Object>();
		batterythr.add(new BatteryListData("电量值"));
		batterythr.add(new DataMetaData("电量", "battery", "string"));
		SupportDataList dataList = null;
		if(devModel.equalsIgnoreCase(cowDevModelName)){
			attrList.add(new CompanyName("蓝思科技", "厂商名称"));
			attrList.add(new ProductName("资产管理终端", "产品名称"));
			attrList.add(new ModelName(devModel,"型号名称"));
			attrList.add(new DevId(devid.toUpperCase(), "设备标识"));
			attrList.add(new DevType("资产监控终端", "设备类型"));
			
			funcList.add(new CfgMetaData("energyCycleValue", "int", "电量周期值", "120", "10,1440", "RW"));
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
			funcList.add(new CfgMetaData("stepsCycleValue", "int", "计步周期值", "10", "10,120", "RW"));
			
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1","设备上线"));
			eventList.add(new EventMetaData("0","设备下线"));
			
			location.add(new LocationListData("终端位置"));
			location.add(new DataMetaData("地图ID", "mapId", "string"));
			location.add(new DataMetaData("经度", "gpsLong", "string"));
			location.add(new DataMetaData("纬度", "gpsLat", "string"));
			
			dataList = new SupportDataList(location,steps,batterythr);
			
		}else if(devModel.equalsIgnoreCase(handDevModelName)){
			attrList.add(new CompanyName("蓝思科技", "厂商名称"));
			attrList.add(new ProductName("蓝牙手环", "产品名称"));
			attrList.add(new ModelName(devModel,"型号名称"));
			attrList.add(new DevId(devid.toUpperCase(), "设备标识"));
			attrList.add(new DevType("蓝牙手环监控设备", "设备类型"));
			
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
			
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1003","设备SOS告警"));
			
			location.add(new LocationListData("蓝牙手环位置"));
			location.add(new DataMetaData("地图ID", "mapId", "string"));
			location.add(new DataMetaData("经度", "gpsLong", "string"));
			location.add(new DataMetaData("纬度", "gpsLat", "string"));
			
			List<Object> calories = new LinkedList<Object>();
			calories.add(new CaloriesData("运动热量"));
			calories.add(new DataMetaData("卡洛里", "caloric", "string"));
			
			List<Object> heartBeatAndBloodPre = new LinkedList<Object>();
			heartBeatAndBloodPre.add(new HBandBloodPreData("心跳和血压值"));
			heartBeatAndBloodPre.add(new DataMetaData("心跳", "heartBeat", "string"));
			heartBeatAndBloodPre.add(new DataMetaData("收缩压", "systolicPressure", "string"));
			heartBeatAndBloodPre.add(new DataMetaData("舒张压", "diastolicPressure", "string"));
			
			List<Object> gwPosAndDistance = new LinkedList<Object>();
			gwPosAndDistance.add(new XYandDistanceData("网关坐标和距离值"));
			gwPosAndDistance.add(new DataMetaData("网关x坐标", "gatewayX", "string"));
			gwPosAndDistance.add(new DataMetaData("网关y坐标", "gatewayY", "string"));
			gwPosAndDistance.add(new DataMetaData("距离", "distance", "string"));
			
			dataList = new SupportDataList(location, steps, calories, batterythr, heartBeatAndBloodPre, gwPosAndDistance);
		}
		
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		OnlineStatusData onlineStatus = new OnlineStatusData(datetime, status);
		
		AddDeviceRspData data = new AddDeviceRspData(attrList, funcList, eventList, dataList,onlineStatus);
		AddDeviceRsp rsp = new AddDeviceRsp("0", data);
	    //String rep = JSON.toJSONString(rsp);
	    //System.out.println(rep);
		return rsp;
	}
	
    public RetWholeDevSchemaRsp genRetWholeDevSchemaReq(String devId,String status,String devModel) {
    	List<Object> attrList = new LinkedList<Object>();
    	List<CfgMetaData> funcList = new LinkedList<CfgMetaData>();
    	List<EventMetaData> eventList = new LinkedList<EventMetaData>();
    	if(devModel.equalsIgnoreCase(devModelName)){
			attrList.add(new CompanyName("蓝思科技", "厂商名称"));
			attrList.add(new ProductName("资产管理终端", "产品名称"));
			attrList.add(new ModelName(devModel,"型号名称"));
			attrList.add(new DevId(devId.toUpperCase(), "设备标识"));
			attrList.add(new DevType("资产监控终端", "设备类型"));
			
			funcList.add(new CfgMetaData("cfgmode", "enum", "配置模式", "0", "0:定位模式,1:震动模式", "RW"));
			funcList.add(new CfgMetaData("movethr", "int", "移动报警阈值", "1", "1,9", "RW"));
			funcList.add(new CfgMetaData("energyCycleValue", "int", "电量周期值", "120", "10,1440", "RW"));
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阔值", "20", "1,90", "RW"));
			
			eventList.add(new EventMetaData("1001", "设备移动"));
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1","设备上线"));
			eventList.add(new EventMetaData("0","设备下线"));
		}else if(devModel.equalsIgnoreCase(xtDevModelName)){
			attrList.add(new CompanyName("蓝思科技", "厂商名称"));
			attrList.add(new ProductName("工卡型资产定位终端", "产品名称"));
			attrList.add(new ModelName(devModel,"型号名称"));
			attrList.add(new DevId(devId.toUpperCase(), "设备标识"));
			attrList.add(new DevType("室内外资产监控终端", "设备类型"));
			
			funcList.add(new CfgMetaData("energyCycleValue", "int", "电量周期值", "120", "10,1440", "RW"));
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
			
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1","设备上线"));
			eventList.add(new EventMetaData("0","设备下线"));
		}
		List<Object> location = new LinkedList<Object>();
		location.add(new LocationListData("终端位置"));
		location.add(new DataMetaData("地图ID", "mapId", "string"));
		location.add(new DataMetaData("经度", "gpsLong", "string"));
		location.add(new DataMetaData("纬度", "gpsLat", "string"));
		
		List<Object> batterythr = new LinkedList<Object>();
		batterythr.add(new BatteryListData("电量值"));
		batterythr.add(new DataMetaData("电量", "battery", "string"));
		
		SupportDataList dataList = new SupportDataList(location,batterythr);
		
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		OnlineStatusData onlineStatus = new OnlineStatusData(datetime, status);
		
		RetWholeDevSchemaRspData data = new RetWholeDevSchemaRspData(attrList, funcList, eventList, dataList,onlineStatus);
		RetWholeDevSchemaRsp rsp = new RetWholeDevSchemaRsp("0", data);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
		return rsp;
	}
	
    public RetWholeDevSchemaRsp genRetWholeCowDevSchemaReq(String devid,String status,String devModel) {
    	List<Object> attrList = new LinkedList<Object>();
		List<CfgMetaData> funcList = new LinkedList<CfgMetaData>();
		List<EventMetaData> eventList = new LinkedList<EventMetaData>();
		List<Object> location = new LinkedList<Object>();
		List<Object> steps = new LinkedList<Object>();
		steps.add(new StepsListData("运动步数"));
		steps.add(new DataMetaData("步数", "step", "string"));
		
		List<Object> batterythr = new LinkedList<Object>();
		batterythr.add(new BatteryListData("电量值"));
		batterythr.add(new DataMetaData("电量", "battery", "string"));
		SupportDataList dataList = null;
		if(devModel.equalsIgnoreCase(cowDevModelName)){
			attrList.add(new CompanyName("蓝思科技", "厂商名称"));
			attrList.add(new ProductName("资产管理终端", "产品名称"));
			attrList.add(new ModelName(devModel,"型号名称"));
			attrList.add(new DevId(devid.toUpperCase(), "设备标识"));
			attrList.add(new DevType("资产监控终端", "设备类型"));
			
			funcList.add(new CfgMetaData("energyCycleValue", "int", "电量周期值", "120", "10,1440", "RW"));
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
			funcList.add(new CfgMetaData("stepsCycleValue", "int", "计步周期值", "10", "10,120", "RW"));
			
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1","设备上线"));
			eventList.add(new EventMetaData("0","设备下线"));
			
			location.add(new LocationListData("终端位置"));
			location.add(new DataMetaData("地图ID", "mapId", "string"));
			location.add(new DataMetaData("经度", "gpsLong", "string"));
			location.add(new DataMetaData("纬度", "gpsLat", "string"));
			
			dataList = new SupportDataList(location,steps,batterythr);
			
		}else if(devModel.equalsIgnoreCase(handDevModelName)){
			attrList.add(new CompanyName("蓝思科技", "厂商名称"));
			attrList.add(new ProductName("蓝牙手环", "产品名称"));
			attrList.add(new ModelName(devModel,"型号名称"));
			attrList.add(new DevId(devid.toUpperCase(), "设备标识"));
			attrList.add(new DevType("蓝牙手环监控设备", "设备类型"));
			
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
			
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1003","设备SOS告警"));
			
			location.add(new LocationListData("蓝牙手环位置"));
			location.add(new DataMetaData("地图ID", "mapId", "string"));
			location.add(new DataMetaData("经度", "gpsLong", "string"));
			location.add(new DataMetaData("纬度", "gpsLat", "string"));
			
			List<Object> calories = new LinkedList<Object>();
			calories.add(new CaloriesData("运动热量"));
			calories.add(new DataMetaData("卡洛里", "caloric", "string"));
			
			List<Object> heartBeatAndBloodPre = new LinkedList<Object>();
			heartBeatAndBloodPre.add(new HBandBloodPreData("心跳和血压值"));
			heartBeatAndBloodPre.add(new DataMetaData("心跳", "heartBeat", "string"));
			heartBeatAndBloodPre.add(new DataMetaData("收缩压", "systolicPressure", "string"));
			heartBeatAndBloodPre.add(new DataMetaData("舒张压", "diastolicPressure", "string"));
			
			List<Object> gwPosAndDistance = new LinkedList<Object>();
			gwPosAndDistance.add(new XYandDistanceData("网关坐标和距离值"));
			gwPosAndDistance.add(new DataMetaData("网关x坐标", "gatewayX", "string"));
			gwPosAndDistance.add(new DataMetaData("网关y坐标", "gatewayY", "string"));
			gwPosAndDistance.add(new DataMetaData("距离", "distance", "string"));
			
			dataList = new SupportDataList(location, steps, calories, batterythr, heartBeatAndBloodPre, gwPosAndDistance);
		}
		
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		OnlineStatusData onlineStatus = new OnlineStatusData(datetime, status);
		
		RetWholeDevSchemaRspData data = new RetWholeDevSchemaRspData(attrList, funcList, eventList, dataList,onlineStatus);
		RetWholeDevSchemaRsp rsp = new RetWholeDevSchemaRsp("0", data);
	    //String rep = JSON.toJSONString(rsp);
	    //System.out.println(rep);
		return rsp;
    }
	
    public RetrieveDataSchemaRsp genRetrieveDataSchemaRsp(){
    	List<Object> location = new LinkedList<Object>();
		location.add(new LocationListData("终端位置"));
		location.add(new DataMetaData("地图ID", "mapId", "string"));
		location.add(new DataMetaData("经度", "gpsLong", "string"));
		location.add(new DataMetaData("纬度", "gpsLat", "string"));
		
		List<Object> batterythr = new LinkedList<Object>();
		batterythr.add(new BatteryListData("电量值"));
		batterythr.add(new DataMetaData("电量", "battery", "string"));
		
		SupportDataList dataList = new SupportDataList(location,batterythr);
		RetrieveDataSchemaRspData rspdata = new RetrieveDataSchemaRspData(dataList);
		RetrieveDataSchemaRsp  rsp = new RetrieveDataSchemaRsp("0", rspdata);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
		return rsp;
	}
    
	public RetrieveDataSchemaRsp genRetrieveCowDevDataSchemaRsp(String devModel){
		List<Object> location = new LinkedList<Object>();
		List<Object> steps = new LinkedList<Object>();
		steps.add(new StepsListData("运动步数"));
		steps.add(new DataMetaData("步数", "step", "string"));
		List<Object> batterythr = new LinkedList<Object>();
		batterythr.add(new BatteryListData("电量值"));
		batterythr.add(new DataMetaData("电量", "battery", "string"));
		SupportDataList dataList = null;
		if(devModel.equalsIgnoreCase(cowDevModelName)){
			location.add(new LocationListData("终端位置"));
			location.add(new DataMetaData("地图ID", "mapId", "string"));
			location.add(new DataMetaData("经度", "gpsLong", "string"));
			location.add(new DataMetaData("纬度", "gpsLat", "string"));
			
			dataList = new SupportDataList(location,steps,batterythr);
		}else if(devModel.equalsIgnoreCase(handDevModelName)){
			location.add(new LocationListData("蓝牙手环位置"));
			location.add(new DataMetaData("地图ID", "mapId", "string"));
			location.add(new DataMetaData("经度", "gpsLong", "string"));
			location.add(new DataMetaData("纬度", "gpsLat", "string"));
			
			List<Object> calories = new LinkedList<Object>();
			calories.add(new CaloriesData("运动热量"));
			calories.add(new DataMetaData("卡洛里", "caloric", "string"));
			
			List<Object> heartBeatAndBloodPre = new LinkedList<Object>();
			heartBeatAndBloodPre.add(new HBandBloodPreData("心跳和血压值"));
			heartBeatAndBloodPre.add(new DataMetaData("心跳", "heartBeat", "string"));
			heartBeatAndBloodPre.add(new DataMetaData("收缩压", "systolicPressure", "string"));
			heartBeatAndBloodPre.add(new DataMetaData("舒张压", "diastolicPressure", "string"));
			
			List<Object> gwPosAndDistance = new LinkedList<Object>();
			gwPosAndDistance.add(new XYandDistanceData("网关坐标和距离值"));
			gwPosAndDistance.add(new DataMetaData("网关x坐标", "gatewayX", "string"));
			gwPosAndDistance.add(new DataMetaData("网关y坐标", "gatewayY", "string"));
			gwPosAndDistance.add(new DataMetaData("距离", "distance", "string"));
			
			dataList = new SupportDataList(location, steps, calories, batterythr, heartBeatAndBloodPre, gwPosAndDistance);
		}
		RetrieveDataSchemaRspData rspdata = new RetrieveDataSchemaRspData(dataList);
		RetrieveDataSchemaRsp  rsp = new RetrieveDataSchemaRsp("0", rspdata);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
		return rsp;
	}
	
	
	
	public RetFuncSchemaDataRsp genRetFuncSchemaDataRsp(String devModel){
		List<CfgMetaData> funcList = new LinkedList<CfgMetaData>();
		if(devModel.equalsIgnoreCase(devModelName)){
			funcList.add(new CfgMetaData("cfgmode", "enum", "配置模式", "0", "0:定位模式,1:震动模式", "RW"));
			funcList.add(new CfgMetaData("movethr", "int", "移动报警阈值", "1", "1,9", "RW"));
			funcList.add(new CfgMetaData("energyCycleValue", "int", "电量周期值", "120", "10,1440", "RW"));
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
		}else if(devModel.equalsIgnoreCase(xtDevModelName)){
			funcList.add(new CfgMetaData("energyCycleValue", "int", "电量周期值", "120", "10,1440", "RW"));
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
		}
		RetFuncSchemaDataRspData data = new RetFuncSchemaDataRspData(funcList);
		RetFuncSchemaDataRsp rsp = new RetFuncSchemaDataRsp("0", data);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);	
		return rsp;
	}
	
	public RetFuncSchemaDataRsp genRetCowDevFuncSchemaDataRsp(String devModel){
		List<CfgMetaData> funcList = new LinkedList<CfgMetaData>();
		if(devModel.equalsIgnoreCase(cowDevModelName)){
			funcList.add(new CfgMetaData("energyCycleValue", "int", "电量周期值", "120", "10,1440", "RW"));
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
			funcList.add(new CfgMetaData("stepsCycleValue", "int", "计步周期值", "10", "10,120", "RW"));
		}else if(devModel.equalsIgnoreCase(handDevModelName)){
			funcList.add(new CfgMetaData("lowbatterythr", "int", "低电量报警阈值", "20", "1,90", "RW"));
		}
		
		RetFuncSchemaDataRspData data = new RetFuncSchemaDataRspData(funcList);
		RetFuncSchemaDataRsp rsp = new RetFuncSchemaDataRsp("0", data);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
		return rsp;
	}
	
	public RetEventSchemaRsp genRetEventSchemaRsp(String devModel){
		List<EventMetaData> eventList = new LinkedList<EventMetaData>();
		if(devModel.equalsIgnoreCase(devModelName)){
			eventList.add(new EventMetaData("1001", "设备移动"));
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1","设备上线"));
			eventList.add(new EventMetaData("0","设备下线"));
		}else if(devModel.equalsIgnoreCase(xtDevModelName)){
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1","设备上线"));
			eventList.add(new EventMetaData("0","设备下线"));
		}
		RetEventSchemaRspData data = new RetEventSchemaRspData(eventList);
		RetEventSchemaRsp rsp = new RetEventSchemaRsp("0", data);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
		return rsp;
	}
	
	public RetEventSchemaRsp genRetCowDevEventSchemaRsp(String devModel){
		List<EventMetaData> eventList = new LinkedList<EventMetaData>();
		if(devModel.equalsIgnoreCase(cowDevModelName)){
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1","设备上线"));
			eventList.add(new EventMetaData("0","设备下线"));
		}else if(devModel.equalsIgnoreCase(handDevModelName)){
			eventList.add(new EventMetaData("1002","设备低电量告警"));
			eventList.add(new EventMetaData("1003","设备SOS告警"));
		}
		
		RetEventSchemaRspData data = new RetEventSchemaRspData(eventList);
		RetEventSchemaRsp rsp = new RetEventSchemaRsp("0", data);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
		return rsp;
	}
	
	
	public RetrieveNewestDataRsp genRetrieveNewestDataRsp(String mapid,String gpsLong,String gpsLat,String battery){
		
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		DevLocation localtion = new DevLocation(mapid,gpsLong,gpsLat);
		BatterythrData batterythr = new BatterythrData(battery);
		DevLocationAndBattery all = new DevLocationAndBattery(localtion,batterythr);
		RetrieveNewestDataRspData data = new  RetrieveNewestDataRspData(datetime,all);
		RetrieveNewestDataRsp rsp = new RetrieveNewestDataRsp("0", data);
//		String rep = JSON.toJSONString(rsp);
//		System.out.println(rep);
		return rsp;
	}
	
   public RetrieveNewestCowDevDataRsp genRetrieveNewestCowDevDataRsp(String gateway,String mapId,String gpsLong,String gpsLat,String step,String battery,
		                                                             String devModel,BlueToothBraceletData blueToothBraceletData){
	   Date now = new Date();
	   String datetime = Long.toString(now.getTime());
	   if(null == mapId){
		   mapId = "";
	   }
	   DevLocation location = new DevLocation(mapId, gpsLong, gpsLat);
	   step = QueryResult.cutStrAchieveNumber(step);
	   StepsData steps = null;
	   
	   BatterythrData batterythr = null;
	   LocationAndStepsAndBatteryData all = null;
	   if(devModel.equalsIgnoreCase(cowDevModelName)){
		   steps = new StepsData(step);
		   all = new LocationAndStepsAndBatteryData(location, steps,batterythr);
		   batterythr = new BatterythrData(battery);
	   }else if(devModel.equalsIgnoreCase(handDevModelName)){
		   String caloric = "";
		   String heartBeat = "";
		   String systolicPressure = "";
		   String diastolicPressure = "";
		   String gatewayX = "";
		   String gatewayY = "";
		   String distance = "";
		   DataBaseMgr db = DataBaseMgr.getInstance(); 
		   String sql = "select * from dev_list_tbl where deveui=\""+gateway+"\"";
		   ResultSet rs = db.executeQuery(sql);
		   try {
			   rs.beforeFirst();
			   if(rs.next()){
				  gatewayX = Float.toString(rs.getFloat("lastx"));
				  gatewayY = Float.toString(rs.getFloat("lasty"));
			   }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   if(null != blueToothBraceletData ){
		   caloric = blueToothBraceletData.getCaloric();
		   heartBeat = Short.toString(blueToothBraceletData.getHeartBeat());
		   systolicPressure = Short.toString(blueToothBraceletData.getSystolicPressure());
		   diastolicPressure = Short.toString(blueToothBraceletData.getDiastolicPressure());
		   distance = blueToothBraceletData.getDistance();
	   }
	   steps = new StepsData(step+"步");
	   batterythr = new BatterythrData(battery);
	   Calories calories = new Calories(caloric);
	   HeartBeatAndBloodPre heartBeatAndBloodPre = new HeartBeatAndBloodPre(heartBeat+"bpm", systolicPressure+"mmHg",diastolicPressure+"mmHg");
	   GWPosAndDistance gwPosAndDistance = new GWPosAndDistance(gatewayX, gatewayY,distance);
	   all = new LocationAndStepsAndBatteryData(location, steps, calories, batterythr, heartBeatAndBloodPre, gwPosAndDistance);
	   }
	   RetrieveNewestCowDevDataRspData data = new RetrieveNewestCowDevDataRspData(datetime, all);
	   RetrieveNewestCowDevDataRsp rsp = new RetrieveNewestCowDevDataRsp("0", data);
//	   String rep = JSON.toJSONString(rsp);
//	   System.out.println(rep);
	   return rsp;
	   
   }
	
	public RetNewFuncStatusRsp genRetNewFuncStatusRsp(String cfgtype,String movthr,String energyCycleValue,String lowbatterythr,String devModel){
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		List<QueryCfgData>  querydata = new LinkedList<QueryCfgData>();
		if(devModel.equalsIgnoreCase(devModelName)){
			querydata.add(new QueryCfgData(cfgtype,movthr,energyCycleValue,lowbatterythr));
		}else if(devModel.equalsIgnoreCase(xtDevModelName)){
			querydata.add(new QueryCfgData(energyCycleValue,lowbatterythr));
		}
		RetNewFuncStatusRspData data = new RetNewFuncStatusRspData(querydata);
		RetNewFuncStatusRsp rsp = new RetNewFuncStatusRsp("0",datetime,data);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
		return rsp;
	}
	
	public RetCowNewFuncStatusRsp genRetNewCowDevFuncStatusRsp(String energyCycleValue,String lowbatterythr,String stepsCycleValue,String devModel){
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		List<QueryCowCfgData>  querydata = new LinkedList<QueryCowCfgData>();
		if(devModel.equalsIgnoreCase(cowDevModelName)){
			querydata.add(new QueryCowCfgData(energyCycleValue,lowbatterythr,stepsCycleValue));
		}else if(devModel.equalsIgnoreCase(handDevModelName)){
			querydata.add(new QueryCowCfgData(lowbatterythr));
		}
		RetCowNewFuncStatusRspData data = new RetCowNewFuncStatusRspData(querydata);
		RetCowNewFuncStatusRsp rsp = new RetCowNewFuncStatusRsp("0",datetime,data);
//		String rep = JSON.toJSONString(rsp);
//		System.out.println(rep);
		return rsp;
	}
	
	public BatchRetNewDataRsp genBatchRetNewDataRsp(List<DevsData> devid){
		String gpsLong = null;
		String gpsLat = null;
		DevAndGpsData devdata = null;
		BatchRetNewDataRspData  rspdata = new BatchRetNewDataRspData();
		DevLocation  location = null;
		BatterythrData batterythr = null;
		List<GpsData> gpsData = null;
		DevLocationAndBattery all = null;
		String deviceId = null;
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		//格式化数据，获取小数点后六位
		DecimalFormat form=new DecimalFormat("0.000000");
		List<Zwwldevices> zwwlDevid = null;
		String mapid = null;
		String battery = null;
		try {
			for(DevsData devidata :devid){
				deviceId =devidata.getDevId(); 
				if(deviceId == null){
					logger.error("the devid is {} ",deviceId);
			    	continue;
				}
		        zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(deviceId);
		        if(zwwlDevid.size() == 0){
		    	 logger.error("the devid{} doesn't belong to ZWWL",deviceId);
		    	 continue;
		        }
		    
		    mapid = QueryResult.queryMapId(deviceId);
		    battery = QueryResult.queryBattery(deviceId);
		    gpsData = GpsDataDAO.getGpsDataByDevid(deviceId);
		    for(GpsData data : gpsData){
				 gpsLong = form.format(data.getLongitude());
				 gpsLat = form.format(data.getLatitude());
		    }
		    location = new DevLocation(mapid,gpsLong,gpsLat);
		    batterythr = new BatterythrData(battery);
		    all = new DevLocationAndBattery(location, batterythr);
			devdata = new DevAndGpsData(deviceId.toUpperCase(),datetime,all);
			rspdata.getDevs().add(devdata);
			}
		} catch (Exception e) {
			logger.error(" try/catch error{} in genBatchRetNewDataRsp ");
			e.printStackTrace();
		}
		BatchRetNewDataRsp rsp = new BatchRetNewDataRsp("0",rspdata);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
		return rsp;
	}
	
	public BatchRetNewCowDevDataRsp genBatchRetNewCowDevDataRsp(List<DevsData> devidList,String devModel){
		String gpsLong = null;
		String gpsLat = null;
		String battery = null;
		
		DevLocation  location=null;
		StepsData steps = null;
		BatterythrData batterythr = null;
		LocationAndStepsAndBatteryData devdata = null;
		BatchLocationAndSteps batchAll = null;
		BatchRetNewCowDevDataRspData  rspdata = new BatchRetNewCowDevDataRspData();
		
		List<GpsData> gpsData = null;
		String deviceId = null;
		Date now = new Date();
		String datetime = Long.toString(now.getTime());
		//格式化数据，获取小数点后六位
		DecimalFormat form=new DecimalFormat("0.000000");
		List<Zwwldevices> zwwlDevid = null;
		String step = "";
		String mapid = "";
		String gatewayX = "";
		String gatewayY = "";
		try {
			for(DevsData devidata : devidList){
				deviceId =devidata.getDevId(); 
				if(deviceId == null){
					logger.error("the devid is {} ",deviceId);
			    	continue;
				}
		        zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(deviceId);
		        if(zwwlDevid.size() == 0){
		    	 logger.error("the devid{} doesn't belong to ZWWL",deviceId);
		    	 continue;
		        }
		        //blueTooth bracelet
		        String deveui = null;
		        if(deviceId.length() == 12){
		        	deveui = zwwlDevid.get(0).getGateway();
		        }else{
		        	deveui = deviceId;
		        }
		        mapid = QueryResult.queryMapId(deveui);
			    if(devModel.equalsIgnoreCase(cowDevModelName)){
			    	battery = QueryResult.queryBattery(deviceId);
				    gpsData = GpsDataDAO.getGpsDataByDevid(deviceId);
				    for(GpsData data : gpsData){
						 gpsLong = form.format(data.getLongitude());
						 gpsLat = form.format(data.getLatitude());
				    }
				    step = QueryResult.querySteps(deviceId);
				    step = QueryResult.cutStrAchieveNumber(step);
					if(step.equalsIgnoreCase("0") || null == step){
						  logger.info("the Steps {} doesn't contain number or exist",step);
				    }
				    location = new DevLocation(mapid,gpsLong,gpsLat);
				    steps = new StepsData(step);
				    batterythr = new BatterythrData(battery);
			    	devdata = new LocationAndStepsAndBatteryData(location, steps,batterythr);
			    }else if(devModel.equalsIgnoreCase(handDevModelName)){
			    	BlueToothBraceletData blueToothBraceletData = BlueToothBraceletDataDAO.getBlueToothBraceletDataByDev(deviceId);
					String bgpsLong = "";
					String bgpsLat = "";
					String bstep = "";
					String caloric = "";
					String heartBeat = "";
					String systolicPressure = "";
					String diastolicPressure = "";
					String distance = "";
					String bbattery = "";
					DataBaseMgr db = DataBaseMgr.getInstance(); 
					String gateway = zwwlDevid.get(0).getGateway();
					String sql = "select * from dev_list_tbl where deveui=\""+gateway+"\"";
					ResultSet rs = db.executeQuery(sql);
					try {
						rs.beforeFirst();
						if(rs.next()){
						     gatewayX = Float.toString(rs.getFloat("lastx"));
							 gatewayY = Float.toString(rs.getFloat("lasty"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	if(null != blueToothBraceletData){
						bgpsLat = Float.toString(blueToothBraceletData.getGpsLat());
						bgpsLong = Float.toString(blueToothBraceletData.getGpsLong());
						bstep = blueToothBraceletData.getStep();
			    		caloric = blueToothBraceletData.getCaloric();
						heartBeat = Short.toString(blueToothBraceletData.getHeartBeat());
						systolicPressure = Short.toString(blueToothBraceletData.getSystolicPressure());
						diastolicPressure = Short.toString(blueToothBraceletData.getDiastolicPressure());
						distance = blueToothBraceletData.getDistance();
						bbattery = blueToothBraceletData.getBattery();
			    	}
			    	location = new DevLocation(mapid,bgpsLong,bgpsLat);
				    steps = new StepsData(bstep);
				    batterythr = new BatterythrData(bbattery);
			    	Calories calories = new Calories(caloric);
			    	HeartBeatAndBloodPre heartBeatAndBloodPre = new HeartBeatAndBloodPre(heartBeat+"bpm", systolicPressure+"mmHg", diastolicPressure+"mmHg");
			    	GWPosAndDistance gwPosAndDistance = new GWPosAndDistance(gatewayX, gatewayY, distance);
			    	devdata = new LocationAndStepsAndBatteryData(location, steps, calories, batterythr, heartBeatAndBloodPre, gwPosAndDistance);
			    }
				
				batchAll = new BatchLocationAndSteps(deviceId, datetime, devdata);
				rspdata.getDevs().add(batchAll);
			}
		} catch (Exception e1) {
			logger.error(" try/catch error{} in genBatchRetNewDataRsp ");
			e1.printStackTrace();
		}
		BatchRetNewCowDevDataRsp rsp = new BatchRetNewCowDevDataRsp("0", rspdata);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
	    return rsp;
		
	}
	
	public BatRetNewFuncStatusRsp genBatRetNewFuncStatusRsp(List<BatRetNewFuncStatusDevsData> devid,String deModel){
		String cfgtype = null ;
		String movethr = null ; 
		String energyCycleValue = null;
		String lowbatterythr = null;
		DevAndCfgData cfgData = null;
		
		BatRetNewFuncStatusRspData rspdata = new BatRetNewFuncStatusRspData();
		
		String deviceId = null;
		List<Zwwldevices> zwwlDevid = null;
		try{
		   for(BatRetNewFuncStatusDevsData devsData : devid ){
				deviceId = devsData.getDevId();
				if(deviceId == null){
					logger.error("the devid is {} ",deviceId);
			    	continue;
				}
				zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(deviceId);
				if(zwwlDevid.size() == 0){
			    	 logger.error("the devid {} doesn't belong to ZWWL",deviceId);
			    	 continue;
			     }
				 for(Zwwldevices zwwldevs : zwwlDevid ){
					 cfgtype = zwwldevs.getCfgtype();
					 movethr = Integer.toString(zwwldevs.getMovthr());
					 energyCycleValue = Integer.toString(zwwldevs.getEnergycyclevalue());
					 lowbatterythr = Integer.toString(zwwldevs.getLowbatterythr());
				 }
				
				 if(deModel.equalsIgnoreCase(devModelName)){
					 cfgData  = new DevAndCfgData(deviceId.toUpperCase(), cfgtype, movethr,energyCycleValue,lowbatterythr); 
				 }else if(deModel.equalsIgnoreCase(xtDevModelName)){
					 cfgData  = new DevAndCfgData(deviceId.toUpperCase(), energyCycleValue, lowbatterythr);
				 }
				 rspdata.getDevs().add(cfgData);
				 
		   }
		}catch (Exception e) {
			logger.error(" try/catch error {} in genBatRetNewFuncStatusRsp ");
			e.printStackTrace();
		}
		BatRetNewFuncStatusRsp rsp = new BatRetNewFuncStatusRsp("0", rspdata);
		//String rep = JSON.toJSONString(rsp);
		//System.out.println(rep);
		return rsp;
		
	}
	
	public BatRetCowNewFuncStatusRsp genBatRetCowNewFuncStatusRsp(List<BatRetNewFuncStatusDevsData> cowDevid,String devModel){
		String energyCycleValue = null;
		String lowbatterythr = null;
		String stepsCycleValue = null;
		CowDevAndCfgData cowDev = null;
		BatRetCowNewFuncStatusRspData  rspdata = new BatRetCowNewFuncStatusRspData();
		String deviceId = null;
		List<Zwwldevices> zwwlDevid = null;
		try {
			for(BatRetNewFuncStatusDevsData batRetNew : cowDevid){
				deviceId = batRetNew.getDevId();
				zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(deviceId);
				if((null == zwwlDevid) || (zwwlDevid.size() == 0)){
					 logger.error("the devid {} doesn't belong to ZWWL",deviceId);
			    	 continue;
				}
				for(Zwwldevices zwwldevs : zwwlDevid){
					energyCycleValue = Integer.toString(zwwldevs.getEnergycyclevalue());
					lowbatterythr = Integer.toString(zwwldevs.getLowbatterythr());
				    stepsCycleValue = Integer.toString(zwwldevs.getStepscyclevalue());
				}
				
			   if(devModel.equalsIgnoreCase(cowDevModelName)){
				   cowDev = new CowDevAndCfgData(deviceId, energyCycleValue, lowbatterythr,stepsCycleValue);
			   }else if(devModel.equalsIgnoreCase(handDevModelName)){
				   cowDev = new CowDevAndCfgData(deviceId, lowbatterythr);
			   }
		     
		       rspdata.getDevs().add(cowDev);
				 
			}
		} catch (Exception e) {
			logger.info("try/catch error in BatRetCowNewFuncStatusRsp");
			e.printStackTrace();
		}
		BatRetCowNewFuncStatusRsp rsp = new BatRetCowNewFuncStatusRsp("0", rspdata);
		return rsp;
	}
	
	public ReqProcRsltType handleAddDeviceReq(AddDeviceReq addDev, ReqProcRslt rsp) {
		String devid = addDev.getDevId();
		String devType = addDev.getDevType();
		String username = null;
		int energycyclevalue = 120;//battery周期固定值
		int lowbatterythr = 20;//最低battery报警值
		int stepsCycleValue = 10;//计步周期固定值
		String status = "1"; //判断设备是否在线，1在线，0离线
		List<ZwwlDevsUseInfo> zwwlUser = null;
		DevicesOperateBean devperate = DevicesOperateBean.getInstance();
		try {
			logger.info("deveui id: {}", devid);
			if(devid.length() != 12){
				zwwlUser = ZwwlDevsUseInfoDAO.getZwwlUsernameByDevid(devid);
				if(null == zwwlUser){
					logger.info("Fail to query zwwlUser {} by devid {}",zwwlUser,devid);
					return ReqProcRsltType.NOEXIST_NO_RESP;
				}
				for(ZwwlDevsUseInfo usrdev : zwwlUser){
					username = usrdev.getUsername();
					logger.info("username {} of this devid {} ",username,devid);
				}
			}else{
				username = "xt";
			}
			
			//所有中物物联的用户名都以zwwl开头
			if(null == username || ((!username.contains("zwwl") && !username.contains("qinzhou")) && ((!username.contains("xt")) || (!username.equalsIgnoreCase("xt"))))){
				logger.error("this devid {} doesn't belong to ZWWL or XT",devid);
				return ReqProcRsltType.NOEXIST_NO_RESP;
			}else{
				if((devType.equalsIgnoreCase(devModelName)) || (devType.equalsIgnoreCase(xtDevModelName))){
				    List<Zwwldevices> query = ZwwldevicesDAO.getZwwldeviceByDevid(devid);
				    if ((query == null) || (query.size() == 0)) {
						DevicesOperateBean operBean = DevicesOperateBean.getInstance();					
	                    //添加设备时，直接查询离线设备表判断设备是否在线			    	
				        if(operBean.scanTimeOutByDev(devid)){
					          status = "0";
				        }				      
				        String gatewaysn = QueryResult.repGatewaysn(devid.toUpperCase());
				        if(devType.equalsIgnoreCase(devModelName)){
				        	ZwwldevicesDAO.create(new Zwwldevices(devid, CFGMODE_POSITION, 1,status,energycyclevalue,lowbatterythr,stepsCycleValue,gatewaysn,ZwwlAuth.nowTenanId,devType));	
				        }else if(devType.equalsIgnoreCase(xtDevModelName)){
				        	ZwwldevicesDAO.create(new Zwwldevices(devid, CFGMODE_POSITION, 1,status,energycyclevalue,lowbatterythr,stepsCycleValue,"",ZwwlAuth.nowTenanId,devType));	
				        }
				    }else{
					   logger.error("this devid {} exists in Zwwldevices",devid);
					   return ReqProcRsltType.COPYADD_NO_RESP;
				    }
				    if(devType.equalsIgnoreCase(devModelName)){
				    	rsp.setResp(genAddDeviceRsp(devid,status,devModelName));
				    }else if(devType.equalsIgnoreCase(xtDevModelName)){
				    	rsp.setResp(genAddDeviceRsp(devid,status,xtDevModelName));
				    }
				  
				  //判断type是定位终端还是计步终端
			    }else if((devType.equalsIgnoreCase(cowDevModelName)) || (devType.equalsIgnoreCase(handDevModelName))){
			    	List<Zwwldevices> query = ZwwldevicesDAO.getZwwldeviceByDevid(devid);
				    if ((query == null) || (query.size() == 0)) {
		               if(devType.equalsIgnoreCase(cowDevModelName)){
		            	   List<DevConfig> devConfig = DevConfigDAO.getDevConfigByDevice(devid);
		            	   DevicesOperateBean operBean = DevicesOperateBean.getInstance();					
		                    //添加设备时，直接查询离线设备表判断设备是否在线			    	
					        if(operBean.scanTimeOutByDev(devid)){
						          status = "0";
					        }				      
			                if((devConfig == null) || (devConfig.size() == 0)){
			                	  logger.info("the devid doesn't exists in dev_config");
			                	  return ReqProcRsltType.ERROR_RESP_OBJ;
			                }
			               String gatewaysn = QueryResult.repGatewaysn(devid.toUpperCase());
		            	   ZwwldevicesDAO.create(new Zwwldevices(devid, "0", 0,status,energycyclevalue,lowbatterythr,stepsCycleValue,gatewaysn,ZwwlAuth.nowTenanId,devType));
		            	   
		            	   //富牛计步
		            	   DevConfig updateConfig = devConfig.get(0);
						   updateConfig.setHB((short)(stepsCycleValue*2));
						   DevConfigDAO.update(updateConfig);
						   //update devid status
						   devperate.updateOpr(devid, DevOpr.LOCATE);
		               }else if(devType.equalsIgnoreCase(handDevModelName)){
		            	   ZwwldevicesDAO.create(new Zwwldevices(devid, "0", 0,status,energycyclevalue,lowbatterythr,stepsCycleValue,"",ZwwlAuth.nowTenanId,devType));
		               }
					   
			        }else{
						 logger.error("this devid {} exists in Zwwldevices",devid);
						 return ReqProcRsltType.COPYADD_NO_RESP;
					 }
				    if(devType.equalsIgnoreCase(cowDevModelName)){
				    	rsp.setResp(genAddCowDeviceRsp(devid,status,devType));
				    }else if(devType.equalsIgnoreCase(handDevModelName)){
				    	rsp.setResp(genAddCowDeviceRsp(devid,status,devType));
				    }
			        
		      }else{
		    	  logger.info("the devid's {} devType {} isn't conform!",devid,devType);
		    	  return ReqProcRsltType.NOEXIST_NO_RESP;
		      }
		  }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("try/catch error in handleAddDeviceReq");
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}
		
		
		return ReqProcRsltType.SUCC_RESP_OBJ;
	}
	
	public ReqProcRsltType handleDelDeviceReq(DelDeviceReq delDev, ReqProcRslt rsp) {
		String devid = delDev.getDevId();
		try {
			List<Zwwldevices> query = ZwwldevicesDAO.getZwwldeviceByDevid(devid);
			if ((query != null) && (query.size() != 0)) {
				ZwwldevicesDAO.delete(query);
			} else {
				logger.error("Deleting devid {} doesn't exist in zwwldevice_tbl ",devid);
				return ReqProcRsltType.NOTADD_NO_RESP;
			}
		} catch (Exception e) {
			logger.error("try/catch error in handleDelDeviceReq");
			e.printStackTrace();
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}
		
		return ReqProcRsltType.SUCC_NO_RESP;
	}
	
	public ReqProcRsltType handleCfgDeviceReq(CfgDeviceReq cfgDev, ReqProcRslt rsp) {
		String devid = cfgDev.getDevId();
		String type = cfgDev.getControl().getCfgmode();
		String movthr = cfgDev.getControl().getMovethr();
		String energyCycleValue = cfgDev.getControl().getEnergyCycleValue();
		String lowbatterythr = cfgDev.getControl().getLowbatterythr();
		String stepsCycleValue = cfgDev.getControl().getStepsCycleValue();
		String devType = null;
		List<Zwwldevices> query = null;
		DevicesOperateBean devperate = DevicesOperateBean.getInstance();
		
		try {
			query = ZwwldevicesDAO.getZwwldeviceByDevid(devid);
			if ((null == query) || (query.size() == 0)) {
				logger.error("the devid {} doesn't exist in ZwwlDevice",devid);
			    return ReqProcRsltType.NOTADD_NO_RESP;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}
		
		devType = query.get(0).getDevType();
		if((null == devType) || (devType.equals(""))){
			logger.info("the devid {} doesn't have the devType {}",devid,devType);
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}else if((devType.equalsIgnoreCase(devModelName)) || (devType.equalsIgnoreCase(xtDevModelName))){
			Zwwldevices dev = query.get(0);
			//定位终端若配置值存在位null,则查询数据库的值赋值给此变量
			if(null == type){
				type = dev.getCfgtype();
			}
			if(null == movthr){	
				movthr = Integer.toString(dev.getMovthr());
			}
			if(null == energyCycleValue){
				energyCycleValue = Integer.toString(dev.getEnergycyclevalue());
			}
			if(null == lowbatterythr){
				lowbatterythr = Integer.toString(dev.getLowbatterythr());
			}
			if((!type.equals("0")) && (!type.equals("1"))){
				logger.error("zwwl Cfgmode {} doesn't meet the requirements ",type);
				return ReqProcRsltType.FAIL_NO_RESP;
			}
			if((Integer.parseInt(movthr) > 9) || (Integer.parseInt(movthr) < 1) ){
				logger.error("zwwl movthr {} doesn't meet the requirements ",movthr);
				//ZwwlSender.sendFuncData(devid,movthr);
				return ReqProcRsltType.FAIL_NO_RESP;
			}
			if((Integer.parseInt(energyCycleValue) < 10) || (Integer.parseInt(energyCycleValue) > 1440)
				|| (Integer.parseInt(lowbatterythr) < 1) ||(Integer.parseInt(lowbatterythr) > 90)){
				 logger.error("zwwl CfgDevidHeartBeat {} or CfgDevidLowBatterythr {} doesn't meet the requirements ",energyCycleValue,lowbatterythr);
				 return ReqProcRsltType.FAIL_NO_RESP;
			}
			dev.setCfgtype(type);
			dev.setMovthr(Integer.parseInt(movthr));
			dev.setEnergycyclevalue(Integer.parseInt(energyCycleValue));
			dev.setLowbatterythr(Integer.parseInt(lowbatterythr));
			logger.info("zwwl CfgDevInfo type {} movthr {} energyCycleValue {} lowbatterythr {}",type,movthr,energyCycleValue,lowbatterythr);
			try {
				ZwwldevicesDAO.update(dev);
			} catch (Exception e) {
				e.printStackTrace();
				return ReqProcRsltType.ERROR_RESP_OBJ;
			}
			
	    }else if((devType.equalsIgnoreCase(cowDevModelName)) || (devType.equalsIgnoreCase(handDevModelName))){
	    	List<DevConfig> devConfig = null;
	    	if((null == type)||(null == movthr)||(null !=type) || (null != movthr)){
	    		type = "0";
	    		movthr = "0";
			}
	    	
	    	try {
		    	Zwwldevices dev = query.get(0);
		    	//计步终端若配置值存在位null,则查询数据库的值赋值给此变量
	    		if(null == energyCycleValue){
	    			energyCycleValue = Integer.toString(dev.getEnergycyclevalue());
				}
				if(null == lowbatterythr){
					lowbatterythr = Integer.toString(dev.getLowbatterythr());
				}
				if(null == stepsCycleValue){
					stepsCycleValue = Integer.toString(dev.getStepscyclevalue());
				}
				if((Integer.parseInt(energyCycleValue) < 10) || (Integer.parseInt(energyCycleValue) > 1440)
						|| (Integer.parseInt(lowbatterythr) < 1) ||(Integer.parseInt(lowbatterythr) > 90) 
						|| (Integer.parseInt(stepsCycleValue) < 10) || (Integer.parseInt(stepsCycleValue) > 120)){
						logger.error("zwwl CfgDevidHeartBeat {} or CfgDevidLowBatterythr {} or CfgDevidStepsCycleValue {} doesn't meet the requirements ",energyCycleValue,lowbatterythr);
						return ReqProcRsltType.FAIL_NO_RESP;
				  }
				dev.setCfgtype(type);
				dev.setMovthr(Integer.parseInt(movthr));
				dev.setEnergycyclevalue(Integer.parseInt(energyCycleValue));
				dev.setLowbatterythr(Integer.parseInt(lowbatterythr));
				dev.setStepscyclevalue(Integer.parseInt(stepsCycleValue));
				logger.info("zwwl CfgCowDevInfo type {} movthr {} energyCycleValue {} lowbatterythr {} stepsCycleValue {}",type,movthr,energyCycleValue,lowbatterythr,stepsCycleValue);
				
				//富牛设备更新上报计步周期
				if(devType.equalsIgnoreCase(cowDevModelName)){
					devperate.pushZwwlCfgDeviceHeartBeat(devid, (Integer.parseInt(stepsCycleValue)*2));
					devConfig = DevConfigDAO.getDevConfigByDevice(devid);
					if((null == devConfig) || (devConfig.isEmpty())){
		    			logger.error("Fail to query CfgDevInfo of this devid",devid);
						return ReqProcRsltType.ERROR_RESP_OBJ;
		    		}
					DevConfig devCfg = devConfig.get(0);
					devCfg.setHB((short)(Integer.parseInt(stepsCycleValue)*2));
				    DevConfigDAO.update(devCfg);
				}
				
				ZwwldevicesDAO.update(dev);
				} catch (Exception e) {
					e.printStackTrace();
					return ReqProcRsltType.ERROR_RESP_OBJ;
				}
	    }
		return ReqProcRsltType.SUCC_NO_RESP;
	}
    
	
	
	public ReqProcRsltType handleRetWholeDevSchemaReq(RetWholeDevSchemaReq retDevSchema, ReqProcRslt rsp){
		String devId = retDevSchema.getDevId();
		String devType = null;//判断终端类型
		String status = "1"; //判断设备是否在线，1在线，0离线
		List<Zwwldevices> zwwlDevid = null;
		 try {
			zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(devId);
			if((null == zwwlDevid) || (zwwlDevid.size() == 0)){
				logger.error("the devid {} does't in ZwwlDevice",devId);
				return ReqProcRsltType.NOTADD_NO_RESP;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}
		 DevicesOperateBean operBean = DevicesOperateBean.getInstance();					
         //添加设备时，直接查询离线设备表判断设备是否在线			    	
	     if(operBean.scanTimeOutByDev(devId)){
		     status = "0";
	     }
	     devType = zwwlDevid.get(0).getDevType();
		 if((null == devType) || (devType.equals(""))){
			 logger.info("the devid {} doesn't have the devType {}",devId,devType);
			return ReqProcRsltType.ERROR_RESP_OBJ;
		 }else if(devType.equalsIgnoreCase(devModelName)){
	    	rsp.setResp(genRetWholeDevSchemaReq(devId,status,devType));
	     }else if(devType.equalsIgnoreCase(xtDevModelName)){
	    	rsp.setResp(genRetWholeDevSchemaReq(devId,status,devType));
	     }else if(devType.equalsIgnoreCase(cowDevModelName)){
	    	rsp.setResp(genRetWholeCowDevSchemaReq(devId,status,devType));
	     }else if(devType.equalsIgnoreCase(handDevModelName)){
		    rsp.setResp(genRetWholeCowDevSchemaReq(devId,status,devType));
		 }
		
		return ReqProcRsltType.SUCC_RESP_OBJ;
	}
	
	public ReqProcRsltType handleRetrieveDataSchemaReq(RetrieveDataSchemaReq retDataSchema,ReqProcRslt rsp){
		String devId = retDataSchema.getDevId();
		String devType = null;
		List<Zwwldevices> zwwlDevid = null;
		 try {
			zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(devId);
			if((null == zwwlDevid) || (zwwlDevid.size() == 0)){
				logger.error("the devid {} does't in ZwwlDevice",devId);
				return ReqProcRsltType.NOTADD_NO_RESP;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}
		 
		devType = zwwlDevid.get(0).getDevType();
		if((null == devType) || (devType.equals(""))){
			logger.info("the devid {} doesn't have the devType {}",devId,devType);
		    return ReqProcRsltType.ERROR_RESP_OBJ;
		}else if((devType.equalsIgnoreCase(devModelName)) || (devType.equalsIgnoreCase(xtDevModelName))){
		    rsp.setResp(genRetrieveDataSchemaRsp());
	    }else if(devType.equalsIgnoreCase(cowDevModelName)){
	        rsp.setResp(genRetrieveCowDevDataSchemaRsp(devType));
	    }else if(devType.equalsIgnoreCase(handDevModelName)){
	        rsp.setResp(genRetrieveCowDevDataSchemaRsp(devType));
	    }
		return ReqProcRsltType.SUCC_RESP_OBJ;
	}
	
	 public ReqProcRsltType handleRetFuncSchemaDataReq(RetFuncSchemaDataReq retFunSchema,ReqProcRslt rsp){
		 String devId = retFunSchema.getDevId();
		 String devType = null;
		 List<Zwwldevices> zwwlDevid = null;
		 try {
			 zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(devId);
			if((null == zwwlDevid) ||(zwwlDevid.size() == 0)){
				logger.error("the devid {} does't in ZwwlDevice",devId);
				return ReqProcRsltType.NOTADD_NO_RESP;
			}
		 } catch (Exception e) {
			e.printStackTrace();
			return ReqProcRsltType.ERROR_RESP_OBJ;
		 }
		 
		 devType = zwwlDevid.get(0).getDevType();
		 if((null == devType) || (devType.equals(""))){
			 logger.info("the devid {} doesn't have the devType {}",devId,devType);
			 return ReqProcRsltType.ERROR_RESP_OBJ;
		 }else if(devType.equalsIgnoreCase(devModelName)){
		     rsp.setResp(genRetFuncSchemaDataRsp(devModelName));
	     }else if(devType.equalsIgnoreCase(xtDevModelName)){
		     rsp.setResp(genRetFuncSchemaDataRsp(xtDevModelName));
	     }else if(devType.equalsIgnoreCase(cowDevModelName)){
	    	 rsp.setResp(genRetCowDevFuncSchemaDataRsp(cowDevModelName));
	     }else if(devType.equalsIgnoreCase(handDevModelName)){
	    	 rsp.setResp(genRetCowDevFuncSchemaDataRsp(handDevModelName));
	     }
		 return ReqProcRsltType.SUCC_RESP_OBJ;
	 }
	
	public ReqProcRsltType handleRetEventSchemaReq(RetEventSchemaReq retEventSchema,ReqProcRslt rsp){
		String devId = retEventSchema.getDevId();
		String devType = null;
		List<Zwwldevices> zwwlDevid = null;
		try {
		   zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(devId);
		   if((null == zwwlDevid) || (zwwlDevid.size() == 0)){
			   logger.error("the devid {} does't in ZwwlDevice",devId);
			  return ReqProcRsltType.NOTADD_NO_RESP;
		   }
		 } catch (Exception e) {
			e.printStackTrace();
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}
		devType = zwwlDevid.get(0).getDevType();
		if((null == devType) || (devType.equals(""))){
			logger.info("the devid {} doesn't have the devType {}",devId,devType);
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}else if(devType.equalsIgnoreCase(devModelName)){
		    rsp.setResp(genRetEventSchemaRsp(devModelName));
	    }else if(devType.equalsIgnoreCase(xtDevModelName)){
		    rsp.setResp(genRetEventSchemaRsp(xtDevModelName));
	    }else if(devType.equalsIgnoreCase(cowDevModelName)){
	    	rsp.setResp(genRetCowDevEventSchemaRsp(cowDevModelName));
	    }else if(devType.equalsIgnoreCase(handDevModelName)){
	    	rsp.setResp(genRetCowDevEventSchemaRsp(handDevModelName));
	    }
	    return ReqProcRsltType.SUCC_RESP_OBJ;
	}
	
	public ReqProcRsltType handleRetrieveNewestDataReq(RetrieveNewestDataReq retNewDev, ReqProcRslt rsp){
		String devid = retNewDev.getDevId();
		String devType = null;
		String steps = null;
		String gpsLong = null;
		String gpsLat = null;
		String battery = null;
		List<Zwwldevices> zwwlDevid = null;
		String mapid = null;
		 try {
			zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(devid);
			if((null == zwwlDevid) || (zwwlDevid.size() == 0)){
				logger.error("the devid {} does't in ZwwlDevice",devid);
				return ReqProcRsltType.NOTADD_NO_RESP;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}
		devType = zwwlDevid.get(0).getDevType();
		List<GpsData> gpsData = null;
		//blueTooth Bracelet
		String deveui = null;
		if(devid.length() == 12){
			deveui = zwwlDevid.get(0).getGateway();
		}else{
			deveui = devid;
		}
		mapid = QueryResult.queryMapId(deveui);
		logger.info("mapid {} of the device {}",mapid,deveui);
		//格式化数据，获取小数点后六位
		if(!devType.equalsIgnoreCase(handDevModelName)){
			DecimalFormat form=new DecimalFormat("0.000000");  
			try {
				gpsData = GpsDataDAO.getGpsDataByDevid(devid);
				for(GpsData data : gpsData){
					gpsLong = form.format(data.getLongitude());
					gpsLat = form.format(data.getLatitude());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if((null == devType) || (devType.equals(""))){
			logger.info("the devid {} doesn't have the devType {}",devid,devType);
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}else if((devType.equalsIgnoreCase(devModelName)) || (devType.equalsIgnoreCase(xtDevModelName))){
			battery = QueryResult.queryBattery(devid);
			rsp.setResp(genRetrieveNewestDataRsp(mapid,gpsLong,gpsLat,battery));
		}else if(devType.equalsIgnoreCase(cowDevModelName)){
			steps = QueryResult.querySteps(devid);
			if(null == steps){
				logger.info("the deveui does't have the steps {} !",devid,steps);
				steps = "";
			}
			steps = QueryResult.cutStrAchieveNumber(steps);
			battery = QueryResult.queryBattery(devid);
			if(steps.equalsIgnoreCase("0")){
				  logger.info("the Steps {} doesn't contain number",steps);
		    }
			
			rsp.setResp(genRetrieveNewestCowDevDataRsp("",mapid,gpsLong,gpsLat,steps,battery,cowDevModelName,null));
			
		}else if(devType.equalsIgnoreCase(handDevModelName)){
			String bgpsLong = "";
			String bgpsLat = "";
			String bsteps = "";
			String bbattery = "";
			BlueToothBraceletData blueToothBraceletData = null;
			try {
				blueToothBraceletData = BlueToothBraceletDataDAO.getBlueToothBraceletDataByDev(devid);
				if(null != blueToothBraceletData){
					bgpsLat = Float.toString(blueToothBraceletData.getGpsLat());
					bgpsLong = Float.toString(blueToothBraceletData.getGpsLong());
					bsteps = blueToothBraceletData.getStep();
					bbattery = blueToothBraceletData.getBattery();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String gateway = zwwlDevid.get(0).getGateway();
			rsp.setResp(genRetrieveNewestCowDevDataRsp(gateway,mapid,bgpsLong,bgpsLat,bsteps,bbattery,handDevModelName,blueToothBraceletData));
		
		}
		
//		String rep = JSON.toJSONString(rsp);
//		System.out.println(rep);
		return ReqProcRsltType.SUCC_RESP_OBJ;
	}
	
	public ReqProcRsltType handleRetNewFuncStatusReq(RetNewFuncStatusReq retNewFuncStatus,ReqProcRslt rsp){
		String devId = retNewFuncStatus.getDevId();
		String devType = null;
		String cfgtype = null;
		String  movthr = null; 
		String energyCycleValue = null;
	    String lowbatterythr = null;
	    String stepsCycleValue = null;
		List<Zwwldevices> zwwlDevid = null;
		 try {
			zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(devId);
			if((null == zwwlDevid) || (zwwlDevid.size() == 0)){
				logger.error("the devid {} does't in ZwwlDevice",devId);
				return ReqProcRsltType.NOTADD_NO_RESP;
			}
			for(Zwwldevices zwwldev : zwwlDevid){
	            cfgtype = zwwldev.getCfgtype();
	            movthr = Integer.toString(zwwldev.getMovthr());
	            energyCycleValue = Integer.toString(zwwldev.getEnergycyclevalue());
	            lowbatterythr = Integer.toString(zwwldev.getLowbatterythr());
	            stepsCycleValue = Integer.toString(zwwldev.getStepscyclevalue());
	        }
		} catch (Exception e1) {
			e1.printStackTrace();
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}
		devType = zwwlDevid.get(0).getDevType();
		if((null == devType) || (devType.equals(""))){
			logger.info("the devid {} doesn't have the devType {}",devId,devType);
			return ReqProcRsltType.ERROR_RESP_OBJ;
		}else if(devType.equalsIgnoreCase(devModelName)){
			rsp.setResp(genRetNewFuncStatusRsp(cfgtype,movthr,energyCycleValue,lowbatterythr,devModelName));
		}else if(devType.equalsIgnoreCase(xtDevModelName)){
			rsp.setResp(genRetNewFuncStatusRsp(cfgtype,movthr,energyCycleValue,lowbatterythr,xtDevModelName));
		}else if(devType.equalsIgnoreCase(cowDevModelName)){
		    rsp.setResp(genRetNewCowDevFuncStatusRsp(energyCycleValue,lowbatterythr,stepsCycleValue,cowDevModelName));
		}else if(devType.equalsIgnoreCase(handDevModelName)){
			 rsp.setResp(genRetNewCowDevFuncStatusRsp(energyCycleValue,lowbatterythr,stepsCycleValue,handDevModelName));
		}
		return ReqProcRsltType.SUCC_RESP_OBJ;
	}
	//only same devType's deveui batch querys.
	public ReqProcRsltType handleBatchRetNewDataDevidsReq(BatchRetNewDataDevidsReq batRetNewDev, ReqProcRslt rsp){
		List<DevsData> devid = batRetNewDev.getDevs();
		List<DevsData> cowDevid = new LinkedList<DevsData>();
		String device = null;
		String devType = null;
		List<Zwwldevices> zwwlDev = null;
		for(DevsData devData : devid){
			device = devData.getDevId();
			try {
				zwwlDev = ZwwldevicesDAO.getZwwldeviceByDevid(device);
				if((null == zwwlDev) || (zwwlDev.size() == 0)){
					logger.info("the deveui {} doesn't exist in zwwlDeveui!",device);
					continue;
				}
				devType = zwwlDev.get(0).getDevType();
				if((null == devType) || (devType.equals(""))){
					logger.info("the devid {} doesn't have the devType {}",device,devType);
					return ReqProcRsltType.ERROR_RESP_OBJ;
				} else if(devType.equalsIgnoreCase(cowDevModelName)){
					DevsData devsData = new DevsData();
					devsData.setDevId(device);
					devid.remove(device);
					cowDevid.add(devsData);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if((devType.equalsIgnoreCase(devModelName)) || (devType.equalsIgnoreCase(xtDevModelName))){
			rsp.setResp(genBatchRetNewDataRsp(devid));
		}else if(devType.equalsIgnoreCase(cowDevModelName)){
			rsp.setResp(genBatchRetNewCowDevDataRsp(cowDevid,cowDevModelName));
		}else if(devType.equalsIgnoreCase(handDevModelName)){
			rsp.setResp(genBatchRetNewCowDevDataRsp(devid,handDevModelName));
		}
		
		return ReqProcRsltType.SUCC_RESP_OBJ;
	}
	
	public ReqProcRsltType handleBatRetNewFuncStatusReq(BatRetNewFuncStatusReq batRetNewFuncStatus,ReqProcRslt rsp){
		List<BatRetNewFuncStatusDevsData> devid = batRetNewFuncStatus.getDevs();
		List<BatRetNewFuncStatusDevsData> cowDevid = new LinkedList<BatRetNewFuncStatusDevsData>();
		String deveui = null;
		String devType = null;
		List<Zwwldevices> zwwlDevid = null;
		for(BatRetNewFuncStatusDevsData batRetNewFunDev : devid){
			deveui = batRetNewFunDev.getDevId();
			try {
				zwwlDevid = ZwwldevicesDAO.getZwwldeviceByDevid(deveui);
				if((null == zwwlDevid) || (zwwlDevid.size() == 0)){
					logger.info("the deveui {} doesn't exist in zwwlDeveui!",deveui);
					continue;
				}
				devType = zwwlDevid.get(0).getDevType();
				if((null == devType) || (devType.equals(""))){
					logger.info("the devid {} doesn't have the devType {}",deveui,devType);
					return ReqProcRsltType.ERROR_RESP_OBJ;
				} else if(devType.equalsIgnoreCase(cowDevModelName)){
					BatRetNewFuncStatusDevsData batRetNewFunDevData = new BatRetNewFuncStatusDevsData();
					batRetNewFunDevData.setDevId(deveui);
					devid.remove(deveui);
					cowDevid.add(batRetNewFunDevData);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if(devType.equalsIgnoreCase(devModelName)){
			rsp.setResp(genBatRetNewFuncStatusRsp(devid,devModelName));
		}else if(devType.equalsIgnoreCase(xtDevModelName)){
			rsp.setResp(genBatRetNewFuncStatusRsp(devid,xtDevModelName));
		}else if(devType.equalsIgnoreCase(cowDevModelName)){
			rsp.setResp(genBatRetCowNewFuncStatusRsp(cowDevid,cowDevModelName));
		}else if(devType.equalsIgnoreCase(handDevModelName)){
			rsp.setResp(genBatRetCowNewFuncStatusRsp(devid,handDevModelName));
		}
		
		return ReqProcRsltType.SUCC_RESP_OBJ;
	}
	
	@Override
	public ReqProcRsltType handleRequest(Object req, ReqProcRslt rsp) {
		
		if (req instanceof AddDeviceReq) {
			return handleAddDeviceReq((AddDeviceReq)req, rsp);
		} else if (req instanceof CfgDeviceReq) {
			return handleCfgDeviceReq((CfgDeviceReq)req, rsp);
		} else if (req instanceof DelDeviceReq) {
			return handleDelDeviceReq((DelDeviceReq)req, rsp);
		}else if(req instanceof RetWholeDevSchemaReq){
			return handleRetWholeDevSchemaReq((RetWholeDevSchemaReq)req,rsp);
		}else if(req instanceof RetrieveDataSchemaReq){
			return handleRetrieveDataSchemaReq((RetrieveDataSchemaReq)req,rsp);
		}else if(req instanceof RetFuncSchemaDataReq){
			return handleRetFuncSchemaDataReq((RetFuncSchemaDataReq)req,rsp);
		}else if(req instanceof RetEventSchemaReq){
			return handleRetEventSchemaReq((RetEventSchemaReq)req,rsp);
		}else if(req instanceof RetrieveNewestDataReq){
			return handleRetrieveNewestDataReq((RetrieveNewestDataReq)req, rsp);
		}else if(req instanceof RetNewFuncStatusReq){
			return handleRetNewFuncStatusReq((RetNewFuncStatusReq)req,rsp);
		}else if(req instanceof BatchRetNewDataDevidsReq){
			return handleBatchRetNewDataDevidsReq((BatchRetNewDataDevidsReq)req, rsp);
		}else if(req instanceof BatRetNewFuncStatusReq){
			return handleBatRetNewFuncStatusReq((BatRetNewFuncStatusReq)req,rsp);
		}else {
			return ReqProcRsltType.FAIL_NO_RESP;
		}
	}
}
