package com.yc.hulahoop.controller;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Yang Chen on 18-2-28.
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 验证注册时的参数
     *
     * @param val  验证的值
     * @param type username / phone
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
     * @param type     username/phone
     * @param val      用户名 / 手机号
     * @param password 密码
     * @param session  存放当前用户
     * @return 登录成功/失败
     */
    @RequestMapping(value = "login.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse login(String type, String val, String password, HttpSession session) {
        ServerResponse serverResponse = userService.login(type, val, password);
        if (serverResponse.isSuccess()) {
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
    @RequestMapping(value = "logout.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse logout(HttpSession session) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出登录成功");
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
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        return userService.resetPassword(passwordOld, passwordNew, currentUser);
    }

    /**
     * 获取用户信息
     *
     * @param session 当前用户
     * @return 当前用户的用户信息
     */
    @RequestMapping(value = "queryUserInformation.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse queryUserInformation(HttpSession session) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        return userService.queryUserInformation(currentUser.getId());
    }

    /**
     * 更新个人信息  todo 1.上传头像图片
     *
     * @param session 当前用户
     * @param user    当前用户更新的用户信息
     * @return 更新成功/失败
     */
    @RequestMapping(value = "updateUserInformation.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse updateUserInformation(HttpSession session, User user) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        //防止横向越权
        user.setId(currentUser.getId());
        //禁止修改的字段
        user.setPassword(null);
        user.setRole(null);
        user.setCreateTime(null);
        //todo 2.手机号可不可以更新???

        ServerResponse serverResponse = userService.updateUserInformation(user);
        //将修改后的信息存进session
        if (serverResponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, user);
        }
        return serverResponse;
    }

}