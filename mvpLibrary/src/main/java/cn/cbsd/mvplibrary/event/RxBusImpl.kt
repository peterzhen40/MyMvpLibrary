package cn.cbsd.mvplibrary.event


import com.blankj.rxbus.RxBus

/**
 * eventbus实现类
 * 现在采取的是rxbus
 * Created by wanglei on 2016/12/22.
 */

object RxBusImpl : IBus {

    override fun register(`object`: Any) {}

    override fun unregister(`object`: Any) {
        RxBus.getDefault().unregister(`object`)
    }

    override fun post(event: IBus.AbsEvent) {
        RxBus.getDefault().post(event)
    }

    override fun postSticky(event: IBus.AbsEvent) {
        RxBus.getDefault().postSticky(event)
    }

    fun <T : IBus.AbsEvent> subscribe(subscriber: Any,
                                      callback: RxBus.Callback<T>) {
        RxBus.getDefault().subscribe(subscriber, callback)
    }

    fun <T : IBus.AbsEvent> subscribeSticky(subscriber: Any,
                                            callback: RxBus.Callback<T>) {
        RxBus.getDefault().subscribeSticky(subscriber, callback)
    }

}

