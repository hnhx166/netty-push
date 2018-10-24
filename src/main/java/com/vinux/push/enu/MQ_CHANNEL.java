package com.vinux.push.enu;

public enum MQ_CHANNEL {

	/**
	 * 缓存数据
	 */
	CHANNEL_CACHE("netty_push_cache", 0),
	
	/**
	 * 保存数据
	 */
	CHANNEL_SAVE("netty_push_save",1),
	
	/**
	 * 后台应用程序向客户端推送数据
	 */
	CHANNEL_PUSH_TO_SERVER("netty_push_to_server",2);
	
	private String name;
	private int index;
	
	
	MQ_CHANNEL(String name, int index){
		this.name = name;
		this.index = index;
	}
	
	public static String getName(int index) {
		for(MQ_CHANNEL v : MQ_CHANNEL.values()) {
			if(v.index == index) {
				return v.getName();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	
	
	
}
