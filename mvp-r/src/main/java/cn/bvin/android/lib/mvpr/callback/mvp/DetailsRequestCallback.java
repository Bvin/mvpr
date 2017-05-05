package cn.bvin.android.lib.mvpr.callback.mvp;

import cn.rainbow.westore.base.callback.RequestCallback;
import cn.rainbow.westore.base.request.RequestModel;
import cn.rainbow.westore.base.response.Response;
import cn.rainbow.westore.base.views.SimpleResponseView;
import cn.rainbow.westore.base.views.states.extra.LoadIndicator;

/**
 * Created by bvin on 2016/7/13.
 */
public class DetailsRequestCallback<T extends RequestModel, E> extends MvpRequestCallback<T, Response<E>> {

    private SimpleResponseView<E> mView;

    /**
     * 单项请求回掉.
     *
     * @param view      DetailsView
     * @param callback  RequestCallback
     * @param enableLog 是否打印log
     */
    public DetailsRequestCallback(SimpleResponseView<E> view, RequestCallback<T, Response<E>> callback, boolean enableLog) {
        super(view,callback, enableLog);
        mView = view;
        if (view instanceof LoadIndicator) setLoadIndicator((LoadIndicator) mView);
    }

    /**
     * 单项请求回掉
     * @param view DetailsView
     */
    public DetailsRequestCallback(SimpleResponseView<E> view){
        this(view, null, true);
    }

    @Override
    public void onResponse(Response<E> response) {
        super.onResponse(response);
        if (response.isEmpty()) {
            mView.empty(response.getMessage());
        } else {
            mView.show(response.get());
        }
    }

}
