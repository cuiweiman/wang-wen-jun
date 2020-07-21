package com.wang.java8.dateapi;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;

/**
 * @description: LocalDate API 常用API介绍
 * @author: wei·man cui
 * @date: 2020/7/2 13:22
 */
public class LocalDateApi {
    public static void main(String[] args) throws InterruptedException {
        // testLocalDate();
        // testLocalTime();
        // testLocalDateTime();
        // testInstant();
        // testDuration();
        // testPeriod();
        // testDateFormat();
        testParse();
    }

    private static void testDateFormat() {
        LocalDate date = LocalDate.now();
        System.out.println(date.format(DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE));

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(date.format(df));
    }

    private static void testParse() {
        String date1 = "20200702";
        LocalDate parse = LocalDate.parse(date1, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(parse);

        String date2 = "2020_07_02";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        System.out.println(LocalDate.parse(date2, df));
    }


    private static void testPeriod() {
        Period between = Period.between(LocalDate.of(2020, 1, 10), LocalDate.of(2021, 3, 11));
        System.out.println(between.getYears());
        System.out.println(between.getMonths());
        System.out.println(between.getDays());
    }


    private static void testDuration() {
        LocalTime start = LocalTime.now();
        LocalTime before = start.minusHours(1);
        System.out.println(Duration.between(before, start).toHours());
    }


    /**
     * 最精准的时间点
     */
    private static void testInstant() throws InterruptedException {
        Instant start = Instant.now();
        Thread.sleep(1000);
        Instant end = Instant.now();
        Duration between = Duration.between(start, end);
        System.out.println(between.toMillis());
    }

    private static void testLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(dateTime);
    }

    private static void testLocalTime() {
        System.out.println(LocalTime.of(13, 30, 15));
        LocalTime time = LocalTime.now();
        System.out.println(time.getSecond());
        System.out.println(time.getNano());
        System.out.println(time.isBefore(LocalTime.MAX));
    }

    private static void testLocalDate() {
        System.out.println(LocalDate.now());

        LocalDate localDate = LocalDate.of(2020, 7, 2);
        System.out.println(localDate);
        System.out.println("年：" + localDate.getYear() + "；月份：" + localDate.getMonth() + "；月份：" + localDate.getMonthValue());
        System.out.println("一年的第多少天：" + localDate.getDayOfYear() + "；一个月的第多少天：" + localDate.getDayOfMonth() + "；星期：" + localDate.getDayOfWeek());

        System.out.println("【ChronoField】" + localDate.get(ChronoField.DAY_OF_YEAR));
    }

}
