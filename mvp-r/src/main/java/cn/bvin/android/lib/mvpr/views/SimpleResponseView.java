package cn.bvin.android.lib.mvpr.views;

import cn.rainbow.westore.base.views.states.extra.ErrorDisplay;
import cn.rainbow.westore.base.views.states.extra.LoadIndicator;

/**
 * 页面级SimpleResponseView.
 * <p>Created by bvin on 2016/10/11.
 */

public interface SimpleResponseView<T> extends ItemResponseView<T>, LoadIndicator, ErrorDisplay {
    //集成BeforeStart, Start, LoadIndicator, EndSimpleDisplay, EndEmpty, EndError, ErrorDisplay
    //reset(), start(boolean indicate),
}
