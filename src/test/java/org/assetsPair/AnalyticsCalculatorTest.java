package org.assetsPair;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import static org.junit.Assert.*;

public class AnalyticsCalculatorTest {
    private JsonArray jsonArray;

    @Before
    public void setUp() {
        // Створюємо тестовий набір даних
        jsonArray = new JsonArray();
        JsonObject object1 = new JsonObject();
        object1.addProperty("assetPair", "USDT/PLN");
        object1.addProperty("value", 10.0);
        object1.addProperty("timestamp", 1079906400); // Sun Mar 21 2004 22:00:00 GMT+0000
        jsonArray.add(object1);

        JsonObject object2 = new JsonObject();
        object2.addProperty("assetPair", "USDT/PLN");
        object2.addProperty("value", 20.0);
        object2.addProperty("timestamp", 1084914000); // Tue May 18 2004 21:00:00 GMT+0000
        jsonArray.add(object2);
    }

    @After
    public void tearDown() {
        jsonArray = null;
    }

    @Test
    public void testCalculateAnalytics() {
        AnalyticsCalculator calculator = new AnalyticsCalculator(jsonArray);
        AnalyticsResult result = calculator.calculateAnalytics("USDT/PLN");
        assertNotNull(result);
        assertEquals(15.0, result.getAveragePrice(), 0.01);
        assertEquals(1079906400, result.getFirstTimestamp());
        assertEquals(1084914000, result.getLastTimestamp());
        assertFalse(result.hasDuplicates());
    }
}
