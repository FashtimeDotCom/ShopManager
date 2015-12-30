package com.stone.shopmanager.config;

/**
 * Created by stonekity.shi on 2015/4/6.
 */
public class HBUTConfig {


    //*********************************************JSON

    //学校配置
    public static final String URL_HBUT_DOMAIN = "http://202.114.176.103";

    //登录[http://202.114.176.103/Account/LogOnForJson?Mobile=1&UserName=1110321110&Password=scj201214&Role=Student]
    public static final String URL_HBUT_LOGIN = URL_HBUT_DOMAIN + "/Account/LogOnForJson";

    //退出
    public static final String URL_HBUT_LOGOUT = URL_HBUT_DOMAIN + "/Account/LogOffForJson/?Mobile=1";

    public static String urlLogin(String username, String password) {
        StringBuilder url = new StringBuilder();
        url.append(URL_HBUT_LOGIN);
        url.append("?");
        url.append("Username=" + username);
        url.append("&Password=" + password);
        url.append("&Role=Student");
        return url.toString();
    }

    public static String urlLogout() {
        return URL_HBUT_LOGOUT;
    }

}
