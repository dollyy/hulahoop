<%@ page import="com.yc.hulahoop.pojo.User" %><%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/3/24
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>nav</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/nav.css">
    <link rel="stylesheet" href="../font/iconfont.css">
</head>
<body onload="dwr.engine.setActiveReverseAjax(true);dwr.engine.setNotifyServerOnPageUnload(true);">
<div id="bg"></div>
<!-- 导航 -->
<nav>
    <div class="nav">
        <a href="index.jsp"><img src="../images/logo.png"></a>
        <div class="search">
            <input type="text" id="searchInp">
            <span class="iconfont icon-fangdajing"></span>
        </div>
        <a href="strategy.jsp" id="strategy">攻略</a>
        <%
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
        %>
        <span id="sign">
            <span id="signin">登录</span>
            <span id="signup">注册</span>
        </span>
        <%
        } else {
        %>
        <span id="userIcon">
            <a href="center.jsp"><img id="navAvatar" src='<%=user.getAvatar()%>'></a>
            <span id="username"><%=user.getUsername()%></span>
            <span id="msgTip">
                <span class="iconfont icon-lingdang"></span>
                <span id="msgDot"></span>
            </span>
            <a id="uploadStrategy" href="uploadStrategy.jsp">上传攻略</a>
            <span id="logout">退出</span>
        </span>
        <%
            }
        %>
    </div>
</nav>

<div class="signContainer">
    <span id="signWarn"></span>
    <!--登录容器-->
    <form id="signinContainer">
        <a id="goUp">去注册</a>
        <input type="text" id="inName" name="username" placeholder="Username or email"><br>
        <input type="password" id="inPwd" name="password" placeholder="Password"><br>
        <span id="pwdOperation">
                <a href="resetPassword.jsp" id="resetPwd">忘记密码 </a>
            </span><br>
        <input type="button" id="inBtn" value="登 录" disabled>
    </form>
    <!--注册容器-->
    <form id="signupContainer">
        <a id="goIn">去登录</a>
        <input type="text" id="upName" name="username" placeholder="Username"><br>
        <input type="password" id="upPwd" name="password" placeholder="Password"><br>
        <input type="password" id="upPwdRe" name="passwordRe" placeholder="Password again"><br>
        <input type="text" id="upEmail" name="email" placeholder="Email"><br>
        <input type="button" id="upBtn" value="注 册" disabled>
    </form>
</div>

<!-- dwr的js支持库 -->
<script type='text/javascript' src='/dwr/engine.js'></script>
<script type='text/javascript' src='/dwr/interface/dwrMessage.js'></script>
<script type='text/javascript' src='/dwr/util.js'></script>

<script src="../js/jquery-3.2.1.min.js"></script>
<script src="../js/nav.js"></script>
<script src="../js/common.js"></script>
</body>
</html>