package cn.cbsd.mvplibrary.mvp;

import android.os.Bundle;
import android.view.View;

/**
 * Created by wanglei on 2016/12/29.
 */

public interface IView<P> {

    void bindUI(View rootView);

    void bindEvent();

    int getLayoutId();

    void initData(Bundle savedInstanceState);

    int getOptionsMenuId();

    boolean useEventBus();

    P newP();

    void showLoading();
    void showLoading(String msg);

    void hideLoading();

    boolean useUiState();

}
