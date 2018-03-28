package com.yc.hulahoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.FeedbackInfoMapper;
import com.yc.hulahoop.pojo.FeedbackInfo;
import com.yc.hulahoop.service.FeedbackService;
import com.yc.hulahoop.vo.FeedbackInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    FeedbackInfoMapper feedbackInfoMapper;

    @Override
    public ServerResponse list(Integer pageNum) {
        List<FeedbackInfoVo> feedbackInfoList = feedbackInfoMapper.queryFeedbackList();
        if (feedbackInfoList.size() == 0) {
            return ServerResponse.createBySuccessMessage("暂无数据");
        }
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.PAGE_SIZE);
        //2.pageHelper--end
        PageInfo pageInfo = new PageInfo(feedbackInfoList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse add(FeedbackInfo feedbackInfo) {
        feedbackInfo.setLevel(String.valueOf(feedbackInfoMapper.queryFeedbackCount() + 1));
        int count = feedbackInfoMapper.insert(feedbackInfo);
        //添加反馈成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("反馈信息成功");
        }
        //添加反馈失败
        return ServerResponse.createByErrorMessage("反馈信息失败");
    }

    @Override
    public ServerResponse reply(FeedbackInfo feedbackInfo) {
        int count = feedbackInfoMapper.insert(feedbackInfo);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("回复信息成功");
        }
        return ServerResponse.createByErrorMessage("回复信息失败");
    }

    @Override
    public ServerResponse detail(String level) {
        List<FeedbackInfoVo> feedbackInfoVoList = feedbackInfoMapper.detail(level + "%");
        if(feedbackInfoVoList.size() == 0){
            return ServerResponse.createBySuccessMessage("暂无数据");
        }
        return ServerResponse.createBySuccessData(feedbackInfoVoList);
    }
}
