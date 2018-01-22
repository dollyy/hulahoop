$(function(){
    var i;
    $.ajax({
/*        type:"post",
        url:"",
        dataType:"json",*/
        success:function(data){
            var data={"province":[['1','安徽'],['2','澳门'],['3','北京'],['4','重庆'],['5','福建'],['6','吉林'],['7','江苏'],['8','江西'],['9','海南'],['10','河北'],['11','河南'],['12','黑龙江'],['13','湖北'],['14','湖南'],['15','甘肃'],['16','广东'],['17','广西'],['18','贵州'],['19','辽宁'],['20','南海诸岛'],['21','内蒙古'],['22','宁夏'],['23','青海'],['24','山东'],['25','山西'],['26','陕西'],['27','上海'],['28','四川'],['29','台湾'],['30','天津'],['31','西藏'],['32','香港'],['33','新疆'],['34','云南'],['35','浙江']],
            "duration":[["1","3天"],["2","5天"],["3","7天"],["4","15天"],["5","30天+"]]};
            //1.province
            for(i=0;i<data.province.length;i++){
                $(".province").append("<span value='"+data.province[i][0]+"'>"+data.province[i][1]+"</span>");
            }
            //province click event
            $(".province span").off("click").on("click",function(){
                $(this).addClass("hover").siblings().removeClass("hover");
                console.log($(this).attr("value"));
            });
            //2.duration
            for(i=0;i<data.duration.length;i++){
                $(".duration").append("<span value='"+data.duration[i][0]+"'>"+data.duration[i][1]+"</span>")
            }
            //duration click event
            $(".duration span").off("click").on("click",function(){
                $(this).addClass("hover").siblings().removeClass("hover");
                console.log($(this).attr("value"));
            });
        },
        error:function(){
            alert("query resource error");
        }
    });
});