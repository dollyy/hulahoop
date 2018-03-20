package com.yc.hulahoop.controller.backend;

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
@RequestMapping("/manage/feedback/")
public class FeedbackManageController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 列出所有的反馈信息
     *
     * @param session 当前用户
     * @return feedback的list
     */
    @RequestMapping(value = "list.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse list(HttpSession session) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return feedbackService.list();
        }
        return serverResponse;
    }

    /**
     * 反馈信息
     *
     * @param session  当前用户
     * @param content  信息的内容
     * @param parent   该回复的父级level
     * @param sequence 回复的次序
     * @return 回复成功/失败
     */
    @RequestMapping(value = "reply.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse reply(HttpSession session, String content, int parent, int sequence) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            FeedbackInfo feedbackInfo = new FeedbackInfo();
            feedbackInfo.setUserId(currentUser.getId());
            feedbackInfo.setContent(content);
            feedbackInfo.setParent(parent);
            feedbackInfo.setSequence(sequence + 1);
            feedbackInfo.setLevel(parent + "." + (sequence + 1));
            return feedbackService.reply(feedbackInfo);
        }
        return serverResponse;
    }

    /**
     * 查看反馈信息
     *
     * @param session 当前用户
     * @param level   反馈信息的level
     * @return FeedbackInfo
     */
    @RequestMapping(value = "detail.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse detail(HttpSession session, String level) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return feedbackService.detail(level);
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
