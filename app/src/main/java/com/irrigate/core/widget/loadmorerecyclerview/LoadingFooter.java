package com.irrigate.core.widget.loadmorerecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.irrigate.R;
import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Created by zhong on 2016/4/9.
 * <p>
 * ListView/GridView/RecyclerView 分页加载时使用到的FooterView
 */
@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
public class LoadingFooter extends RelativeLayout {

    protected State mState;
    private View mFooterContainer;
    private View mLoadingView;
    private View mNetworkErrorView;
    private View mTheEndView;
    private ProgressWheel mLoadingProgress;
    private TextView mLoadingText;

    private View mCustomEmptyView;

    public LoadingFooter(Context context) {
        super(context);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mFooterContainer = inflate(context, R.layout.common_list_footer, this).
                findViewById(R.id.footer_container);
        setOnClickListener(null);
        setState(State.Normal);
    }

    public State getState() {
        return mState;
    }

    public void setState(State status) {
        setState(status, true);
    }

    /**
     * 设置状态
     *
     * @param status
     * @param showView 是否展示当前View
     */
    private void setState(State status, boolean showView) {
        if (mState == status) {
            return;
        }
        mState = status;

        switch (status) {

            case Normal:
                hideContainer();
                clearState();
                break;
            case Loading:
                showContainer();
                clearState();

                showLoading(showView);
                break;
            case TheEnd:
                showContainer();
                clearState();

                showEndView(showView);
                break;
            case NetWorkError:
                showContainer();
                clearState();

                showErrorView(showView);
                break;
            default:

                break;
        }
    }

    private void showErrorView(boolean showView) {
        if (mNetworkErrorView == null) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.network_error_viewstub);
            mNetworkErrorView = viewStub.inflate();
        } else {
            mNetworkErrorView.setVisibility(VISIBLE);
        }
        mNetworkErrorView.setVisibility(showView ? VISIBLE : GONE);
    }

    private void showEndView(boolean showView) {
        if (mTheEndView == null) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.end_viewstub);

            if (mCustomEmptyView != null) {
                viewStub.inflate().setVisibility(GONE);

                mTheEndView = mCustomEmptyView;
                addView(mTheEndView);
                LayoutParams params = (LayoutParams) mTheEndView.getLayoutParams();
                params.addRule(CENTER_IN_PARENT);
                mTheEndView.setLayoutParams(params);
            } else {
                mTheEndView = viewStub.inflate();
            }
        } else {
            mTheEndView.setVisibility(VISIBLE);
        }

        mTheEndView.setVisibility(showView ? VISIBLE : GONE);
    }

    private void showLoading(boolean showView) {
        if (mLoadingView == null) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.loading_viewstub);
            mLoadingView = viewStub.inflate();

            mLoadingProgress = (ProgressWheel) mLoadingView.findViewById(R.id.loading_progress);
            mLoadingText = (TextView) mLoadingView.findViewById(R.id.loading_text);
        } else {
            mLoadingView.setVisibility(VISIBLE);
        }

        mLoadingView.setVisibility(showView ? VISIBLE : GONE);

        mLoadingProgress.setVisibility(View.VISIBLE);
        mLoadingText.setText("正在加载...");
    }

    void clearState() {
        hideLoadingView();
        hideEndView();
        hideErrorView();
        setOnClickListener(null);
    }

    private void hideErrorView() {
        if (mNetworkErrorView != null) {
            mNetworkErrorView.setVisibility(GONE);
        }
    }

    private void hideEndView() {
        if (mTheEndView != null) {
            mTheEndView.setVisibility(GONE);
        }
    }

    private void hideLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
        }
    }

    private void hideContainer() {
        mFooterContainer.setVisibility(GONE);
    }

    private void showContainer() {
        mFooterContainer.setVisibility(VISIBLE);
    }

    public void setEmptyView(View view) {
        mCustomEmptyView = view;
    }

    public enum State {
        /**
         * 正常
         */
        Normal,
        /**
         * 加载到最底了
         */
        TheEnd,
        /**
         * 加载中..
         */
        Loading,
        /**
         * 网络异常
         */
        NetWorkError
    }
}