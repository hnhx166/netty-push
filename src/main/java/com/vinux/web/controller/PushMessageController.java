package com.vinux.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinux.dao.entity.PushMessage;
import com.vinux.push.server.PushServer;
import com.vinux.service.PushMessageService;

@RestController
@RequestMapping("api")
public class PushMessageController {
	
	@Autowired
	PushMessageService pushMessageService;
	
	@Autowired
	PushServer pushServer;

	@RequestMapping("push")
	public Map<String, Object> pushMessage(PushMessage message) {
		Map<String, Object> resultData = new HashMap<String, Object>();
		int result = pushMessageService.insert(message);
		if(result > 0) {
			resultData.put("status", "添加成功");
		}else {
			resultData.put("status", "添加失败");
		}
		return resultData;
	}
	
	
}
