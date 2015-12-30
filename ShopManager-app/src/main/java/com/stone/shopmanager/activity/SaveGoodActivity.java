package com.stone.shopmanager.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.manager.ShopManager;
import com.stone.shopmanager.model.shop.Good;
import com.stone.shopmanager.model.shop.Shop;
import com.stone.shopmanager.util.ToastUtils;
import com.stone.shopmanager.util.Utils;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SaveGoodActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "SaveGoodActivity";

    public static final String KEY_EXTRA_SHOW_TYPE = "com.stone.shopmanager.KEY_EXTRA_SHOW_TYPE";
    public static final String KEY_EXTRA_GOOD = "com.stone.shopmanager.KEY_EXTRA_GOOD";

    // 添加商品
    public static final int SHOW_TYPE_CREATE = 0;

    // 修改商品信息
    public static final int SHOW_TYPE_MODIFY = 1;

    private EditText etGoodName;
    private EditText etGoodPrice;
    private EditText etGoodPriceD;
    private EditText etGoodStock;
    private Spinner spGoodClass;
    private EditText etGoodInfo;
    private ArrayAdapter<CharSequence> adapter;

    private int type = SHOW_TYPE_CREATE;

    private Good good;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initShowType();
        initView();
        initData();
    }

    /**
     * 获取显示类型（新建｜修改）
     */
    private void initShowType() {
        if (getIntent().hasExtra(KEY_EXTRA_SHOW_TYPE)) {
            type = getIntent().getIntExtra(KEY_EXTRA_SHOW_TYPE, SHOW_TYPE_CREATE);
            if (type == SHOW_TYPE_MODIFY) {
                good = (Good) getIntent().getSerializableExtra(KEY_EXTRA_GOOD);
                getSupportActionBar().setTitle(R.string.title_activity_update_good);
            } else if (type == SHOW_TYPE_CREATE) {
                getSupportActionBar().setTitle(R.string.title_activity_save_good);
            }

        }
    }

    public void initView() {
        etGoodName = (EditText) findViewById(R.id.et_good_name);
        etGoodPrice = (EditText) findViewById(R.id.et_good_price);
        etGoodPriceD = (EditText) findViewById(R.id.et_good_price_d);
        etGoodStock = (EditText) findViewById(R.id.et_good_stock);
        etGoodInfo = (EditText) findViewById(R.id.et_good_info);
        spGoodClass = (Spinner) findViewById(R.id.sp_good_class);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.array_good_class, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGoodClass.setAdapter(adapter);
        spGoodClass.setOnItemSelectedListener(onItemSelectedListener);
    }

    private void initData() {

        if (type == SHOW_TYPE_MODIFY && good != null) {

            etGoodName.setText(good.getName());
            etGoodPrice.setText(good.getPrice());
            etGoodPriceD.setText(good.getdPrice());
            etGoodStock.setText(good.getStock().toString());
            etGoodInfo.setText(good.getDescription());
            spGoodClass.setSelection(adapter.getPosition(good.getCategory()));
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    private void updateGood() {
        if (!checkInput() || good == null)
            return;

        if (!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络后重试");
            return;
        }

        showProgressDialog();
        good.setName(etGoodName.getText().toString());
        good.setPrice(etGoodPrice.getText().toString());
        good.setdPrice(etGoodPriceD.getText().toString());
        good.setStock(Integer.parseInt(etGoodStock.getText().toString()));
        good.setCategory(spGoodClass.getSelectedItem().toString());
        good.setDescription(etGoodInfo.getText().toString());
        good.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                ToastUtils.showToast("商品信息更新成功");
            }

            @Override
            public void onFailure(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast("商品信息更新失败");
            }
        });
    }


    /**
     * 添加商品
     */
    private void saveGood() {

        if (!checkInput())
            return;

        if (!Utils.isNetworkAvailable(this)) {
            ToastUtils.showToast("网络异常，请检查网络后重试");
            return;
        }

        showProgressDialog();
        final Good good = new Good();
        good.setName(etGoodName.getText().toString());
        good.setPrice(etGoodPrice.getText().toString());
        good.setdPrice(etGoodPriceD.getText().toString());
        good.setStock(Integer.parseInt(etGoodStock.getText().toString()));
        good.setCategory(spGoodClass.getSelectedItem().toString());
        good.setDescription(etGoodInfo.getText().toString());
        good.setShop(ShopManager.getInstance().getSelectedShop());
        good.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                addGoodToShop(good);
            }

            @Override
            public void onFailure(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast("添加失败");
                finish();
            }
        });
    }


    /**
     * 商品店铺关联关系
     *
     * @param good
     */
    private void addGoodToShop(Good good) {

        if (good == null)
            return;

        showProgressDialog();
        Shop shop = good.getShop();
        BmobRelation relation = new BmobRelation();
        relation.add(good);
        shop.setGoods(relation);
        shop.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                ToastUtils.showToast("添加成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                dismissProgressDialog();
                ToastUtils.showToast("店铺信息更新失败");
                finish();
            }
        });
    }

    /**
     * 输入合法性验证
     *
     * @return
     */
    private boolean checkInput() {
        if (etGoodName.getText().equals("")
                || etGoodPrice.getText().toString().equals("")
                || etGoodPriceD.getText().toString().equals("")
                || etGoodStock.getText().toString().equals("")
                || spGoodClass.getSelectedItem().toString().equals("")
                || etGoodInfo.getText().toString().equals("")) {
            ToastUtils.showToast("请先完善产品信息后再次提交");
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_save_good, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.menu_item_save_good) {
            if (type == SHOW_TYPE_CREATE)
                saveGood();
            else if (type == SHOW_TYPE_MODIFY)
                updateGood();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Spinner.OnItemSelectedListener onItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


}
