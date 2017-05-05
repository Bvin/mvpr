package cn.bvin.android.lib.mvpr.views.holder;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nineoldandroids.view.ViewHelper;

import cn.rainbow.westore.R;
import cn.rainbow.westore.common.utils.ItemTouchHelper;

/**
 * Created by bvin on 2016/11/16.
 */

public class SwipeDeleteViewHolder<T> extends BaseViewHolder implements ItemTouchHelper.ItemBackgroundAction, View.OnClickListener {

    private static final String TAG = "SwipeDeleteViewHolder";
    private static final int KEY_ITEM = R.id.goods_item;
    private static final int KEY_POS = R.id.goods_position;

    private Button mBtDelete;
    private OnItemRemoveListener<T> mOnItemRemoveListener;
    private View mContentView;

    public SwipeDeleteViewHolder(Context context, ViewGroup parent, View content) {
        super(makeView(context, parent, content));
        mBtDelete = (Button) itemView.findViewById(R.id.bt_delete);
        mBtDelete.setEnabled(false);
        mBtDelete.setOnClickListener(this);
        mContentView = content;
    }

    private SwipeDeleteViewHolder(View itemView) {
        super(itemView);
    }

    private static View makeView(Context context, ViewGroup parent, View content){
        ViewGroup container = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.list_item_delete_container, parent, false);
        container.addView(content);
        return container;
    }

    @Override
    public float getActionWidth() {
        return mBtDelete.getWidth();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtDelete){
            if (mOnItemRemoveListener != null) {
                T t = v.getTag(KEY_ITEM) != null ? (T) v.getTag(KEY_ITEM) : null;
                int position = v.getTag(KEY_POS) != null ? (int) v.getTag(KEY_POS) : -1;
                mOnItemRemoveListener.onItemRemoved(v, t, position);
            }
        }
    }

    public void setDeletable(boolean enable){
        if (mBtDelete != null) {
            mBtDelete.setEnabled(enable);
        }
    }

    public void bindItemData(T t, int position){
        mBtDelete.setTag(KEY_ITEM,t);
        mBtDelete.setTag(KEY_POS,position);
    }


    public void setOnItemRemoveListener(OnItemRemoveListener<T> onItemRemoveListener) {
        mOnItemRemoveListener = onItemRemoveListener;
    }

    public View getContentView() {
        return mContentView;
    }

    public interface OnItemRemoveListener<T>{
        void onItemRemoved(View view, T t, int position);
    }

    public interface OnSwipeListener{
        void onSwiping();
        void onSwiped();
    }

    public static class SwipeDeleteCallback extends ItemTouchHelper.SimpleCallback {

        private OnSwipeListener mOnSwipeListener;
        private SwipeDeleteViewHolder mSwipeDeleteViewHolder;

        /**
         * 滑动菜单Callback（左滑）.
         * @param swipeListener
         */
        public SwipeDeleteCallback(OnSwipeListener swipeListener) {
            //不可拖动，可向左滑动
            super(0, 0);
            mOnSwipeListener = swipeListener;
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof SwipeDeleteViewHolder) {
                mSwipeDeleteViewHolder = (SwipeDeleteViewHolder) viewHolder;
                return ItemTouchHelper.START;//往左侧可划动
            }
            return super.getSwipeDirs(recyclerView, viewHolder);
        }


        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (dX == 0 && dY != 0)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            else {
                if (viewHolder instanceof SwipeDeleteViewHolder) {
                    float desireDx = dX;
                    if (getSwipeDirs(recyclerView, viewHolder) == ItemTouchHelper.START || getSwipeDirs(recyclerView, viewHolder) == ItemTouchHelper.LEFT) {
                        float actionWidth = ((SwipeDeleteViewHolder) viewHolder).getActionWidth();
                        if (desireDx <= -actionWidth) {
                            desireDx = -actionWidth;
                            if (mSwipeDeleteViewHolder != null) {
                                mSwipeDeleteViewHolder.setDeletable(true);
                            }
                            if (mOnSwipeListener != null) mOnSwipeListener.onSwiped();
                        }else {
                            if (mSwipeDeleteViewHolder != null) {
                                mSwipeDeleteViewHolder.setDeletable(false);
                            }
                            if (mOnSwipeListener != null) mOnSwipeListener.onSwiping();
                        }
                    }
                    ViewHelper.setTranslationX(((SwipeDeleteViewHolder) viewHolder).getContentView(), desireDx);
                }
            }
        }
    }
}
