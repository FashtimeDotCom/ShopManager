package com.stone.shopmanager.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.stone.shopmanager.application.BaseApplication;
import com.stone.shopmanager.util.ToastUtils;

import org.apache.http.cookie.Cookie;

import java.util.List;
import java.util.Map;

/**
 * Created by stonekity.shi on 2015/4/6.
 */
public class CookiesManager {

    private static CookiesManager instance;
    private static Object lock = new Object();

    private CookiesManager() {

    }

    public static CookiesManager getInstance() {
        if (null == instance) {
            synchronized (lock) {
                if (null == instance) {
                    instance = new CookiesManager();
                }
            }
        }

        return instance;
    }

    /**
     * 判断是否使用学号成功登陆
     *
     * @return
     */
    public boolean hasCookies() {
        Map<String, ?> map = getCookies();
        if (map != null && map.size() > 0)
            return true;
        else
            return false;
    }


    public void saveCookies(List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            SharedPreferences sp = BaseApplication.getAppContext().getSharedPreferences("cookies",
                    Context.MODE_PRIVATE);
            sp.edit().clear().commit();
            SharedPreferences.Editor editor = sp.edit();
            for (Cookie cookie : cookies) {
                editor.putString(cookie.getName(), cookie.getValue());
            }
            editor.commit();
            ToastUtils.showToast("Cookies Saved Success");
        }
    }

    public Map<String, ?> getCookies() {
        return getStoreCookies();
    }

    private Map<String, ?> getStoreCookies() {
        SharedPreferences sp = BaseApplication.getAppContext().getSharedPreferences("cookies",
                Context.MODE_PRIVATE);
        return sp.getAll();
    }


}
