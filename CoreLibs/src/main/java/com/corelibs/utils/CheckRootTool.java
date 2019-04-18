package com.corelibs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by pengzhu on 2018/9/4/004.
 */
public class CheckRootTool {

    public static boolean isDeviceRooted() {
        if (checkDeviceDebuggable()){return true;}//check buildTags
        if (checkSuperuserApk()){return true;}//Superuser.apk
        //if (checkRootPathSU()){return true;}//find su in some path
        //if (checkRootWhichSU()){return true;}//find su use 'which'
        if (checkBusybox()){return true;}//find su use 'which'
        if (checkAccessRootData()){return true;}//find su use 'which'
        if (checkGetRootAuth()){return true;}//exec su

        return false;
    }

    public static boolean checkDeviceDebuggable(){
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            LogUtils.i("buildTags="+buildTags);
            return true;
        }
        return false;
    }

    public static boolean checkSuperuserApk(){
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                LogUtils.i("/system/app/Superuser.apk exist");
                return true;
            }
        } catch (Exception e) { }
        return false;
    }

    public static boolean checkRootPathSU()
    {
        File f=null;
        final String kSuSearchPaths[]={"/system/bin/","/system/xbin/","/system/sbin/","/sbin/","/vendor/bin/"};
        try{
            for(int i=0;i<kSuSearchPaths.length;i++)
            {
                f=new File(kSuSearchPaths[i]+"su");
                if(f!=null&&f.exists())
                {
                    LogUtils.i("find su in : "+kSuSearchPaths[i]);
                    return true;
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkRootWhichSU() {
        String[] strCmd = new String[] {"/system/xbin/which","su"};
        ArrayList<String> execResult = executeCommand(strCmd);
        if (execResult != null){
            LogUtils.i("execResult="+execResult.toString());
            return true;
        }else{
            LogUtils.i("execResult=null");
            return false;
        }
    }

    public static ArrayList<String> executeCommand(String[] shellCmd){
        String line = null;
        ArrayList<String> fullResponse = new ArrayList<String>();
        Process localProcess = null;
        try {
            LogUtils.i("to shell exec which for find su :");
            localProcess = Runtime.getRuntime().exec(shellCmd);
        } catch (Exception e) {
            return null;
        }
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
        try {
            while ((line = in.readLine()) != null) {
                LogUtils.i("–> Line received: " + line);
                fullResponse.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.i("–> Full response was: " + fullResponse);
        return fullResponse;
    }

    public static synchronized boolean checkGetRootAuth()
    {
        Process process = null;
        DataOutputStream os = null;
        try
        {
            LogUtils.i("to exec su");
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            LogUtils.i("exitValue="+exitValue);
            if (exitValue == 0)
            {
                return true;
            } else
            {
                return false;
            }
        } catch (Exception e)
        {
            LogUtils.i("Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        } finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
                process.destroy();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static synchronized boolean checkBusybox()
    {
        try
        {
            LogUtils.i("to exec busybox df");
            String[] strCmd = new String[] {"busybox","df"};
            ArrayList<String> execResult = executeCommand(strCmd);
            if (execResult != null){
                LogUtils.i("execResult="+execResult.toString());
                return true;
            }else{
                LogUtils.i("execResult=null");
                return false;
            }
        } catch (Exception e)
        {
            LogUtils.i("Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        }
    }

    public static synchronized boolean checkAccessRootData()
    {
        try
        {
            LogUtils.i("to write /data");
            String fileContent = "test_ok";
            Boolean writeFlag = writeFile("/data/su_test",fileContent);
            if (writeFlag){
                LogUtils.i("write ok");
            }else{
                LogUtils.i("write failed");
            }

            LogUtils.i("to read /data");
            String strRead = readFile("/data/su_test");
            LogUtils.i("strRead="+strRead);
            if(fileContent.equals(strRead)){
                return true;
            }else {
                return false;
            }
        } catch (Exception e)
        {
            LogUtils.i("Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        }
    }

    //写文件
    public static Boolean writeFile(String fileName,String message){
        try{
            FileOutputStream fout = new FileOutputStream(fileName);
            byte [] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //读文件
    public static String readFile(String fileName){
        File file = new File(fileName);
        try {
            FileInputStream fis= new FileInputStream(file);
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            while((len=fis.read(bytes))>0){
                bos.write(bytes, 0, len);
            }
            String result = new String(bos.toByteArray());
            LogUtils.i(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
