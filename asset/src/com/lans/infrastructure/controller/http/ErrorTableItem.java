package com.lans.infrastructure.controller.http;

public class ErrorTableItem {

	int id;
	String description;

	public ErrorTableItem(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

}
