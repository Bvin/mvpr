package cn.bvin.android.lib.mvpr.universal;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bvin.android.lib.widget.refresh.GestureRefreshLayout;
import cn.rainbow.westore.R;
import cn.rainbow.westore.base.adapter.LoadNextRecyclerAdapter;
import cn.rainbow.westore.base.callback.mvp.ListRequestCallback;
import cn.rainbow.westore.base.present.AbsListPresenter;
import cn.rainbow.westore.base.request.RequestModel;
import cn.rainbow.westore.ui.base.BaseActivity;
import cn.rainbow.westore.ui.base.ErrorFragment;
import cn.rainbow.westore.ui.views.TitleBar;

/**
 * Created by bvin on 2017/4/5.
 * 1.请求参数
 * 2.响应结构
 * 3.itemViewHolder
 * 4.itemClick
 */

public abstract class UniversalListActivity<T extends RequestModel,E> extends BaseActivity implements UniversalListContract.ResponseView<E>, GestureRefreshLayout.OnRefreshListener {

    private AbsListPresenter mPresenter;
    protected GestureRefreshLayout mGestureRefreshLayout;
    protected RecyclerView mRecyclerView;
    private List<E> mDataList;

    private ErrorFragment mErrorFragment;
    protected TextView mTvEmpty;

    @Override
    protected int getContentView() {
        return R.layout.activity_universal_list;
    }

    @Override
    protected void doInitData() {
        super.doInitData();
        mPresenter = getPresenter();
        // 是否一进来就加载
        mPresenter.start();
    }

    @Override
    protected void doInitView() {
        super.doInitView();
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setTitle(getTitle());
        mGestureRefreshLayout = (GestureRefreshLayout) findViewById(R.id.gesture_refresh_layout);
        mGestureRefreshLayout.setTranslateContent(true);
        mGestureRefreshLayout.setOnRefreshListener(this);
        final RefreshViewHolder refreshViewHolder = new RefreshViewHolder(mGestureRefreshLayout.findViewById(R.id.refresh_view));
        refreshViewHolder.setAnimationCompleteListener(new RefreshViewHolder.OnAnimationCompleteListener() {
            @Override
            public void onAnimationComplete() {
                mGestureRefreshLayout.setRefreshing(false);
            }
        });

        mGestureRefreshLayout.setDistanceToTriggerSync(120);
        mGestureRefreshLayout.setOnGestureChangeListener(refreshViewHolder);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mDataList = new ArrayList<>();
        mRecyclerView.setAdapter(provideItemAdapter(this, mDataList, getPresenter()));

        mErrorFragment = (ErrorFragment) getSupportFragmentManager().findFragmentById(R.id.error_fragment);
        mErrorFragment.setOnReloadListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.reStart();
            }
        });

        mTvEmpty = (TextView) findViewById(R.id.empty);
    }

    /**
     * 请求数据.
     * @param pageIndex 页码
     * @param callback 回掉
     */
    public abstract void onList(int pageIndex, ListRequestCallback<T, E> callback);

    /**
     * 提供适配器
     * @param context 上下文
     * @param data 数据源
     * @return LoadNextRecyclerAdapter
     */
    public abstract LoadNextRecyclerAdapter provideItemAdapter(Context context, List<E> data, AbsListPresenter presenter);

    private AbsListPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new AbsListPresenter() {
                @Override
                public void list(int pageIndex) {
                    ListRequestCallback<T, E> callback = new ListRequestCallback<>
                            (UniversalListActivity.this, isNeedResetUI(), isNeedClearList(), isDisplayErrorView());
                    if (mResponseView != null) {
                        callback.addResponseView(mResponseView);
                    }
                    onList(pageIndex, callback);
                }
            };
        }
        return mPresenter;
    }

    @Override
    public void reset() {
        mErrorFragment.hide(getSupportFragmentManager());
        mTvEmpty.setVisibility(View.GONE);
        mGestureRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void start(boolean indicate) {
        activeIndicator(indicate);
    }

    @Override
    public void activeIndicator(boolean active) {
        if (active) showProgressDialog();
        else  dismissProgressDialog();
    }

    @Override
    public void displayError(boolean show) {
        if (show){
            mErrorFragment.show(getSupportFragmentManager());
        }else {
            //ListFooter show error message
        }
    }

    @Override
    public void empty(String message) {
        mTvEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void error(String message) {
        mGestureRefreshLayout.setRefreshing(false);
    }


    @Override
    public void load(int position) {

    }

    @Override
    public void show(List<E> response, boolean clearList, boolean hasNext) {
        mGestureRefreshLayout.setVisibility(View.VISIBLE);
        if(clearList) mDataList.clear();
        mDataList.addAll(response);
        ((LoadNextRecyclerAdapter)mRecyclerView.getAdapter()).notifyDataSetChanged(hasNext);
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh();
    }
}
