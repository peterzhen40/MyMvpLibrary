package cn.cbsd.mvplibrary.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import cn.cbsd.mvplibrary.R
import java.util.*

/**
 * Author: liuqiang
 * Time: 2018-01-02 13:28
 * Description:
 */
class IosSheetDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private var txt_title: TextView? = null
    private var txt_cancel: TextView? = null
    private var lLayout_content: LinearLayout? = null
    private var sLayout_content: ScrollView? = null
    private var showTitle = false
    private var sheetItemList: MutableList<SheetItem>? = null
    private val display: Display

    init {
        val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
    }

    fun builder(): IosSheetDialog {
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(
                R.layout.view_actionsheet, null)

        // 设置Dialog最小宽度为屏幕宽度
        view.minimumWidth = display.width

        // 获取自定义Dialog布局中的控件
        sLayout_content = view.findViewById<View>(R.id.sLayout_content) as ScrollView
        lLayout_content = view
                .findViewById<View>(R.id.lLayout_content) as LinearLayout
        txt_title = view.findViewById<View>(R.id.txt_title) as TextView
        txt_cancel = view.findViewById<View>(R.id.txt_cancel) as TextView
        txt_cancel!!.setOnClickListener { dialog!!.dismiss() }

        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.ActionSheetDialogStyle)
        dialog!!.setContentView(view)
        val dialogWindow = dialog!!.window
        dialogWindow!!.setGravity(Gravity.LEFT or Gravity.BOTTOM)
        val lp = dialogWindow.attributes
        lp.x = 0
        lp.y = 0
        dialogWindow.attributes = lp

        return this
    }

    fun setTitle(title: String): IosSheetDialog {
        showTitle = true
        txt_title!!.visibility = View.VISIBLE
        txt_title!!.text = title
        return this
    }

    fun setCancelable(cancel: Boolean): IosSheetDialog {
        dialog!!.setCancelable(cancel)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): IosSheetDialog {
        dialog!!.setCanceledOnTouchOutside(cancel)
        return this
    }

    /**
     *
     * @param strItem
     * 条目名称
     * @param color
     * 条目字体颜色，设置null则默认蓝色
     * @param listener /(which:Int)->Unit) 函数类型也可以，但java需要适配Unit
     * @return
     */
    fun addSheetItem(strItem: String, color: SheetItemColor,
                     listener: OnSheetItemClickListener): IosSheetDialog {
        if (sheetItemList == null) {
            sheetItemList = ArrayList()
        }
        sheetItemList!!.add(SheetItem(strItem, color, listener))
        return this
    }

    /** 设置条目布局  */
    private fun setSheetItems() {
        if (sheetItemList == null || sheetItemList!!.size <= 0) {
            return
        }

        val size = sheetItemList!!.size

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            val params = sLayout_content!!
                    .layoutParams as LinearLayout.LayoutParams
            params.height = display.height / 2
            sLayout_content!!.layoutParams = params
        }

        // 循环添加条目
        for (i in 1..size) {
            val sheetItem = sheetItemList!![i - 1]
            val strItem = sheetItem.name
            val color = sheetItem.color

            val textView = TextView(context)
            textView.text = strItem
            textView.textSize = 18f
            textView.gravity = Gravity.CENTER

            // 背景图片
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.iossheet_bottom_selector)
                } else {
                    textView.setBackgroundResource(R.drawable.iossheet_single_selector)
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.iossheet_middle_selector)
                    } else {
                        textView.setBackgroundResource(R.drawable.iossheet_bottom_selector)
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.iossheet_top_selector)
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.iossheet_middle_selector)
                    } else {
                        textView.setBackgroundResource(R.drawable.iossheet_bottom_selector)
                    }
                }
            }

            // 字体颜色
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .colorName))
            } else {
                textView.setTextColor(Color.parseColor(color.colorName))
            }

            // 高度
            val scale = context.resources.displayMetrics.density
            val height = (45 * scale + 0.5f).toInt()
            textView.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, height)

            // 点击事件
            textView.setOnClickListener {
                sheetItem.itemClickListener.onClick(i)
                //sheetItem.itemClickListener.invoke(i)
                dialog!!.dismiss()
            }

            lLayout_content!!.addView(textView)
        }
    }

    fun show() {
        setSheetItems()
        dialog!!.show()
    }

    /**
     * kotlin的接口不能SAM，java的才行
     */
    //interface OnSheetItemClickListener {
    //    fun onClick(which: Int)
    //}

    inner class SheetItem(internal var name: String, internal var color: SheetItemColor?,
                          internal var itemClickListener: OnSheetItemClickListener)

    enum class SheetItemColor private constructor(var colorName: String?) {
        Blue("#037BFF"), Red("#FD4A2E")
    }
}
