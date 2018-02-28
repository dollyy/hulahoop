package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.FeedbackInfo;

public interface FeedbackInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FeedbackInfo record);

    int insertSelective(FeedbackInfo record);

    FeedbackInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FeedbackInfo record);

    int updateByPrimaryKey(FeedbackInfo record);

    int queryFeedbackCount();
}