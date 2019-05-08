$(function () {
    var loading = false;
    var maxItems = 20;
    var pageSize = 10;
    var listUrl = "/o2o/frontend/listuserawardmapbycustomer";
    var pageNum = 1;
    var awardName = "";
    addItems(pageSize,pageNum);

    function addItems(pageSize,pageIndex) {
        var url = listUrl + "?pageIndex="+pageIndex+"&pageSize="+pageSize+"&awardName="+awardName;
        loading = true;
        $.getJSON(url,function (data) {
            if(data.success){
                //获取总数
                maxItems = data.count;
                var tempHtml = "";
                data.userAwardMapList.map(function (item,index) {
                    var status="";
                    if (item.usedStatus == 0){
                        status = "未领取";
                    } else if(item.usedStatus == 1){
                        status = "已领取";
                    }
                    tempHtml+= "<div class='card' data-user-award-id='" + item.userAwardId + "'>"
                        + "<div class='card-header'>" + item.shop.shopName + "<span class='pull-right'>"
                        + status + "</span></div>" + "<div class='card-content'>" + "<div class='list-block media-list'>"
                        + "<ul>"+ "<li class='item-content'>" + "<div class='item-inner'>" + "<div class='item-subtitle'>"
                        + item.award.awardName +"</div> " + "</div> " + "</li>" +"</ul>" + "</div> " + "</div> "
                        + "<div class='card-footer'>" + "<p class='color-gray'>" + new Date(item.createTime).Format("yyyy-MM-dd")
                        + "</p>" + "<span>消耗积分:" + item.point + "</span>" + "</div> " + "</div>";
                });

                $(".list-div").append(tempHtml);
                var total = $(".list-div .card").length;
                if (total>=maxItems){
                    //加载完毕，注销无限加载事件，以防止不必要的加载
                    $(".infinite-scroll-preloader").hide();
                } else {
                    $(".infinite-scroll-preloader").show();
                }
                pageNum+=1;
                loading = false;
                $.refreshScroller();
            }
        });
    }

    //下滑屏幕自动进行分页加载
    $(document).on("infinite",".infinite-scroll-bottom",function () {
        if (loading) return;
        addItems(pageSize,pageNum);
    });
    
    $(".list-div").on("click",".card",function (e) {
        var userAwardId = e.currentTarget.dataset.userAwardId;
        window.location.href = "/o2o/frontend/myawarddetail?userAwardId="+userAwardId;
    });

    $("#search").on("change",function (e) {
        awardName = e.target.value;
        $(".list-div").empty();
        pageNum = 1;
        addItems(pageSize,pageNum);
    });

    //打开侧边栏
    $("#me").click(function () {
        $.openPanel("#panel-right-demo");
    });

    $.init();
});