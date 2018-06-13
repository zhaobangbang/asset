package com.lansitec.controller.networkgw;

public class TVMsgDefs {
	public static final byte UL_DEV_REG = 0x1;
	public static final byte UL_DEV_HB = 0x2;
	public static final byte UL_DEV_POS_UNACK = 0x3;
	public static final byte UL_DEV_POS_ACK = 0x4;
	public static final byte UL_DEV_LOST_POS = 0x5;
	public static final byte UL_DEV_ALARM = 0x6;
	public static final byte UL_DEV_INDOOR_POS = 0x7;
	public static final byte UL_DEV_ACK = 0xF;
	
	public static final byte DL_REG_RESULT = 0x1;
	public static final byte DL_LORA_CFG = 0x8;
	public static final byte DL_MODE_CFG = 0x9;
	public static final byte DL_COMMAND_REQ = 0xA;
	public static final byte DL_SLOT = 0xB;
	public static final byte DL_ACK = 0xF;
	
}
