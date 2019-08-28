package cn.cbsd.mvplibrary.mvp

import android.os.Bundle
import android.view.View

/**
 * Created by wanglei on 2016/12/29.
 */

interface IView {

    val layoutId: Int

    val optionsMenuId: Int

    fun bindUI(rootView: View?)

    fun bindEvent()

    fun initData(savedInstanceState: Bundle?)

    fun useEventBus(): Boolean

    fun showLoading()

    fun showLoading(msg: String?)

    fun hideLoading()

    fun useDefaultUiState(): Boolean

}
