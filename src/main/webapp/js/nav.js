var navUserId;
$(function () {

    //获取当前用户的信息
    $.ajax({
        type: "get",
        url: "/user/queryUserInformation.action",
        dataType: "json",
        success: function (data) {
            if (data.status != 1) {
                return;
            }
            navUserId = data.data.id;
            //dwr页面加载
            dwrMessage.onPageLoad(navUserId);
        },
        error: function () {
            window.location.href="systemError.jsp";
        }
    });

    //有没有消息未查看
    $.ajax({
        type: "get",
        url: "/feedback/queryNotice.action",
        dataType: "json",
        success: function (data) {
            if (data.status != 1) {
                $("#msgDot").css("display", "none");
                return;
            }
            $("#msgDot").css("display", "inline-block");
        },
        error: function () {
            window.location.href="systemError.jsp";
        }
    });

    //查看消息
    $(".icon-lingdang").click(function () {
        window.location.href = "center.jsp?message";
    });

    /* 为背景的宽高赋值 */
    $("#bg").height($(document).height()).width($(document).width());
    $(window).resize(function () {    //宽高随着浏览器大小变化
        $("#bg").height($(document).height()).width($(document).width());
    });
    /* 点击背景(隐藏背景和登录/注册框) */
    $("#bg").click(function () {
        $(this).slideUp();  //隐藏bg
        $("#signinContainer").slideUp();    //隐藏登陆框
        $("#signinContainer")[0].reset();   //清空登陆框
        $("#signupContainer").slideUp();    //隐藏注册框
        $("#signupContainer")[0].reset();   //清空注册框
        $("#signWarn").html("");    //清空错误提示
    });

    /* 点击搜索 */
    $(".icon-fangdajing").click(function () {
        var searchInp = search();
        if (searchInp == undefined) {
            return;
        }
        window.location.href = "search.jsp?search=" + searchInp;
    });

    /* 点击注册 */
    $("#signup").click(function () {
        $("#signupContainer").slideDown();  //显示注册框
        $("#bg").slideDown();   //显示背景
    });

    /* 点击登录 */
    $("#signin").click(function () {
        $("#signinContainer").slideDown();  //显示登录框
        $("#bg").slideDown();   //显示背景
    });

    /* "去注册" */
    $("#goUp").click(function () {
        $("#signupContainer").slideDown();  //显示注册框
        $("#signinContainer").css("display", "none");    //隐藏登录框
        $("#signupContainer")[0].reset();   //清空注册框
        $("#signWarn").html("");    //清空错误提示
    });

    /* "去登陆" */
    $("#goIn").click(function () {
        $("#signinContainer").slideDown();  //显示登陆框
        $("#signupContainer").css("display", "none");    //隐藏注册框
        $("#signinForm")[0].reset();    //清空登陆框
        $("#signWarn").html("");    //清空错误提示
    });

    /* 注册 */
    //校验注册时所需参数是否符合规范的标记
    var upNameFlag, upPwdFlag, upPwdReFlag, upEmailFlag;
    //注册时输入的密码
    var upPwd;
    //校验用户名
    $("#upName").focusout(function () {
        var upName = $("#upName").val().trim();
        if (upName == null || upName == "") {
            upNameFlag == false;
            $("#signWarn").html("");    //清空错误提示
            $("#upBtn").attr("disabled", "true");    //注册按钮置为无效
            return;
        }
        //如果用户名满足邮箱的正则表达式则不符合规范
        if (checkEmailFormat(upName)) {
            upNameFlag == false;
            $("#signWarn").html("用户名不符合格式规范");    //清空错误提示
            $("#upBtn").attr("disabled", "true");    //注册按钮置为无效
            return;
        }
        $.ajax({
            type: "post",
            url: "/user/verify.action",
            data: {"val": upName, "type": "username"},
            dataType: "json",
            success: function (data) {
                //用户名校验失败
                if (data.status == 0) {
                    upNameFlag == false;
                    $("#signWarn").html("用户名已存在");  //错误提示
                    $("#upBtn").attr("disabled", "true");    //注册按钮置为无效
                    return;
                }
                //用户名校验成功
                upNameFlag = true;
                $("#signWarn").html("");//清空错误提示
                if (upPwdFlag && upPwdReFlag && upEmailFlag) {    //输入参数均符合规范注册按钮置为有效
                    $("#upBtn").removeAttr("disabled");
                }
            },
            error: function () {
                window.location.href="systemError.jsp";
            }
        });
    });
    //校验密码
    $("#upPwd").focusout(function () {
        upPwd = $("#upPwd").val().trim();
        if (upPwd == null || upPwd == "") {
            upPwdFlag == false;
            $("#upBtn").attr("disabled", "true");//注册按钮置为无效
            return;
        }
        upPwdFlag = true;
        if (upNameFlag && upPwdReFlag && upEmailFlag) {   //输入参数均符合规范注册按钮置为有效
            $("#upBtn").removeAttr("disabled");
        }
    });
    //校验再次输入密码
    $("#upPwdRe").focusout(function () {
        var upPwdRe = $("#upPwdRe").val().trim();
        if (upPwdRe == null || upPwdRe == "") {
            upPwdReFlag == false;
            $("#upBtn").attr("disabled", "true");    //注册按钮置为无效
            return;
        }
        if (upPwd != null && upPwd != "") {
            //两次输入的密码相同
            if (upPwd == upPwdRe) {
                upPwdReFlag = true;
                $("#signWarn").html("");    //清空错误提示
                if (upNameFlag && upPwdFlag && upEmailFlag) { //输入参数均符合规范注册按钮置为有效
                    $("#upBtn").removeAttr("disabled");
                }
                return;
            }
            //两次输入的密码不同
            upPwdReFlag == false;
            $("#signWarn").html("两次密码不一致"); //错误提示
            $("#upBtn").attr("disabled", "true");    //注册按钮置为无效
        }
    });
    //校验邮箱
    $("#upEmail").focusout(function () {
        var upEmail = $("#upEmail").val().trim();
        if (upEmail == null || upEmail == "") {
            upEmailFlag == false;
            $("#signWarn").html("");    //清空错误提示
            $("#upBtn").attr("disabled", "true");    //注册按钮置为无效
            return;
        }
        //邮箱正则表达式
        if (!checkEmailFormat(upEmail)) {
            upEmailFlag = false;
            $("#signWarn").html("邮箱格式错误");    //错误提示
            return;
        }
        //校验邮箱是否重复
        $.ajax({
            type: "post",
            url: "/user/verify.action",
            data: {"val": upEmail, "type": "email"},
            dataType: "json",
            success: function (data) {
                console.log(JSON.stringify(data));
                //邮箱校验失败
                if (data.status == 0) {
                    upEmailFlag == false;
                    $("#signWarn").html("邮箱已存在");  //错误提示
                    $("#upBtn").attr("disabled", "true");    //注册按钮置为无效
                    return;
                }
                //邮箱校验成功
                upEmailFlag = true;
                $("#signWarn").html("");    //清空错误提示
                if (upNameFlag && upPwdFlag && upPwdReFlag) { //输入参数均符合规范注册按钮置为有效
                    $("#upBtn").removeAttr("disabled");
                }
            },
            error: function () {
                window.location.href="systemError.jsp";
            }
        });
    });
    //点击注册
    $("#upBtn").click(function () {
        $.ajax({
            type: "post",
            url: "/user/register.action",
            data: $("#signupContainer").serialize(),
            dataType: "json",
            success: function (data) {
                //注册失败
                if (data.status == 0) {
                    $("#signWarn").html("注册失败");    //错误提示
                    return;
                }
                //注册成功
                $("#signupContainer")[0].reset();   //清空输入框
                $("#signupContainer").slideUp();    //隐藏注册框
                $("#signinContainer").slideDown();  //显示登陆框
                $("#upBtn").attr("disabled", "true");    //注册按钮置为无效
            },
            error: function () {
                window.location.href="systemError.jsp";
            }
        });
    });

    /* 登录 */
    //校验登录时所需参数是否符合规范的标记
    var inNameFlag, inPwdFlag;
    var inName, inPwd;
    //校验用户名
    $("#inName").keyup(function () {
        inName = $("#inName").val().trim();
        if (inName == null || inName == "") {
            inNameFlag = false;
            $("#inBtn").attr("disabled", "true");    //登录按钮置为无效
            return;
        }
        inNameFlag = true;
        if (inPwdFlag) {
            $("#inBtn").removeAttr("disabled"); //输入参数均符合规范登录按钮置为有效
        }
    });
    //校验密码
    $("#inPwd").keyup(function () {
        inPwd = $("#inPwd").val().trim();
        if (inPwd == null || inPwd == "") {
            inPwdFlag = false;
            $("#inBtn").attr("disabled", "true");    //登录按钮置为无效
            return;
        }
        inPwdFlag = true;
        if (inNameFlag) { //输入参数均符合规范登录按钮置为有效
            $("#inBtn").removeAttr("disabled");
        }
    });
    //点击登录
    $("#inBtn").click(function () {
        var type = checkEmailFormat(inName) ? "email" : "username";
        $.ajax({
            type: "post",
            url: "/user/login.action",
            data: {"type": type, "val": inName, "password": inPwd},
            dataType: "json",
            success: function (data) {
                //登录失败
                if (data.status == 0) {
                    $("#signWarn").html(data.msg);    //错误提示
                    return;
                }
                //登陆成功
                history.go(0);
            },
            error: function () {
                window.location.href="systemError.jsp";
            }
        });
    });

    /* 退出登录 */
    $("#logout").click(function () {
        $.ajax({
            type: "get",
            url: "/user/logout.action",
            dataType: "json",
            success: function (data) {
                //退出登录成功
                if (data.status == 1) {
                    window.location.href="index.jsp";
                }
            }
        });
    });

});


//推送信息处理
function showMessage(data) {
    console.log(data);
    $("#msgDot").css("display", "inline-block");
}