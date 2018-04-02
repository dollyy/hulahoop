var adminAvatar;    //管理员头像
var i;              //循环参数
var editor;         //富文本编辑器
var searchInp;      //搜索框
var userId;

$(function () {

    //select2
    $(document).ready(function () {
        $('.js-example-basic-single').select2();
    });

    //有没有消息未查看
    $.ajax({
        type: "get",
        url: "/manage/feedback/queryNotice.action",
        dataType: "json",
        success: function (data) {
            if (data.status != 1) {
                $("#msgDot").css("display", "none");
                return;
            }
            $("#msgDot").css("display", "inline-block");
        },
        error: function () {
            console.log("get notice error");
        }
    });

    //查看消息
    $(".icon-lingdang").click(feedClick);

    //获取用户信息
    $.ajax({
        type: "get",
        url: "/user/queryUserInformation.action",
        dataType: "json",
        success: function (data) {
            if (data.status == -2 || data.status == -1) {    //用户未登录 || 用户不存在
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.data.id != 8) {
                window.location.href = "adminSignin.html";
            }
            adminAvatar = data.data.avatar;

            //dwr页面加载
            dwrMessage.onPageLoad(data.data.id);
        }
    });

    //退出登录
    $(".icon-icon").click(function () {
        $.ajax({
            type: "get",
            url: "/manage/user/logout.action",
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {     //退出登录失败
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
        var searchIndex = 0;
        $('.catalog li').each(function (index) {
            if ($(this).hasClass("clicked")) {
                searchIndex = index;
            }
        });
        searchInp = search();
        switch (searchIndex) {
            case 0:
                break;
            case 1: //搜索帮助信息
                helpSearch(searchInp);
                break;
            case 2:
                break;
            case 3: //搜索反馈信息
                feedSearch(searchInp);
                break;
            case 4: //搜索攻略信息
                strategySearch(searchInp);
                break;
        }
    });

    /* 1.index */
    $(".index").click(indexClick);

    /* 2.help */
    $(".help").click(helpClick);
    //add icon-xinzeng
    $(".icon-xinzeng").click(function () {
        $("#bg").slideDown();
        $(".opeContainer").empty().append("<input type='text' id='subTitle' placeholder='帮助信息标题，不要超过20字'>" +
            "<div id='editor'></div><input type='button' value='添 加'  id='addBtn'>");
        $("#addBtn").off("click").on("click", addHelp);
        $("#subTitle").off("click").on("change", titleChange);
        showEditor();
        $(".opeContainer").slideDown();
    });

    /* 3.res */
    $(".res").click(resClick);

    /* 4.feeback */
    $(".feedback").click(feedClick);

    /* 5.strategy*/
    $(".strategy").click(strategyClick);
    //'操作'
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
                if (data.status == -2) {    //用户未登录
                    window.location.href = "adminSignin.html";
                    return;
                }
                if (data.status == -4) {    //非管理员
                    window.location.href = "../pages/index.jsp";
                    return;
                }
                if (data.status == 0) {
                    alert(data.msg);
                    return;
                }
                window.location.href = "admin.html?catalog=5";
            }
        });
    });

    switch (parseInt(getQueryStringArgs())) {
        case 1:
            indexClick();
            break;
        case 2:
            helpClick();
            break;
        case 3:
            resClick();
            break;
        case 4:
            feedClick();
            break;
        case 5:
            strategyClick();
            break;
        default:
            break;
    }

});

/* 1 */
function indexClick() {
    $(".indexContainer").show().siblings().hide();
    $(".catalog li").eq(0).addClass("clicked").siblings().removeClass("clicked");
    $(".right .location span").html($(this).children().last().html());
    //禁止搜索
    $("#searchInp").attr("disabled", true);
}

/* 2 */
function helpClick() {  //可以搜索
    $("#searchInp").removeAttr("disabled").val("");
    $.ajax({
        type: "get",
        url: "/manage/helpInfo/list.action",
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
            $(".helpTable tbody").empty();
            if (data.status == -1) {    //没有匹配信息
                $(".helpTable").hide();
                $(".nothing").show();
                return;
            }
            $(".helpContainer").show().siblings().hide();
            $(".catalog li").eq(1).addClass("clicked").siblings().removeClass("clicked");
            $(".right .location span").html($(this).children().last().html());
            //封装信息
            packHelp(data.data);
        },
        error: function () {
            console.log("help error");
        }
    });
}

//搜索帮助信息
function helpSearch(searchInp) {
    $.ajax({
        type: "get",
        url: "/manage/helpInfo/search.action",
        data: {"content": searchInp},
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
            if (data.status == 0) {     //参数错误
                alert(data.msg);
                return;
            }
            $(".helpTable tbody").empty();
            if (data.status == -1) {    //没有匹配信息
                $(".helpTable").hide();
                $(".nothing").show();
                return;
            }
            packSearchHelp(data.data);
        }
    });
}

//搜索反馈信息
function feedSearch(searchInp) {
    $.ajax({
        type: "get",
        url: "/manage/feedback/search.action",
        data: {"content": searchInp},
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $("#feedbackTable tbody").empty();
            if (data.status == -1) {    //没有匹配信息
                $("#feedbackTable").hide();
                $(".nothing").show();
                return;
            }
            packSearchFeedback(data.data);
        },
        error: function () {
            console.log("feedback error");
        }
    });
}

//搜索攻略信息
function strategySearch(searchInp) {
    $.ajax({
        type: "get",
        url: "/manage/strategy/search.action",
        data: {"content": searchInp},
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $(".strategies").empty();
            if (data.status == -1) {    //没有匹配信息
                $(".nothing").show();
                return;
            }
            packSearchStrategy(data.data);
        },
        error: function () {
            console.log("admin strategy error");
        }
    });
}

//create editor
function showEditor() {
    //wangEidtor
    var E = window.wangEditor;
    editor = new E('#editor');
    //name名称为uploadImage
    editor.customConfig.uploadFileName = 'file';
    //上传图片
    editor.customConfig.uploadImgServer = '/manage/helpInfo/richtext_img_upload.action';
    editor.customConfig.uploadImgHooks = {
        // 图片上传之前触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，files 是选择的图片文件
        before: function (xhr, editor, files) {

        },
        // 图片上传并返回结果，图片插入成功之后触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
        success: function (xhr, editor, result) {
            console.log("wangEditor succeed-->" + result);

        },
        // 图片上传并返回结果，但图片插入错误时触发
        fail: function (xhr, editor, result) {
            console.log("wangEditor failed--->" + result)
        },
        // 图片上传出错时触发
        error: function (xhr, editor) {
        },
        // 图片上传超时时触发
        timeout: function (xhr, editor) {
        },
        // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
        // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）

        // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
        // insertImg 是插入图片的函数
        customInsert: function (insertImg, result, editor) {
            // 举例：假如上传图片成功后，服务器端返回的是 {url:'....'} 这种格式，即可这样插入图片：
            // result 必须是一个 JSON 格式字符串！！！否则报错
            var url = result.file_path
            console.log(url);
            insertImg(url);
        }
    };
    editor.create();
}

//封装帮助信息
function packHelp(data) {
    $(".helpTable").show();
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
                type: "get",
                url: "/manage/helpInfo/list.action",
                data: {"pageNum": num},
                dataType: "json",
                success: function (data) {
                    if (data.status == -2) {    //用户未登录
                        window.location.href = "adminSignin.html";
                        return;
                    }
                    if (data.status == -4) {    //非管理员
                        window.location.href = "../pages/index.jsp";
                        return;
                    }
                    $(".helpTable tbody").empty();
                    if (data.status == -1) {    //没有匹配信息
                        $(".helpTable").hide();
                        $(".nothing").show();
                        return;
                    }
                    packUpdateHelp(data.data);
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

//封装更新后的帮助信息
function packUpdateHelp(data) {
    $(".helpTable").show();
    $(".nothing").hide();
    for (i = 0; i < data.list.length; i++) {
        $(".helpTable tbody").append("<tr value='" + data.list[i].id + "'><td>" + (i + 1) + "</td><td class='trTitle'>" +
            data.list[i].title + "</td><td>" + formatDate(data.list[i].createTime) + "</td><td class='updateTime'>" +
            formatDate(data.list[i].updateTime) + "</td><td><span class='iconfont icon-close-eye' title='隐藏'></span>" +
            "<span class='iconfont icon-chakan' title='查看'></span><span class='iconfont icon-bianji' title='编辑'>" +
            "</span><span class='iconfont icon-delete' title='删除'></span></td></tr><tr style='border:none'>" +
            "<td colspan='5' style='padding: 0'><div class='inner'>" + data.list[i].content + "</div></td></tr>");
    }
    $(".icon-close-eye").off("click").on("click", helpHide);    //隐藏
    $(".icon-chakan").off("click").on("click", helpView);    //查看
    $(".icon-bianji").off("click").on("click", helpEdit);    //编辑
    $(".icon-delete").off("click").on("click", helpDelete);  //删除
}

//封装帮助的搜索信息
function packSearchHelp(data) {
    $(".helpTable").show();
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
                type: "get",
                url: "/manage/helpInfo/search.action",
                data: {"pageNum": num, "content": searchInp},
                dataType: "json",
                success: function (data) {
                    if (data.status == -2) {    //用户未登录
                        window.location.href = "adminSignin.html";
                        return;
                    }
                    if (data.status == -4) {    //非管理员
                        window.location.href = "../pages/index.jsp";
                        return;
                    }
                    $(".helpTable tbody").empty();
                    if (data.status == -1) {    //没有匹配信息
                        $(".helpTable").hide();
                        $(".nothing").show();
                        return;
                    }
                    packSearchHelp(data.data);
                }
            });
        }
    });
    $(".icon-close-eye").off("click").on("click", helpHide);    //隐藏
    $(".icon-chakan").off("click").on("click", helpView);    //查看
    $(".icon-bianji").off("click").on("click", helpEdit);    //编辑
    $(".icon-delete").off("click").on("click", helpDelete);  //删除
}

//添加帮助信息
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
                window.location.href = "admin.html?catalog=2";
            },
            error: function () {
                console.log("add title error");
            }
        });
    }
}

//隐藏帮助信息的内容
function helpHide() {
    $(this).parent().parent().next().find(".inner").css({"height": "0", "min-height": "0"});
    $(this).css({"font-size": "0", "margin": "0"});
    $(this).next().show();
}

//查看帮助信息的内容
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

//编辑帮助信息的内容
function helpEdit() {
    var helpId = $(this).parent().parent().attr("value");
    $("#bg").slideDown();
    $(".opeContainer").empty().append("<span value='" + helpId + "' class='iconfont icon-baocun'></span>" +
        "<input type='text' id='subTitle' placeholder='帮助信息标题，不要超过20字'><div id='editor'></div>");
    showEditor();
    $(".opeContainer #subTitle").val($(this).parent().parent().find(".trTitle").html());
    $(".opeContainer #editor .w-e-text").html($(this).parent().parent().next().find(".inner").html());
    $(".opeContainer").slideDown();
    that = $(this).parent().parent(); //current modify item
    $(".icon-baocun").off("click").on("click", helpSave);
    $("#subTitle").off("change").on("change", titleChange);
}

//保存修改
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
                window.location.href = "admin.html?catalog=2";
            },
            error: function () {
                console.log("save error");
            }
        });
    }
}

//修改子标题 todo 判断重复
function titleChange() {
    if ($(this).val().trim() == "") {
        $("#subTitle").css("border-color", "red");
    } else {
        $("#subTitle").css("border-color", "#333");
    }
}

//删除帮助信息
function helpDelete() {
    var that = $(this).parent().parent();
    $.ajax({
        type: "get",
        url: "/manage/helpInfo/delete.action",
        data: {"helpInfoId": that.attr("value")},
        dataType: "json",
        success: function (data) {
            window.location.href = "admin.html?catalog=2";
        }
    });
}


/* 3 */
function resClick() {
    $(".resContainer").show().siblings().hide();
    $(".catalog li").eq(2).addClass("clicked").siblings().removeClass("clicked");
    $(".right .location span").html($(this).children().last().html());
    //禁止搜索
    $("#searchInp").attr("disabled", true);
}

/* 4 */
function feedClick() {
    $("#searchInp").removeAttr("disabled").val("");
    $.ajax({
        type: "get",
        url: "/manage/feedback/list.action",
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
            if (data.status == 0) {     //非法参数
                alert(data.msg);
                return;
            }
            $("#feedbackTable tbody").empty();
            if (data.status == -1) {
                $("#feedbackTable").hide();
                $(".nothing").show();
                return;
            }
            $(".feedbackContainer").show().siblings().hide();
            $(".catalog li").eq(3).addClass("clicked").siblings().removeClass("clicked");
            $(".right .location span").html($(this).children().last().html());
            packFeedback(data.data);
        },
        error: function () {
            console.log("feedback error");
        }
    });
}

//封装反馈信息
var readClass;

function packFeedback(data) {
    $("#feedbackTable").show();
    $(".nothing").hide();
    for (i = 0; i < data.list.length; i++) {
        readClass = data.list[i].status > 0 ? "" : "read";
        $("#feedbackTable tbody").append("<tr class='" + readClass + "' level='" + data.list[i].level + "' value='" +
            data.list[i].id + "'><td>" + (i + 1) + "</td><td class='feedSend' value='" + data.list[i].userId + "'>" + data.list[i].username + "</td>" +
            "<td class='feedContent'>" + data.list[i].content + "</td><td class='feedDate'>" + data.list[i].updateTime +
            "</td></tr>");
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
                    if (data.status == -2) {    //用户未登录
                        window.location.href = "adminSignin.html";
                        return;
                    }
                    if (data.status == -4) {    //非管理员
                        window.location.href = "../pages/index.jsp";
                        return;
                    }
                    if (data.status == 0) {
                        alert(data.msg);
                        return;
                    }
                    $("#feedbackTable tbody").empty();
                    if (data.status == -1) {    //没有匹配信息
                        $("#feedbackTable").hide();
                        $(".nothing").show();
                        return;
                    }
                    packUpdateFeedback(data.data);
                },
                error: function () {
                    console.log("feedback error");
                }
            });
        }
    });
}

//封装更新后的反馈信息
function packUpdateFeedback(data) {
    $("#feedbackTable").show();
    $(".nothing").hide();
    for (i = 0; i < data.list.length; i++) {
        readClass = data.list[i].status > 0 ? "" : "read";
        $("#feedbackTable tbody").append("<tr class='" + readClass + "' level='" + data.list[i].level + "' value='" +
            data.list[i].id + "'><td>" + (i + 1) + "</td><td class='feedSend'>" + data.list[i].username + "</td>" +
            "<td class='feedContent'>" + data.list[i].content + "</td><td class='feedDate'>" + data.list[i].updateTime +
            "</td></tr>");
    }
    $("#feedbackTable tbody tr").off("click").on("click", feedDetail);
}

//封装反馈的搜索信息
function packSearchFeedback(data) {
    $("#feedbackTable").show();
    $(".nothing").hide();
    for (i = 0; i < data.list.length; i++) {
        readClass = data.list[i].status > 0 ? "" : "read";
        $("#feedbackTable tbody").append("<tr class='" + readClass + "' level='" + data.list[i].level + "' value='" +
            data.list[i].id + "'><td>" + (i + 1) + "</td><td class='feedSend'>" + data.list[i].username + "</td>" +
            "<td class='feedContent'>" + data.list[i].content + "</td><td class='feedDate'>" + data.list[i].updateTime +
            "</td></tr>");
    }
    $("#feedbackTable tbody tr").off("click").on("click", feedDetail);
    //4.pages
    $(".page").paging({
        totalPage: data.pages,
        callback: function (num) {
            $.ajax({
                type: "get",
                url: "/manage/feedback/search.action",
                data: {"content": searchInp, "pageNum": num},
                dataType: "json",
                success: function (data) {
                    if (data.status == -2) {    //用户未登录
                        window.location.href = "adminSignin.html";
                        return;
                    }
                    if (data.status == -4) {    //非管理员
                        window.location.href = "../pages/index.jsp";
                        return;
                    }
                    if (data.status == 0) {
                        alert(data.msg);
                        return;
                    }
                    $("#feedbackTable tbody").empty();
                    if (data.status == -1) {    //没有匹配信息
                        $("#feedbackTable").hide();
                        $(".nothing").show();
                        return;
                    }
                    packSearchFeedback(data.data);
                },
                error: function () {
                    console.log("feedback error");
                }
            });
        }
    });
}

var receiveId;
//查看反馈详情
var level;

function feedDetail() {
    var that = $(this);
    level = $(this).attr("level");
    alert(that.find(".feedSend").attr("value"));
    $.ajax({
        type: "get",
        url: "/manage/feedback/updateFeedStatus.action",
        data: {"level": level, "receiveId": that.find(".feedSend").attr("value")},
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {  //用户未登录
                window.location.href = "index.jsp";
                return;
            }
            if (data.status == -3) {  //参数错误
                alert(data.msg);
            }
            that.addClass("read");
        },
        error: function () {
            console.log("update feed error");
        }
    });
    $.ajax({
        type: "get",
        url: "/manage/feedback/detail.action",
        data: {"level": level},
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $("#bg").slideDown();
            receiveId = data.data[0].userId;
            $(".feedbackDetail").empty().append("<div class='sender'>" + data.data[0].username + "</div>" +
                "<div class='chats'></div><textarea class='response'></textarea><input type='button' class='feedBtn' value='发送'>");
            $(".feedBtn").off("click").on("click", sendFeedback);
            var feedClass;
            for (i = 0; i < data.data.length; i++) {
                feedClass = data.data[i].username == "admin" ? "class='rightSide'" : "";
                $(".chats").append("<div level='" + data.data[i].level + "' parent='" + data.data[i].parent + "' sequence='" + data.data[i].sequence + "' "
                    + feedClass + "><div class='date'>" + data.data[i].createTime + "</div><img src='" +
                    data.data[i].avatar + "'><span class='content'>" + data.data[i].content + "</span></div>");
            }
            $(".feedbackDetail").slideDown();
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
    parent = parent == 0 ? $(".chats>div:last").attr("level") : parent;
    var sequence = $(".chats>div:last").attr("sequence");
    $.ajax({
        type: "post",
        url: "/manage/feedback/reply.action",
        data: {"content": response, "parent": parent, "sequence": sequence, "receiveId": receiveId},
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $(".response").val("");
            $(".chats").append("<div parent='" + parent + "' sequence='" + data.data + "' class='rightSide'>" +
                "<div class='date'>" + getformatDate() + "</div><img src='" + adminAvatar + "'>" +
                "<span class='content'>" + response + "</span></div>");
            dwrMessage.publishFeed(receiveId);
        }
    });
}

//推送信息处理
function showMessage(data) {
    $("#msgDot").css("display", "inline-block");
}

/* 5 */
function strategyClick() {
    $("#searchInp").removeAttr("disabled").val("");
    $.ajax({
        type: "get",
        url: "/manage/strategy/list.action",
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
            //没有匹配信息
            if (data.data.cities == undefined || data.data.strategies == undefined || data.data.durations == undefined) {
                $(".nothing").show();
                return;
            }
            packStrategy(data.data);
            $(".strategyContainer").show().siblings().hide();
            $(".catalog li").eq(4).addClass("clicked").siblings().removeClass("clicked");
            $(".right .location span").html($(this).children().last().html());
        },
        error: function () {
            console.log("admin strategy error");
        }
    });
}

//封装攻略信息
function packStrategy(data) {
    $(".nothing").hide();
    //1.cities
    $(".province").empty().append("<option value='0'>全部</option>");
    for (i = 0; i < data.cities.length; i++) {
        $(".province").append("<option value='" + data.cities[i].id + "'>" + data.cities[i].name + "</option>");
    }
    $(".province").change(update);  //province click event
    //2.duration
    $(".duration").empty().append("<option>全部</option>");
    for (i = 0; i < data.durations.length; i++) {
        $(".duration").append("<option>" + data.durations[i] + "</option>")
    }
    $(".duration").change(update);  //duration click event
    //3.strategies
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
                    if (data.status == -2) {    //用户未登录
                        window.location.href = "adminSignin.html";
                        return;
                    }
                    if (data.status == -4) {    //非管理员
                        window.location.href = "../pages/index.jsp";
                        return;
                    }
                    if (data.status == 0) {
                        alert(data.msg);
                        return;
                    }
                    $(".strategies").empty();
                    packUpdateStrategy(data.data);
                },
                error: function () {
                    console.log("admin strategy error");
                }
            });
        }
    });
    $(".strategies .strategyItem").off("click").on("click", strategyDetail);
}

//封装更新的攻略信息
function packUpdateStrategy(data) {
    $(".nothing").hide();
    for (i = 0; i < data.strategies.list.length; i++) {
        $(".strategies").append("<input type='checkbox' class='adminStrategy' value='" + data.strategies.list[i].strategyId +
            "'><div value='" + data.strategies.list[i].strategyId + "' class='strategyItem'>" +
            "<img src='" + data.strategies.list[i].mainImg + "'><div class='strategyInfo'><span>"
            + data.strategies.list[i].strategyName + "</span><span>" + data.strategies.list[i].username +
            "</span><span>" + data.strategies.list[i].cityName + " " + data.strategies.list[i].duration +
            "</span></div></div>");
    }
    $(".strategies .strategyItem").off("click").on("click", strategyDetail);
}

//封装更新的攻略信息
function packCityDurationStrategy(data) {
    $(".nothing").hide();
    for (i = 0; i < data.strategies.list.length; i++) {
        $(".strategies").append("<input type='checkbox' class='adminStrategy' value='" + data.strategies.list[i].strategyId +
            "'><div value='" + data.strategies.list[i].strategyId + "' class='strategyItem'>" +
            "<img src='" + data.strategies.list[i].mainImg + "'><div class='strategyInfo'><span>"
            + data.strategies.list[i].strategyName + "</span><span>" + data.strategies.list[i].username +
            "</span><span>" + data.strategies.list[i].cityName + " " + data.strategies.list[i].duration +
            "</span></div></div>");
    }
    $(".strategies .strategyItem").off("click").on("click", strategyDetail);
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
                    if (data.status == -2) {    //用户未登录
                        window.location.href = "adminSignin.html";
                        return;
                    }
                    if (data.status == -4) {    //非管理员
                        window.location.href = "../pages/index.jsp";
                        return;
                    }
                    if (data.status == 0) {
                        alert(data.msg);
                        return;
                    }
                    $(".strategies").empty();
                    packUpdateStrategy(data.data);
                },
                error: function () {
                    console.log("admin strategy error");
                }
            });
        }
    });
}

//封装攻略的搜索信息
function packSearchStrategy(data) {
    $(".nothing").hide();
    for (i = 0; i < data.list.length; i++) {
        $(".strategies").append("<input type='checkbox' class='adminStrategy' value='" + data.list[i].strategyId + "'>" +
            "<div value='" + data.list[i].strategyId + "' class='strategyItem'><img src='" + data.list[i].mainImg + "'>" +
            "<div class='strategyInfo'><span>" + data.list[i].strategyName + "</span><span>" + data.list[i].username +
            "</span><span>" + data.list[i].cityName + " " + data.list[i].duration + "</span></div></div>");
    }
    //4.pages
    $(".page").paging({
        totalPage: data.pages,
        callback: function (num) {
            $.ajax({
                type: "get",
                url: "/manage/strategy/search.action",
                data: {"content": searchInp, "pageNum": num},
                dataType: "json",
                success: function (data) {
                    if (data.status == -2) {    //用户未登录
                        window.location.href = "adminSignin.html";
                        return;
                    }
                    if (data.status == -4) {    //非管理员
                        window.location.href = "../pages/index.jsp";
                        return;
                    }
                    if (data.status == 0) {
                        alert(data.msg);
                        return;
                    }
                    $(".strategies").empty();
                    if (data.status == -1) {
                        $(".nothing").show();
                        return;
                    }
                    packSearchStrategy(data.data);
                },
                error: function () {
                    console.log("admin strategy error");
                }
            });
        }
    });
    $(".strategies .strategyItem").off("click").on("click", strategyDetail);
}

//更新攻略列表
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
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
            if (data.status == 0) {
                alert(data.msg);
                return;
            }
            $(".strategies").empty();
            if (data.data.strategies == undefined) {
                $(".nothing").show();
                return;
            }
            packCityDurationStrategy(data.data);
        },
        error: function () {
            console.log("updateStrategies error");
        }
    });
}

//攻略详情
function strategyDetail() {
    var strategyId = $(this).attr("value");
    $.ajax({
        type: "get",
        url: "/manage/strategy/detail.action",
        data: {"strategyId": strategyId},
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {    //用户未登录
                window.location.href = "adminSignin.html";
                return;
            }
            if (data.status == -4) {    //非管理员
                window.location.href = "../pages/index.jsp";
                return;
            }
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