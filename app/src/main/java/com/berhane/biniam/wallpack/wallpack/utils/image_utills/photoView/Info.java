/*
 * DayTime:11/8/18 4:28 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.image_utills.photoView;

import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.ImageView;

/**
 * Info.
 * */

public class Info {

    RectF mRect = new RectF();


    RectF mImgRect = new RectF();

    RectF mWidgetRect = new RectF();

    RectF mBaseRect = new RectF();

    PointF mScreenCenter = new PointF();

    float mScale;

    float mDegrees;

    ImageView.ScaleType mScaleType;

    public Info(RectF rect, RectF img, RectF widget, RectF base, PointF screenCenter, float scale, float degrees, ImageView.ScaleType scaleType) {
        mRect.set(rect);
        mImgRect.set(img);
        mWidgetRect.set(widget);
        mScale = scale;
        mScaleType = scaleType;
        mDegrees = degrees;
        mBaseRect.set(base);
        mScreenCenter.set(screenCenter);
    }

    public RectF getImageBound() {
        return mRect;
    }

    public float getScale() {
        return mScale;
    }
}
