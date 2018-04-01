<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/3/24
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>strategy</title>
    <link rel="stylesheet" href="../vendors/select2-4.0.6-rc.1/dist/css/select2.min.css" />
    <link rel="stylesheet" href="../font/iconfont.css">
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/strategy.css">
</head>
<body>
<!--side tools-->
<div class="sideTool">
    <span class="iconfont icon-fankui"></span>
    <span class="iconfont icon-fanhuidingbu"></span>
</div>

<jsp:include page="nav.jsp"/>

<!-- catalog -->
<div class="catalog">
        <span class="provTab">
            省份:
            <select class="js-example-basic-single province" name="state">
                <option value="0">全部</option>
            </select>
        </span>
    <span class="durTab">
            时长:
            <select class="js-example-basic-single duration" name="state">
                <option>全部</option>
            </select>
        </span>
</div>

<div class="body">
    <div class="nothing"></div>
    <div class="content"></div>
    <div id="pageNum">
        <div id="page" class="page_div"></div>
    </div>
</div>

<script src="../js/jquery-3.2.1.min.js"></script>
<script src="../vendors/select2-4.0.6-rc.1/dist/js/select2.min.js"></script>
<script src="../js/paging.js"></script>
<script src="../js/strategy.js"></script>
<script src="../js/common.js"></script>
</body>
</html>