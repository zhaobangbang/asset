package com.lans.controller.networkgw.msghandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageIdGenerater {
	//�ýӿ�Map����HashMapʵ���࣬�����������
	static Map<String, Byte> idMap = new ConcurrentHashMap<String, Byte>();
	static byte seqId = 0;

	public MessageIdGenerater() {

	}

	public static byte getMessageId() {
		return seqId++;
	}

	public static byte getMessageId(String eui) {
		//boolean containsKey(Object key)default true
		if (!idMap.containsKey(eui)) {
			idMap.put(eui, (byte) 0);
			return 0;
		}

		byte value = idMap.get(eui);
		idMap.put(eui, (byte) (value + 1));
		return (byte) (value + 1);
	}
}
