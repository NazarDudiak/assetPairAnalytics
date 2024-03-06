import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MainTest {
    @Test
    public void testFormatDate_ValidTimestamp() {
        long timestamp = 1079931600;
        String expected = "22/03/2004 05:00:00"; // GMT Mon Mar 22 2004 05:00:00 GMT+0000
        String actual = Main.formatDate(timestamp);

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDate_ZeroTimestamp() {
        long timestamp = 0;
        String expected = "01/01/1970 00:00:00";
        String actual = Main.formatDate(timestamp);

        assertEquals(expected, actual);
    }
}
