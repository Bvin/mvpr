package cn.bvin.android.lib.mvpr.views;

import cn.rainbow.westore.base.views.states.end.EndListDisplay;
import cn.rainbow.westore.base.views.states.extra.ErrorDisplay;
import cn.rainbow.westore.base.views.states.extra.Load;
import cn.rainbow.westore.base.views.states.extra.LoadIndicator;

/**
 * ListResponseView.
 */

public interface ListResponseView<T> extends ResponseView, EndListDisplay<T>, LoadIndicator, Load, ErrorDisplay {
}
