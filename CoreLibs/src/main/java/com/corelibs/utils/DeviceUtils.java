/*
 * Copyright (c) 2014 by EagleXad
 * Team: EagleXad
 * Create: 2014-08-29
 */

package com.corelibs.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.corelibs.R;
import com.corelibs.utils.openudid.OpenUDID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 设备管理类
 */
@SuppressLint("InlinedApi")
public class DeviceUtils {

    private static Context mContext; // 上下文
    private static volatile DeviceUtils instance = null;

    private TelephonyManager tm; // 手机管理
    private WifiManager wm; // 网络管理
    private PackageManager pm; // 包管理
    private ActivityManager am; // 界面管理

    /**
     * 获取当前实例
     *
     * @param context
     * @return
     */
    public static DeviceUtils getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        mContext = context;
        if (instance == null) {
            synchronized (DeviceUtils.class) {
                if (instance == null) {
                    instance = new DeviceUtils();
                }
            }

        }
        return instance;
    }

    /**
     * Method_获取手机管理对象
     *
     * @return
     */
    public TelephonyManager getTM() {

        if (tm == null) {
            tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return tm;
    }

    /**
     * Method_获取网络管理对象
     *
     * @return
     */
    public WifiManager getWM() {

        if (wm == null) {
            wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        }
        return wm;
    }

    /**
     * Method_获取包管理对象
     *
     * @return
     */
    public PackageManager getPM() {

        if (pm == null) {
            pm = mContext.getPackageManager();
        }
        return pm;
    }

    /**
     * Method_获取界面管理对象
     *
     * @return
     */
    public ActivityManager getAM() {

        if (am == null) {
            am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return am;
    }

    /**
     * Method_获取应用名
     *
     * @return
     */
    public String getContextName() {

        try {
            ApplicationInfo appInfo = getPM().getApplicationInfo(getPackageName(), 0);
            String appName = (String) getPM().getApplicationLabel(appInfo);

            return appName;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取语言
     *
     * @return
     */
    public String getLanguage() {

        Locale l = Locale.getDefault();

        return String.format("%s-%s", l.getLanguage(), l.getCountry());
    }

    /**
     * Method_获取设备Id
     *
     * @return
     */
    public String getDeviceId() {

        String deviceId = "";

        try {
            OpenUDID.syncContext(mContext);
            deviceId = OpenUDID.getCorpUDID(mContext.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceId;
    }

    /**
     * Method_获取 IMSI
     *
     * @return
     */
    public String getIMSI() {

        try {
            String IMSI = getTM().getSubscriberId();

            return IMSI;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取手机号码
     *
     * @return
     */
    public String getPhoneNumber() {

        try {
            String phoneNo = getTM().getLine1Number();

            return phoneNo;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取网络运营商
     *
     * @return
     */
    public String getNetProvider() {

        try {
            String netWorkOperator = getTM().getNetworkOperator();

            return netWorkOperator;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取 SIM
     *
     * @return
     */
    public String getSIM() {

        try {
            String SIM = getTM().getSimSerialNumber();

            return SIM;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取网络类型
     *
     * @return
     */
    @SuppressLint("UseSparseArrays")
    public String getNetWorkType() {

        try {
            int netWorkId = -1;

            netWorkId = getTM().getNetworkType();

            HashMap<Integer, String> ntMap = new HashMap<Integer, String>();

            ntMap.put(TelephonyManager.NETWORK_TYPE_UNKNOWN, mContext.getResources().getString(R.string.device_net_type_unknown));
            ntMap.put(TelephonyManager.NETWORK_TYPE_GPRS, mContext.getResources().getString(R.string.device_net_type_gprs));
            ntMap.put(TelephonyManager.NETWORK_TYPE_EDGE, mContext.getResources().getString(R.string.device_net_type_edge));
            ntMap.put(TelephonyManager.NETWORK_TYPE_UMTS, mContext.getResources().getString(R.string.device_net_type_umts));
            ntMap.put(TelephonyManager.NETWORK_TYPE_CDMA, mContext.getResources().getString(R.string.device_net_type_cdma));
            ntMap.put(TelephonyManager.NETWORK_TYPE_EVDO_0, mContext.getResources().getString(R.string.device_net_type_evdo0));
            ntMap.put(TelephonyManager.NETWORK_TYPE_EVDO_A, mContext.getResources().getString(R.string.device_net_type_evdoa));
            ntMap.put(TelephonyManager.NETWORK_TYPE_1xRTT, mContext.getResources().getString(R.string.device_net_type_1xrtt));
            ntMap.put(TelephonyManager.NETWORK_TYPE_HSDPA, mContext.getResources().getString(R.string.device_net_type_hsdpa));
            ntMap.put(TelephonyManager.NETWORK_TYPE_HSUPA, mContext.getResources().getString(R.string.device_net_type_hsupa));
            ntMap.put(TelephonyManager.NETWORK_TYPE_HSPA, mContext.getResources().getString(R.string.device_net_type_hspa));
            ntMap.put(TelephonyManager.NETWORK_TYPE_IDEN, mContext.getResources().getString(R.string.device_net_type_iden));
            ntMap.put(TelephonyManager.NETWORK_TYPE_EVDO_B, mContext.getResources().getString(R.string.device_net_type_evdob));
            ntMap.put(TelephonyManager.NETWORK_TYPE_LTE, mContext.getResources().getString(R.string.device_net_type_lte));
            ntMap.put(TelephonyManager.NETWORK_TYPE_EHRPD, mContext.getResources().getString(R.string.device_net_type_ehrpd));
            ntMap.put(TelephonyManager.NETWORK_TYPE_HSPAP, mContext.getResources().getString(R.string.device_net_type_hspap));

            String networkType = ntMap.get(netWorkId);

            return networkType;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取手机类型
     *
     * @return
     */
    @SuppressLint("UseSparseArrays")
    public String getPhoneType() {

        try {
            int phoneTypeId = -1;
            phoneTypeId = getTM().getPhoneType();

            Map<Integer, String> ptMap = new HashMap<Integer, String>();

            ptMap.put(TelephonyManager.PHONE_TYPE_NONE, mContext.getResources().getString(R.string.device_phone_type_none));
            ptMap.put(TelephonyManager.PHONE_TYPE_GSM, mContext.getResources().getString(R.string.device_phone_type_gsm));
            ptMap.put(TelephonyManager.PHONE_TYPE_CDMA, mContext.getResources().getString(R.string.device_phone_type_cmda));
            ptMap.put(TelephonyManager.PHONE_TYPE_SIP, mContext.getResources().getString(R.string.device_phone_type_sip));

            String phoneType = ptMap.get(phoneTypeId);

            return phoneType;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取手机模式
     *
     * @return
     */
    public String getProductModel() {

        try {
            String productModel = Build.MODEL;

            return productModel;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取系统版本
     *
     * @return
     */
    public String getOSVersion() {

        try {
            String osVer = Build.VERSION.RELEASE;

            return osVer;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取 SDK 版本
     *
     * @return
     */
    public int getSDKVersion() {

        try {
            int sdkVersion = Build.VERSION.SDK_INT;

            return sdkVersion;
        } catch (Exception e) {
            e.printStackTrace();

            return -1;
        }

    }

    /**
     * Method_获取 Mac 地址
     *
     * @return
     */
    public String getMacAddress() {
        String mac = "02:00:00:00:00:00";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(mContext);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacFromFile();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        return mac;


    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * @param context
     * @return
     */
    private String getMacDefault(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }

        if (getWM() == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = getWM().getConnectionInfo();
        } catch (Exception e) {
        }
        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     * @return
     */
    private String getMacFromFile() {
        String WifiAddress = "02:00:00:00:00:00";
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }


    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     * @return
     */
    private String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }


    /**
     * Method_获取 IP 地址
     *
     * @return
     */
    public int getIpAddress() {

        try {
            int ipAddress = getWM().getConnectionInfo().getIpAddress();

            return ipAddress;
        } catch (Exception e) {
            e.printStackTrace();

            return -1;
        }
    }

    /**
     * Method_获取版本名
     *
     * @return
     */
    public String getVersionName() {

        try {
            PackageInfo info = getPM().getPackageInfo(getPackageName(), 0);
            String verName = info.versionName;

            return verName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_获取版本号
     *
     * @return
     */
    public int getVersionCode() {

        try {
            PackageInfo info = getPM().getPackageInfo(getPackageName(), 0);
            int verCode = info.versionCode;

            return verCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();

            return -1;
        }
    }

    /**
     * Method_获取包名
     *
     * @return
     */
    public String getPackageName() {

        try {
            String pkgName = mContext.getPackageName();

            return pkgName;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * 获取分辨率内容
     * @return
     */
    public String getResolution(){
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        String resolution = dm.widthPixels + "*" + dm.heightPixels;

        return resolution;
    }

    /**
     * Method_获取 Meta 值
     *
     * @param metakey
     * @return
     */
    public String getMetaValue(String metakey) {

        Bundle meta = null;
        String value = null;

        if (mContext == null || TextUtils.isEmpty(metakey)) {

            return "";
        }

        try {
            ApplicationInfo ai = getPM().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            if (ai != null) {
                meta = ai.metaData;
            }
            if (meta != null) {
                value = meta.getString(metakey);
            }

            return value;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Method_是否在后台运行
     *
     * @return
     */
    public boolean isBackground() {

        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(mContext.getPackageName())) {
                // 后台运行
// 前台运行
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }

        return false;
    }

    /**
     * Method_是否处在休眠状态
     *
     * @return
     */
    public boolean isSleeping() {

        KeyguardManager kgMgr = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);

        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();

        return isSleeping;
    }

    /**
     * Method_是否存在 App
     *
     * @param pkgName
     * @return
     */
    public boolean isExitsApp(String pkgName) {

        if (TextUtils.isEmpty(pkgName)) {

            return false;
        }

        try {
            getPM().getPackageInfo(pkgName, 0);

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Method_是否是系统 App
     *
     * @param pkgName
     * @return
     */
    public boolean isSysApp(String pkgName) {

        if (TextUtils.isEmpty(pkgName)) {

            return false;
        }

        try {
            ApplicationInfo appInfo = getPM().getApplicationInfo(pkgName, 0);

            if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {

                return false;
            } else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Method_检查是否存在 SD 卡
     *
     * @return
     */
    public boolean checkSDcard() {

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Method_获取签名字符串
     *
     * @param pkgName
     * @return
     */
    public String getSign(String pkgName) {

        try {
            PackageInfo pis = mContext.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);

            return hexdigest(pis.signatures[0].toByteArray());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Method_转换签名字符串
     *
     * @param paramArrayOfByte
     * @return
     */
    private String hexdigest(byte[] paramArrayOfByte) {

        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

        try {

            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];

            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {

                    return new String(arrayOfChar);
                }

                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    // 获取CPU名字
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
