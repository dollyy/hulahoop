//wangEidtor
var E = window.wangEditor;
var editor = new E('#editor');
//use base64 to save img
editor.customConfig.uploadImgShowBase64 = true;
editor.create();

$(function(){
    var helpData,resData;
    
    /*index*/
    $(".index").click(function(){
        $(".indexContainer").show().siblings().hide();
    });
    
    /* help */
    $(".help").click(function(){
        if(helpData == undefined){
            $.ajax({
/*                type:"post",
                url:"",
                dataType:"json",*/
                success:function(data){
                    var data={"helps":[["1","攻略标签是什么？","<div style='text-align: center;'>从下拉框中为这篇攻略打上关于地点、时长的tag吧！</div>","2018-01-22 12:12:12","2018-01-26 16:16:16"],["2","交换Days","<div><div style='text-align: center;'>•step1:</div><div style='text-align: center;'><img src='../images/help/uploadStrategy1.png'></div></div><div><div style='text-align: center;'>•step2:</div><div style='text-align: center;'><img src='../images/help/uploadStrategy2.png'></div></div><div><div style='text-align: center;'>•step3:</div><div style='text-align: center;'><img src='../images/help/uploadStrategy3.png'></div></div>","2018-01-28 19:34:26","2018-01-28 22:02:45"]]};
                    if(data.helps == undefined){
                        $(".helpTable").hide();
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
    });
    //点击新增
    $(".icon-xinzeng").click(function(){
        $("#bg").slideDown();
        $(".opeContainer").slideDown();
    });
    $("#bg").click(function(){
        $(this).slideUp();
        $(".opeContainer").slideUp();
        $("#helpTitle").val("");
        $(".opeContainer .w-e-text").html("");
    });
    //添加
    $("#addBtn").click(function(){
        var title=$("#helpTitle").val();
        var content=$(".w-e-text").html();
        content=formatData(title, content);
        if(content){
            console.log(content);
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
                    $("#helpTitle").val("");
                    $(".opeContainer .w-e-text").html("");
                },
                error:function(){
                    alert("add title error");
                }
            });
        }
    });
    
    /* res */
    $(".res").click(function(){
        $(".resContainer").show().siblings().hide();
    });
});

function packHelp(data){
    for(i=0;i<data.length;i++){
        $(".helpTable tbody").append("<tr id='"+data[i][0]+"'><td>"+$(".helpTable tr").length+"</td><td class='trTitle'>"+data[i][1]+"</td><td class='trContent'>"+data[i][2]+"</td><td>"+data[i][3]+"</td><td>"+data[i][4]+"</td><td><span class='iconfont icon-chakan' title='查看'></span><span class='iconfont icon-bianji' title='编辑'></span><span class='iconfont icon-delete' title='删除'></span></td></tr>");
    }
    $(".icon-chakan").off("click").on("click",helpView);    //查看
    $(".icon-bianji").off("click").on("click",helpEdit);    //编辑
    $(".icon-delete").off("click").on("click",helpDelete);  //删除
}

function helpView(){
    $(".opeContainer #helpTitle").val($(this).parent().parent().find(".trTitle").html());
    $(".opeContainer #editor .w-e-text").html($(this).parent().parent().find(".trContent").html());
    $("#bg").slideDown();
    $(".opeContainer").slideDown();
}

function helpEdit(){
    $(".opeContainer #helpTitle").val($(this).parent().parent().find(".trTitle").html());
    $(".opeContainer #editor .w-e-text").html($(this).parent().parent().find(".trContent").html());
    $("#bg").slideDown();
    $(".opeContainer").slideDown();
}

function helpDelete(){
}