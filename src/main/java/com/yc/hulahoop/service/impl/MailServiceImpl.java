package com.yc.hulahoop.service.impl;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.common.TokenCache;
import com.yc.hulahoop.service.MailService;
import com.yc.hulahoop.util.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("mailService")
public class MailServiceImpl implements MailService {

    @Override
    public ServerResponse sendMail(String recipient) {
        if (StringUtils.isBlank(recipient)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        String code = MailUtil.sendMail(recipient);
        if (!StringUtils.isBlank(code)) {
            return ServerResponse.createBySuccessData(code);
        }
        return ServerResponse.createByErrorMessage("发送邮件失败");
    }

    @Override
    public ServerResponse confirmCode(String recipient, String code, String trueCode) {
        if (StringUtils.isBlank(recipient) || StringUtils.isBlank(code) || StringUtils.isBlank(trueCode)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        if (code.equals(trueCode)) {
            //返回一个token标记当前用户
            String resetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + recipient, resetToken);
            return ServerResponse.createBySuccessData(resetToken);
        }
        //验证码错误
        return ServerResponse.createByErrorMessage("验证码错误");
    }
}