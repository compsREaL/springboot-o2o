package com.real.o2o.entity;

import java.util.Date;

/**
 * 统计某件商品每天的销量
 *
 * @author: mabin
 * @create: 2019/5/4 17:42
 */
public class ProductSellDaily {

    private Long productSellDailyId;

    //哪天的销量，精确到天
    private Date createTime;

    //销量
    private Integer total;

    private Product product;

    private Shop shop;

    public Long getProductSellDailyId() {
        return productSellDailyId;
    }

    public void setProductSellDailyId(Long productSellDailyId) {
        this.productSellDailyId = productSellDailyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
