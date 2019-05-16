package com.jew.coree.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.corelibs.utils.DateUtils;
import com.corelibs.utils.DeviceUtils;
import com.corelibs.utils.LogUtils;
import com.google.gson.Gson;
import com.jew.coree.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 工具类
 *
 * @author Administrator花日中
 */
public class Utils {


    // 判断是否有网
    public static boolean isNetworkAvailable(Context ctx) {
        Context context = ctx.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断当前网络是否是wifi网络.
     *
     * @param context the context
     * @return boolean
     */
    public static boolean isWifiConnectivity(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * @param activity
     * @param clazz
     * @param bundle
     * @param requestCode
     */
    @SuppressWarnings("rawtypes")
    public static void startActivityForResult(Activity activity, Class clazz,
                                              Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /*
     * 把dp值转换成px值
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }


    //添加了199之类新的手机号
    public static boolean isMobileNO(String str)
            throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    // 是否座机号或者手机号
    public static boolean isTelNO(String tel) {

        // Pattern p = Pattern.compile("^0[0-9]{9,11}$|^[0-9]{7,8}$");
        Pattern p = Pattern.compile("^((0\\d{2,3}-\\d{7,8})|(1[3584]\\d{9}))$");
        Matcher m = p.matcher(tel);
        return m.matches();
    }

    // 是否是email
    public static boolean isEmail(String email) {
        String str = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(str);
    }

    // 防止webview内存泄露
    public static void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }

            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
        }
    }

    // 根据SD卡图片路径获取Bitmap
    public static Bitmap getLoacalBitmap(String url) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        // 4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        opts.inPurgeable = true;
        FileInputStream in = null;
        try {
            in = new FileInputStream(url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return BitmapFactory.decodeStream(in, null, opts);
    }

    /*
     * 4.3以及一下从相册获取图片的路径
     */
    public static String getPath(Uri uri, Activity ctx) {
        String filePath = "";
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = ctx.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(columnIndex);
            try {
                // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
                if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                    cursor.close();
                }
            } catch (Exception e) {
            }
        }
        return filePath;
    }

    /*
     * 4.4以上从相册获取图片的路径
     */
    @SuppressLint("NewApi")
    public static String getPath2(Uri uri, Activity context) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    // 显示本地图片
    public static void showLocalImage(final String path, ImageView image) {
        File file = new File(path);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            image.setImageURI(uri);
        }
    }

    // 隐藏虚拟键盘
    public static void hiddenInput(Context context, EditText et_Content) {
        if (null != et_Content) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_Content.getWindowToken(), 0);
        }
    }

    // 隐藏虚拟键盘
    public static void hiddenInputForce(Activity context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (context.getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(context.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // 字符全角化
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    // 替换、过滤特殊字符
    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        value = removeBOM(value);
        try {
            new JSONObject(value);

        } catch (JSONException e) {
            Log.i("RelayRecommendActivity",
                    "JSONException--------------->" + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 判断集合是否为空
    public static boolean isListNotEmpty(List<?> list) {
        if (null != list) {
            if ((list.size() > 0) && !list.isEmpty())
                return true;
        }
        return false;
    }


    /**
     * 关闭键盘
     */
    public static void closeInputMethod(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            IBinder binder = activity.getCurrentFocus().getWindowToken();
            if (null != binder) {
                imm.hideSoftInputFromWindow(binder,
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 验证是否全部中文
     *
     * @param str
     * @return true 是中文 false 不是中文
     */
    public static boolean isChineseStr(String str) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        char c[] = str.toCharArray();
        for (char aC : c) {
            Matcher matcher = pattern.matcher(String.valueOf(aC));
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean containsChineseStr(String str) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 判断选择年份大于当前年份
     *
     * @param mContext
     * @param choiceYear  年份
     * @param choiceMonth 月份
     * @param choiceDay   日期
     * @return true 大于 false 小于
     */
    public static boolean checkTime(Context mContext, int choiceYear,
                                    int choiceMonth, int choiceDay) {

        boolean IstrueTime = true;
        // 拿到当前时间
        int currentYear = Integer
                .parseInt(String.valueOf(DateUtils.getYears()));
        int currentMonth = Integer.parseInt(DateUtils.getMonth());
        int currentDay = Integer.parseInt(DateUtils.getDay());

        if (choiceYear > currentYear) {

            IstrueTime = false;

        } else if (choiceYear == currentYear) {

            if (choiceMonth > currentMonth) {
                IstrueTime = false;

            } else if (choiceMonth == currentMonth) {

                if (choiceDay > currentDay) {
                    IstrueTime = false;
                }
            }

        }
        return IstrueTime;

    }

    /**
     * @param s1
     * @param s2
     * @return result -1 小于 0 等于 1 大于
     */
    public static int compareTime(String s1, String s2) {
        DateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();

        try {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));

        } catch (ParseException e) {
        }
        int result = c1.compareTo(c2);
        // if (result == 0)
        // System.out.println("c1相等c2");
        // else if (result < 0)
        // System.out.println("c1小于c2");
        // else
        // System.out.println("c1大于c2");
        return result;

    }

    // 判断文件是否存在
    public static boolean isFileExsit(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * url地址验证
     */
    public static boolean isUrl(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(url);
        boolean isMatch = matcher.matches();
        return isMatch;
    }

    // 设置webview 默认属性
    @SuppressLint("SetJavaScriptEnabled")
    public static void setWebViewConfig(WebView webView) {
        // 设置滚动条
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 触摸焦点起作用
        webView.requestFocus();
        WebSettings webSetting = webView.getSettings();
//        webSetting.setDefaultFontSize(28);
        // 设置js可用 ,js弹出窗
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setSupportMultipleWindows(true);
        // 设置userAgent
        String versionCode = DeviceUtils.getInstance(App.getApp()).getVersionName();
        String useragent = "traceback" + "/" + versionCode;
        webSetting.setUserAgentString(useragent);
        // 加这个是为了解决打开页面时候有解析报错问题
        webSetting.setDomStorageEnabled(true);
        // 支持geo
        webSetting.setGeolocationEnabled(true);
        // 自适应屏幕
//        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setLoadWithOverviewMode(true);
        // 扩大比例的缩放
        webSetting.setUseWideViewPort(true);
        // 给webview加缓存
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE); // 设置缓存模式
//        if (!Utils.isNetworkAvailable(App.getApp())) { // 无网络缓存模式，有网默认
//            // LOAD_DEFAULT，不用缓存
//            webSetting.setCacheMode(WebSettings.LOAD_CACHE_ONLY); // 设置缓存模式
//        }
//        if (Utils.isWifiConnectivity(App.getApp())) {
//            webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE); // 设置缓存模式
//        }

        // 开启 database storage API 功能 
        webSetting.setDatabaseEnabled(true);
        String cacheDirPath = FileUtil.getWebCacaheDir();
        // 设置  Application Caches 缓存目录
        webSetting.setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        webSetting.setAppCacheEnabled(true);
    }


    // 移除json返回中的bom头，当服务器端字符编码和客户端不同时出现
    public static final String removeBOM(String data) {
        if (TextUtils.isEmpty(data)) {
            return data;
        }
        if (data.startsWith("\ufeff")) {
            return data.substring(1);
        } else {
            return data;
        }
    }

    // app不需要根据系统字体的大小来改变的
    public static Resources setResources(Context context) {
        Resources res = context.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    public static void scrollToBottom(final ScrollView src) {
        src.post(new Runnable() {
            @Override
            public void run() {
                src.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public static void scrollToUp(final ScrollView src) {
        src.post(new Runnable() {
            @Override
            public void run() {
                src.fullScroll(View.FOCUS_UP);
            }
        });
    }

    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String beanToJson(Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T beanfromJson(String str, Class<T> type) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(str, type);
        } catch (Exception e) {
            // TODO: handle exception
            LogUtils.e("beanfromJson ====" + e.getMessage());
            return null;
        }
    }

    public static String getJsonData(String str) {
        String data = "";
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            if (jsonObject.has("data")) {
                data = jsonObject.getString("data");
            }
        } catch (JSONException e) {

            e.printStackTrace();
            data = "";
        }
        return data;
    }

    public static String getJsonById(String str, String key) {
        String data = "";
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            if (jsonObject.has(key)) {
                data = jsonObject.getString(key);
            }
        } catch (JSONException e) {

            e.printStackTrace();
            data = "";
        }
        return data;
    }

    /**
     * @param array
     * @param cls
     * @return
     */
    public static <T> List<T> listfromJson(String array, Class<T> cls) {

        List<T> list = new ArrayList<T>();

        try {
            JSONArray jsonArray = new JSONArray(array);

            for (int i = 0; i < jsonArray.length(); i++) {
                T t = beanfromJson(jsonArray.get(i).toString(), cls);
                list.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 获取相应包名的信息
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean uninstallSoftware(Context context, String packageName) {

        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo pInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            // 判断是否获取到了对应的包名信息
            if (pInfo != null) {

                return true;

            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 禁止输入表情
     */
    public static InputFilter emojiFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }
    };
    public static InputFilter[] emojiFilters = {emojiFilter};

    /**
     * 验证str是否为正确的身份证格式
     *
     * @param
     * @return
     */
    public static boolean isIdentityCard(String licenc) {
        boolean flag = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        /*
         * { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
         * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
         * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
         * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
         * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
         * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外" }
         */
        String provinces = "11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91";

        Pattern pattern = Pattern.compile("^[1-9]\\d{14}");
        Matcher matcher = pattern.matcher(licenc);
        Pattern pattern2 = Pattern.compile("^[1-9]\\d{16}[\\d,x,X]$");
        Matcher matcher2 = pattern2.matcher(licenc);
        // 粗略判断
        if (!matcher.find() && !matcher2.find()) {
            flag = false;
        } else {
            // 判断出生地
            if (!provinces.contains(licenc.substring(0, 2))) {
                flag = false;
            }

            // 判断出生日期
            if (licenc.length() == 15) {
                String birth = "19" + licenc.substring(6, 8) + "-"
                        + licenc.substring(8, 10) + "-"
                        + licenc.substring(10, 12);
                try {
                    Date birthday = sdf.parse(birth);
                    if (!sdf.format(birthday).equals(birth)) {
                        flag = false;
                    }
                    if (birthday.after(new Date())) {
                        flag = false;
                    }
                } catch (ParseException e) {
                    flag = false;
                }
            } else if (licenc.length() == 18) {
                String birth = licenc.substring(6, 10) + "-"
                        + licenc.substring(10, 12) + "-"
                        + licenc.substring(12, 14);
                try {
                    Date birthday = sdf.parse(birth);
                    if (!sdf.format(birthday).equals(birth)) {
                        flag = false;
                    }
                    if (birthday.after(new Date())) {
                        flag = false;
                    }
                } catch (ParseException e) {
                    flag = false;
                }
            } else {
                flag = false;
            }
        }

        return flag;
    }

    /**
     * 判断应用是否在运行
     *
     * @param context
     * @return
     */
    public static boolean isRun(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        if (!isListNotEmpty(list)) {
            return isAppRunning;
        }
        String MY_PKG_NAME = context.getPackageName();
        //100表示取的最大的任务数，info.topActivity表示当前正在运行的Activity，info.baseActivity表系统后台有此进程在运行
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                Log.i("ActivityService isRun()", info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
                break;
            }
        }

        Log.i("ActivityService isRun()", "com.ad 程序   ...isAppRunning......" + isAppRunning);
        return isAppRunning;
    }

    public static boolean isRunningApp(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (!isListNotEmpty(list)) {
            return isAppRunning;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                // find it, break
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 检测某ActivityUpdate是否在当前Task的栈顶
     */
    public static boolean isTopActivity(Context context, String cmdName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;


        if (null != runningTaskInfos) {
            cmpNameTemp = (runningTaskInfos.get(0).topActivity.getClassName()).toString();
        }


        if (null == cmpNameTemp) return false;
        return cmpNameTemp.equals(cmdName);
    }

}
