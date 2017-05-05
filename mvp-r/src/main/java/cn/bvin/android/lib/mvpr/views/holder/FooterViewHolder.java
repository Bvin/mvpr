package cn.bvin.android.lib.mvpr.views.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.rainbow.westore.R;
import cn.rainbow.westore.base.adapter.LoadNextRecyclerAdapter;
import cn.rainbow.westore.base.views.ListResponseView;

/**
 * 加载下一页的Footer
 * Created by bvin on 2017/2/22.
 */

public class FooterViewHolder extends BaseViewHolder implements View.OnClickListener,ListResponseView {

    public static final String LOAD_MORE = "努力加载中...";
    public static final String NO_MORE = "没有更多啦";

    private TextView mTvLoadBottom;
    private View.OnClickListener mOnReloadListener;
    private LoadNextRecyclerAdapter.OnLoadCompleteListener mOnLoadCompleteListener;
    private boolean mIsLoading;

    public FooterViewHolder(ViewGroup viewGroup) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_footer_to_load, viewGroup, false));
        itemView.setOnClickListener(this);
        mTvLoadBottom = (TextView) itemView.findViewById(R.id.tv_load_more);
    }

    public void setText(String text){
        mTvLoadBottom.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (mOnReloadListener != null) {
            mOnReloadListener.onClick(v);
        }
    }

    public void setOnReloadListener(View.OnClickListener onReloadListener) {
        mOnReloadListener = onReloadListener;
    }

    public void setOnLoadCompleteListener(LoadNextRecyclerAdapter.OnLoadCompleteListener onLoadCompleteListener) {
        mOnLoadCompleteListener = onLoadCompleteListener;
    }

    @Override
    public void start(boolean indicate) {
        mIsLoading = true;
        itemView.setClickable(false);
        if (!mTvLoadBottom.getText().toString().equals(LOAD_MORE)){
            mTvLoadBottom.setText(LOAD_MORE);
        }
    }

    @Override
    public void error(String message) {
        mIsLoading = false;
        itemView.setClickable(true);
        mTvLoadBottom.setText("加载出错，点击重试！");
    }

    @Override
    public void show(List response, boolean clearList, boolean hasNext) {
        mIsLoading = false;
        notifyDataSetChanged(hasNext);
    }

    /**
     * notifyDataSetChanged.
     * @param hasNext 是否有下一页
     */
    public void notifyDataSetChanged(boolean hasNext) {
        if (hasNext){
            mTvLoadBottom.setText(LOAD_MORE);
        }else {
            mTvLoadBottom.setText(NO_MORE);
            if (mOnLoadCompleteListener != null) {
                mOnLoadCompleteListener.onLoadComplete();
            }
        }
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public void reset() {

    }

    @Override
    public void activeIndicator(boolean active) {

    }

    @Override
    public void displayError(boolean show) {

    }

    @Override
    public void empty(String message) {

    }

    @Override
    public void load(int position) {

    }
}
