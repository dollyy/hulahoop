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

function formatData(subTitle,data){
    if(subTitle == null || subTitle == ""){
        $("#subTitle").css("border-color","red");
        $(".tips").html("please enter content").show().fadeOut(2000);
        return false;
    }
    while(data.indexOf("<p>") || data.indexOf("</p>") || data.indexOf("<div>") || data.indexOf("</div>") || data.indexOf("&nbsp;") || data.indexOf("<br>")){
        data.replace("<p>","");
        data.replace("</p>","");
        data.replace("<div>","");
        data.replace("</div>","");
        data.replace("&nbsp;","");
        data.replace("<br>","");
    }
/*    while(data.indexOf("</p>") != -1){
        data.replace("</p>","");
    }
    while(data.indexOf("<div>") != -1){
        data.replace("<div>","");
    }
    while(data.indexOf("</div>") != -1){
        data.replace("</div>","");
    }
    while(data.indexOf("&nbsp;") != -1){
        data.replace("&nbsp;","");
    }
    while(data.indexOf("<br>") != -1){
        data.replace("<br>","");
    }*/
    if(data.trim() == ""){
        $(".tips").html("please enter content").show().fadeOut(2000);
        return false;
    }
    return data;
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