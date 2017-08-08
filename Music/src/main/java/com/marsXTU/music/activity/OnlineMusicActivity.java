package com.marsXTU.music.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marsXTU.music.R;
import com.marsXTU.music.adapter.OnMoreClickListener;
import com.marsXTU.music.adapter.OnlineMusicAdapter;
import com.marsXTU.music.callback.JsonCallback;
import com.marsXTU.music.enums.LoadStateEnum;
import com.marsXTU.music.executor.DownloadOnlineMusic;
import com.marsXTU.music.executor.PlayOnlineMusic;
import com.marsXTU.music.executor.ShareOnlineMusic;
import com.marsXTU.music.model.JOnlineMusic;
import com.marsXTU.music.model.JOnlineMusicList;
import com.marsXTU.music.model.Music;
import com.marsXTU.music.model.SongListInfo;
import com.marsXTU.music.service.PlayService;
import com.marsXTU.music.utils.Constants;
import com.marsXTU.music.utils.Extras;
import com.marsXTU.music.utils.FileUtils;
import com.marsXTU.music.utils.ImageUtils;
import com.marsXTU.music.utils.ScreenUtils;
import com.marsXTU.music.utils.ToastUtils;
import com.marsXTU.music.utils.ViewUtils;
import com.marsXTU.music.widget.AutoLoadListView;
import com.marsXTU.music.widget.OnLoadListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
/**
 * create by whd 2016/4/7
 */
public class OnlineMusicActivity extends BaseActivity implements OnItemClickListener, OnMoreClickListener, OnLoadListener {
    @Bind(R.id.lv_online_music_list) AutoLoadListView lvOnlineMusic;
    @Bind(R.id.ll_loading) LinearLayout llLoading;
    @Bind(R.id.ll_load_fail) LinearLayout llLoadFail;

    private View vHeader;
    //歌单信息
    private SongListInfo mListInfo;
    //在线音乐列表
    private JOnlineMusicList mJOnlineMusicList;
    //装有在线音乐的list集合
    private List<JOnlineMusic> mMusicList;
    //在线音乐适配器
    private OnlineMusicAdapter mAdapter;
    //播放服务
    private PlayService mPlayService;
    //对话框
    private ProgressDialog mProgressDialog;
    //
    private int mOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music);
        //
        mListInfo = (SongListInfo) getIntent().getSerializableExtra(Extras.MUSIC_LIST_TYPE);
        setTitle(mListInfo.getTitle());//设置歌单信息的标题

        init();
    }

    private void init() {
        //填充在线音乐歌单列表顶部视图
        vHeader = LayoutInflater.from(this).inflate(R.layout.activity_online_music_list_header, null);
        //创建一个布局的实例，并指定该布局的宽度和高度
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.dp2px(150));
        //将该布局设置为顶部视图中
        vHeader.setLayoutParams(params);
        //为listview添加顶部视图，并且vHeader不能被选择
        lvOnlineMusic.addHeaderView(vHeader, null, false);
        //实例化list集合
        mMusicList = new ArrayList<>();
        //实例化在线音乐适配器
        mAdapter = new OnlineMusicAdapter(this, mMusicList);

        lvOnlineMusic.setAdapter(mAdapter);
        //设置加载监听器
        lvOnlineMusic.setOnLoadListener(this);
        //创建一个进度条对话框
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        //通过视图工具改变加载状态
        ViewUtils.changeViewState(lvOnlineMusic, llLoading, llLoadFail, LoadStateEnum.LOADING);

        bindService();
    }

    @Override
    protected void setListener() {
        lvOnlineMusic.setOnItemClickListener(this);
        mAdapter.setOnMoreClickListener(this);
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
            onLoad();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    /**
     *get请求获取音乐
     * @param offset
     */
    private void getMusic(final int offset) {
        //通过链式去根据自己的需要添加各种参数，
        //最后调用execute(callback)进行执行，传入callback则代表是异步
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_GET_MUSIC_LIST)
                .addParams(Constants.PARAM_TYPE, mListInfo.getType())
                .addParams(Constants.PARAM_SIZE, String.valueOf(Constants.MUSIC_LIST_SIZE))
                .addParams(Constants.PARAM_OFFSET, String.valueOf(offset))
                .build()
                .execute(new JsonCallback<JOnlineMusicList>(JOnlineMusicList.class) {
                    @Override
                    public void onResponse(JOnlineMusicList response) {
                        lvOnlineMusic.onLoadComplete();
                        mJOnlineMusicList = response;
                        if (offset == 0) {
                            initHeader();
                            ViewUtils.changeViewState(lvOnlineMusic, llLoading, llLoadFail, LoadStateEnum.LOAD_SUCCESS);
                        }
                        if (response.getSong_list() == null || response.getSong_list().length == 0) {
                            lvOnlineMusic.setEnable(false);
                            return;
                        }
                        mOffset += Constants.MUSIC_LIST_SIZE;
                        Collections.addAll(mMusicList, response.getSong_list());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        lvOnlineMusic.onLoadComplete();
                        if (e instanceof RuntimeException) {
                            // 歌曲全部加载完成
                            lvOnlineMusic.setEnable(false);
                            return;
                        }
                        if (offset == 0) {
                            ViewUtils.changeViewState(lvOnlineMusic, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
                        } else {
                            ToastUtils.show(R.string.load_fail);
                        }
                    }
                });
    }

    @Override
    public void onLoad() {
        getMusic(mOffset);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        play(mMusicList.get(position - 1));
    }

    @Override
    public void onMoreClick(int position) {
        final JOnlineMusic jOnlineMusic = mMusicList.get(position);
        //实例化对话框
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(mMusicList.get(position).getTitle());
        String path = FileUtils.getMusicDir() + FileUtils.getMp3FileName(jOnlineMusic.getArtist_name(), jOnlineMusic.getTitle());
        File file = new File(path);
        int itemsId = file.exists() ?
                R.array.online_music_dialog_no_download :
                R.array.online_music_dialog;
        dialog.setItems(itemsId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:// 分享
                        share(jOnlineMusic);
                        break;
                    case 1:// 查看歌手信息
                        artistInfo(jOnlineMusic);
                        break;
                    case 2:// 下载
                        download(jOnlineMusic);
                        break;
                }
            }
        });
        dialog.show();
    }

    private void initHeader() {
        final ImageView ivHeaderBg = (ImageView) vHeader.findViewById(R.id.iv_header_bg);
        final ImageView ivCover = (ImageView) vHeader.findViewById(R.id.iv_cover);
        TextView tvTitle = (TextView) vHeader.findViewById(R.id.tv_title);
        TextView tvUpdateDate = (TextView) vHeader.findViewById(R.id.tv_update_date);
        TextView tvComment = (TextView) vHeader.findViewById(R.id.tv_comment);
        tvTitle.setText(mJOnlineMusicList.getBillboard().getName());
        tvUpdateDate.setText(getString(R.string.recent_update, mJOnlineMusicList.getBillboard().getUpdate_date()));
        tvComment.setText(mJOnlineMusicList.getBillboard().getComment());
        ImageSize imageSize = new ImageSize(200, 200);
        ImageLoader.getInstance().loadImage(mJOnlineMusicList.getBillboard().getPic_s640(), imageSize,
                ImageUtils.getDefaultDisplayImageOptions(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                           ivCover.setImageBitmap(loadedImage);
                        ivHeaderBg.setImageBitmap(ImageUtils.blur(loadedImage, ImageUtils.BLUR_RADIUS));
                    }
                });
    }

    private void play(JOnlineMusic jOnlineMusic) {
        new PlayOnlineMusic(this, jOnlineMusic) {

            @Override
            public void onPrepare() {
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(Music music) {
                mProgressDialog.cancel();
                mPlayService.play(music);
                ToastUtils.show(getString(R.string.now_play, music.getTitle()));
            }

            @Override
            public void onFail(Call call, Exception e) {
                mProgressDialog.cancel();
                ToastUtils.show(R.string.unable_to_play);
            }
        }.execute();
    }

    private void share(final JOnlineMusic jOnlineMusic) {
        new ShareOnlineMusic(this, jOnlineMusic.getTitle(), jOnlineMusic.getSong_id()) {
            @Override
            public void onPrepare() {
                mProgressDialog.show();
            }

            @Override
            public void onSuccess() {
                mProgressDialog.cancel();
            }

            @Override
            public void onFail(Call call, Exception e) {
                mProgressDialog.cancel();
            }
        }.execute();
    }

    private void artistInfo(JOnlineMusic jOnlineMusic) {
        ArtistInfoActivity.start(this, jOnlineMusic.getTing_uid());
    }

    private void download(final JOnlineMusic jOnlineMusic) {
        new DownloadOnlineMusic(this, jOnlineMusic) {
            @Override
            public void onPrepare() {
                mProgressDialog.show();
            }

            @Override
            public void onSuccess() {
                mProgressDialog.cancel();
                ToastUtils.show(getString(R.string.now_download, jOnlineMusic.getTitle()));
            }

            @Override
            public void onFail(Call call, Exception e) {
                mProgressDialog.cancel();
                ToastUtils.show(R.string.unable_to_download);
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        unbindService(mPlayServiceConnection);
        super.onDestroy();
    }
}
