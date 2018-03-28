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

function formatDate(time){
    var date=new Date(time);
    var year=date.getFullYear();
    var month=formatNumber(date.getMonth()+1);
    var day=formatNumber(date.getDate());
    var hour=formatNumber(date.getHours());
    var min=formatNumber(date.getMinutes());
    var sec=formatNumber(date.getSeconds());
    return year+"-"+month+"-"+day+" "+hour+":"+min+":"+sec
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

//手机号码正则表达式
function checkPhoneFormat(phone){
    return /^1[3|4|5|8][0-9]\d{8}$/.test(phone);
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

function search(){
    var search=$("#searchInp").val();
    if(search == "" || search == null){
        return;
    }
    return search;
}