package cn.cbsd.mvplibrary.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

/**
 * Created by wanglei on 2016/12/10.
 */

class XFragmentAdapter(fm: FragmentManager, fragmentList: List<Fragment>, private val titles: Array<String>?) : FragmentPagerAdapter(fm) {
    private val fragmentList = ArrayList<Fragment>()

    init {
        this.fragmentList.clear()
        this.fragmentList.addAll(fragmentList)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (titles != null && titles.size > position) {
            titles[position]
        } else ""
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
