package cn.bvin.android.lib.mvpr.present;

/**
 * 抽象出来的List页面行为.
 * <p><i>调用时机说明：</i>
 * <ul>
 *     <li>start —— 进入页面时调用
 *     <li>reStart —— 出错视图点击重试时调用
 *     <li>refresh —— 下拉刷新时调用
 *     <li>nextPage —— 加载更多时调用
 *     <li>reLoad —— 加载更多出错，点击重新加载时调用
 * </ul>
 */
public interface ListPresenter extends BasePresenter{

    int REQUEST_TYPE_START = 0;
    int REQUEST_TYPE_RESTART = 1;
    int REQUEST_TYPE_REFRESH = 2;
    int REQUEST_TYPE_LOAD = 3;
    int REQUEST_TYPE_RELOAD = 4;

    /**
     * 重新开始.
     */
    void reStart();

    /**
     * 刷新.
     */
    void refresh();

    /**
     * 下一页
     */
    void load();

    /**
     * 重新加载.
     */
    void reLoad();

}
