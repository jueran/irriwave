package com.irrigate.core.helper.callback;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-05-15.
 */

public interface FragmentLifecycleCallbacks {
    void onFragmentAttach(Context context);

    void onFragmentCreate(Bundle savedInstanceState);

    void onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void onFragmentActivityCreated(Bundle savedInstanceState);

    void onFragmentStarted();

    void onFragmentResumed();

    void onFragmentPaused();

    void onFragmentStopped();

    void onFragmentDestroyView();

    void onFragmentDestroy();

    void onFragmentDetach();
}