<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/3/31
  Time: 14:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>搜索攻略</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/search.css">
</head>
<body>

<!--side tools-->
<div class="sideTool">
    <span class="iconfont icon-fankui"></span>
    <span class="iconfont icon-fanhuidingbu"></span>
</div>

<jsp:include page="nav.jsp"/>

<div class="nothing"></div>
<div class="searchContainer"></div>
<div id="pageNum">
    <div id="page" class="page_div"></div>
</div>

<script src="../js/jquery-3.2.1.min.js"></script>
<script src="../js/paging.js"></script>
<script src="../js/common.js"></script>
<script src="../js/search.js"></script>

</body>
</html>
