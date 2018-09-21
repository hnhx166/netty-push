package com.vinux.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinux.dao.entity.PushMessage;
import com.vinux.dao.mapper.PushMessageMapper;
import com.vinux.service.PushMessageService;
@Service
public class PushMessageServiceImpl implements PushMessageService {
	
	@Autowired
	PushMessageMapper pushMessageMapper;

	@Override
	public int insert(PushMessage record) {
		return pushMessageMapper.insert(record);
	}

	@Override
	public int insertSelective(PushMessage record) {
		return pushMessageMapper.insertSelective(record);
	}

	@Override
	public List<PushMessage> findPushMessages() {
		// TODO Auto-generated method stub
		return null;
	}

}
