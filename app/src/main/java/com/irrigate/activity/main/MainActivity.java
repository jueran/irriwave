package com.irrigate.activity.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.irrigate.R;
import com.irrigate.base.activity.BaseActivity;


import butterknife.ButterKnife; // todo 增加依赖

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Bear MVP BaseActivity Template, at  .
 */

public class MainActivity extends BaseActivity implements MainContract.View {

    private MainContract.Presenter presenter;

    @Override
    protected void takeOutYourParam(@NonNull Parcelable param) throws ClassCastException {
        if (param instanceof MainViewModel) {
            presenter.initViewModel((MainViewModel) param);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected String getTitleText() {
        return "";
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();
        presenter.start();
    }

    @Override
    protected void onViewHelperCreate() {
        presenter = new MainPresenter(this, vHelper, getActivity());
    }

    @Override
    protected void onSafeBack() {
        super.onSafeBack();
    }

    /*--------------------------------------------------------------------------------------------*/

    private void initView() {

    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void updateContentViews(MainViewModel viewModel) {

    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void gotoXActivity(MainViewModel viewModel) {

    }
}
