package cn.cbsd.mvplibrary.event

/**
 * Created by wanglei on 2016/12/22.
 */

interface IBus {

    fun register(obj: Any)

    fun unregister(obj: Any)

    fun post(event: AbsEvent)

    fun postSticky(event: AbsEvent)


}
