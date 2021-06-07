package com.vbm.riaaya.session;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class AppController extends Application {
    private static AppController mInstance;
    private static SharedPreferences sp_userinfo;
    private static SharedPreferences sp_isLogin;
    public static synchronized AppController getInstance() {
        return mInstance;
    }
    public static synchronized SharedPreferences getSpUserInfo() {
        return sp_userinfo;
    }
    public static synchronized SharedPreferences getSpIsLogin() {
        return sp_isLogin;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mInstance = this;
            initSharedPreferences();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSharedPreferences() {
        try {

            sp_userinfo = getApplicationContext().getSharedPreferences(SPUtils.USER_INFO, Context.MODE_PRIVATE);
            sp_isLogin = getApplicationContext().getSharedPreferences(SPUtils.IS_LOGIN, Context.MODE_PRIVATE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}