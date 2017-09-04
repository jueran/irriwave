package com.irrigate.base.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;


/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-04-11.
 */

public final class BaseFragmentFactory {
    public static final String FRAGMENT_PARAM = "FRAGMENT_PARAM";
    public static final String FRAGMENT_STRING_PARAM = "FRAGMENT_STRING_PARAM";

    private BaseFragmentFactory() {
    }

    /**
     * 每个子类Fragment都是用这三个方法start的
     * 传入的param类型建议前往要跳转的Fragment的takeOutYourParam()方法中查看
     * 建议只使用一种param类型，不同的入口往其中初始化不同的域
     *
     * @param childFragment as "new ChildXXXFragment()"
     */
    public static BaseFragment newInstance(BaseFragment childFragment, Parcelable param) {
        return getBundledFragment(childFragment, param);
    }

    public static BaseFragment newInstance(BaseFragment childFragment, String param) {
        return getBundledFragment(childFragment, param);
    }

    public static BaseFragment newInstance(BaseFragment childFragment) {
        return childFragment;
    }

    private static <T extends BaseFragment> T getBundledFragment(T fragment, Parcelable param) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(FRAGMENT_PARAM, param);
        fragment.setArguments(bundle);
        return fragment;
    }

    private static <T extends BaseFragment> T getBundledFragment(T fragment, String param) {
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_STRING_PARAM, param);
        fragment.setArguments(bundle);
        return fragment;
    }

    /*--------------------------------------------------------------------------------------------*/

    public static void analysisData(@Nullable Bundle bundle,
                                    @Nullable TakeOutParamCallBack callBack) {
        if (bundle == null) return;

        String stringParam = bundle.getString(FRAGMENT_STRING_PARAM);
        if (stringParam != null && callBack != null) {
            callBack.takeOutParam(stringParam);
        }

        Parcelable param = bundle.getParcelable(FRAGMENT_PARAM);
        if (param != null && callBack != null) {
            callBack.takeOutParam(param);
        }
    }

    public interface TakeOutParamCallBack {
        void takeOutParam(String stringParam);

        void takeOutParam(Parcelable param);
    }
}
