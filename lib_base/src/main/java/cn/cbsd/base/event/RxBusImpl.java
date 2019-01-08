package cn.cbsd.base.event;


import com.blankj.rxbus.RxBus;

/**
 * eventbus实现类，现在采取的是rxbus
 * @author zhenyanjun
 * @date 2019/1/8 10:56
 */

public class RxBusImpl implements IBus {

    private RxBusImpl() {
    }


    @Override
    public void register(Object object) {
    }

    @Override
    public void unregister(Object object) {
        RxBus.getDefault().unregister(object);
    }

    @Override
    public void post(BaseEvent event) {
        RxBus.getDefault().post(event);
    }

    @Override
    public void postSticky(BaseEvent event) {
        RxBus.getDefault().postSticky(event);
    }

    public <T extends BaseEvent> void subscribe(Object subscriber,
                                                RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribe(subscriber, callback);
    }

    public <T extends BaseEvent> void subscribeSticky(Object subscriber,
                                                      RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribeSticky(subscriber, callback);
    }

    public static RxBusImpl get() {
        return Holder.instance;
    }

    private static class Holder {
        private static final RxBusImpl instance = new RxBusImpl();
    }
}

