package com.yc.hulahoop.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {

    public static String sendMail(String recipient) {
        Properties prop = new Properties();
        prop.put("mail.host", PropertiesUtil.getProperty("mail.host"));
        prop.put("mail.transport.protocol", PropertiesUtil.getProperty("mail.transport.protocol"));
        prop.put("mail.smtp.auth", true);
        //生成随机的6位数验证码
        Integer code = (int) ((Math.random() * 9 + 1) * 100000);
        //使用java发送邮件
        //1.创建sesssion
        Session session = Session.getInstance(prop);
        //开启session的调试模式，可以查看当前邮件发送状态
        session.setDebug(true);
        try {
            //2.通过session获取Transport对象
            Transport ts = session.getTransport();
            //3.通过邮件用户名密码链接
            ts.connect(PropertiesUtil.getProperty("mail.from"), PropertiesUtil.getProperty("mail.code"));
            //4.创建邮件
            Message msg = createSimpleMail(session, recipient, code);
            //5.发送电子邮件
            ts.sendMessage(msg, msg.getAllRecipients());
            return code.toString();
        } catch (MessagingException e) {
            e.printStackTrace();
            return "";
        }
    }

    //创建邮件
    public static MimeMessage createSimpleMail(Session session, String recipient, Integer code) throws MessagingException {
        //创建邮件对象
        MimeMessage mimeMessage = new MimeMessage(session);
        //设置发件人
        mimeMessage.setFrom(new InternetAddress(PropertiesUtil.getProperty("mail.from")));
        //设置收件人
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        //设置邮件的标题
        mimeMessage.setSubject(PropertiesUtil.getProperty("mail.subject"));
        //设置邮件的内容
        mimeMessage.setContent(PropertiesUtil.getProperty("mail.content") + code, "text/html;charset=UTF-8");

        return mimeMessage;
    }

}