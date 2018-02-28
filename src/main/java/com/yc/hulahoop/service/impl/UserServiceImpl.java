package com.yc.hulahoop.service.impl;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.FeedbackInfoMapper;
import com.yc.hulahoop.dao.UserMapper;
import com.yc.hulahoop.pojo.FeedbackInfo;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * Created by ts on 18-2-28.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    FeedbackInfoMapper feedbackInfoMapper;

    @Override
    public ServerResponse<String> verify(String val, String type) {
        int count;
        switch (type) {
            case Const.USERNAME:    //校验 用户名
                count = userMapper.verifyUsername(val);
                break;
            case Const.PHONE:       //校验 手机号
                count = userMapper.verifyPhone(val);
                break;
            default:
                return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        if (count > 0) {    //参数已存在
            return ServerResponse.createByErrorMessage(type + Const.DUPLICATE_PARAMETER);
        }
        //校验成功
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> register(User user) {
        //校验 用户名
        ServerResponse<String> serverResponse = verify(user.getUsername(), Const.USERNAME);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        //校验 手机号
        serverResponse = verify(user.getPhone(), Const.PHONE);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        //密码加密
        //todo: password MD5

        //添加用户
        int count = userMapper.insertSelective(user);
        //注册成功
        if (count > 0) {
            return ServerResponse.createBySuccess();
        }
        //注册失败
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse login(String val, String password, String type) {
        //密码加密
        //todo: password MD5
        User user;
        switch (type) {
            case Const.USERNAME:    //用用户名登录
                user = userMapper.loginByUsername(val, password);
                break;
            case Const.PHONE:       //用手机号登录
                user = userMapper.loginByPhone(val, password);
                break;
            default:
                return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //登录成功
        if (user != null) {
            user.setPassword(StringUtils.EMPTY);    //将密码置空再返回user对象
            return ServerResponse.createBySuccess(Const.ResponseCode.SUCCESS.getDescription(), user);
        }
        //登录失败
        return ServerResponse.createByErrorMessage(Const.ResponseCode.ERROR.getDescription());
    }

    @Override
    public ServerResponse verifyPassword(String passwordOld, int userId) {
        //todo: password MD5
        int count = userMapper.verifyPassword(passwordOld, userId);
        //校验密码正确
        if (count > 0) {
            return ServerResponse.createBySuccess();
        }
        //校验密码失败
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse resetPassword(String passwordNew, User user) {
        //todo: password MD5
        user.setPassword(passwordNew);
        int count = userMapper.updateByPrimaryKeySelective(user);
        //重置密码成功
        if (count > 0) {
            return ServerResponse.createBySuccess();
        }
        //重置密码失败
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse queryUserInformation(int userId) {
        User user=userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage(Const.NO_USER);
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessData(user);
    }

    @Override
    public ServerResponse updateUserInformation(User user) {
        int count=userMapper.updateByPrimaryKeySelective(user);
        if(count > 0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse feedback(FeedbackInfo feedbackInfo) {
        feedbackInfo.setLevel(String.valueOf(feedbackInfoMapper.queryFeedbackCount() + 1));
        int count=feedbackInfoMapper.insertSelective(feedbackInfo);
        //添加反馈成功
        if(count > 0){
            return ServerResponse.createBySuccess();
        }
        //添加反馈失败
        return ServerResponse.createByError();
    }
}