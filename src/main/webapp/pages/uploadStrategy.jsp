<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/3/24
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>上传攻略</title>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">
    <link href="../vendors/select2-4.0.6-rc.1/dist/css/select2.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/uploadStrategy.css">
    <link rel="stylesheet" href="../font/iconfont.css">
</head>
<body>

<div class="tips"></div>

<jsp:include page="nav.jsp"/>

<div class="strategyContainer">
    <div class="st">
        <input type="button" value="提 交" id="subBtn">
        <span class="iconfont icon-iconhelp" title="查看帮助"></span>
    </div>
    <div class="helpPage">
        <div id="helpClose">x</div>
    </div>
    <div class="tags">
        <div>
            <span>省份：</span>
            <select class="js-example-basic-single cities">
                <option value="0">全部</option>
            </select>
        </div>
        <div>
            <span>时长：</span>
            <input type="number" min="1" value="1" id="duration">天
        </div>
    </div>
    <input type="text" id="title" placeholder="攻略标题，不超过20字">
</div>
<input type="text" id="subTitle" placeholder="子标题，不超过10字">
<!-- wangEditor -->
<div id="editor"></div>
<div class="addContainer">
    <input type="button" value="添 加" id="addBtn">
</div>

<script src="../js/jquery-3.2.1.min.js"></script>
<!-- wangEditor -->
<script src="../vendors/wangEditor-3.0.15/release/wangEditor.min.js"></script>
<!-- select2 -->
<script src="../vendors/select2-4.0.6-rc.1/dist/js/select2.min.js"></script>
<script src="../js/common.js"></script>
<script src="../js/uploadStrategy.js"></script>
</body>
</html>
