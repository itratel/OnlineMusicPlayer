// Generated code from Butter Knife. Do not modify!
package com.marsXTU.music.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SongListAdapter$ViewHolderMusicList$$ViewBinder<T extends com.marsXTU.music.adapter.SongListAdapter.ViewHolderMusicList> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558513, "field 'ivCover'");
    target.ivCover = finder.castView(view, 2131558513, "field 'ivCover'");
    view = finder.findRequiredView(source, 2131558569, "field 'tvMusic1'");
    target.tvMusic1 = finder.castView(view, 2131558569, "field 'tvMusic1'");
    view = finder.findRequiredView(source, 2131558570, "field 'tvMusic2'");
    target.tvMusic2 = finder.castView(view, 2131558570, "field 'tvMusic2'");
    view = finder.findRequiredView(source, 2131558571, "field 'tvMusic3'");
    target.tvMusic3 = finder.castView(view, 2131558571, "field 'tvMusic3'");
  }

  @Override public void unbind(T target) {
    target.ivCover = null;
    target.tvMusic1 = null;
    target.tvMusic2 = null;
    target.tvMusic3 = null;
  }
}
