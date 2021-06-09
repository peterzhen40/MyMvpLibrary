package cn.cbsd.mvplibrary.mvp

import android.app.Activity
import android.view.View
import android.widget.Toast

import cn.cbsd.mvplibrary.widget.IosDialog
import es.dmoral.toasty.MyToast
import me.leefeng.promptlibrary.PromptDialog

/**
 * Created by wanglei on 2016/12/29.
 */

data class VDelegateBase constructor(private val context: Activity) : VDelegate {
    override fun showError(msg: String?) {
        if (context.isFinishing) {
            showInfo(msg)
        } else {
            IosDialog(context).builder()
                    .setMessage(msg)
                    .setPositiveButton("我知道了", null)
                    .show()
        }

    }

    override fun showError(title: String?, msg: String?) {
        if (context.isFinishing) {
            showInfo(msg)
        } else {
            IosDialog(context).builder()
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton("我知道了", null)
                    .show()
        }

    }

    override fun showError(title: String?, msg: String?, confirmText: String?, listener: View.OnClickListener?) {
        //MyToast.errorBig(msg);
        //我们不应该把Toast用于错误提示，因为错误提示需要明确告知用户具体原因，因此不适合用这种一闪而过的Toast弹出式提示。
        // 一般需要用户明确知晓操作结果状态的话，会使用模态对话框来提示，同时附带下一步操作的指引
        if (context.isFinishing) {
            showInfo(msg)
        } else {
            IosDialog(context).builder()
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(confirmText, listener)
                    .show()
        }

    }

    open var mLoadingDialog: PromptDialog? = null


    override fun resume() {

    }

    override fun pause() {

    }

    override fun destroy() {

    }

    override fun showSuccess(msg: String?) {
        //成功信息
        MyToast.successBig(msg)
    }

    override fun showWarn(msg: String?) {
        //橙色警告信息
        MyToast.warn(msg)
    }

    override fun showInfo(msg: String?) {
        //提示信息
        MyToast.errorBig(msg)
    }

    override fun showConfirm(msg: String?) {
        if (context.isFinishing) {
            showInfo(msg)
        } else {
            IosDialog(context).builder()
                    .setMessage(msg)
                    .setPositiveButton("确定", null)
                    .show()
        }

    }

    override fun show(msg: String?) {
        //一般信息，跟toast差不多
        MyToast.show(msg)
    }

    override fun showToast(msg: String?, duration: Int) {
        Toast.makeText(context, msg, duration).show()
    }

    override fun showLoading(msg: String?) {
        if (mLoadingDialog == null) {
            mLoadingDialog = PromptDialog(context)
        }
        mLoadingDialog?.showLoading(msg, false)
    }

    override fun dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog?.dismissImmediately()
        }
    }

}
