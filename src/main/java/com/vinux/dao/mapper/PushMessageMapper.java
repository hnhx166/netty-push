package com.vinux.dao.mapper;

import java.util.List;

import com.vinux.dao.entity.PushMessage;

public interface PushMessageMapper {

    int insert(PushMessage record);

    int insertSelective(PushMessage record);
    
    List<PushMessage> findPushMessages();

}