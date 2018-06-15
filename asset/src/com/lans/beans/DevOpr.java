package com.lans.beans;

/*
 * REG: Dev registered already.
 * LOCATE: Locate command received.
 * UNLOCATE: Stop locate command received.
 * DATARCV: Position info received.
 * OFFLINE: No connection with server.
 */

public enum DevOpr{
	REG, 
	LOCATE, 
	UNLOCATE, 
	DATARCV, 
	OFFLINE,
	ONLINE
}

