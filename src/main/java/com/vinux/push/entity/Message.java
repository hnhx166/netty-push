package com.vinux.push.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//应用ID
    private String appId;
    
    //设备ID
//    private String deviceId;
 
    //版本
    private int version;
 
    //消息发送人ID
    private String uid;
 
    //消息类型 0：登录 1：文字消息 2:文件 3：视频
    private byte msgType;
    
    //消息：系统消息，聊天消息
    //系统消息：所有用户，部分用户，单个用户
    //聊天消息: 群聊，好友
    //0：群发，1：单发
//    private int sendType;
//    //群组ID
//    private Integer groupId;
    //消息接收人
    private String receiveId;
    
    //消息组
    private String groupId;
    
    //接收类型(默认:用户消息)
    private byte receiveType;
 
    //消息内容
    private String msg;
    
    //消息内容
    private Date sendTime;
 
}
