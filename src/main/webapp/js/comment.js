var level;   //level of each comment;
var userId, userAvatar, username;
var commentPadding;   //padding-left of each comment
$(function () {
    var parameter = getQueryStringArgs();

    //获取当前用户的信息
    $.ajax({
        type: "get",
        url: "/user/isLogin.action",
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "index.jsp";
                return;
            }
            if (data.status == 0 || data.status == -3) {
                alert(data.msg);
                return;
            }
            username = data.data.username;
            userId = data.data.id;
            userAvatar = data.data.avatar;
        },
        error: function () {
            console.log("info error");
        }
    });

    //list all the comments --start
    $.ajax({
        type: "get",
        url: "/comment/list.action",
        data: {"level": parameter},
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "index.jsp";
                return;
            }
            if (data.status == -1) {    //没有匹配信息
                window.location.href = "notFound.jsp";
                return;
            }
            if (data.status == 0 || data.status == -3) {
                alert(data.msg);
                return;
            }
            $(".strategyTitle").html(data.data[0][0].strategyName).attr("href", "strategyItem.jsp?strategyId=" + data.data[0][0].strategyId);
            $("#comments").append("<div class='comment'></div>");
            for (var i = 0; i < data.data.length; i++) {
                for (var j = 0; j < data.data[i].length; j++) {
                    level = data.data[i][j].level;
                    //calculate the padding-left
                    commentPadding = (level.indexOf(".") != -1) ? 40 * (level.split(".").length - 1) : 0;
                    //add each div
                    $(".comment").append("<div class='each' id='" + data.data[i][j].id + "' level='" + level +
                        "' strategyId='" + data.data[i][j].strategyId + "'><div><div class='user' style='padding-left:" +
                        " " + commentPadding + "px'><img src='" + data.data[i][j].avatar + "'>" +
                        "<span class='responseUid' uid='" + data.data[i][j].responseId + "'>" + data.data[i][j].responseName + "" +
                        "</span></div><div class='content'><div><span class='response'>" +
                        (data.data[i][j].requestId == 0 ? "" : "@" + data.data[i][j].requestName) + "</span>" +
                        "<span>" + data.data[i][j].content + "</span></div><div class='operation'><span class='commentTime'>"
                        + data.data[i][j].createTime + "</span><i class='iconfont icon-zan1'></i><span class='for'>"
                        + data.data[i][j].forNum + "</span><i class='iconfont icon-zan11'></i><span class='against'>"
                        + data.data[i][j].againstNum + "</span><span class='addComment'>回复</span></div></div></div>" +
                        "<div class='commentContent' style='padding-left: " + commentPadding + "px'><div>" +
                        "<span class='currentUser'>" + username + "</span><textarea class='currentComment'></textarea></div>" +
                        "<div class='btns'><input type='button' class='btnAdd' value='add'>" +
                        "<input type='button' class='btnCancle' value='cancle'></div></div></div>");

                    //click 回复 to show the addComment div
                    $(".addComment").unbind("click").bind("click", showAddComment);

                    //click cancle to hide the addComment div
                    $(".btnCancle").unbind("click").bind("click", hideAddComment);

                    //click add to add comment
                    $(".btnAdd").unbind("click").bind("click", addComment);

                    //click against, add against number
                    $(".icon-zan1").unbind("click").bind("click", calNumber);

                    //click for, add for number
                    $(".icon-zan11").unbind("click").bind("click", calNumber);
                }
            }
        },
        error: function () {
            //alert("error");
        }
    });

});

//显示回复框
function showAddComment() {
    $(this).parent().parent().parent().next().show();
}

//隐藏回复框
function hideAddComment() {
    $(this).parent().parent().hide();
    //清空文本内容
    $(this).parent().prev().find(".currentComment").val("");
}

//回复
function addComment() {
    var comment = $(this).parent().prev().find(".currentComment").val();
    if (comment == null || comment == "") {
        return;
    }
    var that = $(this);
    var parent = $(this).parent().parent().parent();
    var strategyId = parent.attr("strategyId");
    var level = parent.attr("level");
    var requestName = parent.find(".responseUid").html();
    $.ajax({
        type: "post",
        url: "/comment/reply.action",
        data: {"strategyId": strategyId, "content": comment, "parent": level},
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "index.jsp";
                return;
            }
            if (data.status == -1) {    //没有匹配信息
                window.location.href = "notFound.jsp";
                return;
            }
            if (data.status == 0 || data.status == -3) {
                alert(data.msg);
                return;
            }
            commentPadding = parseInt(that.parent().parent().css("padding-left")) + 40;  //calculate the padding-left
            //add comment on the web
            that.parent().parent().parent().after("<div class='each' id='" + data.data.id + "' level='" + data.data.level +
                "' strategyId='" + strategyId + "'><div><div class='user' style='padding-left:" + commentPadding + "px'>" +
                "<img src='" + userAvatar + "'><span class='responseUid' uid='" + userId + "'>" + username + "</span></div>" +
                "<div class='content'><div><span class='response'>@" + requestName + "</span><span>" + comment + "</span>" +
                "</div><div class='operation'><span class='commentTime'>" + currentTime() + "</span><i class='iconfont icon-zan11'>" +
                "</i><span class='for'>0</span><i class='iconfont icon-zan1'></i><span class='against'>0</span>" +
                "<span class='addComment'>回复</span></div></div></div>" +
                "<div class='commentContent' style='padding-left: " + commentPadding + "px'><div>" +
                "<span class='currentUser'>" + username + "</span><textarea class='currentComment'></textarea></div>" +
                "<div class='btns'><input type='button' class='btnAdd' value='add'>" +
                "<input type='button' class='btnCancle' value='cancle'></div></div></div>");

            //clean the comment textarea
            that.parent().parent().hide();
            that.parent().prev().find(".currentComment").val("");

            //click 回复 to show the addComment div
            $(".addComment").unbind("click").bind("click", showAddComment);

            //click cancle to hide the addComment div
            $(".btnCancle").unbind("click").bind("click", hideAddComment);

            //click add to add comment
            $(".btnAdd").unbind("click").bind("click", addComment);

            //click against, add against number
            $(".icon-zan1").unbind("click").bind("click", calNumber);

            //click for, add for number
            $(".icon-zan11").unbind("click").bind("click", calNumber);

            //dwr
            var requestId = that.parent().parent().prev().find(".responseUid").attr("uid");
            var commentLevel = parent.attr("level");
            dwrMessage.publishComment(userId + "@=" + comment + "@=" + commentLevel, requestId);
        },
        error: function () {
            console.log("reply error");
        }
    });
}

//calculate current time
function currentTime() {
    var time = new Date();
    var month = formatNum(time.getMonth() + 1);
    var day = formatNum(time.getDate());
    var hour = formatNum(time.getHours());
    var min = formatNum(time.getMinutes());
    var second = formatNum(time.getSeconds());
    return time.getFullYear() + "-" + month + "-" + day + " " + hour + ":" + min + ":" + second;
}

//format number
function formatNum(num) {
    if (1 == num.toString().split("").length) {
        return "0" + num;
    }
    return num;
}

//操作点赞/反对
function calNumber() {
    var that = $(this);
    if (!operateNumber(that)) {
        return;
    }
    var numberType = that.attr("class").indexOf("icon-zan11") != -1 ? "against" : "for";
    var number = that.next().html();
    var id = that.parent().parent().parent().parent().attr("id");
    console.log(numberType, number, id);
    $.ajax({
        type: "post",
        url: "/comment/forOrAgainstComment.action",
        data: {"id": id, "type": numberType, "number": number},
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "index.jsp";
                return;
            }
            if (data.status == 0 || data.status == -3) {
                alert(data.msg);
                return;
            }
        },
        error: function () {
            console.log("number error");
        }
    });
}