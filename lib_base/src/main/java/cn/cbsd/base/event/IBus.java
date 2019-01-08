package cn.cbsd.base.event;

/**
 *
 * @author zhenyanjun
 * @date 2019/1/8 10:56
 */

public interface IBus {

    void register(Object object);

    void unregister(Object object);

    void post(BaseEvent event);

    void postSticky(BaseEvent event);


    abstract class BaseEvent {
        public abstract int getTag();
    }

}
