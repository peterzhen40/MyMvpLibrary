package cn.peterzhen.demo;

import android.app.Application;
import android.content.Intent;
import android.provider.Settings;

import com.fengchen.uistatus.UiStatusManager;
import com.fengchen.uistatus.annotation.UiStatus;

import timber.log.Timber;

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
                .addUiStatusConfig(UiStatus.LOADING, R.layout.view_loading_text)
                .addUiStatusConfig(UiStatus.NETWORK_ERROR, R.layout.view_network_error, R.id.btn_check, (o, iUiStatusController, view) ->{
                    this.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                })
                .addUiStatusConfig(UiStatus.LOAD_ERROR,R.layout.view_error)
                .addUiStatusConfig(UiStatus.EMPTY,R.layout.view_empty)
                .setWidgetMargin(UiStatus.WIDGET_NETWORK_ERROR,0, 0)
                .setWidgetMargin(UiStatus.WIDGET_ELFIN,0, 0)
                .setWidgetMargin(UiStatus.WIDGET_FLOAT,0,0)
                .addUiStatusConfig(UiStatus.WIDGET_NETWORK_ERROR,R.layout.view_widget_network_error,R.id.tv_check,(o, iUiStatusController, view) -> {
                    this.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                })
                .addUiStatusConfig(UiStatus.WIDGET_ELFIN,R.layout.view_widget_hint)
                .addUiStatusConfig(UiStatus.WIDGET_FLOAT,R.layout.view_widget_float);

//        UiStatusNetworkStatusProvider.getInstance()
//                .registerOnRequestNetworkStatusEvent(new OnRequestNetworkStatusEvent() {
//                    @Override
//                    public boolean onRequestNetworkStatus(Context context) {
//                        return NetworkUtils.isConnected();
//                    }
//                });

        Timber.plant(new Timber.DebugTree());
    }
}
