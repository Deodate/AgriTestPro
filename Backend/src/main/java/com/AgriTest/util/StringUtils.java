// File: src/main/java/com/AgriTest/util/StringUtils.java
package com.AgriTest.util;

import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class for string operations.
 */
public class StringUtils {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    
    /**
     * Check if a string is null or empty.
     *
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if a string is not null and not empty.
     *
     * @param str the string to check
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Convert a string to a slug (URL-friendly string).
     *
     * @param input the string to convert
     * @return the slug
     */
    public static String toSlug(String input) {
        if (input == null) {
            return "";
        }
        
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String noWhitespace = WHITESPACE.matcher(normalized).replaceAll("-");
        String slug = NON_LATIN.matcher(noWhitespace).replaceAll("");
        
        return slug.toLowerCase();
    }
    
    /**
     * Truncate a string to the specified length.
     *
     * @param str the string to truncate
     * @param maxLength the maximum length
     * @return the truncated string
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        
        if (str.length() <= maxLength) {
            return str;
        }
        
        return str.substring(0, maxLength) + "...";
    }
    
    /**
     * Generate a random string.
     *
     * @return a random string
     */
    public static String randomString() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Capitalize the first letter of each word in a string.
     *
     * @param str the string to capitalize
     * @return the capitalized string
     */
    public static String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        
        StringBuilder result = new StringBuilder();
        String[] words = str.split("\\s");
        
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
    
    /**
     * Remove all whitespace from a string.
     *
     * @param str the string to process
     * @return the string without whitespace
     */
    public static String removeWhitespace(String str) {
        if (str == null) {
            return null;
        }
        
        return str.replaceAll("\\s", "");
    }
    
    /**
     * Check if a string contains only alphanumeric characters.
     *
     * @param str the string to check
     * @return true if the string contains only alphanumeric characters, false otherwise
     */
    public static boolean isAlphanumeric(String str) {
        if (str == null) {
            return false;
        }
        
        return str.matches("^[a-zA-Z0-9]+$");
    }
}