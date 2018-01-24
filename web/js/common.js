function operateNumber(that){
    var clickClass=$(that).parent().find(".colorBlack").attr("class");
    var thisClass=$(that).attr("class");
    var number=$(that).next().html();
    if(thisClass.indexOf("colorBlack") == -1){
        if(!clickClass){
            $(that).addClass("colorBlack");
            $(that).next().html(++number);
        }else{
            //$(".tips").html("please cancle your forward operation").show().fadeOut(2000);
            return false;
        }
    }else{
        $(that).removeClass("colorBlack");
        $(that).next().html(--number);
    }
    return true;
}

function packageAjax(type,url,data,dataType){
    console.log(2);
    $.ajax({
/*        type: type,
        url: url,
        data: data,
        dataType: dataType,*/
        success:function(data){
            var data=-1;
            console.log(data);
            return data;
        },
        error:function(){
            console.log(1);
            return false;
        }
    });
    console.log(3);
}

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
});