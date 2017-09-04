package com.irrigate.activity.main.mainactivity;

import android.content.Context;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Bear MVP BaseActivity Template.
 */

public class MainModel implements MainContract.Model {
    private Context context;

    private MainViewModel viewModel;

    public MainModel(Context context) {
        this.context = context;
    }

    @Override
    public void initViewModel(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public MainViewModel getMainViewModel() {
        return viewModel;
    }
}
