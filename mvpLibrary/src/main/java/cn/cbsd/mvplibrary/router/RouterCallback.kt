package cn.cbsd.mvplibrary.router

import android.app.Activity

/**
 * Created by wanglei on 2016/11/29.
 */

interface RouterCallback {

    fun onBefore(from: Activity, to: Class<*>)

    fun onNext(from: Activity, to: Class<*>)

    fun onError(from: Activity, to: Class<*>, throwable: Throwable)

}
