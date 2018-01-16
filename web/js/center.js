//todo 1.highlight color of province? 2.the gradient color of map
$(function(){
    var myChart,data;
    $(window).resize(function(){
        myChart.resize();
    });
    $("#mapDot").click(function(){
        $("#strategyMap").css("display","block");
        $("#strategyPie").css("display","none");
    });
    $("#pieDot").click(function(){
        $("#strategyMap").css("display","none");
        $("#strategyPie").css("display","block");
        showPie(data);
    });
    
    $.ajax({
/*        type:"post",
        url:"",
        dataType:"json",*/
        success:function(){
/*            var data={"userMap":[[1,7],[2,11],[3,15],[4,12],[5,7],[6,10],[7,2],[8,11],[9,2],[10,1],[11,5],[12,1],[13,3],[14,11],[15,2],[16,1],[17,5],[18,1],[19,2],[20,11],[21,11],[22,4],[23,5],[24,1],[25,2],[26,11],[27,1],[28,1],[29,5],[30,1],[31,2],[32,11],[34,1],[35,9]]};*/
            var datas={"userMap":[[1,15,'安徽'],[2,2,'澳门'],[3,1,'北京'],[4,2,'重庆'],[5,1,'福建'],[6,1,'吉林'],[8,8,'江西'],[12,2,'黑龙江'],[15,2,'甘肃'],[18,2,'贵州'],[20,1,'南海诸岛'],[23,8,'青海'],[27,4,'上海'],[35,4,'浙江']]};
            data=datas;
            showMap(datas);
        },
        error:function(){
            alert("init error");
        }
    });
    
    function showPie(data){
        var province=new Array();
        for(i=0;i<data.userMap.length;i++){
            province[i]=data.userMap[i][2];
        }
        myChart=echarts.init(document.getElementById("strategyPie"));
        option = {
            tooltip : {
                trigger: 'item',
                formatter: "{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                bottom: 'bottom',
                data: province
            },
            series : [
            {
                name: '',
                type: 'pie',
                radius : ['20%','55%'],
                center: ['50%', '50%'],
                data:[
                    {value: 15, name: province[0]},
                    {value: 2, name: province[1]},
                    {value: 1, name: province[2]},
                    {value: 2, name: province[3]},
                    {value: 1, name: province[4]},
                    {value: 1, name: province[5]},
                    {value: 8, name: province[6]},
                    {value: 2, name: province[7]},
                    {value: 2, name: province[8]},
                    {value: 2, name: province[9]},
                    {value: 1, name: province[10]},
                    {value: 8, name: province[11]},
                    {value: 4, name: province[12]},
                    {value: 4, name: province[13]}
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
        };
        myChart.setOption(option);
    }
    
    function showMap(datas){
        var color=new Array();
        var i;
        for(i = 0; i < datas.userMap.length; i++){
            color[datas.userMap[i][0]-1]=datas.userMap[i][1];
        }
        for(i=0; i< 35; i++){
            (typeof(color[i]) == "undefined") ? color[i]=0 : ""
        }
        myChart=echarts.init(document.getElementById("strategyMap"));
        option = {
            tooltip:{
                formatter:"{b} : {c}"
            },
            visualMap: {
                min: 0,
                max: 20,
                left: 'left',
                top: 'bottom',
                //text: ['High','Low'],
                seriesIndex: [1],   //collect data from series
                inRange: {
                    color: ['#d8dcf9', '#0A1451']
                },
                calculable : true   //triangle and the limit number
            },
            geo: {
                map: 'china',
                roam: 'scale',
                scaleLimit:{
                    max:1.2,
                    min:0.8
                },
                label:{
                    emphasis:{
                        show:false,
                    }
                },
                itemStyle: {
                    normal:{
                        borderColor: 'transparent',
                    },
                    emphasis:{
                        shadowBlur: 20,
                        shadowColor: 'rgba(0, 0, 0, 0.5)',
                        color:'#000211', //highlight color
                    }
                }
            },
            series : [
            {
               type: 'scatter',
               coordinateSystem: 'geo',
               symbolSize: 0,
            },
            {
                name: 'color of each province',
                type: 'map',
                geoIndex: 0,
                data:[
                    {name: '安徽', value: color[0]},
                    {name: '澳门', value: color[1]},
                    {name: '北京', value: color[2]},
                    {name: '重庆', value: color[3]},
                    {name: '福建', value: color[4]},
                    {name: '吉林', value: color[5]},
                    {name: '江苏', value: color[6]},
                    {name: '江西', value: color[7]},
                    {name: '海南', value: color[8]},
                    {name: '河北', value: color[9]},
                    {name: '河南', value: color[10]},
                    {name: '黑龙江', value: color[11]},
                    {name: '湖北', value: color[12]},
                    {name: '湖南', value: color[13]},
                    {name: '甘肃', value: color[14]},
                    {name: '广东', value: color[15]},
                    {name: '广西', value: color[16]},
                    {name: '贵州', value: color[17]},
                    {name: '辽宁', value: color[18]},
                    {name: '南海诸岛', value: color[19]},
                    {name: '内蒙古', value: color[20]},
                    {name: '宁夏', value: color[21]},
                    {name: '青海', value: color[22]},
                    {name: '山东', value: color[23]},
                    {name: '山西', value: color[24]},
                    {name: '陕西', value: color[25]},
                    {name: '上海', value: color[26]},
                    {name: '四川', value: color[27]},
                    {name: '台湾', value: color[28]},
                    {name: '天津', value: color[29]},
                    {name: '西藏', value: color[30]},
                    {name: '香港', value: color[31]},
                    {name: '新疆', value: color[32]},
                    {name: '云南', value: color[33]},
                    {name: '浙江', value: color[34]}
                ]
            }]
        };
        myChart.setOption(option);
    }
});