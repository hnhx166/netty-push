package com.vinux.service;

import java.util.List;

import com.vinux.dao.entity.PushMessage;

public interface PushMessageService {

	int insert(PushMessage record);

    int insertSelective(PushMessage record);

    List<PushMessage> findPushMessages();
}
