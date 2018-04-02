package com.yc.hulahoop.controller.backend;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    private ServerResponse list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return feedbackService.list(pageNum);
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
    private ServerResponse reply(HttpSession session, String content, Integer parent, Integer sequence,
                                 Integer receiveId) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return feedbackService.reply(currentUser.getId(), content, parent, sequence, receiveId);
        }
        return serverResponse;
    }

    @RequestMapping(value = "queryNotice.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse queryNotice(HttpSession session) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return feedbackService.queryNotice(currentUser.getId());
        }
        return serverResponse;
    }

    //将admin的feedback的status从1置为0
    @RequestMapping(value = "updateFeedStatus.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse updateFeedStatus(HttpSession session, String level, Integer receiveId) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return feedbackService.updateFeedStatus(receiveId, currentUser.getId(), level, 0);
        }
        //身份校验失败
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

    /**
     *搜索反馈信息
     * @param session 当前用户
     * @param content 搜索的内容
     * @param pageNum 页码
     * @return FeedbackInfo
     */
    @RequestMapping(value = "search.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse search(HttpSession session, String content,
                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return feedbackService.search(content, pageNum);
        }
        return serverResponse;
    }

    private ServerResponse<Object> isAdmin(HttpSession session) {
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
        return ServerResponse.createBySuccess();
    }

}
