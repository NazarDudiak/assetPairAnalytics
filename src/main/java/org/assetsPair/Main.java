package org.assetsPair;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final String JSON_FILE_PATH = "src/main/resources/stock_data.json";

    public static void main(String[] args) throws CustomDataLoadException {
        try {
            // Read the contents of the file as a string
            String jsonStr = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));

            // Parse JSON string to JsonArray
            JsonArray jsonArray = new Gson().fromJson(jsonStr, JsonArray.class);

            TradingPairsService tradingPairsService = new TradingPairsService(jsonArray);

            // A loop for constantly entering new requests from the user
            while (true) {
                String assetPair = promptAssetPair(); // Ask the user for a pair of assets, or expect /q
                if (isQuitSignal(assetPair)) {
                    System.out.println("Exiting the application...");
                    System.exit(0); // Shutdown the whole app
                }
                if (!isValidAssetPair(assetPair)) {
                    System.out.println("Incorrect input. Try again or type /q.");
                    continue;
                }
                tradingPairsService.displayAnalytics(assetPair); // Display analytics for a given pair of assets
            }
        } catch (IOException e) {
            throw new CustomDataLoadException("Error data:", e);
        }
    }

    // Method for checking if the input is the quit signal
    public static boolean isQuitSignal(String assetPair) {
        return assetPair.equals("/q");
    }

    // Method for checking if the input is a valid asset pair
    public static boolean isValidAssetPair(String assetPair) {
        Set<String> validPairs = new HashSet<>();
        validPairs.add("USDT/PLN");
        validPairs.add("USDT/EUR");
        validPairs.add("USDT/UAH");
        validPairs.add("USDT/BTC");

        return validPairs.contains(assetPair);
    }

    // Method for user input about a pair of assets
    public static String promptAssetPair() {
        // Loop to validate user input
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a pair of assets (USDT/PLN, USDT/EUR, USDT/UAH, USDT/BTC): ");
            String assetPair = scanner.nextLine();
            if (isQuitSignal(assetPair) || isValidAssetPair(assetPair)) {
                return assetPair;
            } else {
                System.out.println("Incorrect input. Try again or type /q.");
            }
        }
    }
}

class TradingPairsService {
    private final JsonArray jsonArray;

    public TradingPairsService(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    // Method for outputting analytics for a given pair of assets
    public void displayAnalytics(String assetPair) {
        AnalyticsCalculator analyticsCalculator = new AnalyticsCalculator(jsonArray);
        AnalyticsResult analyticsResult = analyticsCalculator.calculateAnalytics(assetPair);

        // Display the analytics on the screen
        System.out.println(analyticsResult.toString());
    }
}

class AnalyticsCalculator {
    private final JsonArray jsonArray;

    public AnalyticsCalculator(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    // Method for calculating analytics for a given pair of assets
    public AnalyticsResult calculateAnalytics(String assetPair) {
        double sum = 0;
        int matchingPairsCount = 0; // A counter of the number of pairs we're considering
        long firstTimestamp = Long.MAX_VALUE;
        long lastTimestamp = Long.MIN_VALUE;
        Set<Long> timestamps = new HashSet<>();

        // Iterates over each JSON object in the array
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            if (jsonObject.get("assetPair").getAsString().equals(assetPair)) {
                matchingPairsCount++;

                double value = jsonObject.get("value").getAsDouble();
                long timestamp = jsonObject.get("timestamp").getAsLong();

                sum += value;
                if (timestamp < firstTimestamp) {
                    firstTimestamp = timestamp;
                }
                if (timestamp > lastTimestamp) {
                    lastTimestamp = timestamp;
                }
                timestamps.add(timestamp);
            }
        }

        double averagePrice = calculateAveragePrice(sum, matchingPairsCount);
        boolean hasDuplicates = timestamps.size() < matchingPairsCount;

        return new AnalyticsResult(averagePrice, firstTimestamp, lastTimestamp, hasDuplicates);
    }

    // Method to calculate the average price
    private double calculateAveragePrice(double sum, int count) {
        if (count == 0) return 0; // Prevent division by zero
        return sum / count;
    }
}

class AnalyticsResult {
    private final double averagePrice;
    private final long firstTimestamp;
    private final long lastTimestamp;
    private final boolean hasDuplicates;

    public AnalyticsResult(double averagePrice, long firstTimestamp, long lastTimestamp, boolean hasDuplicates) {
        this.averagePrice = averagePrice;
        this.firstTimestamp = firstTimestamp;
        this.lastTimestamp = lastTimestamp;
        this.hasDuplicates = hasDuplicates;
    }

    @Override
    public String toString() {
        return "Average price: %s\nThe first timestamp seen is: %s\nTimestamp of last observation: %s\nPresence of duplicates: %s\n".formatted(averagePrice, formatDate(firstTimestamp), formatDate(lastTimestamp), hasDuplicates);
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public long getFirstTimestamp() {
        return firstTimestamp;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public boolean hasDuplicates() {
        return hasDuplicates;
    }

    // Method to format the timestamp into the format we need
    public String formatDate(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }
}


