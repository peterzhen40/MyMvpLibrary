package cn.cbsd.base.net.kit;

import android.support.v4.widget.SwipeRefreshLayout;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import cn.cbsd.base.mvp.IView;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 当前类注释:Rxjava线程切换工具
 * @author zhenyanjun
 * @date   2019/1/8 10:42
 */
public class RxKit {

    /**
     * 线程切换
     */
    public static <T> FlowableTransformer<T, T> getScheduler(IView view) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle(view));
    }

    /**
     * 带加载的线程切换
     */
    public static <T> FlowableTransformer<T, T> getLoadScheduler(IView view) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> view.showLoading())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(view::hideLoading)
                .compose(bindToLifecycle(view));
    }

    /**
     * 带加载更多的线程切换
     */
    public static <T> FlowableTransformer<T, T> getLoadMoreScheduler(IView view, boolean isLoadMore) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {
                    if (!isLoadMore) {
                        view.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(view::hideLoading)
                .compose(bindToLifecycle(view));
    }

    /**
     * 带加载更多的线程切换
     */
    public static <T> FlowableTransformer<T, T> getLoadMoreScheduler(IView view, boolean isLoadMore,
                                                                     SwipeRefreshLayout swipeRefreshLayout) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {
                    if (!isLoadMore) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                })
                .compose(bindToLifecycle(view));
    }

    /**
     * 生命周期管理
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
        if (view instanceof RxAppCompatActivity) {
            return ((RxAppCompatActivity) view).bindToLifecycle();
        } else if (view instanceof RxFragment) {
            return ((RxFragment) view).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }

    }

}
