package soya.framework.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class DateTimeUtils {
    private static final long ONE_DAY_IN_MILLIS = 1000l * 60 * 60 * 24;

    private DateTimeUtils() {
    }

    public static Date current() {
        return new Date();
    }

    // Cron Scheduler Parsing:

    // Format
    public static String toString(Date date, String format) {
        Objects.requireNonNull(date);
        Objects.requireNonNull(format);

        return new SimpleDateFormat(format).format(date);
    }

    public static Date toDate(String str, String format) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(format);

        try {
            return new SimpleDateFormat(format).parse(str);

        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // Calculate
    public static Date getFutureTime(Date base, long time, TimeUnit unit) {
        return new Date(base.getTime() + unit.toMillis(time));
    }

    public static Date getFutureTime(Date base, Duration duration) {
        return new Date(base.getTime() + duration.toMillis());
    }

    public static Date getFutureTime(Date base, Period period) {
        return new Date(base.getTime() + ONE_DAY_IN_MILLIS * period.getDays());
    }

    public static Date getPastTime(Date base, long time, TimeUnit unit) {
        return new Date(base.getTime() - unit.toMillis(time));
    }

    public static Date getPastTime(Date base, Duration duration) {
        return new Date(base.getTime() - duration.toMillis());
    }

    public static Date getPastTime(Date base, Period period) {
        return new Date(base.getTime() - ONE_DAY_IN_MILLIS * period.getDays());
    }

    // Calendar.Builder
    public static Calendar.Builder calendarBuilder() {
        return new Calendar.Builder();
    }

    public static Calendar.Builder calendarBuilder(Date date) {
        return new Calendar.Builder().setInstant(date);
    }

}
