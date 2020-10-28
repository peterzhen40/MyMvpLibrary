package cn.peterzhen.demo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import butterknife.BindView
import cn.cbsd.mvplibrary.mvp.XActivity
import cn.cbsd.mvplibrary.widget.IosDialog
import cn.cbsd.mvplibrary.widget.OnSheetItemClickListener
import com.afollestad.materialdialogs.MaterialDialog
import com.orhanobut.logger.Logger
import es.dmoral.toasty.MyToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainActivity : XActivity() {
    @JvmField
    @BindView(R.id.button1)
    var mButton1: Button? = null

    @JvmField
    @BindView(R.id.button2)
    var mButton2: Button? = null

    @JvmField
    @BindView(R.id.textView)
    var mTextView: TextView? = null

    @JvmField
    @BindView(R.id.activity_main)
    var mActivityMain: LinearLayout? = null
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        MyToast.init(application, true, true)
        mButton1!!.setOnClickListener {
            showDialog()
        }
        mButton2!!.setOnClickListener {
            showDialog2()
        }

    }

    private fun showDialog() {
        val text = """
            1.更改：公安修改单位信息不校验必填项；
            2.新增：单位信息和注册信息新增分局审核；
            3.新增：安全检查选择单位可以查看单位详情；
            4.更改：公安管理员账号不能进行安全检查及现场巡查；
            5.更改：辖区统计单位列表字段调整
            1.更改：公安修改单位信息不校验必填项；
            2.新增：单位信息和注册信息新增分局审核；
            3.新增：安全检查选择单位可以查看单位详情；
            4.更改：公安管理员账号不能进行安全检查及现场巡查；
            5.更改：辖区统计单位列表字段调整
            1.更改：公安修改单位信息不校验必填项；
            2.新增：单位信息和注册信息新增分局审核；
            3.新增：安全检查选择单位可以查看单位详情；
            4.更改：公安管理员账号不能进行安全检查及现场巡查；
            5.更改：辖区统计单位列表字段调整
            1.更改：公安修改单位信息不校验必填项；
            2.新增：单位信息和注册信息新增分局审核；
            3.新增：安全检查选择单位可以查看单位详情；
            4.更改：公安管理员账号不能进行安全检查及现场巡查；
            end.更改：辖区统计单位列表字段调整
        """.trimIndent()

        val longText = "是否删除该视频的缴费订单？可以再次登记"

        val list: MutableList<String> = ArrayList()
        for (i in 0..19) {
            list.add("item" + (i + 1))
        }

        IosDialog(context).builder()
                //.setTitle("温馨提示")
                .setMessage(longText)
                //.setItems(list, listener = OnSheetItemClickListener {
                //    getvDelegate().showInfo(String.format("点击的是%d", it))
                //})
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null)
                .show()
    }

    private fun showDialog2() {
        var page =1
        do {
            Timber.d(page.toString())
            page++
        }while (page<10)
        //val list: MutableList<String?> = ArrayList()
        //for (i in 0..19) {
        //    list.add("item" + (i + 1))
        //}
        //MaterialDialog.Builder(context)
        //        .items(list)
        //        .itemsCallbackSingleChoice(0) { dialog: MaterialDialog?, itemView: View?, which: Int, text: CharSequence? ->
        //            getvDelegate().showInfo(String.format("点击的是%d", which))
        //            false
        //        }
        //        .show()

        //AlertDialog.Builder(context)
        //        .setTitle("温馨提示")
        //        .setMessage("公安修改单位信息不校验必填项")
        //        .setPositiveButton("确定", null)
        //        .setNegativeButton("取消", null)
        //        .show()
    }

    override fun newP(): Any? {
        return null
    }
}