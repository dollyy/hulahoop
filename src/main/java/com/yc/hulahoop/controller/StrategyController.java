package com.yc.hulahoop.controller;

import com.google.common.collect.Maps;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.Strategy;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.FileService;
import com.yc.hulahoop.service.StrategyService;
import com.yc.hulahoop.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/strategy/")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private FileService fileService;


    @RequestMapping(value = "indexInfo.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse indexInfo(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        Integer userId = currentUser == null ? 0 : currentUser.getId();
        return strategyService.indexInfo(userId);
    }

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
        return strategyService.queryStrategyList(pageNum, Const.PAGE_SIZE, cityId, duration);
    }

    /**
     * 更新strategy list
     *
     * @param pageNum  页码
     * @param cityId   城市编号
     * @param duration 时长
     * @return 攻略集合
     */
    @RequestMapping(value = "updateList.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse updateList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "cityId", required = false) Integer cityId,
                                      @RequestParam(value = "duration", required = false) String duration) {
        return strategyService.updateStrategyList(pageNum, Const.PAGE_SIZE, cityId, duration);
    }

    /**
     * 攻略详情
     *
     * @param session    当前用户
     * @param strategyId 攻略id
     * @return 攻略信息
     */
    @RequestMapping(value = "detail.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse detail(HttpSession session, Integer strategyId) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return strategyService.detail(currentUser.getId(), strategyId);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 新增攻略
     *
     * @param session  当前用户
     * @param strategy 新增攻略的信息
     * @return 新增成功/失败
     */
    @RequestMapping(value = "add.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse add(HttpSession session, Strategy strategy) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            strategy.setUserId(currentUser.getId());
            return strategyService.add(strategy);
        }
        //身份校验失败
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
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return strategyService.delete(currentUser.getId(), strategyId);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 修改攻略
     *
     * @param session  当前用户
     * @param strategy 修改攻略的信息
     * @return 修改成功/失败
     */
    @RequestMapping(value = "update.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse update(HttpSession session, Strategy strategy) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            //防止横向越权
            strategy.setUserId(currentUser.getId());
            return strategyService.update(strategy);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 更新点赞/收藏
     *
     * @param session  当前用户
     * @param strategy 攻略信息
     * @return 更新成功/失败
     */
    @RequestMapping(value = "updateForOrCollect.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse updateForOrCollect(HttpSession session, Strategy strategy, Integer status, String type) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return strategyService.updateForOrCollect(currentUser.getId(), strategy, status, type);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 搜索攻略
     *
     * @param content 搜索的值
     * @return 符合条件的strategy的list
     */
    @RequestMapping(value = "search.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse search(String content,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
        return strategyService.search(content, pageNum, Const.PAGE_SIZE);
    }

    /**
     * 查看我的攻略
     *
     * @param session 当前用户
     * @return strategy的list
     */
    @RequestMapping(value = "queryUserStrategy.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse queryUserStrategy(HttpSession session) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return strategyService.queryUserStrategy(currentUser.getId());
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 查看我的收藏
     *
     * @param session 当前用户
     * @param pageNum 页码
     * @param orderBy 排序
     * @return strategy的list
     */
    @RequestMapping(value = "queryUserCollection.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse queryUserCollection(HttpSession session,
                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            return strategyService.queryUserCollection(currentUser.getId(), Const.PAGE_SIZE, pageNum, orderBy);
        }
        //身份校验失败
        return serverResponse;
    }

    /**
     * 上传攻略图片
     *
     * @param file 攻略图片
     * @return 添加成功/失败
     */
    @RequestMapping(value = "richtext_img_upload.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> richtextImgUpload(HttpSession session, MultipartFile file) {
        //返回的结果集
        Map<String, Object> resultMap = Maps.newHashMap();
        //身份校验
        ServerResponse serverResponse = isLogin(session);
        //身份校验成功
        if (serverResponse.isSuccess()) {
            //上传文件
            String uploadFileName = fileService.upload(file, Const.STRATEGY);
            //文件上传失败
            if (StringUtils.isBlank(uploadFileName)) {
                resultMap.put("status", 0);
                resultMap.put("msg", "上传文件失败");
                return resultMap;
            }
            //文件上传成功
            resultMap.put("status", 1);
            resultMap.put("msg", "上传文件成功");
            String path = PropertiesUtil.getProperty("ftp.server.http.prefix") + PropertiesUtil.getProperty("ftp.strategy")
                    + uploadFileName;
            resultMap.put("file_path", path);
            return resultMap;
        }
        //身份校验失败
        resultMap.put("status", -2);
        resultMap.put("msg", "用户未登录");
        return resultMap;
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