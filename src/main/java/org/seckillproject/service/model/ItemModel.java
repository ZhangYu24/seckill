package org.seckillproject.service.model;

import java.math.BigDecimal;

/**
 * @author:ZhangYu
 * @date:2021/09/15 23:19
 */
public class ItemModel {
    
    private Integer id; //商品ID
    private String title; //商品名称
    private BigDecimal price; //商品价格
    private Integer stock; //商品库存
    private String description; //商品描述
    private Integer sales; //商品销量
    private String imgUrl; //商品图片url

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
