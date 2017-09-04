package com.irrigate.core.widget.divider;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.irrigate.R;
import com.irrigate.core.util.UIUtils;

/**
 * Description:
 * 链式调用配置水平分割线
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-19.
 */

public class HorizontalDivider extends View {
    private static final int HEIGHT_DIMEN_RES_ID = R.dimen.global_divider_width;
    private static final int BACKGROUND_COLOR_RES_ID = R.color.global_divider_color;

    public HorizontalDivider(Context context) {
        super(context);

        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                context.getResources().getDimensionPixelOffset(HEIGHT_DIMEN_RES_ID)));
        this.setBackgroundResource(BACKGROUND_COLOR_RES_ID);
    }

    public HorizontalDivider color(int colorId) {
        this.setBackgroundResource(colorId);
        return this;
    }

    public HorizontalDivider height(float dp) {
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                UIUtils.dp2px(getContext(), dp)));
        return this;
    }

    /**
     * @param dp left and right margin
     */
    public HorizontalDivider margin(float dp) {
        return this.margin(dp, dp);
    }

    public HorizontalDivider margin(float left, float right) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.getLayoutParams();
        layoutParams.setMargins(
                UIUtils.dp2px(getContext(), left), 0, UIUtils.dp2px(getContext(), right), 0);
        this.setLayoutParams(layoutParams);

        return this;
    }
}