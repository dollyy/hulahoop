//todo 收藏点赞不要重复
var strategyId, i;
$(function () {
    var parameter = getQueryStringArgs();
    $.ajax({
        type: "get",
        url: "/strategy/detail.action",
        data: {"strategyId": parameter},
        dataType: "json",
        success: function (data) {
            //用户未登录
            if (data.status == -2) {
                window.location.href="index.jsp";
                return;
            }
            //攻略不存在
            if (data.status == -1) {
                window.location.href="notFound.jsp";
                return;
            }
            //1.province + duration
            $(".ItemNav .tags").html(data.data.strategy.cityName + " > " + data.data.strategy.duration);
            $(".ItemNav .navFor").html(data.data.strategy.forNum);
            $(".ItemNav .navCollect").html(data.data.strategy.collectNum);

            //2.catalog
            for (i = 0; i < data.data.catalog.length; i++) {
                $(".catalog").append("<li class='mtb10'><a class='cata" + (i + 1) + "' href='#cata" + (i + 1) + "'>" +
                    data.data.catalog[i] + "</a></li>");
            }
            $(".catalog li a").eq(0).addClass("current");
            $(".commentTitle").attr("id", "cata" + i);

            $(".title").html(data.data.strategy.strategyName);
            $(".author").html(data.data.strategy.username);

            //3.content
            strategyId = data.data.strategy.strategyId;
            $(".content").attr("value", strategyId);
            for (i = 0; i < data.data.content.length; i++) {
                $(".contentContainer").append("<div id='cata" + (i + 1) + "' class='cata'><div class='dayTitle'>" +
                    data.data.catalog[i] + "</div><div class='dayContent'>" + data.data.content[i] + "</div></div>");
            }

            //4.comment
            for (i = 0; i < data.data.comments.length; i++) {
                $(".comments").prepend("<div class='comment' value='" + data.data.comments[i].id + "' level='" +
                    data.data.comments[i].level + "'><img src='" + data.data.comments[i].avatar + "'><div class='response'>" +
                    "<div class='commInfo'><span class='mr10'>" + data.data.comments[i].responseName + "</span><span class='mr10'>"
                    + data.data.comments[i].createTime + "</span></div><div class='commContent'>" + data.data.comments[i].content +
                    "</div><div class='commOperation'><span class='iconfont icon-zan11'></span><span class='mr10'>" +
                    data.data.comments[i].againstNum + "</span><span class='iconfont icon-zan1'></span><span class='mr10'>" +
                    data.data.comments[i].forNum + "</span><span>回复 </span><span>" + data.data.comments[i].commentCount +
                    "</span></div></div></div>");
            }
            //against click
            $(".commOperation .icon-zan1").unbind("click").bind("click", calNumber);
            //for click
            $(".commOperation .icon-zan11").unbind("click").bind("click", calNumber);
        },
        error: function () {
            alert("strategyItem error");
        }
    });

    $("#addCommentBtn").click(function () {
        var comment = $("#addComment").val().trim();
        if (comment == "" || comment == null) {
            return;
        }
        $.ajax({
            type: "post",
            url: "/comment/add.action",
            data: {"strategyId": $(".content").attr("value"), "content": comment},
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    alert(data.msg);
                    return;
                }
                history.go(0);
            },
            error: function () {
                console.log("add comment error");
            }
        });
    });

    //点赞攻略
    $(".ItemNav .icon-zan1").click(function () {
        var value = $(this).next().html();
        if ($(this).attr("class").indexOf("navColor") != -1) {
            $(this).removeClass("navColor");
            $(this).next().html(--value).removeClass("navColor");
        } else {
            $(this).addClass("navColor");
            $(this).next().html(++value).addClass("navColor");
        }
        $.ajax({
            type: "post",
            url: "/strategy/updateForOrCollect.action",
            data: {"forNum": value, "id": strategyId},
            success: function () {

            },
            error: function () {

            }
        });
    });
    //收藏攻略
    $(".ItemNav .icon-collection-b").click(function () {
        var value = $(this).next().html();
        if ($(this).attr("class").indexOf("collectColor") != -1) {
            $(this).removeClass("collectColor");
            $(this).next().html(--value).removeClass("collectColor");
        } else {
            $(this).addClass("collectColor");
            $(this).next().html(++value).addClass("collectColor");
        }
        $.ajax({
            type: "post",
            url: "/strategy/updateForOrCollect.action",
            data: {"collectNum": value, "id": strategyId},
            success: function () {

            },
            error: function () {

            }
        });
    });

    //catalog event
    $(window).scroll(function () {
        var days = $(".content").find(".cata");
        var catalog = $(".catalog");
        var top = $(document).scrollTop();
        var currentId = "";
        days.each(function () {
            var that = $(this);
            if (top > that.offset().top - 300) {
                currentId = "." + that.attr("id");
            } else {
                return false;
            }
        });
        var currentLink = catalog.find(".current");
        if (currentId && currentLink.attr("href") != currentId) {
            currentLink.removeClass("current");
            catalog.find(currentId).addClass("current");
        }
    });
});


function calNumber() {
    var that = $(this);
    if (!operateNumber(that)) {
        return;
    }
    var numberType = that.attr("class").indexOf("icon-zan11") != -1 ? "against" : "for";
    var number = that.next().html();
    var commentId = that.parent().parent().parent().attr("value");
    console.log(numberType, number, commentId);
    $.ajax({
        type: "post",
        url: "/comment/forOrAgainstComment.action",
        data: {"id": commentId, "type": numberType, "number": number},
        success: function () {
        },
        error: function () {
            console.log("number error");
        }
    });
}