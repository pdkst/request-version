package io.github.pdkst.request.versions;

import lombok.experimental.UtilityClass;

import java.util.StringJoiner;

/**
 * @author pdkst
 * @since 2022/4/11
 */
@UtilityClass
class StringUtils {
    public static boolean isBlank(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    public static String joinWith(String delimiter, String... strArr) {
        if (strArr == null || strArr.length == 0) {
            return null;
        }
        final StringJoiner joiner = new StringJoiner(delimiter);
        for (String str : strArr) {
            if (isNotBlank(str)) {
                joiner.add(str);
            }
        }
        return joiner.toString();
    }
}
