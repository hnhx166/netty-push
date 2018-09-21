package com.vinux.push.enu;

public enum ReceiveType {

	/**
	 * 服务器消息
	 */
	TYPE_SERVER((byte)1),
	/**
	 * 用户消息
	 */
	TYPE_USER((byte)2);
	
	private byte value;
	
	
	private ReceiveType(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}
	
}
