package com.yc.hulahoop.controller.backend;

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

@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param session  存放当前对象
     * @return 登陆成功/失败
     */
    @RequestMapping(value = "login.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse login(String username, String password, HttpSession session) {
        ServerResponse serverResponse = userService.adminLogin(username, password);
        if (serverResponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    /**
     * 退出登录
     *
     * @param session 当前用户
     * @return 管理员退出登录成功/未登录/非管理员
     */
    @RequestMapping(value = "logout.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse logout(HttpSession session) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NEED_LOGIN.getCode(),
                    Const.ResponseCode.NEED_LOGIN.getDescription());
        }
        //检查当前用户是否为管理员
        if (currentUser.getRole() != Const.Role.ADMIN) {    //非管理员
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NON_ADMIN.getCode(),
                    Const.ResponseCode.NON_ADMIN.getDescription());
        }
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("管理员退出登录成功");
    }

}
