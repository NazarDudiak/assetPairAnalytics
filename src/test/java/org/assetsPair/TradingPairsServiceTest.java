package org.assetsPair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.Assert.assertEquals;

public class TradingPairsServiceTest {
    private JsonArray jsonArray;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        // Створюємо тестовий набір даних
        jsonArray = new JsonArray();
        JsonObject object1 = new JsonObject();
        object1.addProperty("assetPair", "USDT/PLN");
        object1.addProperty("value", 20.0);
        object1.addProperty("timestamp", 1079906400); // Sun Mar 21 2004 22:00:00 GMT+0000
        jsonArray.add(object1);

        JsonObject object2 = new JsonObject();
        object2.addProperty("assetPair", "USDT/PLN");
        object2.addProperty("value", 20.0);
        object2.addProperty("timestamp", 1079906400); // Sun Mar 21 2004 22:00:00 GMT+0000
        jsonArray.add(object2);

        JsonObject object3 = new JsonObject();
        object3.addProperty("assetPair", "USDT/PLN");
        object3.addProperty("value", 10.0);
        object3.addProperty("timestamp", 1079924400); // Mon Mar 22 2004 03:00:00 GMT+0000
        jsonArray.add(object3);

        JsonObject object4 = new JsonObject();
        object4.addProperty("assetPair", "USDT/PLN");
        object4.addProperty("value", 15.0);
        object4.addProperty("timestamp", 1084914000); // Tue May 18 2004 21:00:00 GMT+0000
        jsonArray.add(object4);
    }

    @After
    public void tearDown() {
        jsonArray = null;
    }

    @Test
    public void testDisplayAnalytics() {
        // Перенаправляємо System.out у наш потік перехоплення виводу
        System.setOut(new PrintStream(outputStreamCaptor));

        TradingPairsService service = new TradingPairsService(jsonArray);
        service.displayAnalytics("USDT/PLN");

        // Expected results
        double expectedAveragePrice = 16.25;
        LocalDateTime expectedFirstTimestamp = LocalDateTime.of(2004, 3, 21, 22, 0, 0);
        LocalDateTime expectedLastTimestamp = LocalDateTime.of(2004, 5, 18, 21, 0, 0);
        boolean expectedHasDuplicates = true;

        // Format the expected dates and times using the formatDate() method
        String formattedFirstTimestamp = formatDate(expectedFirstTimestamp);
        String formattedLastTimestamp = formatDate(expectedLastTimestamp);

        // Expected output
        String expectedOutput = "Average price: %s\nThe first timestamp seen is: %s\nTimestamp of last observation: %s\nPresence of duplicates: %s".formatted(expectedAveragePrice, formattedFirstTimestamp, formattedLastTimestamp, expectedHasDuplicates);

        // Actual output
        String actualOutput = outputStreamCaptor.toString().trim();

        // Check that the actual output is equal to the expected one
        assertEquals(expectedOutput, actualOutput);
    }

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }
}
