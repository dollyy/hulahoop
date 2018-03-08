package com.yc.hulahoop.service.impl;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.UserMapper;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.UserService;
import com.yc.hulahoop.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Yang Chen on 18-2-28.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public ServerResponse<String> verify(String val, String type) {
        if (StringUtils.isBlank(val) || StringUtils.isBlank(type)) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
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
            return ServerResponse.createByErrorMessage("参数已存在");
        }
        //校验成功
        return ServerResponse.createBySuccessMessage("参数校验成功");
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
        //密码MD5加密
        user.setPassword(MD5Util.MD5Encode(user.getPassword(), "utf-8"));
        //设置当前用户的角色
        user.setRole(Const.Role.USER);

        //添加用户
        int count = userMapper.insert(user);
        //注册成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("注册成功");
        }
        //注册失败
        return ServerResponse.createByErrorMessage("注册失败");
    }

    @Override
    public ServerResponse login(String val, String password) {
        if (StringUtils.isBlank(val) || StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //密码MD5加密
        password = MD5Util.MD5Encode(password, "utf-8");
        //1.用用户名登录
        if (userMapper.verifyUsername(val) == 0) {    //校验用户名是否存在
            return ServerResponse.createByErrorMessage(Const.NO_USER);
        }
        User nameUser = userMapper.loginByUsername(val, password);
        //登录成功
        if (nameUser != null) {
            nameUser.setPassword(StringUtils.EMPTY);    //将密码置空再返回user对象
            return ServerResponse.createBySuccess("登陆成功", nameUser);
        }
        //2.用手机号登录
        //校验手机号时候存在
        if (userMapper.verifyPhone(val) == 0) {
            return ServerResponse.createByErrorMessage(Const.NO_PHONE);
        }
        User phoneUser = userMapper.loginByPhone(val, password);
        //登录成功
        if (phoneUser != null) {
            phoneUser.setPassword(StringUtils.EMPTY);    //将密码置空再返回user对象
            return ServerResponse.createBySuccess("登陆成功", phoneUser);
        }
        //登录失败
        return ServerResponse.createByErrorMessage("用户名或密码错误");
    }

    @Override
    public ServerResponse resetPassword(String passwordOld, String passwordNew, User user) {
        if (StringUtils.isBlank(passwordOld) || StringUtils.isBlank(passwordNew)) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //校验旧密码
        passwordOld = MD5Util.MD5Encode(passwordOld, "utf-8");
        int count = userMapper.verifyPassword(passwordOld, user.getId());
        //校验密码失败
        if (count == 0) {
            return ServerResponse.createByErrorMessage("原始密码错误");
        }
        //新密码MD5加密
        passwordNew = MD5Util.MD5Encode(passwordNew, "utf-8");
        user.setPassword(passwordNew);
        count = userMapper.updateByPrimaryKeySelective(user);
        //重置密码成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("重置密码成功");
        }
        //重置密码失败
        return ServerResponse.createByErrorMessage("重置密码失败");
    }

    @Override
    public ServerResponse queryUserInformation(Integer userId) {
        if (userId == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage(Const.NO_USER);
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessData(user);
    }

    @Override
    public ServerResponse updateUserInformation(User user) {
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新用户信息成功");
        }
        return ServerResponse.createByErrorMessage("更新用户信息失败");
    }

    @Override
    public ServerResponse adminLogin(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        password = MD5Util.MD5Encode(password, "utf-8");
        User user = userMapper.adminLogin(username, password);
        if (user != null) {
            user.setPassword(StringUtils.EMPTY);        //密码置空
            return ServerResponse.createBySuccess("管理员登陆成功", user);
        }
        int count = userMapper.existAdmin();
        if (count > 0) {
            return ServerResponse.createByErrorMessage("管理员用户名或密码错误");
        }
        //第一次登录即为管理员
        User admin=new User();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole(Const.Role.ADMIN);
        //todo admin phone ???
        admin.setPhone("");
        count=userMapper.insert(admin);
        //获取user的信息
        user=userMapper.selectByPrimaryKey(admin.getId());
        if (count > 0) {
            user.setPassword(StringUtils.EMPTY);        //密码置空
            return ServerResponse.createBySuccess("管理员登陆成功", user);
        }
        return ServerResponse.createByErrorMessage("管理员登陆失败");
    }
}