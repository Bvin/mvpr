package cn.bvin.android.lib.mvpr.adapter;

import android.content.Context;

import cn.rainbow.westore.base.views.holder.BaseViewHolder;

/**
 * Created by bvin on 2016/10/11.
 * 适配器数据绑定.
 */

public interface AdapterDataBinding<T, H extends BaseViewHolder> {

    /**
     * 创建ViewHolder.
     * @param context 上下文
     * @param layoutRes 布局
     * @param position position
     * @return ViewHolder
     */
    H createViewHolder(Context context, int layoutRes, int position);

    /**
     * 数据绑定.
     * @param holder viewHolder
     * @param position position
     * @param t data
     */
    void bind(H holder, int position, T t);

}
