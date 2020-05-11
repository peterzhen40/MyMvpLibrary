package cn.cbsd.mvplibrary.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter

/**
 * 当前类注释:
 * @author zhenyanjun
 * @date   2020/4/14 14:31
 */

/**
 * 自定义类型：multi type 点击事件的函数类型
 */
typealias MyMultiItemClickListener = (adapter: MultiTypeAdapter, view: View?, position: Int) ->Unit

/**
 * 自定义类型：通用点击事件的函数类型
 */
typealias MyItemClickListener = (view: View?, position: Int) ->Unit

/**
 * 自定义类型：图片选择事件的函数类型
 */
typealias MyPicturePickerListener = (selectCount: Int)->Unit

/**
 * 函数类型重命名：数据转换
 */
typealias CommonDataConvert<A, B> = (A) -> B