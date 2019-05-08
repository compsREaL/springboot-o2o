$(function () {
    //从地址栏中获取userAwardId
    var userAwardId = getQueryString("userAwardId");
    //根据userAwardId获取用户奖品映射信息
    var awardUrl = "/o2o/frontend/getawardbyuserawardid?userAwardId="+userAwardId;

    $.getJSON(awardUrl,function (data) {
        if (data.success){
            var award = data.award;
            $("#award-name").text(award.awardName);
            $("#award-desc").text(award.awardDesc);
            $("#award-time").text(new Date(data.userAwardMap.createTime).Format("yyyy-MM-dd"));
            $("#award-img").attr("src",getContextPath()+award.awardImg);
            var imgListHtml="";
            //若未去实体店兑换实体奖品，生成兑换奖品的二维码供店家扫描
            if (data.usedStatus == 0){
                imgListHtml+="<div><img src='/o2o/frontend/generateqrcode4Award?userAwardId=" + userAwardId + "' width='100%'/></div>"
            }
            $("#imgList").html(imgListHtml);
        }
    });

    //打开侧边栏
    $("#me").click(function () {
        $.openPanel("#panel-right-demo");
    });

    $.init();
});