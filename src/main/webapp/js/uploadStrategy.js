//wangEidtor
var E = window.wangEditor;
var editor = new E('#editor');
//name名称为uploadImage
editor.customConfig.uploadFileName = 'file';
//上传图片
editor.customConfig.uploadImgServer = '/strategy/richtext_img_upload.action';
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
        var url = result.file_path;
        console.log(url);
        insertImg(url);
    }
};
editor.create();

var data, that, i;

$(function () {
    //select2
    $(document).ready(function () {
        $('.js-example-basic-single').select2();
    });
    $(".form-control").select2({
        tags: true
    });


    $.ajax({
        type: "get",
        url: "/helpInfo/listByUpload.action",
        dataType: "json",
        success: function (data) {
            if (data.status == -2) {  //用户未登录
                window.location.href = "index.jsp";
                return;
            }

            //1.tips
            if (data.data.helpInfo != undefined) {
                for (i = 0; i < data.data.helpInfo.length; i++) {
                    $(".helpPage").append("<div class='tip' id='tip'><div>" + data.data.helpInfo[i].title + "</div></div>");
                    $("#tip").append("<div>" + data.data.helpInfo[i].content + "</div>");
                }
            }

            //2.cities
            if (data.data.cities != undefined) {
                for (i = 0; i < data.data.cities.length; i++) {
                    $(".cities").append("<option value='" + data.data.cities[i].id + "'>" + data.data.cities[i].name + "</option>");
                }
            }
        },
        error: function () {
            window.location.href = "systemError.jsp";
        }
    });

    /* strategy title */
    $("#title").change(function () {
        if ($(this).val().trim() == "") {
            $("#title").css("border-color", "red");
        } else {
            $("#title").css("border-color", "#999");
        }
    });

    /* sub title */
    $("#subTitle").change(function () {
        if ($(this).val().trim() == "") {
            $("#subTitle").css("border-color", "red");
        } else {
            $("#subTitle").css("border-color", "#999");
        }
    });

    /* add day */
    $("#addBtn").click(function () {
        var subTitle = $("#subTitle").val();
        var text = editor.txt.text();
        console.log(("text---->" + text));
        //输入框中内容不为空
        if (text == "" || text == null) {
            return;
        }
        var day = ($(".strategyContainer").find(".day").length + 1);
        $(".strategyContainer").append("<div class='day' ondrop='drop(event,this)' ondragover='allowDrop(event)' " +
            "draggable='true' ondragstart='drag(event, this)'><div class='dayTitle mb10'><input id='daySub' type='text' " +
            "value=" + subTitle + " readonly><span class='iconfont icon-delete' title='删除'></span>" +
            "<span class='iconfont icon-bianji' title='编辑'></span><span class='iconfont icon-baocun' title='保存'>" +
            "</span></div><div class='content' id='day" + day + "'>" + editor.txt.html() + "</div></div>");
        //clean content
        $(".w-e-text").html("<p><br></p>");
        $("#subTitle").val("");
        //click event
        $(".strategyContainer .day .icon-bianji").off("click").on("click", editDay);
        $(".strategyContainer .day .icon-baocun").off("click").on("click", saveDay);
        $(".strategyContainer .day .icon-delete").off("click").on("click", deleteDay);
    });

    /* submit strategy */
    $("#subBtn").click(function () {
        var cityId = $(".cities option:selected").val();
        var duration = $("#duration").val();
        if (cityId == 0) {
            $(".tips").html("请选择城市").show().fadeOut(2000);
            return;
        }
        if (duration == null || duration == "") {
            $(".tips").html("请选择时长").show().fadeOut(2000);
            return;
        }
        var title = $("#title").val();
        if (title == null || title == "") {
            $("#title").css("border-color", "red");
            $(".tips").html("请输入标题").show().fadeOut(2000);
            return;
        }
        if ($(".strategyContainer").find(".day").length == 0) {
            $(".tips").html("暂无内容").show().fadeOut(2000);
            return;
        }
        var strategy = "";
        $(".strategyContainer .day").each(function () {
            strategy += $(this).find("#daySub").val() + "@#" + $(this).find(".content").html() + "#-";
        });
        var strategy = {
            "name": title,
            "cityId": cityId,
            "duration": duration,
            "content": strategy.substr(0, strategy.length - 2)
        };
        $.ajax({
            type: "post",
            url: "/strategy/add.action",
            data: strategy,
            success: function (data) {
                alert(data.msg);
                window.location.href = "center.jsp";
            },
            error: function () {
                window.location.href = "systemError.jsp";
            }
        })
    });

    /* hide help */
    $("#helpClose").click(function () {
        $(".helpPage").hide();
    });

    /* show help */
    $(".icon-iconhelp").click(function () {
        $(".helpPage").show();
    });

});

/* move start */
function allowDrop(ev) {
    ev.preventDefault();    //prevent default behavior
}

var srcdiv = null;

function drag(ev, divdom) {   //store dom and data
    srcdiv = divdom;
    ev.dataTransfer.setData("text/html", divdom.innerHTML);
}

function drop(ev, divdom) {   //exchange data
    ev.preventDefault();    //prevent default behavior
    if (srcdiv != divdom) {
        srcdiv.innerHTML = divdom.innerHTML;
        divdom.innerHTML = ev.dataTransfer.getData("text/html");
    }

    //click event
    $(".strategyContainer .day .icon-bianji").off("click").on("click", editDay);
    $(".strategyContainer .day .icon-baocun").off("click").on("click", saveDay);
    $(".strategyContainer .day .icon-delete").off("click").on("click", deleteDay);
}

/* move end */
var editor2;
//edit day's content
function editDay() {
    //1.css
    $(this).parent().parent().attr("draggable", "false").css("cursor", "auto");
    $(this).next().show();
    $(this).hide();
    $(this).parent().find("#daySub").removeAttr("readonly");
    $(this).parent().find("#daySub").css("background", "#ccc");
    //2.data
    that = $(this).parent().parent().find(".content");
    data = that.html();
    that.empty();
    var contentId = "#" + that.attr("id");
    editor2 = new E(contentId);
    //name名称为uploadImage
    editor2.customConfig.uploadFileName = 'file';
    //上传图片
    editor2.customConfig.uploadImgServer = '/strategy/richtext_img_upload.action';
    editor2.customConfig.uploadImgHooks = {
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
            var url = result.file_path;
            console.log(url);
            insertImg(url);
        }
    };
    editor2.create();
    $(this).parent().parent().find(".content .w-e-text").html(data);
}

//save day's content
function saveDay() {
    $(this).parent().parent().attr("draggable", "true").css("cursor", "pointer");
    that = $(this).parent().parent().find(".content");
    var text = editor2.txt.text();
    console.log(("text---->" + text));
    //输入框中内容不为空
    if (text == "" || text == null) {
        return;
    }
    data = that.find(".w-e-text").html();
    //1.css
    $(this).prev().show();
    $(this).hide();
    $(this).parent().find("#daySub").attr("readonly");
    $(this).parent().find("#daySub").css("background", "#fff");
    //2.data
    that.empty().append(data);
}

//delete day's content
function deleteDay() {
    $(this).parent().parent().remove();
}
