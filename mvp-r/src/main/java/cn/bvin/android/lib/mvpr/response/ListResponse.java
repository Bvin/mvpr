package cn.bvin.android.lib.mvpr.response;

import java.util.List;

import cn.rainbow.westore.models.entity.element.PaginationEntity;

/**
 * Created by bvin on 2016/7/13.
 */
public abstract class ListResponse<T> extends Response<List<T>>{

    public abstract List<T> get();

    public abstract PaginationEntity getPagination();

    /**
     * 是否是空数据.
     * <p><i>以分页参数为参考.</i>
     * @return 无分页参数，直接根据List<T>数据来判断.
     */
    @Override
    public boolean isEmpty() {
        if (getPagination() != null) {
            return getPagination().getTotal() <= 0;
        } else {
            return get() == null || get().isEmpty();
        }
    }

    /**
     * 是否有下一页.
     * <p><i>以分页参数为参考，无分页参数需要复写此方法.</i>
     * @return 无分页参数默认返回false
     */
    public boolean hasNext() {
        if (getPagination() != null) {
            return getPagination().hasNextPage();
        } else {
            return false;
        }
    }
}
