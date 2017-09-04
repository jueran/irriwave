package com.irrigate.core.widget.pickerview;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.animation.DecelerateInterpolator;

import com.irrigate.R;

@SuppressWarnings({"PMD.SingularField","PMD.AvoidLiteralsInIfCondition"})
public class ScrollPickerView extends android.support.v7.widget.AppCompatTextView {
    private static final int DEFAULT_TEXT_SIZE_MAX = 40;
    private static final int DEFAULT_TEXT_SIZE_MIN = 20;
    private static final int DEFAULT_TEXT_LINES = 3;
    private static final int VIEW_OFFSET = 3;
    private static final float INIT_OFFSET = 0.0f;

    private float textSizeMax;
    private float textSizeMin;
    private int textLines;
    private int textColorDefault;
    private int textColorSelected;
    private int divideColorDefault;
    private int divideColorSelected;
    private float lineHeight;
    private Paint paint;
    private ValueAnimator animator;
    private VelocityTracker velocityTracker;
    private Adapter adapter;
    private OnSelectedIndexChangedListener onSelectedIndexChangedListener;
    private int currentSelectIndex;
    private float relativeCurrentSelectOffset;
    private int lastTouchPositionPixel;

    public ScrollPickerView(Context context) {
        this(context, null);
    }

    public ScrollPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.adapter = new Adapter() {
            public int getCount() {
                return ScrollPickerView.this.textLines;
            }

            public Object getItem(int position) {
                return null;
            }

            public String getString(int position) {
                return "测试数据" + position;
            }

            public boolean isEmpty() {
                return false;
            }
        };
        this.currentSelectIndex = 0;
        this.initFieldValues(context, attrs, defStyleAttr);
        this.initPaint();
        this.initVelocityTracker();
    }

    private void initFieldValues(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScrollPickerView,
                defStyleAttr, 0);
        this.textSizeMax = (float) array.getDimensionPixelSize(
                R.styleable.ScrollPickerView_text_size_max, DEFAULT_TEXT_SIZE_MAX);
        this.textSizeMin = (float) array.getDimensionPixelSize(
                R.styleable.ScrollPickerView_text_size_min, DEFAULT_TEXT_SIZE_MIN);
        this.textLines = array.getInt(R.styleable.ScrollPickerView_text_lines, DEFAULT_TEXT_LINES);
        if (this.textLines < VIEW_OFFSET) {
            this.textLines = VIEW_OFFSET;
        }

        if (this.textLines % 2 == 0) {
            ++this.textLines;
        }

        this.textColorDefault = array.getColor(R.styleable.ScrollPickerView_text_color_default,
                Color.parseColor("#ff8023"));
        this.textColorSelected = array.getColor(R.styleable.ScrollPickerView_text_color_selected,
                Color.parseColor("#f5f6f7"));
        this.divideColorDefault = array.getColor(R.styleable.ScrollPickerView_divide_color_default,
                this.textColorDefault);
        this.divideColorSelected = array.getColor(
                R.styleable.ScrollPickerView_divide_color_selected,
                this.textColorSelected);
        array.recycle();
    }

    private void initPaint() {
        this.paint = new Paint();
        this.paint.setTextAlign(Align.CENTER);
        this.paint.setTextSize(this.getTextSize());
        this.paint.setColor(this.textColorDefault);
        this.paint.setAntiAlias(true);
    }

    private void initVelocityTracker() {
        this.velocityTracker = VelocityTracker.obtain();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.lineHeight = this.getMeasuredHeight() / (float) this.textLines;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.adapter.isEmpty()) {
            for (int i = this.currentSelectIndex - this.textLines / 2;
                 i <= this.currentSelectIndex + this.textLines / 2; ++i) {
                if (i >= 0 && i < this.adapter.getCount()) {
                    this.drawTextAndLine(canvas, this.adapter.getString(i), i);
                }
            }
        }

    }

    private void drawTextAndLine(Canvas canvas, String text, int position) {
        Paint paint = this.getTextPaint(position);
        int horizontalCenter = this.getMeasuredWidth() / 2;
        float top = this.lineHeight *
                (float) (this.textLines / 2 + position - this.currentSelectIndex) +
                this.relativeCurrentSelectOffset + 3.0F;
        float bottom = top + this.lineHeight;
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = (bottom + top - (float) fontMetrics.bottom -
                (float) fontMetrics.top) / 2.0F;
        canvas.drawText(text, (float) horizontalCenter, baseline, paint);
        Paint paint1 = this.getDividePaint(position);
        canvas.drawLine(0.0F, bottom, (float) this.getMeasuredWidth(), bottom, paint1);
    }

    private Paint getTextPaint(int position) {
        if (position == this.currentSelectIndex) {
            this.paint.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            this.paint.setTypeface(Typeface.DEFAULT);
        }

        this.paint.setColor(this.getTextColor(position));
        this.paint.setTextSize(this.getTextSize(position));
        return this.paint;
    }

    private Paint getDividePaint(int position) {
        this.paint.setTypeface(Typeface.DEFAULT);
        this.paint.setColor(this.getDivideColor(position));
        this.paint.setStrokeWidth(1.0F);
        return this.paint;
    }

    private int getTextColor(int position) {
        return position == this.currentSelectIndex ? this.textColorSelected : this.textColorDefault;
    }

    private int getDivideColor(int position) {
        return position ==
                this.currentSelectIndex ? this.divideColorSelected : this.divideColorDefault;
    }

    private float getTextSize(int position) {
        float offsetPex;
        if (position == this.currentSelectIndex) {
            offsetPex = Math.abs(this.relativeCurrentSelectOffset);
        } else if (position < this.currentSelectIndex) {
            offsetPex = (float) Math.abs(position - this.currentSelectIndex) * this.lineHeight -
                    this.relativeCurrentSelectOffset;
        } else {
            offsetPex = (float) Math.abs(position - this.currentSelectIndex) * this.lineHeight +
                    this.relativeCurrentSelectOffset;
        }
        return this.textSizeMax - (this.textSizeMax - this.textSizeMin) * offsetPex /
                (float) this.getMeasuredHeight() / 2;
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.velocityTracker.addMovement(event);
        switch (event.getActionMasked()) {
            case 0:
                this.lastTouchPositionPixel = (int) event.getY();
                this.cancleAnimator();
                break;
            case 1:
            case 3:
                this.velocityTracker.computeCurrentVelocity(50, 2.0F * this.lineHeight);
                int yVelocity = (int) this.velocityTracker.getYVelocity();
                Log.e("100毫秒的平均速度", "" + yVelocity);
                this.inertiaMove(yVelocity);
                break;
            case 2:
                float offset = event.getY() - (float) this.lastTouchPositionPixel;
                this.lastTouchPositionPixel = (int) event.getY();
                this.postOffset(offset);
                break;
            default:
                break;
        }

        return true;
    }

    private void inertiaMove(int speed) {
        this.cancleAnimator();
        if ((float) Math.abs(speed) < this.lineHeight) {
            this.adjustAtInertiaMoveEnd();
        } else {
            this.animator = ValueAnimator.ofInt(new int[]{speed / 2, 0});
            this.animator.setDuration(500L);
            this.animator.setInterpolator(new DecelerateInterpolator());
            this.animator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float curValue = (float) ((Integer) animation.getAnimatedValue()).intValue();
                    ScrollPickerView.this.postOffset(curValue);
                }
            });
            this.animator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                    //Empty
                }

                public void onAnimationEnd(Animator animation) {
                    ScrollPickerView.this.adjustAtInertiaMoveEnd();
                }

                public void onAnimationCancel(Animator animation) {
                    //Empty
                }

                public void onAnimationRepeat(Animator animation) {
                    //Empty
                }
            });
            this.animator.start();
        }
    }

    private void adjustAtInertiaMoveEnd() {
        this.cancleAnimator();
        if (this.relativeCurrentSelectOffset != INIT_OFFSET) {
            this.animator = ValueAnimator.ofFloat(
                    new float[]{this.relativeCurrentSelectOffset, INIT_OFFSET});
            this.animator.setDuration(100L);
            this.animator.setInterpolator(new DecelerateInterpolator());
            this.animator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    ScrollPickerView.this.relativeCurrentSelectOffset
                            = ((Float) animation.getAnimatedValue()).floatValue();
                    ScrollPickerView.this.invalidate();
                }
            });
            this.animator.start();
        }
    }

    private void cancleAnimator() {
        if (this.animator != null && this.animator.isRunning()) {
            this.animator.removeAllUpdateListeners();
            this.animator.cancel();
        }
    }

    public void postOffset(float offset) {
        float o = this.revisePostOffset(offset);
        this.relativeCurrentSelectOffset += o;
        this.adjustCurrentSelectAndOffset();
        this.invalidate();
    }

    private float revisePostOffset(float postOffset) {
        float useAbleOffset;
        if (postOffset > 0.0F) {
            useAbleOffset = (float) this.currentSelectIndex * this.lineHeight -
                    this.relativeCurrentSelectOffset;
            if (postOffset >= useAbleOffset) {
                return useAbleOffset;
            }
        }

        if (postOffset < 0.0F) {
            useAbleOffset = (float) (this.currentSelectIndex - (this.adapter.getCount() - 1)) *
                    this.lineHeight - this.relativeCurrentSelectOffset;
            if (postOffset < useAbleOffset) {
                return useAbleOffset;
            }
        }

        return postOffset;
    }

    private void adjustCurrentSelectAndOffset() {
        int offsetPix = (int) this.relativeCurrentSelectOffset;
        int offsetRows = 0;
        if ((float) Math.abs(offsetPix) >= this.lineHeight / 2.0F) {
            offsetRows = (int) ((float) offsetRows - (float) offsetPix / this.lineHeight);
            if (offsetPix < 0) {
                ++offsetRows;
            } else {
                --offsetRows;
            }
        }

        this.updateViewAndCurrentSelectIndexAndOffset(offsetRows);
    }

    public void updateViewAndCurrentSelectIndexAndOffset(int offsetRows) {
        int expectIndex = this.currentSelectIndex + offsetRows;
        if (expectIndex < 0) {
            expectIndex = 0;
        } else if (expectIndex >= this.adapter.getCount()) {
            expectIndex = this.adapter.getCount() - 1;
        }

        int offsetIndex = expectIndex - this.currentSelectIndex;
        this.currentSelectIndex = expectIndex;
        if (this.onSelectedIndexChangedListener != null) {
            this.onSelectedIndexChangedListener.onSelectedIndexChange(this.currentSelectIndex);
        }

        this.relativeCurrentSelectOffset += this.lineHeight * (float) offsetIndex;
    }

    public void setCurrentSelectIndex(int currentSelectIndex) {
        if (this.currentSelectIndex != currentSelectIndex) {
            this.currentSelectIndex = currentSelectIndex;
            if (this.onSelectedIndexChangedListener != null) {
                this.onSelectedIndexChangedListener.onSelectedIndexChange(currentSelectIndex);
            }
        }

        this.invalidate();
    }

    public int getCurrentSelectIndex() {
        return this.currentSelectIndex;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public void setOnSelectedIndexChangedListener(
            OnSelectedIndexChangedListener onSelectedIndexChangedListener) {
        this.onSelectedIndexChangedListener = onSelectedIndexChangedListener;
    }

    public interface OnSelectedIndexChangedListener {
        void onSelectedIndexChange(int var1);
    }
}