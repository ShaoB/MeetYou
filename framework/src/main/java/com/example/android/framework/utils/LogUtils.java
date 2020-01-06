package com.example.android.framework.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.framework.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {

    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");


    public static void i(String tex){
        if(BuildConfig.LOG_DEBUG){
            if(!TextUtils.isEmpty(tex)){
                Log.i(BuildConfig.LOG_TAG,tex);
                writeToFile(tex);
            }
        }
    }

    public static void e(String tex){
        if(BuildConfig.LOG_DEBUG){
            if(!TextUtils.isEmpty(tex)){
                Log.e(BuildConfig.LOG_TAG,tex);
                writeToFile(tex);
            }
        }
    }

    /*
        log日志写入文件  别忘了加权限
     */
    private static void writeToFile(String txt){
        //保存文件的路径
        String fileName = "/sdcard/Meet/Meet.txt";
        //时间 + 内容
        String log = mSimpleDateFormat.format(new Date()) + "  " + txt + "\n";
        //检查父路径
        File file = new File("/sdcard/Meet/");
        if(!file.exists()){
            file.mkdir();
        }
        //开始写入
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = new FileOutputStream(fileName, true);
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(fileOutputStream, Charset.forName("gbk"))
            );
            bufferedWriter.write(log);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
