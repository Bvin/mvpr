package cn.bvin.android.lib.mvpr.universal;

import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bvin.android.lib.widget.refresh.GestureRefreshLayout;
import cn.rainbow.westore.R;
import cn.rainbow.westore.base.views.holder.BaseViewHolder;

/**
 * Created by bvin on 2017/4/5.
 */
public class RefreshViewHolder extends BaseViewHolder implements GestureRefreshLayout.OnGestureStateChangeListener {

    //1.随手势放大小天到100%，
    //2.播放背景向左移动动画
    //3.当下拉超过刷新距离，小天播放帧动画和往右移动动画
    private ImageView mRefreshBackground;
    private ImageView mRefreshBackgroundNext;//由于背景图片只有屏幕那么宽，当快要移出屏幕时为了不让后面是空白
    private TextView mRefreshText;
    private ImageView mRefreshRunning;

    private Animation mMoveBackgroundAnimation;
    private Animation mNextMoveBackgroundAnimation;
    private Animation mMoveXiaotianAnimation;
    private AnimationDrawable mRunXiaotianAnimation;

    private OnAnimationCompleteListener mAnimationCompleteListener;

    public RefreshViewHolder(View itemView) {
        super(itemView);
        mRefreshBackground = (ImageView) itemView.findViewById(R.id.refresh_background);
        mRefreshBackgroundNext = (ImageView) itemView.findViewById(R.id.refresh_background_next);
        mRefreshText = (TextView) itemView.findViewById(R.id.refresh_text);
        mRefreshRunning = (ImageView) itemView.findViewById(R.id.refresh_running);
        mRunXiaotianAnimation = (AnimationDrawable) mRefreshRunning.getDrawable();

        mMoveBackgroundAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.translate_to_left);
        mRefreshBackground.setAnimation(mMoveBackgroundAnimation);

        mNextMoveBackgroundAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.translate_to_left_next);
        mRefreshBackgroundNext.setAnimation(mNextMoveBackgroundAnimation);

        mMoveXiaotianAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.translate_to_right);
        //mRefreshRunning.setAnimation(mMoveXiaotianAnimation);// 此句将会导致第一次异常触发动画
        mMoveXiaotianAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mRunXiaotianAnimation != null) {
                    mRunXiaotianAnimation.stop();
                }
                if (mAnimationCompleteListener != null) {
                    mAnimationCompleteListener.onAnimationComplete();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    public void onStartDrag(float v) {
        //背景移动动画，还没开始或者已经结束，将重新播放
        playAnimationCorrect(mRefreshBackground, mMoveBackgroundAnimation);

        playAnimationCorrect(mRefreshBackgroundNext, mNextMoveBackgroundAnimation);

    }

    private void playAnimationCorrect(View view, Animation animation) {
        if (!animation.hasStarted() || animation.hasEnded()) {
            view.startAnimation(animation);
        }
    }

    @Override
    public void onDragging(float draggedDistance, float releaseDistance) {
        float scale = Math.min(1f, draggedDistance / releaseDistance);
        ViewCompat.setScaleX(mRefreshRunning, scale);
        ViewCompat.setScaleY(mRefreshRunning, scale);
        if (draggedDistance > releaseDistance) {
            mRefreshText.setText("放手吧，我要刷新啦");
        } else {
            mRefreshText.setText("下拉刷新...");
        }
    }

    @Override
    public void onFinishDrag(float v) {
        mRefreshText.setText("努力刷新中");
        if (mRunXiaotianAnimation != null && !mRunXiaotianAnimation.isRunning())
            mRunXiaotianAnimation.start();
        mMoveXiaotianAnimation.reset();
        mRefreshRunning.clearAnimation();
        if (mMoveXiaotianAnimation != null && (mMoveXiaotianAnimation.hasEnded() || !mMoveXiaotianAnimation.hasStarted())) {
            mRefreshRunning.startAnimation(mMoveXiaotianAnimation);
        }
    }

    public void setAnimationCompleteListener(OnAnimationCompleteListener animationCompleteListener) {
        mAnimationCompleteListener = animationCompleteListener;
    }

    public interface OnAnimationCompleteListener{
        void onAnimationComplete();
    }
}
