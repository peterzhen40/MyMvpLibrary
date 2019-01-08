package cn.cbsd.base.mvp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.Unbinder;
import cn.cbsd.base.BaseConfig;
import cn.cbsd.base.R;
import cn.cbsd.base.event.BusProvider;
import cn.cbsd.base.utils.ActivityCollector;
import cn.cbsd.base.utils.KnifeKit;

/**
 * @author zhenyanjun
 * @date 2019/1/8 10:56
 */
public abstract class BaseActivity<P extends IPresent> extends RxAppCompatActivity implements IView<P> {

    private IDelegate vDelegate;
    private P p;
    protected Activity context;

    private RxPermissions rxPermissions;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.getInstance().addActivity(this);
        context = this;

        getP();

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            bindUI(null);
            bindEvent();
        }

        //全局沉浸式设置，只改颜色
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        //子类默认竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initData(savedInstanceState);
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

    protected IDelegate getBaseDelegate() {
        if (vDelegate == null) {
            vDelegate = BaseDelegate.create(context);
        }
        return vDelegate;
    }

    protected P getP() {
        if (p == null) {
            p = newP();
        }
        if (p != null) {
            if (!p.hasV()) {
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
        getBaseDelegate().resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        getBaseDelegate().pause();
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
        getBaseDelegate().destroy();
        p = null;
        vDelegate = null;
    }

    protected RxPermissions getRxPermissions() {
        rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(BaseConfig.DEBUG);
        return rxPermissions;
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void showLoading() {
        if (!isFinishing()) {
            getBaseDelegate().showLoading("加载中...");
        }
    }

    @Override
    public void hideLoading() {
        getBaseDelegate().dismissLoading();
    }
}
