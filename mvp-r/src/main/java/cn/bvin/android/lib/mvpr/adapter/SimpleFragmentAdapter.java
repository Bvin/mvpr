package cn.bvin.android.lib.mvpr.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by bvin on 2016/10/11.
 * <p>这里不实现复杂的AdapterDataBinding接口是因为Fragment有自己声明周期，ViewHold这部分不需要再Fragment体现.
 */

public abstract class SimpleFragmentAdapter<T> extends FragmentStatePagerAdapter {

    private List<T> mData;

    public SimpleFragmentAdapter(FragmentManager fm, List<T> data) {
        super(fm);
        mData = data;
    }

    @Override
    public Fragment getItem(int i) {
        if (mData != null) {
            return createFragment(i, mData.get(i));
        }
        return createFragment(i, null);
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    /**
     * 根据数据创建Fragment
     * @param position 索引
     * @param t 数据
     * @return Fragment
     */
    public abstract Fragment createFragment(int position, T t);

}
