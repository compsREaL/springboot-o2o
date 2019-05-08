$(function () {
    var loading = false;
    var maxItems = 999;
    var pageSize = 10;
    //获取奖品列表的URL
    var listUrl = "/o2o/frontend/listawardsbyshop";
    //兑换奖品的URL
    var changeUrl = "/o2o/frontend/adduserawardmap";
    var pageNum = 1;
    //从地址栏URL获取shopId
    var shopId = getQueryString("shopId");
    var awardName = "";
    var canProceed = false;
    var totalPoint = 0;
    //预先加载20条
    addItems(pageSize,pageNum);
    //按照查询条件获取奖品列表，并生成对应的HTML元素添加到页面中
    function addItems(pageSize,pageIndex) {
        //生成新条目的URL
        listUrl = listUrl+"?pageIndex="+pageNum+"&pageSize="+pageSize+"&shopId="+shopId+"&awardName="+awardName;
        loading = true;
        $.getJSON(listUrl,function (data) {
            if (data.success){
                maxItems = data.count;
                var awardList = data.awardList;
                var tempHtml = "";
                awardList.map(function (item,index) {
                    tempHtml+="<div class='card' data-award-id='" + item.awardId + "' data-point='" + item.point + "'>"
                        + "<div class='card-header'>" + item.awardName + "<span class='pull-right'>需要积分" + item.point + "</span></div> "
                        + "<div class='card-content'><div class='list-block media-list'>" + "<ul><li class='item-content'>"
                        + "<div class='item-media'>" + "<img src='"+ getContextPath() + item.awardImg +"' width='44'></div>"
                        + "<div class='item-inner'>" + "<div class='item-subtitle'>" + item.awardDesc + "</div>" + "</div>" + "</li></ul>"
                        + "</div></div>" + "<div class='card-footer'>" + "<p class='color-gray'>"
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")+ "更新</p>";
                    if(data.totalPoint != undefined){
                        //若用户有积分，则显示领取按钮
                        tempHtml+= "<span>点击领取</span></div></div>";
                    } else{
                        tempHtml+= "</div></div>";
                    }
                });
                $(".list-div").append(tempHtml);
                if (data.point!=undefined){
                    //若用户在该店铺有积分，则显示
                    canProceed = true;
                    $("#title").text("当前积分"+data.totalPoint);
                    totalPoint = data.totalPoint;
                }
                var total = $(".list-div .card").length;
                if (total>=maxItems){
                    //加载完毕，注销无限加载事件
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

    $(".award-list").on("click",".card",function (e) {
        //若用户在店铺有积分且积分大于该奖品需要消耗的积分
        if (canProceed && totalPoint>e.currentTarget.dataset.point){
            //弹出操作确认框
            $.confirm("需要消耗"+e.currentTarget.dataset.point+"积分，确定操作吗？",function () {
                $.ajax({
                    url:changeUrl,
                    type:"POST",
                    data:{
                        awardId:e.currentTarget.dataset.awardId
                    },
                    dataType:"json",
                    success:function (data) {
                        if (data.success){
                            $.toast("操作成功");
                            totalPoint = totalPoint-e.currentTarget.dataset.point;
                            $("#title").text("当前积分"+totalPoint);
                        }else {
                            $.toast("操作失败");
                        }
                    }
                });
            });
        } else {
            $.toast("积分不足或无权限操作");
        }
    });

    $("#search").click(function (e) {
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