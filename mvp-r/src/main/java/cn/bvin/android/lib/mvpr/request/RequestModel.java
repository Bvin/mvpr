package cn.bvin.android.lib.mvpr.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import java.util.Map;

import cn.rainbow.thbase.model.THBaseModel;
import cn.rainbow.westore.base.callback.PreRequestCallback;
import cn.rainbow.westore.base.callback.RequestCallback;
import cn.rainbow.westore.common.Constants;
import cn.rainbow.westore.models.base.BaseModel;
import cn.rainbow.westore.models.entity.base.BaseEntity;

/**
 * 数据请求.
 * <p>在BaseModel的基础上，增加onStart()的回掉.
 */
public abstract class RequestModel<T extends BaseEntity> extends BaseModel<T> {


    private RequestCallback<RequestModel<T>, T> mCallback;

    /**
     * 数据请求.
     * @param callback 请求回掉
     */
    public RequestModel(RequestCallback<RequestModel<T>, T> callback) {
        super(callback instanceof PreRequestCallback ? callback : new PreRequestCallback<>(callback));
        mCallback = callback;
    }

    /**
     * 数据请求.
     * @param urlParams
     * @param callback
     */
    public RequestModel(String urlParams, RequestCallback<RequestModel<T>, T> callback) {
        super(callback instanceof PreRequestCallback ? callback : new PreRequestCallback<>(callback), urlParams);
        mCallback = callback;
    }

    @Override
    public THBaseModel<T> start() {
        THBaseModel<T> requestModel = super.start();
        //放在start后面，是因为父类有可能会串改request源
        if (mCallback != null) {
            mCallback.onStart(this);
        }
        return requestModel;
    }

    @Override
    public void cancel() {
        getRequest().cancel();
    }

    @Override
    public String getServerAddress() {
        return Constants.SERVER_ADDRESS();
    }

    /**
     * toString（如果是Post将会把参数按照QueryString方式打印出来）.
     * @return as query string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(asMethodString(getHttpMethod()));
        sb.append(" ");
        sb.append(getServerAddress());
        sb.append(getRequestPath());

        //query params
        Map<String, String> params = getParams();
        if (params != null && !params.isEmpty()) {
            sb.append("?").append(extractMap(getParams()));
        }

        return sb.toString();
    }

    /**
     * extra(headers etc.)
     * @return
     */
    public String extra(){
        StringBuilder sb = new StringBuilder();
        try {//query headers
            Map<String, String> headers = getRequest().getHeaders();
            if (headers != null && !headers.isEmpty()) {
                sb.append(extractMap(headers));
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        return sb.toString();
    }

    private String extractMap(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry :
                map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (sb.toString().endsWith("&")) {
            sb.delete(sb.toString().length() - 1, sb.toString().length());
        }
        return sb.toString();
    }

    private String asMethodString(int method) {
        switch (method) {
            case Request.Method.GET:
                return "GET";
            default:
            case Request.Method.POST:
                return "POST";
            case Request.Method.PUT:
                return "PUT";
            case Request.Method.DELETE:
                return "DELETE";
            case Request.Method.HEAD:
                return "HEAD";
            case Request.Method.OPTIONS:
                return "OPTIONS";
            case Request.Method.PATCH:
                return "PATCH";
            case Request.Method.TRACE:
                return "TRACE";
        }
    }
}
