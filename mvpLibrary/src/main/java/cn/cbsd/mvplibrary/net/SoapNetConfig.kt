package cn.cbsd.mvplibrary.net

/**
 * 当前类注释:
 * Author: zhenyanjun
 * Date  : 2018/1/19 11:16
 */

object SoapNetConfig {

    var provider: SoapProvider? = null

    fun registerProvider(provider: SoapProvider) {
        this.provider = provider
    }
}
