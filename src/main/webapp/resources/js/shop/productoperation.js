$(function () {
    //从URL中获取productId参数的值
    var productId=getQueryString("productId");
    //通过productId获取商品信息的URL
    var infoUrl = "/o2o/shopadmin/getproductbyid?productId=" + productId;
    //获取当前店铺设定的商品类别列表的URL
    var categoryUrl = "/o2o/shopadmin/getproductcategorylist";
    //更新商品信息的URL
    var productPostUrl = "/o2o/shopadmin/modifyproduct";

    //设置一个标识符用来确定该页面执行的是商品添加还是商品编辑操作
    var isEdit = false;

    if(productId){
        //若productId不为空，则执行的是商品编辑操作
        getInfo(productId);
        isEdit = true;
    }else {
        getCategory();
        productPostUrl = "/o2o/shopadmin/addproduct";
    }

    //获取需要编辑的商品的商品信息，并赋值给表单
    function getInfo(id) {
        $.getJSON(infoUrl,function (data) {
            if (data.success){
                //从返回的JSON中获取product对象的信息，并赋值给表单
                var product = data.product;
                $("#product-name").val(product.productName);
                $("#product-desc").val(product.productDesc);
                $("#priority").val(product.priority);
                $("#point").val(product.point);
                $("#normal-price").val(product.normalPrice);
                $("#promotion-price").val(product.promotionPrice);
                //获取原本的商品类别以及该店铺下所有商品类别列表
                var optionHtml="";
                var optionAttr = data.productCategoryList;
                var optionSelected = product.productCategory.productCategoryId;
                //生成前端的HTML商品类别列表，并默认选择编辑前的商品类别
                optionAttr.map(function (item, index) {
                    var isSelected = optionSelected === item.productCategoryId ? "selected":"";
                    optionHtml+="<option data-value='" + item.productCategoryId + "'" + isSelected +">"+ item.productCategoryName +"</option>";
                });
                $("#category").html(optionHtml);
            }
        });
    }

    //为商品添加操作提供该店铺下的所有商品商品类别列表
    function getCategory() {
        $.getJSON(categoryUrl,function (data) {
            if (data.success){
                var productCategoryList = data.data;
                var optionHtml = "";
                productCategoryList.map(function (item, index) {
                    optionHtml+="<option data-id='" + item.productCategoryId + "'>" + item.productCategoryName + "</option>";
                });
                $("#category").html(optionHtml);
            }
        });
    }

    $(".detail-img-div").on("change",".detail-img:last-child",function () {
        if ($(".detail-img").length < 6){
            $("#detail-img").append("<input type='file' class='detail-img'>");
        }
    });

    //提交按钮的事件响应，分别对商品添加和编辑操作做不同响应
    $("#submit").click(function () {
        //创建商品的JSON对象，并从表单中获取对应的属性值
        var product={};
        product.productName = $("#product-name").val();
        product.productDesc = $("#product-desc").val();
        product.priority = $("#priority").val();
        product.point = $("#point").val();
        product.normalPrice = $("#normal-price").val();
        product.promotionPrice = $("#promotion-price").val();
        //获取选定的商品类别
        product.productCategory ={
            productCategoryId:$("#category").find("option").not(function () {
                return !this.selected;
            }).data("value")
        };
        product.productId = productId;

        //获取缩略图文件流
        var thumbnail = $("#small-img")[0].files[0];
        //生成表单对象用于接收参数并传递给后台
        var formData = new FormData();
        formData.append("thumbnail",thumbnail);
        //遍历商品详情图控件，获取里面的文件流
        $(".detail-img").map(function (index, item) {
            //判断该控件是否已经选择了文件
            if($(".detail-img")[index].files.length>0){
                //将第i个文件流赋值给key为productImgi的表单键值对中
                formData.append("productImg"+index,$(".detail-img")[index].files[0]);
            }
        });
        //将product json对象转换为字符流保存至表单对象key为productStr的键值对中
        formData.append("productStr",JSON.stringify(product));
        //获取表单中输入的验证码
        var verifyCodeActual = $("#j_kaptcha").val();
        if (!verifyCodeActual){
            $.toast("请输入验证码");
            return;
        }
        formData.append("verifyCodeActual",verifyCodeActual);
        //将数据提交给后台处理相关操作
        $.ajax({
            url:productPostUrl,
            type:"POST",
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function (data) {
                if (data.success){
                    $.toast("提交成功");
                    $("#kaptcha_img").click();
                }else {
                    $.toast("提交失败");
                    $("#kaptcha_img").click();
                }
            }
        })
    });

})