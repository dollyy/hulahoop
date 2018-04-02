package com.yc.hulahoop.controller;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.DwrRecordService;
import com.yc.hulahoop.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/feedback/")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    DwrRecordService dwrRecordService;


    @RequestMapping(value = "detail.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse detail(HttpSession session, String level) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            return feedbackService.detail(level);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 按用户列出反馈信息
     *
     * @param session 当前用户
     * @return feedbackList
     */
    @RequestMapping(value = "listByUser.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse listByUser(HttpSession session,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return feedbackService.listByUser(currentUser.getId(), pageNum);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 反馈信息
     *
     * @param session 当前用户
     * @param content 反馈信息的内容
     * @return 反馈成功/失败
     */
    @RequestMapping(value = "add.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse add(HttpSession session, String content) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return feedbackService.add(currentUser.getId(), content);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 回复信息
     *
     * @param session  当前用户
     * @param content  信息的内容
     * @param parent   该回复的父级level
     * @param sequence 回复的次序
     * @return 回复成功/失败
     */
    @RequestMapping(value = "reply.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse reply(HttpSession session, String content, Integer parent, Integer sequence) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return feedbackService.reply(currentUser.getId(), content, parent, sequence, Const.ADMIN_ID);
        }
        //身份校验失败
        return serverResponse;
    }

    //检查用户是否登录
    private ServerResponse<Object> isLogin(HttpSession session) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NEED_LOGIN.getCode(),
                    Const.ResponseCode.NEED_LOGIN.getDescription());
        }
        return ServerResponse.createBySuccess();
    }


    @RequestMapping(value = "queryNotice.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse queryNotice(HttpSession session) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return feedbackService.queryNotice(currentUser.getId());
        }
        //身份校验失败
        return serverResponse;
    }

    //将user的feedback的status从1置为0
    @RequestMapping(value = "updateFeedStatus.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse updateFeedStatus(HttpSession session, String level) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return feedbackService.updateFeedStatus(Const.ADMIN_ID, currentUser.getId(), level, 0);
        }
        //身份校验失败
        return serverResponse;
    }

    @RequestMapping(value = "updateDwr.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse updateDwr(HttpSession session, Integer dwrId) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            return dwrRecordService.update(dwrId);
        }
        //身份校验失败
        return serverResponse;
    }
}
