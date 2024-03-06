import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static JSONArray jsonArray; // Оголошення змінної

    public static void main(String[] args) {
        try {
            // Зчитуємо вміст файлу у вигляді рядка
            String jsonStr = new String(Files.readAllBytes(Paths.get("stock_data.json")));
            // Створюємо JSONArray з рядка
            jsonArray = new JSONArray(jsonStr);

            // Цикл для постійного введення нових запитів від користувача
            while (true) {
                String assetPair = promptAssetPair(); // Запитуємо користувача про пару активів, або очікуємо /q
                if (!assetPair.equals("/q")) { // Якщо користувач не бажає завершити роботу
                    displayAnalytics(assetPair); // Відображаємо аналітику для заданої пари активів
                } else {
                    System.out.println("До зустрічі!");
                    break; // Вихід з циклу
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для введення користувача про пару активів
    private static String promptAssetPair() {
        // Список доступних пар активів
        Set<String> assetPairs = new HashSet<>();
        assetPairs.add("USDT/PLN");
        assetPairs.add("USDT/EUR");
        assetPairs.add("USDT/UAH");
        assetPairs.add("USDT/BTC");

        // Цикл для перевірки введення користувача
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введіть пару активів (USDT/PLN, USDT/EUR, USDT/UAH, USDT/BTC):");
            String assetPair = scanner.nextLine();
            if (assetPairs.contains(assetPair)) {
                return assetPair;
            } else if (assetPair.equals("/q")) {
                break; // Вихід з циклу
            } else {
                System.out.println("Неправильний ввід. Спробуйте ще раз, або введіть /q.");
            }
        }
        return "/q"; // Повернення пустого рядка в разі виходу з циклу через "/q"
    }

    // Метод для виведення аналітики для заданої пари активів
    private static void displayAnalytics(String assetPair) {
        double sum = 0;
        int assetPairMatches = 0; // Лічильник кількості пар, які ми беремо до уваги
        long firstTimestamp = Long.MAX_VALUE;
        long lastTimestamp = Long.MIN_VALUE;
        Set<Long> timestamps = new HashSet<>();

        // Ітеруємося по кожному об'єкту JSON у масиві
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString("assetPair").equals(assetPair)) {
                assetPairMatches++;

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
        // Виводимо аналітику на екран
        System.out.println("Середня ціна: " + (sum / assetPairMatches));
        System.out.println("Перша помічена позначка часу: " + formatDate(firstTimestamp));
        System.out.println("Позначка часу останнього спостереження: " + formatDate(lastTimestamp));
        System.out.println("Наявність дублікатів: " + (timestamps.size() < assetPairMatches));
    }

    // Метод для форматування мітки часу у потрібний нам формат
    private static String formatDate(long timestamp) {
        Date date = new Date(timestamp * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }
}
