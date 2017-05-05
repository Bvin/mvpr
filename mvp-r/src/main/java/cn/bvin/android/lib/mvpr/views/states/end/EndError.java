package cn.bvin.android.lib.mvpr.views.states.end;

import cn.rainbow.westore.base.views.states.End;

/**
 * 加载结束后,数据出错的状态..
 */

public interface EndError extends End {
    void error(String message);
}
