<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/3/24
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>nav</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/nav.css">
    <link rel="stylesheet" href="../font/iconfont.css">
</head>
<body>
    <div id="bg"></div>
    <!-- 导航 -->
    <nav>
        <div class="nav">
            <a href="index.html"><img src="../images/icons/icon1.jpg"></a>
            <div class="serach">
                <input type="text" id="searchInp">
                <span class="iconfont icon-fangdajing"></span>
            </div>
            <span id="sign">
                    <span id="signin">登录</span>
                    <span id="signup">注册</span>
                </span>
            <span id="userIcon">
                    <img src="">
                    <span id="username"></span>
                    <span id="msgTip">
                        <span class="iconfont icon-lingdang"></span>
                        <span id="msgDot"></span>
                    </span>
                    <span id="logout">退出</span>
                </span>
        </div>
    </nav>
    <!-- 登录/注册 -->
    <div class="signContainer">
        <span id="signWarn"></span>
        <!--登录容器-->
        <form id="signinContainer">
            <span id="goUp">去注册</span>
            <input type="text" id="inName" name="username" placeholder="Username or Telephone"><br>
            <input type="text" id="inPwd" name="password" placeholder="Password"><br>
            <span id="pwdOperation">
                    <input type="checkbox" id="savePwd">记住密码
                    <a href="resetPassword.html" id="resetPwd">忘记密码 </a>
                </span><br>
            <input type="button" id="inBtn" value="登 录" disabled>
        </form>
        <!--注册容器-->
        <form id="signupContainer">
            <span id="goIn">去登录</span>
            <input type="text" id="upName" name="username" placeholder="Username"><br>
            <input type="text" id="upPwd" name="password" placeholder="Password"><br>
            <input type="text" id="upPwdRe" name="passwordRe" placeholder="Password again"><br>
            <input type="text" id="upPhone" name="phone" value="18360868799" placeholder="Phone number"><br>
            <input type="button" id="upBtn" value="注 册" disabled>
        </form>
    </div>

    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/nav.js"></script>
    <script src="../js/common.js"></script>
</body>
</html>
