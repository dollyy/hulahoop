var pieData, myChart, i, userAvatar, forClass;
$(function () {
    var parameter = (location.search.length > 0 ? location.search.substring(1) : "");
    if ("message" == parameter) {
        msgClick();
    }

    //click bg
    $("#bg").height($(document).height()).width($(document).width());
    $("#bg").click(function () {
        history.go(0);
    });

    //select2
    $(document).ready(function () {
        $('.js-example-basic-single').select2();
    });

    //获取当前用户信息
    $.ajax({
        type: "get",
        url: "/user/queryUserInformation.action",
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "index.jsp";
                return;
            }
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            userAvatar = data.data.avatar;
            $("#headuser").html(data.data.username);  //用户名
            $("#avatar").attr("src", userAvatar);    //用户头像
        },
        error: function () {
            window.location.href = "systemError.jsp";
        }
    });

    //获取用户的攻略
    $.ajax({
        type: "get",
        url: "/strategy/queryUserStrategy.action",
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "index.jsp";
                return;
            }
            if (data.status == -1) {  //没有匹配信息
                $(".strategyNothing").html("您还没有上传过攻略，现在去上传吧...").css("opacity", 1);
                $("#pageDot").hide();
                $("#separate").hide();
                return;
            }
            $(".strategyNothing").css("opacity", 0);
            $("#pageDot").show();
            $("#separate").show();
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
                    $("#all" + i).append("<input type='checkbox' value='" + data.data[i].strategyVoList[j].strategyId + "'>" +
                        "<div class='each' value='" + data.data[i].strategyVoList[j].strategyId + "'>" +
                        "<img src='" + data.data[i].strategyVoList[j].mainImg + "'><div class='title'>" +
                        data.data[i].strategyVoList[j].strategyName + "</div></div>");
                }
            }
            var strategyDom = $("#strategies .strategy");
            //前往攻略详情页面
            strategyDom.find(".each").off("click").on("click", function () {
                window.location.href = "strategyItem.jsp?strategyId=" + $(this).attr("value");
            });
            //隐藏所有的展开
            strategyDom.find(".icon-zhankai").hide();
            //展开的点击事件
            strategyDom.find(".icon-zhankai").off("click").on("click", function () {
                $(this).hide().next().show();
                $(this).parent().next().show();
            });
            //收起的点击事件
            strategyDom.find(".icon-shouqi").off("click").on("click", function () {
                $(this).hide().next().show().next().hide();
                $(this).hide().prev().show();
                $(this).parent().next().hide();
                $(this).parent().next().find("input[type='checkbox']").css("opacity", "0");
            });
            //操作的点击事件
            strategyDom.find(".operate").off("click").on("click", function () {
                if (!$(this).prev().is(":hidden")) {
                    $(this).hide().next().show();
                    $(this).parent().next().find("input[type='checkbox']").css("opacity", "1");
                }
            });
            //取消的点击事件
            strategyDom.find(".cancle").off("click").on("click", function () {
                $(this).parent().hide().prev().show();
                $(this).parent().parent().next().find("input[type='checkbox']").css("opacity", "0");
            });
            //删除的点击事件
            strategyDom.find(".delete").off("click").on("click", function () {
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
                $.ajax({
                    type: "get",
                    url: "/strategy/delete.action",
                    data: {"strategyId": selectId},
                    dataType: "json",
                    success: function (data) {
                        if (data.status == 0) {
                            alert(data.msg);
                            return;
                        }
                        var deleteId = selectId.split(",");
                        for (i = 0; i < deleteId.length; i++) {
                            parent.find(".all").find("[value='" + deleteId[i] + "']").remove();
                        }
                        var totalNum = parent.find(".number span").html();
                        parent.find(".number span").html(parseInt(totalNum) - deleteId.length);
                    },
                    error: function () {
                        window.location.href = "systemError.jsp";
                    }
                });
            });
        },
        error: function () {
            window.location.href = "systemError.jsp";
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
        if (myChart == undefined) {
            return;
        }
        myChart.resize();
    });
    $("#mapDot").click(function () {
        $("#strategyMap").show().siblings().hide();
        showMap(pieData);
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
                if (data.status == -2) {    //用户未登录
                    window.location.href = "index.jsp";
                    return;
                }
                $(".collects").empty();
                if (data.status == -1) {  //没有匹配信息
                    $(".collectNothing").html("暂无收藏，去逛逛吧...").css("opacity", 1);
                    return;
                }
                $(".collectCatalog").show();
                $(".collectNothing").css("opacity", 0);
                for (i = 0; i < data.data.list.length; i++) {
                    forClass = data.data.list[i].forStatus == 1 ? "iconfont icon-zan1 forNum navColor" : "iconfont icon-zan1 forNum";
                    $(".collects").append("<div class='collect' value='" + data.data.list[i].id + "'>" +
                        "<img id='mainImg' src='" + data.data.list[i].mainImg + "'><div class='collectInfo'><span>" +
                        "<span class='title'><a href='strategyItem.jsp?strategyId=" + data.data.list[i].id + "'>" + data.data.list[i].strategyName + "</a></span><span class='author'>" +
                        data.data.list[i].username + "</span><span class='city'>" + data.data.list[i].cityName + "</span></span>" +
                        "<span class='bot'><span class='date'>" + data.data.list[i].createTime + "</span><span>" +
                        "<span class='" + forClass + "'></span><span>" + data.data.list[i].forNum + "</span>&nbsp;&nbsp;" +
                        "<span class='iconfont icon-collection-b collectColor'></span><span>" + data.data.list[i].collectNum +
                        "</span></span></span></div></div>");
                }
                //点赞攻略
                $(".icon-zan1").off("click").on("click", likeClick);
                //收藏攻略
                $(".icon-collection-b").off("click").on("click", collectClick);
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
                                    forClass = data.data.list[i].forStatus == 1 ? "forNum navColor" : "forNum";
                                    $(".collects").append("<div class='collect' value='" + data.data.list[i].id + "'>" +
                                        "<img id='mainImg' src='" + data.data.list[i].mainImg + "'>" +
                                        "<div class='collectInfo'><span><span class='title'><a href='strategyItem.jsp?strategyId=" + data.data.list[i].id + "'>"
                                        + data.data.list[i].strategyName + "</a></span><span class='author'>" +
                                        data.data.list[i].username + "</span><span class='city'>" +
                                        data.data.list[i].cityName + "</span></span><span class='bot'><span class='date'>"
                                        + data.data.list[i].createTime + "</span><span><span class='iconfont icon-zan1'>" +
                                        "</span><span class='" + forClass + "'>" + data.data.list[i].forNum + "</span>" +
                                        "<span class='iconfont icon-collection-b collectColor'></span><span>" +
                                        data.data.list[i].collectNum + "</span></span></span></div></div>");
                                }
                                //点赞攻略
                                $(".icon-zan1").off("click").on("click", likeClick);
                                //收藏攻略
                                $(".icon-collection-b").off("click").on("click", collectClick);
                            },
                            error: function () {
                                window.location.href = "systemError.jsp";
                            }

                        });
                    }
                });
            },
            error: function () {
                window.location.href = "systemError.jsp";
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

    /* 3.修改资料 */
    var oldEmail;
    //编辑资料
    $("#navEdit").click(function () {
        $.ajax({
            type: "get",
            url: "/user/queryUserInformation.action",
            dataType: "json",
            success: function (data) {
                if (data.status == -2) {    //用户未登录
                    window.location.href = "index.jsp";
                    return;
                }
                oldEmail = data.data.email;
                $("#centerUsername").val(data.data.username);
                $("#gender").val(data.data.gender);
                $("#email").val(data.data.email);
                $("#city").val(data.data.city);
                $("#bio").val(data.data.bio);
                $("#usericon").attr("src", data.data.avatar);
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
        $(this).find(".icon").addClass("iconClick");
        $(this).siblings().find(".icon").removeClass("iconClick");
        $("#editContainer").css("display", "flex").siblings().hide();
    });
    //编辑资料
    $("#editUpdate").click(function () {
        $(this).addClass("editCilck").siblings().removeClass("editCilck");
        $(".edit").show();
        $("#avatarForm").show();
        $(".mailContainer").hide();
        $(".reset").hide();
    });
    //更新头像
    $("#avatarForm").ajaxForm(function (result) {
        //上传失败
        if (result.status == 0) {
            alert("更新失败");
            return;
        }
        if (result.status == 1) {
            //将页面头部的头像更新过来
            $("#avatar").attr("src", result.file_path);
        }
    });
    //修改用户名
    $("#centerUsername").focusout(function () {
        var value = $(this).val();
        if (value == null || value == "") {
            $(".nameWarn").html("用户名不得为空");
            $("#editBtn").attr("disabled", "true");
            return;
        }
        $.ajax({
            type: "post",
            url: "/user/verify.action",
            data: {"val": value, "type": "username"},
            dataType: "json",
            success: function (data) {
                //用户名校验失败
                if (data.status == 0) {
                    $(".nameWarn").html("用户名已存在");
                    $("#editBtn").attr("disabled", "true");    //注册按钮置为无效
                    return;
                }
                //用户名校验成功
                $(".nameWarn").html("");
                $("#editBtn").removeAttr("disabled");
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
    });
    //提交修改
    $("#editBtn").click(function () {
        $.ajax({
            type: "post",
            url: "/user/updateUserInformation.action",
            data: {
                "username": $("#centerUsername").val(),
                "bio": $("#bio").val(),
                "gender": $("#gender").val(),
                "city": $("#city").val()
            },
            dataType: "json",
            success: function (data) {
                if (data.status == -2) {    //用户未登录
                    window.location.href = "index.jsp";
                    return;
                }
                if (data.status == 0) { //更新失败
                    alert(data.msg);
                    return;
                }
                if(data.status == 1){   //更新成功
                    alert(data.msg);
                }
                //history.go(0);
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
    });

    //更换邮箱
    $("#editEmail").click(function () {
        $(this).addClass("editCilck").siblings().removeClass("editCilck");
        $(".mailContainer").show().siblings().hide();
    });
    var newEmail;
    //校验邮箱格式
    $("#email").focusout(function () {
        newEmail = $(this).val().trim();
        if (!checkEmailFormat(newEmail)) {    //邮箱格式错误
            $(".emailWarn").html("邮箱错误");
            return;
        }
        $(".emailWarn").html("");
        if (oldEmail == newEmail) {
            $("#updateBtn").attr("disabled", "true");
            $("#editBtn").attr("disabled", "true");
        } else {
            $.ajax({
                type:"post",
                url:"/user/verify.action",
                data: {"val": newEmail, "type": "email"},
                dataType:"json",
                success:function (data) {
                    if (data.status == -3) {  //参数错误
                        alert(data.msg);
                        return;
                    }
                    //邮箱校验失败
                    if (data.status == 0) {
                        $(".emailWarn").html("邮箱已存在");
                        $("#updateBtn").attr("disabled", "true");
                        $("#editBtn").attr("disabled", "true");
                        return;
                    }
                    $("#updateBtn").removeAttr("disabled");
                    $("#editBtn").removeAttr("disabled");
                },
                error:function () {
                    window.location.href = "systemError.jsp";
                }
            });
        }
    });
    //更换邮箱获取验证码
    $("#updateBtn").click(function () {
        $(this).val("60后重新获取").attr("disabled", "true");
        var countDown = setInterval(function () {
            var val = $("#updateBtn").val();
            var num;
            if (val.length == 7) {
                num = val.substr(0, 2);
            } else if (val.length == 6) {
                num = val.substr(0, 1);
            }
            if (num > 0) {
                $("#updateBtn").val((--num) + "后重新获取");
            } else if (num == 0) {
                $("#updateBtn").val("获取验证码").removeAttr("disabled");
                clearInterval(countDown);
            }
        }, 1000);
        $.ajax({
            type: "post",
            url: "/mail/sendMail.action",
            data: {"recipient": newEmail},
            dataType: "json",
            success: function (data) {
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
        $(".codeContainer").show();
    });
    $("#code").keyup(function () {
        var codeVal = $(this).val().trim();
        if (codeVal == null || codeVal == "") {
            $("#emailBtn").attr("disabled", "true");
        }
        $("#emailBtn").removeAttr("disabled");
    });
    $("#emailBtn").click(function () {
        $.ajax({
            type: "post",
            url: "/mail/confirmCode.action",
            data: {"recipient": newEmail, "code": $("#code").val()},
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {   //验证码错误
                    $(".codeWarn").html(data.msg).show();
                    return;
                }
                $.ajax({
                    type: "post",
                    url: "/user/updateEmail.action",
                    data: {"email": newEmail},
                    dataType: "json",
                    success: function (data) {
                        //验证码正确更新邮箱
                        alert("更新成功");
                        history.go(0);
                    },
                    error: function () {
                        window.location.href = "systemError.jsp";
                    }
                });
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
    });

    //修改密码
    $("#editReset").click(function () {
        $(this).addClass("editCilck").siblings().removeClass("editCilck");
        $(".reset").show().siblings().hide();
    });
    //旧密码
    $(".oldPwd").focusout(function () {
        var value = $(this).val();
        if (value != null && value != "") {
            $(".newPwd").removeAttr("disabled");
        }
    });
    //新密码
    $(".newPwd").focusout(function () {
        var value = $(this).val();
        if (value != null && value != "") {
            $(".rePwd").removeAttr("disabled");
        }
    });
    //再次输入新密码
    $(".rePwd").focusout(function () {
        if ($(".newPwd").val().trim() != $(this).val().trim()) {
            $(".rePwdWarn").html("两次密码不一致");
            $("#resetBtn").attr("disabled", "true");
            return;
        }
        $(".rePwdWarn").html("");
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
                if (data.status == -2) {    //用户未登录
                    window.location.href = "index.jsp";
                    return;
                }
                if (data.status == 0) {
                    if (data.msg == "原始密码错误") {
                        $(".oldPwdWarn").html("原始密码错误");
                        return;
                    }
                }
                //重置密码成功
                alert("重置成功");
                $(".oldPwdWarn").html("");
                $(".oldPwd").val("");
                $(".newPwd").val("").attr("disabled", "true");
                $(".rePwd").val("").attr("disabled", "true");
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
    });

    /* 5.消息中心 */
    $("#navMsg").click(msgClick);

});

var status;

//点赞攻略
function likeClick() {
    var strategyId = $(this).parent().parent().parent().parent().attr("value");
    var value = $(this).next().html();
    if ($(this).attr("class").indexOf("navColor") != -1) {
        $(this).removeClass("navColor");
        $(this).next().html(--value).removeClass("navColor");
        status = 0;
    } else {
        $(this).addClass("navColor");
        $(this).next().html(++value).addClass("navColor");
        status = 1;
    }
    $.ajax({
        type: "post",
        url: "/strategy/updateForOrCollect.action",
        data: {"forNum": value, "id": strategyId, "status": status, "type": "for"},
        error: function () {
            window.location.href = "systemError.jsp";
        }
    });
}

//收藏攻略
function collectClick() {
    var strategyId = $(this).parent().parent().parent().parent().attr("value");
    var value = $(this).next().html();
    if ($(this).attr("class").indexOf("collectColor") != -1) {
        $(this).removeClass("collectColor");
        $(this).next().html(--value).removeClass("collectColor");
        status = 0;
    } else {
        $(this).addClass("collectColor");
        $(this).next().html(++value).addClass("collectColor");
        status = 1;
    }
    $.ajax({
        type: "post",
        url: "/strategy/updateForOrCollect.action",
        data: {"collectNum": value, "id": strategyId, "status": status, "type": "collect"},
        error: function () {
            window.location.href = "systemError.jsp";
        }
    });
}

//查看消息中心
function msgClick() {
    $("#navMsg").find(".icon").addClass("iconClick");
    $("#navMsg").siblings().find(".icon").removeClass("iconClick");
    $.ajax({
        type: "get",
        url: "/feedback/listByUser.action",
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "index.jsp";
                return;
            }
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $("#msgs").empty();
            if (data.status == -1) {  //没有匹配信息
                $(".msgNothing").html("暂无任何信息...").css("opacity", 1);
                $("#msgDot").css("display", "none");
                return;
            }
            $(".msgNothing").css("opacity", 0);
            var readClass;
            for (i = 0; i < data.data.list.length; i++) {
                readClass = data.data.list[i].status > 0 ? "" : "read";
                if (data.data.list[i].id != null) {
                    $("#msgs").append("<div class='msg " + readClass + "' level='" + data.data.list[i].level + "'>" +
                        "<span class='msgContent'><span class=send>admin</span>" +
                        "<span class='call'>回复你</span><span class='msgInfo'>" + data.data.list[i].content +
                        "</span></span><span class='msgDate'>" + data.data.list[i].createTime + "</span></div>");
                } else {
                    $("#msgs").append("<div class='dwr " + readClass + "' id='" + data.data.list[i].dwrId +
                        "' sequence='" + data.data.list[i].commentSequence + "'><span class='msgContent'>" +
                        "<span class='send'>" + data.data.list[i].responseName + "</span><span class='call'>@你" +
                        "</span><span class='msgInfo'>" + data.data.list[i].content + "</span></span>" +
                        "<span class='msgDate'>" + data.data.list[i].createTime + "</span></div>");
                }
            }
            $("#msgs .msg").off("click").on("click", detail);
            $("#msgs .dwr").off("click").on("click", function () {
                var that = $(this);
                if ($(this).attr("class").indexOf("read") == -1) {
                    $.ajax({
                        type: "get",
                        url: "/feedback/updateDwr.action",
                        data: {"dwrId": that.attr("id")},
                        dataType: "json",
                        success: function (data) {
                            if (data.status == -2) {  //用户未登录
                                window.location.href = "index.jsp";
                                return;
                            }
                            if (data.status == -3) {  //参数错误
                                alert(data.msg);
                                return;
                            }
                        },
                        error: function () {
                            window.location.href = "systemError.jsp";
                        }
                    });
                }
                window.location.href = "comment.jsp?level=" + that.attr("sequence");
            });
        },
        error: function () {
            window.location.href = "systemError.jsp";
        }
    });
    $("#msgContainer").show().siblings().hide();
}

//显示饼图
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

//显示地图
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
                    show: false
                }
            },
            itemStyle: {
                normal: {
                    borderColor: 'transparent'
                },
                emphasis: {
                    shadowBlur: 20,
                    shadowColor: 'rgba(0, 0, 0, 0.5)',
                    color: '#000211' //highlight color
                }
            }
        },
        series: [
            {
                type: 'scatter',
                coordinateSystem: 'geo',
                symbolSize: 0
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

//上传图片
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
    //将图片上传到服务器
    $("#usericon").attr("src", path);
    $("#navAvatar").attr("src", path);
    $("#avatar").attr("src", path);
    $("#avatarForm").attr("action", "/user/updateAvatar.action").submit();
}

//排序我的收藏
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
            if (data.status == -2) {    //用户未登录
                window.location.href = "index.jsp";
                return;
            }
            $(".collects").empty();
            if (data.status == -1) {  //没有匹配信息
                $(".collectNothing").html("暂无收藏，去逛逛吧...").css("opacity", 1);
                return;
            }
            $(".collectNothing").css("opacity", 0);
            for (i = 0; i < data.data.list.length; i++) {
                forClass = data.data.list[i].forStatus == 1 ? "forNum navColor" : "forNum";
                $(".collects").append("<div class='collect' value='" + data.data.list[i].id + "'>" +
                    "<img id='mainImg' src='" + data.data.list[i].mainImg + "'><div class='collectInfo'><span>" +
                    "<span class='title'>" + data.data.list[i].strategyName + "</span><span class='author'>" +
                    data.data.list[i].username + "</span><span class='city'>" + data.data.list[i].cityName + "</span></span>" +
                    "<span class='bot'><span class='date'>" + data.data.list[i].createTime + "</span><span>" +
                    "<span class='iconfont icon-zan1'></span><span class='" + forClass + "'>" + data.data.list[i].forNum +
                    "</span><span class='iconfont icon-collection-b collectColor'></span><span>" + data.data.list[i].collectNum +
                    "</span></span></span></div></div>");
            }
        },
        error: function () {
            window.location.href = "systemError.jsp";
        }
    });
}

var level;

//查看消息的详情
function detail() {
    var that = $(this);
    level = $(this).attr("level");
    //将feedback的status置为0
    if ($(this).attr("class").indexOf("read") == -1) {
        $.ajax({
            type: "get",
            url: "/feedback/updateFeedStatus.action",
            data: {"level": level},
            dataType: "json",
            success: function (data) {
                if (data.status == -2) {  //用户未登录
                    window.location.href = "index.jsp";
                    return;
                }
                if (data.status == -3) {  //参数错误
                    alert(data.msg);
                    return;
                }
                that.addClass("read");
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        });
    }
    $.ajax({
        type: "get",
        url: "/feedback/detail.action",
        data: {"level": level},
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $("#bg").slideDown();
            $(".feedbackDetail").empty().append("<div class='sender'>admin</div>" +
                "<div class='chats'></div><textarea class='response'></textarea><input type='button' class='feedBtn' value='发送'>");
            $(".feedBtn").off("click").on("click", sendFeedback);
            var feedClass;
            for (i = 0; i < data.data.length; i++) {
                feedClass = data.data[i].username == "admin" ? "" : "class='rightSide'";
                $(".chats").append("<div parent='" + data.data[i].parent + "' sequence='" + data.data[i].sequence + "' "
                    + feedClass + "><div class='date'>" + data.data[i].createTime + "</div><img src='" +
                    data.data[i].avatar + "'><span class='feedContent'>" + data.data[i].content + "</span></div>");
            }
            $(".feedbackDetail").slideDown();
        },
        error: function () {
            window.location.href = "systemError.jsp";
        }
    });
}

//发送信息
function sendFeedback() {
    var response = $(".response").val().trim();
    if (response == null || response == "") {
        return;
    }
    var parent = $(".chats>div:last").attr("parent");
    var sequence = $(".chats>div:last").attr("sequence");
    $.ajax({
        type: "post",
        url: "/feedback/reply.action",
        data: {"content": response, "parent": parent, "sequence": sequence},
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $(".response").val("");
            $(".chats").append("<div parent='" + parent + "' sequence='" + sequence + "' class='rightSide'>" +
                "<div class='date'>" + getformatDate() + "</div><img src='" + userAvatar + "'>" +
                "<span class='feedContent'>" + response + "</span></div>");
            dwrMessage.publishFeed(1);
        },
        error: function () {
            window.location.href = "systemError.jsp";
        }
    });
}