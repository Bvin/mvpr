package cn.bvin.android.lib.mvpr.views.states.end;

import cn.rainbow.westore.base.views.states.End;

/**
 * 加载结束后,数据为空的状态.
 */

public interface EndEmpty extends End {
    void empty(String message);
}
