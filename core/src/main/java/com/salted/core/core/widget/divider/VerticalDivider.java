package com.salted.core.core.widget.divider;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.salted.core.R;
import com.salted.core.core.util.UIUtils;

/**
 * Description:
 * 链式调用配置竖直分割线
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-19.
 */

public class VerticalDivider extends View {
    private static final int WIDTH_DIMEN_RES_ID = R.dimen.global_divider_width;
    private static final int BACKGROUND_COLOR_RES_ID = R.color.global_divider_color;

    public VerticalDivider(Context context) {
        super(context);

        this.setLayoutParams(new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelOffset(WIDTH_DIMEN_RES_ID),
                LinearLayout.LayoutParams.MATCH_PARENT));
        this.setBackgroundResource(BACKGROUND_COLOR_RES_ID);
    }

    public VerticalDivider color(int colorId) {
        this.setBackgroundResource(colorId);
        return this;
    }

    public VerticalDivider width(float dp) {
        this.setLayoutParams(new LinearLayout.LayoutParams(
                UIUtils.dp2px(getContext(), dp), LinearLayout.LayoutParams.MATCH_PARENT));
        return this;
    }

    /**
     * @param dp top and bottom margin
     */
    public VerticalDivider margin(float dp) {
        return this.margin(dp, dp);
    }

    public VerticalDivider margin(float top, float bottom) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.getLayoutParams();
        layoutParams.setMargins(
                0, UIUtils.dp2px(getContext(), top), 0, UIUtils.dp2px(getContext(), bottom));
        this.setLayoutParams(layoutParams);

        return this;
    }
}