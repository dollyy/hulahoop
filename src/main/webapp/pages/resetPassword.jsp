<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/3/24
  Time: 15:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>重置密码</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/resetPassword.css">
    <link rel="stylesheet" href="../font/iconfont.css">
</head>
<body>

<jsp:include page="nav.jsp"/>

<div class="resetContainer">
    <div class="top">
        <div class="title">找回密码</div>
        <ul class="catalog">
            <li class="success">
                <span class="iconfont icon-jiangluo"></span>
                确认帐号
                <span class="iconfont icon-qifei"></span>
            </li>
            <li>
                <span class="iconfont icon-jiangluo"></span>
                重置密码
                <span class="iconfont icon-qifei"></span>
            </li>
            <li>
                <span class="iconfont icon-jiangluo"></span>
                成功
                <span class="iconfont icon-qifei"></span>
            </li>
        </ul>
    </div>
    <div class="error"></div>
    <div class="content">
        <div class="step step1">
            <div>
                <input type="text" id="email" placeholder="Email">
                <input type="button" value="获取验证码" id="queryCode" disabled>
            </div>
            <input type="text" id="code" placeholder="Message Code">
            <input type="button" id="confirmBtn" value="确 认" disabled>
        </div>
        <div class="step step2">
            <input type="password" id="password" placeholder="Password">
            <input type="password" id="passwordRe" placeholder="Password Again">
            <input type="button" id="resetBtn" value="确 认" disabled>
        </div>
        <div class="step step3">重置密码成功，<span id="counttime">3</span>s后返回首页</div>
    </div>
</div>

<script src="../js/jquery-3.2.1.min.js"></script>
<script src="../js/resetPassword.js"></script>

</body>
</html>
