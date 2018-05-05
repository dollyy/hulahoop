//支持/反对的操作
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

//解析地址
function getQueryStringArgs() {
    var args=[];
    //取得查询字符串并去掉开头的问号--->第一个是问号
    var parameters = (location.search.length > 0 ? location.search.substring(1) : "");
    //取得每一项
    var items = parameters.length ? parameters.split("&") : [];
    var item, value;
    //逐个讲每一项添加到args对象中
    for (i = 0; i < items.length; i++) {
        item = items[i].split("=");
        name = decodeURIComponent(item[0]);
        value = decodeURIComponent(item[1]);
        if (name.length > 0) {
            args[name] = value;
        }
    }
    return args[name];
}

//格式化日期
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

//获取格式化的当前日期
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

//数字处理
function formatNumber(number){
    return (number < 10) ? "0"+number : number;
}

//邮箱正则表达式
function checkEmailFormat(email){
    return /^([a-z0-9A-Z]+[-|\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-zA-Z]{2,}$/.test(email);
}

//搜索
function search(){
    var search=$("#searchInp").val();
    if(search == "" || search == null){
        return;
    }
    return search;
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

    //反馈
    $(".icon-fankui").click(function () {
        window.location.href="feedback.jsp";
    });
});