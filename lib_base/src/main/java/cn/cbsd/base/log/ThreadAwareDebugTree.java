package cn.cbsd.base.log;

import com.orhanobut.logger.Logger;

import timber.log.Timber;

/**
 * 当前类注释:DebugTree基础上打印当前线程和当前行号
 *
 * @author zhenyanjun
 * @date 2018/7/4 17:15
 */
public class ThreadAwareDebugTree extends Timber.DebugTree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        Logger.log(priority, tag, message, t);
    }

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        //日志显示行号
        return super.createStackElementTag(element) + "(Line " + element.getLineNumber() + ")";
    }
}