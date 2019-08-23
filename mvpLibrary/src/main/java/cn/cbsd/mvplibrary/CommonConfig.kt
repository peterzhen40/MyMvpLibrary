package cn.cbsd.mvplibrary


import cn.cbsd.mvplibrary.router.Router

/**
 * Created by wanglei on 2016/12/4.
 * 全局设置
 */

object CommonConfig {
    // #log
    val LOG = BuildConfig.DEBUG
    val LOG_TAG = "XLog"

    // #cache
    val CACHE_SP_NAME = "config"
    val CACHE_DISK_DIR = "cache"

    // #router
    val ROUTER_ANIM_ENTER = Router.RES_NONE
    val ROUTER_ANIM_EXIT = Router.RES_NONE

    // #imageloader

    // #dev model
    val DEV = true

    object Pattern {
        val REG_PHONE = "^1\\d{10}$"
        val REG_KDG_SIZE = "^\\d+\\*\\d+$"
        val REG_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![<>@$#%^&*()-+!?]+$)[\\w<>@$#%^&*()-+!?]{6,20}$"
        val DEFAULT_DATE = "yyyyMMdd"
        val DEFAULT_DATE_TIME = "yyyyMMdd HH:mm"
        val DEFAULT_TIME = "HH:mm:ss"
    }
}
