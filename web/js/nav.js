//1.Can I use email to signin? Or only can I use username to login
//2.check the format of email input
//******3.clean the inputs when click the bg
//******4.check whether the username is valid when signup
$(function(){
    /* 1. to top */
    /* toggle toTop icon*/
    $(window).scroll(function(){
        var heightTop=$(window).scrollTop();
        if(heightTop > 300){
            $("#backTop").slideDown();
        }else{
            $("#backTop").slideUp();
        }
    });
    /* back to top */
    $("#toTop").click(function(){
        $("body,html").animate({scrollTop:0},500);
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
    var upNameFlag=true,upPwdFlag=true,upPwdReFlag=true,upEmailFlag=true;
    var upPwd;
    /* upName */
    $("#upName").change(function(){
        var upName=$("#upName").val().trim();  //.trim(): exclude space
        if(upName == null || upName == ""){
            $(".tips").html("please input username").show().fadeOut(3000);
            $("#upName").addClass("warnBorder");
        }else{
            $.ajax({
/*                type:"post",
                url:"",
                data:{"username":upName},
                dataType:"text",*/
                success:function(data){
                    var data=1;
                    if(data == 1){
                        upNameFlag=false;
                        $("#upName").removeClass("warnBorder");
                    }else{
                        upNameFlag==true;
                        $(".tips").html("username has been used").show().fadeOut(3000);
                        $("#upName").addClass("warnBorder");
                    }
                },
                error:function(){
                    alert("nameValid error");
                }
            });
        }
    });
    /* upPwd */
    $("#upPwd").change(function(){
        upPwd=$("#upPwd").val().trim();
        if(upPwd == null || upPwd == ""){
            upPwdFlag==true;
            $(".tips").html("please input password").show().fadeOut(3000);
            $("#upPwd").addClass("warnBorder");
        }else{
            upPwdFlag=false;
            $("#upPwd").removeClass("warnBorder");
        }
    });
    /* upPwdRe */
    $("#upPwdRe").change(function(){
        var upPwdRe=$("#upPwdRe").val().trim();
        if(upPwdRe == null || upPwdRe == ""){
            $(".tips").html("please confirm password").show().fadeOut(3000);
            $("#upPwdRe").addClass("warnBorder");
        }else{
            if(upPwd != null && upPwd != ""){
                if(upPwd == upPwdRe){
                    upPwdReFlag=false;
                    $("#upPwdRe").removeClass("warnBorder");
                }else{
                    upPwdReFlag==true;
                    $(".tips").html("different password").show().fadeOut(3000);
                    $("#upPwdRe").addClass("warnBorder");
                }
            }
        }
    });
    /* upEmail */
    $("#upEmail").change(function(){
        var upEmail=$("#upEmail").val().trim();
        if(upEmail == null || upEmail == ""){
            upEmailFlag==true;
            $(".tips").html("please input email").show().fadeOut(3000);
            $("#upEmail").addClass("warnBorder");
        }else{
            //todo 2
            upEmailFlag=false;
            $("#upEmail").removeClass("warnBorder");
        }
    });
    /* upBtn */
    $("#upBtn").click(function(){
        upNameFlag ? $("#upName").addClass("warnBorder") : ""
        upPwdFlag ? $("#upPwd").addClass("warnBorder") : ""
        upPwdReFlag ? $("#upPwdRe").addClass("warnBorder") : ""
        upEmailFlag ? $("#upEmail").addClass("warnBorder") : ""
        if(upNameFlag || upPwdFlag || upPwdReFlag || upEmailFlag){
            $(".tips").html("signup messages are invalid").show().fadeOut(3000);
            return;
        }
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
            },
            error:function(){
                alert("signup error");
            }
        });
    });
    
    /* signin */
    var inNameFlag=true,inPwdFlag=true;
    /* inName */
    $("#inName").change(function(){
        var inName=$("#inName").val().trim();
        if(inName == null || inName == ""){
            inNameFlag=true;
            $(".tips").html("please input username").show().fadeOut(3000);
            $("#inName").addClass("warnBorder");
        }else{
            inNameFlag=false;
            $("#inName").removeClass("warnBorder");
        }
    });
    /* inPwd */
    $("#inPwd").change(function(){
        var inPwd=$("#inPwd").val().trim();
        if(inPwd == null || inPwd == ""){
            inPwdFlag=true;
            $(".tips").html("please input password").show().fadeOut(3000);
            $("#inPwd").addClass("warnBorder");
        }else{
            inPwdFlag=false;
            $("#inPwd").removeClass("warnBorder");
        }
    });
    /* inBtn */
    $("#inBtn").click(function(){
        inNameFlag ? $("#inName").addClass("warnBorder") : ""
        inPwdFlag ? $("#inPwd").addClass("warnBorder") : ""
        if(inNameFlag || inPwdFlag){
            $(".tips").html("signin messages are invalid").show().fadeOut(3000);
            return;
        }
        //todo 1
        $.ajax({
/*            type:"post",
            url:"",
            data:$("#signupContainer").serialize(),
            dataType:"json",*/
            success:function(data){
                var data={"username":"mushroom","userIcon":"2"};
                console.log(JSON.stringify(data));
                $("#bg").slideUp();
                $("#signinContainer").slideUp();
                $("#sign").css("display","none");
                $("#userIcon").css("display","inline-block");
                $("#signinContainer")[0].reset();   //clean input
                
                $("#username").html(data.username);
                $("#userIcon img").attr("src","../images/icon"+data.userIcon+".jpg");
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