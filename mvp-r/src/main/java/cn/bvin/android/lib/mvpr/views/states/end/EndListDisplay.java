package cn.bvin.android.lib.mvpr.views.states.end;

import java.util.List;

import cn.rainbow.westore.base.views.states.End;

/**
 * 数据集合.
 */

public interface EndListDisplay<T> extends End {


    /**
     * 展示内容（空列表）.
     * @param response 数据集合
     *
     */
    void show(List<T> response, boolean clearList, boolean hasNext);
}
