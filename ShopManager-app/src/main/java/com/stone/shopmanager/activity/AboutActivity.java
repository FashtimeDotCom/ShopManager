package com.stone.shopmanager.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;

import cn.bmob.v3.update.BmobUpdateAgent;

public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initView() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_about, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            //检查更新
            case R.id.menu_item_about_update:
                Log.d(TAG, "开始更新");
                BmobUpdateAgent.forceUpdate(this);
                return true;

            //关于软件
            case R.id.menu_item_about_about:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
