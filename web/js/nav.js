//1.Can I use email to signin? Or only can I use username to login
//2.check the format of email input
//******3.clean the inputs when click the bg
//******4.check whether the username is valid when signup
//5.save password (localStorage)
$(function(){
    $("#bg").height($(document).height());
    
    $("#goUp").hover(function(){
        $(".icon-feiji").css("transform","rotateZ(-45deg)");
    });
    
    /* to signup*/
    $("#goUp").click(function(){
        $("#signupContainer").slideDown();
        $("#signinContainer").css("display","none");
        $("#signupContainer")[0].reset();
        $("#signupContainer input").each(function(i,item){
            $(item).removeClass("warnBorder");
        });
    });
    
    /* to signin */
    $("#goIn").click(function(){
        $("#signinContainer").slideDown();
        $("#signupContainer").css("display","none");
        $("#signinForm")[0].reset();
        $("#signinForm input").each(function(i,item){
            $(item).removeClass("warnBorder");
        });
    });
    
    /* 2.sign */
    /* click signin*/
    $("#signin").click(function(){
        $("#signinContainer").slideDown();
        $("#bg").slideDown();
    });
    /* click signup*/
    $("#signup").click(function(){
        $("#signupContainer").slideDown();
        $("#bg").slideDown();
    });
    /* click bg*/
    $("#bg").click(function(){
        $(this).slideUp();
        $("#signinContainer").slideUp();
        $("#signupContainer").slideUp();
        //clean input
        $("#signinContainer")[0].reset();
        $("#signupContainer")[0].reset();
        //remove css
        $("#signinContainer input").each(function(i,item){
            $(item).removeClass("warnBorder");
        });
        $("#signupContainer input").each(function(i,item){
            $(item).removeClass("warnBorder");
        });
    });
    
    /* signup */
    var upNameFlag,upPwdFlag,upPwdReFlag,upEmailFlag;
    var upPwd;
    /* upName */
    $("#upName").keyup(function(){
        var upName=$("#upName").val().trim();
        if(upName == null || upName == ""){
            $("#upName").addClass("warnBorder");
        }else{
/*            var data=packageAjax("post","",{"username":upName},"text");
            if(data){
                if(data == 1){
                    upNameFlag=false;
                    $("#upName").removeClass("warnBorder");
                }else{
                    upNameFlag==true;
                    $(".tips").html("username has been used").show().fadeOut(3000);
                    $("#upName").addClass("warnBorder");
                }
            }else{
                alert("nameValid error");
            }*/
            $.ajax({
/*                type:"post",
                url:"",
                data:{"username":upName},
                dataType:"text",*/
                success:function(data){
                    var data=1;
                    if(data != 1){
                        upNameFlag==false;
                        $("#upName").addClass("warnBorder");
                        $("#upBtn").attr("disabled","true");
                    }else{
                        upNameFlag=true;
                        $("#upName").removeClass("warnBorder");
                        if(upPwdFlag && upPwdReFlag && upEmailFlag){
                            $("#upBtn").removeAttr("disabled");
                        }
                    }
                },
                error:function(){
                    alert("nameValid error");
                }
            });
        }
    });
    /* upPwd */
    $("#upPwd").keyup(function(){
        upPwd=$("#upPwd").val().trim();
        if(upPwd == null || upPwd == ""){
            upPwdFlag==false;
            $("#upPwd").addClass("warnBorder");
            $("#upBtn").attr("disabled","true");
        }else{
            upPwdFlag=true;
            $("#upPwd").removeClass("warnBorder");
            if(upNameFlag && upPwdReFlag && upEmailFlag){
                $("#upBtn").removeAttr("disabled");
            }
        }
    });
    /* upPwdRe */
    $("#upPwdRe").keyup(function(){
        var upPwdRe=$("#upPwdRe").val().trim();
        if(upPwdRe == null || upPwdRe == ""){
            $("#upPwdRe").addClass("warnBorder");
        }else{
            if(upPwd != null && upPwd != ""){
                if(upPwd == upPwdRe){
                    upPwdReFlag=true;
                    $("#upPwdRe").removeClass("warnBorder");
                    if(upNameFlag && upPwdFlag && upEmailFlag){
                        $("#upBtn").removeAttr("disabled");
                    }
                }else{
                    upPwdReFlag==false;
                    $("#upPwdRe").addClass("warnBorder");
                    $("#upBtn").attr("disabled","true");
                }
            }
        }
    });
    /* upEmail */
    $("#upEmail").keyup(function(){
        var upEmail=$("#upEmail").val().trim();
        if(upEmail == null || upEmail == ""){
            upEmailFlag==false;
            $("#upEmail").addClass("warnBorder");
            $("#upBtn").attr("disabled","true");
        }else{
            //todo 2
            upEmailFlag=true;
            $("#upEmail").removeClass("warnBorder");
            if(upNameFlag && upPwdFlag && upPwdReFlag){
                $("#upBtn").removeAttr("disabled");
            }
        }
    });
    /* upBtn */
    $("#upBtn").click(function(){
        $.ajax({
/*            type:"post",
            url:"",
            data:$("#signupContainer").serialize(),
            dataType:"text",*/
            success:function(data){
                var data=1;
                if(data == -1){
                    $(".tips").html("signup failed").show().fadeOut(3000);
                    return;
                }
                $("#signupContainer")[0].reset();   //clean input
                $("#signupContainer").slideUp();
                $("#signinContainer").slideDown();
                $("#upBtn").attr("disabled","true");
            },
            error:function(){
                alert("signup error");
            }
        });
    });
    
    /* signin */
    var inNameFlag,inPwdFlag;
    /* inName */
    $("#inName").keyup(function(){
        var inName=$("#inName").val().trim();
        if(inName == null || inName == ""){
            $("#inBtn").attr("disabled","true");
            inNameFlag=false;
            $("#inName").addClass("warnBorder");
        }else{
            inNameFlag=true;
            $("#inName").removeClass("warnBorder");
            if(inPwdFlag){
                $("#inBtn").removeAttr("disabled");
            }
        }
    });
    /* inPwd */
    $("#inPwd").keyup(function(){
        var inPwd=$("#inPwd").val().trim();
        if(inPwd == null || inPwd == ""){
            $("#inBtn").attr("disabled","true");
            inPwdFlag=false;
            $("#inPwd").addClass("warnBorder");
        }else{
            inPwdFlag=true;
            $("#inPwd").removeClass("warnBorder");
            if(inNameFlag){
                $("#inBtn").removeAttr("disabled");
            }
        }
    });
    /* inBtn */
    $("#inBtn").click(function(){
        //todo 1
        $.ajax({
/*            type:"post",
            url:"",
            data:$("#signinForm").serialize(),
            dataType:"json",*/
            success:function(data){
                var data={"username":"mushroom","userIcon":"2"};
                console.log(JSON.stringify(data));
                $("#bg").slideUp();
                $("#signinContainer").slideUp();
                $("#sign").css("display","none");
                $("#userIcon").css("display","inline-block");
                $("#signinForm")[0].reset();   //clean input
                $("#inBtn").attr("disabled","true");
                
                $("#username").html(data.username);
                $("#userIcon img").attr("src","../images/icons/icon"+data.userIcon+".jpg");
            },
            error:function(){
                alert("signin error");
            }
        });
    });
    
    /* logout */
    $("#logout").click(function(){
        $("#sign").css("display","inline-block");
        $("#userIcon").css("display","none");
    });
    
});