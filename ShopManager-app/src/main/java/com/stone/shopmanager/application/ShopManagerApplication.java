package com.stone.shopmanager.application;

public class ShopManagerApplication extends BaseApplication {

    private static ShopManagerApplication instance;

    public static ShopManagerApplication getInstance() {
        if (instance != null)
            return instance;
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onTerminate() {

        super.onTerminate();
        // 整体摧毁的时候调用这个方法
    }

}
