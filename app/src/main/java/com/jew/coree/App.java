package com.jew.coree;

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
import com.jew.coree.constants.Urls;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadLog;

import java.net.Proxy;

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
        GlobalExceptionHandler.getInstance().init(this, getResources().getString(com.jew.coree.R.string.app_name)); //初始化全局异常捕获
        ToastMgr.init(getApplicationContext()); //初始化Toast管理器
        Configuration.enableLoggingNetworkParams(); //打开网络请求Log打印，需要在初始化Retrofit接口工厂之前调用
        ApiFactory.getFactory().add(Urls.ROOT_API); //初始化Retrofit接口工厂
        ApiFactory.getFactory().add(Urls.ROOT_LED,Urls.ROOT_LED); //初始化Retrofit接口工厂
        PreferencesHelper.init(getApplicationContext()); //初始化SharedPreferences工具类

        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY)
                ))
                .commit();
        FileDownloadLog.NEED_LOG = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // android 7.0系统解决拍照报exposed beyond app through ClipData.Item.getUri()
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }
}
