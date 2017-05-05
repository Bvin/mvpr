package cn.bvin.android.lib.mvpr.callback;


import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;

import cn.rainbow.thbase.network.JsonSyntaxError;
import cn.rainbow.westore.base.request.RequestModel;
import cn.rainbow.westore.common.Constants;
import cn.rainbow.westore.common.exception.ResponseError;
import cn.rainbow.westore.models.entity.base.BaseEntity;

/**
 * PreRequestCallback,主要请求回掉的预处理，这里只做打印工作.
 * @param <T> request
 * @param <E> response
 * <p>Created by bvin on 2016/7/13.
 */
public class PreRequestCallback<T extends RequestModel, E extends BaseEntity> implements RequestCallback<T, E> {

    private static final String TAG = "RequestLog";

    private boolean mEnableLog;

    private RequestCallback<T, E> mCallback;

    /**
     * 请求回掉.
     *
     * @param callback  RequestCallback
     * @param enableLog 是否打印log
     */
    public PreRequestCallback(RequestCallback<T, E> callback, boolean enableLog) {
        mEnableLog = enableLog;
        mCallback = callback;
    }

    public PreRequestCallback(RequestCallback<T, E> callback) {
        this(callback, true);
    }
    @Override
    public void onStart(T request) {

        //1.log
        log("Start", request.toString());

        //Log通过RequestL可以查看全部请求信息，想要过滤header就用RequestLog
        log("RequestL", "Extra", request.extra());

        //3.dispatch callback
        if (mCallback != null) {
            mCallback.onStart(request);
        }
    }

    @Override
    public void onSuccess(T request, E response) {
        //1.log
        log("Success", response.toString());

        if (response.isSuccessful()) {

            onResponse(response);

            //3.dispatch callback
            if (mCallback != null) {
                mCallback.onSuccess(request, response);
            }

        } else if (response.getCode() == 40001){
            onResponse(response);
            //3.dispatch callback
            if (mCallback != null) {
                mCallback.onSuccess(request, response);
            }
        } else{
            onFailure(request, new ResponseError(response));
        }

    }

    /**
     * 服务端成功响应(即code==200).
     *
     * @param response 响应
     */
    protected void onResponse(E response) {

    }


    @Override
    public void onFailure(T request, VolleyError error) {

        //1.log
        if (error instanceof JsonSyntaxError) {
            log("Failure", error.toString() +"\n origin json ："+ ((JsonSyntaxError) error).getStringResponse());
        } else {
            log("Failure", error.getMessage());
        }

        //3.dispatch callback
        if (mCallback != null) {
            mCallback.onFailure(request, error);
        }
    }

    protected void log(String state, String message) {
        log(TAG, state, message);
    }

    protected void log(String prefix, String state, String message) {
        if (Constants.DEBUG) {
            if (mEnableLog) {
                if (message == null) {
                    message = "null";
                } else if (TextUtils.isEmpty(message)) {
                    message = "empty message...";
                }
                Log.d(prefix + " " + state, message);
            }
        }
    }
}
