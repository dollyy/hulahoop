var adminAvatar, i, editor;
$(function () {

    //select2
    $(document).ready(function () {
        $('.js-example-basic-single').select2();
    });

    $.ajax({
        type: "get",
        url: "/user/queryUserInformation.action",
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            adminAvatar = data.data.avatar;
        }
    })

    //退出登录
    $(".icon-icon").click(function () {
        $.ajax({
            type: "get",
            url: "/manage/user/logout.action",
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    alert(data.msg);
                    return;
                }
                window.location.href = "adminSignin.html";
            },
            error: function () {
                console.log("logout error");
            }
        });
    });

    //首页禁止搜索
    $("#searchInp").attr("disabled", true);

    //click bg
    $("#bg").height($(document).height()).width($(document).width());
    $("#bg").click(function () {
        $(this).slideUp();
        $(".opeContainer").slideUp();
        $(".feedbackDetail").slideUp();
        $(".strategyDetail").slideUp();
        $("#subTitle").val("");
        $(".opeContainer .w-e-text").html("");
    });

    //搜索
    $(".icon-fangdajing").click(function () {
        var searchIndex;
        $('.catalog li').each(function (index) {
            if ($(this).hasClass("clicked")) {
                searchIndex = index;
            }
        });
        var searchInp = search();
        switch (searchIndex) {
            case 0:
                break;
            case 1:
                helpSearch(searchInp);
                break;
            case 2:
                break;
            case 3:
                feedSearch(searchInp);
                break;
            case 4:
                strategySearch(searchInp);
                break;
        }
    });

    /* 1.index */
    $(".index").click(function () {
        $(".indexContainer").show().siblings().hide();
        $(this).addClass("clicked").siblings().removeClass("clicked");
        $(".right .location span").html($(this).children().last().html());
        //禁止搜索
        $("#searchInp").attr("disabled", true);
    });

    /* 2.help */
    $(".help").click(function () {
        $("#searchInp").removeAttr("disabled");
        $.ajax({
            type: "get",
            url: "/manage/helpInfo/list.action",
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    alert(data.msg);
                    return;
                }
                $(".helpTable tbody").empty();
                if (data.data == undefined) {
                    $(".helpTable").hide();
                    $(".nothing").show();
                    return;
                }
                packHelp(data.data);
            },
            error: function () {
                console.log("help error");
            }
        });
        $(".helpContainer").show().siblings().hide();
        $(this).addClass("clicked").siblings().removeClass("clicked");
        $(".right .location span").html($(this).children().last().html());
    });
    //add icon-xinzeng
    $(".icon-xinzeng").click(function () {
        $("#bg").slideDown();
        $(".opeContainer").empty().append("<input type='text' id='subTitle' placeholder='Help Title. No more than 20 words'><div id='editor'></div><input type='button' value='添 加'  id='addBtn'>");
        $("#addBtn").off("click").on("click", addHelp);
        $("#subTitle").off("click").on("change", titleChange);
        showEditor();
        $(".opeContainer").slideDown();
    });

    /* 3.res */
    $(".res").click(function () {
        $(".resContainer").show().siblings().hide();
        $(this).addClass("clicked").siblings().removeClass("clicked");
        $(".right .location span").html($(this).children().last().html());
        //禁止搜索
        $("#searchInp").attr("disabled", true);
    });

    /* 4.feeback */
    $(".feedback").click(function () {
        $("#searchInp").removeAttr("disabled");
        $.ajax({
            type: "get",
            url: "/manage/feedback/list.action",
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    alert(data.msg);
                    return;
                }
                $("#feedbackTable tbody").empty();
                if (data.data == undefined) {
                    $("#feedbackTable").hide();
                    $(".nothing").show();
                    return;
                }
                packFeedback(data.data);
            },
            error: function () {
                console.log("feedback error");
            }
        });
        $(".feedbackContainer").show().siblings().hide();
        $(this).addClass("clicked").siblings().removeClass("clicked");
        $(".right .location span").html($(this).children().last().html());
    });

    /* 5.strategy*/
    $(".strategy").click(function () {
        $("#searchInp").removeAttr("disabled");
        $.ajax({
            type: "get",
            url: "/manage/strategy/list.action",
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    alert(data.msg);
                    return;
                }
                packStrategy(data.data);
            },
            error: function () {
                console.log("admin strategy error");
            }
        });
        $(".strategyContainer").show().siblings().hide();
        $(this).addClass("clicked").siblings().removeClass("clicked");
        $(".right .location span").html($(this).children().last().html());
    });
    //操作
    $(".strategyOpe").click(function () {
        var val = $(this).html();
        if (val == "操作") {
            $(".deleteAdminStrategy").css("display", "inline");
            $(".strategies").find("input[type='checkbox']").css("opacity", 1);
            $(".strategyOpe").html("取消");
        } else {
            $(".deleteAdminStrategy").css("display", "none");
            $(".strategies").find("input[type='checkbox']").css("opacity", 0);
            $(".strategyOpe").html("操作");
        }
    });
    //删除攻略
    $(".deleteAdminStrategy").click(function () {
        var select = $(".strategies").find("input[type='checkbox']");
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
            url: "/manage/strategy/delete.action",
            data: {"strategyId": selectId},
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    alert(data.msg);
                    return;
                }
                var deleteId = selectId.split(",");
                for (i = 0; i < deleteId.length; i++) {
                    $(".strategies").find("[value='" + deleteId[i] + "']").remove();
                }
            }
        });
    });
});

//搜索帮助信息
function helpSearch(searchInp) {

}

//搜索反馈信息
function feedSearch(searchInp) {

}

//搜索攻略信息
function strategySearch(searchInp) {

}

/*2*/

//create editor
function showEditor() {
    //wangEidtor
    var E = window.wangEditor;
    editor = new E('#editor');
    //use base64 to save img
    editor.customConfig.uploadImgShowBase64 = true;
    editor.create();
}

//package data for "help page"
function packHelp(data) {
    $(".nothing").hide();
    for (i = 0; i < data.list.length; i++) {
        $(".helpTable tbody").append("<tr value='" + data.list[i].id + "'><td>" + (i + 1) + "</td><td class='trTitle'>" +
            data.list[i].title + "</td><td>" + formatDate(data.list[i].createTime) + "</td><td class='updateTime'>" +
            formatDate(data.list[i].updateTime) + "</td><td><span class='iconfont icon-close-eye' title='隐藏'></span>" +
            "<span class='iconfont icon-chakan' title='查看'></span><span class='iconfont icon-bianji' title='编辑'>" +
            "</span><span class='iconfont icon-delete' title='删除'></span></td></tr><tr style='border:none'>" +
            "<td colspan='5' style='padding: 0'><div class='inner'>" + data.list[i].content + "</div></td></tr>");
    }
    //4.pages
    $(".page").paging({
        totalPage: data.pages,
        callback: function (num) {
            $.ajax({
                type: "post",
                url: "/manage/helpInfo/list.action",
                data: {"pageNum": num},
                dataType: "json",
                success: function (data) {
                    if (data.status == 0) {
                        alert(data.msg);
                        return;
                    }
                    $(".helpTable tbody").empty();
                    if (data.data == undefined) {
                        $(".helpTable").hide();
                        $(".nothing").show();
                        return;
                    }
                    packHelp(data.data);
                },
                error: function () {
                    console.log("help error");
                }
            });
        }
    });
    $(".icon-close-eye").off("click").on("click", helpHide);    //隐藏
    $(".icon-chakan").off("click").on("click", helpView);    //查看
    $(".icon-bianji").off("click").on("click", helpEdit);    //编辑
    $(".icon-delete").off("click").on("click", helpDelete);  //删除
}

//add new help
function addHelp() {
    var title = $("#subTitle").val();
    var text = editor.txt.text();
    var content = editor.txt.html();
    //输入框中内容不为空
    if (text != "" && text != null) {
        $.ajax({
            type: "post",
            url: "/manage/helpInfo/add.action",
            data: {"title": title, "content": content},
            dataType: "json",
            success: function (data) {
                console.log(JSON.stringify(data));
                if (data.status == 0) {
                    alert(data.msg);
                    return;
                }
                var addDate = getformatDate();
                var num = $(".helpTable").children().length + 1;
                $(".helpTable tbody").append("<tr value='" + data.data + "'><td>" + num + "</td><td class='trTitle'>" +
                    title + "</td><td>" + addDate + "</td><td class='updateTime'>" +
                    addDate + "</td><td><span class='iconfont icon-close-eye' title='隐藏'></span>" +
                    "<span class='iconfont icon-chakan' title='查看'></span><span class='iconfont icon-bianji' title='编辑'>" +
                    "</span><span class='iconfont icon-delete' title='删除'></span></td></tr><tr style='border:none'>" +
                    "<td colspan='5' style='padding: 0'><div class='inner'>" + content + "</div></td></tr>");
                $(".icon-close-eye").off("click").on("click", helpHide);    //隐藏
                $(".icon-chakan").off("click").on("click", helpView);    //查看
                $(".icon-bianji").off("click").on("click", helpEdit);    //编辑
                $(".icon-delete").off("click").on("click", helpDelete);  //删除
                $(".helpTable").show();
                $("#bg").slideUp();
                $(".opeContainer").slideUp();
                $("#subTitle").val("");
                $(".opeContainer .w-e-text").html("");
            },
            error: function () {
                console.log("add title error");
            }
        });
    }
}

//hide help's content
function helpHide() {
    $(this).parent().parent().next().find(".inner").css({"height": "0", "min-height": "0"});
    $(this).css({"font-size": "0", "margin": "0"});
    $(this).next().show();
}

//view help's content
function helpView() {
    //collapse other helpView
    $(".inner").css({"height": "0", "min-height": "0"});
    $(".icon-close-eye").css({"font-size": "0", "margin": "0"});
    $(".icon-chakan").show();
    //show current helpView
    $(this).parent().parent().next().find(".inner").css({"height": "auto", "min-height": "50px"});
    $(this).prev().css({"font-size": "16px", "margin-right": "10px"});
    $(this).hide();
}

var that;

//edit help
function helpEdit() {
    var helpId = $(this).parent().parent().attr("value");
    $("#bg").slideDown();
    $(".opeContainer").empty().append("<span value='" + helpId + "' class='iconfont icon-baocun'></span><input type='text' id='subTitle' placeholder='Help Title. No more than 20 words'><div id='editor'></div>");
    showEditor();
    $(".opeContainer #subTitle").val($(this).parent().parent().find(".trTitle").html());
    $(".opeContainer #editor .w-e-text").html($(this).parent().parent().next().find(".inner").html());
    $(".opeContainer").slideDown();
    that = $(this).parent().parent(); //current modify item
    $(".icon-baocun").off("click").on("click", helpSave);
    $("#subTitle").off("change").on("change", titleChange);
}

//save changes
function helpSave() {
    var text = editor.txt.text();
    var content = editor.txt.html();
    var title = $("#subTitle").val();
    var id = $(this).attr("value");
    //输入框中内容不为空
    if (text != "" && text != null) {
        $.ajax({
            type: "post",
            url: "/manage/helpInfo/update.action",
            data: {"title": title, "content": content, "id": id},
            dataType: "json",
            success: function (data) {
                var data = 1;
                if (data.status == 0) {
                    alert(data.msg);
                    return;
                }
                that.find(".trTitle").html(title);
                that.next().find(".inner").html(content);
                that.find(".updateTime").html(getformatDate());
                $("#bg").slideUp();
                $(".opeContainer").slideUp();
                $("#subTitle").val("");
                $(".opeContainer .w-e-text").html("");
            },
            error: function () {
                console.log("save error");
            }
        });
    }
}

//subTitle's change event todo 判断重复
function titleChange() {
    if ($(this).val().trim() == "") {
        $("#subTitle").css("border-color", "red");
    } else {
        $("#subTitle").css("border-color", "#333");
    }
}

//delete help
function helpDelete() {
    var that = $(this).parent().parent();
    $.ajax({
        type: "get",
        url: "/manage/helpInfo/delete.action",
        data: {"helpInfoId": that.attr("value")},
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            that.next().remove();
            that.remove();
            if ($(".helpTable tbody tr").length == 0) {
                $(".helpTable").hide();
                $(".nothing").show();
            }
        }
    });
}

/*4*/
function packFeedback(data) {
    $(".nothing").hide();
    for (i = 0; i < data.list.length; i++) {
        $("#feedbackTable tbody").append("<tr level='" + data.list[i].level + "' value='" + data.list[i].id + "'><td>" +
            (i + 1) + "</td><td class='feedSend'>" + data.list[i].username + "</td><td class='feedContent'>" +
            data.list[i].content + "</td><td class='feedDate'>" + data.list[i].createTime + "</td></tr>");
    }
    $("#feedbackTable tbody tr").off("click").on("click", feedDetail);
    //4.pages
    $(".page").paging({
        totalPage: data.pages,
        callback: function (num) {
            $.ajax({
                type: "get",
                url: "/manage/feedback/list.action",
                data: {"pageNum": num},
                dataType: "json",
                success: function (data) {
                    if (data.status == 0) {
                        alert(data.msg);
                        return;
                    }
                    $("#feedbackTable tbody").empty();
                    if (data.data == undefined) {
                        $("#feedbackTable").hide();
                        $(".nothing").show();
                        return;
                    }
                    packFeedback(data.data);
                },
                error: function () {
                    console.log("feedback error");
                }
            });
        }
    });
}

//查看反馈详情
function feedDetail() {
    $.ajax({
        type: "get",
        url: "/manage/feedback/detail.action",
        data: {"level": $(this).attr("level")},
        success: function (data) {
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $("#bg").slideDown();
            $(".feedbackDetail").empty().append("<div class='sender'>" + data.data[0].username + "</div>" +
                "<div class='chats'></div><textarea class='response'></textarea><input type='button' class='feedBtn' value='发送'>");
            $(".feedBtn").off("click").on("click", sendFeedback);
            var feedClass;
            for (i = 0; i < data.data.length; i++) {
                feedClass = data.data[i].username == "admin" ? "class='rightSide'" : "";
                $(".chats").append("<div parent='" + data.data[i].parent + "' sequence='" + data.data[i].sequence + "' "
                    + feedClass + "><div class='date'>" + data.data[i].createTime + "</div><img src='" +
                    data.data[i].avatar + "'><span class='content'>" + data.data[i].content + "</span></div>");
            }
            $(".feedbackDetail").slideDown()
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
        url: "/manage/feedback/reply.action",
        data: {"content": response, "parent": parent, "sequence": sequence},
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $(".response").val("");
            $(".chats").append("<div parent='" + parent + "' sequence='" + sequence + "' class='rightSide'>" +
                "<div class='date'>" + getformatDate() + "</div><img src='" + adminAvatar + "'>" +
                "<span class='content'>" + response + "</span></div>");
        }
    });
}

/*5*/
function packStrategy(data) {
    //1.province
    $(".province").empty().append("<option value='0'>全部</option>");
    if (data.cities.length > 0) {
        for (i = 0; i < data.cities.length; i++) {
            $(".province").append("<option value='" + data.cities[i].id + "'>" + data.cities[i].name + "</option>");
        }
        //province click event
        $(".province").change(update);
    }
    //2.duration
    $(".duration").empty().append("<option>全部</option>");
    if (data.durations.length > 0) {
        for (i = 0; i < data.durations.length; i++) {
            $(".duration").append("<option>" + data.durations[i] + "</option>")
        }
        //duration click event
        $(".duration").change(update);
    }
    $(".strategies").empty();
    for (i = 0; i < data.strategies.list.length; i++) {
        $(".strategies").append("<input type='checkbox' class='adminStrategy' value='" + data.strategies.list[i].strategyId +
            "'><div value='" + data.strategies.list[i].strategyId + "' class='strategyItem'>" +
            "<img src='" + data.strategies.list[i].mainImg + "'><div class='strategyInfo'><span>"
            + data.strategies.list[i].strategyName + "</span><span>" + data.strategies.list[i].username +
            "</span><span>" + data.strategies.list[i].cityName + " " + data.strategies.list[i].duration +
            "</span></div></div>");
    }
    //4.pages
    $(".page").paging({
        totalPage: data.strategies.pages,
        callback: function (num) {
            $.ajax({
                type: "get",
                url: "/manage/strategy/list.action",
                data: {"pageNum": num},
                dataType: "json",
                success: function (data) {
                    if (data.status == 0) {
                        alert(data.msg);
                        return;
                    }
                    packStrategy(data.data);
                },
                error: function () {
                    console.log("admin strategy error");
                }
            });
        }
    });
    $(".strategies .strategyItem").off("click").on("click", strategyDetail);
}

function packUpdateStrategy(data) {
    $(".strategies").empty();
    for (i = 0; i < data.strategies.list.length; i++) {
        $(".strategies").append("<input type='checkbox' class='adminStrategy' value='" + data.strategies.list[i].strategyId +
            "'><div value='" + data.strategies.list[i].strategyId + "' class='strategyItem'>" +
            "<img src='" + data.strategies.list[i].mainImg + "'><div class='strategyInfo'><span>"
            + data.strategies.list[i].strategyName + "</span><span>" + data.strategies.list[i].username +
            "</span><span>" + data.strategies.list[i].cityName + " " + data.strategies.list[i].duration +
            "</span></div></div>");
    }
    //4.pages
    $(".page").paging({
        totalPage: data.strategies.pages,
        callback: function (num) {
            $.ajax({
                type: "get",
                url: "/manage/strategy/list.action",
                data: {"pageNum": num},
                dataType: "json",
                success: function (data) {
                    if (data.status == 0) {
                        alert(data.msg);
                        return;
                    }
                    packStrategy(data.data);
                },
                error: function () {
                    console.log("admin strategy error");
                }
            });
        }
    });
    $(".strategies .strategyItem").off("click").on("click", strategyDetail);
}

function update() {
    var cityId = $(".province option:selected").val();
    var duration = $(".duration option:selected").text();
    var sendData = {};
    if (cityId == 0) {
        if (duration != "全部") {
            sendData = {"duration": duration};
        }
    } else {
        sendData = {"cityId": cityId};
        if (duration != "全部") {
            sendData = {"cityId": cityId, "duration": duration};
        }
    }
    $.ajax({
        type: "get",
        url: "/manage/strategy/list.action",
        data: sendData,
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            packUpdateStrategy(data.data);
        },
        error: function () {
            console.log("updateStrategies error");
        }
    });
}

function strategyDetail() {
    var strategyId = $(this).attr("value");
    $.ajax({
        type: "get",
        url: "/manage/strategy/detail.action",
        data: {"strategyId": strategyId},
        dataType: "json",
        success: function (data) {
            console.log(JSON.stringify(data));
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $("#bg").slideDown();
            $(".strategyDetail").empty().append("<span>" + data.data.strategy.strategyName + "</span><span>"
                + data.data.strategy.username + "</span><span class='detailContent'></span>");
            for (i = 0; i < data.data.content.length; i++) {
                $(".detailContent").append("<div><span>" + data.data.catalog[i] + "</span><span>" +
                    data.data.content[i] + "</span></div>");
            }
            $(".strategyDetail").slideDown();
        },
        error: function () {
            console.log("strategy detail error");
        }
    });
}