package cn.peterzhen.demo

import android.os.Bundle
import cn.cbsd.mvplibrary.mvp.XLazyFragment

/**
 * 当前类注释:
 * @author zhenyanjun
 * @date   2019-12-03 09:33
 */
class KotlinFragment :XLazyFragment(){
    override val layoutId: Int
        get() = R.layout.view_all_list

    override fun initData(savedInstanceState: Bundle?) {
        getvDelegate().showError("有吗")
    }

}