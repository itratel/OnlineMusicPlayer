package com.marsXTU.music.enums;

/**
 * 播放模式
 * Created by whd on 2016/3/14.
 */
public enum PlayModeEnum {
    LOOP(0),  //列表循环
    SHUFFLE(1),  //随机播放
    ONE(2);  //单曲循环

    private int value;

    PlayModeEnum(int value) {
        this.value = value;
    }

    public static PlayModeEnum valueOf(int value) {
        switch (value) {
            case 0:
                return LOOP;  //列表循环
            case 1:
                return SHUFFLE;  //随机播放
            case 2:
                return ONE;   //单曲循环
            default:
                return LOOP;
        }
    }

    public int value() {
        return value;
    }
}
