//1.highlight color of province?
//2.the gradient color of map
var pieData, myChart;
$(function () {

    //select2
    $(document).ready(function () {
        $('.js-example-basic-single').select2();
    });

    $.ajax({
        type: "get",
        url: "/strategy/queryUserStrategy.action",
        dataType: "json",
        success: function (data) {
            if (data.msg == "用户未登录") {
                window.location.href = "index.jsp";
            }
            pieData = data.data;
            showMap(data.data);
            for (i = 0; i < data.data.length; i++) {
                $("#strategies").append("<div class='strategy'><div class='province'><span>" + data.data[i].cityName +
                    "</span><span class='number'> ( <span>" + data.data[i].count + "</span> ) </span>" +
                    "<span class='iconfont icon-zhankai'></span><span class='iconfont icon-shouqi'></span>" +
                    "<span class='operate'>操作</span><span class='deleteAndCancle'>" +
                    "<span class='delete'>删除</span>&nbsp;<span class='cancle'>取消</span>" +
                    "</span></div><div class='all' id='all" + i + "'></div></div>");
                for (j = 0; j < data.data[i].strategyVoList.length; j++) {
                    $("#all" + i).append("<input type='checkbox' value='" + data.data[i].strategyVoList[j].id + "'>" +
                        "<div class='each' value='" + data.data[i].strategyVoList[j].id + "'>" +
                        "<img src='" + data.data[i].strategyVoList[j].mainImg + "'><div class='title'>" +
                        data.data[i].strategyVoList[j].strategyName + "</div></div>");
                }
            }
            $("#strategies .strategy .each").off("click").on("click", function () {
                window.location.href = "strategyItem.jsp?strategyId=" + $(this).attr("value");
            });
            $("#strategies .strategy").find(".icon-zhankai").hide();
            //展开
            $("#strategies .strategy .icon-zhankai").off("click").on("click", function () {
                $(this).hide().next().show();
                $(this).parent().next().show();
            });
            //收起
            $("#strategies .strategy .icon-shouqi").off("click").on("click", function () {
                $(this).hide().next().show().next().hide();
                $(this).hide().prev().show();
                $(this).parent().next().hide();
                $(this).parent().next().find("input[type='checkbox']").css("opacity", "0");
            });
            //操作
            $("#strategies .strategy .operate").off("click").on("click", function () {
                if (!$(this).prev().is(":hidden")) {
                    $(this).hide().next().show();
                    $(this).parent().next().find("input[type='checkbox']").css("opacity", "1");
                }
            });
            //取消
            $("#strategies .strategy .cancle").off("click").on("click", function () {
                $(this).parent().hide().prev().show();
                $(this).parent().parent().next().find("input[type='checkbox']").css("opacity", "0");
            });
            //删除
            $("#strategies .strategy .delete").off("click").on("click", function () {
                var parent = $(this).parent().parent().parent();
                var select = parent.find("input[type='checkbox']");
                var selectId = "";
                for (i = 0; i < select.length; i++) {
                    if ($(select[i]).is(":checked")) {
                        selectId += $(select[i]).attr("value") + ",";
                    }
                }
                selectId = selectId.substr(0, selectId.length - 1);
                if (selectId == "" || selectId == null) {
                    return;
                }
                var deleteId = selectId.split(",");
                for (i = 0; i < deleteId.length; i++) {
                    parent.find(".all").find("[value='" + deleteId[i] + "']").remove();
                }
                var totalNum = parent.find(".number span").html();
                parent.find(".number span").html(parseInt(totalNum) - deleteId.length);
                $.ajax({
                    type:"get",
                    url:"",
                    data:{},
                    dataType:"json",
                    success:function (data) {
                        
                    },
                    error:function () {
                        
                    }
                })
            });
        },
        error: function () {
            alert("init error");
        }
    });

    /* 1.我的攻略 */
    $("#navStrategy").click(function () {
        $(this).find(".icon").addClass("iconClick");
        $(this).siblings().find(".icon").removeClass("iconClick");
        $("#strategyContainer").show().siblings().hide();
    });
    //echarts
    $(window).resize(function () {
        myChart.resize();
    });
    $("#mapDot").click(function () {
        $("#strategyMap").show().siblings().hide();
    });
    $("#pieDot").click(function () {
        $("#strategyPie").show().siblings().hide();
        showPie(pieData);
    });

    /* 2.我的收藏 */
    $("#navCollect").click(function () {
        $.ajax({
            type: "get",
            url: "/strategy/queryUserCollection.action",
            dataType: "json",
            success: function (data) {
                $(".collects").empty();
                if (!data.data) {
                    $(".collects").append("<span class='nothing'>暂无收藏，去逛逛吧...</span>");
                    return;
                }
                for (i = 0; i < data.data.list.length; i++) {
                    $(".collects").append("<div class='collect' value='" + data.data.list[i].id + "'>" +
                        "<img id='mainImg' src='" + data.data.list[i].mainImg + "'><div class='collectInfo'><span>" +
                        "<span class='title'>" + data.data.list[i].strategyName + "</span><span class='author'>" +
                        data.data.list[i].username + "</span><span class='city'>" + data.data.list[i].cityName + "</span></span>" +
                        "<span class='bot'><span class='date'>" + data.data.list[i].createTime + "</span><span>" +
                        "<span class='iconfont icon-zan1'></span><span class='forNum'>" + data.data.list[i].forNum +
                        "</span><span class='iconfont icon-collection-b'></span><span>" + data.data.list[i].collectNum +
                        "</span></span></span></div></div>");
                }
                //pages
                $("#page").paging({
                    totalPage: data.data.pages,
                    callback: function (num) {
                        $.ajax({
                            type: "get",
                            url: "/strategy/queryUserCollection.action",
                            data: {"pageNum": num},
                            dataType: "json",
                            success: function (data) {
                                $(".collects").empty();
                                for (i = 0; i < data.data.list.length; i++) {
                                    $(".collects").append("<div class='collect' value='" + data.data.list[i].id + "'>" +
                                        "<img id='mainImg' src='" + data.data.list[i].mainImg + "'><div class='collectInfo'><span>" +
                                        "<span class='title'>" + data.data.list[i].strategyName + "</span><span class='author'>" +
                                        data.data.list[i].username + "</span><span class='city'>" + data.data.list[i].cityName + "</span></span>" +
                                        "<span class='bot'><span class='date'>" + data.data.list[i].createTime + "</span><span>" +
                                        "<span class='iconfont icon-zan1'></span><span class='forNum'>" + data.data.list[i].forNum +
                                        "</span><span class='iconfont icon-collection-b'></span><span>" + data.data.list[i].collectNum +
                                        "</span></span></span></div></div>");
                                }
                            }

                        });
                    }
                });
            },
            error: function () {

            }
        });
        $(this).find(".icon").addClass("iconClick");
        $(this).siblings().find(".icon").removeClass("iconClick");
        $("#collectContainer").css("display", "flex").siblings().hide();
    });
    //升序
    $(".icon-shengxu").click(function () {
        sortCollection("asc", $(this).next().next().html());
    });
    //降序
    $(".icon-jiangxu").click(function () {
        sortCollection("desc", $(this).next().html());
    });

    /* 3.上传攻略 */
    $("#navUpload").click(function () {
        $(this).find(".icon").addClass("iconClick");
        $(this).siblings().find(".icon").removeClass("iconClick");
        $("#uploadContainer").show().siblings().hide();
    });

    /* 4.修改资料 */
    var oldPhone;
    $("#navEdit").click(function () {
        $.ajax({
            type: "get",
            url: "/user/queryUserInformation.action",
            dataType: "json",
            success: function (data) {
                console.log(JSON.stringify(data));
                oldPhone = data.data.phone;
                $("#username").val(data.data.username);
                //todo 性别赋值有问题
                $("#gender").val(data.data.gender);
                $("#phone").val(data.data.phone);
                $("#city").val(data.data.city);
                $("#bio").val(data.data.bio);
            },
            error: function () {
                console.log("info error");
            }
        });
        $(this).find(".icon").addClass("iconClick");
        $(this).siblings().find(".icon").removeClass("iconClick");
        $("#editContainer").css("display", "flex").siblings().hide();
    });
    //编辑资料
    $("#editUpdate").click(function () {
        $(this).addClass("editCilck").siblings().removeClass("editCilck");
        $(".edit").show().siblings().hide();
    });
    //更新头像
    $("#avatarForm").ajaxForm(function (result) {
        //上传失败
        if (result.status == 0) {
            alert("更新失败");
            return;
        }
        //将页面头部的头像更新过来
        $("#avatar").attr("src", result.file_path);
    });
    //校验手机号格式
    $("#phone").focusout(function () {
        var newPhone = $(this).val().trim();
        if (!checkPhoneFormat(newPhone)) {    //手机号格式错误
            //todo 手机号格式错误
            return;
        }
        if (oldPhone == newPhone) {
            $("#updateBtn").attr("disabled", "true");
        } else {
            $("#updateBtn").removeAttr("disabled");
            //todo 获取验证码
        }
    });
    //更换手机号获取验证码
    $("#updateBtn").click(function () {
        $(".codeContainer").show();
    });
    //提交修改
    $("#editBtn").click(function () {
        $.ajax({
            type: "post",
            url: "/user/updateUserInformation.action",
            data: {
                "username": $("#username").val(),
                "phone": $("#phone").val(),
                "bio": $("#bio").val(),
                "gender": $("#gender").val(),
                "city": $("#city").val()
            },
            dataType: "json",
            success: function (data) {
                //更新失败
                if(data.status == 0){

                }
                //更新成功
            },
            error: function () {
                console.log("update error");
            }
        });
    });

    //修改密码
    $("#editReset").click(function () {
        $(this).addClass("editCilck").siblings().removeClass("editCilck");
        $(".reset").show().siblings().hide();
    });
    $(".oldPwd").focusout(function () {
        $(".newPwd").removeAttr("disabled");
    });
    $(".newPwd").focusout(function () {
        $(".rePwd").removeAttr("disabled");
    });
    $(".rePwd").focusout(function () {
        if ($(".newPwd").val().trim() != $(this).val().trim()) {
            alert("different");
            //todo 两次密码不一致
            return;
        }
        $("#resetBtn").removeAttr("disabled");
    });
    //重置密码
    $("#resetBtn").click(function () {
        $.ajax({
            type: "post",
            url: "/user/resetPassword.action",
            data: {"passwordOld": $(".oldPwd").val(), "passwordNew": $(".newPwd").val()},
            dataType: "json",
            success: function (data) {
                //todo
                alert(data.msg);
            },
            error: function () {
                console.log("reset error");
            }
        });
    });

    /* 5.消息中心 */
    //todo list feedback + dwr
    $("#navMsg").click(function () {
        $(this).find(".icon").addClass("iconClick");
        $(this).siblings().find(".icon").removeClass("iconClick");
        $.ajax({
            type: "post",
            url: "",
            dataType: "json",
            success: function (data) {
                var data = {"msg": [[]]};
            },
            error: function () {

            }
        });
        $("#msgContainer").show().siblings().hide();
    });

});

function showPie(pieData) {
    var province = new Array();
    var count = new Array();
    for (i = 0; i < pieData.length; i++) {
        province.push(pieData[i].cityName);
        count.push(pieData[i].count);
    }
    myChart = echarts.init(document.getElementById("strategyPie"));
    option = {
        tooltip: {
            trigger: 'item',
            formatter: "{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            bottom: 'bottom',
            data: province
        },
        series: [
            {
                name: '',
                type: 'pie',
                radius: ['20%', '55%'],
                center: ['50%', '50%'],
                data: [
                    {value: count[0], name: province[0]},
                    {value: count[1], name: province[1]},
                    {value: count[2], name: province[2]},
                    {value: count[3], name: province[3]},
                    {value: count[4], name: province[4]},
                    {value: count[5], name: province[5]},
                    {value: count[6], name: province[6]},
                    {value: count[7], name: province[7]},
                    {value: count[8], name: province[8]},
                    {value: count[9], name: province[9]},
                    {value: count[10], name: province[10]},
                    {value: count[11], name: province[11]},
                    {value: count[12], name: province[12]},
                    {value: count[13], name: province[13]}
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

function showMap(datas) {
    var color = new Array();
    var i;
    for (i = 0; i < datas.length; i++) {
        color[datas[i].cityId - 1] = datas[i].count;
    }
    for (i = 0; i < 35; i++) {
        (typeof(color[i]) == "undefined") ? color[i] = 0 : ""
    }
    myChart = echarts.init(document.getElementById("strategyMap"));
    option = {
        tooltip: {
            formatter: "{b} : {c}"
        },
        visualMap: {
            min: 0,
            max: 20,
            left: 'left',
            top: 'bottom',
            //text: ['High','Low'],
            seriesIndex: [1],   //collect data from series
            inRange: {
//                color: ['#8EAD9C', '#286042']
                color: ['#d8dcf9', '#0A1451']
            },
            calculable: true   //triangle and the limit number
        },
        geo: {
            map: 'china',
            roam: 'scale',
            scaleLimit: {
                max: 1.2,
                min: 0.8
            },
            label: {
                emphasis: {
                    show: false,
                }
            },
            itemStyle: {
                normal: {
                    borderColor: 'transparent',
                },
                emphasis: {
                    shadowBlur: 20,
                    shadowColor: 'rgba(0, 0, 0, 0.5)',
                    color: '#000211', //highlight color
                }
            }
        },
        series: [
            {
                type: 'scatter',
                coordinateSystem: 'geo',
                symbolSize: 0,
            },
            {
                name: 'color of each province',
                type: 'map',
                geoIndex: 0,
                data: [
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

function getPath(node) {

    var path = "";
    try {
        var file = null;
        if (node.files && node.files[0]) {
            file = node.files[0];
        } else if (node.files && node.files.item(0)) {
            file = node.files.item(0);
        }
        //Firefox 因安全性问题已无法直接通过input[file].value 获取完整的文件路径
        try {
            //Firefox7.0
            path = file.getAsDataURL();
        } catch (e) {
            //Firefox8.0以上
            path = window.URL.createObjectURL(file);
        }
    } catch (e) {
        //支持html5的浏览器,比如高版本的firefox、chrome、ie10
        if (node.files && node.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                path = e.target.result;
            };
            reader.readAsDataURL(node.files[0]);
        }
    }
    $("#usericon").attr("src", path);
}

function sortCollection(order, parameter) {
    var orderBy = "";
    if (parameter.indexOf("城市") != -1) {
        orderBy = "city_id|" + order;
    } else if (parameter.indexOf("时间") != -1) {
        orderBy = "s.create_time|" + order;
    } else if (parameter.indexOf("收藏数") != -1) {
        orderBy = "for_num|" + order;
    } else if (parameter.indexOf("点赞数") != -1) {
        orderBy = "collect_num|" + order;
    }
    $.ajax({
        type: "get",
        url: "/strategy/queryUserCollection.action",
        data: {"orderBy": orderBy},
        dataType: "json",
        success: function (data) {
            $(".collects").empty();
            for (i = 0; i < data.data.list.length; i++) {
                $(".collects").append("<div class='collect' value='" + data.data.list[i].id + "'>" +
                    "<img id='mainImg' src='" + data.data.list[i].mainImg + "'><div class='collectInfo'><span>" +
                    "<span class='title'>" + data.data.list[i].strategyName + "</span><span class='author'>" +
                    data.data.list[i].username + "</span><span class='city'>" + data.data.list[i].cityName + "</span></span>" +
                    "<span class='bot'><span class='date'>" + data.data.list[i].createTime + "</span><span>" +
                    "<span class='iconfont icon-zan1'></span><span class='forNum'>" + data.data.list[i].forNum +
                    "</span><span class='iconfont icon-collection-b'></span><span>" + data.data.list[i].collectNum +
                    "</span></span></span></div></div>");
            }
            //pages
            $("#page").paging({
                totalPage: data.data.pages,
                callback: function (num) {
                    $.ajax({
                        type: "get",
                        url: "/strategy/queryUserCollection.action",
                        data: {"pageNum": num},
                        dataType: "json",
                        success: function (data) {
                            $(".collects").empty();
                            for (i = 0; i < data.data.list.length; i++) {
                                $(".collects").append("<div class='collect' value='" + data.data.list[i].id + "'>" +
                                    "<img id='mainImg' src='" + data.data.list[i].mainImg + "'><div class='collectInfo'><span>" +
                                    "<span class='title'>" + data.data.list[i].strategyName + "</span><span class='author'>" +
                                    data.data.list[i].username + "</span><span class='city'>" + data.data.list[i].cityName + "</span></span>" +
                                    "<span class='bot'><span class='date'>" + data.data.list[i].createTime + "</span><span>" +
                                    "<span class='iconfont icon-zan1'></span><span class='forNum'>" + data.data.list[i].forNum +
                                    "</span><span class='iconfont icon-collection-b'></span><span>" + data.data.list[i].collectNum +
                                    "</span></span></span></div></div>");
                            }
                        }

                    });
                }
            });
        }

    });
}