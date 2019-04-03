package cn.peterzhen.demo;

import android.app.Application;

import com.fengchen.uistatus.UiStatusManager;
import com.fengchen.uistatus.annotation.UiStatus;

/**
 * 当前类注释:
 *
 * @author zhenyanjun
 * @date 2019/4/3 09:59
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UiStatusManager.getInstance()
                .addUiStatusConfig(UiStatus.LOADING,R.layout.view_loading)
                .addUiStatusConfig(UiStatus.EMPTY,R.layout.view_empty)
                .addUiStatusConfig(UiStatus.LOAD_ERROR, R.layout.view_error);
    }
}
