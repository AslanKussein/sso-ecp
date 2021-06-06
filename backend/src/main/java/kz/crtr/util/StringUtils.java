package kz.crtr.util;

import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class StringUtils {

    public static String nullToEmpty(String value) {
        return isEmpty(value) ? "" : value;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty() || value.equals("");
    }

    public static List<String> split(String value, String delim) {
        List<String> list = new ArrayList<>();
        if (isNotEmpty(value)) {
            for (String s : value.split(delim)) {
                list.add(s.trim());
            }
        }
        return list;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String[] splitByEndOfLine(String value) {
        return splitByRegex(value, "\\r?\\n");
    }

    public static String[] splitByTab(String value) {
        return splitByRegex(value, "\t");
    }

    public static String[] splitByRegex(String value, String regex) {
        return value == null ? null : value.split(regex, -1);
    }

    public static List<Map<String, String>> dataToMap(String value, String regex) {
        String[] lines = StringUtils.splitByEndOfLine(value);

        String[] headers = StringUtils.splitByTab(lines[0]);

        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            String[] split = StringUtils.splitByRegex(lines[i], regex);
            Map<String, String> map = new HashMap<>();
            for (int j = 0; j < headers.length; j++) {
                map.put(headers[j], split[j]);
            }
            list.add(map);
        }
        return list;
    }

    public static String getString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String emptyToDash(String value) {
        return emptyToDefault(value, "-");
    }

    public static String emptyToDash(Object obj) {
        return emptyToDash(obj == null ? null : obj.toString());
    }

    public static String emptyToDefault(String value, String defaultValue) {
        return isNotEmpty(value) ? value : defaultValue;
    }

    public static String lowerCase(final String str, final Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase(locale);
    }

    public static String upperCase(final String str) {
        if (isEmpty(str)) {
            return null;
        }
        return str.toUpperCase();
    }

    public static String notEmptyOrThrow(String value, RuntimeException e) {
        if (isEmpty(value)) {
            throw e;
        }
        return value;
    }

    public static String notEmptyOrThrow(String value, Exception e) throws Exception {
        if (isEmpty(value)) {
            throw e;
        }
        return value;
    }

    public static String getFileName(HttpServletRequest request, String fileName, String encoded) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.toLowerCase().contains("firefox")) { // firefox
            // удаление лишних пробелом при кодировании названия файлов
            return fileName.equals(encoded) ? encoded : encoded.replace(" ", "");
        }
        return encoded;
    }

    public static String encodeUTF8(String text) throws UnsupportedEncodingException {
        if (isNotEmpty(text)) {
            return UriUtils.encode(text, "UTF-8");
        }
        return text;
    }

    public static String prepareQueryString(String text) {
        return Arrays
                .stream(
                        text.replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]", " ")
                                .trim()
                                .replaceAll("\\s{2,}", " ")
                                .split("[\\s]+")
                )
                .map(x -> x + "*")
                .collect(Collectors.joining(" "));
    }

    public static String toUtf8String(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * @desc oracle nvl analog
     */
    public static String nvl(String val1, String val2) {
        return isEmpty(val1) ? val2 : val1;
    }

    public static <X> X nvl(X val1, X val2) {
        return val1 == null ? val2 : val1;
    }

    public static String replaceSymbols(String text) {
        if (isNotEmpty(text)) {
            text = text.replace("[", "").replace("]", "");
            return text;
        }
        return "";
    }
}
