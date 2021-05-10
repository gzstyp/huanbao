package com.fwtai.tool;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ToolDate{

    /** 比较日期,大于true,否则false */
    public static boolean isBeforeDate(final LocalDate start,final LocalDate end){
        return end.isBefore(start);
    }

    /** 比较时间,大于true,否则false */
    public static boolean isBeforeTime(final LocalDateTime start,final LocalDateTime end){
        return end.isBefore(start);
    }

    /** 比较日期,大于true,否则false */
    public static boolean isBeforeDate(final String start,final String end){
        return isBeforeDate(toDate(start),toDate(end));
    }

    /** 比较时间,大于true,否则false */
    public static boolean isBeforeTime(final String start,final String end){
        return isBeforeTime(toTime(start),toTime(end));
    }

    /**是否相等 */
    public static boolean isEqualDate(final LocalDate start,final LocalDate end){
        return start.isEqual(end);
    }

    /**是否相等*/
    public static boolean isEqualTime(final LocalDateTime start,final LocalDateTime end){
        return start.isEqual(end);
    }

    public static LocalDate toDate(final CharSequence text){
        return LocalDate.parse(text);
    }

    public static LocalDateTime toTime(final CharSequence text){
        return LocalDateTime.parse(text);
    }
}