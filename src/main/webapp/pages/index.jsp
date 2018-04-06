<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/3/24
  Time: 16:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>index</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/index.css">
</head>
<body>

<jsp:include page="nav.jsp"/>

<div class="banner"></div>
<div class="recommendContainer">
    <span class="title">_为_你_推_荐_</span>
    <div class="recommends"></div>
</div>
<div class="newestContainer">
    <span class="title">_最_新_攻_略_</span>
    <div class="news"></div>
</div>
<div class="hottestContainer">
    <span class="title">_最_热_攻_略_</span>
    <div class="hots"></div>
</div>

<script src="../js/jquery-3.2.1.min.js"></script>
<script src="../js/index.js"></script>
<script src="../js/nav.js"></script>
</body>
</html>