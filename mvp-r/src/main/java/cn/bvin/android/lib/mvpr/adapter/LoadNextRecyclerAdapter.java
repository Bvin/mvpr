package cn.bvin.android.lib.mvpr.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.rainbow.westore.R;
import cn.rainbow.westore.base.present.AbsListPresenter;
import cn.rainbow.westore.base.present.ListPresenter;
import cn.rainbow.westore.base.views.holder.BaseViewHolder;
import cn.rainbow.westore.base.views.holder.FooterViewHolder;

/**
 * Created by bvin on 2016/9/20.
 * 对应单ViewHolder的RecyclerAdapter
 */
public abstract class LoadNextRecyclerAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 1;
    private static final int KEY_POSITION = R.id.goods_position;
    private static final int KEY_ITEM = R.id.goods_item;
    protected List<T> mData;
    protected LayoutInflater mInflater;
    private int mLayoutId;
    private boolean mEnableFooter = true;
    private boolean mEnableLoadNext = true;
    private boolean mEnableAutoLoad;
    private FooterViewHolder mBottomItemHolder;
    private OnNextListener mOnNextListener;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private boolean mLoadComplete;

    private AbsListPresenter mPresenter;

    protected LoadNextRecyclerAdapter(Context context) {
        if (context != null) {
            mInflater = LayoutInflater.from(context);
        }
    }

    /**
     * SimpleRecyclerAdapter
     * @param context
     * @param layoutId
     * @param data
     * @param enableAutoLoad 是否自动加载下一页
     */
    public LoadNextRecyclerAdapter(Context context, int layoutId, List<T> data, boolean enableAutoLoad) {
        if (context != null) {
            mInflater = LayoutInflater.from(context);
        }
        mData = data;
        mLayoutId = layoutId;
        mEnableAutoLoad = enableAutoLoad;
    }

    public LoadNextRecyclerAdapter(Context context, int layoutId, List<T> data, AbsListPresenter presenter) {
        if (context != null) {
            mInflater = LayoutInflater.from(context);
        }
        mData = data;
        mLayoutId = layoutId;
        mEnableAutoLoad = (mPresenter = presenter) != null;
    }

    /**
     * SimpleRecyclerAdapter
     * <p>使用此构造，createViewHolder(View rootView)里的rootView参数将会是null.
     * @param data 数据源
     */
    public LoadNextRecyclerAdapter(List<T> data) {
        this(null, -1, data, false);
    }

    protected LoadNextRecyclerAdapter layout(int layoutId){
        mLayoutId = layoutId;
        return this;
    }

    protected LoadNextRecyclerAdapter data(List<T> data){
        mData = data;
        return this;
    }

    public void setEnableFooter(boolean enableFooter) {
        mEnableFooter = enableFooter;
    }

    public void setEnableLoadNext(boolean enableLoadNext) {
        mEnableLoadNext = enableLoadNext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            mBottomItemHolder = new FooterViewHolder(parent);
            mBottomItemHolder.setOnLoadCompleteListener(new OnLoadCompleteListener() {
                @Override
                public void onLoadComplete() {
                    // 全部加载完了，就禁止滑动底部加载下一页了
                    setEnableLoadNext(false);
                }
            });
            if (mLoadComplete) mBottomItemHolder.notifyDataSetChanged(false);
            AbsListPresenter listPresenter = mPresenter != null ? mPresenter : attachListPresenter();
            if (listPresenter != null) {
                listPresenter.registerResponseView(mBottomItemHolder);
            }
            onFooterItemCreate(mBottomItemHolder);
            return mBottomItemHolder;
        } else {
            RecyclerView.ViewHolder vh;
            if (mLayoutId > 0 || mInflater != null) {
                vh =  createViewHolder(mInflater.inflate(mLayoutId, parent, false));
            } else {
                vh =  createViewHolder(null);
            }
            if (vh != null&&vh.itemView!=null) {
                vh.itemView.setClickable(true);
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T t = v.getTag(KEY_ITEM) != null ? (T) v.getTag(KEY_ITEM) : null;
                        int position = v.getTag(KEY_POSITION) != null ? (int) v.getTag(KEY_POSITION) : -1;
                        onItemClick(v, position, t);
                    }
                });
            }
            return vh;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if (h instanceof FooterViewHolder || isFooterItem(position)){
            // no op
        }else {
            if (h != null && h.itemView != null) {

                h.itemView.setTag(KEY_POSITION, position);
                if (mData != null && !mData.isEmpty()) {
                    h.itemView.setTag(KEY_ITEM, mData.get(position));
                }
            }
            bindView((H) h, position, mData != null && !mData.isEmpty() ? mData.get(position) : null);
        }
    }

    /**
     * 创建具体ViewHolder.
     * @param rootView adapter root view，如果构造方法没传layoutId和Context将会是null值
     * @return 具体ViewHolder.
     */
    public abstract H createViewHolder(View rootView);

    public abstract void bindView(H holder, int position, T t);

    public void onItemClick(View view, int position, T t){}

    public void onFooterItemCreate(FooterViewHolder footerViewHolder){}

    /**
     * attachListPresenter.用于注册Footer状态绑定.
     */
    public AbsListPresenter attachListPresenter(){
        return null;
    }

    /**
     * 加载下一页.
     */
    public void onNext(){

        ListPresenter listPresenter = mPresenter != null ? mPresenter : attachListPresenter();
        if (mEnableAutoLoad && listPresenter != null) {
            //自动加载下一页.
            listPresenter.load();
        }

        if (mOnNextListener != null) {
            mOnNextListener.onNext();
        }
    }

    public void notifyDataSetChanged(boolean hasNext){
        mLoadComplete = !hasNext;
        notifyDataSetChanged();
        if (mBottomItemHolder != null) {
            // 通知改变底部文字
            mBottomItemHolder.notifyDataSetChanged(hasNext);
        }else {
            // 当mBottomItemHolder还没创建，到创建完根据mLoadComplete来更新UI
        }
        setEnableLoadNext(hasNext);// 设置是否可加载下一页
    }

    @Override
    public int getItemCount() {
        if (mEnableFooter) {
            return isEmpty() ? 0 : mData.size() + 1;
        } else {
            if (mData != null) {
                return mData.size();
            } else return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mEnableFooter && isFooterItem(position)) {
            return TYPE_FOOTER;
        } else {
            return super.getItemViewType(position);
        }
    }

    private boolean isEmpty() {
        if (mData != null) {
            return mData.isEmpty();
        } else return true;
    }

    /**
     * 是否FooterItem
     * @param position item所在position
     * @return
     */
    private boolean isFooterItem(int position) {
        // 开启了加载更多||位于最后一行
        return !isEmpty() && position == mData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mOnScrollListener == null) {
            mOnScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    // TODO: 2017/2/24 偶尔出现触发多次在加载过程中 isLoading
                    if (newState == RecyclerView.SCROLL_STATE_IDLE&& mBottomItemHolder != null)
                    Log.d("AttachedToRecyclerView", "onScrollStateChanged: "+mBottomItemHolder.isLoading());
                    if (mEnableLoadNext && mBottomItemHolder != null && !mBottomItemHolder.isLoading()
                            && newState == RecyclerView.SCROLL_STATE_IDLE/* && recyclerView.canScrollVertically(1)*/) {
                        boolean reachBottom;
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        if (layoutManager instanceof LinearLayoutManager) {
                            reachBottom = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition()
                                    >= layoutManager.getItemCount() - 1;
                        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                            StaggeredGridLayoutManager sgLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                            int[] into = new int[sgLayoutManager.getSpanCount()];
                            sgLayoutManager.findLastVisibleItemPositions(into);

                            reachBottom = last(into) >= layoutManager.getItemCount() - 1;
                        } else {
                            reachBottom = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition()
                                    >= layoutManager.getItemCount() - 1;
                        }
                        if (reachBottom) {
                            onNext();
                        }
                    }
                }
            };
        }
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mOnScrollListener != null) {
            recyclerView.removeOnScrollListener(mOnScrollListener);
            mOnScrollListener = null;
        }
    }

    /**
     * 取到最后的一个节点
     */
    private static int last(int[] lastPositions) {
        int last = lastPositions[0];
        for (int value : lastPositions) {
            if (value > last) {
                last = value;
            }
        }
        return last;
    }

    public interface OnNextListener{
        void onNext();
    }

    public interface OnLoadCompleteListener{
        void onLoadComplete();
    }
}
