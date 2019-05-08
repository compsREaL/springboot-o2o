$(function () {

    var userName = "";
    var listUrl = "/o2o/shopadmin/listusershopmapsbyshop?pageIndex=1&pageSize=999&userName="+userName;
    getList();

    //查询该店铺下用户及积分
    function getList() {
        $.getJSON(listUrl,function (data) {
            if (data.success){
                var userShopMapList = data.userShopMapList;
                var tempHtml = "";
                userShopMapList.map(function (item,index) {
                    tempHtml += "<div class='row row-usershopcheck'><div class='col-50'>"+ item.user.name +"</div>"
                        +"<div class='col-50'>" + item.point + "</div>"+ "</div>";
                });

                $(".productbuycheck-wrap").html(tempHtml);
            }
        });
    }

    //为搜索绑定事件
    $("#search").on("change",function (e) {
        userName = e.target.value;
        $(".productbuycheck-wrap").empty();
        getList();
    })
});