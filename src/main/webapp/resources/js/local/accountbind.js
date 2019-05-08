$(function () {
    //绑定账号的controller url
    var bindUrl = "/o2o/local/bindlocalauth";
    //从地址栏的url里获取usertype
    var userType = getQueryString("usertype");

    $("#submit").click(function () {
        var userName = $("#username").val();
        var password = $("#pwd").val();
        var verifyCodeActual = $("#j_kaptcha").val();

        var needVerify = false;
        if (!verifyCodeActual){
            $.toast("请输入验证码");
            return;
        }
        //访问后台，绑定账号
        $.ajax({
            url:bindUrl,
            async:false,
            cache:false,
            type:"POST",
            dataType:"json",
            data:{
                userName:userName,
                password:password,
                verifyCodeActual:verifyCodeActual
            },
            success:function (data) {
                if (data.success){
                    $.toast("绑定成功！");
                    if (userType==1){
                        window.location.href="/o2o/frontend/index";
                    }else {
                        window.location.href="/o2o/shopadmin/shoplist";
                    }
                } else {
                    $.toast("绑定失败！" + data.errMsg);
                    $("#kaptcha_img").click();
                }
            }
        });
    });
});