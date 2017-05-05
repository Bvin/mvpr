package cn.bvin.android.lib.mvpr.views.holder;

import android.view.View;

import cn.rainbow.westore.R;

/**
 * Created by bvin on 2016/9/2.
 */
public class ErrorViewHolder extends BaseViewHolder implements View.OnClickListener {

    private View.OnClickListener mOnReloadListener;

    public ErrorViewHolder(View itemView) {
        super(itemView);
        itemView.findViewById(R.id.bt_reload).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mOnReloadListener != null) {
            mOnReloadListener.onClick(view);
        }
    }

    public void setOnReloadListener(View.OnClickListener onReloadListener) {
        mOnReloadListener = onReloadListener;
    }

}
