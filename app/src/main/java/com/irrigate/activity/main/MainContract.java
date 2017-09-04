package com.irrigate.activity.main;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Bear MVP BaseActivity Template.
 */

public interface MainContract {
    interface View {
        void gotoXActivity(MainViewModel viewModel);

        void updateContentViews(MainViewModel viewModel);
    }

    interface Presenter {
        void initViewModel(MainViewModel viewModel);

        void start();

        void onClickXBtn();
    }

    interface Model {
        void initViewModel(MainViewModel MainViewModel);

        MainViewModel getMainViewModel();
    }
}
