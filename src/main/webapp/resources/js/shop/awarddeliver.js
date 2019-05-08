$(function () {
    var awardName="";
    var listUrl = "/o2o/shopadmin/listuserawardmapsbyshop?awardName="+awardName;
    getList();
    
    function getList() {
        $.getJSON(listUrl,function (data) {
            if (data.success){
                var userAwardMapList = data.userAwardMapList;
                var tempHtml="";
                userAwardMapList.map(function (item,index) {
                    tempHtml+="<div class='row row-awarddeliver'>"+ "<div class='col-10'>"+ item.awardName +"</div> "
                        +"<div class='col-40'>"+ new Date(item.createTime).Format("yyyy-MM-dd hh:mm:ss") +"</div> "
                        +"<div class='col-20'>"+ item.user.name +"</div> "
                        +"<div class='col-10'>"+ item.point +"</div> "
                        +"<div class='col-20'>"+ item.operator.name +"</div> "+"</div>";
                });

                $(".productbuycheck-wrap").html(tempHtml);
            } 
        });
    }

    $("#search").on("change",function (e) {
        awardName = e.target.value;
        $(".productbuycheck-wrap").empty();
        getList();
    });
});