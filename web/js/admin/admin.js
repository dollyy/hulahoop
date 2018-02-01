//todo
//1.do not show scroll when dom's height doesn't exceed the max-height
//2.get the content of wangEditor
//3.retrive the deleted help
$(function(){
    var helpData,resData;
    
    /* 1.index */
    $(".index").click(function(){
        $(".indexContainer").show().siblings().hide();
        $(this).addClass("clicked").siblings().removeClass("clicked");
        $(".right .location span").html($(this).children().last().html());
    });
    
    /* 2.help */
    $(".help").click(function(){
        $(".helpTable tbody").empty();
        if(helpData == undefined){
            $.ajax({
/*                type:"post",
                url:"",
                dataType:"json",*/
                success:function(data){
/*                    var data={"helps":[["1","攻略标签是什么？","<div style='text-align: center;'>从下拉框中为这篇攻略打上关于地点、时长的tag吧！</div>","2018-01-22 12:12:12","2018-01-26 16:16:16"],["2","交换Days","<div><div style='text-align: center;'>•step1:</div><div style='text-align: center;'><img src='../images/help/uploadStrategy1.png'></div></div><div><div style='text-align: center;'>•step2:</div><div style='text-align: center;'><img src='../images/help/uploadStrategy2.png'></div></div><div><div style='text-align: center;'>•step3:</div><div style='text-align: center;'><img src='../images/help/uploadStrategy3.png'></div></div>","2018-01-28 19:34:26","2018-01-28 22:02:45"]]};*/
                        var data={};
                    if(data.helps == undefined){
                        $(".helpTable").hide();
                        $(".nothing").show();
                        return;
                    }
                    helpData=data;
                    packHelp(helpData.helps);
                },
                error:function(){
                    alert("help error");
                }
            });
        }else{
            packHelp(helpData.helps);
        }
        $(".helpContainer").show().siblings().hide();
        $(this).addClass("clicked").siblings().removeClass("clicked");
        $(".right .location span").html($(this).children().last().html());
    });
    //add icon-xinzeng
    $(".icon-xinzeng").click(function(){
        $("#bg").slideDown();
        $(".opeContainer").empty().append("<input type='text' id='subTitle' placeholder='Help Title. No more than 20 words'><div id='editor'></div><input type='button' value='添 加'  id='addBtn'>");
        $("#addBtn").off("click").on("click", addHelp);
        $("#subTitle").off("click").on("change",titleChange);
        showEditor();
        $(".opeContainer").slideDown();
    });
    //click bg
    $("#bg").click(function(){
        $(this).slideUp();
        $(".opeContainer").slideUp();
        $("#subTitle").val("");
        $(".opeContainer .w-e-text").html("");
    });
    
    /* 3.res */
    $(".res").click(function(){
        $(".resContainer").show().siblings().hide();
        $(this).addClass("clicked").siblings().removeClass("clicked");
        $(".right .location span").html($(this).children().last().html());
    });
});

//create editor
function showEditor(){
    //wangEidtor
    var E = window.wangEditor;
    var editor = new E('#editor');
    //use base64 to save img
    editor.customConfig.uploadImgShowBase64 = true;
    editor.create();
}
//package data for "help page"
function packHelp(data){
    $(".nothing").hide();
    for(i=0;i<data.length;i++){
        $(".helpTable tbody").append("<tr><td>"+data[i][0]+"</td><td class='trTitle'>"+data[i][1]+"</td><td>"+data[i][3]+"</td><td>"+data[i][4]+"</td><td><span class='iconfont icon-close-eye' title='隐藏'></span><span class='iconfont icon-chakan' title='查看'></span><span class='iconfont icon-bianji' title='编辑'></span><span class='iconfont icon-delete' title='删除'></span></td></tr><tr style='border:none'><td colspan='5' style='padding: 0'><div class='inner'>"+data[i][2]+"</div></td></tr>");
    }
    $(".icon-close-eye").off("click").on("click",helpHide);    //隐藏
    $(".icon-chakan").off("click").on("click",helpView);    //查看
    $(".icon-bianji").off("click").on("click",helpEdit);    //编辑
    $(".icon-delete").off("click").on("click",helpDelete);  //删除
}
//add new help
function addHelp(){
    var title=$("#subTitle").val();
    var content=$(".w-e-text").html();
    content=formatData(title, content);
    if(content){
        var addDate=getformatDate();
        $.ajax({
/*                type:"post",
            url:"",
            data:{"title":title,"content":content,"createTime":addDate,"updateTime":addDate},
            dataType:"text",*/
            success:function(data){
                var data=4; //返回的该help的id
                packHelp([[data,title,content,addDate,addDate]]);
                $(".helpTable").show();
                $("#bg").slideUp();
                $(".opeContainer").slideUp();
                $("#subTitle").val("");
                $(".opeContainer .w-e-text").html("");
            },
            error:function(){
                alert("add title error");
            }
        });
    }
}
//hide help's content
function helpHide(){
    $(this).parent().parent().next().find(".inner").css({"height":"0","min-height":"0"});
    $(this).css({"font-size":"0","margin":"0"});
    $(this).next().show();
}
//view help's content
function helpView(){
    //collapse other helpView
    $(".inner").css({"height":"0","min-height":"0"});
    $(".icon-close-eye").css({"font-size":"0","margin":"0"});
    $(".icon-chakan").show();
    //show current helpView
    $(this).parent().parent().next().find(".inner").css({"height":"auto","min-height":"50px"});
    $(this).prev().css({"font-size":"16px","margin-right":"10px"});
    $(this).hide();
}
var that;
//edit help
function helpEdit(){
    $("#bg").slideDown();
    $(".opeContainer").empty().append("<span class='iconfont icon-baocun'></span><input type='text' id='subTitle' placeholder='Help Title. No more than 20 words'><div id='editor'></div>");
    showEditor();
    $(".opeContainer #subTitle").val($(this).parent().parent().find(".trTitle").html());
    $(".opeContainer #editor .w-e-text").html($(this).parent().parent().next().find(".inner").html());
    $(".opeContainer").slideDown();
    that=$(this).parent().parent(); //current modify item
    $(".icon-baocun").off("click").on("click",helpSave);
    $("#subTitle").off("change").on("change",titleChange);
}
//save changes
function helpSave(){
    var title=$("#subTitle").val();
    var content=$(".w-e-text").html();
    content=formatData(title, content);
    if(content){
        var updateDate=getformatDate();
        $.ajax({
/*                type:"post",
            url:"",
            data:{"title":title,"content":content,"updateTime":updateDate},
            dataType:"text",*/
            success:function(data){
                var data=1;
                if(data != 1){
                    $(".tips").html("update help failed").show().fadeOut(2000);
                    return;
                }
                that.find(".trTitle").html(title);
                that.next().find(".inner").html(content);
                $("#bg").slideUp();
                $(".opeContainer").slideUp();
                $("#subTitle").val("");
                $(".opeContainer .w-e-text").html("");
            },
            error:function(){
                alert("add title error");
            }
        });
    }
}
//subTitle's change event
function titleChange(){
    if($(this).val().trim() == ""){
        $("#subTitle").css("border-color","red");
    }else{
        $("#subTitle").css("border-color","#333");
    }
}
//delete change
function helpDelete(){
    $(this).parent().parent().next().remove();
    $(this).parent().parent().remove();
    if($(".helpTable tbody tr").length == 0){
        $(".helpTable").hide();
        $(".nothing").show();
    }
}