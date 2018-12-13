package cn.cbsd.mvplibrary.mvp;

import android.app.Activity;

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
        MyToast.successBig(msg);
//        new PromptDialog(context)
//                .showSuccess(msg);
    }

    @Override
    public void showError(String msg) {
        MyToast.errorBig(msg);
//        PromptDialog dialog = new PromptDialog(context);
//        dialog.setViewAnimDuration(1000);
//        dialog.showError(msg,false);
    }

    @Override
    public void showWarn(String msg) {
        MyToast.warn(msg);
//        new PromptDialog(context)
//                .showWarn(msg);
    }

    @Override
    public void showInfo(String msg) {
        MyToast.info(msg);
//        new PromptDialog(context)
//                .showInfo(msg);
    }

    @Override
    public void showConfirm(String msg) {
        new IosDialog(context).builder()
                .setMessage(msg)
                .setPositiveButton("确定",null)
                .show();

    }

    @Override
    public void show(String msg) {
        MyToast.show(msg);
    }

    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new PromptDialog(context);
        }
        mLoadingDialog.showLoading(msg,false);
    }

    @Override
    public void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismissImmediately();
        }
    }

}
