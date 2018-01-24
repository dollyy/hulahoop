var level;   //level of each comment; 
$(function(){
    //list all the comments --start
    $.ajax({
/*        type:"post",
        url:"",
        dataType:"json",*/
        success:function(data){
            //comments' level,responseImg,responseUid,responseName,requestUid,requestName,recontent,date,forNum,againstNum
            var data={"comments":[
                [
                    ["1","1","1","user1","0","0","今天是几号呀","2017-12-11 12:11:11",'1','2'],
                    ["1.1","2","2","user2","1","user1","15号","2017-12-11 12:11:12",'8','7'],
                    ["1.1.1","3","3","user3","2","user2","哈哈","2017-12-11 12:11:13",'5','2'],
                    ["1.1.2","1","1","user1","2","user2","好嘞","2017-12-11 12:11:14",'7','4'],
                    ["1.2","3","3","user3","1","user1","12.15","2017-12-11 12:11:13",'1','2'],
                    ["1.3","4","4","user4","1","user1","不造啊","2017-12-11 12:11:16",'4','6'],
                    ["1.4","5","5","user5","1","user1","hhhh????","2017-12-11 12:11:17",'1','0']
                ],
                [
                    ["2","1","1","user1","0","0","明天是几号啊","2017-12-11 12:11:12",'1','2'],
                    ["2.1","2","2","user2","1","user1","12.16","2017-12-11 12:11:13",'6','9'],
                    ["2.1.1","3","3","user3","2","user2","嘿嘿","2017-12-11 12:11:14",'6','2'],
                    ["2.1.2","1","1","user1","2","user2","造了","2017-12-11 12:11:15",'1','3'],
                    ["2.2","3","3","user3","3","user1","16号","2017-12-11 12:11:14",'8','2'],
                    ["2.3","4","4","user4","4","user1","emmmm","2017-12-11 12:11:18",'0','4']
                ]
            ]};
            //console.log(JSON.stringify(data));
            
            var commentPadding;   //padding-left of each comment
            for(var i=0;i<data.comments.length;i++){
                //add comment div,in order to tell the different comment div, I put a variable i after comment
                $("#comments").append("<div class='comment"+i+"'></div>");
                for(var j=0;j<data.comments[i].length;j++){
                    level=data.comments[i][j][0];
                    commentPadding=(level.indexOf(".") != -1) ? 40*(level.split(".").length-1) : 0; //calculate the padding-left                    
                    //add each div
                    $(".comment"+i).append("<div class='each' level='"+level+"'><div><div class='user' style='padding-left: "+commentPadding+"px'><img src='../images/icon"+data.comments[i][j][1]+".jpg'><span class='responseUid' uid='"+data.comments[i][j][2]+"'>"+data.comments[i][j][3]+"</span></div><div class='content'><div><span class='response'>"+(data.comments[i][j][4] == 0 ? "" : "@"+data.comments[i][j][5])+"</span><span>"+data.comments[i][j][6]+"</span></div><div class='operation'><span class='commentTime'>"+currentTime()+"</span><i class='iconfont icon-zan11'></i><span class='for'>0</span><i class='iconfont icon-zan1'></i><span class='against'>0</span><span class='addComment'>回复</span></div></div></div><div class='commentContent' style='padding-left: "+commentPadding+"px'><div><span class='currentUser'>currentuser</span><textarea class='currentComment'></textarea></div><div class='btns'><input type='button' class='btnAdd' value='add'><input type='button' class='btnCancle' value='cancle'></div></div></div>");
                    
                    //click 回复 to show the addComment div
                    $(".addComment").unbind("click").bind("click",showAddComment);
                    
                    //click cancle to hide the addComment div
                    $(".btnCancle").unbind("click").bind("click",hideAddComment);
                    
                    //click add to add comment
                    $(".btnAdd").unbind("click").bind("click",addComment);
                    
                    //click against, add against number
                    $(".icon-zan1").unbind("click").bind("click",calNumber);
                    
                    //click for, add for number
                    $(".icon-zan11").unbind("click").bind("click",calNumber);
                }
            }
        },
        error:function(){
            //alert("error");
        }
    });
    //list all the comments --end
    
});

//show addComment div --start
function showAddComment(){
    $(this).parent().parent().parent().next().show();
}
//show addComment div --end

//hide addComment div --start
function hideAddComment(){
    $(this).parent().parent().hide();
    $(this).parent().prev().find(".currentComment").val("");    //clean the value of comment textarea
}
//hide addComment div --end

//add comment --start
function addComment(){
    var that=$(this);
    var level=$(this).parent().parent().parent().attr("level");
    var responseUid="6";
    var responseName="user6";
    var responseImg="6";
    var requestUid=$(this).parent().parent().parent().find(".responseUid").attr("uid");
    var requestName=$(this).parent().parent().parent().find(".responseUid").html();
    var comment=$(this).parent().prev().find(".currentComment").val();
    console.log(level+","+responseUid+","+responseName+","+requestUid+","+requestName+","+comment);
    $.ajax({
/*            type:"post",
        url:"",
        data:{"level":level,"responseUid":responseUid,"comment":comment},
        dataType:"text",*/
        success:function(data){
            var data=12;
            commentPadding=parseInt(that.parent().parent().css("padding-left")) + 40;  //calculate the padding-left
            //add comment on the web                
            that.parent().parent().parent().after("<div class='each' level='"+data+"'><div><div class='user' style='padding-left: "+commentPadding+"px'><img src='../images/icon"+responseImg+".jpg'><span class='responseUid' uid='"+responseUid+"'>"+responseName+"</span></div><div class='content'><div><span class='response'>@"+requestName+"</span><span>"+comment+"</span></div><div class='operation'><span class='commentTime'>"+currentTime()+"</span><span class='addComment'>回复</span><i class='iconfont icon-zan1'>0</i><i class='iconfont icon-zan11'>0</i></div></div></div><div class='commentContent' style='padding-left: "+commentPadding+"px'><div><span class='currentUser'>currentuser</span><textarea class='currentComment'></textarea></div><div class='btns'><input type='button' class='btnAdd' value='add'><input type='button' class='btnCancle' value='cancle'></div></div></div>");

            //clean the comment textarea
            that.parent().parent().hide();
            that.parent().prev().find(".currentComment").val("");

            //click 回复 to show the addComment div
            $(".addComment").unbind("click").bind("click",showAddComment);

            //click cancle to hide the addComment div
            $(".btnCancle").unbind("click").bind("click",hideAddComment);

            //click add to add comment
            $(".btnAdd").unbind("click").bind("click",addComment);

            //click against, add against number
            $(".icon-zan1").unbind("click").bind("click",operateAgainst);

            //click for, add for number
            $(".icon-zan11").unbind("click").bind("click",operateFor);
        },
        error:function(){
            //alert("error");
        }
    });
}
//add comment --end

//calculate current time --start
function currentTime(){
    var time=new Date();
    var month=formatNum(time.getMonth()+1);
    var day=formatNum(time.getDate());
    var hour=formatNum(time.getHours());
    var min=formatNum(time.getMinutes());
    var second=formatNum(time.getSeconds());
    return time.getFullYear()+"-"+month+"-"+day+" "+hour+":"+min+":"+second;
}
//calculate current time --end

//format number --start
function formatNum(num){
    if(1 == num.toString().split("").length){
        return "0"+num;
    }
    return num;
}
//format number --end

//operate for or against --start
function calNumber(){
    var that=$(this);
    if(!operateNumber(that)){
        return;
    }
    var numberType=that.attr("class").indexOf("icon-zan11") != -1 ? "against" : "for"
    var number=that.next().html();
    var level=that.parent().parent().parent().parent().attr("level");
    console.log(numberType,number,level);
    $.ajax({
/*        type:"post",
        url:"",
        data:{"level":level,"":number},*/
        success:function(){
            
        },
        error:function(){
            //alert("error");
        }
    });
}
//operate for or against --end
