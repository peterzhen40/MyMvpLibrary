package cn.cbsd.mvplibrary.event

/**
 * Created by wanglei on 2016/12/22.
 */

interface IBus {

    fun register(`object`: Any)

    fun unregister(`object`: Any)

    fun post(event: AbsEvent)

    fun postSticky(event: AbsEvent)


    abstract class AbsEvent {
        abstract val tag: Int
    }

}
