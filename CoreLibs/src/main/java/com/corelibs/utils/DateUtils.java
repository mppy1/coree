package com.corelibs.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取系统时间工具类
 */
public class DateUtils {
    // 年度日期的格式
    public static final String YEAR_FORMAT_DATE = "yyyy";
    // 月份日期的格式
    public static final String MONTH_FORMAT_DATE = "MM";
    // 日的日期的格式
    public static final String DAY_FORMAT_DATE = "dd";
    // 中国式日期的格式
    public static final String CHINESE_DATE_FORMAT = "MM月dd日 HH:mm:ss";
    // 中国式日期的格式
    public static final String CHINESE_YEARS_FORMAT = "yyyy年MM月dd日";
    // 中国式日期的格式
    public static final String CHINESE_YEAR_FORMAT = "yy年MM月dd日";
    // 默认日期格式
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-ddHH:mm:ss";
    // 日期的格式
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    // 日期的格式
    public static final String DATE_FORMAT_MYDATE = "yyyy.MM.dd";
    // 小时日哥的格式
    public static final String HOUR_FORMAT_DATE = "HH:mm";

    // 分钟的日期格式
    public static final String MINUTE_FORMAT_DATE = "MM-dd HH:mm";
    // 默认的时间格式
    public static final String YEAR_MINUTE_FORMAT_DATE = "yyyy-MM-dd HH:mm";
    // 默认的时间格式
    public static final String YEAR2_MINUTE_FORMAT_DATE = "yy-MM-dd HH:mm";
    // 全样式默认的时间格式
    public static final String DEFAULT_DATE_MILLIS_FORMAT = "yyyy-MM-dd HH:mm:ss:sss";

    public static final String DEFAULT_DATE_FILE_MILLI_FORMAT = "yyyyMMddHHmmss";

    public static final String DEFAULT_DATE_FILE_FORMAT = "yyyyMMddHHmmssSSS";

    public static final String DEFAULT_DATE_PIC_FORMAT = "yyyyMMdd";

    public static final String DEFAULT_DATE_VOICE_FORMAT = "mm:ss";

    public static final String DEFAULT_HOURS_VOICE_FORMAT = "HH:mm:ss";


    public static String getTime(String time, String dateFormat){
        long longTime = convert2long(time);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat(dateFormat);
        return dateFormat1.format(new Date(longTime));
    }

    // 把long型时间转换成指定格式时间
    public static String getTime(long timeInMillis, String dateFormat) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat(dateFormat);
        return dateFormat1.format(new Date(timeInMillis));
    }

    // 把long型时间转换成默认格式时间
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }
    // 把long型时间转换成默认格式时间
    public static String getTime2(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    // 获取系统当前时间戳
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    // 获取（默认时间格式的）系统当前时间戳
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    // 获取（指定时间格式的）系统当前时间戳
    public static String getCurrentTimeInString(String dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    // 获取（指定时间格式的）系统当前时间戳(精确到毫秒)
    public static String getCurrentMillsInString() {
        return getTime(getCurrentTimeInLong(), DEFAULT_DATE_MILLIS_FORMAT);
    }

    // 获取系统当前unix时间戳 毫秒数/1000,java13为 php10位
    public static long getCurrentUnixTimeInLong() {
        return System.currentTimeMillis() / 1000;
    }

    public static String currentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATE);
        String currentDate = sdf.format(date);
        return currentDate;
    }

    public static String getTime() {
        return new SimpleDateFormat(MINUTE_FORMAT_DATE, Locale.CHINA)
                .format(new Date());
    }

    public static String getYears() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_FORMAT_DATE);
        String currentYear = sdf.format(date);
        return currentYear;

    }

    public static String getMonth() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(MONTH_FORMAT_DATE);
        String currentMonth = sdf.format(date);
        return currentMonth;
    }


    public static String getDay(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT_DATE);
        String currentDay = sdf.format(date);
        return currentDay;
    }

    public static String getDay() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT_DATE);
        String currentDay = sdf.format(date);
        return currentDay;
    }

    // 格式化时间1000ms转化为1'1''
    public static String formatVoiceTime(long millsTime) {
        int s = Math.round((float) millsTime / 1000);
        if (s > 60) {
            int m = s / 60;
            s = s % 60;

            if (s > 0) {
                return m + "'" + s + "''";
            } else {
                return m + "'";
            }
        } else {
            return s + "''";
        }
    }

    // 格式化时间1000ms转化为1:12
    public static String formatVoiceTime2(long millsTime) {
        if (millsTime > 1000 * 60 * 60) { //大于1小时
            return getTime(millsTime, DEFAULT_HOURS_VOICE_FORMAT);
        } else {
            return getTime(millsTime, DEFAULT_DATE_VOICE_FORMAT);
        }
    }

    // 格式化时间1000ms转化为1:12
    public static String formatVoiceTime3(long millsTime) {
        int s = Math.round((float) millsTime / 1000);
        if (s > 60) {
            int m = s / 60;
            s = s % 60;

            if (s > 0) {
                return (m > 9 ? m : "0" + m) + ":" + (s > 9 ? s : "0" + s);
            } else {
                return (m > 9 ? m : "0" + m) + ":00";
            }
        } else {
            return "00:" + (s > 9 ? s : "0" + s);
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @return
     */
    public static long convert2long(String date) {
        long time = getCurrentTimeInLong();
        try {
            if (!TextUtils.isEmpty(date)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                time = dateFormat.parse(date).getTime();
            }
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return time;
    }

    /**
     * @param date      日期 秒
     * @param otherDate 另一个日期
     * @return 相差天数。如果失败则返回-1
     */
    public static int getIntervalDays(long date, long otherDate) {
        int num = -1;
        long time = Math.abs(date - otherDate);
        num = (int) (time / (24 * 60 * 60));
        return num;
    }

    /**
     * @param date      日期
     * @param otherDate 另一个日期
     * @return 相差小时数。如果失败则返回-1
     */
    public static int getIntervalHours(long date, long otherDate) {
        int num = -1;
        long time = Math.abs(date - otherDate);
        num = (int) (time / (60 * 60));
        return num;
    }

    public static int getIntervalMinutes(long date, long otherDate) {
        int num = -1;
        long time = Math.abs(date - otherDate);
        num = (int) (time / (60));
        return num;
    }

    /**
     * @param date
     * 日期
     * @param otherDate
     * 另一个日期
     * @return 转中文日期
     */
    private static final String[] NUMBERS = {"零", "一", "二", "三", "四", "五",
            "六", "七", "八", "九"};

    public static Calendar getChinese(long longtime) {
        Date dateparam = new Date(longtime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateparam);
        return calendar;
    }

    public static synchronized String toChinese(long longtime) {
        Date dateparam = new Date(longtime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateparam);
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR)).append("年")
                .append(calendar.get(Calendar.MONTH) + 1).append("月")
                .append(calendar.get(Calendar.DAY_OF_MONTH)).append("日 ")
                .append(calendar.get(Calendar.HOUR_OF_DAY)).append(":")
                .append(calendar.get(Calendar.MINUTE)).append(" ");
        return sb.toString();
    }

    public static synchronized String toChinese(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getSplitDateStr(str, 0)).append("年")
                .append(getSplitDateStr(str, 1)).append("月")
                .append(getSplitDateStr(str, 2)).append("日");
        return sb.toString();
    }

    public static String getSplitDateStr(String str, int unit) {
        // unit是单位 0=年 1=月 2日
        String[] DateStr = str.split("-");
        if (unit > DateStr.length)
            unit = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DateStr[unit].length(); i++) {

            if ((unit == 1 || unit == 2) && Integer.valueOf(DateStr[unit]) > 9) {
                sb.append(convertNum(DateStr[unit].substring(0, 1)))
                        .append("十")
                        .append(convertNum(DateStr[unit].substring(1, 2)));
                break;
            } else {
                sb.append(convertNum(DateStr[unit].substring(i, i + 1)));
            }
        }
        if (unit == 1 || unit == 2) {
            return sb.toString().replaceAll("^壹", "").replace("零", "");
        }
        return sb.toString();

    }

    private static String convertNum(String str) {
        return NUMBERS[Integer.valueOf(str)];
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 评论日期自定义
     *
     * @param dateparam yyyy-MM-dd
     * @return 几小时前 几天前
     */
    public static String dateTOStringComment(Date dateparam) {
        SimpleDateFormat sdfmonth = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfyear = new SimpleDateFormat(" ");
        Date datenow = new Date();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(dateparam);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int years = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int months = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        if (years == 0) {
            if (months == 0) {
                long datelong = datenow.getTime() - dateparam.getTime();
                long day = datelong / (24 * 60 * 60 * 1000);
                long hour = (datelong / (60 * 60 * 1000) - day * 24);
                long min = ((datelong / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (datelong / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                if (day > 0) {
                    return day + "天前";
                } else {
                    if (hour > 0) {
                        return hour + "小时前";
                    } else {
                        if (min > 0) {
                            return min + "分钟前";
                        } else {
                            return "刚刚";
                        }
                    }
                }
            } else {
                return sdfmonth.format(dateparam);
            }
        } else {
            return sdfyear.format(dateparam);
        }
    }

    /**
     * 评论日期自定义
     *
     * @param longtime 1473664062000
     * @return 几小时前 几天前
     */
    public static String dateTOStringComment(long longtime) {
        Date dateparam = new Date(longtime);
        SimpleDateFormat sdfmonth = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfyear = new SimpleDateFormat(" ");
        Date datenow = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dateparam);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int years = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int months = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        if (years == 0) {
            if (months == 0) {
                long datelong = datenow.getTime() - dateparam.getTime();
                long day = datelong / (24 * 60 * 60 * 1000);
                long hour = (datelong / (60 * 60 * 1000) - day * 24);
                long min = ((datelong / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (datelong / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                if (day > 0) {
                    return day + "天前";
                } else {
                    if (hour > 0) {
                        return hour + "小时前";
                    } else {
                        if (min > 0) {
                            return min + "分钟前";
                        } else {
                            return "刚刚";
                        }
                    }
                }
            } else {
                return sdfmonth.format(dateparam);
            }
        } else {
            return sdfyear.format(dateparam);
        }
    }

    public static String dateTOStringChat(long longtime) {
        Date dateparam = new Date(longtime);
        SimpleDateFormat sdfmonth = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfyear = new SimpleDateFormat(" ");
        Date datenow = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dateparam);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int years = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int months = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        if (years == 0) {
            if (months == 0) {

                long datelong = datenow.getTime() - dateparam.getTime();        // 差距时间
                long day = datelong / (24 * 60 * 60 * 1000);
                long hour = (datelong / (60 * 60 * 1000) - day * 24);
                long min = ((datelong / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (datelong / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                if (Math.abs(Integer.parseInt(getDay()) - Integer.parseInt(getDay(dateparam.getTime()))) == 1) {    // 天数相差一天
                    return "昨天 " + getTime(longtime, HOUR_FORMAT_DATE);
                } else if (day > 0) {
                    if (day < 7){
                        return getWeek(longtime) + " " + getTime(longtime, HOUR_FORMAT_DATE);
                    }
                    else {
                        return getTime(longtime, DATE_FORMAT_DATE);
                    }
                } else {
                    if (hour > 0) {
                        return hour + "小时前";
                    } else {
                        if (min > 5) {
                            return getTime(longtime, HOUR_FORMAT_DATE);     // 小时/分钟
                        } else {
                            return "刚刚";
                        }
                    }
                }
            } else {
                return sdfmonth.format(dateparam);
            }
        } else {
            return sdfyear.format(dateparam);
        }
    }

    /**
     * 获取星期几
     *
     * @param time
     * @return
     */
    public static String getWeek(long time) {
        Date date = new Date();
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    public static Date stringToDate(String str) {
        Date sdate = null; //初始化
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date udate = dateFormat.parse(str);
            sdate = new Date(udate.getTime()); //2013-01-14
            return sdate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdate;
    }

    /**
     * 筹款多少天
     */
    public static String hasDate(String str) {
        Date dateparam = stringToDate(str);
        if (dateparam == null) {
            return "";
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dateparam);
        Calendar c2 = Calendar.getInstance();
        Date datenow = new Date();
        c2.setTime(datenow);
        int years = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        int months = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        if (years == 0) {
            if (months == 0) {
                long datelong = datenow.getTime() - dateparam.getTime();
                long day = datelong / (24 * 60 * 60 * 1000);
                long hour = (datelong / (60 * 60 * 1000) - day * 24);
                long min = ((datelong / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (datelong / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                if (day > 0) {
//                    if (day > 31) {
//                        return YEAR_MINUTE_FORMAT_DATE.format(dateparam);
//                    }
                    return day + "天前";
                } else {
                    if (hour > 0) {
                        return hour + "小时前";
                    } else {
                        if (min > 0) {
                            return min + "分钟前";
                        } else {
                            return "刚刚";
                        }
                    }
                }
            } else {
                return months + "月前";
            }
        } else {
            return years + "年前";
        }
    }

    /**
     * 筹款多少天
     */
    public static String hasDate(long str) {
        Date dateparam = longToDate(str);

        Calendar c1 = Calendar.getInstance();
        c1.setTime(dateparam);
        Calendar c2 = Calendar.getInstance();
        Date datenow = new Date();
        c2.setTime(datenow);
        int years = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        int months = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        if (years == 0) {
            if (months == 0) {
                long datelong = datenow.getTime() - dateparam.getTime();
                long day = datelong / (24 * 60 * 60 * 1000);
                long hour = (datelong / (60 * 60 * 1000) - day * 24);
                long min = ((datelong / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (datelong / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                if (day > 0) {
//                    if (day > 31) {
//                        return YEAR_MINUTE_FORMAT_DATE.format(dateparam);
//                    }
                    return day + "天前";
                } else {
                    if (hour > 0) {
                        return hour + "小时前";
                    } else {
                        if (min > 0) {
                            return min + "分钟前";
                        } else {
                            return "刚刚";
                        }
                    }
                }
            } else {
                return months + "月前";
            }
        } else {
            return years + "年前";
        }
    }

    public static Date longToDate(long str) {
        Date sdate = null; //初始化
        try {
            sdate = new Date(str); //2013-01-14
            return sdate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdate;
    }

}
