package com.stone.shopmanager.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.adapter.OrderListAdapter;
import com.stone.shopmanager.manager.ShopManager;
import com.stone.shopmanager.model.shop.Order;
import com.stone.shopmanager.model.shop.Shop;
import com.stone.shopmanager.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class OrderManagerActivity extends BaseActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "OrderManagerActivity";

    private ListView lvOrderList;
    private OrderListAdapter mOrderListAdapter;
    private List<Order> mOrderList = new ArrayList<Order>();

    private Spinner spOrderSelector;
    private ArrayAdapter<CharSequence> mSpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        initData();
    }

    private void initView() {
        lvOrderList = (ListView) findViewById(R.id.lv_order);
        mOrderListAdapter = new OrderListAdapter(this, mOrderList);
        lvOrderList.setAdapter(mOrderListAdapter);

        spOrderSelector = (Spinner) findViewById(R.id.sp_select_order);
        mSpAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_order_select, android.R.layout.simple_spinner_item);
        mSpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrderSelector.setAdapter(mSpAdapter);
    }

    private void initData() {
        Shop shop = ShopManager.getInstance().getSelectedShop();
        if (shop == null)
            return;

        showProgressDialog();
        BmobQuery<Order> query = new BmobQuery<Order>();
        query.addWhereEqualTo("shop", shop);
        query.include("good.shop, user");
        query.order("state, -createdAt");
        query.findObjects(this, new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> orders) {
                dismissProgressDialog();
                ToastUtils.showToast("查到订单 " + orders.size() + " 条");
                mOrderList = orders;
                mOrderListAdapter.refresh(mOrderList);
            }

            @Override
            public void onError(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast("查询订单失败");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();
        if(id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
