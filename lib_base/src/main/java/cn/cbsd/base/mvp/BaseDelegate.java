package cn.cbsd.base.mvp;

import android.app.Activity;
import android.widget.Toast;

import cn.cbsd.ui.widget.IosDialog;
import es.dmoral.toasty.MyToast;
import me.leefeng.promptlibrary.PromptDialog;

/**
 *  代理IDelegate的实现类
 * @author zhenyanjun
 * @date 2019/1/8 10:56
 */
public class BaseDelegate implements IDelegate {

    private Activity context;

    private BaseDelegate(Activity context) {
        this.context = context;
    }

    public static IDelegate create(Activity context) {
        return new BaseDelegate(context);
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
    }

    @Override
    public void showError(String msg) {
        MyToast.errorBig(msg);
    }

    @Override
    public void showWarn(String msg) {
        MyToast.warn(msg);
    }

    @Override
    public void showInfo(String msg) {
        MyToast.info(msg);
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

    private PromptDialog mLoadingDialog;

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

    @Override
    public void toastShort(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastShort(int res) {
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void toastLong(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void toastLong(int res) {
        Toast.makeText(context, res, Toast.LENGTH_LONG).show();
    }
}
