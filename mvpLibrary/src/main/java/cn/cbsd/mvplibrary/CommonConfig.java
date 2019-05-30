package cn.cbsd.mvplibrary;


import cn.cbsd.mvplibrary.router.Router;

/**
 * Created by wanglei on 2016/12/4.
 * 全局设置
 */

public class CommonConfig {
    // #log
    public static final boolean LOG = BuildConfig.DEBUG;
    public static final String LOG_TAG = "XLog";

    // #cache
    public static final String CACHE_SP_NAME = "config";
    public static final String CACHE_DISK_DIR = "cache";

    // #router
    public static final int ROUTER_ANIM_ENTER = Router.RES_NONE;
    public static final int ROUTER_ANIM_EXIT = Router.RES_NONE;

    // #imageloader

    // #dev model
    public static final boolean DEV = true;

    public static class Pattern {
        public static final String REG_PHONE = "^1\\d{10}$";
        public static final String REG_KDG_SIZE = "^\\d+\\*\\d+$";
        public static final String REG_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![<>@$#%^&*()-+!?]+$)[\\w<>@$#%^&*()-+!?]{6,20}$";
        public static final String DEFAULT_DATE = "yyyyMMdd";
        public static final String DEFAULT_DATE_TIME = "yyyyMMdd HH:mm";
        public static final String DEFAULT_TIME = "HH:mm:ss";
    }
}
