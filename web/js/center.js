$(function(){
    $.ajax({
/*        type:"post",
        url:"",
        dataType:"json",*/
        success:function(data){
            var data={"userMap":[[1,2],[4,11],[6,1],[13,1],[24,1]]};
            showMap(data);
        },
        error:function(){
            alert("center init error");
        }
    });
    function showMap(data){
        var color=new Array();
        for(var i = 0; i < data.userMap.length; i++){
            color[data.userMap[i][0]-1]=data.userMap[i][1];
        }
        for(var i=0;i<35;i++){
            if(typeof(color[i]) == "undefined"){
                color[i]="#f2f2f2";
            }else if(color[i] < 5){
                color[i]="#95A6FE";
            }else if(color[i] < 10){
                color[i]="#7A90FB";
            }else if(color[i] < 15){
                color[i]="#546FF9";
            }else if(color[i] < 20){
                color[i]="#3151FA";
            }else{
                color[i]="#1E42FA";
            }
        }
        var myChart=echarts.init(document.getElementById("personalmap"));
        // 只是不同颜色
        option = {
            title: {
                text: '',
                subtext: '',
                x: 'center',
                textStyle: {
                    color: '#424242'
                }
            },
            //hover
            tooltip: {
                trigger:'item',
                formatter:"{b}:{c}"
            },
            dataRange: {
               x: '-1000px',//图例横轴位置
               y: '-1000px',//图例纵轴位置
               //各省地图颜色；start：值域开始值；end：值域结束值；label：图例名称；color：自定义颜色值；
               splitList: [
                    {start:1, end:1, label: '安徽', color: color[0]},
                    {start:2, end:2, label: '澳门', color: color[1]},
                    {start:3, end:3, label: '北京', color: color[2]},
                    {start:4, end:4, label: '重庆', color: color[3]},
                    {start:5, end:5, label: '福建', color: color[4]},
                    {start:6, end:6, label: '吉林', color: color[5]},
                    {start:7, end:7, label: '江苏', color: color[6]},
                    {start:8, end:8, label: '江西', color: color[7]},
                    {start:9, end:9, label: '海南', color: color[8]},
                    {start:10, end:10, label: '河北', color: color[9]},
                    {start:11, end:11, label: '河南', color: color[10]},
                    {start:12, end:12, label: '黑龙江', color: color[11]},
                    {start:13, end:13, label: '湖北', color: color[12]},
                    {start:14, end:14, label: '湖南', color: color[13]},
                    {start:15, end:15, label: '甘肃', color: color[14]},
                    {start:16, end:16, label: '广东', color: color[15]},
                    {start:17, end:17, label: '广西', color: color[16]},
                    {start:18, end:18, label: '贵州', color: color[17]},
                    {start:19, end:19, label: '辽宁', color: color[18]},
                    {start:20, end:20, label: '南海诸岛', color: color[19]},
                    {start:21, end:21, label: '内蒙古', color: color[20]},
                    {start:22, end:22, label: '宁夏', color: color[21]},
                    {start:23, end:23, label: '青海', color: color[22]},
                    {start:24, end:24, label: '山东', color: color[23]},
                    {start:25, end:25, label: '山西', color: color[24]},
                    {start:26, end:26, label: '陕西', color: color[25]},
                    {start:27, end:27, label: '上海', color: color[26]},
                    {start:28, end:28, label: '四川', color: color[27]},
                    {start:29, end:29, label: '台湾', color: color[28]},
                    {start:30, end:30, label: '天津', color: color[29]},
                    {start:31, end:31, label: '西藏', color: color[30]},
                    {start:32, end:32, label: '香港', color: color[31]},
                    {start:33, end:33, label: '新疆', color: color[32]},
                    {start:34, end:34, label: '云南', color: color[33]},
                    {start:35, end:35, label: '浙江', color: color[34]},

               ]
           },
            geo: {
                //map: 'china',   //地图的类型
                //roam: 'move',   //开启平移漫游
                // scaleLimit:{
                //   max:'1.2',
                //   min:'0.7'
                // },
    /*            label: {
                    normal: {
                        show: true,
                        textStyle: {
                            color: 'rgba(0,0,0,1)'
                        }
                    }
                },*/
    /*            itemStyle: {
                    normal: {
                        borderColor: 'rgba(0, 0, 0, 0.2)'
                    },
                    emphasis: {
                        areaColor: null,
                        shadowOffsetX: 0,
                        shadowOffsetY: 0,
                        shadowBlur: 20,
                        borderWidth: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }*/
            },
            series : [
           {
               name: '随机数据',
               type: 'map',
               mapType: 'china',
               selectedMode : 'single',
               itemStyle:{
                   normal:{
                        label:{show:true},
                        //borderWidth:1,//省份的边框宽度
                        borderColor:'transparent',//省份的边框颜色
                        color:'#ece2df'//地图背景颜色
                        //areaStyle:{color:'#f60'}//设置地图颜色
                   },
                   emphasis:{label:{show:true}}
               },
               //各省地图颜色数据依赖value
               data:[
                   {name: '安徽', selected:false,value:1},
                   {name: '澳门', selected:false,value:2},
                   {name: '北京', selected:false,value:3},
                   {name: '重庆', selected:false,value:4},
                   {name: '福建', selected:false,value:5},
                   {name: '吉林', selected:false,value:6},
                   {name: '江苏', selected:false,value:7},
                   {name: '江西', selected:false,value:8},
                   {name: '海南', selected:false,value:9},
                   {name: '河北', selected:false,value:10},
                   {name: '河南', selected:false,value:11},
                   {name: '黑龙江', selected:false,value:12},
                   {name: '湖北', selected:false,value:13},
                   {name: '湖南', selected:false,value:14},
                   {name: '甘肃', selected:false,value:15},
                   {name: '广东', selected:false,value:16},
                   {name: '广西', selected:false,value:17},
                   {name: '贵州', selected:false,value:18},
                   {name: '辽宁', selected:false,value:19},
                   {name: '南海诸岛', selected:false,value:20},
                   {name: '内蒙古', selected:false,value:21},
                   {name: '宁夏', selected:false,value:22},
                   {name: '青海', selected:false,value:23},
                   {name: '山东', selected:false,value:24},
                   {name: '山西', selected:false,value:25},
                   {name: '陕西', selected:false,value:26},
                   {name: '上海', selected:false,value:27},
                   {name: '四川', selected:false,value:28},
                   {name: '台湾', selected:false,value:29},
                   {name: '天津', selected:false,value:30},
                   {name: '西藏', selected:false,value:31},
                   {name: '香港', selected:false,value:32},
                   {name: '新疆', selected:false,value:33},
                   {name: '云南', selected:false,value:34},
                   {name: '浙江', selected:false,value:35},
               ]
           }
       ]
        };
        myChart.setOption(option);
    }
})