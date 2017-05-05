package cn.bvin.android.lib.mvpr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.rainbow.westore.R;
import cn.rainbow.westore.base.views.holder.BaseViewHolder;

/**
 * Created by bvin on 2016/9/20.
 * 对应单ViewHolder的RecyclerAdapter
 */
public abstract class SimpleRecyclerAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<H> {
    private static final int KEY_POSITION = R.id.goods_position;
    private static final int KEY_ITEM = R.id.goods_item;
    protected List<T> mData;
    protected LayoutInflater mInflater;
    private int mLayoutId;

    protected SimpleRecyclerAdapter(Context context) {
        if (context != null) {
            mInflater = LayoutInflater.from(context);
        }
    }

    public SimpleRecyclerAdapter(Context context, int layoutId, List<T> data) {
        if (context != null) {
            mInflater = LayoutInflater.from(context);
        }
        mData = data;
        mLayoutId = layoutId;
    }

    /**
     * SimpleRecyclerAdapter
     * <p>使用此构造，createViewHolder(View rootView)里的rootView参数将会是null.
     * @param data 数据源
     */
    public SimpleRecyclerAdapter(List<T> data) {
        this(null, -1, data);
    }

    protected SimpleRecyclerAdapter layout(int layoutId){
        mLayoutId = layoutId;
        return this;
    }

    protected SimpleRecyclerAdapter data(List<T> data){
        mData = data;
        return this;
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        H viewHolder;
        if (mLayoutId > 0 || mInflater != null) {
            viewHolder =  createViewHolder(mInflater.inflate(mLayoutId, parent, false));
        } else {
            viewHolder =  createViewHolder(null);
        }
        if (viewHolder != null&&viewHolder.itemView!=null) {
            viewHolder.itemView.setClickable(true);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T t = v.getTag(KEY_ITEM) != null ? (T) v.getTag(KEY_ITEM) : null;
                    int position = v.getTag(KEY_POSITION) != null ? (int) v.getTag(KEY_POSITION) : -1;
                    if (t != null && position >= 0) {
                        onItemClick(v, position, t);
                    }
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(H h, int position) {
        if (h != null && h.itemView != null) {

            h.itemView.setTag(KEY_POSITION, position);
            if (mData != null && !mData.isEmpty()) {
                h.itemView.setTag(KEY_ITEM, mData.get(position));
            }
        }
        bindView(h, position, mData != null ? mData.get(position) : null);
    }

    /**
     * 创建具体ViewHolder.
     * @param rootView adapter root view，如果构造方法没传layoutId和Context将会是null值
     * @return 具体ViewHolder.
     */
    public abstract H createViewHolder(View rootView);

    public abstract void bindView(H holder, int position, T t);

    public void onItemClick(View view, int position, T t){}

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }
}
