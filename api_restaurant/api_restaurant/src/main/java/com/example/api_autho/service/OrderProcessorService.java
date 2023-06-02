package com.example.api_autho.service;

import java.sql.*;

public class OrderProcessorService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Db954216837";

    public static void processOrders() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String selectQuery = "SELECT * FROM orders WHERE status = 'active'";
            String updateQuery = "UPDATE orders SET status = 'done' WHERE id = ?";

            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                // Обработка заказа с некоторой задержкой
                processOrderWithDelay(orderId);

                // Изменение статуса заказа на "выполнен"
                updateStatement.setInt(1, orderId);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void processOrderWithDelay(int orderId) {
        try {
            System.out.println("Processing order with ID: " + orderId);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
