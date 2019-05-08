$(function () {

    var loading = false;
    //分页允许返回的最大条数，超过此数则禁止访问后台
    var maxItems = 20;
    //一页返回的最大条数
    var pageSize = 10;
    //获取商品列表的Url
    var listUrl = "/o2o/frontend/listproductsbyshop";
    //页码
    var pageNum = 1;
    //从地址栏URL里尝试获取shopId
    var shopId = getQueryString("shopId");
    var productCategoryId = "";
    var productName = "";
    //获取本店铺信息以及商品类别信息列表URL
    var searchDivUrl = "/o2o/frontend/listshopdetailpageinfo?shopId="+shopId;
    //渲染出店铺信息以及商品类别列表以供搜索
    getSearchDivData();
    //预先加载10条商品信息
    addItems(pageSize,pageNum);

    //给兑换礼品的a标签赋值兑换奖品的URL
    $("#exchangelist").attr("href","/o2o/frontend/awardlist?shopId="+shopId);

    //获取本店铺信息以及商品类别信息列表
    function getSearchDivData() {
        var url = searchDivUrl;
        $.getJSON(url,function (data) {
            if (data.success){
                var shop = data.shop;
                $("#shop-cover-pic").attr("src",shop.shopImg);
                $("#shop-update-time").html(new Date(shop.lastEditTime).Format("yyyy-MM-dd"));
                $("#shop-name").html(shop.shopName);
                $("#shop-desc").html(shop.shopDesc);
                $("#shop-addr").html(shop.shopAddr);
                $("#shop-phone").html(shop.phone);
                //获取该店铺的商品类别列表
                var productCategoryList = data.productCategoryList;
                var html="";
                productCategoryList.map(function (item, index) {
                    html += "<a href='#' class='button' data-product-search-id='"+ item.productCategoryId +"'>"+
                        item.productCategoryName + "</a>"
                });
                $("#shopdetail-button-div").html(html);
            }
        });
    }

    /**
     * 获取分页展示的商品信息列表
     * @param pageSize
     * @param pageIndex
     */
    function addItems(pageSize,pageIndex) {
        //拼接出查询商品的URL
        var url = listUrl+"?pageSize="+pageSize+"&pageIndex="+pageIndex+"&shopId="+shopId
            +"&productCategoryId="+productCategoryId +"&productName="+productName;
        //设定加载符
        loading = true;
        $.getJSON(url,function (data) {
            if (data.success){
                //获取查询到的数目
                maxItems = data.count;
                var html="";
                data.productList.map(function (item, index) {
                    html += "<div class='card' data-product-id='"+ item.productId +"'>"
                        +"<div class='card-header'>"+ item.productName +"</div>"
                        +"<div class='card-content'>"
                        +"<div class='list-block media-list'>"
                        +"<ul>"+"<li class='item-content'>"
                        +"<div class='item-media'>"
                        +"<img src='"+ getContextPath() + item.productImg +"' width='44'>"+"</div>"
                        +"<div class='item-inner'>"
                        +"<div class='item-subtitle'>"+ item.productDesc +"</div> "+"</div> " +"</li> "+"</ul>"+"</div>"+"</div> "
                        +"<div class='card-footer'>"
                        +"<p class='color-gray'>"+ new Date(item.lastEditTime).Format("yyyy-MM-dd")+"更新</p> "
                        +"<span>点击查看</span>"+"</div> "+"</div>";
                });
                $(".list-div").append(html);
                //获取已经加载的卡片的数量
                var total = $(".list-div .card").length;
                if (total>=maxItems){
                    //隐藏加载提示符
                    $(".infinite-scroll-preloader").hide();
                }else {
                    $(".infinite-scroll-preloader").show();
                }
                //否则页码加1
                pageNum = pageNum+1;
                //本次加载结束
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

    //选择新的商品类别之后，重置页码，清空原先的商品列表，按照新的类别去查询
    $("#shopdetail-button-div").on("click",".button",function (e) {
        //获取商品类别Id
        var productCategoryId = e.target.dataset.productSearchId;
        if(productCategoryId){
            //如果之前选择了其他category按钮，则移除其选定效果
            if($(e.target).hasClass("button-fill")){
                $(e.target).removeClass("button-fill");
                productCategoryId="";
            }else {
                $(e.target).addClass("button-fill").siblings().removeClass("button-fill");
            }
            $(".list-div").empty();
            pageNum=1;
            addItems(pageSize,pageNum);
        }
    });

    //点击商品卡片进入商品详情页
    $(".list-div").on("click",".card",function (e) {
        var productId = e.target.dataset.productId;
        window.location.href="/o2o/frontend/productdetail?productId="+productId;
    });

    //需要查询的商品名字发生变化后，重置页码，清空原先的商品列表，按照新的名字去查询
    $("#search").on("change",function (e) {
        productName = e.target.value;
        $(".list-div").empty();
        pageNum = 1;
        addItems(pageSize,pageNum);
    });
    //打开侧边栏
    $("#me").click(function () {
        $.openPanel("#panel-right-demo");
    });
    $.init();
})