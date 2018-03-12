package com.jew.chzhshch;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.corelibs.api.ApiFactory;
import com.corelibs.common.Configuration;
import com.corelibs.exception.GlobalExceptionHandler;
import com.corelibs.utils.GalleryFinalConfigurator;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.ToastMgr;
import com.jew.chzhshch.constants.Urls;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * 全局APP
 * Created by Administrator on 2018/3/2/002.
 */

public class App extends MultiDexApplication
{
    private static App app;

    public static App getApp()
    {
        if (app == null)
        {
            synchronized (App.class)
            {
                if (app == null)
                {
                    app = new App();
                }
            }
        }
        return app;
    }

    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;

        GlobalExceptionHandler.getInstance().init(this, getResources().getString(R.string.app_name)); //初始化全局异常捕获
        ToastMgr.init(getApplicationContext()); //初始化Toast管理器
        Configuration.enableLoggingNetworkParams(); //打开网络请求Log打印，需要在初始化Retrofit接口工厂之前调用
        ApiFactory.getFactory().add(Urls.ROOT_API); //初始化Retrofit接口工厂
        PreferencesHelper.init(getApplicationContext()); //初始化SharedPreferences工具类
        FileDownloader.init(getApplicationContext()); //初始化下载工具
        GalleryFinalConfigurator.config(getApplicationContext()); //初始化GalleryFinal
    }
}
