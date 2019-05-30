package cn.cbsd.mvplibrary.kit;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;
import java.util.Date;


/**
 * 时间选择器
 *
 * @author zhenyanjun
 * @date 2018/10/23 09:48
 */

public class DateTimePickerUtil {

    /**
     * 同时选择日期与时间 记住选择的日期
     */
    public static void dateTimePicker(Activity activity, final TextView target, final String pattern, Calendar selectDate,
                                      Calendar startDate, Calendar endDate) {
        if (startDate == null) {
            startDate = Calendar.getInstance();
            startDate.set(Calendar.YEAR, 1900);
        }
        if (endDate == null) {
            endDate = Calendar.getInstance();
            endDate.set(Calendar.YEAR, 2100);
        }

        TimePickerView timePickerView = new TimePickerBuilder(activity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String dateStr;
                if (TextUtils.isEmpty(pattern)) {
                    dateStr = DateUtil.getFormatDateTime(date);
                } else {
                    dateStr = DateUtil.getFormatDate(date, pattern);
                }
                target.setText(dateStr);
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})
                .setRangDate(startDate, endDate)
                .isCyclic(true)
                .build();
        //默认
        timePickerView.setDate(selectDate);
        timePickerView.show(target);
    }

    public static void dateTimePicker(Activity activity, TextView target) {
        dateTimePicker(activity, target, "", Calendar.getInstance(), null, null);
    }

    public static void dateTimePicker(Activity activity, TextView target, String pattern) {
        dateTimePicker(activity, target, pattern, Calendar.getInstance(), null, null);
    }

    public static void dateTimePicker(Activity activity, final TextView target, Calendar startDate, Calendar endDate) {
        dateTimePicker(activity, target, "", Calendar.getInstance(), startDate, endDate);
    }

    public static void dateTimePicker(Activity activity, final TextView target, Calendar selectDate, Calendar startDate, Calendar endDate) {
        dateTimePicker(activity, target, "", selectDate, startDate, endDate);
    }

    /**
     * 兼容旧版本
     */
    public static void dateTimePicker(Activity activity, final TextView target, long minDate, long maxDate) {
        Calendar now = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        if (minDate != 0) {
            startDate.setTimeInMillis(minDate);
        } else {
            //从1900开始
            startDate.set(Calendar.YEAR, 1900);
        }
        Calendar endDate = Calendar.getInstance();
        if (maxDate != 0) {
            endDate.setTimeInMillis(maxDate);
        } else {
            //从2100结束
            endDate.set(Calendar.YEAR, 2100);
        }

        TimePickerView timePickerView = new TimePickerBuilder(activity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                target.setText(DateUtil.getFormatDateTime(date));
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})
                .setRangDate(startDate, endDate)
                .isCyclic(true)
                .build();
        //默认
        timePickerView.setDate(now);
        timePickerView.show(target);
    }

    /**
     * 选择日期
     */
    public static void datePicker(Activity activity, final TextView target, final String pattern, Calendar selectDate,
                                  Calendar startDate, Calendar endDate, boolean isCycle) {
        if (startDate == null) {
            startDate = Calendar.getInstance();
            startDate.set(Calendar.YEAR, 1900);
        }
        if (endDate == null) {
            endDate = Calendar.getInstance();
            endDate.set(Calendar.YEAR, 2100);
        }
        TimePickerView timePickerView = new TimePickerBuilder(activity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String dateStr;
                if (TextUtils.isEmpty(pattern)) {
                    dateStr = DateUtil.getFormatDate(date);
                } else {
                    dateStr = DateUtil.getFormatDate(date, pattern);
                }
                target.setText(dateStr);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setRangDate(startDate, endDate)
                .isCyclic(isCycle)
                .build();
        //默认
        timePickerView.setDate(selectDate);
        timePickerView.show(target);
    }

    public static void datePicker(Activity activity, TextView target) {
        datePicker(activity, target, "", Calendar.getInstance(), null, null,true);
    }

    public static void datePicker(Activity activity, TextView target, String pattern) {
        datePicker(activity, target, pattern, Calendar.getInstance(), null, null,true);
    }

    public static void datePicker(Activity activity, TextView target, Calendar startDate, Calendar endDate) {
        datePicker(activity, target, "", Calendar.getInstance(), startDate, endDate,true);
    }

    public static void datePicker(Activity activity, TextView target, Calendar startDate, Calendar endDate, boolean isCycle) {
        datePicker(activity, target, "", Calendar.getInstance(), startDate, endDate ,isCycle);
    }

    /**
     * 兼容旧版本
     */
    public static void datePicker(Activity activity, final TextView target, long minDate, long maxDate) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        if (minDate != 0) {
            startDate.setTimeInMillis(minDate);
        } else {
            //从1900开始
            startDate.set(Calendar.YEAR, 1900);
        }
        if (maxDate != 0) {
            endDate.setTimeInMillis(maxDate);
        } else {
            //从2100结束
            endDate.set(Calendar.YEAR, 2100);
        }

        TimePickerView timePickerView = new TimePickerBuilder(activity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                target.setText(DateUtil.getFormatDate(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})//年月日
                .setRangDate(startDate, endDate)
                .isCyclic(true)
                .build();
        //默认
        timePickerView.setDate(Calendar.getInstance());
        timePickerView.show(target);
    }


    /**
     * 选择时间
     */
    public static void timePicker(Activity activity, final TextView target) {
        TimePickerView timePickerView = new TimePickerBuilder(activity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                target.setText(DateUtil.getFormatTime(date));
            }
        })
                .setType(new boolean[]{false, false, false, true, true, true})//时分秒
                .isCyclic(true)
                .build();
        timePickerView.show(target);
    }

}