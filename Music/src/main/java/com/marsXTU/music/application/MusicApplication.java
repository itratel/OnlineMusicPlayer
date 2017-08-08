package com.marsXTU.music.application;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.support.v4.util.LongSparseArray;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.marsXTU.music.executor.CrashHandler;
import com.marsXTU.music.utils.ToastUtils;

import com.marsXTU.music.utils.Preferences;

/**
 * 自定义Application
 * Created by whd on 2016/3/7.
 */
public class MusicApplication extends Application {
    private static MusicApplication sInstance;
    private LongSparseArray<String> mDownloadList = new LongSparseArray<>();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ToastUtils.setContext(this);
        Preferences.setContext(this);
        CrashHandler.getInstance().init();
        initImageLoader();
    }

    public static MusicApplication getInstance() {
        return sInstance;
    }

    public static boolean isDebugMode() {
        ApplicationInfo info = getInstance().getApplicationInfo();
        return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public LongSparseArray<String> getDownloadList() {
        return mDownloadList;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(configuration);
    }
}
