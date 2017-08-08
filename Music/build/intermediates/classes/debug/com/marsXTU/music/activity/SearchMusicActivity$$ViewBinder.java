// Generated code from Butter Knife. Do not modify!
package com.marsXTU.music.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SearchMusicActivity$$ViewBinder<T extends com.marsXTU.music.activity.SearchMusicActivity> extends com.marsXTU.music.activity.BaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131558517, "field 'lvSearchMusic'");
    target.lvSearchMusic = finder.castView(view, 2131558517, "field 'lvSearchMusic'");
    view = finder.findRequiredView(source, 2131558502, "field 'llLoading'");
    target.llLoading = finder.castView(view, 2131558502, "field 'llLoading'");
    view = finder.findRequiredView(source, 2131558503, "field 'llLoadFail'");
    target.llLoadFail = finder.castView(view, 2131558503, "field 'llLoadFail'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.lvSearchMusic = null;
    target.llLoading = null;
    target.llLoadFail = null;
  }
}
