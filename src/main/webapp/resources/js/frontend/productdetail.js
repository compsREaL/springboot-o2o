$(function () {
    //从地址栏的URL获取productId
    var productId = getQueryString("productId");
    //获取商品信息URL
    var productUrl = "/o2o/frontend/listproductdetailpageinfo?productId="+productId;

    $.getJSON(productUrl,function (data) {
        if (data.success){
            var product = data.product;
            //商品图
            $("#product-img").attr("src",getContextPath() + product.imgAddr);
            //商品更新时间
            $("#product-time").text(new Date(product.lastEditTime).Format("yyyy-MM-dd"));
            //积分
            if (product.point!=undefined){
                $("#product-point").text("购买可得"+product.point+"积分");
            }

            $("#product-name").text(product.productName);
            $("#product-desc").text(product.productDesc);

            //商品价格展示，若原价和现价都为空，则不做展示
            if (product.normalPrice != null && product.promotionPrice!=null){
                $("#price").show();
                $("#normalPrice").html("<del>"+"￥"+product.normalPrice+"</del>>");
                $("#promotionPrice").text("￥"+product.promotionPrice);
            } else if (product.normalPrice!=undefined && product.promotionPrice == undefined){
                $("#price").show();
                $("#promotionPrice").text("￥"+product.normalPrice);
            } else if (product.promotionPrice!=undefined && product.normalPrice == undefined){
                $("#promotionPrice").text("￥"+product.promotionPrice);
            }

            var imgListHtml="";
            product.productImgList.map(function (item, index) {
                imgListHtml+="<div><img src='"+ getContextPath() + item.imgAddr +"' width='100%' /></div>"
            });

            if (data.needQRCode) {
                //若顾客已登录，则生成购买商品的二维码供商家扫描
                imgListHtml+= "<div><img src='/o2o/shopadmin/generateqrcode4product?productId="+ productId +"' width='100%'/></div>"
            }
            $("#imgList").html(imgListHtml);
        }
    });

    $("#me").click(function () {
        $.openPanel("#panel-right-demo");
    });

    $.init();
});