var email, value, passwordNew;
$(function () {
    //step1
    $("#email").focusout(checkAccountValue);
    //发送邮件
    $("#queryCode").click(function () {
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
    $("#code").keyup(checkAccountValue);
    $("#confirmBtn").click(function () {
        console.log("email->" + email + ",code->" + value);
        $.ajax({
            type:"post",
            url:"",
            data:{},
            dataTye:"json",
            success:function(data){
                if(data.status == 1){
                    $(".step1").hide();
                    $(".step2").css("display", "flex");
                    $("ul li").eq(1).addClass("success");
                }
            },
            error:function(){
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
            data: {"email": email, "passwordNew": passwordNew},
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    $(".error").html("修改密码失败").show();
                    return;
                }
                //step3
                $(".step3").show().siblings().hide();
                $("ul li").eq(2).addClass("success");
                var time = $("#counttime").html();
                setInterval(function () {
                    if (time == 0) {
                        window.location.href = "signin.html";
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

function checkAccountValue() {
    email = $("#email").val();
    value = $("#code").val();
    if (email == null || email == "") {
        $("#confirmBtn").attr("disabled", "true");
        return;
    }
    if (!checkEmailFormat(email)) {
        $(".error").html("手机号错误").show();
        $("#queryCode").attr("disabled", "true");
        return;
    }
    $(".error").hide();
    $("#queryCode").removeAttr("disabled");
    if (value == null || value == "") {
        $("#confirmBtn").attr("disabled", "true");
        return;
    }
    $("#confirmBtn").removeAttr("disabled");
}

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