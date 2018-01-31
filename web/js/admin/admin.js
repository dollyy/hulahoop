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
            console.log(1);
            $.ajax({
/*                type:"post",
                url:"",
                dataType:"json",*/
                success:function(data){
                    var data={"helps":[["1","攻略标签是什么？","2018-01-22 12:12:12","2018-01-26 16:16:16","123"],["2","交换Days","2018-01-28 19:34:26","2018-01-28 22:02:45","456"]]};
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
    $("#addBtn").click(function(){
        var title=$("#helpTitle").val();
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
                    var data=4;
                    packHelp([[data,title,addDate,addDate,content]]);
                    $(".helpTable").show();
                    $("#bg").slideUp();
                    $(".opeContainer").slideUp();
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
        $(".helpTable tbody").append("<tr id='"+data[i][0]+"'><td>"+(i+1)+"</td><td>"+data[i][1]+"</td><td>"+data[i][2]+"</td><td>"+data[i][3]+"</td><td><span class='iconfont icon-chakan' title='查看'></span><span class='iconfont icon-bianji' title='编辑'></span><span class='iconfont icon-delete' title='删除'></span></td></tr>");
    }
}