package com.stone.shopmanager.manager;

import com.stone.shopmanager.model.shop.Shop;

public class ShopManager {

    private static final String TAG = "ShopManager";

    private static Object lock = new Object();
    private static ShopManager shopManager;

    // 当前选择的店铺
    private Shop shop;

    private ShopManager() {
    }

    public static ShopManager getInstance() {
        if (null == shopManager) {
            synchronized (lock) {
                if (null == shopManager) {
                    shopManager = new ShopManager();
                }
            }
        }

        return shopManager;
    }

    public void setSelectedShop(Shop shop) {
        if (null != shop) {
            this.shop = shop;
        }
    }

    public Shop getSelectedShop() {
        return this.shop;
    }


}
