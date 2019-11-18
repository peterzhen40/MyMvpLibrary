package cn.cbsd.mvplibrary.kit


import android.app.Activity
import java.util.*

/**
 * 退出程序时，可以通过[.finishAll]关闭应用的所有Activity.<br></br>
 */
object ActivityCollector {

    //存放应用已开启的所有Activity.
    private val mActivities = LinkedList<Activity>()

    /**
     * 添加一个Activity到Activity列表中.
     */
    @JvmStatic
    fun addActivity(activity: Activity) {
        if (!mActivities.contains(activity)) {
            mActivities.add(activity)
        }
    }

    /**
     * 从Activity列表中删除指定的Activity.
     */
    @JvmStatic
    fun removeActivity(activity: Activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity)
        }
    }

    /**
     * 关闭所有的Activity, 退出程序.
     */
    @JvmStatic
    fun finishAll() {
        for (activity in mActivities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        System.exit(0)
    }

}