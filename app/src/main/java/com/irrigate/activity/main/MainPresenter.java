package com.irrigate.activity.main;

import android.app.Activity;

import com.irrigate.core.helper.activityhelper.ViewHelper;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Bear MVP BaseActivity Template.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private MainContract.Model model;
    private ViewHelper vHelper;
    private Activity activity;

    public MainPresenter(MainContract.View view, ViewHelper vHelper, Activity activity) {
        this.view = view;
        this.vHelper = vHelper;
        this.activity = activity;
        this.model = new MainModel(activity);
    }

    @Override
    public void initViewModel(MainViewModel viewModel) {
        model.initViewModel(viewModel);
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void start() {
        view.updateContentViews(model.getMainViewModel());
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void onClickXBtn() {

    }
}
