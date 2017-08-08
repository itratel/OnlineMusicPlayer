package com.marsXTU.music.service;

import com.marsXTU.music.model.Music;

/**
 * 播放进度监听器
 * Created by whd on 2016/3/28.
 */
public interface OnPlayerEventListener {
    /**
     * 更新进度
     */
    void onPublish(int progress);

    /**
     * 切换歌曲
     */
    void onChange(Music music);

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 继续播放
     */
    void onPlayerResume();
}
