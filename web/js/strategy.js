//notice: the length of strategy's title is no more than 14 words
$(function(){
    //select2
    $(document).ready(function() {
        $('.js-example-basic-single').select2();
    });
    
    $.ajax({
/*        type:"post",
        url:"",
        dataType:"json",*/
        success:function(data){
            var data={"province":[['1','安徽'],['2','澳门'],['3','北京'],['4','重庆'],['5','福建'],['6','吉林'],['7','江苏'],['8','江西'],['9','海南'],['10','河北'],['11','河南'],['12','黑龙江'],['13','湖北'],['14','湖南'],['15','甘肃'],['16','广东'],['17','广西'],['18','贵州'],['19','辽宁'],['20','南海诸岛'],['21','内蒙古'],['22','宁夏'],['23','青海'],['24','山东'],['25','山西'],['26','陕西'],['27','上海'],['28','四川'],['29','台湾'],['30','天津'],['31','西藏'],['32','香港'],['33','新疆'],['34','云南'],['35','浙江']],"duration":[["3天"],["5天"],["7天"],["15天"],["30天+"]],"strategies":
            [
                ["1","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游1","香港 3天"],
                ["4","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游2","香港 3天"],
                ["6","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游3","香港 3天"],
                ["7","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游4","香港 3天"],
                ["9","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游5","香港 3天"],
                ["12","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游6","香港 3天"],
                ["15","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游7","香港 3天"],
                ["21","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游8","香港 3天"],
                ["1","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游1","香港 3天"],
                ["4","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游2","香港 3天"],
                ["6","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游3","香港 3天"],
                ["7","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游4","香港 3天"],
                ["9","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游5","香港 3天"],
                ["12","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游6","香港 3天"],
                ["15","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游7","香港 3天"],
                ["21","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游8","香港 3天"],
                ["1","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游1","香港 3天"],
                ["4","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游2","香港 3天"],
                ["6","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游3","香港 3天"],
                ["7","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游4","香港 3天"],
                ["9","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游5","香港 3天"],
                ["12","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游6","香港 3天"],
                ["15","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游7","香港 3天"],
                ["21","../images/notfound.jpg","../images/icons/icon1.jpg","Lily","香港三日游8","香港 3天"]
            ]};
            packageData(data);
        },
        error:function(){
            alert("query resource error");
        }
    });
    
});

function packageData(data){
    //1.province
    if(data.province){
        for(i=0;i<data.province.length;i++){
            $(".province").append("<option value='"+data.province[i][0]+"'>"+data.province[i][1]+"</option>");
        }
        //province click event
        $(".province").change(function(){
            updateStrategies();
        });
    }
    //2.duration
    if(data.duration){
        for(i=0;i<data.duration.length;i++){
            $(".duration").append("<option>"+data.duration[i][0]+"</option>")
        }
        //duration click event
        $(".duration").change(function(){
            updateStrategies();
        });
    }
    //3.strategies
    $(".content").empty();
    if(data.strategies){
        for(i=0;i<data.strategies.length;i++){            
            $(".content").append("<div value='"+data.strategies[i][0]+"' class='strategy'><img id='strategyBg' src='"+data.strategies[i][1]+"'><div class='author'><img id='avatar' src='"+data.strategies[i][2]+"'><span class='user'>"+data.strategies[i][3]+"</span></div><div class='s_msg'><div><span class='iconfont icon-biaoti'></span><span class='title'>"+data.strategies[i][4]+"</span></div><div><span class='iconfont icon-icontag'></span><span class='tags'>"+data.strategies[i][5]+"</span></div></div></div>");
        }
        $(".strategy #avatar").off("mouseover").on("mouseover",function(){
            $(this).css("opacity","0.5");
            $(this).next().show();
        });
        $(".strategy #avatar").off("mouseout").on("mouseout",function(){
            $(this).css("opacity","1");
            $(this).next().hide();
        });
        //strategies click
        $(".content .strategy").off("click").on("click",function(){ alert($(".province").find(":selected").text()+","+$(".duration").find(":selected").val()+","+$(this).attr("value"));
        });
    }
}

function updateStrategies(){
    $.ajax({
/*            type:"post",
        url:"",
        data:{"provinceId":$(".province").val(),"duration":$(".duration").find(":selected").val()},
        dataType:"json",*/
        success:function(data){
            var data={"strategies":[
                ["1","../images/systemerror.jpg","../images/icons/icon1.jpg","Lucy","海南十五日游1","海南 15天"],
                ["4","../images/systemerror.jpg","../images/icons/icon2.jpg","Lucy","海南十五日游2","海南 15天"],
                ["6","../images/systemerror.jpg","../images/icons/icon3.jpg","Lucy","海南十五日游3","海南 15天"],
                ["7","../images/systemerror.jpg","../images/icons/icon4.jpg","Lucy","海南十五日游4","海南 15天"],
                ["9","../images/systemerror.jpg","../images/icons/icon5.jpg","Lucy","海南十五日游5","海南 15天"],
                ["12","../images/systemerror.jpg","../images/icons/icon6.jpg","Lucy","海南十五日游6","海南 15天"],
                ["15","../images/systemerror.jpg","../images/icons/icon4.jpg","Lucy","海南十五日游7","海南 15天"],
                ["21","../images/systemerror.jpg","../images/icons/icon2.jpg","Lucy","海南十五日游8","海南 15天"]
            ]};
            packageData(data);
        },
        error:function(){
            alert("updateStrategies error");
        }
    });
}