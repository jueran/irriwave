package com.irrigate.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.irrigate.R;

/**
 * 给定起始角度和终点角度来描述进度条的形状
 * <p>
 * Created by jian on 16/7/12.
 */
public class ArcProgressBar extends AppCompatImageView {
    private int progressBackground = 0xff00ff00;
    private int color = 0xffff0000;
    private float width = 3f;
    private float radius = 200f;
    private float angleStart = 0f;
    private float angleEnd = 300f;
    private float progressRatio = 0.6f;
    private Paint paint;

    public ArcProgressBar(Context context) {
        super(context);
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
    }

    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar);
        progressBackground = typedArray.getColor(R.styleable.ArcProgressBar_progress_background, 0xff00ff00);
        color = typedArray.getColor(R.styleable.ArcProgressBar_progress_color, 0xffff0000);
        width = typedArray.getDimensionPixelSize(R.styleable.ArcProgressBar_progress_width, 3);

        radius = typedArray.getDimensionPixelSize(R.styleable.ArcProgressBar_progress_radius, 200);

        angleStart = typedArray.getFloat(R.styleable.ArcProgressBar_progress_angle_start, 90);
        angleEnd = typedArray.getFloat(R.styleable.ArcProgressBar_progress_angle_end, 270);
        progressRatio = typedArray.getFloat(R.styleable.ArcProgressBar_progress_ratio, 0.5f);
        typedArray.recycle();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(width);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackGround(canvas);
        drawProgress(canvas);
    }

    private void drawBackGround(Canvas canvas) {
        Path path = new Path();
        path.addArc(getRect(), angleStart, angleEnd - angleStart);
        paint.setColor(progressBackground);
        canvas.drawPath(path, paint);
    }

    private void drawProgress(Canvas canvas) {
        Path path = new Path();
        path.addArc(getRect(), angleStart, (angleEnd - angleStart) * progressRatio);
        paint.setColor(color);
        canvas.drawPath(path, paint);
    }

//    虚线部分,目前不需要是的
//    private void drawComet(Canvas canvas) {
//        if (comet == null)
//            return;
//        Bitmap bitmap = ((BitmapDrawable) comet).getBitmap();
//        float offsetAngle = 360 - ((360 - angleRange) / 2 + angleRange * progressRatio);
//        double radianAngle = offsetAngle / 180 * Math.PI;
//        float offsetX = (float) (getMeasuredWidth() / 2 - bitmap.getWidth() / 2 + radius * Math.sin(radianAngle));
//        float offsetY = (float) (getMeasuredHeight() / 2 - bitmap.getHeight() / 2 + radius * Math.cos(radianAngle));
//        Matrix matrix = new Matrix();
//        matrix.reset();
//        matrix.setTranslate(offsetX, offsetY);
//        float rotateAngle = ((360 - angleRange) / 2 + angleRange * progressRatio) - 180;
//        matrix.preRotate(rotateAngle, bitmap.getWidth() / (float) 2, bitmap.getHeight() / (float) 2);
//        canvas.drawBitmap(bitmap, matrix, null);
//    }

    private RectF getRect() {
        float width = (float) getMeasuredWidth();
        float height = (float) getMeasuredHeight();

        float offsetX = (width - radius * 2) / 2;
        float offsetY = (height - radius * 2) / 2;

        return new RectF(offsetX, offsetY, width - offsetX, height - offsetY);
    }

    public float getProgressRatio() {
        return progressRatio;
    }

    public void setProgressRatio(float ratio) {
        this.progressRatio = ratio;
        invalidate();
    }
}
