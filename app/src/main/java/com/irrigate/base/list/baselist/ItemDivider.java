package com.irrigate.base.list.baselist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.irrigate.R;
import com.irrigate.core.widget.loadmorerecyclerview.RecyclerViewUtils;

/**
 * Created by xinyuanzhong on 2017/7/19.
 */
@SuppressWarnings("PMD.CallSuperInConstructor")
public class ItemDivider extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private int lineColor;
    private int marginColor;
    private Drawable mDivider;
    private Paint mPaint;

    private boolean isSkipLastItem = true;

    private boolean hasHeader;
    private int marginLeft;
    private int marginRight;
    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    private final Rect mBounds = new Rect();

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private ItemDivider(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * 创建一个高度为height的间隔
     *
     * @param context   Current context, it will be used to access resources.
     * @param height    间隔高度
     * @param hasHeader 是否有头部，一定要确保和Adapter一致，不然会算错
     */
    public ItemDivider(Context context, int height, boolean hasHeader) {
        this(context, VERTICAL);
        this.hasHeader = hasHeader;
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.setAlpha(0);
        drawable.setIntrinsicWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        drawable.setIntrinsicHeight(height);
        setDividerDrawable(drawable);
    }

    /**
     * 创建一个间隔线，可以自定义颜色和左右边距
     *
     * @param context
     * @param height
     * @param hasHeader
     * @param color
     * @param marginColor
     * @param marginLeft
     * @param marginRight
     */
    public ItemDivider(Context context, int height, boolean hasHeader,
                       int color, int marginColor, int marginLeft, int marginRight) {
        this(context, VERTICAL);
        this.lineColor = color;
        this.marginColor = marginColor;
        this.mPaint = new Paint();
        mPaint.setColor(color);
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.hasHeader = hasHeader;
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.setAlpha(0);
        drawable.setIntrinsicWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        drawable.setIntrinsicHeight(height);
        setDividerDrawable(drawable);
    }

    public ItemDivider(Context context, boolean hasHeader, int colorResId, int marginLeftResId, int marginRightResId) {
        this(context, VERTICAL);
        int dividerColor = context.getResources().getColor(colorResId);
        this.lineColor = dividerColor;
        this.marginColor = Color.TRANSPARENT;
        this.mPaint = new Paint();
        mPaint.setColor(dividerColor);
        this.marginLeft = context.getResources().getDimensionPixelSize(marginLeftResId);
        this.marginRight = context.getResources().getDimensionPixelSize(marginRightResId);
        this.hasHeader = hasHeader;
        this.isSkipLastItem = false;
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.setAlpha(0);
        drawable.setIntrinsicWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        drawable.setIntrinsicHeight(context.getResources().getDimensionPixelSize(R.dimen.global_divider_width));
        setDividerDrawable(drawable);
    }

    /**
     * 创建一个间隔线，可以自定义颜色
     *
     * @param context
     * @param height
     * @param hasHeader
     * @param color
     */
    public ItemDivider(Context context, int height, boolean hasHeader,
                       int color) {
        this(context, height, hasHeader, color, 0, 0, 0);

    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    private void setDividerDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @SuppressLint("NewApi")
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            //跳过头部和尾部的绘制
            if (!skipDecor(parent, position)) {
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
                final int top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left + marginLeft, top, right - marginRight, bottom);
                //分成画线和空白间隔
                if (lineColor != 0) {
                    mPaint.setColor(marginColor);
                    canvas.drawRect(left, top, marginLeft, bottom, mPaint);
                    canvas.drawRect(right - marginRight, top, right, bottom, mPaint);
                    mPaint.setColor(lineColor);
                    canvas.drawRect(mDivider.getBounds(), mPaint);
                } else {
                    mDivider.draw(canvas);
                }
            }
        }
        canvas.restore();
    }

    @SuppressLint("NewApi")
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
            final int left = right - mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (skipDecor(parent, position)) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
        }
    }

    private boolean skipDecor(RecyclerView recyclerView, int position) {
        return isHeader(position, recyclerView)
                || isNeedSkipLastItem(recyclerView, position)
                || RecyclerViewUtils.isFooter(position, recyclerView);
    }

    private boolean isNeedSkipLastItem(RecyclerView recyclerView, int position) {
        return isSkipLastItem && RecyclerViewUtils.isLastItem(position, recyclerView);
    }

    private boolean isHeader(int position, RecyclerView recyclerView) {
        return position < getHeaderCount(recyclerView);
    }

    public int getHeaderCount(RecyclerView parent) {
        int innerHeaderCount = hasHeader ? 1 : 0;
        return RecyclerViewUtils.getHeaderCount(parent) + innerHeaderCount;
    }

}
