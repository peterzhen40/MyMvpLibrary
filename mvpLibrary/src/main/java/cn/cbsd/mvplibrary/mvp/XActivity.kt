package cn.cbsd.mvplibrary.mvp

import android.app.Activity
import android.graphics.Color
import android.net.IpPrefix
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import butterknife.Unbinder
import cn.cbsd.mvplibrary.CommonConfig
import cn.cbsd.mvplibrary.R
import cn.cbsd.mvplibrary.event.BusProvider
import cn.cbsd.mvplibrary.kit.ActivityCollector
import cn.cbsd.mvplibrary.kit.KnifeKit
import com.fengchen.uistatus.UiStatusController
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by wanglei on 2016/12/29.
 */

abstract class XActivity : RxAppCompatActivity(), IView{

    protected lateinit var context: Activity
    private lateinit var vDelegate: VDelegate
    //private var p: P? = null

    private lateinit var rxPermissions: RxPermissions

    private var unbinder: Unbinder? = null

    /**
     * 获取默认的UiState
     * @return
     */
    lateinit var defaultUiController: UiStatusController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)

        if (layoutId > 0) {
            setContentView(layoutId)
            if (useDefaultUiState()) {
                defaultUiController = UiStatusController.get().bind(this)
            }
            bindUI(null)
            bindEvent()
        }
        //全局沉浸式设置，只改颜色
        if (Build.VERSION.SDK_INT >= 21) {
            window.navigationBarColor = Color.WHITE
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }
        initData(savedInstanceState)

        //子类默认竖屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = this
        vDelegate = VDelegateBase(context)
        rxPermissions = RxPermissions(context)
    }

    /**
     * 使用默认的UiState，即添加在界面的顶层
     * @return
     */
    override fun useDefaultUiState(): Boolean {
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //崩溃重启重叠问题
        //super.onSaveInstanceState(outState);
    }

    override fun bindUI(rootView: View?) {
        unbinder = KnifeKit.bind(this)
    }

    fun getvDelegate(): VDelegate {
        return vDelegate
    }

    override fun onStart() {
        super.onStart()
        if (useEventBus()) {
            BusProvider.bus.register(this)
        }
    }


    override fun onResume() {
        super.onResume()
        getvDelegate().resume()
    }


    override fun onPause() {
        super.onPause()
        getvDelegate().pause()
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            BusProvider.bus.unregister(this)
        }
        getvDelegate().destroy()
        //vDelegate = null

        //if (getPresent() != null) {
        //    getPresent()?.detachV()
        //}
        //p = null

        ActivityCollector.removeActivity(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (optionsMenuId > 0) {
            menuInflater.inflate(optionsMenuId, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    protected fun getRxPermissions(): RxPermissions {
        rxPermissions.setLogging(CommonConfig.DEV)
        return rxPermissions
    }

    override fun bindEvent() {

    }

    override fun showLoading() {
        if (!isFinishing) {
            getvDelegate().showLoading("加载中...")
        }
    }

    override fun showLoading(msg: String?) {
        if (!isFinishing) {
            getvDelegate().showLoading(msg)
        }
    }

    override fun hideLoading() {
        getvDelegate().dismissLoading()
    }

    override val optionsMenuId: Int
        get() = 0

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
