package cn.bvin.android.lib.mvpr.views;

import cn.rainbow.westore.base.views.states.BeforeStart;
import cn.rainbow.westore.base.views.states.Start;
import cn.rainbow.westore.base.views.states.end.EndEmpty;
import cn.rainbow.westore.base.views.states.end.EndError;

/**
 * 响应式(耗时获取数据)对应的各种显示状态(以下4个共性状态).
 * <ul>
 *     <li>{@link #reset()} by BeforeStart
 *     <li>{@link #start(boolean)} by Start
 *     <li>{@link #empty(String)}
 *     <li>{@link #error(String)} by {@linkplain EndError}
 * </ul>
 * <p>Created by bvin on 2016/10/11.
 */

public interface ResponseView extends BeforeStart, Start, EndEmpty, EndError, BaseView{
    //所有ResponseView都有这4个共性BeforeStart, Start, EndEmpty, EndError
}
