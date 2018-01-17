$(function(){
    $("#signin").click(function(){
        $("#signinContainer").slideDown();
        $("#bg").slideDown();
    });
    $("#signup").click(function(){
        $("#signupContainer").slideDown();
        $("#bg").slideDown();
    });
    $("#bg").click(function(){
        $(this).slideUp();
        $("#signinContainer").slideUp();
        $("#signupContainer").slideUp();
    });
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
});