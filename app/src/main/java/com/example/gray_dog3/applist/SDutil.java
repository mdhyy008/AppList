package com.example.gray_dog3.applist;



import android.os.*;
import java.io.*;
import android.annotation.*;
import android.icu.text.*;

public class SDutil
{
	/**
     * 判断SD卡是否可用
     *
     * @return true : 可用<br>false : 不可用
     */
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

	/**
     * 获取手机外部总空间大小
     *
     * @return 总大小，字节为单位
     */
    public static String getTotalExternalMemorySize() {
        if (isSDCardEnable()) {
            //获取SDCard根目录
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            double blockSize = stat.getBlockSize();
            double totalBlocks = stat.getBlockCount();
			DecimalFormat df = new DecimalFormat("#.00");
            return df.format(totalBlocks * blockSize/1024/1024/1024);

        } else {
            return "-1";
        }
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return SD卡剩余空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getFreeSpace() {
        if (!isSDCardEnable()) return "sdcard unable!";
		File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        double blockSize, availableBlocks;
        availableBlocks = stat.getAvailableBlocksLong();
        blockSize = stat.getBlockSizeLong();
        double size = availableBlocks * blockSize / 1024;
		DecimalFormat df = new DecimalFormat("#.00");

        return df.format(size/1024/1024);
    }
	
	
	
	
	
	public static String getSystemTotal() {
        if (isSDCardEnable()) {
            //获取SDCard根目录
            File path = new File("/system/");
			StatFs stat = new StatFs(path.getPath());
            double blockSize = stat.getBlockSize();
            double totalBlocks = stat.getBlockCount();
			DecimalFormat df = new DecimalFormat("#.00");
            return df.format(totalBlocks * blockSize/1024/1024/1024);

        } else {
            return "-1";
        }
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return SD卡剩余空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getSystemFree() {
        if (!isSDCardEnable()) return "sdcard unable!";
		File path = new File("/system/");
        StatFs stat = new StatFs(path.getPath());
        double blockSize, availableBlocks;
        availableBlocks = stat.getAvailableBlocksLong();
        blockSize = stat.getBlockSizeLong();
        double size = availableBlocks * blockSize / 1024;
		DecimalFormat df = new DecimalFormat("#.00");

        return df.format(size/1024/1024);
    }
	
	
	public static String getDataTotal() {
        if (isSDCardEnable()) {
            //获取SDCard根目录
            File path = new File("/data/");
			StatFs stat = new StatFs(path.getPath());
            double blockSize = stat.getBlockSize();
            double totalBlocks = stat.getBlockCount();
			DecimalFormat df = new DecimalFormat("#.00");
            return df.format(totalBlocks * blockSize/1024/1024/1024);

        } else {
            return "-1";
        }
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return SD卡剩余空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getDataFree() {
        if (!isSDCardEnable()) return "sdcard unable!";
		File path = new File("/data/");
        StatFs stat = new StatFs(path.getPath());
        double blockSize, availableBlocks;
        availableBlocks = stat.getAvailableBlocksLong();
        blockSize = stat.getBlockSizeLong();
        double size = availableBlocks * blockSize / 1024;
		DecimalFormat df = new DecimalFormat("#.00");

        return df.format(size/1024/1024);
    }
	
	
}


