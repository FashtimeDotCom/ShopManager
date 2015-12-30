package com.stone.shopmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.manager.ShopManager;
import com.stone.shopmanager.model.shop.Shop;
import com.stone.shopmanager.model.shop.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class OldHomeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "OldHomeActivity";

    private Button btnShopManager;
    private Button btnGoodsManager;
    private Button btnOrdersManager;
    private Button btnAboutApp;
    private Button btnNewsManager;

    private List<Shop> myShopList;
    private Observer observer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_old);

        initView();

        initData();

        registerObserver();
    }


    private void initView() {
        btnShopManager = (Button) findViewById(R.id.btn_m_shop);
        btnGoodsManager = (Button) findViewById(R.id.btn_m_good);
        btnOrdersManager = (Button) findViewById(R.id.btn_m_order);
        btnAboutApp = (Button) findViewById(R.id.btn_m_about);
        btnNewsManager = (Button) findViewById(R.id.btn_m_news);
        btnShopManager.setOnClickListener(this);
        btnGoodsManager.setOnClickListener(this);
        btnOrdersManager.setOnClickListener(this);
        btnAboutApp.setOnClickListener(this);
        btnNewsManager.setOnClickListener(this);
    }


    private void registerObserver() {
        if (null == observer) {
            observer = new Observer() {
                @Override
                public void update(Observable observable, Object data) {
                    initData();
                }
            };
        }
    }


    /**
     * 加载店铺数据
     */
    public void initData() {
        myShopList = new ArrayList<Shop>();
        BmobQuery<Shop> query = new BmobQuery<Shop>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(this, User.class));
        query.findObjects(this, new FindListener<Shop>() {

            @Override
            public void onSuccess(List<Shop> shopList) {
                myShopList = shopList;
                if (shopList.size() > 0) {

                    //获得当前店铺的实例
                    Shop showShop = shopList.get(0);
                    ShopManager.getInstance().setSelectedShop(showShop);

                }

            }

            @Override
            public void onError(int i, String arg0) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //店铺管理
            case R.id.btn_m_shop:
                Intent toShopManagerActivity = new Intent(OldHomeActivity.this, ShopManagerActivity.class);
                startActivity(toShopManagerActivity);
                break;
            //商品管理
            case R.id.btn_m_good:
                Intent toGoodsManagerActivity = new Intent(OldHomeActivity.this, GoodManagerActivity.class);
                startActivity(toGoodsManagerActivity);
                break;
            //订单管理
            case R.id.btn_m_order:
                Intent toOrdersManagerActivity = new Intent(OldHomeActivity.this, OrderManagerActivity.class);
                startActivity(toOrdersManagerActivity);
                break;
            //信息发布
            case R.id.btn_m_news:
                Intent toNewsManagerActivity = new Intent(OldHomeActivity.this, NewsManagerActivity.class);
                startActivity(toNewsManagerActivity);
                break;
            //软件相关
            case R.id.btn_m_about:
                Intent toAboutActivity = new Intent(OldHomeActivity.this, AboutActivity.class);
                startActivity(toAboutActivity);
            default:
                break;
        }
    }


}
