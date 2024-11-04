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
                    "FOREIGN KEY (parent_id) REFERENCES parents(id)," +
                    "UNIQUE (first_name, last_name)" +
                    ");");

            // Выполнение вставок
            int parentsInserted = statement.executeUpdate("INSERT IGNORE INTO parents (mother_first_name, father_first_name, last_name) VALUES " +
                    "('svetlana', 'alexandr', 'tsypenkovy'), " +
                    "('liudmila', 'igor', 'udalye'), " +
                    "('anna', 'viktor', 'metezh');");
            System.out.println(parentsInserted + " parents inserted.");

            int childrenInserted = statement.executeUpdate("INSERT IGNORE INTO childs (first_name, last_name, gender, birth_day, photo, parent_id) VALUES " +
                    "('liza', 'tsypenkova', 'female', '2005-10-01 10:00:00', 'https://vitae.com.ua/wp-content/uploads/2020/05/gallery4-1170x650.jpg', 1), " +
                    "('dasha', 'tsypenkova', 'female', '2010-10-01 10:00:00', 'https://vitae.com.ua/wp-content/uploads/2020/05/gallery4-1170x650.jpg', 1), " +
                    "('danik', 'tsypenkov', 'male', '2015-10-01 10:00:00', 'https://vitae.com.ua/wp-content/uploads/2020/05/gallery4-1170x650.jpg', 1), " +
                    "('pasha', 'udaloi', 'male', '2000-10-01 10:00:00', 'https://vitae.com.ua/wp-content/uploads/2020/05/gallery4-1170x650.jpg', 2), " +
                    "('ksush', 'metezh', 'male', '2015-10-01 10:00:00', 'https://vitae.com.ua/wp-content/uploads/2020/05/gallery4-1170x650.jpg', 3), " +
                    "('ksusha', 'metezh', 'female', '2010-10-02 10:00:00', 'https://masterpiecer-images.s3.yandex.net/5fd1df458f8b387:upscaled', 3);");
            System.out.println(childrenInserted + " children inserted.");

            statement.executeUpdate("CREATE VIEW IF NOT EXISTS parents_with_children AS " +
                    "SELECT p.id, p.mother_first_name, p.father_first_name, p.last_name, COUNT(c.id) AS children_count " +
                    "FROM parents p " +
                    "LEFT JOIN childs c ON p.id = c.parent_id " +
                    "GROUP BY p.id, p.mother_first_name, p.father_first_name, p.last_name " +
                    "HAVING COUNT(c.id) >= 2; ");

            statement.executeUpdate("CREATE PROCEDURE IF NOT EXISTS GetParentsWithThreeOrMoreChildren() " +
                    "BEGIN " +
                    "    SELECT p.id, p.mother_first_name, p.father_first_name, p.last_name " +
                    "    FROM parents p " +
                    "    LEFT JOIN childs c ON p.id = c.parent_id " +
                    "    GROUP BY p.id " +
                    "    HAVING COUNT(c.id) >= 3; " +
                    "END;");

            statement.executeUpdate("CREATE VIEW IF NOT EXISTS children_with_parents_info AS " +
                    "SELECT c.id, c.first_name, c.last_name, c.birth_day, c.gender, c.photo, p.mother_first_name, p.father_first_name " +
                    "FROM childs c " +
                    "JOIN parents p ON p.id = c.parent_id;");

            System.out.println("Sql command executed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
