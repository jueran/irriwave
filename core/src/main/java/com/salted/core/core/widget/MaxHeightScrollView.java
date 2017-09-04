package com.salted.core.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.salted.core.R;
import com.salted.core.core.util.UIUtils;


/**
 * Description:
 * 可设置最大高度的ScrollView
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-17.
 */

public class MaxHeightScrollView extends ScrollView {
    private int maxHeight = 0;

    public MaxHeightScrollView(Context context) {
        super(context, null);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs,
                    R.styleable.MaxHeightScrollView);
            maxHeight = styledAttrs.getDimensionPixelSize(
                    R.styleable.MaxHeightScrollView_maxHeight, -1);

            styledAttrs.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > 0) {
            int myHeightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, myHeightSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /*--------------------------------------------------------------------------------------------*/

    public void setMaxHeight(int maxHeightDp) {
        maxHeight = UIUtils.dpi2px(getContext(), maxHeightDp);
    }

    public void setHeight(int heightDp) {
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                UIUtils.dpi2px(getContext(), heightDp)));
    }
}
