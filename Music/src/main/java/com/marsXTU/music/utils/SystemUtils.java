package com.marsXTU.music.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by whd on 2016/3/11.
 */
public class SystemUtils {

    public static boolean isStackResumed(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTaskInfos.get(0);
        return runningTaskInfo.numActivities > 1;
    }
}
