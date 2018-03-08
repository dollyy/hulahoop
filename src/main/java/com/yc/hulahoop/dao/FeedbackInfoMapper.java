package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.FeedbackInfo;
import com.yc.hulahoop.vo.FeedbackInfoVo;

import java.util.List;

public interface FeedbackInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FeedbackInfo record);

    int insertSelective(FeedbackInfo record);

    FeedbackInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FeedbackInfo record);

    int updateByPrimaryKey(FeedbackInfo record);

    List<FeedbackInfoVo> queryFeedbackList();

    int queryFeedbackCount();

    List<FeedbackInfoVo> detail(String level);
}