package cn.peterzhen.demo;

import android.app.Application;
import android.os.Bundle;

import com.blankj.rxbus.RxBus;
import com.fengchen.uistatus.UiStatusController;
import com.fengchen.uistatus.annotation.UiStatus;

import cn.cbsd.mvplibrary.event.AbsEvent;
import cn.cbsd.mvplibrary.event.BusProvider;
import cn.cbsd.mvplibrary.mvp.XLazyFragment;

/**
 * 当前类注释:
 *
 * @author zhenyanjun
 * @date 2019/4/3 09:51
 */
public class DemoFragment extends XLazyFragment {
    @Override
    public int getLayoutId() {
        return R.layout.view_all_list;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        UiStatusController uiController = getDefaultUiController();
        uiController.changeUiStatus(UiStatus.LOAD_ERROR);
        //Router.newIntent(context)
        //FileUtil.FileSize.SIZETYPE_B
        Application application = context.getApplication();
        getvDelegate().show("test");
        BusProvider.getBus().subscribe(this, new RxBus.Callback<AbsEvent>() {
            @Override
            public void onEvent(AbsEvent absEvent) {

            }
        });
    }

    @Override
    public boolean useDefaultUiState() {
        return true;
    }
}
