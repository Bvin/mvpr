package cn.bvin.android.lib.mvpr.views.holder;

import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.rainbow.westore.R;
import cn.rainbow.westore.base.fresco.Fresco;

/**
 * Created by bvin on 2017/4/17.
 */
public class ImageViewHolder extends BaseViewHolder implements View.OnClickListener {

    private View.OnClickListener mOnClickListener;
    private SimpleDraweeView mSimpleDraweeView;

    public ImageViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mSimpleDraweeView = (SimpleDraweeView) itemView;
        mSimpleDraweeView.getHierarchy().setPlaceholderImage(R.drawable.image_default_328x328);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void show(String url){
        Fresco.load(url).fitResize().into(mSimpleDraweeView);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }
}
