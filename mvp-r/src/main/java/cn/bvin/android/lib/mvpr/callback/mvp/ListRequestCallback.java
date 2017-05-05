package cn.bvin.android.lib.mvpr.callback.mvp;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import cn.rainbow.thbase.network.JsonSyntaxError;
import cn.rainbow.westore.base.callback.RequestCallback;
import cn.rainbow.westore.base.request.RequestModel;
import cn.rainbow.westore.base.response.ListResponse;
import cn.rainbow.westore.base.views.ListResponseView;
import cn.rainbow.westore.base.views.states.extra.ErrorDisplay;
import cn.rainbow.westore.base.views.states.extra.LoadIndicator;
import cn.rainbow.westore.common.exception.ResponseError;

/**
 * List数据结构的请求回掉.
 *
 * @param <T> 请求，需继承RequestModel并带E泛型
 * @param <E> 响应，集合数据元素类型，例如以下类型json数据，只需要提供item的类型即可.
 *            <p><code>
 *            list:[{item1},{item2},{item2}...]
 *            </code>
 */
public final class ListRequestCallback<T extends RequestModel, E> extends MvpRequestCallback<T, ListResponse<E>> {


    //private ListResponseView<E> mView;
    private List<ListResponseView<E>> mResponseViews;
    private ErrorDisplay mErrorDisplay;

    /** 开始请求前是否需要重置UI */
    private boolean mNeedResetUI;
    /** 成功响应后是否需要清空列表 */
    private boolean mNeedClearList;
    /** 出错后，是否需要显示ErrorView*/
    private boolean mDisplayErrorView;

    private RequestCallback<T, ListResponse<E>> mCallback;

    /**
     * 集合数据请求回掉.
     * @param view      集合视图
     * @param needResetUI 开始请求前，是否需要重置UI
     * @param needClearList 成功响应后，是否需要清空列表
     * @param displayErrorView 出错后，是否需要显示ErrorView
     * @param callback  RequestCallback
     * @param enableLog 是否打印log
     */
    public ListRequestCallback(ListResponseView<E> view, boolean needResetUI, boolean needClearList, boolean displayErrorView, RequestCallback<T, ListResponse<E>> callback, boolean enableLog) {
        super(view, callback, enableLog);
        mResponseViews = new ArrayList<>();
        mView = view;
        mResponseViews.add(view);
        if (view instanceof ErrorDisplay) {
            mErrorDisplay =  view;
        }
        if (view instanceof LoadIndicator){
            setLoadIndicator(view);
        }
        mNeedResetUI = needResetUI;
        mNeedClearList = needClearList;
        mDisplayErrorView = displayErrorView;
        mCallback = callback;
    }

    /**
     * 集合数据请求回掉.
     *
     * @param view 集合视图
     * @param needResetUI 是否需要重置UI
     * @param needClearList 是否需要清空列表
     * @param displayErrorView 出错后，是否需要显示ErrorView
     */
    public ListRequestCallback(ListResponseView<E> view, boolean needResetUI, boolean needClearList, boolean displayErrorView) {
        this(view, needResetUI, needClearList, displayErrorView, null, true);
    }

    /**
     * 添加ResponseView.
     * @param responseView ListResponseView
     */
    public void addResponseView(ListResponseView<E> responseView){
        mResponseViews.add(responseView);
    }

    @Override
    public void onStart(T request) {
        //1.log
        log("Start", request.toString());
        log("RequestL", "Extra", request.extra());

        //2.dispatch view
        for (ListResponseView<E> view :mResponseViews) {

            if (view != null) {
                if (mNeedResetUI) {
                    view.reset();
                    if (mLoadIndicator != null) {
                        mLoadIndicator.activeIndicator(true);
                    }
                }else {
                    if (mNeedClearList){//refresh是需要clear
                        view.load(ListResponseView.POSITION_TOP);
                    }else {//load和reload是不需要clear
                        view.load(ListResponseView.POSITION_BOTTOM);
                    }
                }
            }
        }

        //3.dispatch callback
        if (mCallback != null) {
            mCallback.onStart(request);
        }
    }

    @Override
    public void onResponse(ListResponse<E> response) {
        super.onResponse(response);
        for (ListResponseView<E> view : mResponseViews) {
            if (view != null) {
                if (response.isEmpty()) {
                    view.empty(response.getMessage());
                } else {
                    view.show(response.get(), mNeedClearList, response.hasNext());
                }
            }
        }
    }

    @Override
    public void onFailure(T request, VolleyError error) {
        //1.log
        if (error instanceof JsonSyntaxError) {
            log("Failure", ((JsonSyntaxError) error).getStringResponse());
        } else {
            log("Failure", error.getMessage());
        }

        //2.dispatch view
        for (ListResponseView<E> view : mResponseViews) {

            if (view != null) {
                view.error(error.getMessage());
            }
        }

        if (mErrorDisplay != null) {
            if (error instanceof ResponseError) {
                if (((ResponseError) error).getResponse().getCode() == 10003)
                    mErrorDisplay.displayError(false);
                else mErrorDisplay.displayError(mDisplayErrorView);
            } else mErrorDisplay.displayError(mDisplayErrorView);
        }

        if (mLoadIndicator != null) {
            mLoadIndicator.activeIndicator(false);
        }

        //3.dispatch callback
        if (mCallback != null) {
            mCallback.onFailure(request, error);
        }
    }

}
