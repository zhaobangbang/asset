package com.lansitec.infrastructure.controller.http;

public class ErrorGen {

	public static Exception gen(int id) {
		String errorId = Integer.toString(id);
		return new Exception(errorId);
	}
}
