package com.yc.hulahoop.controller;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.Strategy;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/strategy/")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    //todo content为json,如果内容含有','怎么办

    /**
     * 列出所有攻略
     *
     * @param pageNum  页码
     * @param cityId   城市编号(如果参数没有值就会默认赋值null,所以要用int的包装类)
     * @param duration 时长
     * @return 攻略集合
     */
    @RequestMapping(value = "list.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "cityId", required = false) Integer cityId,
                                @RequestParam(value = "duration", required = false) String duration) {
        return strategyService.queryStrategyList(pageNum, cityId, duration);
    }

    /**
     * 攻略详情 todo Vo
     *
     * @param session    当前用户
     * @param strategyId 攻略id
     * @return 攻略信息
     */
    @RequestMapping(value = "detail.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse detail(HttpSession session, Integer strategyId) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        return strategyService.detail(strategyId);
    }

    /**
     * 新增攻略 todo 为main_img赋值
     *
     * @param session  当前用户
     * @param strategy 新增攻略的信息
     * @return 新增成功/失败
     */
    @RequestMapping(value = "add.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse add(HttpSession session, Strategy strategy) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        strategy.setUserId(currentUser.getId());
        return strategyService.add(strategy);
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
    private ServerResponse delete(HttpSession session, Integer strategyId) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        return strategyService.delete(strategyId, currentUser.getId());
    }

    /**
     * 修改攻略 todo 为main_img赋值
     *
     * @param session  当前用户
     * @param strategy 修改攻略的信息
     * @return 修改成功/失败
     */
    @RequestMapping(value = "update.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse update(HttpSession session, Strategy strategy) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        //防止横向越权
        strategy.setUserId(currentUser.getId());
        return strategyService.update(strategy);
    }

    /**
     * 搜索攻略
     *
     * @param type username/strategy_name
     * @param val  搜索的值
     * @return 符合条件的strategy的list
     */
    @RequestMapping(value = "search.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse search(String type, String val,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "2") int pageSize) {
        return strategyService.search(type, val, pageNum, pageSize);
    }

    /**
     * 查看我的攻略
     *
     * @param session 当前用户
     * @return strategy的list
     */
    @RequestMapping(value = "queryUserStrategy.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse queryUserStrategy(HttpSession session) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        return strategyService.queryUserStrategy(currentUser.getId());
    }

    /**
     * 查看我的收藏
     *
     * @param session  当前用户
     * @param cityId   城市的id
     * @param pageNum  页码
     * @param pageSize 一页显示的个数
     * @param orderBy  排序
     * @return strategy的list
     */
    @RequestMapping(value = "queryUseCollection.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse queryUseCollection(HttpSession session, Integer cityId,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        //检查用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {     //用户未登录
            return ServerResponse.createByErrorMessage(Const.NOT_LOGIN);
        }
        return strategyService.queryUserCollection(currentUser.getId(), cityId, pageNum, pageSize, orderBy);
    }

}