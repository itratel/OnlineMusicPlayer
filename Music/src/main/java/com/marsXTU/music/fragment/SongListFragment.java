package com.marsXTU.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.marsXTU.music.R;
import com.marsXTU.music.activity.OnlineMusicActivity;
import com.marsXTU.music.adapter.SongListAdapter;
import com.marsXTU.music.enums.LoadStateEnum;
import com.marsXTU.music.model.SongListInfo;
import com.marsXTU.music.utils.Extras;
import com.marsXTU.music.utils.NetworkUtils;
import com.marsXTU.music.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 在线音乐
 * Created by whd on 2016/3/17.
 */
public class SongListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.lv_song_list) ListView lvSongList;
    @Bind(R.id.ll_loading) LinearLayout llLoading;
    @Bind(R.id.ll_load_fail) LinearLayout llLoadFail;
    private List<SongListInfo> mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song_list, container, false);
    }

    @Override
    protected void init() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            ViewUtils.changeViewState(lvSongList, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
            return;
        }
        mData = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.online_music_list_title);
        String[] types = getResources().getStringArray(R.array.online_music_list_type);
        for (int i = 0; i < titles.length; i++) {
            SongListInfo info = new SongListInfo();
            info.setTitle(titles[i]);
            info.setType(types[i]);
            mData.add(info);
        }
        SongListAdapter mAdapter = new SongListAdapter(getActivity(), mData);
        lvSongList.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        lvSongList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SongListInfo songListInfo = mData.get(position);
        Intent intent = new Intent(getActivity(), OnlineMusicActivity.class);
        intent.putExtra(Extras.MUSIC_LIST_TYPE, songListInfo);
        startActivity(intent);
    }
}
