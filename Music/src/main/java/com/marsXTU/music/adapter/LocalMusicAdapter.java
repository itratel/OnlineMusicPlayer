package com.marsXTU.music.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marsXTU.music.R;
import com.marsXTU.music.activity.MusicActivity;
import com.marsXTU.music.enums.MusicTypeEnum;
import com.marsXTU.music.model.Music;
import com.marsXTU.music.service.PlayService;
import com.marsXTU.music.utils.CoverLoader;
import com.marsXTU.music.utils.FileUtils;
//butterknife一个专注于Android系统的View注入框架
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 本地音乐列表适配器
 * Created by whd on 2016/3/16.
 */
public class LocalMusicAdapter extends BaseAdapter {
    private Context mContext;
    private OnMoreClickListener mListener;
    private int mPlayingPosition;

    public LocalMusicAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return PlayService.getMusicList().size();
    }

    @Override
    public Object getItem(int position) {
        return PlayService.getMusicList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_holder_music, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == mPlayingPosition) {
            holder.ivPlaying.setVisibility(View.VISIBLE);
        } else {
            holder.ivPlaying.setVisibility(View.INVISIBLE);
        }
        final Music music = PlayService.getMusicList().get(position);
        Bitmap cover = CoverLoader.getInstance().loadThumbnail(music.getCoverUri());
        holder.ivCover.setImageBitmap(cover);
        holder.tvTitle.setText(music.getTitle());
        String artist = FileUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum());
        holder.tvArtist.setText(artist);
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMoreClick(position);
                }
            }
        });
        return convertView;
    }

    /**
     * 更新播放位置
     */
    public void updatePlayingPosition() {
        PlayService playService = ((MusicActivity) mContext).getPlayService();
        if (playService.getPlayingMusic() != null && playService.getPlayingMusic().getType() == MusicTypeEnum.LOCAL) {
            mPlayingPosition = playService.getPlayingPosition();
        } else {
            mPlayingPosition = -1;
        }
    }

    public void setOnMoreClickListener(OnMoreClickListener listener) {
        mListener = listener;
    }

    /**
     * 使用ViewHolder模式注入视图，用@Bind给字段进行注释
     * 并且Butter Knife会根据给定的View ID去查找并自动转换为与你的layout中相匹配的View
     */
    class ViewHolder {
        @Bind(R.id.iv_playing) ImageView ivPlaying;
        @Bind(R.id.iv_cover) ImageView ivCover;
        @Bind(R.id.tv_title) TextView tvTitle;
        @Bind(R.id.tv_artist) TextView tvArtist;
        @Bind(R.id.iv_more) ImageView ivMore;

        /**
         * 绑定视图
         * @param view
         */
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
