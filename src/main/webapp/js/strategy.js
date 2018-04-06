var i;
$(function () {
    //select2
    $(document).ready(function () {
        $('.js-example-basic-single').select2();
    });

    $.ajax({
        type: "get",
        url: "/strategy/list.action",
        dataType: "json",
        success: function (data) {
            if (data.data.cities == undefined || data.data.strategies == undefined || data.data.durations == undefined) {
                $(".content").hide();
                $("#pageNum").hide();
                $(".nothing").html("暂无相关信息...").show();
                return;
            }
            packageData(data.data);
        },
        error: function () {
            console.log("query resource error");
        }
    });

    //导航的滚动事件
    $(window).scroll(function () {
        var top = $(document).scrollTop();
        if (top >= 55) {
            $(".catalog").css("top", "0");
        } else {
            $(".catalog").css("top", "55px");
        }
    });

    //province click event
    $(".province").change(updateStrategies);

    //duration click event
    $(".duration").change(updateStrategies);

});

function packageData(data) {
    $(".content").show();
    $("#pageNum").show();
    $(".nothing").html("").hide();
    //1.province
    if (data.cities.length > 0) {
        for (i = 0; i < data.cities.length; i++) {
            $(".province").append("<option value='" + data.cities[i].id + "'>" + data.cities[i].name + "</option>");
        }
    }
    //2.duration
    if (data.durations.length > 0) {
        for (i = 0; i < data.durations.length; i++) {
            $(".duration").append("<option>" + data.durations[i] + "</option>")
        }
    }
    //3.strategies
    $(".content").empty();
    if (data.strategies.list.length == 0) {
        $("#pageNum").hide();
        return;
    }
    for (i = 0; i < data.strategies.list.length; i++) {
        $(".content").append("<div value='" + data.strategies.list[i].strategyId + "' class='strategy'>" +
        "<img id='strategyBg' src='" + data.strategies.list[i].mainImg + "'>" +
        "<img id='avatar' src='" + data.strategies.list[i].avatar + "'><div class='s_msg'>" +
        "<span class='title'>标题: " + data.strategies.list[i].strategyName + "</span><span class='user'>作者 : " +
        data.strategies.list[i].username + "</span><span class='tags'>标签 : " +
        data.strategies.list[i].cityName + " " + data.strategies.list[i].duration + "</span></div></div>");
    }
    //strategies click
    $(".content .strategy").off("click").on("click", function () {
        var that = $(this);
        //获取当前用户的信息
        $.ajax({
            type: "get",
            url: "/user/queryUserInformation.action",
            dataType: "json",
            success: function (data) {
                if (data.status == -2) {    //用户未登录
                    $("#signinContainer").slideDown();  //显示登录框
                    $("#bg").slideDown();   //显示背景
                    return;
                }
                window.location.href = "strategyItem.jsp?strategyId=" + that.attr("value");
            },
            error: function () {
                console.log("info error");
            }
        });
    });
    $("#pageNum").show();
    //4.pages
    $("#page").paging({
        totalPage: data.strategies.pages,
        callback: function (num) {
            $.ajax({
                type: "get",
                url: "/strategy/updateList.action",
                data: {"pageNum": num},
                dataType: "json",
                success: function (data) {
                    packUpdateList(data.data);
                },
                error: function () {
                    console.log("page error");
                }
            });
        }
    });
}

function packUpdateList(data) {
    $(".content").show();
    $("#pageNum").show();
    $(".nothing").html("").hide();
    $(".content").empty();
    if (data.list.length == 0) {
        $("#pageNum").hide();
        return;
    }
    $("#pageNum").show();
    for (i = 0; i < data.list.length; i++) {
        $(".content").append("<div value='" + data.list[i].strategyId + "' class='strategy'>" +
        "<img id='strategyBg' src='" + data.list[i].mainImg + "'>" +
        "<img id='avatar' src='" + data.list[i].avatar + "'><div class='s_msg'>" +
        "<span class='title'>标题: " + data.list[i].strategyName + "</span><span class='user'>作者 : " +
        data.list[i].username + "</span><span class='tags'>标签 : " +
        data.list[i].cityName + " " + data.list[i].duration + "</span></div></div>");
    }
    //strategies click
    $(".content .strategy").off("click").on("click", function () {
        window.location.href = "strategyItem.jsp?strategyId=" + $(this).attr("value");
    });
}

function updateStrategies() {
    var sendData = {};
    var city = $(".province").val();
    var duration = $(".duration").find(":selected").val();
    if (city == 0) {
        if (duration != "全部") {
            sendData = {"duration": duration};
        }
    } else {
        sendData = {"cityId": city};
        if (duration != "全部") {
            sendData = {"cityId": city, "duration": duration};
        }
    }
    $.ajax({
        type: "get",
        url: "/strategy/updateList.action",
        data: sendData,
        dataType: "json",
        success: function (data) {
            $(".content").empty();
            if (data.status == -1) {
                $(".content").hide();
                $("#pageNum").hide();
                $(".nothing").html("暂无相关信息...").show();
                return;
            }
            packUpdateList(data.data);
        },
        error: function () {
            console.log("updateStrategies error");
        }
    });
}