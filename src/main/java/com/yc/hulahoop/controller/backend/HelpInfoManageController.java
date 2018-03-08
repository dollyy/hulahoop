package com.yc.hulahoop.controller.backend;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.HelpInfo;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.HelpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/helpInfo/")
public class HelpInfoManageController {

    @Autowired
    private HelpInfoService helpInfoService;

    /**
     * 列出所有的帮助信息
     *
     * @param session 当前用户
     * @return helpInfo的list
     */
    @RequestMapping(value = "list.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse list(HttpSession session) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return helpInfoService.list();
        }
        return serverResponse;
    }

    /**
     * 新增帮助信息
     *
     * @param session  当前用户
     * @param helpInfo 帮助信息
     * @return 新增帮助信息成功/失败
     */
    @RequestMapping(value = "add.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse add(HttpSession session, HelpInfo helpInfo) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return helpInfoService.add(helpInfo);
        }
        return serverResponse;
    }

    /**
     * 更新帮助信息
     *
     * @param session  当前用户
     * @param helpInfo 帮助信息
     * @return 更新帮助信息成功/失败
     */
    @RequestMapping(value = "update.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse update(HttpSession session, HelpInfo helpInfo) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return helpInfoService.update(helpInfo);
        }
        return serverResponse;
    }

    /**
     * 删除帮助信息
     *
     * @param session    当前用户
     * @param helpInfoId 帮助信息的id
     * @return 删除帮助信息成功/失败
     */
    @RequestMapping(value = "delete.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse delete(HttpSession session, Integer helpInfoId) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return helpInfoService.delete(helpInfoId);
        }
        return serverResponse;
    }

    private ServerResponse<Object> isAdmin(HttpSession session) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        //检查当前用户是否为管理员
        if (currentUser.getRole() != Const.Role.ADMIN) {    //非管理员
            return ServerResponse.createByErrorMessage(Const.NON_ADMIN);
        }
        return ServerResponse.createBySuccess();
    }

}
