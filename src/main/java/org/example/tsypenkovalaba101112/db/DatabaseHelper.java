package org.example.tsypenkovalaba101112.db;

import org.example.tsypenkovalaba101112.entity.Child;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public static ResultSet getChildren() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT * FROM childs");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addChild(Child child) {
        String sql = "INSERT INTO childs (first_name, last_name, gender, birth_day, photo, parent_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, child.getFirstName());
            statement.setString(2, child.getLastName());
            statement.setString(3, child.getGender());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(child.getBirthDay()));
            statement.setString(5, child.getPhoto());
            statement.setLong(6, child.getParentId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteChild(long id) {
        String sql = "DELETE FROM childs WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateChild(Child child) {
        String sql = "UPDATE childs SET first_name = ?, last_name = ?, gender = ?, birth_day = ?, photo = ?, parent_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, child.getFirstName());
            statement.setString(2, child.getLastName());
            statement.setString(3, child.getGender());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(child.getBirthDay()));
            statement.setString(5, child.getPhoto());
            statement.setLong(6, child.getParentId());
            statement.setLong(7, child.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Child> searchChildrenByName(String name) {
        List<Child> children = new ArrayList<>();
        String sql = "SELECT * FROM childs WHERE first_name LIKE ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, '%' + name + '%');
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Child child = new Child(resultSet.getLong("id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getTimestamp("birth_day").toLocalDateTime(),
                        resultSet.getString("gender"), resultSet.getString("photo"), resultSet.getLong("parent_id"));
                children.add(child);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return children;
    }
}
