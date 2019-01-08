package cn.cbsd.base.mvp;

/**
 * BaseActivity/BaseFragment的代理
 * 常用方法
 * @author zhenyanjun
 * @date 2019/1/8 10:56
 */
public interface IDelegate {
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

    void toastShort(String msg);
    void toastShort(int res);
    void toastLong(String msg);
    void toastLong(int res);
}
