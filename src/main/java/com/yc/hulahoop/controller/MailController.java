package com.yc.hulahoop.controller;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/mail/")
public class MailController {

    @Autowired
    MailService mailService;

    /**
     * 发送邮件
     * @param recipient 收件人
     * @return 发送成功/失败
     */
    @RequestMapping(value = "sendMail.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse sendMail(HttpSession session, String recipient){
        ServerResponse serverResponse = mailService.sendMail(recipient);
        //邮件发送成功
        if(serverResponse.isSuccess()){
            //将code存进session
            session.setAttribute(recipient, serverResponse.getData());
            //todo test 设置code的有效期为15min
            session.setMaxInactiveInterval(900);
            return ServerResponse.createBySuccess();    //不能将验证码返回
        }
        return serverResponse;
    }


    @RequestMapping(value = "confirmCode.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse confirmCode(HttpSession session, String recipient, String code){
        String trueCode=(String)session.getAttribute(recipient);
        //验证码超过15min失效
        if(trueCode == null){
            return ServerResponse.createByErrorMessage("验证码失效");
        }
        return mailService.confirmCode(recipient, code, trueCode);
    }

}