package com.marsXTU.music.executor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.marsXTU.music.R;
import com.marsXTU.music.callback.JsonCallback;
import com.marsXTU.music.enums.MusicTypeEnum;
import com.marsXTU.music.model.JDownloadInfo;
import com.marsXTU.music.model.JLrc;
import com.marsXTU.music.model.JSearchMusic;
import com.marsXTU.music.model.Music;
import com.marsXTU.music.utils.Constants;
import com.marsXTU.music.utils.FileUtils;
import com.marsXTU.music.utils.NetworkUtils;
import com.marsXTU.music.utils.Preferences;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.Call;

/**
 * 播放搜索的音乐
 * Created by whd on 2016/4/1.
 */
public abstract class PlaySearchedMusic {
    private Context mContext;
    private JSearchMusic.JSong mJSong;
    private int mCounter = 0;

    public PlaySearchedMusic(Context context, JSearchMusic.JSong jSong) {
        mContext = context;
        mJSong = jSong;
    }

    public void execute() {
        checkNetwork();
    }

    private void checkNetwork() {
        boolean mobileNetworkPlay = Preferences.enableMobileNetworkPlay(false);
        if (NetworkUtils.isActiveNetworkMobile(mContext) && !mobileNetworkPlay) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.tips);
            builder.setMessage(R.string.play_tips);
            builder.setPositiveButton(R.string.play_tips_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Preferences.saveMobileNetworkPlay(true);
                    getPlayInfo();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            getPlayInfo();
        }
    }

    private void getPlayInfo() {
        onPrepare();
        String lrcFileName = FileUtils.getLrcFileName(mJSong.getArtistname(), mJSong.getSongname());
        File lrcFile = new File(FileUtils.getLrcDir() + lrcFileName);
        if (lrcFile.exists()) {
            mCounter++;
        }
        final Music music = new Music();
        music.setType(MusicTypeEnum.ONLINE);
        music.setTitle(mJSong.getSongname());
        music.setArtist(mJSong.getArtistname());
        // 获取歌曲播放链接
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_DOWNLOAD_MUSIC)
                .addParams(Constants.PARAM_SONG_ID, mJSong.getSongid())
                .build()
                .execute(new JsonCallback<JDownloadInfo>(JDownloadInfo.class) {
                    @Override
                    public void onResponse(final JDownloadInfo response) {
                        music.setUri(response.getBitrate().getFile_link());
                        music.setDuration(response.getBitrate().getFile_duration() * 1000);
                        mCounter++;
                        if (mCounter == 2) {
                            onSuccess(music);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        onFail(call, e);
                    }
                });
        // 下载歌词
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_LRC)
                .addParams(Constants.PARAM_SONG_ID, mJSong.getSongid())
                .build()
                .execute(new JsonCallback<JLrc>(JLrc.class) {
                    @Override
                    public void onResponse(JLrc response) {
                        if (TextUtils.isEmpty(response.getLrcContent())) {
                            return;
                        }
                        String lrcFileName = FileUtils.getLrcFileName(mJSong.getArtistname(), mJSong.getSongname());
                        saveLrcFile(lrcFileName, response.getLrcContent());
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onAfter() {
                        mCounter++;
                        if (mCounter == 2) {
                            onSuccess(music);
                        }
                    }
                });
    }

    private void saveLrcFile(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(FileUtils.getLrcDir() + fileName);
            writer.flush();
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void onPrepare();

    public abstract void onSuccess(Music music);

    public abstract void onFail(Call call, Exception e);
}
