package com.yc.hulahoop.controller.backend;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.HelpInfo;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.FileService;
import com.yc.hulahoop.service.HelpInfoService;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/manage/helpInfo/")
public class HelpInfoManageController {

    @Autowired
    private HelpInfoService helpInfoService;

    @Autowired
    private FileService fileService;

    /**
     * 列出所有的帮助信息
     *
     * @param session 当前用户
     * @return helpInfo的list
     */
    @RequestMapping(value = "list.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return helpInfoService.list(pageNum);
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

    /**
     * 搜索帮助信息
     *
     * @param session 当前用户
     * @param content 搜索的内容
     * @return 删除帮助信息成功/失败
     */
    @RequestMapping(value = "search.action", method = RequestMethod.GET)
    @ResponseBody
    private ServerResponse search(HttpSession session, String content,
                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功
            return helpInfoService.search(pageNum, content);
        }
        return serverResponse;
    }


    @RequestMapping(value = "richtext_img_upload.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> richtextImgUpload(HttpSession session, MultipartFile file) {
        //返回的结果集
        Map<String, Object> resultMap = new HashMap();
        //身份校验
        ServerResponse serverResponse = isAdmin(session);
        if (serverResponse.isSuccess()) {   //身份校验成功//上传文件
            String uploadFileName = fileService.upload(file, Const.HELPINFO);
            //文件上传失败
            if (StringUtils.isBlank(uploadFileName)) {
                resultMap.put("status", 0);
                resultMap.put("msg", "上传文件失败");
                return resultMap;
            }
            //文件上传成功
            resultMap.put("status", 1);
            resultMap.put("msg", "上传文件成功");
            String path = PropertiesUtil.getProperty("ftp.server.http.prefix") + PropertiesUtil.getProperty("ftp.helpInfo")
                    + uploadFileName;
            resultMap.put("file_path", path);
            return resultMap;
        }
        //身份校验失败
        resultMap.put("status", -2);
        resultMap.put("msg", "用户未登录");
        return resultMap;
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
