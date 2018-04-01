package com.yc.hulahoop.controller;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.Comment;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/comment/")
public class CommentController {

    @Autowired
    CommentService commentService;

    /**
     * 查看攻略的评论
     *
     * @param session    当前用户
     * @param level 攻略level
     * @return comment的list
     */
    @RequestMapping(value = "list.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse list(HttpSession session, Integer level) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            return commentService.listByLevel(level);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 添加评论
     *
     * @param session 当前用户
     * @param comment 评论的内容
     * @return 添加评论成功/失败
     */
    @RequestMapping(value = "add.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse add(HttpSession session, Comment comment) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            //防止横向越权
            comment.setUserId(currentUser.getId());
            return commentService.add(comment);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 回复评论
     *
     * @param session 当前用户
     * @param comment 评论的内容
     * @return 回复评论成功/失败
     */
    @RequestMapping(value = "reply.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse reply(HttpSession session, Comment comment) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            //防止横向越权
            comment.setUserId(currentUser.getId());
            return commentService.reply(comment);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 支持/反对
     *
     * @param session 当前用户
     * @param id      评论的id
     * @param type    for/against
     * @param number  数目
     * @return 更新成功/失败
     */
    @RequestMapping(value = "forOrAgainstComment.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse forOrAgainstComment(HttpSession session, Integer id, String type, Integer number) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return commentService.forOrAgainstComment(currentUser.getId(), id, type, number);
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

}
