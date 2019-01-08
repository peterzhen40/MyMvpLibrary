package cn.cbsd.base.mvp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.Unbinder;
import cn.cbsd.base.BaseConfig;
import cn.cbsd.base.event.BusProvider;
import cn.cbsd.base.utils.KnifeKit;

/**
 * @author zhenyanjun
 * @date 2019/1/8 10:56
 */
public abstract class BaseLazyFragment<P extends IPresent> extends LazyFragment implements IView<P> {

    private IDelegate vDelegate;
    private P p;

    private RxPermissions rxPermissions;
    private Unbinder unbinder;

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }

        getP();

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            bindUI(getRealRootView());
        }
        if (useEventBus()) {
            BusProvider.getBus().register(this);
        }
        bindEvent();
        initData(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //保存隐藏的状态
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void bindUI(View rootView) {
        unbinder = KnifeKit.bind(this, rootView);
    }

    @Override
    public void bindEvent() {

    }

    public IDelegate getBaseDelegate() {
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
    protected void onDestroyLazy() {
        super.onDestroyLazy();
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
        rxPermissions = new RxPermissions(getActivity());
        rxPermissions.setLogging(BaseConfig.DEBUG);
        return rxPermissions;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void showLoading() {
        if (getActivity()!=null && !getActivity().isFinishing()) {
            getBaseDelegate().showLoading("加载中...");
        }
    }

    @Override
    public void hideLoading() {
        getBaseDelegate().dismissLoading();
    }
}
