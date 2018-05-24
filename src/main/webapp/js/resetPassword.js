var email, value, passwordNew;
$(function () {
    //step1
    $("#email").focusout(function () {
        email = $("#email").val();
        if (email == null || email == "") {
            $("#confirmBtn").attr("disabled", "true");
            return;
        }
        if (!checkEmailFormat(email)) {
            $(".error").html("邮箱错误").show();
            $("#queryCode").attr("disabled", "true");
            return;
        }
        $(".error").hide();
        $.ajax({
            type:"post",
            url:"/user/verify.action",
            data: {"val": email, "type": "email"},
            dataType:"json",
            success:function (data) {
                if (data.status == -3) {  //参数错误
                    alert(data.msg);
                    return;
                }
                //邮箱已存在
                if (data.status == 0) {
                    $(".error").hide();
                    $("#queryCode").removeAttr("disabled");
                    if (value == null || value == "") {
                        $("#confirmBtn").attr("disabled", "true");
                        return;
                    }
                    $("#confirmBtn").removeAttr("disabled");
                    return;
                }
                $(".error").html("邮箱不存在，快去注册吧").show();
            },
            error:function () {
                window.location.href = "systemError.jsp";
            }
        });
    });
    //发送邮件
    $("#queryCode").click(function () {
        $(this).val("60后重新获取").attr("disabled", "true");
        var countDown = setInterval(function () {
            var val = $("#queryCode").val();
            var num;
            if (val.length == 7) {
                num = val.substr(0, 2);
            } else if (val.length == 6) {
                num = val.substr(0, 1);
            }
            if (num > 0) {
                $("#queryCode").val((--num) + "后重新获取");
            } else if (num == 0) {
                $("#queryCode").val("获取验证码").removeAttr("disabled");
                clearInterval(countDown);
            }
        }, 1000);
        $.ajax({
            type: "post",
            url: "/mail/sendMail.action",
            data: {"recipient": email},
            dataType: "json",
            success: function () {
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
    });
    var token;
    $("#code").keyup(function () {
        value = $("#code").val();
        if (value == null || value == "") {
            $("#confirmBtn").attr("disabled", "true");
            return;
        }
        $("#confirmBtn").removeAttr("disabled");
    });
    $("#confirmBtn").click(function () {
        $.ajax({
            type: "post",
            url: "/mail/confirmCode.action",
            data: {"recipient": email, "code": value},
            dataTye: "json",
            success: function (data) {
                if (data.status == 0) {   //验证码错误
                    $(".error").html(data.msg).show();
                    return;
                }
                if (data.status == 1) { //验证码正确
                    token = data.data; //更新密码时的token
                    $(".error").hide();
                    $(".step1").hide();
                    $(".step2").css("display", "flex");
                    $("ul li").eq(1).addClass("success");
                }
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
    });

    //step2
    $("#password").focusout(checkPasswordValue);
    $("#passwordRe").focusout(checkPasswordValue);
    $("#resetBtn").click(function () {
        $.ajax({
            type: "post",
            url: "/user/updatePassword.action",
            data: {"email": email, "password": passwordNew, "token": token},
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    $(".error").html(data.msg).show();
                    return;
                }
                $(".error").hide();
                //step3
                $(".step3").show().siblings().hide();
                $("ul li").eq(2).addClass("success");
                var time = $("#counttime").html();
                setInterval(function () {
                    if (time == 0) {
                        window.location.href = "index.jsp";
                        return;
                    }
                    $("#counttime").html(--time);
                }, 1000);
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
    });
});

function checkPasswordValue() {
    passwordNew = $("#password").val();
    value = $("#passwordRe").val();
    $(".error").hide();
    if (passwordNew == null || passwordNew == "" || value == null || value == "") {
        $("#resetBtn").attr("disabled", "true");
        return;
    }
    if (passwordNew != value) {
        $(".error").html("密码不一致").show();
        return;
    }
    $(".error").hide();
    $("#resetBtn").removeAttr("disabled");
}