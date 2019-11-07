package cn.cbsd.mvplibrary.mvp

/**
 * Created by wanglei on 2016/12/29.
 */

class XPresent<V> : IPresent<V> where V : IView{
    private var v: V? = null

    override fun attachV(view: V) {
        v = view
    }

    override fun detachV() {
        v = null
    }

    protected fun getV(): V? {
        checkNotNull(v) { "v can not be null" }
        return v
    }
}
