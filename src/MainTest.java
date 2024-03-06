import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class MainTest {
    private JSONArray jsonArray;

    @Test
    public void testFormatDate_ValidTimestamp() {
        // Test the formatDate method with a valid timestamp value.
        long timestamp = 1079931600;
        String expected = "22/03/2004 05:00:00"; // GMT Mon Mar 22 2004 05:00:00 GMT+0000
        String actual = Main.formatDate(timestamp);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDate_ZeroTimestamp() {
        // Test the formatDate method with a zero timestamp value.
        long timestamp = 0;
        String expected = "01/01/1970 00:00:00"; // GMT Thu Jan 01 1970 00:00:00 GMT+0000
        String actual = Main.formatDate(timestamp);

        assertEquals(expected, actual);
    }

    @Before
    public void setUp() {
        // Test JSONArray with data to analyze
        jsonArray = new JSONArray();
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.90).put("timestamp", 1615078800));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.91).put("timestamp", 1615078801));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.92).put("timestamp", 1615078802));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.93).put("timestamp", 1615078803));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.94).put("timestamp", 1615078804));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.95).put("timestamp", 1615078805));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.96).put("timestamp", 1615078906));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.97).put("timestamp", 1615078907));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.98).put("timestamp", 1615078908));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 3.99).put("timestamp", 1615078909));
        jsonArray.put(new JSONObject().put("assetPair", "USDT/PLN").put("value", 4.00).put("timestamp", 1615078910));
    }

    @Test
    public void testDisplayAnalytics() {
        // Prepare test data for input simulation
        String assetPair = "USDT/PLN";

        // Expected results
        double expectedAveragePrice = 3.95;
        String expectedFirstTimestamp = "07/03/2021 01:00:00";
        String expectedLastTimestamp = "07/03/2021 01:01:50";
        boolean expectedDuplicates = false;

        // Call the method for testing
        String result = Main.displayAnalytics(assetPair, jsonArray);

        // Check the results
        assertEquals(expectedAveragePrice, parseAveragePrice(result), 0.001);
        assertEquals(expectedFirstTimestamp, parseFirstTimestamp(result));
        assertEquals(expectedLastTimestamp, parseLastTimestamp(result));
        assertEquals(expectedDuplicates, parseDuplicates(result));

    }

    // Parse the result string to extract the average price value
    private double parseAveragePrice(String result) {
        String[] lines = result.split("\n");
        String averagePriceLine = lines[0].substring("Average price: ".length());
        return Double.parseDouble(averagePriceLine);
    }

    // Parse the result string to extract the value of the first observed timestamp
    private String parseFirstTimestamp(String result) {
        String[] lines = result.split("\n");
        return lines[1].substring("The first timestamp seen is: ".length());
    }

    // Parse the result string to extract the timestamp value of the last observation
    private String parseLastTimestamp(String result) {
        String[] lines = result.split("\n");
        return lines[2].substring("Timestamp of last observation: ".length());
    }

    // Parse the result string to extract the value of the presence of duplicates
    private boolean parseDuplicates(String result) {
        String[] lines = result.split("\n");
        String duplicatesLine = lines[3].substring("The presence of duplicates: ".length());
        return Boolean.parseBoolean(duplicatesLine);
    }
}
