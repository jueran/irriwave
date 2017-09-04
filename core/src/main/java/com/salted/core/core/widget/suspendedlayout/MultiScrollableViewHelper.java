package com.salted.core.core.widget.suspendedlayout;

import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ScrollView;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-29.
 */

@SuppressWarnings("checkstyle:MagicNumber")
public class MultiScrollableViewHelper {
    private ScrollableContainer mCurrentScrollableCainer;

    /**
     * a viewgroup whitch contains ScrollView/ListView/RecycelerView..
     */
    public interface ScrollableContainer {
        /**
         * @return ScrollView/ListView/RecycelerView..'s instance
         */
        View getScrollableView();
    }

    public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer) {
        this.mCurrentScrollableCainer = scrollableContainer;
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 判断是否滑动到顶部方法,ScrollAbleLayout根据此方法来做一些逻辑判断
     * 目前只实现了AdapterView,ScrollView,RecyclerView
     * 需要支持其他view可以自行补充实现
     */
    public boolean isScrollViewAtTop() {
        View scrollableView = getScrollableView();
        if (scrollableView == null) {
            return true;
        }
        if (scrollableView instanceof AdapterView) {
            return isAdapterViewTop((AdapterView) scrollableView);
        }
        if (scrollableView instanceof ScrollView) {
            return isScrollViewTop((ScrollView) scrollableView);
        }
        if (scrollableView instanceof RecyclerView) {
            return isRecyclerViewTop((RecyclerView) scrollableView);
        }
        if (scrollableView instanceof WebView) {
            return isWebViewTop((WebView) scrollableView);
        }
        if (scrollableView instanceof NestedScrollView) {
            return isNestedScrollViewTop((NestedScrollView) scrollableView);
        }
//        throw new IllegalStateException(
//                "scrollableView must be a instance of AdapterView|ScrollView|RecyclerView");
        return true;
    }

    private View getScrollableView() {
        if (mCurrentScrollableCainer == null) {
            return null;
        }
        return mCurrentScrollableCainer.getScrollableView();
    }

    /*-------------------------------------------------*/

    private static boolean isRecyclerViewTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            if (layoutManager instanceof LinearLayoutManager) {
//                int firstVisibleItemPosition =
//                      ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//                View childAt = recyclerView.getChildAt(0);
//
//                FLog.tag("xzy").singleLine().print("top:%d;scrollY:%d;Y:%f",childAt.getTop(),
//                  childAt.getScrollY(),childAt.getY());
            // 不行，top和y在滑到上面的时候一直是子item的margintop，然后scrollY一直是0，滑下来也是0
//
//                if (childAt == null || (firstVisibleItemPosition == 0 && childAt.getTop() == 0)) {
//                    return true;
//                }
//            }
            return !recyclerView.canScrollVertically(-1);
        }
        return false;
    }

    private static boolean isAdapterViewTop(AdapterView adapterView) {
        if (adapterView != null) {
            int firstVisiblePosition = adapterView.getFirstVisiblePosition();
            View childAt = adapterView.getChildAt(0);
            if (childAt == null || (firstVisiblePosition == 0 && childAt.getTop() == 0)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isScrollViewTop(ScrollView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }
        return false;
    }

    private static boolean isWebViewTop(WebView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }
        return false;
    }

    private static boolean isNestedScrollViewTop(NestedScrollView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }
        return false;
    }

    /*--------------------------------------------------------------------------------------------*/

    public void smoothScrollBy(int velocityY, int distance, int duration) {
        View scrollableView = getScrollableView();
        if (scrollableView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) scrollableView;
            if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                absListView.fling(velocityY);
            } else {
                absListView.smoothScrollBy(distance, duration);
            }
        } else if (scrollableView instanceof ScrollView) {
            ((ScrollView) scrollableView).fling(velocityY);
        } else if (scrollableView instanceof RecyclerView) {
            ((RecyclerView) scrollableView).fling(0, velocityY);
        } else if (scrollableView instanceof WebView) {
            ((WebView) scrollableView).flingScroll(0, velocityY);
        }   else if (scrollableView instanceof NestedScrollView) {
            ((NestedScrollView) scrollableView).fling(velocityY);
        }
    }

    public void scrollToTop() {
        View scrollableView = getScrollableView();
        if (scrollableView == null) return;

        if (scrollableView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) scrollableView;
            absListView.scrollBy(0, -absListView.getScrollY());
        } else if (scrollableView instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) scrollableView;
            scrollView.scrollBy(0, -scrollView.getScrollY());
        }else if (scrollableView instanceof NestedScrollView) {
            NestedScrollView scrollView = (NestedScrollView) scrollableView;
            scrollView.scrollBy(0, -scrollView.getScrollY());
        } else if (scrollableView instanceof RecyclerView) {
            ((RecyclerView) scrollableView).scrollToPosition(0);
        } else if (scrollableView instanceof WebView) {
            WebView webView = (WebView) scrollableView;
            webView.scrollBy(0, -webView.getScrollY());
        }
    }
}
