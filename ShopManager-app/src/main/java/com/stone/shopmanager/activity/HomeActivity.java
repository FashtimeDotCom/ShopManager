package com.stone.shopmanager.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.fragment.ShopListFragment;

/**
 * Created by stone on 15/6/3.
 */
public class HomeActivity extends BaseActivity{

    private static final String TAG = "HomeActivity";

    private ShopListFragment mShopListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    private void initView() {

        mShopListFragment = new ShopListFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fg_show_list, mShopListFragment);
        ft.commit();
    }

}
