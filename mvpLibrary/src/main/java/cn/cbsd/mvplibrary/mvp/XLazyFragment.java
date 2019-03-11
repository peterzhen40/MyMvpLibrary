package cn.cbsd.mvplibrary.mvp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.Unbinder;
import cn.cbsd.mvplibrary.CommonConfig;
import cn.cbsd.mvplibrary.event.BusProvider;
import cn.cbsd.mvplibrary.kit.KnifeKit;

/**
 * Created by wanglei on 2017/1/26.
 */

public abstract class XLazyFragment<P extends IPresent>
        extends LazyFragment implements IView<P> {

    private VDelegate vDelegate;
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
    protected void onDestoryLazy() {
        super.onDestoryLazy();
        if (useEventBus()) {
            BusProvider.getBus().unregister(this);
        }
        if (getP() != null) {
            getP().detachV();
        }
        getvDelegate().destroy();

        p = null;
        vDelegate = null;
    }


    protected RxPermissions getRxPermissions() {
        rxPermissions = new RxPermissions(getActivity());
        rxPermissions.setLogging(CommonConfig.DEV);
        return rxPermissions;
    }


    @Override
    public int getOptionsMenuId() {
        return 0;
    }


    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void showLoading() {
        if (!getActivity().isFinishing())
            getvDelegate().showLoading("加载中...");
    }

    @Override
    public void showLoading(String msg) {
        if (getActivity()!=null && !getActivity().isFinishing()) {
            getvDelegate().showLoading(msg);
        }
    }

    @Override
    public void hideLoading() {
        getvDelegate().dismissLoading();
    }

}
