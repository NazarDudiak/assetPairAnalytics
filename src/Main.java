import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            // Read the contents of the file as a string
            String jsonStr = new String(Files.readAllBytes(Paths.get("src/main/resources/stock_data.json")));

            // Create a JSONArray from a string
            JSONArray jsonArray = new JSONArray(jsonStr);

            // A loop for constantly entering new requests from the user
            while (true) {
                String assetPair = promptAssetPair(); // Ask the user for a pair of assets, or expect /q
                if (!assetPair.equals("/q")) { // If the user doesn't want to end the job
                    displayAnalytics(assetPair, jsonArray); // Display analytics for a given pair of assets
                } else {
                    System.out.println("Bye!");
                    break; // Exit the loop
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for user input about a pair of assets
    public static String promptAssetPair() {
        // Список доступних пар активів
        Set<String> assetPairs = new HashSet<>();
        assetPairs.add("USDT/PLN");
        assetPairs.add("USDT/EUR");
        assetPairs.add("USDT/UAH");
        assetPairs.add("USDT/BTC");

        // Loop to validate user input
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a pair of assets (USDT/PLN, USDT/EUR, USDT/UAH, USDT/BTC): ");
            String assetPair = scanner.nextLine();
            if (assetPairs.contains(assetPair)) {
                return assetPair;
            } else if (assetPair.equals("/q")) {
                break; // Exit the loop
            } else {
                System.out.println("Incorrect input. Try again or type /q.");
            }
        }
        return "/q"; // Return an empty string in case of loop exit via "/q"
    }

    // Method for outputting analytics for a given pair of assets
    public static String displayAnalytics(String assetPair, JSONArray jsonArray) {
        double sum = 0;
        int matchingPairsCount = 0; // A counter of the number of pairs we're considering
        long firstTimestamp = Long.MAX_VALUE;
        long lastTimestamp = Long.MIN_VALUE;
        Set<Long> timestamps = new HashSet<>();

        // Iterates over each JSON object in the array
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString("assetPair").equals(assetPair)) {
                matchingPairsCount++;

                double value = jsonObject.getDouble("value");
                long timestamp = jsonObject.getLong("timestamp");

                sum += value;
                if (timestamp < firstTimestamp) {
                    firstTimestamp = timestamp;
                } else if (timestamp > lastTimestamp) {
                    lastTimestamp = timestamp;
                }
                timestamps.add(timestamp);
            }
        }
        // Display the analytics on the screen
        System.out.println("Average price: " + (sum / matchingPairsCount));
        System.out.println("The first timestamp seen is: " + formatDate(firstTimestamp));
        System.out.println("Timestamp of last observation: " + formatDate(lastTimestamp));
        System.out.println("Presence of duplicates: " + (timestamps.size() < matchingPairsCount));

        // Forming a line with analytics for testing
        String result = "";
        result += "Average price: " + (sum / matchingPairsCount) + "\n";
        result += "The first timestamp seen is: " + formatDate(firstTimestamp) + "\n";
        result += "Timestamp of last observation: " + formatDate(lastTimestamp) + "\n";
        result += "Presence of duplicates: " + (timestamps.size() < matchingPairsCount) + "\n";

        return result;
    }

    // Method to format the timestamp into the format we need
    public static String formatDate(long timestamp) {
        Date date = new Date(timestamp * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the time zone to UTC
        return sdf.format(date);
    }
}
