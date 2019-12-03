package cn.cbsd.mvplibrary.kit

import android.app.Activity
import android.text.TextUtils
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import java.util.*


/**
 * 时间选择器
 *
 * @author zhenyanjun
 * @date 2018/10/23 09:48
 */

object DateTimePickerUtil {

    /**
     * 同时选择日期与时间 记住选择的日期
     */
    @JvmStatic
    @JvmOverloads
    fun dateTimePicker(activity: Activity, target: TextView, pattern: String = "", selectDate: Calendar = Calendar.getInstance(),
                       startDate: Calendar? = null, endDate: Calendar? = null) {
        var startDate = startDate
        var endDate = endDate
        if (startDate == null) {
            startDate = Calendar.getInstance()
            startDate!!.set(Calendar.YEAR, 1900)
        }
        if (endDate == null) {
            endDate = Calendar.getInstance()
            endDate!!.set(Calendar.YEAR, 2100)
        }

        val timePickerView = TimePickerBuilder(activity, OnTimeSelectListener { date, v ->
            val dateStr: String
            if (TextUtils.isEmpty(pattern)) {
                dateStr = DateUtil.getFormatDateTime(date)
            } else {
                dateStr = DateUtil.getFormatDate(date, pattern)
            }
            target.text = dateStr
        })
                .setType(booleanArrayOf(true, true, true, true, true, true))
                .setRangDate(startDate, endDate)
                .isCyclic(true)
                .build()
        //默认
        timePickerView.setDate(selectDate)
        timePickerView.show(target)
    }

    @JvmStatic
    fun dateTimePicker(activity: Activity, target: TextView, startDate: Calendar, endDate: Calendar) {
        dateTimePicker(activity, target, "", Calendar.getInstance(), startDate, endDate)
    }

    @JvmStatic
    fun dateTimePicker(activity: Activity, target: TextView, selectDate: Calendar, startDate: Calendar, endDate: Calendar) {
        dateTimePicker(activity, target, "", selectDate, startDate, endDate)
    }

    /**
     * 兼容旧版本
     */
    @JvmStatic
    fun dateTimePicker(activity: Activity, target: TextView, minDate: Long, maxDate: Long) {
        val now = Calendar.getInstance()
        val startDate = Calendar.getInstance()
        if (minDate != 0L) {
            startDate.timeInMillis = minDate
        } else {
            //从1900开始
            startDate.set(Calendar.YEAR, 1900)
        }
        val endDate = Calendar.getInstance()
        if (maxDate != 0L) {
            endDate.timeInMillis = maxDate
        } else {
            //从2100结束
            endDate.set(Calendar.YEAR, 2100)
        }

        val timePickerView = TimePickerBuilder(activity, OnTimeSelectListener { date, v -> target.text = DateUtil.getFormatDateTime(date) })
                .setType(booleanArrayOf(true, true, true, true, true, true))
                .setRangDate(startDate, endDate)
                .isCyclic(true)
                .build()
        //默认
        timePickerView.setDate(now)
        timePickerView.show(target)
    }

    /**
     * 选择日期
     */
    @JvmStatic
    @JvmOverloads
    fun datePicker(activity: Activity, target: TextView, pattern: String = "", selectDate: Calendar = Calendar.getInstance(),
                   startDate: Calendar? = null, endDate: Calendar? = null, isCycle: Boolean = true) {
        var startDate = startDate
        var endDate = endDate
        if (startDate == null) {
            startDate = Calendar.getInstance()
            startDate!!.set(Calendar.YEAR, 1900)
        }
        if (endDate == null) {
            endDate = Calendar.getInstance()
            endDate!!.set(Calendar.YEAR, 2100)
        }
        val timePickerView = TimePickerBuilder(activity, OnTimeSelectListener { date, v ->
            val dateStr: String
            if (TextUtils.isEmpty(pattern)) {
                dateStr = DateUtil.getFormatDate(date)
            } else {
                dateStr = DateUtil.getFormatDate(date, pattern)
            }
            target.text = dateStr
        })
                .setType(booleanArrayOf(true, true, true, false, false, false))
                .setRangDate(startDate, endDate)
                .isCyclic(isCycle)
                .build()
        //默认
        timePickerView.setDate(selectDate)
        timePickerView.show(target)
    }

    @JvmStatic
    fun datePicker(activity: Activity, target: TextView, startDate: Calendar, endDate: Calendar) {
        datePicker(activity, target, "", Calendar.getInstance(), startDate, endDate, true)
    }

    @JvmStatic
    fun datePicker(activity: Activity, target: TextView, startDate: Calendar, endDate: Calendar, isCycle: Boolean) {
        datePicker(activity, target, "", Calendar.getInstance(), startDate, endDate, isCycle)
    }

    /**
     * 兼容旧版本
     */
    @JvmStatic
    fun datePicker(activity: Activity, target: TextView, minDate: Long, maxDate: Long) {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        if (minDate != 0L) {
            startDate.timeInMillis = minDate
        } else {
            //从1900开始
            startDate.set(Calendar.YEAR, 1900)
        }
        if (maxDate != 0L) {
            endDate.timeInMillis = maxDate
        } else {
            //从2100结束
            endDate.set(Calendar.YEAR, 2100)
        }

        val timePickerView = TimePickerBuilder(activity, OnTimeSelectListener { date, v -> target.text = DateUtil.getFormatDate(date) })
                .setType(booleanArrayOf(true, true, true, false, false, false))//年月日
                .setRangDate(startDate, endDate)
                .isCyclic(true)
                .build()
        //默认
        timePickerView.setDate(Calendar.getInstance())
        timePickerView.show(target)
    }


    /**
     * 选择时间
     */
    @JvmStatic
    fun timePicker(activity: Activity, target: TextView) {
        val timePickerView = TimePickerBuilder(activity, OnTimeSelectListener { date, v -> target.text = DateUtil.getFormatTime(date) })
                .setType(booleanArrayOf(false, false, false, true, true, true))//时分秒
                .isCyclic(true)
                .build()
        timePickerView.show(target)
    }

    /**
     * 选择月份
     */
    @JvmStatic
    fun monthPicker(activity: Activity, target: TextView, pattern: String = "", selectDate: Calendar = Calendar.getInstance(),
                   startDate: Calendar? = null, endDate: Calendar? = null, isCycle: Boolean = true) {
        var startDate = startDate
        var endDate = endDate
        if (startDate == null) {
            startDate = Calendar.getInstance()
            startDate!!.set(Calendar.YEAR, 1900)
        }
        if (endDate == null) {
            endDate = Calendar.getInstance()
            endDate!!.set(Calendar.YEAR, 2100)
        }
        val timePickerView = TimePickerBuilder(activity, OnTimeSelectListener { date, v ->
            val dateStr: String
            if (TextUtils.isEmpty(pattern)) {
                dateStr = DateUtil.getFormatDate(date)
            } else {
                dateStr = DateUtil.getFormatDate(date, pattern)
            }
            target.text = dateStr
        })
                .setType(booleanArrayOf(true, true, false, false, false, false))
                .setRangDate(startDate, endDate)
                .isCyclic(isCycle)
                .build()
        //默认
        timePickerView.setDate(selectDate)
        timePickerView.show(target)
    }

}