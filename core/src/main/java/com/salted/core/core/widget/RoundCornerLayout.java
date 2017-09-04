package com.salted.core.core.widget;

/*
 * Copyright (C) 2017 Legolas Kwok.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.salted.core.R;

import static android.graphics.Path.FillType.EVEN_ODD;

public class RoundCornerLayout extends FrameLayout {
    // CONSTANTS
    private final static float CORNER_RADIUS_DEFAULT = 0.0f;

    // VARIABLES
    private boolean mTopLeftEnabled = true;
    private boolean mTopRightEnabled = true;
    private boolean mBottomLeftEnabled = true;
    private boolean mBottomRightEnabled = true;
    private float mCornerRadius = CORNER_RADIUS_DEFAULT;
    private int mPendingChangeHeight;

    // IMPLEMENTS
    public RoundCornerLayout(Context context) {
        this(context, null);
    }

    public RoundCornerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setupAttributes(attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        final Path path = new Path();
        final RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        final float[] arrayRadius = {0, 0, 0, 0, 0, 0, 0, 0};

        if (mTopLeftEnabled) {
            arrayRadius[0] = mCornerRadius;
            arrayRadius[1] = mCornerRadius;
        }

        if (mTopRightEnabled) {
            arrayRadius[2] = mCornerRadius;
            arrayRadius[3] = mCornerRadius;
        }

        if (mBottomRightEnabled) {
            arrayRadius[4] = mCornerRadius;
            arrayRadius[5] = mCornerRadius;
        }

        if (mBottomLeftEnabled) {
            arrayRadius[6] = mCornerRadius;
            arrayRadius[7] = mCornerRadius;
        }

        path.setFillType(EVEN_ODD);
        path.addRoundRect(rect, arrayRadius, Path.Direction.CW);
        path.addRect(rect,Path.Direction.CW);
//        canvas.clipPath(path, Region.Op.REPLACE); // 有锯齿
//        canvas.clipPath(path);

        super.dispatchDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.TRANSPARENT); // 没有这一句的话，边缘还是黑色，而不是透明
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawPath(path, paint);
        paint.setXfermode(null);

        canvas.restoreToCount(saved);
    }

    public void setRadius(float radius) {
        mCornerRadius = radius;
        invalidate();
    }

    public float getRadius() {
        return mCornerRadius;
    }

    public void setCornerEnabled(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        mTopLeftEnabled = topLeft;
        mTopRightEnabled = topRight;
        mBottomLeftEnabled = bottomLeft;
        mBottomRightEnabled = bottomRight;
        invalidate();
    }

    private void setupAttributes(AttributeSet attrs) {
        final float radius = getPixelValue(CORNER_RADIUS_DEFAULT);
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCornerLayout);

        mCornerRadius = a.getDimension(R.styleable.RoundCornerLayout_cornerRadius, radius);

        if (a.hasValue(R.styleable.RoundCornerLayout_topEnabled)) {
            mTopLeftEnabled = a.getBoolean(R.styleable.RoundCornerLayout_topEnabled, true);
            mTopRightEnabled = mTopLeftEnabled;
        } else {
            mTopLeftEnabled = a.getBoolean(R.styleable.RoundCornerLayout_topLeftEnabled, true);
            mTopRightEnabled = a.getBoolean(R.styleable.RoundCornerLayout_topRightEnabled, true);
        }

        if (a.hasValue(R.styleable.RoundCornerLayout_bottomEnabled)) {
            mBottomLeftEnabled = a.getBoolean(R.styleable.RoundCornerLayout_bottomEnabled, true);
            mBottomRightEnabled = mBottomLeftEnabled;
        } else {
            mBottomLeftEnabled = a.getBoolean(R.styleable.RoundCornerLayout_bottomLeftEnabled, true);
            mBottomRightEnabled = a.getBoolean(R.styleable.RoundCornerLayout_bottomRightEnabled, true);
        }

        a.recycle();
    }

    private float getPixelValue(float dip) {
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getHeight() != bottom - top) {
            mPendingChangeHeight = bottom - top;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPendingChangeHeight != getMeasuredHeight()) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        }
    }
}