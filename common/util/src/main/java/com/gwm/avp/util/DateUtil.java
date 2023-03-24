package com.gwm.avp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateUtil {

    /**
     * mongo 日期查询isodate
     * @param dateStr
     * @return
     */
    public static Date dateToISODate(String dateStr){
        //T代表后面跟着时间，Z代表UTC统一时间
        Date date = formatD(dateStr);
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        String isoDate = format.format(date);
        try {
            return format.parse(isoDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /** 时间格式(yyyy-MM-dd HH:mm:ss) */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String DAT_DAY_PATTERN = "yyyyMMdd";
    public static final String DATE_TIME_PATTERN2 = "yyyyMMddHHmmss";


    public static Date formatD(String dateStr){
        return formatD(dateStr,DATE_TIME_PATTERN);
    }

    public static String formatD2Str(Date date){
        return format(new Date(),DATE_TIME_PATTERN2);
    }

    public static Date formatD(String dateStr ,String format)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date ret = null ;
        try {
            ret = simpleDateFormat.parse(dateStr) ;
        } catch (ParseException e) {
            //
        }
        return ret;
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static int compare(Date beginDate, Date endDate) {
        int compareTo = beginDate.compareTo(endDate);
        return compareTo;
    }

    /**
     * 两个日期相减得到的天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDiffMin(Date beginDate, Date endDate) {
        if(beginDate==null||endDate==null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        long diff=(endDate.getTime()-beginDate.getTime())/(1000*60);
        int mins = new Long(diff).intValue();
        return mins;
    }

    //获取系统当前时间Date类型，需要将字符串类型转成时间
    public static Date getDaDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
//设置为东八区
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date date = new Date();
        String dateStr = sdf.format(date);

//将字符串转成时间
        DateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
        Date newDate=null;
        try {
            newDate = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static Date longToDate(long time){
        SimpleDateFormat format =  new SimpleDateFormat(DATE_TIME_PATTERN);
        String d = format.format(time);
        Date date= null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long getPreHours(int hour){
        DateTimeFormatter fmTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime pro2hour = LocalDateTime.now().minus(hour, ChronoUnit.HOURS);
        System.out.println("1小时前:"+pro2hour.format(fmTime));
        return pro2hour.toEpochSecond(ZoneOffset.of("+8"));
    }

    public static String getDayByFormat(String timeFormat,String startDate,Integer day){
        SimpleDateFormat sdf=new SimpleDateFormat(timeFormat);
        Calendar calendar=new GregorianCalendar();
        Date date=null;
        try {
            date = new SimpleDateFormat(timeFormat).parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int diffDay=calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE,diffDay-day);
        String startTime=sdf.format(calendar.getTime());
        return  startTime;
    }

    public static String getSpecifiedDayBefore(String specifiedDay,int diffDay){
        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-diffDay);
        String dayBefore=new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        return dayBefore;
    }

    public static Date getSpecifiedHourBefore(Date diffDate,int diffHour){
        Calendar c = Calendar.getInstance();
        c.setTime(diffDate);
        int hour=c.get(Calendar.HOUR_OF_DAY);
        c.set(Calendar.HOUR_OF_DAY,hour-diffHour);
        return c.getTime();
    }

    /**
     * 相差多少天
     * @param smDate
     * @param bDate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smDate,String bDate) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smDate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bDate));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 相差多少月
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static int getMonthSpace(String date1, String date2)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int result=0;
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(sdf.parse(date1));
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(sdf.parse(date2));
        result =(cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12 + cal1.get(Calendar.MONTH)- cal2.get(Calendar.MONTH);
        return result==0?1 : Math.abs(result);
    }

    // 获得某天最大时间 2020-10-09 23:59:59
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirstDay(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    // 获得某天最小时间 2020-10-09 00:00:00
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static void main(String[] args) {
//        System.out.println(getDayByFormat("yyyyMMdd","20220325",7));
        System.out.println(format(getCurrYearLast(),"yyyy-MM-dd"));
        System.out.println(format(getCurrYearFirstDay(),"yyyy-MM-dd"));
    }
}
