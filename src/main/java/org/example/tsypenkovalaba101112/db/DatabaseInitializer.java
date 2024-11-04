package org.example.tsypenkovalaba101112.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS parents (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "mother_first_name VARCHAR(255) NOT NULL," +
                    "father_first_name VARCHAR(255) NOT NULL," +
                    "last_name VARCHAR(255) NOT NULL UNIQUE" +
                    ");");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS childs (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "first_name VARCHAR(255) NOT NULL," +
                    "last_name VARCHAR(255) NOT NULL," +
                    "birth_day DATETIME NOT NULL," +
                    "gender ENUM('male', 'female') DEFAULT 'female'," +
                    "photo VARCHAR(255) NOT NULL," +
                    "parent_id BIGINT," +
                    "registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (parent_id) REFERENCES parents(id)" +
                    ");");

            try {
                statement.executeUpdate("INSERT INTO parents (mother_first_name, father_first_name, last_name) VALUES " +
                        "('svetlana', 'alexandr', 'tsypenkovy'), " +
                        "('anna', 'viktor', 'metezh');");

                statement.executeUpdate("INSERT INTO childs (first_name, last_name, gender, birth_day, photo, parent_id) VALUES " +
                        "('liza', 'tsypenkova', 'female', '2005-10-01 10:00:00', 'https://vitae.com.ua/wp-content/uploads/2020/05/gallery4-1170x650.jpg', 1), " +
                        "('ksusha', 'metezh', 'female', '2010-10-02 10:00:00', 'https://masterpiecer-images.s3.yandex.net/5fd1df458f8b387:upscaled', 2);");
            } catch (Exception e) {
                // ignore
            }

            System.out.println("Sql command executed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
