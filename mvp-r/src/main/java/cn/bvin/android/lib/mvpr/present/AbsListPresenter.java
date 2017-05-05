package cn.bvin.android.lib.mvpr.present;

import cn.rainbow.westore.base.views.ListResponseView;

/**
 * 封装对页码的操作.
 */
public abstract class AbsListPresenter implements ListPresenter{

    private static final int PAGE_INDEX_FIRST = 0;

    private int mPageIndex;

    /** 请求前，是否需要重置UI*/
    private boolean mNeedResetUI;

    /** 响应成功后，是否需要清空列表*/
    private boolean mNeedClearList;

    /** 出错后，是否需要显示ErrorView*/
    private boolean mDisplayErrorView;

    private int mListRequestType;

    protected ListResponseView mResponseView;

    /**
     * registerResponseView.
     * @param responseView
     */
    public void registerResponseView(ListResponseView responseView){
        mResponseView = responseView;
    }


    /**
     * 重置页码.
     */
    public void resetPageIndex(){
        mPageIndex = PAGE_INDEX_FIRST;
    }

    /**
     * 递增页码.
     */
    public void increasePageIndex(){
        mPageIndex++;
    }

    /**
     * 递减页码.
     */
    public void decreasePageIndex(){
        mPageIndex++;
    }

    /**
     * 获取当前页码.
     * @return
     */
    public int getCurrentPageIndex() {
        return mPageIndex;
    }

    /**
     * 是否位于第一页.
     * @return
     */
    public boolean atFirstPage() {
        return mPageIndex == PAGE_INDEX_FIRST;
    }

    /**
     * 开始加载.
     * <P>先重置页码，然后发出请求.
     */
    @Override
    public void start() {
        mListRequestType = REQUEST_TYPE_START;
        mNeedResetUI = true;
        mDisplayErrorView = true;
        resetPageIndex();
        list(mPageIndex);
    }

    /**
     * 从ErrorView点击重新加载.
     */
    @Override
    public void reStart() {
        mListRequestType = REQUEST_TYPE_RESTART;
        start();
    }


    /**
     * 刷新操作.
     * <P>先重置页码，然后发出请求.
     */
    @Override
    public void refresh() {
        mListRequestType = REQUEST_TYPE_REFRESH;
        mNeedResetUI = false;
        mNeedClearList = true;
        mDisplayErrorView = true;
        resetPageIndex();
        list(mPageIndex);
    }

    /**
     * 加载下一页数据.
     * <P>页码+1，然后发出请求
     */
    @Override
    public void load() {
        mListRequestType = REQUEST_TYPE_LOAD;
        mNeedResetUI = false;
        mNeedClearList = false;
        mDisplayErrorView = false;
        increasePageIndex();
        list(mPageIndex);
    }


    /**
     * 点击Footer重新加载
     */
    @Override
    public void reLoad() {
        mListRequestType = REQUEST_TYPE_RELOAD;
        mNeedResetUI = false;
        mNeedClearList = false;
        mDisplayErrorView = false;
        //decreasePageIndex();//无需降回页码
        //直接以上一次nextPage的PageIndex进行加载
        list(mPageIndex);
    }


    /**
     * List是否为空，取决于View层的调用方法.
     * @return
     */
    public boolean isNeedClearList() {
        return mNeedClearList;
    }

    public boolean isDisplayErrorView() {
        return mDisplayErrorView;
    }

    public boolean isNeedResetUI() {
        return mNeedResetUI;
    }

    /**
     * list请求.
     * @param pageIndex 页码
     */
    public abstract void list(int pageIndex);

}
