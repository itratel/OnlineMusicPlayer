package com.marsXTU.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marsXTU.music.R;
import com.marsXTU.music.model.JSearchMusic;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 搜索结果适配器
 * Created by whd on 2016/3/20.
 */
public class SearchMusicAdapter extends BaseAdapter {
    private Context mContext;
    private List<JSearchMusic.JSong> mData;
    private OnMoreClickListener mListener;

    public SearchMusicAdapter(Context context, List<JSearchMusic.JSong> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
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
        holder.tvTitle.setText(mData.get(position).getSongname());
        holder.tvArtist.setText(mData.get(position).getArtistname());
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMoreClick(position);
            }
        });
        return convertView;
    }

    public void setOnMoreClickListener(OnMoreClickListener listener) {
        mListener = listener;
    }

    class ViewHolder {
        @Bind(R.id.iv_cover) ImageView ivCover;
        @Bind(R.id.tv_title) TextView tvTitle;
        @Bind(R.id.tv_artist) TextView tvArtist;
        @Bind(R.id.iv_more) ImageView ivMore;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            ivCover.setVisibility(View.GONE);
        }
    }
}
