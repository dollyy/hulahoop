$(function(){
    var username,password;
    var nameFlag,pwdFlag;
    
    $("#inBtn").click(function(){
        $.ajax({
/*            type:"post",
            url:"",
            data:{"username":username,"password":password},
            dataType:"text",*/
            success:function(data){
                var data=-1;
                if(data == -1){  //signin error
                    $(".signin")[0].reset();
                    $("#inBtn").attr("disabled","true");
                    $(".error").html("username or password incorrect").css("visibility","visible");
                }
            },
            error:function(){
                alert("admin signin error");
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