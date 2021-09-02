package com.tunjicus.bank.scheduled;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

public class TimeService {
    private static final BigDecimal simulatedMinutesPerDay = BigDecimal.valueOf(60);
    private static final BigDecimal simulatedSecondsPerDay =
            simulatedMinutesPerDay.multiply(BigDecimal.valueOf(60));
    private static final BigDecimal secondsPerDay = BigDecimal.valueOf(60 * 60 * 24);
    private static final BigDecimal ratio =
            secondsPerDay.divide(simulatedSecondsPerDay, 10, RoundingMode.HALF_EVEN);

    public static Date calculateSimulatedDate(Date utcDate) {
        if (utcDate == null) return null;

        var currentDate = new Date();

        // Get the offset of the machine's local time from utc
        var offset = TimeZone.getDefault().getOffset(currentDate.getTime());
        var localDate =
                LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(currentDate.getTime()), ZoneId.of("UTC"));

        var milliDifference = Math.abs(currentDate.getTime() - (utcDate.getTime() + offset));
        var secondDifference =
                BigDecimal.valueOf(milliDifference)
                        .divide(BigDecimal.valueOf(1000), 10, RoundingMode.HALF_EVEN);

        var adjustedDifference = secondDifference.multiply(ratio);
        var duration = Duration.ofSeconds(adjustedDifference.longValue());

        if (utcDate.after(currentDate)) {
            return Date.from(localDate.plus(duration).toInstant(ZoneOffset.UTC));
        }

        return Date.from(localDate.minus(duration).toInstant(ZoneOffset.UTC));
    }
}
