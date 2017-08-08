// Generated code from Butter Knife. Do not modify!
package com.marsXTU.music.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LocalMusicAdapter$ViewHolder$$ViewBinder<T extends com.marsXTU.music.adapter.LocalMusicAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558573, "field 'ivPlaying'");
    target.ivPlaying = finder.castView(view, 2131558573, "field 'ivPlaying'");
    view = finder.findRequiredView(source, 2131558513, "field 'ivCover'");
    target.ivCover = finder.castView(view, 2131558513, "field 'ivCover'");
    view = finder.findRequiredView(source, 2131558514, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131558514, "field 'tvTitle'");
    view = finder.findRequiredView(source, 2131558525, "field 'tvArtist'");
    target.tvArtist = finder.castView(view, 2131558525, "field 'tvArtist'");
    view = finder.findRequiredView(source, 2131558574, "field 'ivMore'");
    target.ivMore = finder.castView(view, 2131558574, "field 'ivMore'");
  }

  @Override public void unbind(T target) {
    target.ivPlaying = null;
    target.ivCover = null;
    target.tvTitle = null;
    target.tvArtist = null;
    target.ivMore = null;
  }
}
