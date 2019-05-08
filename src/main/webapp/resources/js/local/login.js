$(function () {
    //登录验证的url
    var loginUrl = "/o2o/local/logincheck";
    //从地址栏获取usertype
    var usertype = getQueryString("usertype");
    //登录次数，失败超过三次需要输入验证码
    var loginCount = 0;

    $("#submit").click(function () {
        var userName = $("#username").val();
        var password = $("#pwd").val();
        var verifyCodeActual = $("#j_kaptcha").val();
        //是否需要输入验证码
        var needVerify = false;
        //如果登录失败次数大于3次,需要输入验证码验证
        if (loginCount >=3 ){
            if (!verifyCodeActual){
                $.toast("请输入验证码");
                return;
            } else {
                needVerify = true;
            }
        }

        $.ajax({
            url:loginUrl,
            type:"POST",
            async:false,
            cache:false,
            dataType:"json",
            data:{
                userName:userName,
                password:password,
                verifyCodeActual:verifyCodeActual,
                needVerify:needVerify
            },
            success:function (data) {
                if (data.success){
                    $.toast("登录成功");
                    if (usertype==1) {
                        window.location.href = "/o2o/frontend/index";
                    } else {
                        window.location.href = "/o2o/shopadmin/shoplist";
                    }
                } else {
                    $.toast("登录失败:"+data.errMsg);
                    loginCount++;
                    if (loginCount >=3 ){
                        $("#verifyPart").show();
                    }
                    $("#kaptcha_img").click();
                }
            }
        });
    });
});