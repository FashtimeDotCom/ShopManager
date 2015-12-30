package com.stone.shopmanager.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.GoodManagerActivity;
import com.stone.shopmanager.activity.OrderManagerActivity;
import com.stone.shopmanager.adapter.ShopInfoAdapter;
import com.stone.shopmanager.manager.ShopManager;
import com.stone.shopmanager.model.shop.Shop;

/**
 * Created by stone on 15/4/2.
 */
public class ShopInfoFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ShopInfoFragment";

    private ListView lvShopInfo;
    private ShopInfoAdapter mShopInfoAdapter;

    private HeaderFooterHodler holder;

    @Override
    protected int provideLayoutResId() {
        return R.layout.fg_shop_info;
    }

    @Override
    protected void initView(View rootView) {
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData(View rootView, Bundle savedInstanceState) {
        lvShopInfo = (ListView) rootView.findViewById(R.id.lv_shop_info);
        initHeaderFooter();
        mShopInfoAdapter = new ShopInfoAdapter(getActivity());
        Shop showShop = ShopManager.getInstance().getSelectedShop();
        mShopInfoAdapter.setmShop(showShop);
        lvShopInfo.setAdapter(mShopInfoAdapter);
    }


    private void initHeaderFooter() {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_shop_info, null);
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_shop_info, null);
        holder = new HeaderFooterHodler();
        holder.imgShopInfo = (ImageView) header.findViewById(R.id.img_shop_info);
        holder.btnMGood = (Button) footer.findViewById(R.id.btn_m_good);
        holder.btnMOrder = (Button) footer.findViewById(R.id.btn_m_order);
        holder.btnMGood.setOnClickListener(this);
        holder.btnMOrder.setOnClickListener(this);
        lvShopInfo.addHeaderView(header);
        lvShopInfo.addFooterView(footer);

        Shop showShop = ShopManager.getInstance().getSelectedShop();
        AQuery aq = new AQuery(getActivity());
        if (showShop != null) {
            if (null != showShop.getPicShop() && !showShop.getPicShop().getUrl().isEmpty()) {
                aq.id(R.id.img_shop_info).image(showShop.getPicShop().getFileUrl(getActivity()), true, true);
            } else {
                aq.id(R.id.img_shop_info).image(R.drawable.ic_shop_defalut);
            }
        }
    }

    private void goGoodManger() {
        Intent intent = new Intent(getActivity(), GoodManagerActivity.class);
        startActivity(intent);
    }

    private void goOrderManager() {
        Intent intent = new Intent(getActivity(), OrderManagerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        long id = v.getId();
        if (id == R.id.btn_m_good) {
            goGoodManger();
        } else if (id == R.id.btn_m_order) {
            goOrderManager();
        }
    }

    /**
     * 首尾布局
     */
    class HeaderFooterHodler {
        ImageView imgShopInfo;
        Button btnMGood;
        Button btnMOrder;
    }


}
