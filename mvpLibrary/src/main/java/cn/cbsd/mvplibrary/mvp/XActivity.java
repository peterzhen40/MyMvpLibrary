package cn.cbsd.mvplibrary.mvp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;

import com.fengchen.uistatus.UiStatusController;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.Unbinder;
import cn.cbsd.mvplibrary.CommonConfig;
import cn.cbsd.mvplibrary.R;
import cn.cbsd.mvplibrary.event.BusProvider;
import cn.cbsd.mvplibrary.kit.ActivityCollector;
import cn.cbsd.mvplibrary.kit.KnifeKit;

/**
 * Created by wanglei on 2016/12/29.
 */

public abstract class XActivity<P extends IPresent> extends RxAppCompatActivity implements IView<P> {

    private VDelegate vDelegate;
    private P p;
    protected Activity context;

    private RxPermissions rxPermissions;

    private Unbinder unbinder;
    private UiStatusController mUiController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.getInstance().addActivity(this);
        context = this;

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            if (useDefaultUiState()) {
                mUiController = UiStatusController.get().bind(this);
            }
            bindUI(null);
            bindEvent();
        }
        //全局沉浸式设置，只改颜色
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        initData(savedInstanceState);

        //子类默认竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 获取默认的UiState
     * @return
     */
    public UiStatusController getDefaultUiController() {
        return mUiController;
    }

    /**
     * 使用默认的UiState，即添加在界面的顶层
     * @return
     */
    @Override
    public boolean useDefaultUiState() {
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //崩溃重启重叠问题
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void bindUI(View rootView) {
        unbinder = KnifeKit.bind(this);
    }

    public VDelegate getvDelegate() {
        if (vDelegate == null) {
            vDelegate = VDelegateBase.create(context);
        }
        return vDelegate;
    }

    protected P getP() {
        if (p == null) {
            p = newP();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusProvider.getBus().register(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getvDelegate().resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        getvDelegate().pause();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            BusProvider.getBus().unregister(this);
        }
        if (getP() != null) {
            getP().detachV();
        }
        getvDelegate().destroy();
        p = null;
        vDelegate = null;

        ActivityCollector.getInstance().removeActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getOptionsMenuId() > 0) {
            getMenuInflater().inflate(getOptionsMenuId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    protected RxPermissions getRxPermissions() {
        rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(CommonConfig.DEV);
        return rxPermissions;
    }

    @Override
    public int getOptionsMenuId() {
        return 0;
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void showLoading() {
        if (!isFinishing()) {
            getvDelegate().showLoading("加载中...");
        }
    }

    @Override
    public void showLoading(String msg) {
        if (!isFinishing()) {
            getvDelegate().showLoading(msg);
        }
    }

    @Override
    public void hideLoading() {
        getvDelegate().dismissLoading();
    }

}
