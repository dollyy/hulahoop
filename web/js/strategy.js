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
            var data={"province":[['1','安徽'],['2','澳门'],['3','北京'],['4','重庆'],['5','福建'],['6','吉林'],['7','江苏'],['8','江西'],['9','海南'],['10','河北'],['11','河南'],['12','黑龙江'],['13','湖北'],['14','湖南'],['15','甘肃'],['16','广东'],['17','广西'],['18','贵州'],['19','辽宁'],['20','南海诸岛'],['21','内蒙古'],['22','宁夏'],['23','青海'],['24','山东'],['25','山西'],['26','陕西'],['27','上海'],['28','四川'],['29','台湾'],['30','天津'],['31','西藏'],['32','香港'],['33','新疆'],['34','云南'],['35','浙江']],"duration":[["3天"],["5天"],["7天"],["15天"],["30天+"]],"strategies":[["1","1","香港新疆云南浙江1","Lily","香港 + 3天"],["4","1","香港新疆云南浙江2","Lily","香港 3天"],["6","1","香港新疆云南浙江3","Lily","香港 3天"],["7","1","香港新疆云南浙江4","Lily","香港 3天"],["9","1","香港新疆云南浙江5","Lily","香港 3天"],["12","1","香港新疆云南浙江6","Lily","香港 3天"],["15","1","香港新疆云南浙江7","Lily","香港 3天"],["21","1","香港新疆云南浙江8","Lily","香港 3天"],["1","1","香港新疆云南浙江1","Lily","香港 3天"],["4","1","香港新疆云南浙江2","Lily","香港 3天"],["6","1","香港新疆云南浙江3","Lily","香港 3天"],["7","1","香港新疆云南浙江4","Lily","香港 3天"],["9","1","香港新疆云南浙江5","Lily","香港 3天"],["12","1","香港新疆云南浙江6","Lily","香港 3天"],["15","1","香港新疆云南浙江7","Lily","香港 3天"],["21","1","香港新疆云南浙江8","Lily","香港 3天"],["1","1","香港新疆云南浙江1","Lily","香港 3天"],["4","1","香港新疆云南浙江2","Lily","香港 3天"],["6","1","香港新疆云南浙江3","Lily","香港 3天"],["7","1","香港新疆云南浙江4","Lily","香港 3天"],["9","1","香港新疆云南浙江5","Lily","香港 3天"],["12","1","香港新疆云南浙江6","Lily","香港 3天"],["15","1","香港新疆云南浙江7","Lily","香港 3天"],["21","1","香港新疆云南浙江8","Lily","香港 3天"]]};
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
    if(data.strategies){
        $(".content").empty();
        for(i=0;i<data.strategies.length;i++){
            $(".content").append("<div value='"+data.strategies[i][0]+"' class='container'><div class='strategy'><img src='../images/icon"+data.strategies[i][1]+".jpg'><div class='s_msg'><div><span class='iconfont icon-dizhi'></span><span class='title'>"+data.strategies[i][2]+"</span></div><div><span class='iconfont icon-ren'></span><span class='user'>"+data.strategies[i][3]+"</span></div><div><span class='iconfont icon-icontag'></span><span class='tags'>"+data.strategies[i][4]+"</span></div></div></div></div>");
        }
        //strategies click
        $(".content .container").off("click").on("click",function(){
            alert($(".province").find(":selected").text()+","+$(".duration").find(":selected").val()+","+$(this).attr("value"));
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
            var data={"strategies":[["1","2","海南河北河南黑龙江1","Lucy","海南 15天"],["4","2","海南河北河南黑龙江2","Lucy","海南 15天"],["6","2","海南河北河南黑龙江3","Lucy","海南 15天"],["7","2","海南河北河南黑龙江4","Lucy","海南 15天"],["9","2","海南河北河南黑龙江5","Lucy","海南 15天"],["12","2","海南河北河南黑龙江6","Lucy","海南 15天"],["15","2","海南河北河南黑龙江7","Lucy","海南 15天"],["21","2","海南河北河南黑龙江8","Lucy","海南 15天"]]};
            packageData(data);
        },
        error:function(){
            alert("updateStrategies error");
        }
    });
}