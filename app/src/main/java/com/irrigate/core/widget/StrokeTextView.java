package com.irrigate.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.irrigate.R;


/**
 * 说明：有边框的TextView
 * 这里的实现方式很有创意，新建一个相同的TextView，但并不加入父ViewGroup中，有位置和大小，但是
 * 作者：杨健
 * 时间：2017/6/14.
 */

public class StrokeTextView extends AppCompatTextView {
    private int textStrokeSizePix = 0;
    private int textStrokeColor = 0;
    private AppCompatTextView outlineTextView = null;

    public StrokeTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public final void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        textStrokeSizePix = ta.getDimensionPixelSize(R.styleable.StrokeTextView_textStrokeWidth, 5);
        textStrokeColor = ta.getColor(R.styleable.StrokeTextView_textStrokeColor, Color.WHITE);
        ta.recycle();

        outlineTextView = new AppCompatTextView(context, attrs, defStyle);
        TextPaint paint = outlineTextView.getPaint();
        paint.setStrokeWidth(textStrokeSizePix * 2);// 描边宽度
        paint.setStyle(Paint.Style.STROKE);
        outlineTextView.setTextColor(textStrokeColor);// 描边颜色
        outlineTextView.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        outlineTextView.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置轮廓文字
        CharSequence outlineText = outlineTextView.getText();
        if (outlineText == null || !outlineText.equals(this.getText())) {
            outlineTextView.setText(getText());
            postInvalidate();
        }
        outlineTextView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        outlineTextView.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (textStrokeSizePix > 0) {
            if (!TextUtils.equals(getText(), outlineTextView.getText())) {
                outlineTextView.setText(getText());
            }
            outlineTextView.draw(canvas);
        }
        super.onDraw(canvas);
    }
}
