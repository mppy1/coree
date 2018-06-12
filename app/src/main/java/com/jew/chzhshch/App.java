package com.jew.chzhshch;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.corelibs.api.ApiFactory;
import com.corelibs.common.Configuration;
import com.corelibs.exception.GlobalExceptionHandler;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.ToastMgr;
import com.jew.chzhshch.constants.Urls;

/**
 * 全局APP
 * Created by Administrator on 2018/3/2/002.
 */

public class App extends MultiDexApplication {
    private static App app;

    public static App getApp() {
        return app;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        GlobalExceptionHandler.getInstance().init(this, getResources().getString(R.string.app_name)); //初始化全局异常捕获
        ToastMgr.init(getApplicationContext()); //初始化Toast管理器
        Configuration.enableLoggingNetworkParams(); //打开网络请求Log打印，需要在初始化Retrofit接口工厂之前调用
        ApiFactory.getFactory().add(Urls.ROOT_API); //初始化Retrofit接口工厂
        PreferencesHelper.init(getApplicationContext()); //初始化SharedPreferences工具类

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // android 7.0系统解决拍照报exposed beyond app through ClipData.Item.getUri()
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }
}
