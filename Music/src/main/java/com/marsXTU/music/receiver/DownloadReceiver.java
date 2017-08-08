package com.marsXTU.music.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.marsXTU.music.application.MusicApplication;
import com.marsXTU.music.utils.ToastUtils;

import com.marsXTU.music.R;

/**
 * 下载完成广播接收器
 * Created by whd on 2016/3/26.
 */
public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        String title = MusicApplication.getInstance().getDownloadList().get(id);
        if (TextUtils.isEmpty(title)) {
            return;
        }
        ToastUtils.show(context.getString(R.string.download_success, title));
    }
}
