<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 2018/3/24
  Time: 14:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>center</title>
    <link href="../vendors/select2-4.0.6-rc.1/dist/css/select2.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/center.css">
    <link rel="stylesheet" href="../font/iconfont.css">
</head>
<body>

<!--side tools-->
<div class="sideTool">
    <span class="iconfont icon-fankui"></span>
    <span class="iconfont icon-fanhuidingbu"></span>
</div>

<jsp:include page="nav.jsp"/>

<div class="head">
    <img id="avatar" src="">
    <span id="headuser"></span>
    <img src="../images/center.jpg">
</div>
<div class="body">
    <ul class="catalog">
        <li id="navStrategy">
            <svg class="icon iconClick" aria-hidden="true" style="font-size: 70px">
                <use xlink:href="#icon-diqiu1"></use>
            </svg>
            <span>我的攻略</span>
        </li>
        <li id="navCollect">
            <svg class="icon" aria-hidden="true" style="font-size: 60px">
                <use xlink:href="#icon-zhixiang"></use>
            </svg>
            <span>我的收藏</span>
        </li>
        <li id="navEdit">
            <svg class="icon" aria-hidden="true" style="font-size: 60px">
                <use xlink:href="#icon-gerenziliao-copy"></use>
            </svg>
            <span>修改资料</span>
        </li>
        <li id="navMsg">
            <a href="?message">
                <svg class="icon" aria-hidden="true" style="font-size: 60px">
                    <use xlink:href="#icon-xiaoxizhongxin"></use>
                </svg>
                <span>消息中心</span>
            </a>
        </li>
    </ul>
    <div class="content">
        <div id="strategyContainer">
            <div id="container">
                <div id="echarts">
                    <div id="strategyMap"></div>
                    <div id="strategyPie"></div>
                </div>
                <div id="pageDot"><span id="mapDot"></span><span id="pieDot"></span></div>
            </div>
            <span class="strategyNothing"></span>
            <div id="separate"></div>
            <div id="strategies" style="height: auto;overflow: hidden"></div>
        </div>
        <div id="collectContainer">
            <ul class="collectCatalog">
                <li>
                    <span class="iconfont icon-shengxu"></span>
                    <span class="iconfont icon-jiangxu"></span>
                    <span>按城市排序</span>
                </li>
                <li>
                    <span class="iconfont icon-shengxu"></span>
                    <span class="iconfont icon-jiangxu"></span>
                    <span>按时间排序</span>
                </li>
                <li>
                    <span class="iconfont icon-shengxu"></span>
                    <span class="iconfont icon-jiangxu"></span>
                    <span>按收藏数排序</span>
                </li>
                <li>
                    <span class="iconfont icon-shengxu"></span>
                    <span class="iconfont icon-jiangxu"></span>
                    <span>按点赞数排序</span>
                </li>
            </ul>
            <div class="rightCollect">
                <div class="collects"></div>
                <div id="pageNum">
                    <div id="page" class="page_div"></div>
                </div>
            </div>
            <span class="collectNothing"></span>
        </div>
        <div id="editContainer">
            <ul class="editCatalog">
                <li id="editUpdate" class="editCilck">编辑资料</li>
                <li id="editEmail">更换邮箱</li>
                <li id="editReset">修改密码</li>
            </ul>
            <div class="editInfo">
                <!-- 编辑资料 -->
                <form class="edit">
                    <div class="left">
                        <div>
                            <div>用户名</div>
                            <span class="nameWarn"></span><br>
                            <input type="text" name="username" id="centerUsername">
                        </div>
                        <div>
                            <div>性别</div>
                            <br>
                            <select class="js-example-basic-single" name="gender" id="gender">
                                <option value="unknown">未知</option>
                                <option value="female">女</option>
                                <option value="male">男</option>
                            </select>
                        </div>
                        <div>
                            <div>城市</div>
                            <br>
                            <input type="text" name="city" id="city"><br>
                        </div>
                        <div>
                            <div>个人简介</div>
                            <br>
                            <textarea type="text" name="bio" id="bio"
                                      placeholder="Tell us a little bit about yourself"></textarea><br>
                        </div>
                        <input type="button" id="editBtn" value="保 存">
                    </div>
                </form>
                <!-- 更换头像 -->
                <form enctype="multipart/form-data" id="avatarForm" method="post">
                    <div class="right">
                        <img id="usericon" src="../images/notfound.jpg"><br>
                        <input type="file" name="file" id="iconPath" onchange="getPath(this)"><br>
                        <input type="submit" id="uploadIcon" value="上传图片" onclick="iconPath.click()"><br>
                    </div>
                </form>
                <!-- 更换邮箱 -->
                <div class="mailContainer">
                    <div>
                        <div>邮箱</div>
                        <span class="emailWarn"></span><br>
                        <input type="text" name="email" id="email" value="yangchen302@163.com">
                        <input type="button" id="updateBtn" value="获取验证码" disabled><br>
                    </div>
                    <div class="codeContainer">
                        <div>code</div>
                        <span class="codeWarn"></span><br>
                        <input type="text" id="code"><br>
                    </div>
                    <input type="button" id="emailBtn" value="更 新" disabled>
                </div>
                <!-- 修改密码 -->
                <form class="reset">
                    <div>
                        <div>旧密码</div>
                        <br>
                        <input type="password" class="oldPwd"><br>
                        <span class="oldPwdWarn"></span><br>
                    </div>
                    <div>
                        <div>新密码</div>
                        <br>
                        <input type="password" class="newPwd" disabled><br><br>
                    </div>
                    <div>
                        <div>再次输入</div>
                        <br>
                        <input type="password" class="rePwd" disabled><br>
                        <span class="rePwdWarn"></span><br>
                    </div>
                    <input type="button" value="重置密码" id="resetBtn" disabled>
                </form>
            </div>
        </div>
        <div id="msgContainer">
            <div id="msgs"></div>
            <span class="msgNothing"></span>
        </div>
    </div>
    <div class="feedbackDetail"></div>
</div>

<script src="../js/jquery-3.2.1.min.js"></script>
<!-- echarts -->
<script src="../vendors/ECharts/echarts.js"></script>
<script src="../vendors/ECharts/china.js"></script>
<!-- select2 -->
<script src="../vendors/select2-4.0.6-rc.1/dist/js/select2.min.js"></script>
<!-- iconfont -->
<script src="../font/iconfont.js"></script>
<!-- jquery form -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"
        integrity="sha384-FzT3vTVGXqf7wRfy8k4BiyzvbNfeYjK+frTVqZeNDFl8woCbF0CYG6g2fMEFFo/i"
        crossorigin="anonymous"></script>
<!-- paging -->
<script src="../js/paging.js"></script>

<script src="../js/center.js"></script>
</body>
</html>