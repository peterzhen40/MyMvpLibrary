package cn.cbsd.mvplibrary.log;

import android.util.Log;

/**
 * 当前类注释:release版本
 * Author: zhenyanjun
 * Date  : 2018/7/4 17:16
 */
public class ReleaseTree extends ThreadAwareDebugTree {
    @Override
    protected boolean isLoggable(String tag, int priority) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return false;
        }
        return true;
    }
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (!isLoggable(tag, priority)) {
            return;
        }
        super.log(priority, tag, message, t);
    }
}
