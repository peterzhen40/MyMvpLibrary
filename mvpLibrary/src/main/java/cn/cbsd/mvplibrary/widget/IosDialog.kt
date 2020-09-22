package cn.cbsd.mvplibrary.widget

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.cbsd.mvplibrary.R

/**
 * Author: liuqiang
 * Time: 2018-01-02 13:28
 * Description:
 */
class IosDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private var rootView: LinearLayout? = null
    private var tvTitle: TextView? = null
    private var tvMsg: TextView? = null
    private var btn_neg: Button? = null
    private var btn_pos: Button? = null
    private var iv_line: ImageView? = null
    private var scrollView: NestedScrollView? = null
    private val display: Display
    private var showTitle = false
    private var showMsg = false
    private var showPosBtn = false
    private var showNegBtn = false
    private var showList = false

    private lateinit var recyclerView: RecyclerView
    private lateinit var llButton: LinearLayout

    init {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
    }

    fun builder(): IosDialog {
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_ios2, null)

        // 获取自定义Dialog布局中的控件
        rootView = view.findViewById(R.id.root_view)
        tvTitle = view.findViewById(R.id.tv_title)
        tvTitle?.visibility = View.GONE
        tvMsg = view.findViewById(R.id.tv_msg)
        btn_neg = view.findViewById(R.id.btn_no)
        btn_neg?.visibility = View.GONE
        btn_pos = view.findViewById(R.id.btn_yes)
        btn_pos?.visibility = View.GONE
        iv_line = view.findViewById(R.id.iv_line_ver)
        iv_line?.visibility = View.GONE
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.visibility = View.GONE
        scrollView = view.findViewById(R.id.scrollView)
        scrollView?.visibility = View.GONE
        llButton = view.findViewById(R.id.ll_button)

        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.AlertDialogStyle)
        dialog?.setContentView(view)

        // 调整dialog背景大小
        view.layoutParams = FrameLayout.LayoutParams((display
                .width * 0.85).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)

        //限制内容最大高度
        //view.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
        //    val contentHeight = view.height
        //    val needHeight = 600
        //    if (contentHeight > needHeight) {
        //        val layoutParams = FrameLayout.LayoutParams(
        //            (display.width * 0.85).toInt(), needHeight)
        //        layoutParams.gravity = Gravity.CENTER
        //        view.layoutParams = layoutParams
        //    }
        //
        //}
        scrollView?.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val contentHeight = v.height
            val needHeight = 500
            if (contentHeight > needHeight) {
                scrollView!!.layoutParams.height = needHeight
            }
        }
        recyclerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val contentHeight = v.height
            val needHeight = 500
            if (contentHeight > needHeight) {
                recyclerView.layoutParams.height = needHeight
            }
        }

        return this
    }

    fun setTitle(title: String?): IosDialog {
        showTitle = true
        if ("" == title) {
            tvTitle?.text = "标题"
        } else {
            tvTitle?.text = title
        }
        return this
    }

    fun setMessage(msg: CharSequence?): IosDialog {
        showMsg = true
        if ("" == msg) {
            tvMsg?.text = "内容"
        } else {
            tvMsg?.text = msg
        }
        return this
    }

    fun setCancelable(cancel: Boolean): IosDialog {
        dialog?.setCancelable(cancel)
        return this
    }

    fun setPositiveButton(text: String?,
                          listener: View.OnClickListener?): IosDialog {
        showPosBtn = true
        if ("" == text) {
            btn_pos?.text = "确定"
        } else {
            btn_pos?.text = text
        }
        btn_pos?.setOnClickListener { v ->
            listener?.onClick(v)
            dialog?.dismiss()
        }
        return this
    }

    fun setNegativeButton(text: String?,
                          listener: View.OnClickListener?): IosDialog {
        showNegBtn = true
        if ("" == text) {
            btn_neg?.text = "取消"
        } else {
            btn_neg?.text = text
        }
        btn_neg?.setOnClickListener { v ->
            listener?.onClick(v)
            dialog?.dismiss()
        }
        return this
    }

    fun setItems(list: List<String>, selectedIndex: Int = -1, listener: OnSheetItemClickListener): IosDialog {
        showList = true

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ContentAdapter(list)
        recyclerView.adapter = adapter
        adapter.selectedIndex = selectedIndex
        adapter.listener = OnSheetItemClickListener {
            listener.onClick(it)
            dialog?.dismiss()
        }
        return this
    }

    private fun setLayout() {
        if (!showTitle && !showMsg) {
            tvTitle?.text = "提示"
            tvTitle?.visibility = View.VISIBLE
        }

        if (showTitle) {
            tvTitle?.visibility = View.VISIBLE
        }

        if (showMsg) {
            scrollView?.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos?.text = "确定"
            btn_pos?.visibility = View.VISIBLE
            btn_pos?.setBackgroundResource(R.drawable.iosdialog_single_selector)
            btn_pos?.setOnClickListener { dialog?.dismiss() }
        }

        if (showPosBtn && showNegBtn) {
            btn_pos?.visibility = View.VISIBLE
            btn_pos?.setBackgroundResource(R.drawable.iosdialog_right_selector)
            btn_neg?.visibility = View.VISIBLE
            btn_neg?.setBackgroundResource(R.drawable.iosdialog_left_selector)
            iv_line?.visibility = View.VISIBLE
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos?.visibility = View.VISIBLE
            btn_pos?.setBackgroundResource(R.drawable.iosdialog_single_selector)
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg?.visibility = View.VISIBLE
            btn_neg?.setBackgroundResource(R.drawable.iosdialog_single_selector)
        }

        if (showList) {
            scrollView?.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            llButton.visibility = View.GONE
        } else {
            llButton.visibility = View.VISIBLE
        }
    }

    fun show() {
        setLayout()
        dialog?.show()
    }

    class ContentAdapter(val dataList: List<String>) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

        var selectedIndex: Int = -1
        var listener: OnSheetItemClickListener? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val root = LayoutInflater.from(parent.context).inflate(R.layout.item_ios_checkbox, parent, false)
            return ViewHolder(root)
        }

        override fun getItemCount(): Int {
            return if (dataList.isNullOrEmpty()) 0 else dataList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.checkBox.isChecked = position == selectedIndex
            holder.text.text = item

            holder.text.setOnClickListener {
                listener?.onClick(position)
                selectedIndex = position
                notifyDataSetChanged()
            }

            holder.checkBox.setOnClickListener {
                listener?.onClick(position)
                selectedIndex = position
                notifyDataSetChanged()
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
            var text: TextView = itemView.findViewById(R.id.tv_select)

        }

    }
}
