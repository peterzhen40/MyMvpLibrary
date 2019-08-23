package cn.cbsd.mvplibrary.log

import com.orhanobut.logger.Logger

import timber.log.Timber

/**
 * 当前类注释:DebugTree基础上打印当前线程和当前行号
 * Author: zhenyanjun
 * Date  : 2018/7/4 17:15
 */
open class ThreadAwareDebugTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Logger.log(priority, tag, message, t)
    }

    override fun createStackElementTag(element: StackTraceElement): String? {
        return super.createStackElementTag(element) + "(Line " + element.lineNumber + ")"  //日志显示行号
    }
}