package cn.cbsd.base.net;

import cn.cbsd.base.net.kit.ErrorKit;
import es.dmoral.toasty.MyToast;
import io.reactivex.subscribers.ResourceSubscriber;
import timber.log.Timber;

/**
 * 当前类注释:自定义Subscriber
 * @author zhenyanjun
 * @date   2019/1/8 10:08
 */

public abstract class MySubscriber<T> extends ResourceSubscriber<T> {

    private boolean isShowToast = true;

    public MySubscriber() {
    }

    public MySubscriber(boolean isShowToast) {
        this.isShowToast = isShowToast;
    }

    @Override
    public void onError(Throwable t) {
        if (isShowToast) {
            MyToast.errorBig(ErrorKit.getErrorMessage(t));
        }
        Timber.e(t);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        if (null == t) {
            onError(new Throwable("返回的数据T为空"));
        } else if (t instanceof ReturnModel) {
            ReturnModel data = (ReturnModel) t;
            if (data.getCode() == 1) {
                success(t);
            } else {
                onError(new Throwable(ErrorKit.getErrorMessage(data)));
            }
        } else {
            onError(new Throwable("返回的数据非ReturnModel"));
        }
    }

    /**
     * 请求成功，返回处理过的数据
     */
    public void success(T returnModel) {

    }
}
