package com.marsXTU.music.executor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.marsXTU.music.application.MusicApplication;
import com.marsXTU.music.callback.JsonCallback;
import com.marsXTU.music.model.JDownloadInfo;
import com.marsXTU.music.model.JOnlineMusic;
import com.marsXTU.music.utils.Constants;
import com.marsXTU.music.utils.FileUtils;
import com.marsXTU.music.utils.NetworkUtils;
import com.marsXTU.music.utils.Preferences;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import com.marsXTU.music.R;
import okhttp3.Call;

/**
 * 下载音乐
 * Created by whd on 2016/3/28.
 */
public abstract class DownloadOnlineMusic {
    private Context mContext;
    private JOnlineMusic mJOnlineMusic;

    public DownloadOnlineMusic(Context context, JOnlineMusic jOnlineMusic) {
        mContext = context;
        mJOnlineMusic = jOnlineMusic;
    }

    public void execute() {
        checkNetwork();
    }

    private void checkNetwork() {
        boolean mobileNetworkDownload = Preferences.enableMobileNetworkDownload(false);
        if (NetworkUtils.isActiveNetworkMobile(mContext) && !mobileNetworkDownload) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.tips);
            builder.setMessage(R.string.download_tips);
            builder.setPositiveButton(R.string.download_tips_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    download();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            Dialog dialog = builder.create();
            //
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            download();
        }
    }

    private void download() {
        onPrepare();
        //得到DownloadManager对象
        final DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        // 获取歌曲下载链接
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_DOWNLOAD_MUSIC)
                .addParams(Constants.PARAM_SONG_ID, mJOnlineMusic.getSong_id())
                .build()
                .execute(new JsonCallback<JDownloadInfo>(JDownloadInfo.class) {
                    @Override
                    public void onResponse(final JDownloadInfo response) {
                        Uri uri = Uri.parse(response.getBitrate().getFile_link());
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        //获取音乐文件的名字和歌手
                        String mp3FileName = FileUtils.getMp3FileName(mJOnlineMusic.getArtist_name(), mJOnlineMusic.getTitle());
                        //设置下载路径和文件名
                        request.setDestinationInExternalPublicDir(FileUtils.getRelativeMusicDir(), mp3FileName);
                        //设置下载文件的mime类型
                        request.setMimeType(MimeTypeMap.getFileExtensionFromUrl(response.getBitrate().getFile_link()));
                        //设置允许被媒体扫描器找到
                        request.allowScanningByMediaScanner();
                        //设置允许使用的网络类型，移动网络与wifi都可以
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                        // 不允许漫游
                        request.setAllowedOverRoaming(false);
                        //将下载请求放入队列
                        long id = downloadManager.enqueue(request);
                        MusicApplication.getInstance().getDownloadList().put(id, mJOnlineMusic.getTitle());
                        onSuccess();
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        onFail(call, e);
                    }
                });
        // 下载歌词
        String lrcFileName = FileUtils.getLrcFileName(mJOnlineMusic.getArtist_name(), mJOnlineMusic.getTitle());
        File lrcFile = new File(FileUtils.getLrcDir() + lrcFileName);
        if (!TextUtils.isEmpty(mJOnlineMusic.getLrclink()) && !lrcFile.exists()) {
            OkHttpUtils.get().url(mJOnlineMusic.getLrclink()).build()
                    .execute(new FileCallBack(FileUtils.getLrcDir(), lrcFileName) {
                        @Override
                        public void inProgress(float v, long l) {

                        }

                        @Override
                        public void inProgress(float progress) {
                        }

                        @Override
                        public void onResponse(File response) {
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                        }
                    });
        }
    }

    public abstract void onPrepare();

    public abstract void onSuccess();

    public abstract void onFail(Call call, Exception e);
}
