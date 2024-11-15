package de.skymyth.utility;

import lombok.experimental.UtilityClass;

/**
 * Utility class for string-related operations.
 */
@UtilityClass
public class StringUtil {

    /**
     * Generates a random string of the specified length.
     * The string contains uppercase and lowercase letters and digits.
     *
     * @param length the length of the random string to generate
     * @return a random string of the specified length
     */
    public String getRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(characters.charAt((int) (Math.random() * characters.length())));
        }
        return stringBuilder.toString();
    }

    /**
     * Splits a string in the middle and inserts a separator.
     * @param s the string to split
     * @param separator the separator to insert
     * @return  the string with the separator inserted in the middle
     */
    public String splitStringInMiddle(String s, String separator) {
        int length = s.length();
        int middle = length / 2;
        return s.substring(0, middle) + separator + s.substring(middle, length);
    }

}