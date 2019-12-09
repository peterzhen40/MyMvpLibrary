package cn.cbsd.mvplibrary.mvp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.fengchen.uistatus.UiStatusController
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * Created by wanglei on 2017/1/28.
 */

open class LazyFragment : RxFragment() {
    protected lateinit var myLayoutInflater: LayoutInflater
    protected lateinit var context: Activity

    protected var rootView: View? = null
    private var container: ViewGroup? = null

    private var isInitReady = false
    private var isVisibleToUserState = STATE_NO_SET
    private var saveInstanceState: Bundle? = null
    private val isLazyEnable = true
    private var isStart = false
    private lateinit var layout: FrameLayout
    lateinit var defaultUiController: UiStatusController

    protected val realRootView: View?
        get() {
            if (rootView != null) {
                if (rootView is FrameLayout && TAG_ROOT_FRAMELAYOUT == rootView?.tag) {
                    return (rootView as FrameLayout).getChildAt(0)
                }
            }

            return rootView
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myLayoutInflater = inflater
        this.container = container
        defaultUiController = UiStatusController.get()
        onCreateView(savedInstanceState)
        return if (rootView == null) {
            super.onCreateView(inflater, container, savedInstanceState)
        } else rootView

    }

    private fun onCreateView(savedInstanceState: Bundle?) {
        this.saveInstanceState = savedInstanceState
        val isVisible: Boolean
        if (isVisibleToUserState == STATE_NO_SET) {
            isVisible = userVisibleHint
        } else {
            isVisible = isVisibleToUserState == STATE_VISIBLE
        }
        if (isLazyEnable) {
            if (isVisible && !isInitReady) {
                onCreateViewLazy(savedInstanceState)
                isInitReady = true
            } else {
                var mInflater = myLayoutInflater
                if (mInflater == null && context != null) {
                    mInflater = LayoutInflater.from(context)
                }
                layout = FrameLayout(context)
                layout.tag = TAG_ROOT_FRAMELAYOUT

                val view  = getPreviewLayout(mInflater, layout)
                if (view != null) {
                    layout.addView(view)
                }
                layout.layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                setContentView(layout)
            }
        } else {
            onCreateViewLazy(savedInstanceState)
            isInitReady = true
        }
    }

    protected fun `$`(id: Int): View? {
        return if (rootView != null) {
            rootView!!.findViewById(id)
        } else null
    }

    protected fun setContentView(layoutResID: Int) {
        if (isLazyEnable && rootView != null && rootView!!.parent != null) {
            layout.removeAllViews()
            var view = myLayoutInflater.inflate(layoutResID, layout, false)
            if (useDefaultUiState()) {
                view = defaultUiController.bindFragment(view)
            }
            layout.addView(view)
        } else {
            rootView = myLayoutInflater.inflate(layoutResID, container, false)
            if (useDefaultUiState()) {
                rootView = defaultUiController.bindFragment(rootView!!)
            }
        }
    }

    protected fun setContentView(view: View) {
        var view = view
        if (isLazyEnable && rootView != null && rootView!!.parent != null) {
            layout.removeAllViews()
            if (useDefaultUiState()) {
                view = defaultUiController.bindFragment(view)
            }
            layout.addView(view)
        } else {
            if (useDefaultUiState()) {
                view = defaultUiController.bindFragment(view)
            }
            rootView = view
        }
    }

    protected open fun useDefaultUiState(): Boolean {
        return false
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisibleToUserState = if (isVisibleToUser) STATE_VISIBLE else STATE_NO_VISIBLE
        if (isVisibleToUser
                && !isInitReady
                && rootView != null) {
            isInitReady = true
            onCreateViewLazy(saveInstanceState)
            onResumeLazy()
        }
        if (isInitReady && rootView != null) {
            if (isVisibleToUser) {
                isStart = true
                onStartLazy()
            } else {
                isStart = false
                onStopLazy()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isInitReady) {
            onResumeLazy()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isInitReady) {
            onPauseLazy()
        }
    }

    override fun onStart() {
        super.onStart()
        if (isInitReady
                && !isStart
                && userVisibleHint) {
            isStart = true
            onStartLazy()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isInitReady
                && isStart
                && userVisibleHint) {
            isStart = false
            onStopLazy()
        }
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

        //androidx不需要这段代码，会引起崩溃
        //try {
        //    val childFragmentManager = Fragment::class.java.getDeclaredField("mChildFragmentManager")
        //    childFragmentManager.isAccessible = true
        //    childFragmentManager.set(this, null)
        //} catch (e: NoSuchFieldException) {
        //    throw RuntimeException(e)
        //} catch (e: IllegalAccessException) {
        //    throw RuntimeException(e)
        //}

    }


    override fun onDestroyView() {
        super.onDestroyView()
        //rootView = null
        //container = null
        //myLayoutInflater = null
        if (isInitReady) {
            onDestoryLazy()
        }
        isInitReady = false
    }

    protected fun getPreviewLayout(mInflater: LayoutInflater?, layout: FrameLayout?): View? {
        return null
    }

    protected open fun onCreateViewLazy(savedInstanceState: Bundle?) {

    }

    protected fun onStartLazy() {

    }

    protected fun onStopLazy() {

    }

    protected fun onResumeLazy() {

    }

    protected fun onPauseLazy() {

    }

    protected open fun onDestoryLazy() {

    }

    companion object {

        private const val STATE_VISIBLE = 1 //用户可见
        private const val STATE_NO_SET = -1 //未设置值
        private const val STATE_NO_VISIBLE = 0  //用户不可见

        private const val TAG_ROOT_FRAMELAYOUT = "tag_root_framelayout"
    }


}
