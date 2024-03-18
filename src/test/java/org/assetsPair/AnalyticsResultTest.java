package org.assetsPair;

import org.junit.Test;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import static org.junit.Assert.assertEquals;

public class AnalyticsResultTest {

    @Test
    public void testFormatDate() {
        long timestamp = 1079924400; // Mon Mar 22 2004 03:00:00 GMT+0000
        AnalyticsResult result = new AnalyticsResult(0.0, timestamp, 0, false);

        // Convert UTC time to local time
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("UTC"));
        // Use the DateTimeFormatter from the default zone to display the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String expected = dateTime.format(formatter);

        assertEquals(expected, result.formatDate(timestamp));
    }
}
