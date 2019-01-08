package cn.cbsd.base.mvp;

import android.os.Bundle;
import android.view.View;

/**
 * activity/fragment保持一致
 * @author zhenyanjun
 * @date 2019/1/8 10:56
 */

public interface IView<P> {

    void bindUI(View rootView);
    int getLayoutId();
    void initData(Bundle savedInstanceState);

    void bindEvent();

    boolean useEventBus();

    P newP();

    /**
     * activity/fragment 默认加载动画
     */
    void showLoading();

    void hideLoading();
}
