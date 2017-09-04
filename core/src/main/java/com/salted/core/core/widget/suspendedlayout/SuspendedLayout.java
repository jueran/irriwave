package com.salted.core.core.widget.suspendedlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Description:
 * SuspendedLayout ChildScrollableView
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-29.
 */

@SuppressWarnings("PMD.SwitchDensity")
public class SuspendedLayout extends LinearLayout {
    public static final float VIEW_PAGER_SCROLL_DIRECTION_SCALE = 2.2f;
    private static final int SUSPENDED_LAYOUT_Y_MIN = 0;
    private int suspendedLayoutYMax = 0;

    private int touchSlop;
    private int minimumVelocity;
    private int maximumVelocity;

    private float lastDownX;
    private float lastDownY;
    private float lastMoveY;
    private float lastMoveX;
    private int flingDownLastScrollerY; // 为fling down服务

    private boolean needUpdateMoveOrientationStatus;
    private boolean isMoveVertical;

    private Scroller scroller;
    private VelocityTracker velocityTracker;
    private MultiScrollableViewHelper scrollableViewHelper;

    private DIRECTION upDirection;
    private DIRECTION moveDirection;
    private boolean isFirstFlingDownChildScrollableView;

    private OnScrollListener onScrollListener;

    /*--------------------------------------------------------------------------------------------*/

    public MultiScrollableViewHelper getHelper() {
        return scrollableViewHelper;
    }

    /*--------------------------------------------------------------------------------------------*/

    public SuspendedLayout(Context context) {
        super(context);
        init(context);
    }

    public SuspendedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /*-------------------------------------------------*/

    private void init(Context context) {
        scrollableViewHelper = new MultiScrollableViewHelper();
        scroller = new Scroller(context);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop = configuration.getScaledTouchSlop();
        minimumVelocity = configuration.getScaledMinimumFlingVelocity();
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View headView = getChildAt(0);

        measureChildWithMargins(headView, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);

        int headHeight = headView.getMeasuredHeight();
        suspendedLayoutYMax = -headHeight;

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec) + headHeight, MeasureSpec.EXACTLY));
    }

    /*--------------------------------------------------------------------------------------------*/

    @SuppressWarnings("checkstyle:AvoidNestedBlocks")
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentEventX = ev.getX();
        float currentEventY = ev.getY();
        int shiftX = (int) Math.abs(currentEventX - lastDownX);
        int shiftY = (int) Math.abs(currentEventY - lastDownY);
        DISPATCH dispatchResult = DISPATCH.DEFAULT;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                lastDownX = currentEventX;
                lastDownY = currentEventY;
                updateMovePosition(ev);

                isMoveVertical = true;

                needUpdateMoveOrientationStatus = true;
                isFirstFlingDownChildScrollableView = true;

                scroller.forceFinished(true);
                addVelocityTracker(ev, true);

                dispatchResult = DISPATCH.DOWN_THROUGH;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!isValidDistance(shiftX, shiftY)) {
                    dispatchResult = DISPATCH.MOVE_THROUGH;
                } else {
                    updateMoveOrientationStatus(shiftX, shiftY);
                    if (!isMoveVertical) {
                        // 横滑，全部交给里边处理，并且固定Y位置
                        ev.setLocation(ev.getX(), lastMoveY);
                        dispatchResult = DISPATCH.MOVE_THROUGH;
                    } else {
                        addVelocityTracker(ev, false);
                        // 只要是竖直滑动，就限制x，滑里外都固定
                        ev.setLocation(lastMoveX, ev.getY());
                        updateMoveDirection(currentEventY);
                        if (upAndCanWholeScrollUp() || downAndCanWholeScrollDown()) {
                            scrollSuspendedLayout(currentEventY);
                            dispatchResult = DISPATCH.MOVE_ABANDON;
                        } else {
                            // 外面滑动不了的情况，全部交给里边处理，并且固定X位置
                            dispatchResult = DISPATCH.MOVE_THROUGH;
                        }
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (!isValidDistance(shiftX, shiftY)) {
                    // 一定要through，否则没有点击事件
                    dispatchResult = DISPATCH.UP_THROUGH;
                } else {
                    updateMoveOrientationStatus(shiftX, shiftY);
                    if (!isMoveVertical) {
                        dispatchResult = DISPATCH.UP_THROUGH;
                    } else {
                        float yVelocity = getYVelocity();
                        if (isValidVelocity(yVelocity)) {
                            flingDownLastScrollerY = getScrollY();
                            updateUpDirection(yVelocity);
                            flingSuspendedLayout((int) yVelocity);

                            dispatchResult = DISPATCH.UP_ABANDON;
                        } else {
                            dispatchResult = DISPATCH.UP_ABANDON;
                        }
                    }
                }
                break;
            }
            default:
                break;
        }

        switch (dispatchResult) {
            case DOWN_THROUGH:
                super.dispatchTouchEvent(ev);
                return true;

            case MOVE_THROUGH:
                updateMovePosition(ev);
                return super.dispatchTouchEvent(ev);
            case MOVE_ABANDON:
                updateMovePosition(ev); // 事件拦下来也要更新，因为之后也要用move position，要连贯
                return true;

            case UP_THROUGH:
                return super.dispatchTouchEvent(ev);
            case UP_ABANDON:
                ev.setAction(MotionEvent.ACTION_CANCEL);
                super.dispatchTouchEvent(ev);
                return true;

            case DEFAULT:
                return super.dispatchTouchEvent(ev);
            default:
                return super.dispatchTouchEvent(ev);
        }
    }

    private void updateMovePosition(MotionEvent ev) {
        lastMoveY = ev.getY();
        lastMoveX = ev.getX();
    }

    /*-------------------------------------------------*/


    private void scrollSuspendedLayout(float currentEventY) {
        float deltaY = lastMoveY - currentEventY;
        scrollBy(0, (int) (deltaY + 0.5));
    }

    private void flingSuspendedLayout(int yVelocity) {
        scroller.fling(0, getScrollY(), 0, yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        invalidate();
    }

    private float getYVelocity() {
        velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
        return -velocityTracker.getYVelocity();
    }

    private void updateUpDirection(float yVelocity) {
        upDirection = yVelocity > 0 ? DIRECTION.UP : DIRECTION.DOWN;
    }

    private void updateMoveDirection(float currentEventY) {
        moveDirection = currentEventY <= lastMoveY ? DIRECTION.UP : DIRECTION.DOWN;
    }

    private boolean isValidVelocity(float velocity) {
        return Math.abs(velocity) > minimumVelocity;
    }

    private void addVelocityTracker(MotionEvent ev, boolean isResetIfExist) {
        initVelocityTrackerIfNotExist(isResetIfExist);
        velocityTracker.addMovement(ev);
    }

    private void initVelocityTrackerIfNotExist(boolean isResetIfExist) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
            return;
        }

        if (isResetIfExist) {
            velocityTracker.clear();
        }
    }

    private void updateMoveOrientationStatus(int shiftX, int shiftY) {
        if (needUpdateMoveOrientationStatus) {
            needUpdateMoveOrientationStatus = false;

            if (shiftX > (shiftY * VIEW_PAGER_SCROLL_DIRECTION_SCALE)) {
                isMoveVertical = false;
            } else {
                isMoveVertical = true;
            }
        }
    }

    private boolean isValidDistance(int shiftX, int shiftY) {
        return Math.sqrt(shiftX * shiftX + shiftY * shiftY) > touchSlop;
    }

    /*-------------------------------------------------*/

    private boolean upAndCanWholeScrollUp() {
        return (moveDirection == DIRECTION.UP) && (!isSuspendedLayoutScrollToBottom());
    }

    private boolean downAndCanWholeScrollDown() {
        return (moveDirection == DIRECTION.DOWN) && isChildScrollableViewScrollToTop();
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void computeScroll() {
        if (!scroller.computeScrollOffset()) {
            return;
        }
        final int shouldScrollToY = scroller.getCurrY();

        if (upDirection == DIRECTION.UP) {
            if (!isSuspendedLayoutScrollToBottom()) {
                scrollTo(0, shouldScrollToY);
            } else {
                scroller.forceFinished(true);
                scrollMultiScrollableView(shouldScrollToY);
            }
        }

        if (upDirection == DIRECTION.DOWN) {
            if (!isChildScrollableViewScrollToTop()) {
                if (isFirstFlingDownChildScrollableView) {
                    isFirstFlingDownChildScrollableView = false;
                    scrollableViewHelper.smoothScrollBy(-(int) scroller.getCurrVelocity(), 0, 0);
                }
            } else {
                int deltaY = shouldScrollToY - flingDownLastScrollerY;
                scrollBy(0, deltaY);
            }

            flingDownLastScrollerY = shouldScrollToY;
            invalidate();
        }
    }

    /*-------------------------------------------------*/

    private void scrollMultiScrollableView(int currY) {
        int distance = scroller.getFinalY() - currY;
        int duration = calcDuration(scroller.getDuration(), scroller.timePassed());
        scrollableViewHelper.smoothScrollBy((int) scroller.getCurrVelocity(), distance, duration);
    }

    private int calcDuration(int duration, int timePass) {
        return duration - timePass;
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void scrollBy(int x, int y) {
        int validByY = byYLimiter(y, getScrollY());
        super.scrollBy(x, validByY);
    }

    @Override
    public void scrollTo(int x, int y) {
        int validToY = toYLimiter(y);
        if (onScrollListener != null) {
            onScrollListener.onScrolled(validToY);
        }
        super.scrollTo(x, validToY);
    }

    private int byYLimiter(int byY, int scrollY) {
        int intentToY = scrollY + byY;
        return toYLimiter(intentToY) - scrollY;
    }

    private int toYLimiter(int toY) {
        int validToY;
        if (toY >= (-suspendedLayoutYMax)) {
            validToY = -suspendedLayoutYMax;
        } else if (toY <= SUSPENDED_LAYOUT_Y_MIN) {
            validToY = SUSPENDED_LAYOUT_Y_MIN;
        } else {
            validToY = toY;
        }
        return validToY;
    }

    /*-------------------------------------------------*/

    private boolean isSuspendedLayoutScrollToBottom() {
        return (-getScrollY()) <= suspendedLayoutYMax;
    }

    private boolean isChildScrollableViewScrollToTop() {
        return scrollableViewHelper.isScrollViewAtTop();
    }

    /*--------------------------------------------------------------------------------------------*/

    public void scrollToTop() {
        scrollTo(getScrollX(), 0);
        scrollableViewHelper.scrollToTop();
    }

    /*--------------------------------------------------------------------------------------------*/

    public interface OnScrollListener {
        void onScrolled(int y); // 返回0~header高度
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /*--------------------------------------------------------------------------------------------*/

    private enum DIRECTION {
        UP,
        DOWN
    }

    private enum DISPATCH {
        DEFAULT,
        DOWN_THROUGH,
        MOVE_THROUGH,
        MOVE_ABANDON,
        UP_THROUGH,
        UP_ABANDON
    }
}