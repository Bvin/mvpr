package cn.bvin.android.lib.mvpr.callback.mvp;

import com.android.volley.VolleyError;

import cn.rainbow.westore.base.callback.PreRequestCallback;
import cn.rainbow.westore.base.callback.RequestCallback;
import cn.rainbow.westore.base.request.RequestModel;
import cn.rainbow.westore.base.views.ResponseView;
import cn.rainbow.westore.base.views.states.extra.LoadIndicator;
import cn.rainbow.westore.models.entity.base.BaseEntity;

/**
 *  结合了Mvp的请求回掉,把回掉转移到Mvp的View里.
 * Created by bvin on 2016/7/19.
 * @param <T> request
 * @param <E> response
 */
public abstract class MvpRequestCallback<T extends RequestModel, E extends BaseEntity> extends PreRequestCallback<T, E> {

    protected ResponseView mView;
    protected LoadIndicator mLoadIndicator;

    /**
     * 请求回掉.
     *
     * @param view      ResponseView
     * @param callback  RequestCallback
     * @param enableLog 是否打印log
     */
    public MvpRequestCallback(ResponseView view, RequestCallback<T, E> callback, boolean enableLog) {
        super(callback, enableLog);
        mView = view;
    }

    public void setLoadIndicator(LoadIndicator loadIndicator) {
        mLoadIndicator = loadIndicator;
    }

    @Override
    public void onStart(T request) {
        super.onStart(request);
        //2.dispatch view
        if (mView != null) {
            mView.reset();//开始请求前
            mView.start(true);
        }

    }

    @Override
    public void onResponse(E response) {

        if (mLoadIndicator != null) {
            mLoadIndicator.activeIndicator(false);
        }
    }

    @Override
    public void onFailure(T request, VolleyError error) {
        super.onFailure(request, error);

        if (mView != null) {
            mView.error(error.getMessage());
        }

        if (mLoadIndicator != null) {
            mLoadIndicator.activeIndicator(false);
        }

    }
}
