package com.example.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/17.
 */

public class ImageLoaderUtils {


    private static ImageLoaderUtils imageLoaderUtils=new ImageLoaderUtils();
    public static LruCache<String,Bitmap> lruCache;
    private static DiskLruCache diskLruCache;
    private File file;
    private ImageLoaderUtils(){

        //手机内存
        int i = (int) ((Runtime.getRuntime().maxMemory() / 8));
        //初始化内存缓存
        lruCache=new LruCache<String, Bitmap>(i){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return  value.getByteCount();
            }
        };

        //本地缓存初始化
        file=new File(Environment.getExternalStorageDirectory()+"imgs");
        try {
            diskLruCache=DiskLruCache.open(file,App.versionCode,0,5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ImageLoaderUtils getInstance(){
        return imageLoaderUtils;
    }

    //内存缓存图片
    public static void memory_saveImg(String url,byte[] bytes){
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        if(url!=null){
            lruCache.put(url,bitmap);
        }
    }


    public static void disk_saveImg(String url,byte[] bytes){
        Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        if(url!=null){
            try {
                DiskLruCache.Editor edit = diskLruCache.edit(url);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //获取内存图片
    public static Bitmap memory_getImg(String url){
        if(url!=null){
            return lruCache.get(url);
        }

        return null;
    }

    //删掉内存图片
    public static void memory_removeImg(String url){
        if(url!=null){
           lruCache.remove(url);
        }
    }
}
