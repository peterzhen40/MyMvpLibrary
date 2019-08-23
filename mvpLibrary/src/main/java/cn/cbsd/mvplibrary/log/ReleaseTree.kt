package cn.cbsd.mvplibrary.log

import android.util.Log

/**
 * 当前类注释:release版本
 * Author: zhenyanjun
 * Date  : 2018/7/4 17:16
 */
class ReleaseTree : ThreadAwareDebugTree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            false
        } else true
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (!isLoggable(tag, priority)) {
            return
        }
        super.log(priority, tag, message, t)
    }
}
