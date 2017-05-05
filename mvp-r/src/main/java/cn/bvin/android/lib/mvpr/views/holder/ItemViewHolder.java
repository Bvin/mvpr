package cn.bvin.android.lib.mvpr.views.holder;

import android.view.View;

/**
 * ItemViewHolder
 * Created by bvin on 2016/8/3.
 */
public class ItemViewHolder extends BaseViewHolder{

    /**
     * ItemViewHolder.
     * @param itemView target view
     */
    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * ItemViewHolder.
     * @param parentView 父view
     * @param viewId target view resId
     */
    public ItemViewHolder(View parentView, int viewId) {
        this(parentView.findViewById(viewId));
    }

    public View getItemView() {
        return itemView;
    }
}
