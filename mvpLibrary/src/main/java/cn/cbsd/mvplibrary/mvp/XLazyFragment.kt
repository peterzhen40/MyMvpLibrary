package cn.cbsd.mvplibrary.mvp

import android.os.Bundle
import android.view.View
import butterknife.Unbinder
import cn.cbsd.mvplibrary.CommonConfig
import cn.cbsd.mvplibrary.event.BusProvider
import cn.cbsd.mvplibrary.kit.KnifeKit
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Created by wanglei on 2017/1/26.
 */

abstract class XLazyFragment: LazyFragment(), IView {

    private var vDelegate: VDelegate? = null
    //private var p: P? = null

    private var rxPermissions: RxPermissions? = null
    private var unbinder: Unbinder? = null


    override val optionsMenuId: Int
        get() = 0

    override fun onCreateViewLazy(savedInstanceState: Bundle?) {
        super.onCreateViewLazy(savedInstanceState)
        if (savedInstanceState != null) {
            val isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN)

            val ft = fragmentManager!!.beginTransaction()
            if (isSupportHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }

        if (layoutId > 0) {
            setContentView(layoutId)
            bindUI(realRootView)
        }
        if (useEventBus()) {
            BusProvider.bus!!.register(this)
        }
        bindEvent()
        initData(savedInstanceState)
    }

    public override fun useDefaultUiState(): Boolean {
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //保存隐藏的状态
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
    }

    override fun bindUI(rootView: View?) {
        unbinder = KnifeKit.bind(this, rootView!!)
    }

    override fun bindEvent() {

    }


    fun getvDelegate(): VDelegate? {
        if (vDelegate == null) {
            vDelegate = VDelegateBase(context)
        }
        return vDelegate
    }

    //protected fun getP(): P? {
    //    if (p == null) {
    //        p = newP()
    //        if (p != null) {
    //            p!!.attachV(this)
    //        }
    //    }
    //    return p
    //}

    override fun onDestoryLazy() {
        super.onDestoryLazy()
        if (useEventBus()) {
            BusProvider.bus!!.unregister(this)
        }
        //if (getP() != null) {
        //    getP()!!.detachV()
        //}
        getvDelegate()!!.destroy()

        //p = null
        vDelegate = null
    }


    protected fun getRxPermissions(): RxPermissions? {
        rxPermissions = RxPermissions(activity!!)
        rxPermissions!!.setLogging(CommonConfig.DEV)
        return rxPermissions
    }


    override fun useEventBus(): Boolean {
        return false
    }

    override fun showLoading() {
        if (!activity!!.isFinishing)
            getvDelegate()!!.showLoading("加载中...")
    }

    override fun showLoading(msg: String?) {
        if (activity != null && !activity!!.isFinishing) {
            getvDelegate()!!.showLoading(msg)
        }
    }

    override fun hideLoading() {
        getvDelegate()!!.dismissLoading()
    }

    companion object {
        private val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    }

}
