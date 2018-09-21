package com.vinux.push.im;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IMMessage {

	//应用ID
    private byte appId;
 
    //版本
    private int version;
 
    //用户ID
    private int uid;
 
    //消息类型 0：登录 1：文字消息
    private byte msgType;
 
    //接收方
    private int receiveId;
 
    //消息内容
    private String msg;
 
    
}
