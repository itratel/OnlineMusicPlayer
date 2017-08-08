package com.marsXTU.music.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marsXTU.music.adapter.FragmentAdapter;
import com.marsXTU.music.fragment.PlayFragment;
import com.marsXTU.music.fragment.SongListFragment;
import com.marsXTU.music.model.Music;
import com.marsXTU.music.service.OnPlayerEventListener;
import com.marsXTU.music.service.PlayService;
import com.marsXTU.music.utils.CoverLoader;
import com.marsXTU.music.utils.Extras;

import butterknife.Bind;
import com.marsXTU.music.R;

import com.marsXTU.music.executor.NaviMenuExecutor;
import com.marsXTU.music.fragment.LocalMusicFragment;

/**
 * create by whd 2016/4/7
 */
public class MusicActivity extends BaseActivity implements View.OnClickListener, OnPlayerEventListener,
        NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view) NavigationView navigationView;//导航视图
    @Bind(R.id.iv_menu) ImageView ivMenu; //菜单图片

    @Bind(R.id.tv_local_music) TextView tvLocalMusic;//本地音乐
    @Bind(R.id.tv_online_music) TextView tvOnlineMusic;//在线音乐

    @Bind(R.id.viewpager) ViewPager mViewPager;//顶部的viewpager
    @Bind(R.id.fl_play_bar) FrameLayout flPlayBar;//底部微型播放界面
    @Bind(R.id.iv_play_bar_cover) ImageView ivPlayBarCover;//专辑图片
    @Bind(R.id.tv_play_bar_title) TextView tvPlayBarTitle;//显示音乐名称
    @Bind(R.id.tv_play_bar_artist) TextView tvPlayBarArtist;//显示对应歌手
    @Bind(R.id.iv_play_bar_play) ImageView ivPlayBarPlay;//播放
    @Bind(R.id.iv_play_bar_next) ImageView ivPlayBarNext;//下一首
    @Bind(R.id.pb_play_bar) ProgressBar mProgressBar;//上一首
    @Bind(R.id.iv_online_search) ImageView ivOnlineSearch;//在线搜索

    private View vNavigationHeader; //侧滑菜单的顶部视图

    private LocalMusicFragment mLocalMusicFragment;
    private SongListFragment mSongListFragment;

    private PlayFragment mPlayFragment;//正在播放fragment
    private PlayService mPlayService; //播放服务
    private AudioManager mAudioManager;
    private boolean mIsPlayFragmentShow = false;//是否让播放页面显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        bindService();
    }

    @Override
    protected void onDestroy() {
        unbindService(mPlayServiceConnection);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        bindService(intent, mPlayServiceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((PlayService.PlayBinder) service).getService();
            mPlayService.setOnPlayEventListener(MusicActivity.this);
            init();
            parseIntent(getIntent());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void init() {
        setupView();
//        registerReceiver();
        onChange(mPlayService.getPlayingMusic());
    }

    @Override
    protected void setListener() {
        ivMenu.setOnClickListener(this);
        tvLocalMusic.setOnClickListener(this);
        tvOnlineMusic.setOnClickListener(this);
        ivOnlineSearch.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        flPlayBar.setOnClickListener(this);
        ivPlayBarPlay.setOnClickListener(this);
        ivPlayBarNext.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 创建视图
     */
    private void setupView() {
        // 添加导航头部视图
        vNavigationHeader = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);
        navigationView.addHeaderView(vNavigationHeader);

        // 创建viewpager
        mLocalMusicFragment = new LocalMusicFragment();
        mSongListFragment = new SongListFragment();

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(mLocalMusicFragment);
        adapter.addFragment(mSongListFragment);

        mViewPager.setAdapter(adapter);
        tvLocalMusic.setSelected(true);
    }


//    private void registerReceiver() {
//        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//    }

    private void parseIntent(Intent intent) {
        if (intent.hasExtra(Extras.FROM_NOTIFICATION)) {
            showPlayingFragment();
        }
    }

    /**
     * 更新播放进度
     */
    @Override
    public void onPublish(int progress) {
        mProgressBar.setProgress(progress);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPublish(progress);
        }
    }

    @Override
    public void onChange(Music music) {
        onPlay(music);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onChange(music);
        }
    }

    @Override
    public void onPlayerPause() {
        ivPlayBarPlay.setSelected(false);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlayerPause();
        }
    }

    @Override
    public void onPlayerResume() {
        ivPlayBarPlay.setSelected(true);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlayerResume();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.tv_local_music:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tv_online_music:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.iv_online_search:
                startActivity(new Intent(this,SearchMusicActivity.class));
                break;
            case R.id.fl_play_bar:
                showPlayingFragment();
                break;
            case R.id.iv_play_bar_play:
                play();
                break;
            case R.id.iv_play_bar_next:
                next();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        drawerLayout.closeDrawers();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                item.setChecked(false);
            }
        }, 500);
        return NaviMenuExecutor.onNavigationItemSelected(item, this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tvLocalMusic.setSelected(true);
            tvOnlineMusic.setSelected(false);
        } else {
            tvLocalMusic.setSelected(false);
            tvOnlineMusic.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void onPlay(Music music) {
        if (music == null) {
            return;
        }
        Bitmap cover;
        if (music.getCover() == null) {
            cover = CoverLoader.getInstance().loadThumbnail(music.getCoverUri());
        } else {
            cover = music.getCover();
        }
        ivPlayBarCover.setImageBitmap(cover);
        tvPlayBarTitle.setText(music.getTitle());
        tvPlayBarArtist.setText(music.getArtist());
        if (getPlayService().isPlaying()) {
            ivPlayBarPlay.setSelected(true);
        } else {
            ivPlayBarPlay.setSelected(false);
        }
        mProgressBar.setMax((int) music.getDuration());
        mProgressBar.setProgress(0);

        if (mLocalMusicFragment != null && mLocalMusicFragment.isResume()) {
            mLocalMusicFragment.onItemPlay();
        }
    }

    private void play() {
        getPlayService().playPause();
    }

    private void next() {
        getPlayService().next();
    }

    public PlayService getPlayService() {
        return mPlayService;
    }

    private void showPlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new PlayFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commit();
        mIsPlayFragmentShow = true;
    }

    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commit();
        mIsPlayFragmentShow = false;
    }

    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && mIsPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        moveTaskToBack(false);
    }
}
