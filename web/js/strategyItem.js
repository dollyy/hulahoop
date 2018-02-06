$(function(){
    var i;
    $.ajax({
/*        type:"post",
        url:"",
        dataType:"json",*/
        success:function(data){
            var data={"province":"澳门","duration":"5天","navFor":1,"navCollect":2,"catalog":["1111111111111111111111333331","2","3","4","5","评论"],"content":[[1],[2],[3],[4],[5]],"comments":[["1","1","user1","2018-01-23 19:03:25","位置位置位置位置","2","3","5"],["3","2","user2","2018-01-24 10:33:25","位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置","6","5","4"],["4","3","user3","2018-01-25 18:33:55","位置位置位置位置位置位置位置位置位置位置位置位置位置位置","7","4","7"],["5","4","user4","2018-01-25 09:34:34","位置位置位置位置位置","9","2","6"],["1","1","user1","2018-01-23 19:03:25","位置位置位置位置","2","3","5"],["3","2","user2","2018-01-24 10:33:25","位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置","6","5","4"],["4","3","user3","2018-01-25 18:33:55","位置位置位置位置位置位置位置位置位置位置位置位置位置位置","7","4","7"],["5","4","user4","2018-01-25 09:34:34","位置位置位置位置位置","9","2","6"],["1","1","user1","2018-01-23 19:03:25","位置位置位置位置","2","3","5"],["3","2","user2","2018-01-24 10:33:25","位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置位置","6","5","4"],["4","3","user3","2018-01-25 18:33:55","位置位置位置位置位置位置位置位置位置位置位置位置位置位置","7","4","7"],["5","4","user4","2018-01-25 09:34:34","位置位置位置位置位置","9","2","6"]]};
            
            //1.province + duration
            $("nav .tags").html(data.province+" > "+data.duration);
            $("nav .navFor").html(data.navFor);
            $("nav .navCollect").html(data.navCollect);
            
            //2.catalog
            for(i=0;i<data.catalog.length;i++){
                $(".catalog").append("<li class='mtb10'><a class='cata"+(i+1)+"' href='#cata"+(i+1)+"'>"+data.catalog[i]+"</a></li>");
            }
            $(".catalog li a").eq(0).addClass("current");
            
            //3.content
            for(i=0;i<data.content.length;i++){
                $(".contentContainer").append("<div id='cata"+(i+1)+"' class='cata'><div class='dayTitle'>"+data.catalog[i]+"</div><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br></div>");
            }
            
            //4.comment
            for(i=0;i<data.comments.length;i++){
                $(".comments").prepend("<div class='comment' level='"+data.comments[i][0]+"'><img src='../images/icons/icon"+data.comments[i][1]+".jpg'><div class='response'><div class='commInfo'><span class='mr10'>"+data.comments[i][2]+"</span><span class='mr10'>"+data.comments[i][3]+"</span></div><div class='commContent'>"+data.comments[i][4]+"</div><div class='commOperation'><span class='iconfont icon-zan11'></span><span class='mr10'>"+data.comments[i][5]+"</span><span class='iconfont icon-zan1'></span><span class='mr10'>"+data.comments[i][6]+"</span><span>回复</span><span>"+data.comments[i][7]+"</span></div></div></div>");
            }
            //against click
            $(".commOperation .icon-zan1").unbind("click").bind("click",calNumber);
            //for click
            $(".commOperation .icon-zan11").unbind("click").bind("click",calNumber);
        },
        error:function(){
            alert("strategyItem error");
        }
    });
    
    $("nav .icon-zan1").click(function(){
        var value=$(this).next().html();
        if($(this).attr("class").indexOf("navColor") != -1){
            $(this).removeClass("navColor");
            $(this).next().html(--value).removeClass("navColor");
        }else{
            $(this).addClass("navColor");
            $(this).next().html(++value).addClass("navColor");
        }
/*        $.ajax({
            type:"post",
            url:"",
            data:{"forNumber":value},
            error:function(){
                
            }
        });*/
    });    
    $("nav .icon-collection-b").click(function(){
        var value=$(this).next().html();
        if($(this).attr("class").indexOf("collectColor") != -1){
            $(this).removeClass("collectColor");
            $(this).next().html(--value).removeClass("collectColor");
        }else{
            $(this).addClass("collectColor");
            $(this).next().html(++value).addClass("collectColor");
        }
/*        $.ajax({
            type:"post",
            url:"",
            data:{"forNumber":value},
            error:function(){
                
            }
        });*/
    });
    
    //catalog event
    $(window).scroll(function(){
        var days=$(".content").find(".cata");
        var catalog=$(".catalog");
        var top=$(document).scrollTop();
        var currentId="";
        days.each(function(){
            var that=$(this);
            if(top > that.offset().top - 300){
                currentId="."+that.attr("id");
            }else{
                return false;
            }
        });
        var currentLink=catalog.find(".current");
        if(currentId && currentLink.attr("href") != currentId){
            currentLink.removeClass("current");
            catalog.find(currentId).addClass("current");
        }
    });
});

function calNumber(){
    var that=$(this);
    if(!operateNumber(that)){
        return;
    }
    var numberType=that.attr("class").indexOf("icon-zan11") != -1 ? "against" : "for"
    var number=that.next().html();
    var level=that.parent().parent().parent().attr("level");
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