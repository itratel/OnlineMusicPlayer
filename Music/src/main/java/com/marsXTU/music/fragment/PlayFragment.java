package com.marsXTU.music.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.marsXTU.music.R;
import com.marsXTU.music.adapter.PlayPagerAdapter;
import com.marsXTU.music.enums.MusicTypeEnum;
import com.marsXTU.music.enums.PlayModeEnum;
import com.marsXTU.music.model.Music;
import com.marsXTU.music.utils.Actions;
import com.marsXTU.music.utils.Constants;
import com.marsXTU.music.utils.CoverLoader;
import com.marsXTU.music.utils.FileUtils;
import com.marsXTU.music.utils.ImageUtils;
import com.marsXTU.music.utils.Preferences;
import com.marsXTU.music.utils.ScreenUtils;
import com.marsXTU.music.utils.ToastUtils;
import com.marsXTU.music.widget.AlbumCoverView;
import com.marsXTU.music.widget.IndicatorLayout;
import com.marsXTU.music.widget.LrcView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * 正在播放界面
 * Created by whd on 2016/3/14.
 */
public class PlayFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, SeekBar.OnSeekBarChangeListener {
    @Bind(R.id.ll_content) LinearLayout llContent; //整个播放布局
    @Bind(R.id.iv_play_page_bg) ImageView ivPlayingBg; //播放界面背景图
    @Bind(R.id.iv_back) ImageView ivBack; //返回
    @Bind(R.id.tv_title) TextView tvTitle; //歌名
    @Bind(R.id.tv_artist) TextView tvArtist; //歌手
    @Bind(R.id.vp_play_page) ViewPager vpPlay; //viewpager
    @Bind(R.id.il_indicator) IndicatorLayout ilIndicator; //圆点
    @Bind(R.id.sb_progress) SeekBar sbProgress;//进度条
    @Bind(R.id.tv_current_time) TextView tvCurrentTime; //当前时间
    @Bind(R.id.tv_total_time) TextView tvTotalTime; //歌曲总时间
    @Bind(R.id.iv_mode) ImageView ivMode; //模式切换
    @Bind(R.id.iv_play) ImageView ivPlay; //播放
    @Bind(R.id.iv_next) ImageView ivNext; //下一曲
    @Bind(R.id.iv_prev) ImageView ivPrev; //上一曲
    private AlbumCoverView mAlbumCoverView;
    private LrcView mLrcViewSingle;
    private LrcView mLrcViewFull;
    private SeekBar sbVolume;
    private AudioManager mAudioManager;
    private List<View> mViewPagerContent;
    private int mLastProgress; //上一个进度

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    protected void init() {
        initSystemBar();
        initViewPager();
        ilIndicator.create(mViewPagerContent.size());
        initPlayMode();
        onChange(getPlayService().getPlayingMusic());
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Actions.VOLUME_CHANGED_ACTION);
        getActivity().registerReceiver(mVolumeReceiver, filter);
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(this);
        ivMode.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this);
        sbVolume.setOnSeekBarChangeListener(this);
        vpPlay.setOnPageChangeListener(this);
    }

    /**
     *初始化系统
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int top = ScreenUtils.getSystemBarHeight(getActivity());
            llContent.setPadding(0, top, 0, 0);
        }
    }

    /**
     *初始化viewpager
     */
    private void initViewPager() {
        View coverView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_play_page_cover, null);
        View lrcView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_play_page_lrc, null);
        mAlbumCoverView = (AlbumCoverView) coverView.findViewById(R.id.album_cover_view);
        mLrcViewSingle = (LrcView) coverView.findViewById(R.id.lrc_view_single);
        mLrcViewFull = (LrcView) lrcView.findViewById(R.id.lrc_view_full);
        sbVolume = (SeekBar) lrcView.findViewById(R.id.sb_volume);
        mAlbumCoverView.initNeedle(getPlayService().isPlaying());
        //初始化音量
        initVolume();

        mViewPagerContent = new ArrayList<>(2);
        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);
        vpPlay.setAdapter(new PlayPagerAdapter(mViewPagerContent));
    }

    private void initVolume() {
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        sbVolume.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        sbVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    /**
     * 初始化播放模式
     */
    private void initPlayMode() {
        int mode = Preferences.getPlayMode(0);
        ivMode.setImageLevel(mode);
    }

    /**
     * 更新播放进度
     */
    public void onPublish(int progress) {
        sbProgress.setProgress(progress);
        if (mLrcViewSingle.hasLrc()) {
            mLrcViewSingle.updateTime(progress);
            mLrcViewFull.updateTime(progress);
        }
        //更新当前播放时间
        if (progress - mLastProgress >= 1000) {
            tvCurrentTime.setText(formatTime(progress));
            mLastProgress = progress;
        }
    }

    public void onChange(Music music) {
        onPlay(music);
    }

    public void onPlayerPause() {
        ivPlay.setSelected(false);
        mAlbumCoverView.pause();
    }

    public void onPlayerResume() {
        ivPlay.setSelected(true);
        mAlbumCoverView.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_mode:
                switchPlayMode();
                break;
            case R.id.iv_play:
                play();
                break;
            case R.id.iv_next:
                next();
                break;
            case R.id.iv_prev:
                prev();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        ilIndicator.setCurrent(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == sbProgress) {
            if (getPlayService().isPlaying() || getPlayService().isPause()) {
                int progress = seekBar.getProgress();
                getPlayService().seekTo(progress);
                mLrcViewSingle.onDrag(progress);
                mLrcViewFull.onDrag(progress);
                tvCurrentTime.setText(formatTime(progress));
                mLastProgress = progress;
            } else {
                seekBar.setProgress(0);
            }
        } else if (seekBar == sbVolume) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(),
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }
    }

    private void onPlay(Music music) {
        if (music == null) {
            return;
        }
        tvTitle.setText(music.getTitle());
        tvArtist.setText(music.getArtist());
        sbProgress.setMax((int) music.getDuration());
        sbProgress.setProgress(0);
        mLastProgress = 0;
        tvCurrentTime.setText(R.string.play_time_start);
        tvTotalTime.setText(formatTime(music.getDuration()));
        setCoverAndBg(music);
        setLrc(music);
        if (getPlayService().isPlaying()) {
            ivPlay.setSelected(true);
            mAlbumCoverView.start();
        } else {
            ivPlay.setSelected(false);
            mAlbumCoverView.pause();
        }
    }

    private void play() {
        getPlayService().playPause();
    }

    private void next() {
        getPlayService().next();
    }

    private void prev() {
        getPlayService().prev();
    }

    /**
     * 切换播放模式
     */
    private void switchPlayMode() {
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode(0));
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                ToastUtils.show(R.string.mode_shuffle);
                break;
            case SHUFFLE:
                mode = PlayModeEnum.ONE;
                ToastUtils.show(R.string.mode_one);
                break;
            case ONE:
                mode = PlayModeEnum.LOOP;
                ToastUtils.show(R.string.mode_loop);
                break;
        }
        Preferences.savePlayMode(mode.value());
        initPlayMode();
    }

    private void onBackPressed() {
        getActivity().onBackPressed();
        ivBack.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivBack.setEnabled(true);
            }
        }, 300);
    }

    private void setCoverAndBg(Music music) {
        if (music.getType() == MusicTypeEnum.LOCAL) {
            mAlbumCoverView.setCoverBitmap(CoverLoader.getInstance().loadRound(music.getCoverUri()));
            ivPlayingBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music.getCoverUri()));
        } else {
            if (music.getCover() == null) {
                mAlbumCoverView.setCoverBitmap(CoverLoader.getInstance().loadRound(null));
                ivPlayingBg.setImageResource(R.drawable.ic_play_page_default_bg);
            } else {
                Bitmap cover = ImageUtils.resizeImage(music.getCover(), ScreenUtils.getScreenWidth() / 2, ScreenUtils.getScreenWidth() / 2);
                cover = ImageUtils.createCircleImage(cover);
                mAlbumCoverView.setCoverBitmap(cover);
                Bitmap bg = ImageUtils.blur(music.getCover(), ImageUtils.BLUR_RADIUS);
                ivPlayingBg.setImageBitmap(bg);
            }
        }
    }

    private void setLrc(Music music) {
        String lrcPath;
        if (music.getType() == MusicTypeEnum.LOCAL) {
            lrcPath = FileUtils.getLrcDir() + music.getFileName().replace(Constants.FILENAME_MP3, Constants.FILENAME_LRC);
        } else {
            lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
        }
        mLrcViewSingle.loadLrc(lrcPath);
        mLrcViewFull.loadLrc(lrcPath);
    }

    private String formatTime(long lTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        Date date = new Date(lTime);
        return sdf.format(date);
    }

    private BroadcastReceiver mVolumeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!Actions.VOLUME_CHANGED_ACTION.equals(intent.getAction())) {
                return;
            }
            sbVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
    };

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mVolumeReceiver);
        super.onDestroy();
    }
}
