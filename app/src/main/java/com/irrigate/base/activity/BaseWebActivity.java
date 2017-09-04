package com.irrigate.base.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.irrigate.R;
import com.salted.core.core.helper.systemadvance.StatusBarManager;

import junit.framework.Assert;

/**
 * Created by jian on 16/10/29.
 */
@SuppressLint("SetJavaScriptEnabled")
public abstract class BaseWebActivity extends BaseActivity {

    public static final String WEB_URL = "WEB_URL";
    public static final String WEB_TITLE = "WEB_TITLE";
    public static final String WEB_JSON_PARAMS = "WEB_JSON_PARAMS";

    private static final int TIME_OUT_MSG = 5555;
    private static final int TIMER_DELAY_TIME = 8000;

    protected String originUrl;
    protected String title;
    protected String jsonParams;

    protected WebView webView;
    /**
     * 正常页面
     */
    View webViewContainer;

    boolean htmlLoadSuccess = false;

    protected boolean isH5Alive = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configStatusBarLight(R.color.toolbar_color);
        initView();
        initData();
        setWebViewAttr();
        loadHtml();
    }

    public void configStatusBarLight(int statusBarColor) {
        StatusBarManager.setStatusBarLightMode(this, statusBarColor);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web_view);
        webViewContainer = findViewById(R.id.load_active_panel);
        getViewHelper().getToolBar().getBackBt().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return true;
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(WEB_URL)) {
            originUrl = intent.getStringExtra(WEB_URL);
        }
        if (intent.hasExtra(WEB_TITLE)) {
            title = intent.getStringExtra(WEB_TITLE);
            getViewHelper().updateTitle(title);
        }
        if (intent.hasExtra(WEB_JSON_PARAMS)) {
            jsonParams = intent.getStringExtra(WEB_JSON_PARAMS);
        }
        Assert.assertNotNull(originUrl);
        Assert.assertNotNull(title);
    }

    private void initStatusBar(@ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(id));
        }
    }

    private void setWebViewAttr() {
        webView.addJavascriptInterface(getJsCallBack(), "JsCallback");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationDatabasePath(getFilesDir().getPath());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setBuiltInZoomControls(false);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSaveFormData(false);
        webView.refreshDrawableState();
        webSettings.setLoadsImagesAutomatically(true);
        /* Enable zooming */
        webSettings.setSupportZoom(false);

        webView.setWebChromeClient(new BaseWebActivity.MyWebChromeClient());
        webView.setWebViewClient(new BaseWebActivity.InterruptClient());
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    protected JsCallback getJsCallBack() {
        return new JsCallback();
    }

    /**
     * 自定义WebChromeClient， 用来进行自定对话框的显示
     */
    private static class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    view.getContext());
            builder.setMessage(message).setPositiveButton("确定", null);
            // 不需要绑定按键事件
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            // 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            result.confirm();
            return true;
        }
    }

    private class InterruptClient extends WebViewClient {
        /**
         * 在这里拦截是否跳转客户端
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("url : " + url);
            if (url.startsWith("tel:")) {
                String phone = url.replace("tel:", "");
                dial(phone);
                return true;
            }
            if(intercepetUrlLoading()) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (htmlLoadSuccess) {
                setUILoadSuccess();
            } else {
                setUILoadFailed();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            htmlLoadSuccess = false;
        }

        private void dial(String url) {
            // TODO: 17/8/12
        }
    }

    protected boolean intercepetUrlLoading() {
        return false;
    }

    protected void onTitleRightBtnClick(View v) {
        // should be overwrite
    }

    private void setUILoading() {
        vHelper.showLoadingView();
        webViewContainer.setVisibility(View.INVISIBLE);
    }

    private void setUILoadFailed() {
        stopTimer();
        vHelper.hideLoadingView();
        vHelper.showNetworkBrokenView();
        webViewContainer.setVisibility(View.INVISIBLE);
    }

    private void setUILoadSuccess() {
        stopTimer();
        vHelper.hideLoadingView();
        vHelper.showHaveDataView();
        webViewContainer.setVisibility(View.VISIBLE);
    }

    public void loadHtml() {
        startTimer();
        htmlLoadSuccess = true;
        setUILoading();
        webView.loadUrl(originUrl);
    }

    public void back() {
        if (htmlLoadSuccess && isH5Alive) {
            isH5Alive = false;
            webView.loadUrl("javascript:WX_GO_BACK()");
        } else {
            finish();
        }
    }

    private boolean isTimeout = true;
    private int timeOutMessageWhat = TIME_OUT_MSG;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == timeOutMessageWhat && isTimeout) {
                    htmlLoadSuccess = false;
                    webView.stopLoading();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private void startTimer() {
        isTimeout = true;
        Message message = new Message();
        message.what = timeOutMessageWhat;
        handler.sendMessageDelayed(message, TIMER_DELAY_TIME);
    }

    private void stopTimer() {
        isTimeout = false;
        handler.removeMessages(timeOutMessageWhat);
    }

    protected class JsCallback {
        @JavascriptInterface
        public final void changeTitle(final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getViewHelper().getToolBar().updateTitle(title);
                }
            });
        }

        @JavascriptInterface
        public final void keepAlive() {
            isH5Alive = true;
        }

        @JavascriptInterface
        public final void exitH5() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        @JavascriptInterface
        public final void getJsonParams() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:HQ_WEB_loadSuccess(" + jsonParams + ")");
                }
            });
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    protected int getContentView() {
        return R.layout.activity_web_view;
    }

    @Override
    protected CharSequence getTitleText() {
        return title;
    }

    @Override
    protected boolean showStandardToolBar() {
        return true;
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        loadHtml();
    }
}
