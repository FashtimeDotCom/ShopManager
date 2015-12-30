package com.stone.shopmanager.model.hbut;

import com.stone.shopmanager.model.shop.Good;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 二手交易实体类
 *
 * @author Stone
 * @date 2014-9-15
 */
@SuppressWarnings("serial")
public class SecondGood extends Good {

    // private String 商品ID, 默认

    private String owner = ""; // 出售者
    private String item = ""; // 商品名称
    private String type = ""; // 类型
    private String description = ""; // 描述
    private String price = ""; // 价格
    private String phone = ""; // 联系电话


    private BmobFile picTradeItem = null; // 物品主图

    // 缩略图
    private BmobRelation picGoods = null;

    public String getOwner() {
        return owner;
    }

    public String getItem() {
        return item;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getPhone() {
        return phone;
    }

    public BmobFile getPicTradeItem() {
        return picTradeItem;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPicTradeItem(BmobFile picTradeItem) {
        this.picTradeItem = picTradeItem;
    }

}
