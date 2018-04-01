var username, password;
$(function () {
    var nameFlag, pwdFlag;

    //回车事件
    $(document).keypress(function (e) {
        if (e.which == 13) {
            adminLogin();
        }
    });

    //管理员登录
    $("#inBtn").click(adminLogin);

    //用户名键盘事件
    $("#username").keyup(function () {
        username = $(this).val();
        if (username == "" || username == null) {
            $("#inBtn").attr("disabled", "true");
            nameFlag = false;
        } else {
            nameFlag = true;
            if (pwdFlag) {
                $("#inBtn").removeAttr("disabled");
            }
        }
    });

    //密码键盘事件
    $("#password").keyup(function () {
        password = $(this).val();
        if (password == "" || password == null) {
            $("#inBtn").attr("disabled", "true");
            pwdFlag = false;
        } else {
            pwdFlag = true;
            if (nameFlag) {
                $("#inBtn").removeAttr("disabled");
            }
        }
    });
});

//管理员登陆
function adminLogin() {
    $.ajax({
        type: "post",
        url: "/manage/user/login.action",
        data: {"username": username, "password": password},
        dataType: "json",
        success: function (data) {
            console.log(JSON.stringify(data));
            if (data.status == -3) {  //参数错误
                alert(data.msg);
                return;
            }
            if (data.status == 0) {
                $(".signin")[0].reset();
                $("#inBtn").attr("disabled", "true");
                $(".error").html("username or password incorrect").css("visibility", "visible");
                return;
            }
            window.location.href = "admin.html";
        },
        error: function () {
            console.log("admin sign in error");
        }
    })
}