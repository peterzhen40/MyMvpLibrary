package cn.peterzhen.demo

import android.os.Bundle
import cn.cbsd.mvplibrary.mvp.XActivity

/**
 * 当前类注释:
 * @author zhenyanjun
 * @date   2019-08-27 17:20
 */
class KotlinActivity :XActivity<>(){

    override val layoutId: Int = R.layout.view_all_list

    override fun initData(savedInstanceState: Bundle?) {
        getvDelegate()?.showError(msg = "test")
        context
    }
}