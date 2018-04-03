package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;

public interface MailService {

    ServerResponse sendMail(String recipient);

    ServerResponse confirmCode(String recipient, String code, String trueCode);

}