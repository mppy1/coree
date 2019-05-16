package com.jew.coree.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.jew.coree.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pengzhu on 2019/1/4/004.
 */
public class ThermalInfoUtil {

    public static List<String> getThermalInfo() {
        List<String> result = new ArrayList<>();
        BufferedReader br = null;

        try {
            File dir = new File("/sys/class/thermal/");

            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            final int SIZE = files.length;
            String line = null;
            String type = null;
            String temp = null;
            for (int i = 0; i < SIZE; i++) {
                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/type"));
                line = br.readLine();
                if (line != null) {
                    type = line;
                }

                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/temp"));
                line = br.readLine();
                if (line != null) {
                    long temperature = Long.parseLong(line);
                    if (temperature < 0) {
                        temp = "Unknow";
                    } else {
                        temp = (float) (temperature / 1000.0) + "";
                    }

                }

                result.add(type + " : " + temp);
            }

            br.close();
        } catch (FileNotFoundException e) {
            result.add(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static String getCpuThermal() {
        String result = "0";
        BufferedReader br = null;

        try {
            File dir = new File("/sys/class/thermal/");

            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            final int SIZE = files.length;
            String line = null;
            String type = null;
            String temp = null;
            for (int i = 0; i < SIZE; i++) {
                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/type"));
                line = br.readLine();
                if (line != null) {
                    type = line;
                }

                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/temp"));
                line = br.readLine();
                if (line != null) {
                    long temperature = Long.parseLong(line);
                    if (temperature < 0) {
                        temp = "Unknow";
                    } else {
                        temp = (float) (temperature / 1000.0) + "";
                    }

                }
                if (type.equals("mtktscpu")) {
                    result = temp;
                }
            }

            br.close();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    /**
     * 获取当前CPU占比
     * 在实际测试中发现，有的手机会隐藏CPU状态，不会完全显示所有CPU信息，例如MX5，所有建议只做参考
     *
     * @return
     */
    public static String getCPURateDesc() {
        String path = "/proc/stat";// 系统CPU信息文件
        long totalJiffies[] = new long[2];
        long totalIdle[] = new long[2];
        int firstCPUNum = 0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Pattern pattern = Pattern.compile(" [0-9]+");
        for (int i = 0; i < 2; i++) {
            totalJiffies[i] = 0;
            totalIdle[i] = 0;
            try {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum = 0;
                String str;
                while ((str = bufferedReader.readLine()) != null && (i == 0 || currentCPUNum < firstCPUNum)) {
                    if (str.toLowerCase().startsWith("cpu")) {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find()) {
                            try {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {//空闲时间为该行第4条栏目
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (i == 0) {
                        firstCPUNum = currentCPUNum;
                        try {//暂停50毫秒，等待系统更新信息。
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        double rate = -1;
        if (totalJiffies[0] > 0 && totalJiffies[1] > 0 && totalJiffies[0] != totalJiffies[1]) {
            rate = 1.0 * ((totalJiffies[1] - totalIdle[1]) - (totalJiffies[0] - totalIdle[0])) / (totalJiffies[1] - totalJiffies[0]);
        }

        return String.format("%.2f", rate * 100);
    }


    //获取可用运存大小

    public static long getAvailMemory() {
// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) App.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
//mi.availMem; 当前系统的可用内存

//return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        System.out.println("可用内存---->>>" + mi.availMem / (1024 * 1024));
        return mi.availMem / (1024 * 1024);
    }

//获取总运存大小

    public static long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
//return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
        System.out.println("总运存--->>>" + initial_memory / (1024 * 1024));
        return initial_memory / (1024 * 1024);
    }


}
