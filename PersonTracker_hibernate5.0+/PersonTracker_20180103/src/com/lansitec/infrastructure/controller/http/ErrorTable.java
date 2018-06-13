package com.lansitec.infrastructure.controller.http;

import java.util.LinkedList;
import java.util.List;

public class ErrorTable {

	static List<ErrorTableItem> errorList = new LinkedList<ErrorTableItem>();

	public ErrorTable() {
		// TODO Auto-generated constructor stub
	}

	public static void addErrorItem(ErrorTableItem tblItem) {
		for (ErrorTableItem item : errorList) {
			if (item.getId() == tblItem.getId()) {
				return;
			}
		}
		errorList.add(tblItem);
	}

	public static String getErrorDescription(int alarmId) {
		for (ErrorTableItem item : errorList) {
			if (item.getId() == alarmId) {
				return item.getDescription();
			}
		}
		return "not defined";
	}
}
