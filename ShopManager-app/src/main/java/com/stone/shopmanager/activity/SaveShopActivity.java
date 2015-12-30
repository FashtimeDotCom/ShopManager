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
import com.stone.shopmanager.model.shop.Shop;
import com.stone.shopmanager.model.shop.User;
import com.stone.shopmanager.util.TextUtils;
import com.stone.shopmanager.util.ToastUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SaveShopActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "SaveShopActivity";

    public static final String KEY_EXTRA_SHOW_TYPE = "com.stone.shopmanager.KEY_EXTRA_SHOW_TYPE";

    // Create a shop
    public static final int SHOW_TYPE_CREATE = 0;

    // Modify a shop
    public static final int SHOW_TYPE_MODIFY = 1;

    private EditText etShopName;
    private EditText etShopLoc;
    private EditText etShopInfo;
    private EditText etShopSale;
    private EditText etShopPhone;
    private Spinner spShopScrop;
    private ArrayAdapter<CharSequence> adapter;

    private int type = SHOW_TYPE_CREATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addshop);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initShowType();

        initView();
        initData();
    }

    private void initShowType() {
        if(getIntent().hasExtra(KEY_EXTRA_SHOW_TYPE)) {
            type = getIntent().getIntExtra(KEY_EXTRA_SHOW_TYPE, SHOW_TYPE_CREATE);
            if(type == SHOW_TYPE_CREATE)
                getSupportActionBar().setTitle(R.string.title_activity_save_shop);
            else if(type == SHOW_TYPE_MODIFY)
                getSupportActionBar().setTitle(R.string.title_activity_update_shop);
        }
    }

    private void initView() {
        etShopName = (EditText) findViewById(R.id.et_shop_name);
        etShopLoc = (EditText) findViewById(R.id.et_shop_loc);
        etShopInfo = (EditText) findViewById(R.id.et_shop_info);
        etShopSale = (EditText) findViewById(R.id.et_shop_sale);
        etShopPhone = (EditText) findViewById(R.id.et_shop_phone);
        spShopScrop = (Spinner) findViewById(R.id.sp_shop_type);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.array_shop_scrope, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spShopScrop.setAdapter(adapter);
        spShopScrop.setOnItemSelectedListener(onItemSelectedListener);

    }

    private void initData() {

        if(type == SHOW_TYPE_MODIFY) {
            Shop shop = ShopManager.getInstance().getSelectedShop();
            if(shop == null)
                return;
            etShopName.setText(shop.getName());
            etShopLoc.setText(shop.getLocation());
            etShopSale.setText(shop.getSale());
            etShopInfo.setText(shop.getInfo());
            etShopPhone.setText(shop.getPhone());
            spShopScrop.setSelection(adapter.getPosition(shop.getScrope()));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    // Create a shop
    private void saveShop() {

        Shop shop = new Shop();
        shop.setName(etShopName.getText().toString());
        shop.setLocation(etShopLoc.getText().toString());
        shop.setInfo(etShopInfo.getText().toString());
        shop.setSale(etShopSale.getText().toString());
        shop.setPhone(etShopPhone.getText().toString());
        shop.setScrope(spShopScrop.getSelectedItem().toString());  //经营范围
        shop.setUser(BmobUser.getCurrentUser(this, User.class));
        shop.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                ToastUtils.showToast("添加成功");
                finish();
            }

            @Override
            public void onFailure(int i, String arg0) {
                ToastUtils.showToast("添加失败");
                finish();
            }
        });
    }


    //update the shop info
    private void updateShop() {

        if(!checkInput())
            return;

        Shop shop = ShopManager.getInstance().getSelectedShop();
        if(shop!=null) {
            shop.setName(etShopName.getText().toString());
            shop.setLocation(etShopLoc.getText().toString());
            shop.setInfo(etShopInfo.getText().toString());
            shop.setSale(etShopSale.getText().toString());
            shop.setPhone(etShopPhone.getText().toString());
            shop.setScrope(spShopScrop.getSelectedItem().toString());  //经营范围
            shop.update(this, new UpdateListener() {
                @Override
                public void onSuccess() {
                    ToastUtils.showToast("更新成功");
                    finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.showToast("更新失败");
                    finish();
                }
            });
        }
    }

    private boolean checkInput() {
        if(etShopName.getText().toString().equals("") || etShopLoc.getText().toString().equals("")
                || etShopPhone.getText().toString().equals("") || etShopInfo.getText().toString().equals("")
                || spShopScrop.getSelectedItem().toString().equals("")) {
            ToastUtils.showToast("请先完善店铺信息后再次提交");
            return false;
        }

        if(!TextUtils.isPhoneNumberValid(etShopPhone.getText().toString())) {
            ToastUtils.showToast("请填写正确的电话号码");
            return false;
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_save_shop, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        if(id == R.id.menu_item_save_shop) {
            if(type == SHOW_TYPE_CREATE)
                saveShop();
            else
                updateShop();
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
