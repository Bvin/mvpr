package cn.bvin.android.lib.mvpr.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.rainbow.westore.base.views.holder.BaseViewHolder;

/**
 * Created by bvin on 2016/10/11.
 */

public abstract class SimplePagerAdapter<H extends BaseViewHolder,T> extends PagerAdapter implements AdapterDataBinding<T,H>{

    private Context mContext;
    private List<T> mData;

    public SimplePagerAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view;
        if (mData != null && mContext != null) {
            T t = null;
            if (position < mData.size()) {//超出数据源之外
                t = mData.get(position);
            }
            H h = createViewHolder(mContext, 0, position);
            view = h.itemView;
            container.addView(view);
            bind(h, position, t);
            return view;
        }
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
