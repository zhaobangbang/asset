package com.lansitec.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lansitec.common.JqueryGridParser;
import com.lansitec.dao.ConstructInfoDAO;
import com.lansitec.dao.DevInfoDAO;
import com.lansitec.dao.SystemManagersDAO;
import com.lansitec.dao.WorkerInfoDAO;
import com.lansitec.dao.beans.ConstructInfo;
import com.lansitec.dao.beans.DevInfo;
import com.lansitec.dao.beans.SystemManagers;
import com.lansitec.dao.beans.WorkerInfo;
import com.lansitec.enumlist.Devtype;
import com.lansitec.enumlist.WorkStatus;
import com.lansitec.enumlist.WorkerType;
import com.lansitec.handle.data.GetObjectListByParament;
import com.lansitec.handle.data.OperateSN;
import com.lansitec.handle.data.ParamentSave;
import com.lansitec.handle.data.QueryDiffDataTblInfo;
import com.lansitec.springmvc.beans.WorkerInfoRspBean;

@Controller
@RequestMapping("/WorkerManager")
public class WorkerManager {
	private Logger logger = LoggerFactory.getLogger(WorkerManager.class);
    
	@RequestMapping(value="doGet",method=RequestMethod.GET)
	@ResponseBody
	protected String doGet(HttpServletRequest request){
		String workerid = request.getParameter("id");
		String fieldSN = request.getParameter("fieldSn");
		//page get work name by the type
		String result = "";
		try {
			if((null == workerid) || (workerid.equals(""))){
				List<WorkerInfo> workerInfoList = WorkerInfoDAO.getAllWorkInfo();
				if(workerInfoList.isEmpty()){
					logger.error("Fail to get the workerInfoList {}",workerInfoList);
					return result;
				}
				List<ConstructInfo> constructInfoList = ConstructInfoDAO.getConstructInfoByField(fieldSN);
				if(constructInfoList.isEmpty()){
					logger.error("Fail to get the constructInfoList {}",constructInfoList);
					return result;
				}
				for(ConstructInfo constructInfo : constructInfoList){
					String constrcuctsn = constructInfo.getSn();
					for(WorkerInfo workerInfo : workerInfoList){
						//workersn four bytes before is construct'sn
						String workerFouBytesBefore = workerInfo.getSn().substring(0, 4);
						if(constrcuctsn.equals(workerFouBytesBefore)){
							result += "<option value=\""+workerInfo.getTel()+"\">"+workerInfo.getName()+"</option>";
							break;
						}
					}
					
				}
			}else{
				WorkerInfo workerInfo = WorkerInfoDAO.get(Integer.parseInt(workerid));
				if(null == workerInfo){
					logger.error("Fail to get the workerInfo {} by the workId {}",workerInfo,workerid);
					return result;
				}
				result = workerInfo.getSn();
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="doPost",method=RequestMethod.POST)
	@ResponseBody
	protected WorkerInfoRspBean doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));  
        StringBuffer sb = new StringBuffer("");  
        String temp;  
        while ((temp = br.readLine()) != null) {  
            sb.append(temp);  
        }  
        br.close();
        
        String inStr=URLDecoder.decode(sb.toString(), "UTF-8"); //·ÀÖ¹ºº×ÖÂÒÂë
        logger.info("receive do post in WorkerManager.doPost "+inStr);
       	
        
        
        Map<String, String> postMap = new HashMap<String, String>();
        String oper = JqueryGridParser.parserGridString(inStr, postMap);
        
        String username = request.getParameter("username");
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		if(null == oper)
		{
			return null;
		}
		HttpSession reqSession = request.getSession(false);
		if(reqSession != null)
		{
			String usrname = (String) reqSession.getAttribute("usrname");
			if(usrname != null)
			{
				if(usrname.equals("guest") && !oper.equals("load"))		
					return null;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
		WorkerInfoRspBean workerInfoRspBean = null;
        switch (oper){
        case "load": {
        	List<WorkerInfo> workerInfosList = null;
        	int total = 1;
        	int records = 0;
        	int page = Integer.parseInt(postMap.get("page"));
        	int row = Integer.parseInt(postMap.get("rows"));
        	String hql = "";
        	String result = QueryDiffDataTblInfo.getConstructInfoByNM(username, oper);
        	if(result.equals("['fail']")){
        		logger.error("Fail to get ConstructInfo By usrname {}",username);
        		response.getWriter().write(result);
        		break;
        	}else{
        		boolean judgeValue = QueryDiffDataTblInfo.getWorkerInfoByNM(DeviceListMgr.fieldList);
        		if(!judgeValue){
        			logger.error("Fail to get workerInfo By worker'sn !");
        			break;
        		}else {
        			hql = ParamentSave.getJoinSQL("WorkerInfo wf", "", "", "", "", "", "");
        			if((null == hql) || (hql.equals(""))){
    	        		logger.error("Fail to get the sql {}",hql);
    	        		return null;
    	        	}
        			for(String constructsn : DeviceListMgr.fieldList){
        				result += "sn= '"+constructsn+"' or ";
        			}
        		    hql = hql +" where " + result.substring(0,result.length()-4);
        			workerInfosList = GetObjectListByParament.getWorkerInfosByParament(page, row, hql);
        			if(!workerInfosList.isEmpty()){
        				records = ParamentSave.records;//all record number
    	        		total = ParamentSave.respTotal(ParamentSave.records, row);
        			}else{
        				workerInfosList = GetObjectListByParament.getWorkerInfosByParament(page-1, row, hql);
            			if(!workerInfosList.isEmpty()){
            				records = ParamentSave.records;//all record number
        	        		total = ParamentSave.respTotal(ParamentSave.records, row);
            			}
            			page = ParamentSave.resPage(page);
        			}
        			workerInfoRspBean = new WorkerInfoRspBean(page, total, records, workerInfosList);
    			    break;	
        		}
	        	
        	}
        }
        
        case "add": {
        	String newsn = QueryDiffDataTblInfo.responseSN("worker_info_tbl", "c_sn", postMap.get("c_sn"));
        	WorkerInfo workerInfo = new WorkerInfo(postMap.get("workname"), newsn, postMap.get("w_tel"), WorkerType.valueOf(postMap.get("w_type")), 
        			                                WorkStatus.valueOf(postMap.get("w_status")), postMap.get("w_image"), postMap.get("memo"));
        	WorkerInfoDAO.create(workerInfo);
        	WorkerInfoRspBean rsp = new WorkerInfoRspBean();
        	rsp.setStatus("ok");
        	return rsp;
        }
        
        case "del":{
        	WorkerInfoRspBean rsp = new WorkerInfoRspBean();
        	try {
        		//delete associated data of work'sn
            	OperateSN.deletediffTblAssociateSN("worker_info_tbl", postMap.get("id"));
            	WorkerInfo workerInfo = WorkerInfoDAO.get(Integer.parseInt(postMap.get("id")));
            	String deveui = workerInfo.getDeveui();
            	if((null != deveui) && (!deveui.equals(""))){
            	String workerName = workerInfo.getName();
            	WorkerInfoDAO.delete(workerInfo);
				DevInfo devInfo = DevInfoDAO.getDevInfoByDeveui(deveui);
				if(null != devInfo){
					devInfo.setOwner("");
					DevInfoDAO.update(devInfo);
				}else{
					logger.info("the worker {} doesn't distructed the deveui!",workerName);
				}
            	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rsp.setStatus("internal error!");
				return rsp;
			}
        	rsp.setStatus("ok");
        	return rsp;
        }
        
        case "edit":{
        	//define value before
        	String editDeveui = null;
        	WorkerInfoRspBean rsp = new WorkerInfoRspBean();
        	String oldsn = OperateSN.getOldInfo("worker_info_tbl",postMap.get("w_id"),"c_sn",postMap.get("c_sn"));
        	try {
        		DevInfo devInfo = null;
        		if(null != postMap.get("deveui")){
        			devInfo = DevInfoDAO.getDevInfoByDeveui("004a77021103"+postMap.get("deveui"));
        			if(null != devInfo){
        				if(((null == devInfo.getOwner()) || (devInfo.getOwner().equals(""))) || (devInfo.getOwner().equals(oldsn))){
        					editDeveui = devInfo.getDeveui();
        				}else{
        					SystemManagers systemManagers = SystemManagersDAO.getMangersInfoByUsername(devInfo.getOwner());
        					if(null ==  systemManagers){
        						logger.info("the deveui {} already has been distributed others !","004a77021103"+postMap.get("deveui"));
            					rsp.setStatus("the deveui already has been distributed others !");
            					return rsp;
        					}
        					editDeveui = devInfo.getDeveui();
        				}
        			}else{
        				logger.error("Fail to get the devInfo {} by the deveui {}",devInfo,"004a77021103"+postMap.get("deveui"));
        	        	rsp.setStatus("the deveui doesn't exist in dataBase !");
        	        	return rsp;
        			}
        		}
        		
            	String newsn = null;
            	if(null != oldsn){
            		if(oldsn != OperateSN.newestData){
                		OperateSN.updatediffTblJointSN("worker_info_tbl", oldsn);
                		newsn = OperateSN.changeData;
                	}else{
                		newsn = OperateSN.newestData;
                	}
            		WorkerInfo workerInfo = WorkerInfoDAO.get(Integer.parseInt(postMap.get("w_id")));
                    workerInfo.setName(postMap.get("workname"));
                    workerInfo.setSn(newsn);
                    workerInfo.setTel(postMap.get("w_tel"));
                    workerInfo.setType(WorkerType.valueOf(postMap.get("w_type")));
                    workerInfo.setStatus(WorkStatus.valueOf(postMap.get("w_status")));
                    workerInfo.setDeveui(editDeveui);
                    workerInfo.setMemo(postMap.get("memo"));
                	WorkerInfoDAO.update(workerInfo);
                	if((null != editDeveui) && (!devInfo.getOwner().equals(newsn))){
                		devInfo.setOwner(newsn);
                        devInfo.setDevtype(Devtype.¹¤ÈË);
                        DevInfoDAO.update(devInfo);
                	}
                	rsp.setStatus("ok");
            	}else{
            		logger.error("Fail to get oldsn {} and update to data of worker_info_tbl!",oldsn);
            		rsp.setStatus("Fail to get oldsn !");
            	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rsp.setStatus("internal error!");
				return rsp;
			}
        	return rsp;
        }
        
        case "query":{
        	break;
         }
        
        default:{
         }
       }//switch
        return workerInfoRspBean;
	}
}
