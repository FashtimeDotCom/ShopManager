package com.stone.shopmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.fragment.ShopInfoFragment;

public class ShopManagerActivity extends BaseActivity {

    private static final String TAg = "ShopManagerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_shop);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        initFragment();
    }


    private void initFragment() {
        ShopInfoFragment fg = new ShopInfoFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fg_shop_info_container, fg);
        ft.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_shop, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        long id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        //创建店铺
        if(id == R.id.menu_item_shop_add) {
            Intent toCreateShop = new Intent(ShopManagerActivity.this, SaveShopActivity.class);
            toCreateShop.putExtra(SaveShopActivity.KEY_EXTRA_SHOW_TYPE, SaveShopActivity.SHOW_TYPE_CREATE);
            startActivity(toCreateShop);
            return true;
        }

        //修改信息
        if(id == R.id.menu_item_shop_edit) {
            Intent toCreateShop = new Intent(ShopManagerActivity.this, SaveShopActivity.class);
            toCreateShop.putExtra(SaveShopActivity.KEY_EXTRA_SHOW_TYPE, SaveShopActivity.SHOW_TYPE_MODIFY);
            startActivity(toCreateShop);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }



}
