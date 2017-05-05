package cn.bvin.android.lib.mvpr.universal;

import cn.rainbow.westore.base.present.ListPresenter;
import cn.rainbow.westore.base.views.ListResponseView;
import cn.rainbow.westore.base.views.states.extra.ErrorDisplay;
import cn.rainbow.westore.base.views.states.extra.LoadIndicator;

/**
 * Created by bvin on 2017/4/5.
 */

public class UniversalListContract {
    interface Presenter extends ListPresenter {
    }

    interface ResponseView<T> extends ListResponseView<T>,ErrorDisplay,LoadIndicator {

    }
}
