package com.vinux.push.enu;

public enum MessageType {

	/**
	 * 连接请求
	 */
	CONNECT_REQ((byte) 1), 
	/**
	 * 连接成功
	 */
	CONNECT_SUCCESS((byte) 2), 
	/**
	 * 连接失败
	 */
	CONNECT_FAIL((byte) 3), 
	/**
	 * 心跳检测
	 */
	HEARTBEAT_REQ((byte) 4), 
	/**
	 * 心跳检测响应
	 */
	HEARTBEAT_RESP((byte) 5), 
	/**
	 * 宝盒用户信息
	 */
	MSG_BOX_USER_INFO((byte)6),
	/**
	 * 聊天用户消息
	 */
	MSG_CHAT_USER_INFO((byte)7),
	
	/**
	 * 宝盒消息推送
	 */
	MSG_BOX_PUSH((byte) 8), 
	
	/**
	 * 单人聊天消息
	 */
	MSG_CHAT_SINGLE_PUSH((byte)9),
	/**
	 * 群聊消息
	 */
	MSG_CHAT_GROUP_PUSH((byte)10);

	private byte value;

	private MessageType(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}
}
