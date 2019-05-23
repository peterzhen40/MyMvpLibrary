package cn.cbsd.mvplibrary.mvp;

import android.view.View;

/**
 * Created by wanglei on 2016/12/29.
 */

public interface VDelegate {
    void resume();

    void pause();

    void destroy();

    void showSuccess(String msg);

    void showError(String msg);

    void showError(String title, String msg);

    void showError(String title, String msg, String confirmText, View.OnClickListener listener);

    void showWarn(String msg);

    void showInfo(String msg);

    void showConfirm(String msg);

    void show(String msg);

    void showToast(String msg, int duration);

    void showLoading(String msg);

    void dismissLoading();

    //    void toastShort(String msg);
    //    void toastShort(int res);
    //    void toastLong(String msg);
    //    void toastLong(int res);

}
