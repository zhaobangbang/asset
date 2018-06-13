package com.lansitec.infrastructure.controller.http;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ResponseSender {

	public static void send(HttpServletResponse response, RespGenerator rsp) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().append(rsp.toJSONString());
	}

}
