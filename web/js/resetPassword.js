var value1,value2;
$(function(){
    //$(".step2").hide();
    
    //step1
    $("#phone").keyup(checkAccountValue);
    $("#code").keyup(checkAccountValue);
    $("#confirmBtn").click(function(){
        console.log("phone->"+value1+",code->"+value2);
        $(".step2").show().siblings().hide();
    });
    
    //step2
    $("#password").keyup(checkPasswordValue);
    $("#passwordRe").keyup(checkPasswordValue);
    $("#resetBtn").click(function(){
        console.log("phone->"+value1+",code->"+value2);
        //step3
        $(".step3").show().siblings().hide();
        var time=$("#counttime").html();
        setInterval(function(){
            if(time == 0){
                window.location.href="index.html";
                return;
            }
            $("#counttime").html(--time);
        },1000);
    });
});
function checkAccountValue(){
    value1=$("#phone").val();
    value2=$("#code").val();
    if(value1 == null || value1 == ""){
        $("#confirmBtn").attr("disabled","true");
        return;
    }
    if(value2 == null || value2 == ""){
        $("#confirmBtn").attr("disabled","true");
        return;
    }
    $("#confirmBtn").removeAttr("disabled");
}

function checkPasswordValue(){
    value1=$("#password").val();
    value2=$("#passwordRe").val();
    $(".error").hide();
    if(value1 == null || value1 == ""){
        $("#resetBtn").attr("disabled","true");
        return;
    }
    if(value2 == null || value2 == ""){
        $("#resetBtn").attr("disabled","true");
        return;
    }
    if(value1 != value2){
        $(".error").html("密码不一致").show();
        return;
    }
    $(".error").hide();
    $("#resetBtn").removeAttr("disabled");
}