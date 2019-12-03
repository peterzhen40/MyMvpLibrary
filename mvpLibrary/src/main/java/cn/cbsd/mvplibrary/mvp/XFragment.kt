package cn.cbsd.mvplibrary.mvp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.Unbinder
import cn.cbsd.mvplibrary.CommonConfig
import cn.cbsd.mvplibrary.event.BusProvider
import cn.cbsd.mvplibrary.kit.KnifeKit
import com.fengchen.uistatus.UiStatusController
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlin.properties.Delegates

/**
 * Created by wanglei on 2016/12/29.
 */

abstract class XFragment : RxFragment(), IView {

    protected lateinit var context: Activity
    private val vDelegate: VDelegate by lazy { VDelegateBase(context) }
    private var rootView: View by Delegates.notNull()
    lateinit var myLayoutInflater: LayoutInflater

    val rxPermissions: RxPermissions by lazy {
        val rxPermissions = RxPermissions(context)
        rxPermissions.setLogging(CommonConfig.DEV)
        rxPermissions
    }
    private var unbinder: Unbinder? = null
    //private var p: P? = null

    /**
     * 获取默认的UiState
     * @return
     */
    lateinit var defaultUiController: UiStatusController

    override val optionsMenuId: Int
        get() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    }

    override fun onSaveInstanceState(outState: Bundle) {
        //保存隐藏的状态
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myLayoutInflater = inflater
        if (layoutId > 0) {
            rootView = inflater.inflate(layoutId, null)
            bindUI(rootView)
        } else {
            val viewGroup = rootView.parent as ViewGroup
            viewGroup.removeView(rootView)
        }

        if (useDefaultUiState()) {
            defaultUiController = UiStatusController.get()
            rootView = defaultUiController.bindFragment(rootView)
        }
        return rootView
    }

    /**
     * 使用默认的UiState，即添加在界面的顶层
     * @return
     */
    override fun useDefaultUiState(): Boolean {
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (useEventBus()) {
            BusProvider.bus.register(this)
        }
        bindEvent()
        initData(savedInstanceState)
    }

    override fun bindUI(rootView: View?) {
        unbinder = KnifeKit.bind(this, rootView!!)
    }

    fun getvDelegate(): VDelegate {
        return vDelegate
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            this.context = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        //context = null
    }

    override fun useEventBus(): Boolean {
        return false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (useEventBus()) {
            BusProvider.bus.unregister(this)
        }
        getvDelegate().destroy()
        //vDelegate = null

        //if (getPresent() != null) {
        //    getPresent()?.detachV()
        //}
        //p = null

    }

    //protected fun getRxPermissions(): RxPermissions? {
    //    rxPermissions.setLogging(CommonConfig.DEV)
    //    return rxPermissions
    //}

    override fun bindEvent() {

    }

    override fun showLoading() {
        if (!activity!!.isFinishing)
            getvDelegate().showLoading("加载中...")
    }

    override fun showLoading(msg: String?) {
        if (activity != null && !activity!!.isFinishing) {
            getvDelegate().showLoading(msg)
        }
    }

    override fun hideLoading() {
        getvDelegate().dismissLoading()
    }

    companion object {

        private const val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    }

    //protected fun getPresent(): P? {
    //    if (p == null) {
    //        p = newP()
    //        if (p != null) {
    //            p?.attachV(this)
    //        }
    //    }
    //    return p
    //}

    override fun newP(): Any? {
        return null
    }
}
