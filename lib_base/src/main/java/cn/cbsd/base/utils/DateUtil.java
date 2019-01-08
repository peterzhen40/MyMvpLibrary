package cn.cbsd.base.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.cbsd.base.BaseConfig;
import timber.log.Timber;

/**
 * 日期工具类
 * @author zhenyanjun
 * @date 2018/10/23 09:48
 */

public class DateUtil {
    public static final String DEFAULT_DATE_TIME = BaseConfig.DEFAULT_DATE_TIME;
    public static final String DEFAULT_DATE = BaseConfig.DEFAULT_DATE;
    public static final String DEFAULT_TIME = BaseConfig.DEFAULT_TIME;

    public static String getFormatDate(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getFormatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getFormatDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getFormatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_TIME, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static Date getDate(String dateStr, String pattern) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更改日期格式
     * @param dateStr 日期
     * @param oldPattern 旧格式
     * @param newPattern 新格式
     * @return
     */
    public static String getNewFormatDate(String dateStr, String oldPattern, String newPattern) {
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        } else {
            try {
                //将String按照之前的格式转为Date，格式不一致会报错
                Date date = new SimpleDateFormat(oldPattern).parse(dateStr);
                //再转成新的格式
                return new SimpleDateFormat(newPattern).format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat(DEFAULT_DATE).format(new Date());
    }

    public static String getCurrentDate(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static String getCurrentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME);
        return format.format(date);
    }
    public static String getCurrentTime(String pattern) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat(pattern);
        String time = format.format(date);
        return time;
    }

    public static String getCurrentTimeNianYue() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = format.format(date);
        return time;
    }

    private static SimpleDateFormat m = new SimpleDateFormat("MM");
    private static SimpleDateFormat d = new SimpleDateFormat("dd");
    private static SimpleDateFormat md = new SimpleDateFormat("MM-dd");
    private static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat ymdDot = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat ymdhmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat mdhm = new SimpleDateFormat("MM月dd日 HH:mm");
    private static SimpleDateFormat mdhmLink = new SimpleDateFormat("MM-dd HH:mm");
    private static SimpleDateFormat nianyueri = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 年月日[2015-07-28]
     */
    public static String getYmd(long timeInMills) {
        return ymd.format(new Date(timeInMills));
    }

    /**
     * 年月日
     */
    public static String getNianYueRi(long timeInMills) {
        return nianyueri.format(new Date(timeInMills));
    }

    public static String getYmdDot(long timeInMills) {
        return ymdDot.format(new Date(timeInMills));
    }

    public static String getYmdhms(long timeInMills) {
        return ymdhms.format(new Date(timeInMills));
    }

    public static String getYmdhmsS(long timeInMills) {
        return ymdhmss.format(new Date(timeInMills));
    }

    public static String getYmdhm(long timeInMills) {
        return ymdhm.format(new Date(timeInMills));
    }

    public static String getHm(long timeInMills) {
        return hm.format(new Date(timeInMills));
    }

    public static String getMd(long timeInMills) {
        return md.format(new Date(timeInMills));
    }

    public static String getMdhm(long timeInMills) {
        return mdhm.format(new Date(timeInMills));
    }

    public static String getMdhmLink(long timeInMills) {
        return mdhmLink.format(new Date(timeInMills));
    }

    public static String getM(long timeInMills) {
        return m.format(new Date(timeInMills));
    }

    public static String getD(long timeInMills) {
        return d.format(new Date(timeInMills));
    }

    /**
     * 是否是今天
     *
     * @param timeInMills
     * @return
     */
    public static boolean isToday(long timeInMills) {
        String dest = getYmd(timeInMills);
        String now = getYmd(Calendar.getInstance().getTimeInMillis());
        return dest.equals(now);
    }

    /**
     * 是否是同一天
     *
     * @param aMills
     * @param bMills
     * @return
     */
    public static boolean isSameDay(long aMills, long bMills) {
        String aDay = getYmd(aMills);
        String bDay = getYmd(bMills);
        return aDay.equals(bDay);
    }

    /**
     * 获取年份
     *
     * @param mills
     * @return
     */
    public static int getYear(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     *
     * @param mills
     * @return
     */
    public static int getMonth(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.MONTH) + 1;
    }


    /**
     * 获取月份的天数
     *
     * @param mills
     * @return
     */
    public static int getDaysInMonth(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (year % 4 == 0) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }


    /**
     * 获取星期,0-周日,1-周一，2-周二，3-周三，4-周四，5-周五，6-周六
     *
     * @param mills
     * @return
     */
    public static int getWeek(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);

        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取当月第一天的时间（毫秒值）
     *
     * @param mills
     * @return
     */
    public static long getFirstOfMonth(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTimeInMillis();
    }

    /**
     * 是否在今天之后
     */
    public static boolean isDateAfter(DatePicker tempView) {
        Calendar mCalendar = Calendar.getInstance();
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(tempView.getYear(), tempView.getMonth(),
                tempView.getDayOfMonth(), 0, 0, 0);
        return tempCalendar.after(mCalendar);
    }

    /**
     * 是否在今天之后<br/> 包括今天
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isAfterToday(Date date) {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(date);
        String todayStr = sdf.format(today);
        Timber.d("%s  isAfterToday  %s", todayStr, dateStr);

        try {
            Date newToday = sdf.parse(todayStr);
            Date newDate = sdf.parse(dateStr);
            return newDate.getTime() >= newToday.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否为同一天
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isSameDay(Date date, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date2Str = sdf.format(date2);
        String dateStr = sdf.format(date);
        try {
            Date newDate = sdf.parse(dateStr);
            Date newDate2 = sdf.parse(date2Str);
            return newDate.getTime() == newDate2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否为同一天
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isSameDay(String dateStr, String date2Str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date newDate = sdf.parse(dateStr);
            Date newDate2 = sdf.parse(date2Str);
            return newDate.getTime() == newDate2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否当天
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isToday(String dateStr) {
        if (!TextUtils.isEmpty(dateStr)) {
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = sdf.format(today);
            try {
                Date newDate = sdf.parse(dateStr);
                Date newToday = sdf.parse(todayStr);
                return newDate.getTime() == newToday.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 是否为同一天 格式默认为yyyyMMdd HH:mm格式，以空格分隔取前位
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isSameDay(String dateStr, String date2Str, String pattern) {
        SimpleDateFormat commonSdf = new SimpleDateFormat("yyyyMMdd");
        try {
            String newDateStr = dateStr.split(" ")[0];
            String newDateStr2 = date2Str.split(" ")[0];
            Timber.e("前=%s\n后：%s", dateStr, date2Str);
            Date newDate = commonSdf.parse(newDateStr);
            Date newDate2 = commonSdf.parse(newDateStr2);
            return newDate.getTime() == newDate2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 第一个时间是否在第二个时间之后 开始时间必须在后者时间之前
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isAfterPreTime(String dateStr, String date2Str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
        try {
            Date newDate = sdf.parse(dateStr);
            Date newDate2 = sdf.parse(date2Str);
            return newDate.getTime() <= newDate2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
