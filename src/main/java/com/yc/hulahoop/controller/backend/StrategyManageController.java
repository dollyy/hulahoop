package com.yc.hulahoop.controller.backend;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/strategy/")
public class StrategyManageController {

    @Autowired
    private StrategyService strategyService;

    /**
     * 列出所有攻略
     *
     * @param session  当前用户
     * @param pageNum  页码
     * @param cityId   城市编号
     * @param duration 时长
     * @return 攻略集合
     */
    @RequestMapping(value = "list.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "cityId", required = false) Integer cityId,
                                @RequestParam(value = "duration", required = false) String duration) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return strategyService.queryStrategyList(pageNum, Const.PAGE_SIZE_ADMIN, cityId, duration);
        }
        return serverResponse;
    }

    /**
     * 查看攻略
     *
     * @param session    当前用户
     * @param strategyId 攻略id
     * @return 攻略信息
     */
    @RequestMapping(value = "detail.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse detail(HttpSession session, HttpServletRequest request, Integer strategyId) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return strategyService.detail(request, currentUser.getId(), strategyId);
        }
        return serverResponse;
    }

    /**
     * 搜索攻略
     *
     * @param content  搜索的内容
     * @return 符合条件的strategy的list
     */
    @RequestMapping(value = "search.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse search(HttpSession session, String content,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return strategyService.search(content, pageNum, Const.PAGE_SIZE_ADMIN);
        }
        return serverResponse;
    }

    /**
     * 删除攻略
     *
     * @param session    当前用户
     * @param strategyId 攻略id
     * @return 删除成功/失败
     */
    @RequestMapping(value = "delete.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse delete(HttpSession session, String strategyId) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return strategyService.delete(currentUser.getId(), strategyId);
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
