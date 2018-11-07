package com.vinux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.vinux.push.cache.ChatChannelCache;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MessageType;
import com.vinux.push.server.PushServer;

@RestController
@SpringBootApplication
public class NettyPushApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyPushApplication.class, args);
	}
	
	@Autowired
	PushServer pushServer;
	
	@RequestMapping("push")
	public void push(String msg) {//, String uid, String recId
		Message message = new Message();
        message.setMsgType(MessageType.MSG_BOX_PUSH.getValue());
        message.setAppId("Server");
//        message.setReceiveId(recId);
        message.setUid("uid000");
        message.setVersion(1);
        message.setMsg(msg);
        message.setSendTime(new Date(System.currentTimeMillis()));
		pushServer.push(message);
	}
	
	@RequestMapping("channelCount")
	public int channelCount(String msg) {//, String uid, String recId
		return ChatChannelCache.getChannels().size();
	}
	
	@RequestMapping("channels")
	public Collection channels(String msg) {//, String uid, String recId
		return ChatChannelCache.getChannels();
	}
	
	private String getMacAddrByIp(String ip) {
	    String macAddr = null;
	    try {
	        Process process = Runtime.getRuntime().exec("nbtstat -a " + ip);
	        BufferedReader br = new BufferedReader(
	                new InputStreamReader(process.getInputStream()));
	        Pattern pattern = Pattern.compile("([A-F0-9]{2}-){5}[A-F0-9]{2}");
	        Matcher matcher;
	        for (String strLine = br.readLine(); strLine != null;
	             strLine = br.readLine()) {
	            matcher = pattern.matcher(strLine);
	            if (matcher.find()) {
	                macAddr = matcher.group();
	                break;
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return macAddr;
	}
	
	@RequestMapping("/getUser")
	public Map<String, Object> getUserByLoginNamePassword(String loginName, String loginPass) {
		Map<String, Object> loginData = new HashMap<String, Object>();
		loginData.put("loginMsg", "登录账号成功");
		return loginData;
	}
	
	@RequestMapping("/nih")
	public ModelAndView index() {
		return new ModelAndView("/index");
	}
	
	@RequestMapping("/mac")
	public ModelAndView mac() {
		return new ModelAndView("/mac");
	}
	
	
	
	
	
	
}
