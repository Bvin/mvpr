package cn.bvin.android.lib.mvpr.fresco;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import javax.annotation.Nullable;

/**
 * Created by bvin on 2016/10/17.
 * 1. 如果手动设置了宽高，那不用做任何处理
 * 2. 如果设置了期望宽高，将会覆盖布局里设定的宽高
 */

public class Fresco {

    private static final String TAG = "Fresco";

    private String mUrl;
    private ResizeOptions mResizeOptions;
    private boolean mResize;
    private boolean mFitResize;
    private int mDesiredWidth;
    private int mDesiredHeight;
    private float mAspectRatio;
    private int mPlaceholderImage;

    private Fresco(String url) {
        this.mUrl = url;
    }

    public static Fresco load(String url){
        return new Fresco(url);
    }

    public Fresco resize(int width, int height){
        mResize = true;
        mFitResize = false;

        if (width > 0 && height > 0)
            mResizeOptions = new ResizeOptions(width, height);

        mDesiredWidth = width;
        mDesiredHeight = height;
        return this;
    }

    public Fresco fitResize(){
        mFitResize = true;
        return this;
    }

    public Fresco ratio(float ratio){
        mAspectRatio = ratio;
        return this;
    }

    public Fresco place(@IdRes int resId){
        mPlaceholderImage = resId;
        return this;
    }

    public void into(final DraweeView image){

        if (mPlaceholderImage != 0 && image.getHierarchy() instanceof GenericDraweeHierarchy) {
            ((GenericDraweeHierarchy) image.getHierarchy()).setPlaceholderImage(mPlaceholderImage);
        }

        ImageRequestBuilder requestBuilder = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(mUrl));

        if (mFitResize && image.getLayoutParams().width > 0 && image.getLayoutParams().height > 0) {
            mResizeOptions = new ResizeOptions(image.getLayoutParams().width, image.getLayoutParams().height);
            Log.d(TAG, "resize option " + mResizeOptions.width + "," + mResizeOptions.height);
        }

        if (mResizeOptions != null) {// ResizeOptions的意义应该是图片宽高大于控件宽高，而且已知控件宽高
            requestBuilder.setResizeOptions(mResizeOptions);
        }

        ImageRequest imageRequest = requestBuilder.build();
        DraweeController draweeController = com.facebook.drawee.backends.pipeline.Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>(){
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (mResize && imageInfo != null) {
                            Log.d(TAG, "onFinalImageSet "+imageInfo.getWidth()+ "," + imageInfo.getHeight());
                            updateViewSize(image, imageInfo, mDesiredWidth, mDesiredHeight);
                        }
                    }

                    @Override
                    public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                        super.onIntermediateImageSet(id, imageInfo);
                        if (mResize && imageInfo != null) {
                            Log.d(TAG, "onIntermediateImageSet "+imageInfo.getWidth()+ "," + imageInfo.getHeight());
                            updateViewSize(image, imageInfo, mDesiredWidth, mDesiredHeight);
                        }
                    }
                })
                .build();

        image.setController(draweeController);
    }

    // http://stackoverflow.com/questions/33955510/facebook-fresco-using-wrap-conent
    private void updateViewSize(DraweeView draweeView, ImageInfo imageInfo, int desiredWidth, int desiredHeight) {

        if (imageInfo != null) {

            int originWidth = draweeView.getLayoutParams().width;
            int originHeight = draweeView.getLayoutParams().height;
            if (desiredWidth > 0 && desiredHeight > 0) {//都设置了宽高
                if (desiredWidth > 0) {// 设置了期望宽度
                    if (originWidth != desiredWidth) {// 实际宽度不是期望宽度，则更新为期望宽度
                        draweeView.getLayoutParams().width = desiredWidth;
                    }
                }

                if (desiredHeight > 0) {
                    if (originHeight != desiredHeight) {
                        draweeView.getLayoutParams().height = desiredHeight;
                    }
                }

            } else if (desiredWidth > 0) {
                if (originWidth != desiredWidth) {
                    draweeView.getLayoutParams().width = desiredWidth;
                }
                draweeView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (mAspectRatio > 0) {
                    draweeView.setAspectRatio(mAspectRatio);
                } else {
                    draweeView.setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
                }
            } else if (desiredHeight > 0) {
                if (originHeight != desiredHeight) {
                    draweeView.getLayoutParams().height = desiredHeight;
                }
                draweeView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (mAspectRatio > 0) {
                    draweeView.setAspectRatio(mAspectRatio);
                }else {
                    draweeView.setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
                }
            } else {
                if (originWidth > 0 && originHeight > 0) {
                    // 指定宽高，无需做任何处理
                }else {
                    draweeView.getLayoutParams().width = imageInfo.getWidth();
                    draweeView.getLayoutParams().height = imageInfo.getHeight();
                }
            }
            Log.d(TAG, "updateViewSize "+draweeView.getLayoutParams().width+ "," + draweeView.getLayoutParams().height);
        }
    }


}
