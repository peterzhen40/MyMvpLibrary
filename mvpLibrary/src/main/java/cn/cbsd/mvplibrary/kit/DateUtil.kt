package cn.cbsd.mvplibrary.kit

import android.annotation.SuppressLint
import android.text.TextUtils
import android.widget.DatePicker
import cn.cbsd.mvplibrary.CommonConfig
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author zhenyanjun
 * @date 2018/10/23 09:48
 */

object DateUtil {
    val DEFAULT_DATE_TIME = CommonConfig.Pattern.DEFAULT_DATE_TIME
    val DEFAULT_DATE = CommonConfig.Pattern.DEFAULT_DATE
    val DEFAULT_TIME = CommonConfig.Pattern.DEFAULT_TIME

    val currentDate: String
        get() = SimpleDateFormat(DEFAULT_DATE).format(Date())

    val currentTime: String
        get() {
            val date = Date()
            val format = SimpleDateFormat(DEFAULT_DATE_TIME)
            return format.format(date)
        }

    val currentTimeNianYue: String
        get() {
            val date = Date()
            val format = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
            return format.format(date)
        }

    private val m = SimpleDateFormat("MM")
    private val d = SimpleDateFormat("dd")
    private val md = SimpleDateFormat("MM-dd")
    private val ymd = SimpleDateFormat("yyyy-MM-dd")
    private val ymdDot = SimpleDateFormat("yyyy.MM.dd")
    private val ymdhms = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val ymdhmss = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    private val ymdhm = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private val hm = SimpleDateFormat("HH:mm")
    private val mdhm = SimpleDateFormat("MM月dd日 HH:mm")
    private val mdhmLink = SimpleDateFormat("MM-dd HH:mm")
    private val nianyueri = SimpleDateFormat("yyyy年MM月dd日")

    fun getFormatDate(date: Date, pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getFormatDate(date: Date): String {
        val dateFormat = SimpleDateFormat(DEFAULT_DATE, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getFormatDateTime(date: Date): String {
        val dateFormat = SimpleDateFormat(DEFAULT_DATE_TIME, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getFormatTime(date: Date): String {
        val dateFormat = SimpleDateFormat(DEFAULT_TIME, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getDate(dateStr: String, pattern: String): Date? {
        if (TextUtils.isEmpty(dateStr)) {
            return null
        }

        val format = SimpleDateFormat(pattern, Locale.getDefault())
        try {
            return format.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 更改日期格式
     * @param dateStr 日期
     * @param oldPattern 旧格式
     * @param newPattern 新格式
     * @return
     */
    fun getNewFormatDate(dateStr: String, oldPattern: String, newPattern: String): String {
        return if (TextUtils.isEmpty(dateStr)) {
            ""
        } else {
            try {
                //将String按照之前的格式转为Date，格式不一致会报错
                val date = SimpleDateFormat(oldPattern).parse(dateStr)
                //再转成新的格式
                SimpleDateFormat(newPattern).format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                ""
            }

        }
    }

    fun getCurrentDate(pattern: String): String {
        return SimpleDateFormat(pattern).format(Date())
    }

    fun getCurrentTime(pattern: String): String {
        val date = Date()
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    /**
     * 年月日[2015-07-28]
     */
    fun getYmd(timeInMills: Long): String {
        return ymd.format(Date(timeInMills))
    }

    /**
     * 年月日
     */
    fun getNianYueRi(timeInMills: Long): String {
        return nianyueri.format(Date(timeInMills))
    }

    fun getYmdDot(timeInMills: Long): String {
        return ymdDot.format(Date(timeInMills))
    }

    fun getYmdhms(timeInMills: Long): String {
        return ymdhms.format(Date(timeInMills))
    }

    fun getYmdhmsS(timeInMills: Long): String {
        return ymdhmss.format(Date(timeInMills))
    }

    fun getYmdhm(timeInMills: Long): String {
        return ymdhm.format(Date(timeInMills))
    }

    fun getHm(timeInMills: Long): String {
        return hm.format(Date(timeInMills))
    }

    fun getMd(timeInMills: Long): String {
        return md.format(Date(timeInMills))
    }

    fun getMdhm(timeInMills: Long): String {
        return mdhm.format(Date(timeInMills))
    }

    fun getMdhmLink(timeInMills: Long): String {
        return mdhmLink.format(Date(timeInMills))
    }

    fun getM(timeInMills: Long): String {
        return m.format(Date(timeInMills))
    }

    fun getD(timeInMills: Long): String {
        return d.format(Date(timeInMills))
    }

    /**
     * 是否是今天
     *
     * @param timeInMills
     * @return
     */
    fun isToday(timeInMills: Long): Boolean {
        val dest = getYmd(timeInMills)
        val now = getYmd(Calendar.getInstance().timeInMillis)
        return dest == now
    }

    /**
     * 是否是同一天
     *
     * @param aMills
     * @param bMills
     * @return
     */
    fun isSameDay(aMills: Long, bMills: Long): Boolean {
        val aDay = getYmd(aMills)
        val bDay = getYmd(bMills)
        return aDay == bDay
    }

    /**
     * 获取年份
     *
     * @param mills
     * @return
     */
    fun getYear(mills: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mills
        return calendar.get(Calendar.YEAR)
    }

    /**
     * 获取月份
     *
     * @param mills
     * @return
     */
    fun getMonth(mills: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mills
        return calendar.get(Calendar.MONTH) + 1
    }


    /**
     * 获取月份的天数
     *
     * @param mills
     * @return
     */
    fun getDaysInMonth(mills: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mills

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        when (month) {
            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> return 31
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> return 30
            Calendar.FEBRUARY -> return if (year % 4 == 0) 29 else 28
            else -> throw IllegalArgumentException("Invalid Month")
        }
    }


    /**
     * 获取星期,0-周日,1-周一，2-周二，3-周三，4-周四，5-周五，6-周六
     *
     * @param mills
     * @return
     */
    fun getWeek(mills: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mills

        return calendar.get(Calendar.DAY_OF_WEEK) - 1
    }

    /**
     * 获取当月第一天的时间（毫秒值）
     *
     * @param mills
     * @return
     */
    fun getFirstOfMonth(mills: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mills
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        return calendar.timeInMillis
    }

    /**
     * 是否在今天之后
     */
    fun isDateAfter(tempView: DatePicker): Boolean {
        val mCalendar = Calendar.getInstance()
        val tempCalendar = Calendar.getInstance()
        tempCalendar.set(tempView.year, tempView.month,
                tempView.dayOfMonth, 0, 0, 0)
        return tempCalendar.after(mCalendar)
    }

    /**
     * 是否在今天之后<br></br> 包括今天
     */
    @SuppressLint("SimpleDateFormat")
    fun isAfterToday(date: Date): Boolean {
        val today = Date()
        val sdf = SimpleDateFormat("yyyyMMdd")
        val dateStr = sdf.format(date)
        val todayStr = sdf.format(today)
        Timber.d("%s  isAfterToday  %s", todayStr, dateStr)

        try {
            val newToday = sdf.parse(todayStr)
            val newDate = sdf.parse(dateStr)
            return newDate.time >= newToday.time
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 是否为同一天
     */
    @SuppressLint("SimpleDateFormat")
    fun isSameDay(date: Date, date2: Date): Boolean {
        val sdf = SimpleDateFormat("yyyyMMdd")
        val date2Str = sdf.format(date2)
        val dateStr = sdf.format(date)
        try {
            val newDate = sdf.parse(dateStr)
            val newDate2 = sdf.parse(date2Str)
            return newDate.time == newDate2.time
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 是否为同一天
     */
    @SuppressLint("SimpleDateFormat")
    fun isSameDay(dateStr: String, date2Str: String): Boolean {
        val sdf = SimpleDateFormat("yyyyMMdd")
        try {
            val newDate = sdf.parse(dateStr)
            val newDate2 = sdf.parse(date2Str)
            return newDate.time == newDate2.time
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 是否当天
     */
    @SuppressLint("SimpleDateFormat")
    fun isToday(dateStr: String): Boolean {
        if (!TextUtils.isEmpty(dateStr)) {
            val today = Date()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val todayStr = sdf.format(today)
            try {
                val newDate = sdf.parse(dateStr)
                val newToday = sdf.parse(todayStr)
                return newDate.time == newToday.time
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return false
    }

    /**
     * 是否为同一天 格式默认为yyyyMMdd HH:mm格式，以空格分隔取前位
     */
    @SuppressLint("SimpleDateFormat")
    fun isSameDay(dateStr: String, date2Str: String, pattern: String): Boolean {
        val commonSdf = SimpleDateFormat("yyyyMMdd")
        try {
            val newDateStr = dateStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val newDateStr2 = date2Str.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            Timber.e("前=%s\n后：%s", dateStr, date2Str)
            val newDate = commonSdf.parse(newDateStr)
            val newDate2 = commonSdf.parse(newDateStr2)
            return newDate.time == newDate2.time
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 第一个时间是否在第二个时间之后 开始时间必须在后者时间之前
     */
    @SuppressLint("SimpleDateFormat")
    fun isAfterPreTime(dateStr: String, date2Str: String): Boolean {
        val sdf = SimpleDateFormat("yyyyMMdd HH:mm")
        try {
            val newDate = sdf.parse(dateStr)
            val newDate2 = sdf.parse(date2Str)
            return newDate.time <= newDate2.time
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }
}
