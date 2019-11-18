package cn.cbsd.mvplibrary.router

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import cn.cbsd.mvplibrary.CommonConfig
import java.io.Serializable
import java.util.*


/**
 * Created by wanglei on 2016/11/29.
 * 路由器
 */
class Router private constructor() {

    private val intent: Intent = Intent()
    private lateinit var from: Activity
    private lateinit var to: Class<*>
    private var data: Bundle = Bundle()
    private var options: ActivityOptionsCompat? = null
    private var requestCode = -1
    private var enterAnim = CommonConfig.ROUTER_ANIM_ENTER
    private var exitAnim = CommonConfig.ROUTER_ANIM_EXIT
    private var isFragment = false
    private var fromFragment: Fragment? = null

    fun to(to: Class<*>): Router {
        this.to = to
        return this
    }

    fun fromFragment(fromFragment: Fragment): Router {
        this.isFragment = true
        this.fromFragment = fromFragment
        return this
    }

    fun addFlags(flags: Int): Router {
        intent.addFlags(flags)
        return this
    }

    fun data(data: Bundle): Router {
        this.data = data
        return this
    }

    fun putBoolean(key: String, value: Boolean): Router {
        data.putBoolean(key, value)
        return this
    }

    fun putByte(key: String, value: Byte): Router {
        data.putByte(key, value)
        return this
    }

    fun putChar(key: String, value: Char): Router {
        data.putChar(key, value)
        return this
    }

    fun putInt(key: String, value: Int): Router {
        data.putInt(key, value)
        return this
    }

    fun putString(key: String, value: String): Router {
        data.putString(key, value)
        return this
    }

    fun putShort(key: String, value: Short): Router {
        data.putShort(key, value)
        return this
    }

    fun putFloat(key: String, value: Float): Router {
        data.putFloat(key, value)
        return this
    }

    fun putCharSequence(key: String, value: CharSequence?): Router {
        data.putCharSequence(key, value)
        return this
    }

    fun putParcelable(key: String, value: Parcelable?): Router {
        data.putParcelable(key, value)
        return this
    }

    fun putParcelableArray(key: String, value: Array<Parcelable>?): Router {
        data.putParcelableArray(key, value)
        return this
    }

    fun putParcelableArrayList(key: String,
                               value: ArrayList<out Parcelable>?): Router {
        data.putParcelableArrayList(key, value)
        return this
    }


    fun putIntegerArrayList(key: String, value: ArrayList<Int>?): Router {
        data.putIntegerArrayList(key, value)
        return this
    }

    fun putStringArrayList(key: String, value: ArrayList<String>?): Router {
        data.putStringArrayList(key, value)
        return this
    }

    fun putStringArray(key: String, value: Array<String>?): Router {
        data.putStringArray(key, value)
        return this
    }

    fun putCharSequenceArrayList(key: String,
                                 value: ArrayList<CharSequence>?): Router {
        data.putCharSequenceArrayList(key, value)
        return this
    }

    fun putSerializable(key: String, value: Serializable?): Router {
        data.putSerializable(key, value)
        return this
    }


    fun options(options: ActivityOptionsCompat): Router {
        this.options = options
        return this
    }

    fun requestCode(requestCode: Int): Router {
        this.requestCode = requestCode
        return this
    }

    fun anim(enterAnim: Int, exitAnim: Int): Router {
        this.enterAnim = enterAnim
        this.exitAnim = exitAnim
        return this
    }

    fun launch() {
        try {
            if (intent != null && to != null) {
                if (isFragment) {
                    from = fromFragment!!.activity!!
                }

                if (from != null) {
                    if (callback != null) {
                        callback!!.onBefore(from, to)
                    }

                    intent.setClass(from, to)

                    intent.putExtras(data)

                    if (options == null) {
                        if (requestCode < 0) {
                            from.startActivity(intent)
                        } else {
                            if (isFragment) {
                                fromFragment!!.startActivityForResult(intent, requestCode)
                            } else {
                                from.startActivityForResult(intent, requestCode)
                            }
                        }

                        if (enterAnim > 0 && exitAnim > 0) {
                            from.overridePendingTransition(enterAnim, exitAnim)
                        }
                    } else {
                        if (requestCode < 0) {
                            ActivityCompat.startActivity(from, intent, options!!.toBundle())
                        } else {
                            if (isFragment) {
                                fromFragment!!.startActivityForResult(intent, requestCode, options!!.toBundle())
                            } else {
                                ActivityCompat.startActivityForResult(from, intent, requestCode, options!!.toBundle())
                            }
                        }
                    }

                    if (callback != null) {
                        callback!!.onNext(from, to)
                    }
                }
            }

        } catch (throwable: Throwable) {
            if (callback != null) {
                callback!!.onError(from, to, throwable)
            }
        }

    }

    companion object {

        @JvmField
        val RES_NONE = -1

        private var callback: RouterCallback? = null

        @JvmStatic
        fun newIntent(context: Activity): Router {
            val router = Router()
            router.from = context
            return router
        }

        @JvmStatic
        fun newIntent(context: Fragment): Router {
            val router = Router()
            router.fromFragment = context
            router.isFragment = true
            return router
        }

        @JvmStatic
        fun pop(activity: Activity) {
            activity.finish()
        }

        @JvmStatic
        fun setCallback(callback: RouterCallback) {
            Router.callback = callback
        }
    }
}
