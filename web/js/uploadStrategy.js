//todo 1.after add the strategy, clean the content of simditor if use simditor
//todo ******2.drag to change the order of day
//todo 3.describe how to drag to change the order of the day

//wangEidtor
var E = window.wangEditor;
var editor = new E('#editor');
//use base64 to save img
editor.customConfig.uploadImgShowBase64 = true;
editor.create();

var data,that;

$(function(){
    
    /* strategy title */
    $("#title").change(function(){
        if($(this).val().trim() == ""){
            $("#title").css("border-color","red");
        }else{
            $("#title").css("border-color","#999");
        }
    });
    
    /* add day */
    $("#addBtn").click(function(){
        data=$(".w-e-text").html();
        data=formatData(data);
        if(data){
            var day=($(".strategyContainer").find(".day").length+1);
            $(".strategyContainer").append("<div class='day' ondrop='drop(event,this)' ondragover='allowDrop(event)' draggable='true' ondragstart='drag(event, this)'><div class='dayTitle mb10'><span>Day "+day+"</span><span class='iconfont icon-delete' title='删除'></span><span class='iconfont icon-bianji' title='编辑'></span><span class='iconfont icon-baocun' title='保存'></span></div><div class='content' id='day"+day+"'>"+data+"</div></div>");
            //clean content
            $(".w-e-text").html("");
            //click event
            $(".strategyContainer .day .icon-bianji").off("click").on("click", editDay);
            $(".strategyContainer .day .icon-baocun").off("click").on("click", saveDay);
            $(".strategyContainer .day .icon-delete").off("click").on("click", deleteDay);
        }
    });
    
    /* submit strategy */
    $("#subBtn").click(function(){
        var title=$("#title").val();
        if(title == null || title == ""){
            $("#title").css("border-color","red");
            $(".tips").html("please input title").show().fadeOut(2000);
        }
        if($(".strategyContainer").find(".day").length == 0){
            $(".tips").html("no content entered").show().fadeOut(2000);
        }
    });
    
    $(".icon-iconhelp").mouseenter(function(){
        $(".helpPage").css("display","block");
    }).mouseleave(function(){
        if(!$(".helpPage").mouseenter){
            $(".helpPage").css("display","none");
        }
    });
    $(".helpPage").mouseleave(function(){
        $(".helpPage").css("display","none");
    })
    
});

/* move start */
function allowDrop(ev){
    ev.preventDefault();    //prevent default behavior
}

var srcdiv = null;
function drag(ev,divdom){   //store dom and data
    srcdiv=divdom;
    ev.dataTransfer.setData("text/html",divdom.innerHTML);
}

function drop(ev,divdom){   //exchange data
    ev.preventDefault();    //prevent default behavior
    if(srcdiv != divdom){
        srcdiv.innerHTML = divdom.innerHTML;
        divdom.innerHTML=ev.dataTransfer.getData("text/html");
        exchangeDay();  //modify order
    }
}
/* move end */

//edit day's content
function editDay(){
    //1.css
    $(this).next().css("display","block");
    $(this).css("display","none");
    //2.data
    that=$(this).parent().parent().find(".content");
    data=that.html();
    that.empty();
    var contentId="#"+that.attr("id");
    console.log(contentId);
    editor = new E(contentId);
    editor.customConfig.uploadImgShowBase64 = true;
    editor.create();
    $(this).parent().parent().find(".content .w-e-text").html(data);
}

//save day's content
function saveDay(){
    that=$(this).parent().parent().find(".content");
    data=that.find(".w-e-text").html();
    data=formatData(data);
    if(data){
        //1.css
        $(this).prev().css("display","block");
        $(this).css("display","none");
        //2.data
        that.empty().append(data);
    }
}

//delete day's content
function deleteDay(){
    $(this).parent().parent().remove();
    exchangeDay();
}

function exchangeDay(){
    var days=$(".strategyContainer .day");
    for(i=0;i<days.length;i++){
        $(days[i]).find("span").eq(0).html("Day "+(i+1));   //update dayTtile
        $(days[i]).find(".content").attr("id","day"+(i+1)); //update content's id
    }
}

//judge day's content 
function formatData(data){
    while(data.indexOf("<p>") != -1){
        data=data.replace("<p>","");
    }
    while(data.indexOf("</p>") != -1){
        data=data.replace("</p>","");
    }
    while(data.indexOf("<div>") != -1){
        data=data.replace("<div>","");
    }
    while(data.indexOf("</div>") != -1){
        data=data.replace("</div>","");
    }
    while(data.indexOf("&nbsp;") != -1){
        data=data.replace("&nbsp;","");
    }
    while(data.indexOf("<br>") != -1){
        data=data.replace("<br>","");
    }
    if(data.trim() == ""){
        $(".tips").html("please enter content").show().fadeOut(2000);
        return false;
    }
    return data;
}

//simditor
/*var editor=new Simditor({
    textarea: $("#editor"),
    placeholder: "",
    toolbar: true,
    toolbarFloat: false,//Fixed the toolbar on the top of the browser when scrolling
    toolbarHidden: false,
    defaultImage: "",//Default image placeholder. Used when inserting pictures in Simditor.
    tabIndent: true,//Use 'tab' key to make indent.
    params: {},//Insert a hidden input in textarea to store params (key-value pairs)
    upload: true,
    pasteImage: true//only FF and Chrome
});*/