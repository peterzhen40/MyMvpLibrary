package cn.cbsd.mvplibrary.mvp

/**
 * Created by wanglei on 2016/12/29.
 */

interface IPresent<V> {
    fun attachV(view: V)

    fun detachV()
}
