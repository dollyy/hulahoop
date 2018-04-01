package com.yc.hulahoop.util;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.pojo.User;
import org.directwebremoting.Container;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.servlet.DwrServlet;

import javax.servlet.http.HttpSession;

public class DwrScriptSessionManagerUtil extends DwrServlet {

    public void init(){
        Container container= ServerContextFactory.get().getContainer();
        ScriptSessionManager manager=container.getBean(ScriptSessionManager.class);
        ScriptSessionListener listener=new ScriptSessionListener() {
            @Override
            public void sessionCreated(ScriptSessionEvent scriptSessionEvent) {
                //当用户登录时，将唯一标识userId放到session中
                HttpSession session= WebContextFactory.get().getSession();
                int userId=((User)session.getAttribute(Const.CURRENT_USER)).getId();
                System.out.println("=========a ScriptSession is created!========="+userId);
                scriptSessionEvent.getSession().setAttribute("userId",userId);
            }

            @Override
            public void sessionDestroyed(ScriptSessionEvent scriptSessionEvent) {
                System.out.println("=========a ScriptSession is destroyed!=========");
            }
        };
        manager.addScriptSessionListener(listener);
    }
}
