package com.stone.shopmanager.manager;

import android.util.Log;

import com.stone.shopmanager.config.BmobConfig;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by stonekity.shi on 2015/4/6.
 */
public class JSONManager {

    private static final String TAG = "JSONManager";


    /**
     * 去掉Json字符串首位多余的双引号
     *
     * @param json
     * @return
     */
    private static String formatJsonString(String json) {
        if (json == null || json.equals(""))
            return "";
        json = json.replace("\\", "");
        StringBuilder sb = new StringBuilder(json);
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);
        Log.d(TAG, "json after format: " + sb.toString());
        return sb.toString();
    }


    /**
     * 获得用户登录结果
     *
     * @param httpResponse
     * @return
     */
    public static HashMap<String, String> getLoginResult(String httpResponse) {
        Log.d(TAG, "httpResponse: " + httpResponse);
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            if (null != httpResponse) {
                JSONObject jsonObject = new JSONObject(httpResponse);
                if (jsonObject.has("Status")) {
                    map.put("Status", jsonObject.getString("Status"));
                } else {
                    map.put("Status", "");
                }
                if (jsonObject.has("Message")) {
                    map.put("Message", jsonObject.getString("Message"));
                } else {
                    map.put("Message", "");
                }
            }
        } catch (Exception e) {
        }
        if (BmobConfig.DEBUG) {
            Log.i(TAG, "Login Response HashMap: " + map.toString());
        }
        return map;
    }

}
