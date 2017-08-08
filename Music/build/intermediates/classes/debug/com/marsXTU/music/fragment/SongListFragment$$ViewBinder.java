// Generated code from Butter Knife. Do not modify!
package com.marsXTU.music.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SongListFragment$$ViewBinder<T extends com.marsXTU.music.fragment.SongListFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558531, "field 'lvSongList'");
    target.lvSongList = finder.castView(view, 2131558531, "field 'lvSongList'");
    view = finder.findRequiredView(source, 2131558502, "field 'llLoading'");
    target.llLoading = finder.castView(view, 2131558502, "field 'llLoading'");
    view = finder.findRequiredView(source, 2131558503, "field 'llLoadFail'");
    target.llLoadFail = finder.castView(view, 2131558503, "field 'llLoadFail'");
  }

  @Override public void unbind(T target) {
    target.lvSongList = null;
    target.llLoading = null;
    target.llLoadFail = null;
  }
}
