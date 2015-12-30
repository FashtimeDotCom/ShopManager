package com.stone.shopmanager.model.helper;

import com.stone.shopmanager.model.shop.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stonekity.shi on 2015/4/4.
 */
public class ShopInfo {

    //信息
    public static final String SHOP_INFO_NAME = "店铺";
    public static final String SHOP_INFO_RATE = "评分";
    public static final String SHOP_INFO_TYPE = "店铺类型";
    public static final String SHOP_INFO_STATE = "店铺状态";
    public static final String SHOP_INFO_SCOPE = "经营范围";

    //数据统计
    public static final String SHOP_INFO_GOOD_COUNT_ALL = "商品总数";
    public static final String SHOP_INFO_GOOD_COUNT_SALE = "在售商品";
    public static final String SHOP_INFO_ORDER_COUNT = "历史交易订单";

    //时间戳
    public static final String SHOP_INFO_TIME_CREATAT = "创建日期";
    public static final String SHOP_INFO_TIME_VIP = "认证日期";

    public static List<String> getShopInfoList(Shop shop) {
        if(shop==null)
            return null;
        List<String> shopInfoList = new ArrayList<String>();
        String format = "%1s   %2s";
        shopInfoList.add(String.format(format, SHOP_INFO_NAME, shop.getName()));
        shopInfoList.add(String.format(format, SHOP_INFO_RATE, shop.getRates()+""));
        shopInfoList.add(String.format(format, SHOP_INFO_TYPE, shop.getType()));
        shopInfoList.add(String.format(format, SHOP_INFO_STATE, shop.getState()));
        shopInfoList.add(String.format(format, SHOP_INFO_SCOPE, shop.getScrope()));
        shopInfoList.add(String.format(format, SHOP_INFO_GOOD_COUNT_ALL, ""));
        shopInfoList.add(String.format(format, SHOP_INFO_GOOD_COUNT_SALE, ""));
        shopInfoList.add(String.format(format, SHOP_INFO_ORDER_COUNT, ""));
        shopInfoList.add(String.format(format, SHOP_INFO_TIME_CREATAT, shop.getCreatedAt()));
        shopInfoList.add(String.format(format, SHOP_INFO_TIME_VIP, ""));

        return shopInfoList;
    }

}
