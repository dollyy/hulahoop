$(function () {
    $("#feedbackBtn").click(function () {
        var content = $("#content").val().trim();
        if (content != null && content != "") {
            $.ajax({
                type: "post",
                url: "/feedback/add.action",
                data: {"content": content},
                dataType: "json",
                success: function (data) {
                    if(data.status == -2){  //用户未登录
                        $("#signinContainer").slideDown();  //显示登录框
                        $("#bg").slideDown();   //显示背景
                        return;
                    }
                    if(data.status == 1){
                        alert(data.msg);
                        window.location.href = "index.jsp";
                    }
                },
                error: function () {
                    window.location.href = "systemError.jsp";
                }
            })
        }
    });
});