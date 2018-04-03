var parameter;
$(function () {

    parameter = getQueryStringArgs();
    if (parameter == null || parameter == "") {
        window.location.href = "index.jsp";
        return;
    }
    $.ajax({
        type: "get",
        url: "/strategy/search.action",
        data: {"content": parameter},
        dataType: "json",
        success: function (data) {
            $(".searchContainer").empty();
            if (data.status == -1) {  //没有匹配信息
                $(".searchContainer").hide();
                $("#pageNum").hide();
                $(".nothing").html("暂无相关信息...").show();
                return;
            }
            packUpdateList(data.data);
        },
        error: function () {
            console.log("search error");
        }
    });
});


function packUpdateList(data) {
    $(".searchContainer").show();
    $("#pageNum").show();
    $(".nothing").html("").hide();
    for (var i = 0; i < data.list.length; i++) {
        $(".searchContainer").append("<div value='" + data.list[i].strategyId + "' class='strategy'>" +
        "<img id='strategyBg' src='" + data.list[i].mainImg + "'>" +
        "<img id='avatar' src='" + data.list[i].avatar + "'><div class='s_msg'>" +
        "<span class='title'>标题: " + data.list[i].strategyName + "</span><span class='user'>作者 : " +
        data.list[i].username + "</span><span class='tags'>标签 : " +
        data.list[i].cityName + " " + data.list[i].duration + "</span></div></div>");
    }
    //strategies click
    $(".searchContainer .strategy").off("click").on("click", function () {
        window.location.href = "strategyItem.jsp?strategyId=" + $(this).attr("value");
    });
    //pages
    $("#page").paging({
        totalPage: data.pages,
        callback: function (num) {
            $.ajax({
                type: "get",
                url: "/strategy/search.action",
                data: {"content": parameter, "pageNum": num},
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