<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/3/24
  Time: 15:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>strategyItem</title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../font/iconfont.css">
    <link rel="stylesheet" href="../css/strategyItem.css">
</head>
<body>
    <!--side tools-->
    <div class="sideTool">
        <span class="iconfont icon-fankui"></span>
        <span class="iconfont icon-fanhuidingbu"></span>
    </div>

    <jsp:include page="nav.jsp"/>

    <div class="body">
        <!-- catalog -->
        <ul class="catalog">
            <li><span class="iconfont icon-bookmark"></span>Contents</li>
        </ul>
        <!-- right -->
        <div class="ItemRight">
            <!-- ItemNav -->
            <div class="ItemNav">
                位置 : <span class="tags"></span>
                <span class="navOpe">
                        <span class="iconfont icon-zan1"></span>
                        <span class="navFor"></span>
                        <span class="iconfont icon-collection-b"></span>
                        <span class="navCollect"></span>
                    </span>
            </div>
            <!-- content -->
            <div class="content" value="">
                <div class="strategyInfo">
                    <span class="title">title</span>
                    <span class="author">author</span>
                </div>
                <div class="contentContainer"></div>
                <!-- comment -->
                <div class="cata commentTitle">评论</div>
                <div class="addCommentContainer">
                    <textarea id="addComment" placeholder="留下你的评论..."></textarea>
                    <input type="button" id="addCommentBtn" value="评论">
                </div>
                <div class="comments"></div>
            </div>
        </div>
    </div>

    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/strategyItem.js"></script>
    <script src="../js/common.js"></script>
</body>
</html>
