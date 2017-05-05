package cn.bvin.android.lib.mvpr.views.states.extra;

/**
 * 用于下拉刷新和加载更多功能..
 */

public interface Load {

    /** 顶部位置（下拉刷新） */
    int POSITION_TOP = 0;

    /** 底部位置（加载更多） */
    int POSITION_BOTTOM = 1;

    /**
     * 开始加载（下拉刷新或加载更多）.
     * @param position loading应显示的位置
     */
    void load(int position);
}
