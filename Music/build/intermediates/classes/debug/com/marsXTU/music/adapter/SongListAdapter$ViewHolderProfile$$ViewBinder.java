// Generated code from Butter Knife. Do not modify!
package com.marsXTU.music.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SongListAdapter$ViewHolderProfile$$ViewBinder<T extends com.marsXTU.music.adapter.SongListAdapter.ViewHolderProfile> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558572, "field 'tvProfile'");
    target.tvProfile = finder.castView(view, 2131558572, "field 'tvProfile'");
  }

  @Override public void unbind(T target) {
    target.tvProfile = null;
  }
}
