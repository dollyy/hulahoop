package com.yc.hulahoop.controller;

import com.google.common.collect.Maps;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.FileService;
import com.yc.hulahoop.service.UserService;
import com.yc.hulahoop.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Yang Chen on 18-2-28.
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    /**
     * 验证注册时的参数(username / email)
     *
     * @param val  验证的值
     * @param type username / email
     * @return 验证成功或失败
     */
    @RequestMapping(value = "verify.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<String> verify(String val, String type) {
        return userService.verify(val, type);
    }

    /**
     * 注册
     *
     * @param user 用户信息
     * @return 注册成功/失败
     */
    @RequestMapping(value = "register.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse<String> register(User user) {
        return userService.register(user);
    }

    /**
     * 登录
     *
     * @param type     username/email
     * @param val      用户名 / 手机号
     * @param password 密码
     * @param session  存放当前用户
     * @return 登录成功/失败
     */
    @RequestMapping(value = "login.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse login(String type, String val, String password, HttpSession session) {
        ServerResponse serverResponse = userService.login(type, val, password);
        if (serverResponse.isSuccess()) {   //登陆成功
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }


    /**
     * 退出登录
     *
     * @param session 当前用户
     * @return 退出
     */
    @RequestMapping(value = "logout.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse logout(HttpSession session) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
            return ServerResponse.createBySuccessMessage("退出登录成功");
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 登录状态重置密码
     *
     * @param session     当前用户
     * @param passwordNew 新密码
     * @return 重置密码正确/错误
     */
    @RequestMapping(value = "resetPassword.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return userService.resetPassword(passwordOld, passwordNew, currentUser);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 忘记密码下的修改密码
     *
     * @param passwordNew 新密码
     * @param email       邮箱
     * @return 修改成功/失败
     */
    @RequestMapping(value = "updatePassword.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse updatePassword(String passwordNew, String email) {
        return ServerResponse.createBySuccessMessage("重置密码成功");
        //return userService.updatePassword(passwordNew);
    }

    /**
     * 获取用户信息
     *
     * @param session 当前用户
     * @return 当前用户的用户信息
     */
    @RequestMapping(value = "queryUserInformation.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse queryUserInformation(HttpSession session) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return userService.queryUserInformation(currentUser.getId());
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 更新个人信息
     *
     * @param session 当前用户
     * @param user    当前用户更新的用户信息
     * @return 更新成功/失败
     */
    @RequestMapping(value = "updateUserInformation.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse updateUserInformation(HttpSession session, User user) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            //防止横向越权
            user.setId(currentUser.getId());
            //禁止修改的字段
            user.setPassword(null);
            user.setRole(null);
            user.setCreateTime(null);
            //todo 更新手机号

            serverResponse = userService.updateUserInformation(user);
            //将修改后的信息存进session
            if (serverResponse.isSuccess()) {
                session.setAttribute(Const.CURRENT_USER, user);
            }
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 上传头像图片
     *
     * @param session 当前用户
     * @param file    头像文件
     * @return 更新成功/失败
     */
    @RequestMapping(value = "updateAvatar.action", method = RequestMethod.POST)
    @ResponseBody
    private Map updateAvatar(HttpSession session, MultipartFile file) {
        System.out.println("======"+file);
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //返回的结果集
        Map<String, Object> resultMap = Maps.newHashMap();
        //身份校验成功
        if (serverResponse.isSuccess()) {
            //上传头像
            String uploadFileName = fileService.upload(file, Const.AVATAR);
            //上传失败
            if (StringUtils.isBlank(uploadFileName)) {
                resultMap.put("status", 0);
                resultMap.put("msg", "上传文件失败");
                return resultMap;
            }
            //上传成功
            String path = PropertiesUtil.getProperty("ftp.server.http.prefix") + PropertiesUtil.getProperty("ftp.avatar")
                    + uploadFileName;
            User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
            currentUser.setAvatar(path);
            serverResponse= userService.updateAvatar(currentUser);
            if(serverResponse.isSuccess()){
                resultMap.put("status", 1);
                resultMap.put("msg", "上传文件成功");
                resultMap.put("file_path", path);
                return resultMap;
            }
            resultMap.put("status", 0);
            resultMap.put("msg", "头像更新失败");
            return resultMap;
        }
        //身份校验失败
        resultMap.put("status", -2);
        resultMap.put("msg", "用户未登录");
        return resultMap;
    }


    /**
     * 检查用户是否登录
     * @param session 当前用户
     * @return 用户信息/未登录
     */
    @RequestMapping(value = "isLogin.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse isLogin(HttpSession session) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NEED_LOGIN.getCode(),
                    Const.ResponseCode.NEED_LOGIN.getDescription());
        }
        return ServerResponse.createBySuccessData(currentUser);
    }
}