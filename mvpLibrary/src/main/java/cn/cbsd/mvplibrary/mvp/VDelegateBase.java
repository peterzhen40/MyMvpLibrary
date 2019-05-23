package cn.cbsd.mvplibrary.mvp;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import cn.cbsd.mvplibrary.widget.IosDialog;
import es.dmoral.toasty.MyToast;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by wanglei on 2016/12/29.
 */

public class VDelegateBase implements VDelegate {

    private Activity context;
    private PromptDialog mLoadingDialog;

    private VDelegateBase(Activity context) {
        this.context = context;
    }

    public static VDelegate create(Activity context) {
        return new VDelegateBase(context);
    }


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void showSuccess(String msg) {
        //成功信息
        MyToast.successBig(msg);
    }

    @Override
    public void showError(String msg) {
        //MyToast.errorBig(msg);
        //我们不应该把Toast用于错误提示，因为错误提示需要明确告知用户具体原因，因此不适合用这种一闪而过的Toast弹出式提示。
        // 一般需要用户明确知晓操作结果状态的话，会使用模态对话框来提示，同时附带下一步操作的指引
        new IosDialog(context).builder()
                .setMessage(msg)
                .setPositiveButton("我知道了", null)
                .show();
    }

    @Override
    public void showError(String title, String msg) {
        new IosDialog(context).builder()
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("我知道了", null)
                .show();
    }

    @Override
    public void showError(String title, String msg, String confirmText, View.OnClickListener listener) {
        new IosDialog(context).builder()
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(confirmText, listener)
                .show();
    }

    @Override
    public void showWarn(String msg) {
        //橙色警告信息
        MyToast.warn(msg);
    }

    @Override
    public void showInfo(String msg) {
        //蓝色提示信息
        MyToast.info(msg);
    }

    @Override
    public void showConfirm(String msg) {
        new IosDialog(context).builder()
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .show();
    }

    @Override
    public void show(String msg) {
        //一般提示信息
        MyToast.show(msg);
    }

    @Override
    public void showToast(String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new PromptDialog(context);
        }
        mLoadingDialog.showLoading(msg, false);
    }

    @Override
    public void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismissImmediately();
        }
    }

}
