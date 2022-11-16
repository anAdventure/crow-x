package com.crowx.common.utils;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * LocalDateTime和Date相互转换
 */
public class LocalDateUtils {

    /**
     * ISO 标准
     */
    public static final WeekFields WEEK_FIELDS = WeekFields.ISO;

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 年周转LocalDate
     *
     * @param yearWeek 202103 2021年 第三周
     * @return
     */
    public static LocalDate yearWeekToLocalDate(Integer yearWeek) {

        return yearWeekToLocalDate(yearWeek, 1);
    }

    /**
     * 年周转LocalDate
     *
     * @param yearWeek  202103 2021年 第三周
     * @param dayOfWeek 1 -> 7 之间
     * @return
     */
    public static LocalDate yearWeekToLocalDate(Integer yearWeek, Integer dayOfWeek) {

        int year = yearWeek / 100;
        int week = yearWeek % 100;
        //输入你想要的年份和周数
        LocalDate localDate = LocalDate.now()
                .withYear(year)
                .with(WEEK_FIELDS.weekOfYear(), week);
        //周dayOfWeek
        return localDate.with(WEEK_FIELDS.dayOfWeek(), dayOfWeek);
    }

    public static Integer asWeekInt(Date date) {
        return asWeekInt(asLocalDate(date));
    }

    /**
     * LocalDate -> 年周（202103）
     *
     * @param localDate
     * @return
     */
    public static Integer asWeekInt(LocalDate localDate) {
        return localDate.get(WEEK_FIELDS.weekBasedYear()) * 100
                + localDate.get(WEEK_FIELDS.weekOfWeekBasedYear());
    }

    public static List<Integer> generateWeekList(Date beginDate, Date endDate) {
        return generateWeekList(asLocalDate(beginDate), asLocalDate(endDate));
    }

    /**
     * 时间段内的年周集合
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<Integer> generateWeekList(LocalDate beginDate, LocalDate endDate) {
        Set<Integer> dateRange = new HashSet<>();
        for (LocalDate tempDate = beginDate; !tempDate.isAfter(endDate); tempDate = tempDate.plusDays(1L)) {
            dateRange.add(asWeekInt(tempDate));
        }
        return dateRange.stream()
                .sorted(Comparator.comparing(Integer::intValue))
                .collect(Collectors.toList());
    }

    /**
     * 获取时间段每天的日期（包含首尾）
     *
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return list
     */
    public static List<LocalDate> generateLocalDateList(LocalDate beginDate, LocalDate endDate) {
        Set<LocalDate> dateRage = new HashSet<>();
        for (LocalDate i = beginDate; !i.isAfter(endDate); i = i.plusDays(1L)) {
            dateRage.add(i);
        }
        return dateRage.stream().sorted(Comparator.comparing(Function.identity())).collect(Collectors.toList());
    }

    /**
     * 判读是否在时间段内
     *
     * @param inDate
     * @param startDate
     * @param enDate
     * @return
     */
    public static Boolean isIn(LocalDate inDate, Date startDate, Date enDate) {

        LocalDate begin = LocalDateUtils.asLocalDate(startDate);
        LocalDate end = LocalDateUtils.asLocalDate(enDate);
        if (inDate.isAfter(begin) && inDate.isBefore(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static Integer getDateKeyWithSeason(LocalDate localDate) {
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int season = 0;
        switch (month) {
            case 1:
            case 2:
            case 3:
                season = 1;
                break;
            case 4:
            case 5:
            case 6:
                season = 2;
                break;
            case 7:
            case 8:
            case 9:
                season = 3;
                break;
            case 10:
            case 11:
            case 12:
                season = 4;
                break;
            default:
                break;
        }
        return year * 100 +season;
    }

}
