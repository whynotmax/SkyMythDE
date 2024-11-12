package de.skymyth.utility;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeUtil {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.GERMAN);

    public @NonNull String formatToDate(long timeAsMilliseconds) {
        return simpleDateFormat.format(timeAsMilliseconds);
    }

    public @NonNull String beautifyTime(long time, @NonNull TimeUnit timeUnit) {
        return beautifyTime(time, timeUnit, true, true);
    }

    public @NonNull String beautifyTime(long time, @NonNull TimeUnit timeUnit, boolean useShortStrings) {
        return beautifyTime(time, timeUnit, useShortStrings, true);
    }

    public @NonNull String beautifyTime(long time, @NonNull TimeUnit timeUnit, boolean useShortStrings, boolean showSeconds) {
        String msg = "";
        long inSeconds = TimeUnit.SECONDS.convert(time, timeUnit);
        if (inSeconds >= 86400L) {
            long days = inSeconds / 86400L;
            msg = msg + days + (useShortStrings ? "d " : (days == 1L ? " day, " : " days, "));
            inSeconds %= 86400L;
        }
        if (inSeconds >= 3600L) {
            long hours = inSeconds / 3600L;
            msg = msg + hours + (useShortStrings ? "h " : (hours == 1L ? " hour, " : " hours, "));
            inSeconds %= 3600L;
        }
        if (inSeconds >= 60L) {
            long minutes = inSeconds / 60L;
            msg = msg + minutes + (useShortStrings ? "m " : (minutes == 1L ? " minute, " : " minutes, "));
            inSeconds %= 60L;
        }
        if (showSeconds) {
            if (inSeconds > 0L)
                msg = msg + inSeconds + (useShortStrings ? "s " : (inSeconds == 1L ? " second" : " seconds"));
        }
        if (!msg.isEmpty())
            msg = msg.substring(0, msg.length() - (useShortStrings ? 1 : 0));
        else
            msg = useShortStrings ? "0s" : "0 seconds";

        if (time == -1)
            msg = "permanent";
        return msg;
    }

    public long parseTimeFromString(@NonNull String timeString) {
        long multiplier = 0;
        long timeLong = Long.parseLong(timeString.substring(0, timeString.length() - 1));
        multiplier = timeString.endsWith("y") ? (long) 1000 * 60 * 60 * 24 * 7 * 4 * 12L : multiplier;
        multiplier = timeString.endsWith("w") ? (long) 1000 * 60 * 60 * 24 * 7 : multiplier;
        multiplier = timeString.endsWith("d") ? (long) 1000 * 60 * 60 * 24 : multiplier;
        multiplier = timeString.endsWith("h") ? (long) 1000 * 60 * 60 : multiplier;
        multiplier = timeString.endsWith("m") ? (long) 1000 * 60 : multiplier;
        multiplier = timeString.endsWith("s") ? (long) 1000 : multiplier;
        return timeLong * multiplier;
    }

}