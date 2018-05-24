var i;
$(function () {
    $.ajax({
        type: "get",
        url: "/strategy/indexInfo.action",
        dataType: "json",
        success: function (data) {
            //推荐
            if (data.data.recommend == undefined) {
                $(".recommendContainer").hide();
            } else {
                $(".recommendContainer").show();
                $(".recommends").empty();
                for (i = 0; i < data.data.recommend.length; i++) {
                    $(".recommends").append("<div class='strategy' value='" + data.data.recommend[i].strategyId + "'>" +
                        "<img class='mainImg' src='" + data.data.recommend[i].mainImg + "'><span class='info'>" +
                        "<span class=strategyTitle'>" + data.data.recommend[i].strategyName + "</span>" +
                        "<span class='strategyInfo'><span>" + data.data.recommend[i].username + "</span><span>"
                        + data.data.recommend[i].cityName + "</span><span>" + data.data.recommend[i].duration + "天</span></span>" +
                        "<span class='strategyDate'>" + data.data.recommend[i].createTime + "</span></span></div>");
                }

            }
            //最新
            if (data.data.latest == undefined) {
                $(".newestContainer").hide();
            } else {
                $(".newestContainer").show();
                $(".news").empty();
                for (i = 0; i < data.data.latest.length; i++) {
                    $(".news").append("<div class='strategy' value='" + data.data.latest[i].strategyId + "'>" +
                        "<img class='mainImg' src='" + data.data.latest[i].mainImg + "'><span class='info'>" +
                        "<span class=strategyTitle'>" + data.data.latest[i].strategyName + "</span>" +
                        "<span class='strategyInfo'><span>" + data.data.latest[i].username + "</span><span>"
                        + data.data.latest[i].cityName + "</span><span>" + data.data.latest[i].duration + "天</span></span>" +
                        "<span class='strategyDate'>" + data.data.latest[i].createTime + "</span></span></div>");
                }

            }
            //最热
            if (data.data.hottest == undefined) {
                $(".hottestContainer").hide();
            } else {
                $(".hottestContainer").show();
                $(".hots").empty();
                for (i = 0; i < data.data.hottest.length; i++) {
                    $(".hots").append("<div class='strategy' value='" + data.data.hottest[i].strategyId + "'>" +
                        "<img class='mainImg' src='" + data.data.hottest[i].mainImg + "'><span class='info'>" +
                        "<span class=strategyTitle'>" + data.data.hottest[i].strategyName + "</span>" +
                        "<span class='strategyInfo'><span>" + data.data.hottest[i].username + "</span><span>"
                        + data.data.hottest[i].cityName + "</span><span>" + data.data.hottest[i].duration + "天</span></span>" +
                        "<span class='strategyDate'>" + data.data.hottest[i].createTime + "</span></span></div>");
                }

            }
            $(".strategy").off("click").on("click", function () {
                if (navUserId == undefined) {
                    $("#signinContainer").slideDown();  //显示登录框
                    $("#bg").slideDown();   //显示背景
                    return;
                }
                window.location.href = "strategyItem.jsp?strategyId=" + $(this).attr("value");
            })
        },
        error: function () {
            window.location.href = "systemError.jsp";
        }
    });
});