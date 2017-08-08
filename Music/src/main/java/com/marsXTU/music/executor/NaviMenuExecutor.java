package com.marsXTU.music.executor;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.marsXTU.music.activity.AboutActivity;
import com.marsXTU.music.activity.MusicActivity;
import com.marsXTU.music.activity.SettingActivity;
import com.marsXTU.music.service.PlayService;

import com.marsXTU.music.R;

/**
 * 导航菜单执行器
 * Created by whd on 2016/3/30.
 */
public class NaviMenuExecutor {

    public static boolean onNavigationItemSelected(MenuItem item, Context context) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                startActivity(context, SettingActivity.class);
                return true;
            case R.id.action_about:
                startActivity(context, AboutActivity.class);
                return true;
            case R.id.action_exit:
                exit(context);
                return true;
        }
        return false;
    }

    private static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    private static void exit(Context context) {
        if (context instanceof MusicActivity) {
            MusicActivity activity = (MusicActivity) context;
            PlayService service = activity.getPlayService();
            activity.finish();
            service.stop();
        }
    }
}
