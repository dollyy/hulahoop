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
        $(".helpContainer").show().siblings().hide();
        if(helpData == undefined){
            console.log(1);
            $.ajax({
/*                type:"post",
                url:"",
                dataType:"json",*/
                success:function(data){
                    var data={"helps":[["1","攻略标签是什么？","2018-01-22 12:12:12","2018-01-26 16:16:16","123"],["2","交换Days","2018-01-28 19:34:26","2018-01-28 22:02:45","456"]]};
                    helpData=data;
                    packHelp(helpData);
                },
                error:function(){
                    alert("help error");
                }
            });
        }else{
            packHelp(helpData);
        }
    });
    $(".icon-xinzeng").click(function(){
        $("#bg").slideDown();
        $(".opeContainer").slideDown();
    });
    $("#bg").click(function(){
        $(this).slideUp();
    });
    $("#addBtn").click(function(){
        var title=$("#helpTitle").val();
        var data=$(".w-e-text").html();
        data=formatData(title, data);
        console.log(data);
    });
    
    /* res */
    $(".res").click(function(){
        $(".resContainer").show().siblings().hide();
    });
});

function packHelp(helpData){
    for(i=0;i<helpData.helps.length;i++){
        $(".helpTable tbody").append("<tr id='"+helpData.helps[i][0]+"'><td>"+(i+1)+"</td><td>"+helpData.helps[i][1]+"</td><td>"+helpData.helps[i][2]+"</td><td>"+helpData.helps[i][3]+"</td><td><span class='iconfont icon-chakan' title='查看'></span><span class='iconfont icon-bianji' title='编辑'></span><span class='iconfont icon-delete' title='删除'></span></td></tr>");
    }
}