# Asset Pair Analytics

This Java application provides analytics for asset pairs based on the data provided in the `stock_data.json` file. The user is prompted to provide an asset pair as an argument, and the program retrieves analytics for that asset pair.

## How to Use

1. Clone the repository.
2. Navigate to the project directory.
3. Ensure you have Java installed on your system.
4. Run the application using the following command:

```
java -jar asset-analytics.jar [assetPair]
```
Replace `[assetPair]` with one of the following asset pairs: "USDT/PLN", "USDT/EUR", "USDT/UAH", or "USDT/BTC".


## Analytics Provided

The application provides the following analytics for the specified asset pair:

1. **Average Price:** The average price of the provided asset pair.
2. **First Observed Timestamp:** The first observed timestamp for the provided asset pair in the format DD/MM/YYYY HH:MM:SS.
3. **Last Observed Timestamp:** The last observed timestamp for the provided asset pair in the format DD/MM/YYYY HH:MM:SS.
4. **Existence of Duplicates:** Indicates whether duplicates exist for the asked asset pair (true/false).

## Data Source

The application relies on the `stock_data.json` file placed in the `src/main/resources` folder of the project. This file contains data entries with the following structure:

- **assetPair:** One of - "USDT/PLN", "USDT/EUR", "USDT/UAH", "USDT/BTC".
- **timestamp:** Unix timestamp.
- **value:** Value of the current stock at the provided timestamp.

## Unit Tests

Unit tests have been included to ensure the correctness of the application. You can find the tests in the `src/test/java` directory.

## Notes

- Ensure the `stock_data.json` file is correctly formatted and contains the required data.
- If you encounter any issues or have suggestions for improvements, please feel free to raise an issue or submit a pull request.

Thank you for using the Asset Pair Analytics!
