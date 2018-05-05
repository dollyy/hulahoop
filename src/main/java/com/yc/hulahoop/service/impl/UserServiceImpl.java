package com.yc.hulahoop.service.impl;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.common.TokenCache;
import com.yc.hulahoop.dao.UserMapper;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.UserService;
import com.yc.hulahoop.util.MD5Util;
import com.yc.hulahoop.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        int count;
        switch (type) {
            //校验 用户名
            case Const.USERNAME:
                count = userMapper.verifyUsername(val);
                break;
            //校验 手机号
            case Const.EMAIL:
                count = userMapper.verifyEmail(val);
                break;
            default:
                return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                        Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
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
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        //校验 邮箱
        //验证格式
        String email = user.getEmail();
        Pattern pattern = Pattern.compile(Const.EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return ServerResponse.createByErrorMessage("邮箱格式错误");
        }
        //验证唯一性
        serverResponse = verify(email, Const.EMAIL);
        if (!serverResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("邮箱已绑定");
        }
        //密码MD5加密
        user.setPassword(MD5Util.MD5Encode(user.getPassword(), "utf-8"));
        //设置当前用户的角色
        user.setRole(Const.Role.USER);
        //设置默认头像
        user.setAvatar(PropertiesUtil.getProperty("ftp.avatar.default"));
        //设置性别
        user.setGender("unknown");

        //注册
        int count = userMapper.insert(user);
        //注册成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("注册成功");
        }
        //注册失败
        return ServerResponse.createByErrorMessage("注册失败");
    }

    @Override
    public ServerResponse login(String type, String val, String password) {
        if (StringUtils.isBlank(val) || StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //密码MD5加密
        password = MD5Util.MD5Encode(password, "utf-8");
        User user;
        switch (type) {
            //用户名登录
            case Const.USERNAME:
                user = userMapper.loginByUsername(val, password);
                break;
            //手机号登录
            case Const.EMAIL:
                user = userMapper.loginByEmail(val, password);
                break;
            default:
                return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                        Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //登录成功
        if (user != null) {
            user.setPassword(StringUtils.EMPTY);    //将密码置空再返回user对象
            return ServerResponse.createBySuccess("登陆成功", user);
        }
        //登录失败
        return ServerResponse.createByErrorMessage("用户名或密码错误");
    }

    @Override
    public ServerResponse resetPassword(String passwordOld, String passwordNew, User user) {
        if (StringUtils.isBlank(passwordOld) || StringUtils.isBlank(passwordNew)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
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
    public ServerResponse updatePassword(String email, String password, String token) {
        System.out.println("service token=================");
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //token校验
        String resetToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX + email);
        System.out.println("reset-->" + resetToken + ",token->" + token);
        if (StringUtils.isBlank(resetToken)) {
            System.out.println("service token过期=================");
            return ServerResponse.createByErrorMessage("token过期");
        }
        if (!resetToken.equals(token)) {
            System.out.println("service token错误=================");
            return ServerResponse.createByErrorMessage("token错误");
        }
        System.out.println("4=================");
        //新密码MD5加密
        System.out.println("service 新密码MD5加密=================");
        password = MD5Util.MD5Encode(password, "utf-8");
        int count = userMapper.updatePasswordByToken(email, password);
        if (count > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("更新密码失败");
    }

    @Override
    public ServerResponse queryUserInformation(Integer userId) {
        if (userId == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //查找用户信息
        User user = userMapper.selectByPrimaryKey(userId);
        //用户不存在
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        //返回前密码先置空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessData(user);
    }

    @Override
    public ServerResponse updateUserInformation(Integer userId, String username, String bio, String gender, String city) {
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setBio(bio);
        user.setGender(gender);
        user.setCity(city);
        user.setPassword(null); //禁止修改密码
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0) {
            return ServerResponse.createBySuccessData(user);
        }
        return ServerResponse.createByErrorMessage("更新用户信息失败");
    }

    @Override
    public ServerResponse updateEmail(Integer userId, String email) {
        if (userId == null || StringUtils.isBlank(email)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        int count = userMapper.updateEmail(email, userId);
        if (count > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("更换邮箱失败");
    }

    @Override
    public ServerResponse updateAvatar(User user) {
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //更新用户头像
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("更新头像失败");
    }

    @Override
    public ServerResponse adminLogin(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //密码MD5加密
        password = MD5Util.MD5Encode(password, "utf-8");
        //管理员登陆
        User user = userMapper.adminLogin(username, password);
        if (user != null) {
            //密码置空
            user.setPassword(StringUtils.EMPTY);
            return ServerResponse.createBySuccess("管理员登陆成功", user);
        }
        //判断是否存在管理员
        int count = userMapper.existAdmin();
        if (count > 0) {
            return ServerResponse.createByErrorMessage("管理员用户名或密码错误");
        }
        //第一次登录即为管理员
        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole(Const.Role.ADMIN);
        //admin的email为用户名加邮箱后缀,eg:admin@163.com
        admin.setEmail(admin.getUsername() + "@163.com");
        //admin的avatar为默认
        admin.setAvatar(PropertiesUtil.getProperty("ftp.avatar.default"));
        count = userMapper.insert(admin);
        //获取user的信息
        user = userMapper.selectByPrimaryKey(admin.getId());
        //设置管理员成功，登陆成功
        if (count > 0) {
            user.setPassword(StringUtils.EMPTY);        //密码置空
            return ServerResponse.createBySuccess("设置管理员成功，登陆成功", user);
        }
        //设置管理员失败
        return ServerResponse.createByErrorMessage("设置管理员失败");
    }
}