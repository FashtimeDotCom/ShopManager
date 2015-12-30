package com.stone.shopmanager.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.stone.shopmanager.application.BaseApplication;
import com.stone.shopmanager.application.LocalBroadcasts;
import com.stone.shopmanager.config.BmobConfig;
import com.stone.shopmanager.manager.CookiesManager;
import com.stone.shopmanager.manager.LoginManager;
import com.stone.shopmanager.model.shop.User;
import com.stone.shopmanager.util.MD5;
import com.stone.shopmanager.util.ToastUtils;
import com.stone.shopmanager.util.Utils;

import java.util.HashMap;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by stone on 15/5/31.
 */
public class FixLoginService extends IntentService {

    private static final String TAG = "FixLoginService";

    public static final String ACTION_FIX_LOGIN_RESULT = "com.stone.shop.ACTION_FIX_LOGIN_RESULT";
    public static final String KEY_EXTRA_FIX_LOGIN_RESULT = "com.stone.shop.KEY_EXTRA_FIX_LOGIN_RESULT";

    public FixLoginService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        startFixLogin();
    }

    private void startFixLogin() {

        if (!Utils.isNetworkAvailable(BaseApplication.getAppContext())) {
            ToastUtils.showToast("网络未连接，修复程序启动失败");
            if (BmobConfig.DEBUG)
                Log.d(TAG, "网络未连接，修复程序启动失败");
            sendFailMsg();
            return;
        }

        HashMap<String, String> map = LoginManager.getInstance().getLoginInfo();
        if (map == null) {
            ToastUtils.showToast("自动修复程序启动失败，请紧急联系校园小菜。");
            if (BmobConfig.DEBUG)
                Log.d(TAG, "自动修复程序启动失败，请紧急联系校园小菜。");
            sendFailMsg();
            return;
        }


        if (map.containsKey(LoginManager.SP_KEY_USERNAME) && map.containsKey(LoginManager.SP_KEY_PASSWORD)) {
            String username = map.get(LoginManager.SP_KEY_USERNAME);
            String password = map.get(LoginManager.SP_KEY_PASSWORD);
            String loginMethod = map.get(LoginManager.SP_KEY_LOGIN_METHOD);

            if (!CookiesManager.getInstance().hasCookies() || !loginMethod.equals(LoginManager.LOGIN_M_SCHOOL + "")
                    || username.equals("") || password.equals("")) {
                ToastUtils.showToast("自动修复程序环境验证失败，请紧急联系校园小菜。");
                if (BmobConfig.DEBUG)
                    Log.d(TAG, "自动修复程序环境验证失败，请紧急联系校园小菜。");
                sendFailMsg();
                return;
            }

            fixLogin(username, password);
        }
    }

    private void fixLogin(String username, String password) {
        User user = new User();
        user.setUsername(MD5.md5String(username));
        user.setPassword(MD5.md5String(password));
        user.login(BaseApplication.getAppContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtils.showToast("自动修复成功");
                if (BmobConfig.DEBUG)
                    Log.d(TAG, "自动修复成功");
                sendSuccessMsg();

            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showToast("自动修复失败");
                if (BmobConfig.DEBUG)
                    Log.d(TAG, String.format("自动修复失败 [%d: %s]", i, s));
                sendFailMsg();
            }
        });
    }

    private void sendSuccessMsg() {
        Intent intent = new Intent(ACTION_FIX_LOGIN_RESULT);
        intent.putExtra(KEY_EXTRA_FIX_LOGIN_RESULT, true);
        LocalBroadcasts.sendLocalBroadcast(intent);
    }

    private void sendFailMsg() {
        Intent intent = new Intent(ACTION_FIX_LOGIN_RESULT);
        intent.putExtra(KEY_EXTRA_FIX_LOGIN_RESULT, false);
        LocalBroadcasts.sendLocalBroadcast(intent);
    }


}
