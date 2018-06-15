package com.lans.infrastructure.controller.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RespGenerator {
	Map<String, Object> response;
	Logger logger = LoggerFactory.getLogger(RespGenerator.class);

	public RespGenerator(int statusId, HttpServletRequest request) {
		response = new ConcurrentHashMap<String, Object>();
		response.put("status", statusId);

		String message = ErrorTable.getErrorDescription(statusId);

		response.put("message", message);
	}

	public RespGenerator(Exception e, HttpServletRequest request) {
		response = new ConcurrentHashMap<String, Object>();

		String status = e.getMessage();
		if (status == null) {
			status = "internal exception";
		}
		int statusId = 0;
		String message = null;

		try {
			statusId = Integer.parseInt(status);
			message = ErrorTable.getErrorDescription(statusId);
		} catch (Exception parserE) {
			statusId = -1;
			message = "not defined";
		}
		response.put("status", statusId);
		response.put("message", message);
	}

	public void addResponse(String item, Object obj) {
		response.put(item, obj);
	}

	public String toJSONString() {
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		return JSON.toJSONString(response, SerializerFeature.WriteDateUseDateFormat);
	}
}
