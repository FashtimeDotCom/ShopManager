package com.stone.shopmanager.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * 
 * @date 2014-5-9
 * @author Stone
 */
public class Utils {

    // 判断网络是否连接
    public static boolean isNetworkAvailable(Context context) {
        if ( context == null ) {
            return false;
        }

        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        if ( connectivity == null ) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if ( info != null ) {
                int l = info.length;
                for ( int i = 0; i < l; i++ ) {
                    if ( info[i].getState() == NetworkInfo.State.CONNECTED ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
