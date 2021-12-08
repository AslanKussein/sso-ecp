package kz.mtszn.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

public class DateUtils {

    private static final SimpleDateFormat DATE_MARKER = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATE_TIME_MARKER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_TIME_WITHOUT_SECONDS_MARKER = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static final SimpleDateFormat HUMAN_MONTH_YEAR = new SimpleDateFormat("MM.yyyy");
    private static final SimpleDateFormat DATE_TIME_WITH_MSEC_MARKER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter HUMAN_LOCAL_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final DateTimeFormatter DTF_APLAT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateTimeFormatter DTF_APLAT_DATETIM2E = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DTF_FISCAL_RECEIPT_DATETIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public static final DateTimeFormatter DTF_HUMAN = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DTF_HUMAN_DATETIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter DTF_HUMAN_DATETIME_WITHOUT_SECONDS = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private static final DateTimeFormatter DTF_STANDARD = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final DateTimeFormatter DTF_HUMAN_SHORT_YEAR = DateTimeFormatter.ofPattern("dd:MM:yy");
    public static final DateTimeFormatter DTF_KUZ_DATETIME = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    public static final long MILLIS_IN_SECOND = 1000;
    public static final long SECONDS_IN_MINUTE = 60;
    public static final long MINUTES_IN_HOUR = 60;

    public static final long MILLIS_IN_MINUTE = SECONDS_IN_MINUTE * MILLIS_IN_SECOND;
    public static final long MILLIS_IN_HOUR = MINUTES_IN_HOUR * MILLIS_IN_MINUTE;
    public static final long SECONDS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE;

    public static LocalDate parseHumanLocalDate(String value) {
        if (StringUtils.isEmpty(value)) return null;
        return LocalDate.parse(value, HUMAN_LOCAL_DATE);
    }

    //форматирование даты в строку 2013-11-15T10:08:02.397+06:00
    public static String formatAplatDateTime(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        return zonedDateTime.format(DTF_APLAT_DATETIM2E);
    }

    public static String formatHumanLocalDate(LocalDate localDate) {
        if (localDate == null) return null;
        return HUMAN_LOCAL_DATE.format(localDate);
    }

    public static String formatHumanDate(Date date) {
        if (date == null) return null;
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }


    public static String formatHumanDateTimeWithoutTime(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        return zonedDateTime.format(HUMAN_LOCAL_DATE);
    }

    public static ZonedDateTime parseAplatDateTime(String value) {
        if (StringUtils.isEmpty(value)) return null;
        return ZonedDateTime.parse(value, DTF_APLAT_DATETIME);
    }

    public static ZonedDateTime parseDtfHuman(String value) {
        if (StringUtils.isEmpty(value)) return null;
        return ZonedDateTime.parse(value, DTF_HUMAN);
    }

    public static String formatFiscalReceiptDateTime(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        return zonedDateTime.format(DTF_FISCAL_RECEIPT_DATETIME);
    }

    public static String formatHuman(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        return zonedDateTime.format(DTF_HUMAN);
    }

    public static String formatHumanDateTime(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        return zonedDateTime.format(DTF_HUMAN_DATETIME);
    }

    public static String formatHumanDateTime(ZonedDateTime zonedDateTime, String format) {
        if (zonedDateTime == null) return null;
        return zonedDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * @return dd.MM.yyyy HH:mm
     */
    public static String formatHumanDateTimeWithoutSeconds(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        return zonedDateTime.format(DTF_HUMAN_DATETIME_WITHOUT_SECONDS);
    }

    public static ZonedDateTime parseStandardDateTime(String value) {
        return StringUtils.isNotEmpty(value) ? ZonedDateTime.parse(value, DTF_STANDARD.withZone(ZoneId.systemDefault())) : null;
    }

    /**
     * Разница между датами в миллисекундах
     *
     * @param date1 первая дата
     * @param date2 вторая дата
     * @return кол-во миллисекунд
     * Пример diffInMillis('2017-08-02 12:47:00','2017-08-02 12:47:01') вернет 1000 миллисекунд
     * Пример diffInMillis('2017-08-02 12:47:00','2017-08-02 12:46:59') вернет -1000 миллисекунд
     */
    public static long diffInMillis(Date date1, Date date2) {
        return date2.getTime() - date1.getTime();
    }

    /**
     * Разница между датами в секундах
     *
     * @param date1 первая дата
     * @param date2 вторая дата
     * @return кол-во секунд
     */
    public static long diffInSeconds(Date date1, Date date2) {
        return TimeUnit.MILLISECONDS.toSeconds(diffInMillis(date1, date2));
    }

    /**
     * Разница между датами в днях
     *
     * @param date1 первая дата
     * @param date2 вторая дата
     * @return кол-во дней
     */
    public static int diffInDays(Date date1, Date date2) {
        return (int) TimeUnit.MILLISECONDS.toDays(diffInMillis(date1, date2));
    }

    /**
     * Разница между датами в днях
     *
     * @param date1 первая дата
     * @param date2 вторая дата
     * @return кол-во дней
     */
    public static int diffInDays(ZonedDateTime date1, ZonedDateTime date2) {
        return diffInDays(Date.from(date1.toInstant()), Date.from(date2.toInstant()));
    }

    /**
     * @return value from 1-12
     */
    public static int getCurrentMonth() {
        return getMonth(new Date());
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * @return
     */
    public static int getCurrentYear() {
        return getYear(new Date());
    }

    /**
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    /**
     * Вернуть следующее для удобное для пользователя время.
     * Удобный считается время с 08:00 до 22:00
     *
     * @param date время
     * @return следующее удобное время
     */
    public static ZonedDateTime getNextUserFriendlyTime(ZonedDateTime date) {
        if (date.getHour() >= 8 && date.getHour() <= 22) {
            return date;
        }
        ZonedDateTime nextDate = date.withHour(8).withMinute(0).withSecond(0);
        if (nextDate.isBefore(date)) {
            nextDate = nextDate.plusDays(1);
        }
        return nextDate;
    }

    public static ZonedDateTime toZonedDateTime(Date date) {
        return toZonedDateTime(date, ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(Date date, ZoneId zoneId) {
        return date != null
                ? ZonedDateTime.ofInstant(date.toInstant(), zoneId)
                : null;
    }

    public static ZonedDateTime toZonedDateTime(Instant instant) {
        return instant != null
                ? ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
                : null;
    }

    public static Date localDateToDate(LocalDate localDate) {
        return localDateToDate(localDate, ZoneId.systemDefault());
    }

    public static Date localDateToDate(LocalDate localDate, ZoneId zoneId) {
        return localDate != null
                ? Date.from(localDate.atStartOfDay(zoneId).toInstant())
                : null;
    }

    public static Date stringToDate(String date, DateTimeFormatter formatter) {
        if (date != null && !date.isEmpty()) {
            return Date.from(LocalDate.parse(date, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    public static String dateToString(Date date, DateTimeFormatter formatter) {
        if (date != null) {
            return asLocalDateTime(date).format(formatter);
        }
        return "";
    }

    private static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * @desc превращаем дату в строку и делаем аналог trunc(month);
     */
    public static Date fillCutDate(String cutDate) {
        if (cutDate.length() == 7) {
            return stringToDate("01.".concat(cutDate), DTF_HUMAN);
        }
        return null;
    }

    /**
     * @desc превращаем дату в строку и делаем аналог trunc(month);
     */
    public static LocalDate fillCutLocalDate(String cutDate) {
        if (cutDate.length() == 7) {
            return LocalDate.parse("01.".concat(cutDate), DTF_HUMAN);
        }
        return null;
    }

    public static Calendar getCalendarDateInstance(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * @desc получаем текущую дату аналог trunc(month);
     */
    public static Date getCurrentDate() {
        Calendar calendar = getCalendarDateInstance(stringToDate(dateToString(new Date(), DTF_HUMAN), DTF_HUMAN));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static String getDateStringShortYear(ZonedDateTime zonedDateTime) {
        return DTF_HUMAN_SHORT_YEAR.format(zonedDateTime);
    }

    public static String formatKuzDateTime(Instant instant) {
        if (instant == null) return null;
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DTF_KUZ_DATETIME);
    }

    public static Instant parseKuzDateTime(String value) {
        return StringUtils.isNotEmpty(value) ? ZonedDateTime.parse(value, DTF_KUZ_DATETIME.withZone(ZoneId.systemDefault())).toInstant() : null;
    }

    public static ZonedDateTime getWithEndOfDate(ZonedDateTime date) {
        return date.withHour(23).withMinute(59);
    }

    public static java.sql.Date convertToSqlDate(ZonedDateTime date) {
        if (isNull(date)) {
            return null;
        }
        return new java.sql.Date(Date.from(date.withZoneSameInstant(ZoneId.of("Asia/Almaty")).toInstant()).getTime());
    }

    public static java.sql.Date convertToSqlDate(final LocalDateTime localDate) {
        if (isNull(localDate)) {
            return null;
        }
        return new java.sql.Date(Date.from(localDate.atZone(ZoneId.of("Asia/Almaty")).toInstant()).getTime());
    }

    public static java.sql.Date convertToSqlDate(Date date) {
        if (isNull(date)) {
            return null;
        }
        return new java.sql.Date(Date.from(date.toInstant()).getTime());
    }

    public static java.sql.Date convertToSqlDate(String date) {
        if (isNull(date)) {
            return null;
        }
        return new java.sql.Date(stringToDate(date, DTF_HUMAN).getTime());
    }

    public static ZonedDateTime getFirstDayZone(ZonedDateTime zonedDateTime) {
        zonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Almaty"));
        return ZonedDateTime.of(LocalDate.of(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.with(TemporalAdjusters.firstDayOfMonth()).getDayOfMonth()), LocalTime.of(0, 0, 0), ZoneId.of("Asia/Almaty"));
    }

    public static ZonedDateTime getLastDayZone(ZonedDateTime zonedDateTime) {
        zonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Almaty"));
        return ZonedDateTime.of(LocalDate.of(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()), LocalTime.of(23, 59, 59), ZoneId.of("Asia/Almaty"));
    }
}
