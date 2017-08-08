// Generated code from Butter Knife. Do not modify!
package com.marsXTU.music.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SplashActivity$$ViewBinder<T extends com.marsXTU.music.activity.SplashActivity> extends com.marsXTU.music.activity.BaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131558519, "field 'ivSplash'");
    target.ivSplash = finder.castView(view, 2131558519, "field 'ivSplash'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.ivSplash = null;
  }
}
