package com.AgriTest.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility class for date operations.
 */
public class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Format a LocalDate to a string.
     *
     * @param date the date to format
     * @return the formatted date string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Format a LocalDateTime to a string.
     *
     * @param dateTime the dateTime to format
     * @return the formatted dateTime string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }
    
    /**
     * Parse a string to a LocalDate.
     *
     * @param dateString the date string to parse
     * @return the parsed LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
    
    /**
     * Parse a string to a LocalDateTime.
     *
     * @param dateTimeString the dateTime string to parse
     * @return the parsed LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }
    
    /**
     * Convert a java.util.Date to LocalDate.
     *
     * @param date the java.util.Date
     * @return the LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    /**
     * Convert a LocalDate to java.util.Date.
     *
     * @param localDate the LocalDate
     * @return the java.util.Date
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * Calculate the number of days between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the number of days between startDate and endDate
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * Check if a date is within a date range.
     *
     * @param date the date to check
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return true if the date is within the range, false otherwise
     */
    public static boolean isDateWithinRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    /**
     * Get the current date.
     *
     * @return the current date
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * Get the current date and time.
     *
     * @return the current date and time
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}