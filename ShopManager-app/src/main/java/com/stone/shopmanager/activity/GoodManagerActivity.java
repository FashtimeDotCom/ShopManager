package com.stone.shopmanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.adapter.GoodsListAdapter;
import com.stone.shopmanager.manager.ShopManager;
import com.stone.shopmanager.model.shop.Good;
import com.stone.shopmanager.util.ToastUtils;
import com.stone.shopmanager.util.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class GoodManagerActivity extends BaseActivity implements OnItemClickListener, AdapterView.OnItemLongClickListener{

    private static final String TAG = "GoodManagerActivity";

    private ViewGroup vgEmptyBg;
    private ListView lvGoodsList;
    private List<Good> goodsList;
    private GoodsListAdapter goodsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_good);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        // 初始化商品页面以及适配数据
        initGoodsData();
    }

    private void initView() {

        initEmptyView();

        lvGoodsList = (ListView) findViewById(R.id.lv_m_goods);
        goodsList = new ArrayList<>();
        goodsListAdapter = new GoodsListAdapter(this);
        lvGoodsList.setAdapter(goodsListAdapter);
        lvGoodsList.setOnItemClickListener(this);
        lvGoodsList.setOnItemLongClickListener(this);
    }

    private void initEmptyView() {
        vgEmptyBg = (ViewGroup) findViewById(R.id.rl_good_list_empty);
        Button btnAddGood = (Button) findViewById(R.id.btn_good_add);
        btnAddGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goGoodManager(SaveGoodActivity.SHOW_TYPE_CREATE);
            }
        });
    }

    private void showEmptyView(Boolean isShow) {
        if (isShow) {
            lvGoodsList.setVisibility(View.GONE);
            vgEmptyBg.setVisibility(View.VISIBLE);
        } else {
            lvGoodsList.setVisibility(View.VISIBLE);
            vgEmptyBg.setVisibility(View.GONE);
        }
    }

    /**
     * 获取某一商店的所有商品
     *
     * @date 2014-5-1
     * @autor Stone
     */
    public void initGoodsData() {

        if (!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络连接后重试");
            return;
        }

        showProgressDialog();
        BmobQuery<Good> query = new BmobQuery<>();
        query.addWhereEqualTo("shop", ShopManager.getInstance().getSelectedShop());
        // 限制最多15个结果
        query.setLimit(15);
        query.order("state,-saleVolume,-createdAt");
        query.findObjects(this, new FindListener<Good>() {
            @Override
            public void onSuccess(List<Good> goods) {
                dismissProgressDialog();
                if (goods.size() == 0) {
                    ToastUtils.showToast("亲, 该店还没有添加商品哦");
                }
                ToastUtils.showToast(String.format("查询到商品 %d条", goods.size()));
                goodsList = goods;
                goodsListAdapter.refresh(goodsList);
                showEmptyView(goods.size() == 0);
            }

            @Override
            public void onError(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast("查询失败");
            }
        });

    }

    private void deleteGood(Object good) {
        if(good == null) {
            ToastUtils.showToast("删除失败");
            return;
        }

        if(!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络连接后重试");
            return;
        }

        showProgressDialog();
        Good delGood = (Good) good;
        delGood.delete(this, new DeleteListener() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                ToastUtils.showToast("商品删除成功");
                initGoodsData();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showToast(String.format("商品删除失败 [%d : %s]", i, s));
            }
        });
    }

    private void goGoodManager(int showType) {
        Intent intent = new Intent(GoodManagerActivity.this, SaveGoodActivity.class);
        intent.putExtra(SaveGoodActivity.KEY_EXTRA_SHOW_TYPE, showType);
        startActivityForResult(intent, REQUEST_CODE_GOOD_ADD);
    }

    private void goGoodManager(int showType, Good good) {
        if (!(showType == SaveGoodActivity.SHOW_TYPE_MODIFY) || good == null)
            return;

        Intent intent = new Intent(GoodManagerActivity.this, SaveGoodActivity.class);
        intent.putExtra(SaveGoodActivity.KEY_EXTRA_SHOW_TYPE, showType);
        intent.putExtra(SaveGoodActivity.KEY_EXTRA_GOOD, good);
        startActivityForResult(intent, REQUEST_CODE_GOOD_ADD);
    }

    private void showDeleteDialog(final Object object) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除商品").setMessage("商品删除后将不可恢复，确定要删除？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGood(object);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialogTitleLineColor(dialog);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        goGoodManager(SaveGoodActivity.SHOW_TYPE_MODIFY, goodsList.get(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showDeleteDialog(parent.getAdapter().getItem(position));
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_good, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private static final int REQUEST_CODE_GOOD_ADD = 0x001;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.action_add) {
            goGoodManager(SaveGoodActivity.SHOW_TYPE_CREATE);
            return true;
        }

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GOOD_ADD && resultCode == RESULT_OK) {
            initGoodsData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
