$(function () {
    var changePwdUrl = "/o2o/local/changelocalpwd";
    var usertype = getQueryString("usertype");

    $("#submit").click(function () {
        var userName = $("#username").val();
        var password = $("#password").val();
        var newPassword = $("#newPassword").val();
        var confirmPassword = $("#confirmPassword").val();
        var verifyCodeActual = $("#j_kaptcha").val();
        if (newPassword!=confirmPassword){
            $.toast("新密码与确认密码输入不一致");
            return;
        }

        if (!verifyCodeActual){
            $.toast("请输入验证码");
            return;
        }

        $.ajax({
            url:changePwdUrl,
            type:"POST",
            async:false,
            cache:false,
            dataType:"json",
            data:{
                userName:userName,
                password:password,
                newPassword:newPassword,
                verifyCodeActual:verifyCodeActual
            },
            success:function (data) {
                if(data.success){
                    $.toast("密码修改成功");
                    if (usertype == 1){
                        window.location.href = "/o2o/frontend/index";
                    } else {
                        window.location.href = "/o2o/shopadmin/shoplist";
                    }
                } else {
                    $.toast("密码修改失败:"+data.errMsg);
                    $("#kaptcha_img").click();
                }
            }
        });

    });

    $("#back").click(function () {
        window.location.href = "/o2o/shopadmin/shoplist";
    });
});