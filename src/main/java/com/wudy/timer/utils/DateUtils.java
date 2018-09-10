package com.wudy.timer.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

public class DateUtils {
	/**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd
     */
	public static final int DEFAULT = 0;
	public static final int YM = 1;

    /**
     * 变量：日期格式化类型 - 格式:yyyy-MM-dd
     * 
     */
    public static final int YMR_SLASH = 11;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMMdd
     * 
     */
    public static final int NO_SLASH = 2;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMM
     * 
     */
    public static final int YM_NO_SLASH = 3;

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm:ss
     * 
     */
    public static final int DATE_TIME = 4;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMMddHHmmss
     * 
     */
    public static final int DATE_TIME_NO_SLASH = 5;

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm
     * 
     */
    public static final int DATE_HM = 6;

    /**
     * 变量：日期格式化类型 - 格式:HH:mm:ss
     * 
     */
    public static final int TIME = 7;

    /**
     * 变量：日期格式化类型 - 格式:HH:mm
     * 
     */
    public static final int HM = 8;
    
    /**
     * 变量：日期格式化类型 - 格式:HHmmss
     * 
     */
    public static final int LONG_TIME = 9;
    /**
     * 变量：日期格式化类型 - 格式:HHmm
     * 
     */
    
    public static final int SHORT_TIME = 10;

    /**
     * 变量：日期格式化类型 - 格式:yyyy-MM-dd HH:mm:ss
     */
    public static final int DATE_TIME_LINE = 12;
	public static String dateToStr(Date date, int type) {
        switch (type) {
        case DEFAULT:
            return dateToStr(date);
        case YM:
            return dateToStr(date, "yyyy/MM");
        case NO_SLASH:
            return dateToStr(date, "yyyyMMdd");
        case YMR_SLASH:
        	return dateToStr(date, "yyyy-MM-dd");
        case YM_NO_SLASH:
            return dateToStr(date, "yyyyMM");
        case DATE_TIME:
            return dateToStr(date, "yyyy/MM/dd HH:mm:ss");
        case DATE_TIME_NO_SLASH:
            return dateToStr(date, "yyyyMMddHHmmss");
        case DATE_HM:
            return dateToStr(date, "yyyy/MM/dd HH:mm");
        case TIME:
            return dateToStr(date, "HH:mm:ss");
        case HM:
            return dateToStr(date, "HH:mm");
        case LONG_TIME:
            return dateToStr(date, "HHmmss");
        case SHORT_TIME:
            return dateToStr(date, "HHmm");
        case DATE_TIME_LINE:
            return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
        default:
            throw new IllegalArgumentException("Type undefined : " + type);
        }
    }
	public static String dateToStr(Date date,String pattern) {
	       if (date == null || "".equals(date)) {
               return null;
           }
	       SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	       return formatter.format(date);
    } 

    public static String dateToStr(Date date) {
        return dateToStr(date, "yyyy/MM/dd");
    }

    public static Long getSurplusSeconds() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = LocalDateTime.of(now.getYear(),now.getMonth(),now.getDayOfMonth(),23,59,59);
        return Duration.between(now,endOfDay).getSeconds();
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate dateToLocalDate(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.toLocalDate();
    }

    public static Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 根据cron表达式计算时间间隔
     * @param cron
     * @return
     */
    public static Long seconds(String cron) {
        if (StringUtils.isBlank(cron)) {
            return -1L;
        }
        Date now = new Date();
        Date next = new CronSequenceGenerator(cron).next(now);
        Duration duration = Duration.between(dateToLocalDateTime(now),dateToLocalDateTime(next));
        return duration.plusSeconds(1).getSeconds();
    }

    public static Date strToDate(String source, String pattern) {
	    SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
