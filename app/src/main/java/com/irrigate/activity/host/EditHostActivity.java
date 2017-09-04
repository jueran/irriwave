package com.irrigate.activity.host;

import android.app.Activity;
import android.os.Bundle;

import com.irrigate.Application;
import com.irrigate.R;

/**
 * 说明：
 * 作者：杨健
 * 时间：2017/9/4.
 */

public class EditHostActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_host);
        Application.Host = "http://www.baidu.com";
    }
}
