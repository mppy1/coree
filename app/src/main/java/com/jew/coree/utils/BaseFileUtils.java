package com.jew.coree.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class BaseFileUtils {

    /**
     * SD卡不可读也不可写
     */
    protected static final int EXTERNAL_STORAGE_INVALID = -1;

    /**
     * SD卡可读不可写
     */
    protected static final int EXTERNAL_STORAGE_READ_ONLY = 0;

    /**
     * SD卡可读也可写
     */
    protected static final int EXTERNAL_STORAGE_READ_WRITE = 1;

    /**
     * 获取SD卡当前的读写状态，返回值为{@link #EXTERNAL_STORAGE_INVALID}、
     * {@link #EXTERNAL_STORAGE_READ_ONLY} 及
     * {@link #EXTERNAL_STORAGE_READ_WRITE}中的一个
     *
     * @return EXTERNAL_STORAGE_*类型为int
     */
    public static int getExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            return EXTERNAL_STORAGE_READ_WRITE;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            return EXTERNAL_STORAGE_READ_ONLY;
        } else {
            // Something else is wrong. It may be one of many other states,
            // but all we need to know is we can neither read nor write
            return EXTERNAL_STORAGE_INVALID;
        }
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

        boolean dirExist = false;

        File dir = new File(path); // 判断文件夹是否存在

        if (dir.exists()) {
            dirExist = true;
        } else {
            dirExist = dir.mkdirs(); // 文件夹不存在，则创建文件夹
        }
        addWriteMode(dir.getAbsolutePath());

        if (!dirExist) {
            logD("dir or file not existed:" + dir.getAbsolutePath());
            throw new IOException("dir or file not existed.");
        }

        File file = new File(path, fileName);
        if (!file.exists()) {
            logD("create file:" + file.toString());
            file.createNewFile();
        }
        addWriteMode(file.getAbsolutePath());

        return file;
    }

    /**
     * 获得文件的大小
     *
     * @param fullFilePathName 完整的文件路径及名称
     */
    public static long getFileSize(String fullFilePathName) {
        return new File(fullFilePathName).length();
    }

    /**
     * 删除文件文件夹
     *
     * @param fullPath 完整的路径
     */
    public static boolean deleteFolder(String fullPath) {
        File file = new File(fullPath);
        return deleteFolder(file);
    }

    public static boolean deleteFolder(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            try {
                for (File index : files) {
                    index.delete();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }


    /**
     * 设置文件的权限为可读写
     *
     * @param destDir 目录或文件名
     * @throws IOException
     * @throws InterruptedException
     */
    public static void addWriteMode(String destDir) throws IOException,
            InterruptedException {
        Process p;
        int status;
        p = Runtime.getRuntime().exec("chmod 777 " + destDir);
        status = p.waitFor();
        if (status == 0) {
            // chmod succeed
            logD("Chmod successfully");
        } else {
            // chmod failed
            logD("Chmod failed");
        }
    }

    private static void logD(String message) {
        Log.d("BaseFileUtils", message);

    }
}
