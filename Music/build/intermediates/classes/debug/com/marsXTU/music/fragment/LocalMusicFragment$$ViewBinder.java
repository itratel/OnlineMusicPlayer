// Generated code from Butter Knife. Do not modify!
package com.marsXTU.music.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LocalMusicFragment$$ViewBinder<T extends com.marsXTU.music.fragment.LocalMusicFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558520, "field 'lvLocalMusic'");
    target.lvLocalMusic = finder.castView(view, 2131558520, "field 'lvLocalMusic'");
    view = finder.findRequiredView(source, 2131558521, "field 'tvEmpty'");
    target.tvEmpty = finder.castView(view, 2131558521, "field 'tvEmpty'");
  }

  @Override public void unbind(T target) {
    target.lvLocalMusic = null;
    target.tvEmpty = null;
  }
}
