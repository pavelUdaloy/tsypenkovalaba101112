package org.example.tsypenkovalaba101112.db;

import org.example.tsypenkovalaba101112.entity.Child;
import org.example.tsypenkovalaba101112.entity.Parent;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public static ResultSet getParents() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT * FROM parents");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addParent(Parent parent) {
        String sql = "INSERT INTO parents (mother_first_name, father_first_name, last_name) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, parent.getMotherFirstName());
            statement.setString(2, parent.getFatherFirstName());
            statement.setString(3, parent.getLastName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteParent(long id) {
        String sql = "DELETE FROM parents WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateParent(Parent parent) {
        String sql = "UPDATE parents SET mother_first_name = ?, father_first_name = ?, last_name = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, parent.getMotherFirstName());
            statement.setString(2, parent.getFatherFirstName());
            statement.setString(3, parent.getLastName());
            statement.setLong(4, parent.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Parent> searchParentsByLastName(String name) {
        List<Parent> parents = new ArrayList<>();
        String sql = "SELECT * FROM parents WHERE last_name LIKE ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, '%' + name + '%');
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Parent parent = new Parent(resultSet.getLong("id"),
                        resultSet.getString("mother_first_name"),
                        resultSet.getString("father_first_name"),
                        resultSet.getString("last_name"));
                parents.add(parent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parents;
    }

    public static List<Parent> searchParentsWithTwoOrMoreChildren() {
        List<Parent> parents = new ArrayList<>();
        String query = "SELECT * FROM parents_with_children";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Parent parent = new Parent(rs.getLong("id"),
                        rs.getString("mother_first_name"),
                        rs.getString("father_first_name"),
                        rs.getString("last_name"));
                parents.add(parent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parents;
    }

    public static List<Parent> getParentsWithThreeOrMoreChildren() {
        List<Parent> parents = new ArrayList<>();
        String query = "{CALL GetParentsWithThreeOrMoreChildren()}";

        try (Connection connection = DatabaseConnector.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Parent parent = new Parent(rs.getLong("id"),
                        rs.getString("mother_first_name"),
                        rs.getString("father_first_name"),
                        rs.getString("last_name"));
                parents.add(parent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parents;
    }

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
