package org.example.tsypenkovalaba101112.db;

import org.example.tsypenkovalaba101112.entity.Child;
import org.example.tsypenkovalaba101112.entity.ChildView;
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

    public static String getParentFatherName(Long parentId) {
        String fatherName = null;
        String query = "SELECT father_first_name FROM parents WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, parentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                fatherName = rs.getString("father_first_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fatherName != null ? fatherName : "Неизвестно";
    }

    public static String getParentMotherName(Long parentId) {
        String motherName = null;
        String query = "SELECT mother_first_name FROM parents WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, parentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                motherName = rs.getString("mother_first_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return motherName != null ? motherName : "Неизвестно";
    }

    public static List<Parent> getParentsList() {
        List<Parent> parents = new ArrayList<>();
        String query = "SELECT id, last_name FROM parents";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String lastName = rs.getString("last_name");
                parents.add(new Parent(id, lastName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parents;
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
            return statement.executeQuery("SELECT * FROM children_with_parents_info");
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
        String sql = "UPDATE childs SET first_name = ?, last_name = ?, gender = ?, birth_day = ?, photo = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, child.getFirstName());
            statement.setString(2, child.getLastName());
            statement.setString(3, child.getGender());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(child.getBirthDay()));
            statement.setString(5, child.getPhoto());
            statement.setLong(6, child.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<ChildView> searchChildrenByName(String name) {
        List<ChildView> children = new ArrayList<>();
        String sql = "SELECT * FROM children_with_parents_info WHERE first_name LIKE ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, '%' + name + '%');
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChildView child = new ChildView(resultSet.getLong("id"), resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getTimestamp("birth_day").toLocalDateTime(), resultSet.getString("gender"), resultSet.getString("photo"),
                        resultSet.getString("father_first_name"), resultSet.getString("mother_first_name"));
                children.add(child);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return children;
    }
}
