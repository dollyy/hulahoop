var level;   //level of each comment;
var userId, userAvatar, username;
var commentPadding;   //padding-left of each comment
$(function () {

    CurrentUser();
    //list all the comments --start
    $.ajax({
        type: "get",
        url: "/comment/list.action",
        data: {"level": 5},
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                return;
            }
            $("#comments").append("<div class='comment'></div>");
            for (var j = 0; j < data.data[0].length; j++) {
                level = data.data[0][j].level;
                //calculate the padding-left
                commentPadding = (level.indexOf(".") != -1) ? 40 * (level.split(".").length - 1) : 0;
                //add each div
                $(".comment").append("<div class='each' id='" + data.data[0][j].id + "' level='" + level +
                    "' strategyId='" + data.data[0][j].strategyId + "'><div><div class='user' style='padding-left:" +
                    " " + commentPadding + "px'><img src='" + data.data[0][j].avatar + "'>" +
                    "<span class='responseUid' uid='" + data.data[0][j].responseId + "'>" + data.data[0][j].responseName + "" +
                    "</span></div><div class='content'><div><span class='response'>" +
                    (data.data[0][j].requestId == 0 ? "" : "@" + data.data[0][j].requestName) + "</span>" +
                    "<span>" + data.data[0][j].content + "</span></div><div class='operation'><span class='commentTime'>"
                    + data.data[0][j].createTime + "</span><i class='iconfont icon-zan1'></i><span class='for'>"
                    + data.data[0][j].forNum + "</span><i class='iconfont icon-zan11'></i><span class='against'>"
                    + data.data[0][j].againstNum + "</span><span class='addComment'>回复</span></div></div></div>" +
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
        },
        error: function () {
            //alert("error");
        }
    });
    //list all the comments --end

});

function CurrentUser() {
    $.ajax({
        type: "get",
        url: "/user/queryUserInformation.action",
        dataType: "json",
        success: function (data) {
            username = data.data.username;
            userId = data.data.id;
            userAvatar = data.data.avatar;
        },
        error: function () {
            console.log("info error");
        }
    });
}

//show addComment div --start
function showAddComment() {
    $(this).parent().parent().parent().next().show();
}

//show addComment div --end

//hide addComment div --start
function hideAddComment() {
    $(this).parent().parent().hide();
    //clean the value of comment textarea
    $(this).parent().prev().find(".currentComment").val("");
}

//hide addComment div --end

//add comment --start
function addComment() {
    var comment = $(this).parent().prev().find(".currentComment").val();
    if (comment == null || comment == "") {
        return;
    }
    var that = $(this);
    var strategyId = $(this).parent().parent().parent().attr("strategyId");
    var level = $(this).parent().parent().parent().attr("level");
    var requestName = $(this).parent().parent().parent().find(".responseUid").html();
    $.ajax({
        type: "post",
        url: "/comment/reply.action",
        data: {"strategyId": strategyId, "content": comment, "parent": level},
        dataType: "json",
        success: function (data) {
            console.log(JSON.stringify(data));
            if (data.status == 0) {
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
        },
        error: function () {
            //alert("error");
        }
    });
}

//add comment --end

//calculate current time --start
function currentTime() {
    var time = new Date();
    var month = formatNum(time.getMonth() + 1);
    var day = formatNum(time.getDate());
    var hour = formatNum(time.getHours());
    var min = formatNum(time.getMinutes());
    var second = formatNum(time.getSeconds());
    return time.getFullYear() + "-" + month + "-" + day + " " + hour + ":" + min + ":" + second;
}

//calculate current time --end

//format number --start
function formatNum(num) {
    if (1 == num.toString().split("").length) {
        return "0" + num;
    }
    return num;
}

//format number --end

//operate for or against --start
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
        success: function () {
        },
        error: function () {
            console.log("number error");
        }
    });
}

//operate for or against --end