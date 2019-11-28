package cn.cbsd.mvplibrary.event

/**
 * Created by wanglei on 2016/12/22.
 */

object BusProvider {

    val bus:RxBusImpl
        @JvmStatic
        get() = RxBusImpl

}
