// Generated code from Butter Knife. Do not modify!
package com.marsXTU.music.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PlayFragment$$ViewBinder<T extends com.marsXTU.music.fragment.PlayFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558523, "field 'llContent'");
    target.llContent = finder.castView(view, 2131558523, "field 'llContent'");
    view = finder.findRequiredView(source, 2131558522, "field 'ivPlayingBg'");
    target.ivPlayingBg = finder.castView(view, 2131558522, "field 'ivPlayingBg'");
    view = finder.findRequiredView(source, 2131558524, "field 'ivBack'");
    target.ivBack = finder.castView(view, 2131558524, "field 'ivBack'");
    view = finder.findRequiredView(source, 2131558514, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131558514, "field 'tvTitle'");
    view = finder.findRequiredView(source, 2131558525, "field 'tvArtist'");
    target.tvArtist = finder.castView(view, 2131558525, "field 'tvArtist'");
    view = finder.findRequiredView(source, 2131558526, "field 'vpPlay'");
    target.vpPlay = finder.castView(view, 2131558526, "field 'vpPlay'");
    view = finder.findRequiredView(source, 2131558527, "field 'ilIndicator'");
    target.ilIndicator = finder.castView(view, 2131558527, "field 'ilIndicator'");
    view = finder.findRequiredView(source, 2131558546, "field 'sbProgress'");
    target.sbProgress = finder.castView(view, 2131558546, "field 'sbProgress'");
    view = finder.findRequiredView(source, 2131558545, "field 'tvCurrentTime'");
    target.tvCurrentTime = finder.castView(view, 2131558545, "field 'tvCurrentTime'");
    view = finder.findRequiredView(source, 2131558547, "field 'tvTotalTime'");
    target.tvTotalTime = finder.castView(view, 2131558547, "field 'tvTotalTime'");
    view = finder.findRequiredView(source, 2131558548, "field 'ivMode'");
    target.ivMode = finder.castView(view, 2131558548, "field 'ivMode'");
    view = finder.findRequiredView(source, 2131558550, "field 'ivPlay'");
    target.ivPlay = finder.castView(view, 2131558550, "field 'ivPlay'");
    view = finder.findRequiredView(source, 2131558551, "field 'ivNext'");
    target.ivNext = finder.castView(view, 2131558551, "field 'ivNext'");
    view = finder.findRequiredView(source, 2131558549, "field 'ivPrev'");
    target.ivPrev = finder.castView(view, 2131558549, "field 'ivPrev'");
  }

  @Override public void unbind(T target) {
    target.llContent = null;
    target.ivPlayingBg = null;
    target.ivBack = null;
    target.tvTitle = null;
    target.tvArtist = null;
    target.vpPlay = null;
    target.ilIndicator = null;
    target.sbProgress = null;
    target.tvCurrentTime = null;
    target.tvTotalTime = null;
    target.ivMode = null;
    target.ivPlay = null;
    target.ivNext = null;
    target.ivPrev = null;
  }
}
