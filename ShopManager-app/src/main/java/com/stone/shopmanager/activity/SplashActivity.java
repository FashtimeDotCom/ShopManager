package com.stone.shopmanager.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.config.BmobConfig;
import com.stone.shopmanager.util.DeviceUtils;
import com.stone.shopmanager.util.ToastUtils;
import com.stone.shopmanager.util.Utils;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class SplashActivity extends BaseActivity {

    private static final int GO_LOGIN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkNetwork();

        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, BmobConfig.BMOB_APP_ID_NEW);

        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, BmobConfig.BMOB_APP_ID_NEW);

        setContentView(R.layout.activity_splash);

        initView();

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_LOGIN:
                    goLogin();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void initView() {

        mHandler.sendEmptyMessageDelayed(GO_LOGIN, 3000);
    }


    private void checkNetwork() {
        if (!Utils.isNetworkAvailable(this))
            ToastUtils.showToast("网络连接异常");
    }

    private void goLogin() {
        // 禁止模拟器直接运行
        if (!BmobConfig.DEBUG && DeviceUtils.isEmulator(this)) {
            ToastUtils.showToast("校园小菜不支持该环境，程序员！");
            finish();
            return;
        }

        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashActivity"); // 统计页面
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashActivity"); // 保证 onPageEnd 在onPause
        // 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

}
