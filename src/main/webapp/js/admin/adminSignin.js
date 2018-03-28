$(function(){
    var username,password;
    var nameFlag,pwdFlag;
    
    $("#inBtn").click(function(){
        $.ajax({
            type:"post",
            url:"/manage/user/login.action",
            data:{"username":username,"password":password},
            dataType:"json",
            success:function(data){
                if(data.status == 0){
                    $(".signin")[0].reset();
                    $("#inBtn").attr("disabled","true");
                    $(".error").html("username or password incorrect").css("visibility","visible");
                    alert(data.msg);
                    return;
                }
                window.location.href="admin.html";
            },
            error:function(){
                console.log("admin sign in error");
            }
        })
    });
    
    $("#username").keyup(function(){
        username=$(this).val();
        if(username == "" || username == null){
            $("#inBtn").attr("disabled","true");
            nameFlag=false;
        }else{
            nameFlag=true;
            if(pwdFlag){
                $("#inBtn").removeAttr("disabled");
            }
        }
    });
    
    $("#password").keyup(function(){
        password=$(this).val();
        if(password == "" || password == null){
            $("#inBtn").attr("disabled","true");
            pwdFlag=false;
        }else{
            pwdFlag=true;
            if(nameFlag){
                $("#inBtn").removeAttr("disabled");
            }
        }
    });
});