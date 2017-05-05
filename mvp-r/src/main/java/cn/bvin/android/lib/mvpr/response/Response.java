package cn.bvin.android.lib.mvpr.response;

import cn.rainbow.westore.models.entity.base.BaseEntity;

/**
 * Created by bvin on 2016/7/13.
 */
public abstract class Response<T> extends BaseEntity implements Empty{

    /**
     * 获取数据.
     * @return T 数据
     */
    public abstract T get();

    /**
     * 是否是空数据.
     * @return empty
     */
    public boolean isEmpty() {
        return get() == null;
    }
}
