var phone,value,passwordNew;
$(function(){
    //$(".step2").hide();

    //step1
    $("#phone").focusout(checkAccountValue);
    $("#code").keyup(checkAccountValue);
    $("#confirmBtn").click(function(){
        console.log("phone->"+phone+",code->"+value);
        $(".step1").hide();
        $(".step2").css("display","flex");
        $("ul li").eq(1).addClass("success");
        //todo 短信验证成功以后重置密码
    });

    //step2
    $("#password").focusout(checkPasswordValue);
    $("#passwordRe").focusout(checkPasswordValue);
    $("#resetBtn").click(function(){
        $.ajax({
            type: "post",
            url: "/user/updatePassword.action",
            data: {"phone":phone,"passwordNew": passwordNew},
            dataType: "json",
            success:function(data){
                if(data.status == 0){
                    $(".error").html("修改密码失败").show();
                    return;
                }
                //step3
                $(".step3").show().siblings().hide();
                $("ul li").eq(2).addClass("success");
                var time=$("#counttime").html();
                setInterval(function(){
                    if(time == 0){
                        window.location.href="signin.html";
                        return;
                    }
                    $("#counttime").html(--time);
                },1000);
            },
            error:function(){
                console.log("reset password error");
            }
        });
    });
});

function checkAccountValue(){
    phone=$("#phone").val();
    value=$("#code").val();
    if(phone == null || phone == ""){
        $("#confirmBtn").attr("disabled","true");
        return;
    }
    if(!checkPhoneFormat(phone)){
        $(".error").html("手机号错误").show();
        $("#queryCode").attr("disabled","true");
        return;
    }
    $(".error").hide();
    $("#queryCode").removeAttr("disabled");
    if(value == null || value == ""){
        $("#confirmBtn").attr("disabled","true");
        return;
    }
    $("#confirmBtn").removeAttr("disabled");
}

function checkPasswordValue(){
    passwordNew=$("#password").val();
    value=$("#passwordRe").val();
    $(".error").hide();
    if(passwordNew == null || passwordNew == "" || value == null || value == ""){
        $("#resetBtn").attr("disabled","true");
        return;
    }
    if(passwordNew != value){
        $(".error").html("密码不一致").show();
        return;
    }
    $(".error").hide();
    $("#resetBtn").removeAttr("disabled");
}