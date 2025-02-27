// File: src/main/java/com/AgriTest/util/ValidationUtils.java
package com.AgriTest.util;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations.
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    
    /**
     * Validate if the given email address is valid.
     *
     * @param email the email to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate if the given phone number is valid.
     *
     * @param phone the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validate if the given string is not null and not empty.
     *
     * @param str the string to validate
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate if the given string is null or empty.
     *
     * @param str the string to validate
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Validate if the given object is not null.
     *
     * @param obj the object to validate
     * @return true if the object is not null, false otherwise
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }
    
    /**
     * Validate if the given number is positive.
     *
     * @param number the number to validate
     * @return true if the number is positive, false otherwise
     */
    public static boolean isPositive(Number number) {
        if (number == null) {
            return false;
        }
        return number.doubleValue() > 0;
    }
    
    /**
     * Validate if the given number is non-negative.
     *
     * @param number the number to validate
     * @return true if the number is non-negative, false otherwise
     */
    public static boolean isNonNegative(Number number) {
        if (number == null) {
            return false;
        }
        return number.doubleValue() >= 0;
    }
    
    /**
     * Validate if the given value is within the specified range.
     *
     * @param value the value to validate
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return true if the value is within the range, false otherwise
     */
    public static boolean isInRange(Number value, Number min, Number max) {
        if (value == null || min == null || max == null) {
            return false;
        }
        double doubleValue = value.doubleValue();
        return doubleValue >= min.doubleValue() && doubleValue <= max.doubleValue();
    }
}