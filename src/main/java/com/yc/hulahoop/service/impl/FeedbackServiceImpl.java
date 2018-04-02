package com.yc.hulahoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.DwrRecordMapper;
import com.yc.hulahoop.dao.FeedbackInfoMapper;
import com.yc.hulahoop.pojo.FeedbackInfo;
import com.yc.hulahoop.service.FeedbackService;
import com.yc.hulahoop.vo.DwrRecordVo;
import com.yc.hulahoop.vo.FeedbackInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    FeedbackInfoMapper feedbackInfoMapper;

    @Autowired
    DwrRecordMapper dwrRecordMapper;

    @Override
    public ServerResponse list(Integer pageNum) {
        if (pageNum == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.PAGE_SIZE_ADMIN);
        List<FeedbackInfoVo> feedbackInfoList = feedbackInfoMapper.queryFeedbackList();
        if (feedbackInfoList.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        for(FeedbackInfoVo feedbackInfoVo : feedbackInfoList){
            feedbackInfoVo.setStatus(feedbackInfoMapper.queryStatus(Const.ADMIN_ID, feedbackInfoVo.getLevel()));
        }
        //2.pageHelper--end
        PageInfo pageInfo = new PageInfo(feedbackInfoList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse add(Integer userId, String content) {
        if (userId == null || StringUtils.isBlank(content)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //封装FeedbackInfo对象
        FeedbackInfo feedbackInfo = new FeedbackInfo();
        feedbackInfo.setSendId(userId);
        feedbackInfo.setReceiveId(Const.ADMIN_ID);  //发送给admin
        feedbackInfo.setContent(content);
        feedbackInfo.setParent(0);      //添加评论时parent为0
        feedbackInfo.setSequence(1);    //添加评论时sequence为1
        feedbackInfo.setLevel(String.valueOf(feedbackInfoMapper.queryFeedbackCount() + 1));
        //添加反馈
        int count = feedbackInfoMapper.insert(feedbackInfo);
        //添加反馈成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("反馈信息成功");
        }
        //添加反馈失败
        return ServerResponse.createByErrorMessage("反馈信息失败");
    }

    @Override
    public ServerResponse reply(Integer sendId, String content, Integer parent, Integer sequence,
                                Integer receiveId) {
        if (sendId == null || StringUtils.isBlank(content) || parent == null || sequence == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        FeedbackInfo feedbackInfo = new FeedbackInfo();
        feedbackInfo.setSendId(sendId);
        feedbackInfo.setReceiveId(receiveId);
        feedbackInfo.setContent(content);
        feedbackInfo.setParent(parent);
        feedbackInfo.setSequence(sequence + 1);
        feedbackInfo.setLevel(parent + "." + (sequence + 1));
        //回复信息
        int count = feedbackInfoMapper.insert(feedbackInfo);
        //回复信息成功
        if (count > 0) {
            //更新操作时间
            feedbackInfoMapper.updateUpdateTimeByLevel(parent + "%");
            //将feedback的status从0置为1
            count = feedbackInfoMapper.updateStatusByFeedId(sendId, receiveId, parent + "%", 1);
            if (count > 0) {
                return ServerResponse.createBySuccessData(feedbackInfo.getSequence());
            }
        }
        //回复信息失败
        return ServerResponse.createByErrorMessage("回复信息失败");
    }

    @Override
    public ServerResponse detail(String level) {
        if (StringUtils.isBlank(level)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        List<FeedbackInfoVo> feedbackInfoVoList = feedbackInfoMapper.detail(level + "%");
        //没有匹配信息
        if (feedbackInfoVoList.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        return ServerResponse.createBySuccessData(feedbackInfoVoList);
    }

    @Override
    public ServerResponse search(String content, Integer pageNum) {
        if (StringUtils.isBlank(content) || pageNum == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        List<Integer> index = feedbackInfoMapper.search("%" + content + "%");
        //没有匹配信息
        if (index.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        List<FeedbackInfoVo> feedbackInfoVoList = Lists.newArrayList();
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.PAGE_SIZE_ADMIN);
        //根据parent据查询该反馈的完整信息
        for (Integer integer : index) {
            feedbackInfoVoList.add(feedbackInfoMapper.queryVoById(integer));
        }
        //没有匹配信息
        if (feedbackInfoVoList.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        //2.pageHelper--end
        PageInfo pageInfo = new PageInfo(feedbackInfoVoList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse listByUser(Integer userId, Integer pageNum) {
        if (userId == null || pageNum == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //feedback
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.PAGE_SIZE);
        List<Object> results = Lists.newArrayList();
        List<Integer> index = feedbackInfoMapper.listByUser(userId);
        if (index.size() > 0) {
            //根据parent据查询该反馈的完整信息
            for (Integer integer : index) {
                FeedbackInfoVo feedbackInfoVo = feedbackInfoMapper.queryVoById(integer);
                feedbackInfoVo.setStatus(feedbackInfoMapper.queryStatus(userId, feedbackInfoVo.getLevel() + "%"));
                results.add(feedbackInfoVo);
            }
        }
        //dwr
        //1.startPage--start
        List<DwrRecordVo> dwrRecordVos = dwrRecordMapper.listByUser(userId);
        if (dwrRecordVos.size() > 0) {
            results.addAll(dwrRecordVos);
        }
        //2.pageHelper--end
        PageInfo pageInfo = new PageInfo(results);
        //没有匹配信息
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse queryNotice(Integer userId) {
        int feedCount = feedbackInfoMapper.queryNotice(userId);
        int dwrCount = dwrRecordMapper.queryNotice(userId);
        if (feedCount == 0 && dwrCount == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse updateFeedStatus(Integer sendId, Integer receiveId, String level, Integer status) {
        if (receiveId == null || level == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //更新操作时间
        feedbackInfoMapper.updateUpdateTimeByLevel(level + "%");
        //将feedback的status从1置为0
        int count = feedbackInfoMapper.updateStatusByFeedId(sendId, receiveId, level + "%", 0);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }
}