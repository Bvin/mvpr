package cn.bvin.android.lib.mvpr.callback;


import cn.rainbow.westore.base.request.RequestModel;

/**
 * 请求回掉接口.
 * <p>在RequestListener的基础上增加onStart()方法，可以在请求开始的时候做一些操作，比如打印请求日志
 * 或者展示一个loading框等等.
 * @param <T> request
 * @param <E> response
 */
public interface RequestCallback<T extends RequestModel, E> extends cn.rainbow.thbase.model.RequestListener<T, E> {
    /**
     * 开始请求.
     *
     * @param request 请求
     */
    void onStart(T request);
}
