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
        $(".tips").html("please enter title").show().fadeOut(2000);
        return false;
    }
    var testData=data;
    while(testData.indexOf("<p>") != -1){
        testData=testData.replace("<p>","");
    }
    while(testData.indexOf("</p>") != -1){
        testData=testData.replace("</p>","");
    }
    while(testData.indexOf("<div>") != -1){
        testData=testData.replace("<div>","");
    }
    while(testData.indexOf("</div>") != -1){
        testData=testData.replace("</div>","");
    }
    while(testData.indexOf("&nbsp;") != -1){
        testData=testData.replace("&nbsp;","");
    }
    while(testData.indexOf("<br>") != -1){
        testData=testData.replace("<br>","");
    }
    if(testData.trim() == ""){
        $(".tips").html("please enter content").show().fadeOut(2000);
        return false;
    }
    return data;
}

function getformatDate(){
    var date=new Date();
    var year=date.getFullYear();
    var month=formatNumber(date.getMonth()+1);
    var day=formatNumber(date.getDate());
    var hour=formatNumber(date.getHours());
    var min=formatNumber(date.getMinutes());
    var sec=formatNumber(date.getSeconds());
    return year+"-"+month+"-"+day+" "+hour+":"+min+":"+sec
}

function formatNumber(number){
    return (number < 10) ? "0"+number : number;
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
            $(".sideTool").slideDown();
        }else{
            $(".sideTool").slideUp();
        }
    });
    /* back to top */
    $(".icon-fanhuidingbu").click(function(){
        $("body,html").animate({scrollTop:0},500);
    });
});