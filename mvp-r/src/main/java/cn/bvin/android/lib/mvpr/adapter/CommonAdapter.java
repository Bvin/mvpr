package cn.bvin.android.lib.mvpr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.rainbow.westore.base.views.holder.BaseViewHolder;

/**
 * See：https://github.com/hongyangAndroid/baseAdapter/
 * <br> Created by 32967 on 2016/4/12.
 */
public abstract class CommonAdapter<T, H extends BaseViewHolder> extends BaseAdapter {
    protected List<T> mData;
    protected LayoutInflater mInflater;
    private int layoutId;

    public CommonAdapter(Context context, int layoutId, List<T> data) {
        mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            convertView = createViewFromResource(convertView, parent, layoutId);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (H) convertView.getTag();
        }
        bindView(holder, position, getItem(position));
        return convertView;
    }

    private View createViewFromResource(View convertView,
                                        ViewGroup parent, int resource) {
        /*View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }*/
        return mInflater.inflate(resource, parent, false);
    }

    public abstract H createViewHolder(View convertView);

    public abstract void bindView(H holder, int position, T t);

}
