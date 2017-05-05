package cn.bvin.android.lib.mvpr.views;

/**
 * 简单数据展示.
 * <p>耗时数据可使用{@link SimpleResponseView}(SimpleDisplay<T>结合{@link ResponseView}组合而成)
 * 在各种状态下的视图表现.
 * <p>非耗时数据（即现成数据无需等待）可直接实现此接口，省去Response等多种复杂状态.
 * @param <T> 待展示数据类型
 */
public interface SimpleDisplay<T>{
    /**
     * show data
     * @param t data
     */
    void show(T t);
}
