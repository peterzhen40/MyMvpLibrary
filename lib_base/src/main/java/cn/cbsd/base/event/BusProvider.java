package cn.cbsd.base.event;

/**
 * @author zhenyanjun
 * @date 2019/1/8 10:56
 */
public class BusProvider {

    private static RxBusImpl bus;

    public static RxBusImpl getBus() {
        if (bus == null) {
            synchronized (BusProvider.class) {
                if (bus == null) {
                    bus = RxBusImpl.get();
                }
            }
        }
        return bus;
    }

}
