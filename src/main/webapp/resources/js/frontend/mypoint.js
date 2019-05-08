$(function () {
    var loading = false;
    var maxItems = 20;
    var pageSize = 10;
    var pageNum = 1;
    var shopName = "";
    var listUrl = "/o2o/frontend/listusershopmapsbycustomer";
    addItems(pageSize,pageNum);

    function addItems(pageSize,pageNum) {
        var url = listUrl+"?pageIndex="+pageNum+"&pageSize="+pageSize+"&shopName="+shopName;
        loading = true;
         $.getJSON(url,function (data) {
             if (data.success){
                 maxItems = data.count;
                 var tempHtml = "";
                 data.userShopMapList.map(function (item,value) {
                     tempHtml+= "<div class='card' data-shop-id='"+ item.shop.shopId +"'>"
                         + "<div class='card-header'>"+ item.shop.shopName +"</div> "
                         + "<div class='card-content'>"+ "<div class='list-block media-list'>"
                         + "<ul><li class='item-content'>"+ "<div class='item-inner'>"
                         + "<div class='item-subtitle'>本店积分："+ item.point +"</div> "+"</div> "
                         + "</li></ul>" +"</div> " +"</div> "
                         + "<div class='card-footer'>"+ "<p class='color-gray'>更新时间"
                         + new Date(item.lastEditTime).Format("yyyy-MM-dd")+"</p> " +"</div> " + "</div>";
                 });

                 $(".list-div").append(tempHtml);
                 var total = $(".list-div .card").length;
                 if (total >= maxItems) {
                     //加载完毕，注销无限加载事件，以防止不必要的加载
                     $(".infinite-scroll-preloader").hide();
                 } else {
                     $(".infinite-scroll-preloader").show();
                 }
                 pageNum += 1;
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

    // 绑定搜索事件，这里主要按照产品名称模糊查询
    $("#search").on("change",function (e) {
        shopName = e.target.value;
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