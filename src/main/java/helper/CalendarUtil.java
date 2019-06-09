package helper;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.PersianCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public  class CalendarUtil {

    public static final String DEFAULT_PERSIAN_DATE_PATTERN = "yyyy/MM/dd";
    public static final String DEFAULT_PERSIAN_DATE_PATTERN_WITHOUT_TIME = "yyyy/MM/dd";
    public static final String DEFAULT_PERSIAN_TIME_ZONE = "Asia/Tehran";
    public static final String DEFAULT_PERSIAN_LOCALE = "fa_IR";

    public CalendarUtil() {
    }

    public static String toDateString(Date time, Locale locale) {
        DateFormat df;
        if (locale != null && locale.getLanguage().contains("fa")) {
            TimeZone timeZone = TimeZone.getTimeZone(DEFAULT_PERSIAN_TIME_ZONE);
            ULocale uLocale = ULocale.createCanonical(DEFAULT_PERSIAN_LOCALE);
            Calendar calendar = new PersianCalendar(timeZone, uLocale);
            df = calendar.getDateTimeFormat(DateFormat.FULL, DateFormat.FULL, uLocale);
            ((SimpleDateFormat) df).applyPattern(DEFAULT_PERSIAN_DATE_PATTERN);
        } else {
            df = Calendar.getInstance().getDateTimeFormat(DateFormat.FULL, DateFormat.FULL, locale);
        }
        return df.format(time);
    }

    public static String toDateString(Date time) {
        return toDateString(time, Locale.getDefault());
    }

    public static String toDateString(long time) {
        return toDateString(new Date(time));
    }

    public static Date dateStringToDate(String dateString) throws ParseException {
        return dateStringToDate(dateString, Locale.getDefault());
    }

    public static Date dateStringToDate(String dateString, Locale locale) throws ParseException {
        return dateStringToDate(dateString, DEFAULT_PERSIAN_DATE_PATTERN, locale);
    }

    public static Date dateStringToDate(String dateString, String dateFormat, Locale locale) throws ParseException {
        if (locale != null && locale.getLanguage().contains("fa")) {
            return persianDateStringToDate(dateString, dateFormat);
        } else {
            return commonDateStringToDate(dateString, dateFormat);
        }
    }

    public static Date commonDateStringToDate(String dateString, String dateFormat) throws ParseException {
        SimpleDateFormat sdf = (SimpleDateFormat) Calendar.getInstance().getDateTimeFormat(DateFormat.FULL, DateFormat.FULL, Locale.getDefault());
        sdf.applyPattern(dateFormat);
        return sdf.parse(dateString);
    }

    public static Date persianDateStringToDate(String persianDateString, String dateFormat) throws ParseException {
        TimeZone timeZone = TimeZone.getTimeZone(DEFAULT_PERSIAN_TIME_ZONE);
        ULocale uLocale = ULocale.createCanonical(DEFAULT_PERSIAN_LOCALE);
        Calendar calendar = new PersianCalendar(timeZone, uLocale);
        SimpleDateFormat sdf = (SimpleDateFormat) calendar.getDateTimeFormat(DateFormat.FULL, DateFormat.FULL, uLocale);
        sdf.applyPattern(dateFormat);
        return sdf.parse(persianDateString);
    }

    public static Date persianDateStringToDate(String persianDateString) throws ParseException {
        return persianDateStringToDate(persianDateString, DEFAULT_PERSIAN_DATE_PATTERN);
    }

    public static Date persianDateStringToDate(int year, int month, int day) throws ParseException {
        return persianDateStringToDate(year + "/" + month + "/" + day);
    }

    public static String currentPersianDate() {
        return toDateString(System.currentTimeMillis());
    }
}
