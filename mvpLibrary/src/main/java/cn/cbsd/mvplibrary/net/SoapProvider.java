package cn.cbsd.mvplibrary.net;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * 当前类注释:
 * Author: zhenyanjun
 * Date  : 2018/1/19 11:11
 */

public interface SoapProvider {

    RxErrorHandler configHandleError(boolean isNeedShowToast);
}
