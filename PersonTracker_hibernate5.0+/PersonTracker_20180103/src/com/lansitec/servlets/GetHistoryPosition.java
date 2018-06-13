package com.lansitec.servlets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lansitec.dao.PositionRecordDAO;
import com.lansitec.dao.beans.PositionRecord;
import com.lansitec.springmvc.beans.HistoryInfoBean;

@Controller
@RequestMapping("/GetHistoryPosition")
public class GetHistoryPosition {
	private Logger logger = LoggerFactory.getLogger(GetHistoryPosition.class);
	
	@RequestMapping(value="doPost",method = RequestMethod.POST)
	@ResponseBody
	protected List<PositionRecord> doPost(HistoryInfoBean historyInfoBase) throws Exception{
		String deveui = historyInfoBase.getDeveui();
		String dateTimeOne = historyInfoBase.getDatebut1();
		String dateTimeTwo = historyInfoBase.getDatebut2();
		
		List<PositionRecord> positionRecordList = PositionRecordDAO.getPositionBydeveuiAndTime(deveui, dateTimeOne, dateTimeTwo);
		if((null == positionRecordList) || (positionRecordList.size() == 0)){
			logger.error("Fail to get the positionRecord {} by the deveui {}",positionRecordList,deveui);
			return positionRecordList;
		}
		return positionRecordList;
	}

}
