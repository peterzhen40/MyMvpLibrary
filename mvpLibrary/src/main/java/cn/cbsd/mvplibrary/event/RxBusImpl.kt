package cn.cbsd.mvplibrary.event


import com.blankj.rxbus.RxBus

/**
 * eventbus实现类
 * 现在采取的是rxbus
 * Created by wanglei on 2016/12/22.
 */

object RxBusImpl : IBus {

    override fun register(obj: Any) {}

    override fun unregister(obj: Any) {
        RxBus.getDefault().unregister(obj)
    }

    override fun post(event: AbsEvent) {
        RxBus.getDefault().post(event)
    }

    override fun postSticky(event: AbsEvent) {
        RxBus.getDefault().postSticky(event)
    }

    fun <T : AbsEvent> subscribe(subscriber: Any, callback: RxBus.Callback<T>) {
        RxBus.getDefault().subscribe(subscriber, callback)
    }

    fun <T : AbsEvent> subscribeSticky(subscriber: Any,callback: RxBus.Callback<T>) {
        RxBus.getDefault().subscribeSticky(subscriber, callback)
    }

}

