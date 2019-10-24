package cn.cbsd.mvplibrary.mvp

import android.view.View

/**
 * Created by wanglei on 2016/12/29.
 */

interface VDelegate {
    fun resume()

    fun pause()

    fun destroy()

    fun showSuccess(msg: String)

    fun showError(msg: String)

    fun showError(title: String, msg: String, confirmText: String = "我知道了", listener: View.OnClickListener? = null)

    fun showWarn(msg: String)

    fun showInfo(msg: String)

    fun showConfirm(msg: String)

    fun show(msg: String)

    fun showToast(msg: String, duration: Int)

    fun showLoading(msg: String?)

    fun dismissLoading()

    //    void toastShort(String msg);
    //    void toastShort(int res);
    //    void toastLong(String msg);
    //    void toastLong(int res);

}
