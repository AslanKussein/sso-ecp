package kz.crtr.util;

import kz.crtr.dto.LocalValue;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class BundleMessageUtil {
    private BundleMessageUtil() {
    }

    public static String getLocaledValue(LocalValue language, String key, String params) {
        return getMessage(key, new Locale(language.getLanguage()), params);
    }

    public static String getLocaledValue(LocalValue language, String key) {
        return getMessage(key, new Locale(language.getLanguage()));
    }

    public static String getMessage(String key, Locale locale, Object... params) {
        return MessageFormat.format(ResourceBundle.getBundle("messages", locale).getString(key), params);
    }
}
