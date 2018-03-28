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
    private ServerResponse detail(HttpSession session, Integer strategyId) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return strategyService.detail(strategyId);
        }
        return serverResponse;
    }

    /**
     * 搜索攻略
     *
     * @param type username/strategy_name
     * @param val  搜索的值
     * @return 符合条件的strategy的list
     */
    @RequestMapping(value = "search.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse search(HttpSession session, String type, String val,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return strategyService.search(type, val, pageNum);
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

    /**
     * 富文本上传图片
     *
     * @param session  当前用户
     * @param file     上传的图片
     * @param request  request
     * @param response response
     * @return 上传成功/失败
     */
    @RequestMapping(value = "richtxt_img_upload.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse richtxtImgUpload(HttpSession session, MultipartFile file,
                                            HttpServletRequest request, HttpServletResponse response) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            //todo upload
            return null;
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
