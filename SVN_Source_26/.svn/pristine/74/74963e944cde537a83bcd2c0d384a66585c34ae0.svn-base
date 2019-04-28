package com.app.pao.utils;

import android.text.Html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {


    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;

    public static final String FORMAT_TYPE_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TYPE_2 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_TYPE_3 = "yyyy-MM-dd";


    /**
     * 秒换成耗时字符串
     * 0:00:00
     *
     * @param Seconds 时间长度
     * @return
     */
    public static String formatDurationStr(final long Seconds) {
        long hour = Seconds / 3600;
        long min = Seconds % 3600 / 60;
        long sec = Seconds % 60;
        String hourstr = hour + ":";
        String minstr = ((min < 10) ? ("0" + String.valueOf(min)) : String.valueOf(min));
        String secstr = ((sec < 10) ? ("0" + String.valueOf(sec)) : String.valueOf(sec));
        if (hour > 0) {
            return hourstr + minstr + ":" + secstr;
        } else {
            return minstr + ":" + secstr;
        }
    }
    
    /**
     * 时间格式为yyyy-MM-dd 转换为date格式
     *
     * @param timeStr
     * @return
     */
    public static Date formatStrToDate(final String timeStr, final String format) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将时间转换为yyyy-MM-dd String 形式
     *
     * @param date
     * @return
     */
    public static String formatDateToStr(final Date date, final String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * 秒换算成配速格式
     *
     * @return
     */
    public static String formatSecondsToSpeedTime(long Seconds) {
        if (Seconds > 3600) {
            return "- -";
        }
        long min = Seconds / 60;
        long sec = Seconds % 60;
        String minstr = ((min < 10) ? ("0" + String.valueOf(min)) : String.valueOf(min));
        String secstr = ((sec < 10) ? ("0" + String.valueOf(sec)) : String.valueOf(sec));
        return minstr + "'" + secstr + "''";
    }


    /**
     * 获取2个起始时间之间的时间差
     *
     * @param beginTimeStr
     * @param endTimeStr
     * @return 秒
     */
    public static long getTimeDifference(final String beginTimeStr, final String endTimeStr) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            Date begindate = myFormatter.parse(beginTimeStr);
            Date enddate = myFormatter.parse(endTimeStr);

            time = (enddate.getTime() - begindate.getTime()) / 1000;
        } catch (Exception e) {
            return time;
        }
        return time;
    }


    /**
     * 秒换算成配速格式
     *
     * @return
     */
    public static String formatSecondsToSpeedTime(long Seconds, String pir) {
        if (Seconds > 3600) {
            return "- -";
        }
        long min = Seconds / 60;
        long sec = Seconds % 60;
        String minstr = ((min < 10) ? ("0" + String.valueOf(min)) : String.valueOf(min));
        String secstr = ((sec < 10) ? ("0" + String.valueOf(sec)) : String.valueOf(sec));
        return minstr + pir + secstr + pir + pir;
    }

    /**
     * 秒换算成配速格式
     *
     * @return
     */
    public static String formatSecondsToSpeedTime(long Seconds, String minUnit, String secUnit) {
        if (Seconds > 3600) {
            return "- -";
        }
        long min = Seconds / 60;
        long sec = Seconds % 60;
        String minstr = ((min < 10) ? ("0" + String.valueOf(min)) : String.valueOf(min));
        String secstr = ((sec < 10) ? ("0" + String.valueOf(sec)) : String.valueOf(sec));
        return minstr + minUnit + secstr + secUnit;
    }


    /**
     * 获取当前时间的String(yyyy-MM-dd HH:mm:ss)
     *
     * @return
     */
    public static String NowTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 获取当前时间的String(yyyy-MM-dd HH:mm:ss)
     *
     * @return
     */
    public static String RefreshTime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }


    /**
     * 生成语音播报时间
     *
     * @param Seconds
     * @return
     */
    public static String formatVoiceString(final long Seconds) {
        long hour = Seconds / 3600;
        long min = Seconds % 3600 / 60;
        long sec = Seconds % 60;

        String hourstr = hour + "时";
        String minstr = min + "分";
        String secstr = sec + "秒";
        if (hour == 0) {
            return minstr + secstr;
        } else {
            return hourstr + minstr + secstr;
        }
    }

    /**
     * 用时，时分秒之间用：隔开
     *
     * @param Seconds
     * @return
     */
    public static String getTimeWithFh(final long Seconds) {
        long hour = Seconds / 3600;
        long min = Seconds % 3600 / 60;
        long sec = Seconds % 60;

        String hourstr = hour + ":";
        String minstr = min + ":";
        if (min < 10) {
            minstr = "0" + minstr;
        }
        String secstr = sec + "";
        if (sec < 10) {
            secstr = "0" + secstr;
        }
        if (hour == 0) {
            return minstr + secstr;
        } else {
            return hourstr + minstr + secstr;
        }
    }

    /**
     * 配速，分秒后面加'和''
     *
     * @param Seconds
     * @return
     */
    public static String getPaceWithFh(final long Seconds) {
        long hour = Seconds / 3600;
        long min = Seconds % 3600 / 60;
        long sec = Seconds % 60;

        String minstr = min + "'";
        String secstr = sec + "''";

        return minstr + secstr;

    }

    /**
     * 时间转CharSequence 突出数字
     *
     * @param Seconds
     * @return
     */
    public static CharSequence formatUnitOutSeq(final long Seconds) {
        long hour = Seconds / 3600;
        long min = Seconds % 3600 / 60;
        long sec = Seconds % 60;

        if (hour == 0) {
            return Html.fromHtml("<font><big> " + min + "</big></font>分" + "<font><big> " + sec + "</big></font>秒");

        } else {
            return Html.fromHtml("<font><big> " + hour + "</big></font>时" + "<font><big> " + min + "</big></font>分" + "<font><big> " + sec + "</big></font>秒");
        }
    }

    /**
     * 生成报名截止时间距离
     *
     * @param Seconds
     * @return
     */
    public static String formatSignUpContractString(final long Seconds) {
        long day = Seconds / 86400;
        long hour = Seconds % 86400 / 3600;
        long min = Seconds % 3600 / 60;

        String daystr = day + "天";
        String hourstr = hour + "小时";
        String minstr = min + "分";
        return daystr + hourstr + minstr;

    }

    /**
     * 获取2个起始时间之间的时间差
     *
     * @param timeStrBegin
     * @param timeStrEnd
     * @return
     */
    public static long getTimesetFromStartTime(String timeStrBegin, String timeStrEnd) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            java.util.Date begindate = myFormatter.parse(timeStrBegin);
            java.util.Date enddate = myFormatter.parse(timeStrEnd);

            time = (enddate.getTime() - begindate.getTime()) / 1000;
        } catch (Exception e) {
            return time;
        }
        return time;
    }


    /**
     * 获取目标时间和当前时间之间的差距
     *
     * @param date
     * @return
     */
    public static String getTimestampString(Date date) {
        Date curDate = new Date();
        long splitTime = curDate.getTime() - date.getTime();
        if (splitTime < (7 * ONE_DAY)) {
            if (splitTime < ONE_MINUTE) {
                return "刚刚";
            }
            if (splitTime < ONE_HOUR) {
                return String.format("%d分钟前", splitTime / ONE_MINUTE);
            }

            if (splitTime < ONE_DAY) {
                return String.format("%d小时前", splitTime / ONE_HOUR);
            }

            return String.format("%d天前", splitTime / ONE_DAY);
        }
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return myFormatter.format(date);
    }


    /**
     * 获取目标时间和当前时间之间的差距
     *
     * @param date
     * @return
     */
    public static String getTimestampAfterString(Date date) {
        Date curDate = new Date();
        long splitTime = date.getTime() - curDate.getTime();
        if (splitTime < (7 * ONE_DAY)) {
            if (splitTime < ONE_MINUTE) {
                return "即将";
            }
            if (splitTime < ONE_HOUR) {
                return String.format("%d分钟后", splitTime / ONE_MINUTE);
            }

            if (splitTime < ONE_DAY) {
                return String.format("%d小时后", splitTime / ONE_HOUR);
            }

            return String.format("%d天后", splitTime / ONE_DAY);
        }
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return myFormatter.format(date);
    }


    /**
     * 获取目标时间和当前时间之间的差距
     *
     * @param date
     * @return
     */
    public static String getTimestampDayString(Date date) {
        String result = "M月d日";
        Date todayDate = new Date();
        String today = new SimpleDateFormat(result, Locale.CHINA).format(todayDate);
        String dateDay = new SimpleDateFormat(result, Locale.CHINA).format(date);
        if (today.equals(dateDay)) {
            return "今天";
        } else {
            return dateDay;
        }
    }

    /**
     * 判断传入的时间是当天内时间
     *
     * @param date
     * @return
     */
    public static boolean checkIsToday(Date date) {
        String result = "M月d日";
        Date todayDate = new Date();
        String today = new SimpleDateFormat(result, Locale.CHINA).format(todayDate);
        String dateDay = new SimpleDateFormat(result, Locale.CHINA).format(date);
        if (today.equals(dateDay)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 秒换成时(小时只显示一个0)
     */
    public static String formatSecondsToLongHourTime(long Seconds) {
        long hour = Seconds / 3600;
        long min = Seconds % 3600 / 60;
        long sec = Seconds % 60;

        String hourstr = ((hour < 10) ? (String.valueOf(hour) + ":") : String.valueOf(hour) + ":");

        String minstr = ((min < 10) ? ("0" + String.valueOf(min)) : String.valueOf(min));
        String secstr = ((sec < 10) ? ("0" + String.valueOf(sec)) : String.valueOf(sec));
        return hourstr + minstr + ":" + secstr;
    }

    /**
     * 获取时间推移的时间
     *
     * @param beginTime
     * @param timeofset
     * @return
     */
    public static long getAfrerTimeNum(String beginTime, int timeofset) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date begindate;
        long time = 0;
        try {
            begindate = myFormatter.parse(beginTime);
            time = begindate.getTime() + (timeofset * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getDynamicDayTime(String holeTime) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat Formatter = new SimpleDateFormat("HH:mm");
        try {
            Date date = myFormatter.parse(holeTime);
            return Formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取时间推移的时间字符串
     *
     * @param beginTime
     * @param timeofset
     * @return
     */
    public static String getAfrerTime(String beginTime, int timeofset) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date begindate;
        String result = "1984-12-20 12:00:00";
        try {
            begindate = myFormatter.parse(beginTime);
            long time = begindate.getTime() + (timeofset * 1000);
            result = myFormatter.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 时间格式为yyyy-MM-dd 转换为date格式
     *
     * @param timeStr
     * @return
     */
    public static Date stringToDate(String timeStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间格式为yyyy-MM-dd 转换为date格式
     *
     * @param timeStr
     * @return
     */
    public static Date birthdayStrToDate(String timeStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间格式为yyyy-MM-dd HH:mm:ss 转换为date格式
     *
     * @param timeStr
     * @return
     */
    public static Date stringToDateAndMinS(String timeStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将时间转换为yyyy-MM-dd String 形式
     *
     * @param date
     * @return
     */
    public static String dateToMothString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        return sdf.format(date);
    }

    /**
     * 将时间转换为yyyy-MM-dd String 形式
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 将时间转换为yyyy-MM-dd String 形式
     *
     * @param date
     * @return
     */
    public static String dateToPartyTimeStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    /**
     * 时间格式为yyyy.MM.dd HH:mm 转换为date格式
     *
     * @param timeStr
     * @return
     */
    public static Date partyStringToDate(String timeStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取时间推移的时间字符串
     *
     * @param beginTime
     * @param timeofset
     * @return
     */
    public static String getPartyAfrerTime(String beginTime, int timeofset) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date begindate;
        String result = "1984-12-20 12:00:00";
        try {
            begindate = myFormatter.parse(beginTime);
            long time = begindate.getTime() + (timeofset * 1000);
            result = myFormatter.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * 获取本周起始时间
     *
     * @return
     */
    public static Date getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }


    /**
     * 通过生日反算年龄
     *
     * @param BirthDay //生日
     * @return
     */
    public static int getAgeFromBirthDay(String BirthDay) {
        //未设置年龄的用户生日计算S
        if (BirthDay.startsWith("0000")) {
            return 0;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式

        Date nowDate = new Date();
        Date birthDate;
        try {
            birthDate = df.parse(BirthDay);
            return (nowDate.getYear() - birthDate.getYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取跑步详情地图上的日期格式
     *
     * @return
     */
    public static String getHistoryMapStartTime(String startTime) {
        Date date = stringToDate(startTime);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日  HH:mm");
        return sdf.format(date);
    }

    public static String getFriendlyTimeV2(final String startTime, final long duration) {
        Date curDate = new Date();
        String nowTime = NowTime();
        long splitTime = curDate.getTime() - partyStringToDate(startTime).getTime() - duration;
        if (splitTime < ONE_HOUR) {
            return splitTime / ONE_MINUTE + "分钟前";
        } else if (splitTime < 6 * ONE_HOUR) {
            return splitTime / ONE_HOUR + "小时前";
        } else if (birthdayStrToDate(nowTime) == birthdayStrToDate(startTime)) {
            return "今天 " + startTime.substring(11, 16);
        } else if (birthdayStrToDate(nowTime).getTime() - birthdayStrToDate(startTime).getTime() == ONE_DAY) {
            return "昨天 " + startTime.substring(11, 16);
        } else {
            return startTime.substring(8, 10) + "日 " + startTime.substring(11, 16);
        }
    }

}
