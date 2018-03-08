package com.yc.hulahoop.controller;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.FeedbackInfo;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/feedback/")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 反馈信息
     *
     * @param session 当前用户
     * @param title   反馈信息的标题
     * @param content 反馈信息的内容
     * @return 反馈成功/失败
     */
    @RequestMapping(value = "add.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse add(HttpSession session, String title, String content) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        FeedbackInfo feedbackInfo = new FeedbackInfo();
        feedbackInfo.setUserId(currentUser.getId());
        feedbackInfo.setTitle(title);
        feedbackInfo.setContent(content);
        feedbackInfo.setParent(0);
        feedbackInfo.setSequence(1);
        return feedbackService.add(feedbackInfo);
    }

    /**
     * 回复信息
     *
     * @param session  当前用户
     * @param title    信息的标题
     * @param content  信息的内容
     * @param parent   该回复的父级level
     * @param sequence 回复的次序
     * @return 回复成功/失败
     */
    @RequestMapping(value = "reply.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse reply(HttpSession session, String title, String content, int parent, int sequence) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        FeedbackInfo feedbackInfo = new FeedbackInfo();
        feedbackInfo.setUserId(currentUser.getId());
        feedbackInfo.setTitle(title);
        feedbackInfo.setContent(content);
        feedbackInfo.setParent(parent);
        feedbackInfo.setSequence(sequence + 1);
        feedbackInfo.setLevel(parent + "." + (sequence + 1));
        return feedbackService.reply(feedbackInfo);
    }

}
