package com.real.o2o.entity;

import java.util.Date;

/**
 * 顾客消费的商品映射
 *
 * @author: mabin
 * @create: 2019/5/4 17:22
 */
public class UserProductMap {

    //主键id
    private Long userProductId;

    //创建时间
    private Date createTime;

    //消费商品所得积分
    private Integer point;

    //顾客信息
    private PersonInfo user;

    //商品信息
    private Product product;

    //店铺信息
    private Shop shop;

    //操作员信息
    private PersonInfo operator;

    public Long getUserProductId() {
        return userProductId;
    }

    public void setUserProductId(Long userProductId) {
        this.userProductId = userProductId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public PersonInfo getUser() {
        return user;
    }

    public void setUser(PersonInfo user) {
        this.user = user;
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

    public PersonInfo getOperator() {
        return operator;
    }

    public void setOperator(PersonInfo operator) {
        this.operator = operator;
    }
}
