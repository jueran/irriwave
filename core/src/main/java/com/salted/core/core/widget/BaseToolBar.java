package com.salted.core.core.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salted.core.R;
import com.salted.core.core.util.UIUtils;

/**
 * Description:
 * 可配置项：（准备之后改写成属性进行配置）
 * 高度 - dimens: global_bar_height
 * 背景颜色 - colors: toolbar_color （可动态修改）
 * 返回icon - ids: selector_ic_toolbar_back
 * 关闭icon - ids: selector_ic_toolbar_close
 * 标题文字字号 - dimens: toolbar_title_text_size
 * 标题文字颜色 - colors: toolbar_title_color（可动态修改）
 * 标题文字最大宽度 - dimens: toolbar_title_text_max_width
 * 标题文字内容 （动态修改）
 * 底部线是否显示，底部线颜色（动态修改，默认不显示）
 * 文字按钮文字大小 - dimens: toolbar_button_text_size
 * 文字按钮最大宽度（建议约为4个字宽） - dimens: toolbar_button_text_max_width
 * 文字按钮颜色
 * - colors: toolbar_text_btn_color、toolbar_text_btn_disabled_color、toolbar_text_btn_pressed_color
 * 图片按钮左右padding（等于按钮间距的2/1）
 * - dimens: toolbar_image_button_padding、toolbar_text_button_padding
 * 按钮右侧间距 - dimens: toolbar_button_right_margin
 * <p>
 * Attention:
 * 默认不显示右边你添加的2个按钮，需要手动调用show才会显示
 * 按钮添加的方向是从右往左
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-24.
 */
public class BaseToolBar extends LinearLayout {
    private Context context;
    private ImageView backBt;
    private TextView titleTv;
    private View dividerLine;
    private LinearLayout buttonsContainer;

    public BaseToolBar(Context context) {
        super(context);
        initView(context);
    }

    public BaseToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        configToolBar();
        findViews(context);
    }

    private void configToolBar() {
        this.setOrientation(VERTICAL);
        this.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setToolBarBg(R.color.toolbar_color);
    }

    private void findViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_base_tool_bar, this, true);
        backBt = (ImageView) findViewById(R.id.base_tool_bar_back_bt);
        titleTv = (TextView) findViewById(R.id.base_tool_bar_title_tv);
        buttonsContainer = (LinearLayout) findViewById(R.id.base_tool_bar_buttons_container);
        dividerLine = findViewById(R.id.base_tool_bar_divider_line);
    }

    /*--------------------------------------------------------------------------------------------*/

    public void setBaseInfo(CharSequence title, @Nullable OnClickListener backClickListener) {
        if (title != null) titleTv.setText(title);
        if (backClickListener != null) backBt.setOnClickListener(backClickListener);
    }

    public void updateTitle(CharSequence title) {
        if (title != null) titleTv.setText(title);
    }

    public void hideTitle() {
        UIUtils.hide(titleTv);
    }

    public void setTitleClickListener(OnClickListener listener){
        titleTv.setOnClickListener(listener);
    }

    /*-------------------------------------------------*/

    /**
     * 默认是左箭头返回按钮
     */
    public void setBackBtnIcon(int backBtnIconId) {
        if (backBtnIconId <= 0) return;
        backBt.setImageResource(backBtnIconId);
    }

    public void hideBackBtn() {
        UIUtils.hide(backBt);
    }

    /**
     * 设置返回按钮是一个X
     */
    public void setBackBtCloseIcon() {
        backBt.setImageResource(R.drawable.selector_ic_toolbar_close);
    }

    /*-------------------------------------------------*/

    /**
     * 添加文字按钮
     * <p>
     * 添加顺序从右向左
     * 默认隐藏，建议在网络请求成功后调用显示按钮方法
     * 可以同时添加文字按钮和图片按钮
     * 由于位置限制，最多添加2个按钮，文字按钮名字建议最长4个字
     */
    public void addButton(String btnName, OnClickListener clickListener) {
        if (btnName == null) return;
        if (clickListener == null) return;
        if (buttonsContainer.getChildCount() > 1) return;

        TextView textBtn = new TextView(context);
        textBtn.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textBtn.setTextSize(UIUtils.px2dip(context, getDimension(R.dimen.toolbar_button_text_size)));
        textBtn.setSingleLine();
        textBtn.setMaxWidth(getDimension(R.dimen.toolbar_button_text_max_width));
        textBtn.setGravity(Gravity.CENTER);
        textBtn.setPadding(getDimension(R.dimen.toolbar_text_button_padding), 0,
                getDimension(R.dimen.toolbar_text_button_padding), 0);
        textBtn.setTextColor(getResources().getColorStateList(
                R.color.selector_toolbar_text_btn_color));

        textBtn.setText(btnName);
        textBtn.setOnClickListener(clickListener);

        buttonsContainer.addView(textBtn);
    }

    /**
     * 添加图片按钮
     * <p>
     * 添加顺序从右向左
     * 默认隐藏，建议在网络请求成功后调用显示按钮方法
     * 可以同时添加文字按钮和图片按钮，由于位置限制，最多添加2个按钮
     */
    public void addButton(int iconResId, OnClickListener clickListener) {
        if (iconResId <= 0) return;
        if (clickListener == null) return;
        if (buttonsContainer.getChildCount() > 1) return;

        ImageView imageBtn = new ImageView(context);
        imageBtn.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageBtn.setPadding(getDimension(R.dimen.toolbar_image_button_padding), 0, getDimension(R.dimen.toolbar_image_button_padding), 0);

        imageBtn.setImageResource(iconResId);
        imageBtn.setOnClickListener(clickListener);

        buttonsContainer.addView(imageBtn);
    }

    public void showToolBarBtns() {
        UIUtils.show(buttonsContainer);
    }

    public void hideToolBarBtns() {
        UIUtils.hide(buttonsContainer);
    }

    @Nullable
    public View getButton(int index) {
        if (index > 1) return null;
        if (buttonsContainer.getChildCount() <= index) return null;
        return buttonsContainer.getChildAt(index);
    }

    private int getDimension(int dimenResId) {
        return getResources().getDimensionPixelSize(dimenResId);
    }

    /*-------------------------------------------------*/

    /**
     * 往container中添加的子view会居中覆盖在toolbar上，最大宽度为屏幕宽度
     */
    public void setToolBarCustomView(View titleBarCustomView) {
        FrameLayout titleBarCustomContainer = (FrameLayout)
                findViewById(R.id.base_tool_bar_custom_container);
        titleBarCustomContainer.removeAllViews();
        titleBarCustomContainer.addView(titleBarCustomView);
    }

    /*-------------------------------------------------*/

    public void setToolBarBg(int colorResId) {
        if (colorResId <= 0) return;
        this.setBackgroundColor(context.getResources().getColor(colorResId));
    }

    public void setPaddingTop(int px) {
        this.setPadding(0, px, 0, 0);
    }

    public void setBottomLineColor(int colorResId) {
        if (colorResId <= 0) return;

        UIUtils.show(dividerLine);
        dividerLine.setBackgroundColor(context.getResources().getColor(colorResId));
    }

    public void setBottomLineVisible(boolean visible) {
        UIUtils.setVisible(dividerLine, visible);
    }

    public void setTitleColor(int colorResId) {
        if (colorResId <= 0) return;
        titleTv.setTextColor(context.getResources().getColor(colorResId));
    }

    public ImageView getBackBt() {
        return backBt;
    }
}