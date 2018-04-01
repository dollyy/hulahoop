package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.FeedbackInfo;
import com.yc.hulahoop.vo.FeedbackInfoVo;
import org.apache.ibatis.annotations.Param;

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

    List<Integer> search(String content);

    FeedbackInfoVo queryVoById(Integer feedBackId);

    List<Integer> listByUser(Integer userId);

    int queryNotice(Integer userId);

    int updateStatusByFeedId(@Param(value = "responseId") Integer responseId, @Param(value = "level") String level,
                             @Param(value = "status") Integer status);

    int updateUpdateTimeByLevel(@Param(value = "level") String level);
}