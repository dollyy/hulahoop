<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/4/27
  Time: 13:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>反馈信息</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/feedback.css">
</head>
<body>

<jsp:include page="nav.jsp"/>

<div class="paper">
    <div class="to">To hulahoop:</div>
    <textarea id="content" placeholder="写点什么吧..."></textarea>
    <input type="button" value="提交" id="feedbackBtn">
</div>
<script src="../js/jquery-3.2.1.min.js"></script>
<script src="../js/common.js"></script>
<script src="../js/feedback.js"></script>

</body>
</html>
