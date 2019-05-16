package com.jew.coree.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.format.Formatter;

import com.corelibs.utils.LogUtils;
import com.corelibs.utils.ToastMgr;
import com.jew.coree.App;
import com.jew.coree.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil extends BaseFileUtils {

    public static final String DATA_PATH = "/traceback/";
    /**
     * SD卡中的拍照图片存储路径
     */
    public static final String DATA_CAMERA_PATH = "camera/";
    private static final String DATA_PHOTO_PATH = "photo/";
    private static final String DATA_VIDEO_PATH = "video/";
    /**
     * 临时文件
     */
    public static final String DATA_TEMP_PATH = "temp/";
    /**
     * SD卡中的下载文件的存储路径
     */
    public static final String DOWNLOAD_FILE_PATH = "download/";
    /**
     * SD卡中的用户头像存储路径
     */
    public static final String DATA_PHOTO_USER = "user/";
    /**
     * SD卡中的webview 缓存目录
     */
    public static final String WEBVIEW_CACHE_DIRNAME = "/webcache";
    /**
     * 崩溃日志
     */
    public static final String CRASH_DIRNAME = "crash/";
    /**
     * 聊天图片
     */
    public static final String PICTURE_DIRNAME = "picture/";

    /**
     * 控制变量，是否支持手机存储数据
     */
    private static final boolean ENABLE_INTERNAL_DIR = true;


    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(App.getApp(), blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(App.getApp(), blockSize * availableBlocks);
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static String getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(App.getApp(), blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static String getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(App.getApp(), blockSize * availableBlocks);
    }

    public static int copyFile(String assetsDir, String sandboxDir,
                               String fileName) {
        // 新建输入输出流
        InputStream inStream = null;
        BufferedInputStream bufferInStream = null;
        FileOutputStream outStream = null;
        BufferedOutputStream bufferOutStream = null;
        try {
            // 获得或新建目录
            File outDir = new File(sandboxDir);
            if (!outDir.exists()) {
                outDir.mkdir();
            }

            // 获得或新建文件
            File outFilePath = new File(sandboxDir + fileName);
            if (!outFilePath.exists()) {
                outFilePath.createNewFile();
            }

            int bytesum = 0;
            int byteread = 0;

            // 初始化输入流和输出流
            inStream = App.getApp().getAssets()
                    .open(assetsDir + fileName);
            bufferInStream = new BufferedInputStream(inStream);
            outStream = new FileOutputStream(sandboxDir + fileName);
            bufferOutStream = new BufferedOutputStream(outStream);

            // 开始拷贝
            byte[] buffer = new byte[1024];
            while ((byteread = bufferInStream.read(buffer)) != -1) {
                bytesum += byteread;
                // Logger.d("----" + bytesum);
                bufferOutStream.write(buffer, 0, byteread);
            }
            bufferOutStream.flush();
            LogUtils.d("----copy fileName:[" + fileName + "],content length:"
                    + bytesum);

        } catch (Exception e) {
            LogUtils.e("拷贝文件异常" + e);

            return -1;
        } finally {
            // 关闭输入输出流
            closeOutputStream(bufferOutStream);
            closeOutputStream(outStream);
            closeInputStream(bufferInStream);
            closeInputStream(inStream);
        }

        return 0;
    }


    /**
     * 获取sd卡完整路径(包含sd卡)，如路径不存在，则在sd卡中创建路径, 如果sd不存在，则放到手机内存中
     *
     * @param name 数据路径
     * @return
     * @throws Exception
     */
    public static String getDataDir(String name) throws Exception {
        String basePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            basePath = Environment.getExternalStorageDirectory() + DATA_PATH;
        } else { // get mobile memory
            if (ENABLE_INTERNAL_DIR) {
                ApplicationInfo appInfo = App.getApp().getApplicationContext().getApplicationInfo();
                basePath = appInfo.dataDir;
            } else {
                ToastMgr.show("存储卡不存在");
                throw new Exception("");
            }
        }

        if (basePath.endsWith("/")) {
        } else {
            basePath = basePath + "/";
        }

        String path = basePath;
        File base = new File(path);
        if (!base.exists()) {
            base.mkdirs();
        }
        if (!base.exists()) {
            String message = App.getApp().getResources()
                    .getString(R.string.dir_not_exists);
            message = String.format(message, path);
            // ToastUtils.show(BaseApplication.getInstance(), message);
            // log(message);
            throw new IOException(message);
        }
        if (name != null) {
            path = basePath + name;
        }
        LogUtils.d("getDataDir path:" + path);
        File dir = new File(path); // 判断文件夹是否存在
        boolean dirExist = false;
        if (dir.exists()) {
            dirExist = true;
        } else {
            // log("create dir: " + path);
            dirExist = dir.mkdirs(); // 文件夹不存在，则创建文件夹
            // log("create dir result: " + dirExist);
        }

        addWriteMode(dir.getAbsolutePath());

        if (!dirExist) {
            String message = App.getApp().getResources()
                    .getString(R.string.dir_not_exists);
            message = String.format(message, path);
            // ToastUtils.show(BaseApplication.getInstance(), message);
            // log(message);
            throw new IOException(message);
        }

        return path;
    }

    /**
     * 创建文件
     *
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static File createFile(String path, String fileName)
            throws IOException, InterruptedException {
        File file = null;
        try {
            file = BaseFileUtils.createFile(path, fileName);
        } catch (IOException e) {
            String message = App.getApp().getResources()
                    .getString(R.string.dir_not_exists);
            message = String.format(message, path);
            // ToastUtils.show(BaseApplication.getInstance(), message);
            throw new IOException(message);
        }

        // if (!file.canWrite()){
        // String message = "文件夹" + fileName + "]只读";
        // log(message);
        // throw new IOException(message);
        // }

        return file;
    }

    /**
     * 获得照片存储路径
     */
    public static String getPhotoDir() {
        try {
            return getDataDir(DATA_PHOTO_PATH);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    /**
     * 获得拍照照片存储路径
     */
    public static String getCameraDir() {
        try {
            return getDataDir(DATA_CAMERA_PATH);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    public static String getVideoDir() {
        try {
            return getDataDir(DATA_VIDEO_PATH);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    /**
     * 获取下载文件夹
     *
     * @return
     */
    public static String getDownloadDir() {
        try {
            return getDataDir(DOWNLOAD_FILE_PATH);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    /**
     * 获得User icon存储路径
     */
    public static String getUserIconDir() {
        try {
            return getDataDir(DATA_PHOTO_USER);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    /**
     * 获得临时存储路径
     */
    public static String getTempDir() {
        try {
            return getDataDir(DATA_TEMP_PATH);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    /**
     * 获得崩溃存储路径
     */
    public static String getCrashDir() {
        try {
            return getDataDir(CRASH_DIRNAME);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    /**
     * 获得聊天图片存储路径
     */
    public static String getPictureDir() {
        try {
            return getDataDir(PICTURE_DIRNAME);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    public static String getWebCacaheDir() {
        try {
            return getDataDir(WEBVIEW_CACHE_DIRNAME);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return null;
        }
    }

    /**
     * 往存储卡写文件数据
     *
     * @param dirPath           文件路径(文件在存储卡中的路径，不包含存储卡)
     * @param fileName          文件名(不含路径)
     * @param contents          文件内容，每个数组元素为文件中的一行数据
     * @param isAppend          标识符：true－追加的方式将待写入的数据写入文件末尾，false－覆盖的方式将待写入的数据写入文件
     * @param filterSameContent 标识符：true－过滤掉相同的行内容，false－不过滤
     * @throws IOException
     */
    public static void writeFile(String dirPath, String fileName, List<String> contents, boolean isAppend, boolean filterSameContent)
            throws IOException {
        String[] contentArr = null;
        if (contents != null) {
            contentArr = contents.toArray(new String[0]);
        }

        writeFile(dirPath, fileName, contentArr, isAppend, filterSameContent);
    }

    /**
     * 往存储卡写文件数据
     *
     * @param dirPath           文件路径(全路径，SD卡或/data/data/com.jsecode.police/)
     * @param fileName          文件名(不含路径)
     * @param contents          文件内容，每个数组元素为文件中的一行数据
     * @param isAppend          标识符：true－追加的方式将待写入的数据写入文件末尾，false－覆盖的方式将待写入的数据写入文件
     * @param filterSameContent 标识符：true－过滤掉相同的行内容，false－不过滤
     * @throws IOException
     */
    public static void writeFile(String dirPath, String fileName,
                                 String[] contents, boolean isAppend, boolean filterSameContent)
            throws IOException {

        String path = dirPath;
        File dir = new File(path); // 判断文件夹是否存在
        boolean dirExist = false;
        if (dir.exists()) {
            dirExist = true;
        } else {
            // log("create dir: " + path);
            dirExist = dir.mkdirs(); // 文件夹不存在，则创建文件夹
            // log("create dir result: " + dirExist);
        }

        if (!dirExist) {
            String message = App.getApp().getResources()
                    .getString(R.string.dir_not_exists);
            message = String.format(message, path);
            LogUtils.d(message);
            // ToastUtils.show(BaseApplication.getInstance(), message);
            throw new IOException(message);
        }

        File file = new File(path, fileName);
        if (!file.exists()) {
            LogUtils.d("create file:" + file.toString());
            file.createNewFile();
        }

        if (file.canWrite()) {
            writeFile(file, contents, isAppend, filterSameContent);
        } else {
            String message = App.getApp().getResources()
                    .getString(R.string.file_can_not_write);
            message = String.format(message, fileName);
            LogUtils.d(message);
            throw new IOException(message);
        }
    }

    /**
     * 将数组数据写入文件，每个数组元素在文件中为 一行。
     *
     * @param f                 待写入的文件
     * @param contents          待写入的数据
     * @param isAppend          标识符：true－追加的方式将待写入的数据写入文件末尾，false－覆盖的方式将待写入的数据写入文件
     * @param filterSameContent 标识符：true－过滤掉相同的行内容，false－不过滤
     * @throws IOException
     */
    public static void writeFile(File f, String[] contents, boolean isAppend,
                                 boolean filterSameContent) throws IOException {
        if (contents == null) {
            return;
        }

        List<String> dataList = null;
        if (isAppend) {
            dataList = readFile(f);
        } else {
            dataList = new ArrayList<String>(contents.length);
        }

        for (String content : contents) {
            if (filterSameContent && dataList.contains(content)) {
                continue;
            } else {
                dataList.add(content);
            }
        }

        LogUtils.d("write file begin:" + f.toString());
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(f, false)));
            if (dataList.size() > 0) {
                for (String content : dataList) {
                    writer.write(content);
                    writer.write(System.getProperty("line.separator"));
                }
            } else {
                writer.write("");
            }

            writer.flush();
        } catch (FileNotFoundException e) {
            String message = App.getApp().getResources()
                    .getString(R.string.file_not_exists);
            message = String.format(message, f.getName());
            LogUtils.d(e.getMessage());
            throw new IOException(message);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LogUtils.d(e.getMessage());
                }
            }
        }

        LogUtils.d("write file finish.");
    }

    /**
     * 写文件(覆盖已存在文件)
     *
     * @param dirPath  路径(含SD卡或root)
     * @param fileName 文件名
     * @param contents 待写入的内容的字节数组
     * @throws IOException
     */
    public static void writeFile(String dirPath, String fileName,
                                 byte[] contents) throws IOException {

        // 判断文件夹是否存在，不存在，则创建
        File dir = new File(dirPath);
        boolean dirExist = false;
        if (dir.exists()) {
            dirExist = true;
        } else {
            dirExist = dir.mkdirs(); // 文件夹不存在，则创建文件夹
        }

        if (!dirExist) {
            String message = App.getApp().getResources()
                    .getString(R.string.dir_not_exists);
            message = String.format(message, dirPath);
            // ToastUtils.show(BaseApplication.getInstance(), message);
            LogUtils.d(message);
            throw new IOException(message);
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(dirPath, fileName));
            fos.write(contents);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LogUtils.d(e.getMessage());
                }
            }
        }
    }

    /**
     * 从文件中读取数据，每行数据作为List的一个元素
     *
     * @param f 待读取的数据文件
     * @return 数据列表(文件中的每行就是列表中的一个元素)
     * @throws IOException
     */
    public static List<String> readFile(File f) throws
            IOException {
        BufferedReader reader = null;
        List<String> result = new ArrayList<String>();
        // log("read file:" + f.toString());
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(f)));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.add(line.trim());
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LogUtils.d(e.getMessage());
                }
            }
        }

        return result;
    }

    /**
     * 读文件，将文件内容读到字节数组中
     *
     * @param dirPath  路径(SD卡或root)
     * @param fileName 文件名
     * @throws IOException
     */
    public static byte[] readFileToByteArray(String dirPath, String fileName)
            throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            fis = new FileInputStream(new File(dirPath, fileName));
            baos = new ByteArrayOutputStream();
            while ((len = (fis.read(buffer))) != -1) {
                baos.write(buffer, 0, len);
            }

            return baos.toByteArray();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    LogUtils.d(e.getMessage());
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LogUtils.d(e.getMessage());
                }
            }
        }
    }

    /**
     * 删除图片附件文件夹下的文件
     */
    public void delFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    /**
     * 删除图片附件的文件夹
     */
    public static boolean deleteDir(String path) {

        try {
            File file = new File(path);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (String aFilelist : filelist) {
                    File delfile = new File(path + aFilelist);
                    if (!delfile.isDirectory()) {
                        delfile.delete();

                    } else if (delfile.isDirectory()) {

                        deleteDir(path + aFilelist + "/");
                    }
                }
                file.delete();
            }
        } catch (NullPointerException e) {
            LogUtils.d("deletefile() Exception:" + e.getMessage());
        } catch (Exception e) {
            LogUtils.d("deletefile() Exception:" + e.getMessage());
        }
        return true;
    }

    /**
     * 附件－添加保存图片
     *
     * @param bm
     * @param picName
     */
    public static String saveBitmap(Bitmap bm, String picPath, String picName) {
        LogUtils.d("保存图片");
        FileOutputStream out = null;
        try {
            // 创建一个存放附件的文件夹
            picName = picName + ".png";

            String fullpath = picPath; // 取包含存储卡路径的文件路径
            File dir = new File(fullpath); // 判断文件夹是否存在
            boolean dirExist = dir.exists();
            if (!dirExist) {
                dirExist = dir.mkdirs(); // 文件夹不存在，则创建文件夹
            }
            if (!dirExist) {
                String message = App.getApp().getResources().getString(R.string.dir_not_exists);
                message = String.format(message, fullpath);
                ToastMgr.show(message);

            }

            File f = new File(fullpath, picName);
            if (f.exists()) {
                f.delete();
            }
            out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            if (out != null) {
                out.flush();
                out.close();
            }

            LogUtils.d("已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {

            }
        }
        return picPath + picName;
    }

    /**
     * 附件－添加保存图片
     */
    public static String saveBitmap(Context context, Bitmap bm, String picName) {
        String path = "";
        try {
            picName = picName + ".png";

            File dir = new File(FileUtil.getPhotoDir()); // 判断文件夹是否存在
            boolean dirExist = false;
            if (dir.exists()) {
                dirExist = true;
            } else {
                dirExist = dir.mkdirs(); // 文件夹不存在，则创建文件夹
            }
            if (!dirExist) {
                String message = App.getApp().getResources().getString(R.string.dir_not_exists);
                message = String.format(message, FileUtil.getPhotoDir());
                ToastMgr.show(message);
                throw new IOException(message);
            }

            File f = new File(FileUtil.getPhotoDir(), picName);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            boolean isSuccess = bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getPath(), f.getPath(), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri datauri = Uri.fromFile(f);
            intent.setData(datauri);
            context.sendBroadcast(intent);

            if (isSuccess) {
                path = FileUtil.getPhotoDir() + picName;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return path;
    }


    /**
     * 清除WebView缓存
     */
    public static void clearWebViewCache(Context context) {
        // //清理Webview缓存数据库
        // try {
        // deleteDatabase("webview.db");
        // deleteDatabase("webviewCache.db");
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // WebView 缓存文件
        File appCacheDir = new File(context.getFilesDir().getAbsolutePath() + WEBVIEW_CACHE_DIRNAME);
        // Log.e(TAG, "appCacheDir path="+appCacheDir.getAbsolutePath());
        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath()
                + "/webviewCache");
        // Log.e(TAG,
        // "webviewCacheDir path="+webviewCacheDir.getAbsolutePath());
        // 删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        // 删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFile(file1);
                    }
                }
                file.delete();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    //获取语音视频长度
    public static long getVideoAudioTime(String filepath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filepath);
            mediaPlayer.prepare();
        } catch (Exception e) {

        }
        return mediaPlayer.getDuration();
    }

    public static void closeInputStream(InputStream istream) {

        if (istream != null) {
            try {
                istream.close();
                istream = null;
            } catch (IOException e) {
            }
        }

    }


    public static void closeOutputStream(OutputStream ostream) {

        if (ostream != null) {
            try {
                ostream.flush();
                ostream.close();
                ostream = null;
            } catch (IOException e) {
            }
        }

    }


}
