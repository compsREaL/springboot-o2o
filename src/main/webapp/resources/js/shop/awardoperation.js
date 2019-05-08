$(function () {
    var awardId = getQueryString("awardId");
    //通过awardId获取奖品信息
    var infoUrl = "/o2o/shopadmin/getawardbyid?awardId="+awardId;
    //更新奖品信息
    var awardPostUrl = "/o2o/shopadmin/modifyaward";

    var isEdit = false;

    if (awardId){
        getInfo(awardId);
        isEdit = true;
    } else {
        awardPostUrl = "/o2o/shopadmin/addaward";
    }

    function getInfo() {
        $.getJSON(infoUrl,function (data) {
            if (data.success){
                var award = data.award;
                $("#award-name").val(award.awardName);
                $("#award-desc").val(award.awardDesc);
                $("#priority").val(award.priority);
                $("#point").val(award.point);
            }
        });
    }

    //为新增按钮绑定事件
    $("#submit").click(function () {

        var verifyCodeActual = $("#j_kaptcha").val();
        if (!verifyCodeActual){
            $.toast("请输入验证码！");
            $("#kaptcha_img").click();
            return;
        }
        var award = {};
        award.awardName = $("#award-name").val();
        award.awardDesc = $("#award-desc").val();
        award.priority = $("#priority").val();
        award.point = $("#point").val();
        award.awardId = awardId ? awardId:"";
        //获取缩略图文件流
        var thumbnail = $("#small-img")[0].files[0];
        //生成表单对象，用于接收参数并传递给后台
        var formData = new FormData();
        formData.append("thumbnai",thumbnail);
        formData.append("awardStr",JSON.stringify(award));
        formData.append("verifyCodeActual",verifyCodeActual);
        //将数据提交给后台
        $.ajax({
            url:awardPostUrl,
            type:"POST",
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function (data) {
                if (data.success){
                    $.toast("操作成功");
                    window.location.href="/o2o/shopadmin/awardmanagement";
                } else {
                    $.toast("操作失败");
                    $("#kaptcha_img").click();
                }
            }
        });
    });
});