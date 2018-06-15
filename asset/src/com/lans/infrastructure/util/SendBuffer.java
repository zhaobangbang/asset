package com.lans.infrastructure.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

class bufferMsg {
	String sender;
	Object msg;

	public bufferMsg(String sender, Object msg) {
		this.sender = sender;
		this.msg = msg;
	}

	public String getSender() {
		return sender;
	}

	public Object getMsg() {
		return msg;
	}
}

public class SendBuffer {
	Map<String, ConcurrentLinkedQueue<bufferMsg>> buffMap = null;
	String name;

	public SendBuffer(String bufferName) {
		buffMap = new ConcurrentHashMap<String, ConcurrentLinkedQueue<bufferMsg>>();
		name = bufferName;
	}

	public void addBufferMsgToTail(String receiver, String sender, Object msg) {
		ConcurrentLinkedQueue<bufferMsg> bufQueue = buffMap.get(receiver);

		if (bufQueue == null) {
			bufQueue = new ConcurrentLinkedQueue<bufferMsg>();
			buffMap.put(receiver, bufQueue);
		}

		bufferMsg buff = new bufferMsg(sender, msg);
		bufQueue.add(buff);
	}

	public Object getMsgOfBufferHead(String receiver) {
		ConcurrentLinkedQueue<bufferMsg> bufQueue = buffMap.get(receiver);

		if ((bufQueue == null) || (bufQueue.isEmpty())) {
			return null;
		}

		return bufQueue.peek().getMsg();
	}

	public String getSenderOfBufferHead(String receiver) {
		ConcurrentLinkedQueue<bufferMsg> bufQueue = buffMap.get(receiver);

		if ((bufQueue == null) || (bufQueue.isEmpty())) {
			return null;
		}

		return bufQueue.peek().getSender();
	}

	public void rmHeadOfBuffer(String receiver) {
		ConcurrentLinkedQueue<bufferMsg> bufQueue = buffMap.get(receiver);

		if (bufQueue != null) {
			bufQueue.poll();
		}
	}

	public boolean isReceiverBufferEmpty(String receiver) {
		ConcurrentLinkedQueue<bufferMsg> bufQueue = buffMap.get(receiver);
		if (bufQueue == null) {
			return true;
		}

		return bufQueue.isEmpty();
	}

}
