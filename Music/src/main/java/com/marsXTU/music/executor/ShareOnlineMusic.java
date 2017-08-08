package com.marsXTU.music.executor;

import android.content.Context;
import android.content.Intent;

import com.marsXTU.music.callback.JsonCallback;
import com.marsXTU.music.model.JDownloadInfo;
import com.marsXTU.music.utils.Constants;
import com.marsXTU.music.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import com.marsXTU.music.R;
import okhttp3.Call;

/**
 * 分享在线歌曲
 * Created by whd on 2016/3/19.
 */
public abstract class ShareOnlineMusic {
    private Context mContext;
    private String mTitle;
    private String mSongId;

    public ShareOnlineMusic(Context context, String title, String songId) {
        mContext = context;
        mTitle = title;
        mSongId = songId;
    }

    public void execute() {
        share();
    }

    private void share() {
        onPrepare();
        // 获取歌曲播放链接
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_DOWNLOAD_MUSIC)
                .addParams(Constants.PARAM_SONG_ID, mSongId)
                .build()
                .execute(new JsonCallback<JDownloadInfo>(JDownloadInfo.class) {
                    @Override
                    public void onResponse(final JDownloadInfo response) {
                        onSuccess();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.share_music, mContext.getString(R.string.app_name),
                                mTitle, response.getBitrate().getFile_link()));
                        mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.share)));
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        onFail(call, e);
                        ToastUtils.show(R.string.unable_to_share);
                    }
                });
    }

    public abstract void onPrepare();

    public abstract void onSuccess();

    public abstract void onFail(Call call, Exception e);
}
