package com.example.gcy.photogallery;

import android.os.HandlerThread;
import android.util.Log;

/**
 * Created by gcy on 2017/11/29.
 */

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";

    private Boolean mHasQuit = false;

    public ThumbnailDownloader(){
        super(TAG);
    }

    @Override
    public boolean quit(){
        mHasQuit = true;
        return super.quit();
    }

    public void quequThumbnail(T target, String url){
        Log.i(TAG, "Got a URL: " + url);
    }
}
