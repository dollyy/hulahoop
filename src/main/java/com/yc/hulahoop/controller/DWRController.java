package com.yc.hulahoop.controller;

import com.yc.hulahoop.service.DwrRecordService;
import com.yc.hulahoop.util.DwrScriptSessionManagerUtil;
import org.directwebremoting.*;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.Collection;

@Controller("dWRController")
//需要标识成RemoteProxy才能被dwr调用
@RemoteProxy
public class DWRController {

    @Autowired
    DwrRecordService dwrRecordService;

    /*@RemoteMethod*/

    public void onPageLoad(int userId) {
        ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
        scriptSession.setAttribute("userId", userId);
        DwrScriptSessionManagerUtil dwrScriptSessionManagerUtil = new DwrScriptSessionManagerUtil();
        dwrScriptSessionManagerUtil.init();
    }

    //推送评论
    public void publishComment(final String message, final Integer userId) {
        System.out.println("========DWRController" + message + "," + userId);
        //存入数据库
        if(!dwrRecordService.insertComment(message, userId)){
            return;
        }
        //dwr推送
        Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
            @Override
            public boolean match(ScriptSession scriptSession) {
                Object userObj = scriptSession.getAttribute("userId");
                return userObj != null && userObj.equals(userId);
            }
        }, new Runnable() {
            private ScriptBuffer scriptBuffer = new ScriptBuffer();

            @Override
            public void run() {
                scriptBuffer.appendCall("showMessage", message);
                Collection<ScriptSession> sessions = Browser.getTargetSessions();
                for (ScriptSession scriptSession : sessions) {
                    scriptSession.addScript(scriptBuffer);
                }
            }
        });
    }

    //推送反馈
    public void publishFeed(final Integer userId){
        System.out.println("=================publishFeed"+userId);
        //dwr推送
        Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
            @Override
            public boolean match(ScriptSession scriptSession) {
                Object userObj = scriptSession.getAttribute("userId");
                return userObj != null && userObj.equals(userId);
            }
        }, new Runnable() {
            private ScriptBuffer scriptBuffer = new ScriptBuffer();

            @Override
            public void run() {
                scriptBuffer.appendCall("showMessage", "message");
                System.out.println("---------------------------");
                Collection<ScriptSession> sessions = Browser.getTargetSessions();
                for (ScriptSession scriptSession : sessions) {
                    scriptSession.addScript(scriptBuffer);
                }
            }
        });
    }

}