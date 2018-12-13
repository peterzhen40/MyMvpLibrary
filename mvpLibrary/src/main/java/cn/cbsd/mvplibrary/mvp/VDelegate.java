package cn.cbsd.mvplibrary.mvp;

/**
 * Created by wanglei on 2016/12/29.
 */

public interface VDelegate {
    void resume();
    void pause();
    void destroy();

    void showSuccess(String msg);
    void showError(String msg);
    void showWarn(String msg);
    void showInfo(String msg);
    void showConfirm(String msg);
    void show(String msg);
    void showLoading(String msg);
    void dismissLoading();

//    void toastShort(String msg);
//    void toastShort(int res);
//    void toastLong(String msg);
//    void toastLong(int res);

}
