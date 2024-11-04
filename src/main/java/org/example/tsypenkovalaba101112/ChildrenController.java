package org.example.tsypenkovalaba101112;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.tsypenkovalaba101112.db.DatabaseHelper;
import org.example.tsypenkovalaba101112.entity.Child;
import org.example.tsypenkovalaba101112.entity.Parent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.tsypenkovalaba101112.db.DatabaseConnector.PASSWORD;
import static org.example.tsypenkovalaba101112.db.DatabaseConnector.URL;
import static org.example.tsypenkovalaba101112.db.DatabaseConnector.USER;

public class ChildrenController {

    @FXML
    private TableView<Child> childrenTable;
    @FXML
    private TableColumn<Child, Long> idCol;
    @FXML
    private TableColumn<Child, String> firstNameCol;
    @FXML
    private TableColumn<Child, String> lastNameCol;
    @FXML
    private TableColumn<Child, String> birthDayCol;
    @FXML
    private TableColumn<Child, String> genderCol;
    @FXML
    private TableColumn<Child, String> photoCol;
    @FXML
    private TableColumn<Child, Long> parentIdCol;

    private final ObservableList<Child> childrenList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        String css = getClass().getResource("/css/styles.css").toExternalForm();
        childrenTable.getStylesheets().add(css);

        idCol.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        birthDayCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthDay().toString()));
        genderCol.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        photoCol.setCellValueFactory(cellData -> cellData.getValue().photoProperty());
        parentIdCol.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getParentId()).asObject());
        reloadChildren();
    }

    private void reloadChildren() {
        try {
            childrenList.clear();
            ResultSet rs = DatabaseHelper.getChildren();
            if (rs != null) {

                while (rs.next()) {
                    Child child = new Child(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"),
                            rs.getTimestamp("birth_day").toLocalDateTime(), rs.getString("gender"), rs.getString("photo"),
                            rs.getLong("parent_id"));
                    childrenList.add(child);
                }
                childrenTable.setItems(childrenList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSearchButtonClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Поиск детей");
        dialog.setHeaderText("Введите имя или часть имени для поиска:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String name = result.get();
            List<Child> foundChildren = DatabaseHelper.searchChildrenByName(name);
            childrenList.clear();
            childrenList.addAll(foundChildren);
            childrenTable.setItems(childrenList);
        }
    }

    @FXML
    protected void onResetSearchButtonClick() {
        reloadChildren();
    }

    @FXML
    protected void onAddChildButtonClick() {
        Dialog<Child> dialog = new Dialog<>();
        dialog.setTitle("Добавить ребенка");
        dialog.setHeaderText("Введите информацию о ребенке:");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Имя");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Фамилия");
        TextField genderField = new TextField();
        genderField.setPromptText("Пол (male/female)");
        TextField birthDayField = new TextField();
        birthDayField.setPromptText("Дата рождения (YYYY-MM-DD HH:MM:SS)");
        TextField photoField = new TextField();
        photoField.setPromptText("URL фотографии");
        TextField parentIdField = new TextField();
        parentIdField.setPromptText("ID родителя");

        VBox vbox = new VBox(10, firstNameField, lastNameField, genderField, birthDayField, photoField, parentIdField);
        dialog.getDialogPane().setContent(vbox);

        ButtonType addButtonType = new ButtonType("Добавить", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String gender = genderField.getText();
                String birthDayText = birthDayField.getText();
                LocalDateTime birthDay;
                if (!birthDayText.isEmpty()) {
                    birthDay = LocalDateTime.parse(birthDayText);
                } else {
                    birthDay = LocalDateTime.now();
                }

                String photo = photoField.getText();
                Long parentId = parentIdField.getText().isEmpty() ? 1 : Long.parseLong(parentIdField.getText());

                return new Child(null, firstName, lastName, birthDay, gender, photo, parentId);
            }
            return null;
        });

        Optional<Child> result = dialog.showAndWait();
        result.ifPresent(child -> {
            DatabaseHelper.addChild(child);
            reloadChildren();
        });
    }

    @FXML
    protected void onEditChildButtonClick() {
        Child selectedChild = childrenTable.getSelectionModel().getSelectedItem();
        if (selectedChild == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Выбор не сделан");
            alert.setContentText("Пожалуйста, выберите запись для редактирования.");
            alert.showAndWait();
            return;
        }

        Dialog<Child> dialog = new Dialog<>();
        dialog.setTitle("Редактировать ребенка");
        dialog.setHeaderText("Измените информацию о ребенке:");

        TextField firstNameField = new TextField(selectedChild.getFirstName());
        firstNameField.setPromptText("Имя");
        TextField lastNameField = new TextField(selectedChild.getLastName());
        lastNameField.setPromptText("Фамилия");
        TextField genderField = new TextField(selectedChild.getGender());
        genderField.setPromptText("Пол (male/female)");
        TextField birthDayField = new TextField(selectedChild.getBirthDay().toString().replace("T", " "));
        birthDayField.setPromptText("Дата рождения (YYYY-MM-DD HH:MM:SS)");
        TextField photoField = new TextField(selectedChild.getPhoto());
        photoField.setPromptText("URL фотографии");
        TextField parentIdField = new TextField(selectedChild.getParentId() != null ? selectedChild.getParentId().toString() : "");

        VBox vbox = new VBox(10, firstNameField, lastNameField, genderField, birthDayField, photoField, parentIdField);
        dialog.getDialogPane().setContent(vbox);

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String gender = genderField.getText();
                String birthDayStr = birthDayField.getText().replace(" ", "T");
                LocalDateTime birthDay = birthDayStr.isEmpty() ? selectedChild.getBirthDay() : LocalDateTime.parse(birthDayStr);
                String photo = photoField.getText();
                Long parentId = parentIdField.getText().isEmpty() ? selectedChild.getParentId() : Long.parseLong(parentIdField.getText());

                return new Child(
                        selectedChild.getId(),
                        firstName.isEmpty() ? selectedChild.getFirstName() : firstName,
                        lastName.isEmpty() ? selectedChild.getLastName() : lastName,
                        birthDay,
                        gender.isEmpty() ? selectedChild.getGender() : gender,
                        photo.isEmpty() ? selectedChild.getPhoto() : photo,
                        parentId
                );
            }
            return null;
        });

        Optional<Child> result = dialog.showAndWait();
        result.ifPresent(child -> {
            DatabaseHelper.updateChild(child);
            reloadChildren();
        });
    }

    @FXML
    protected void onDeleteChildButtonClick() {
        Child selectedChild = childrenTable.getSelectionModel().getSelectedItem();
        if (selectedChild != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить выбранного ребенка?");
            alert.setContentText(selectedChild.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DatabaseHelper.deleteChild(selectedChild.getId());
                reloadChildren();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Не выбран ребенок для удаления");
            alert.setContentText("Пожалуйста, выберите запись из таблицы.");
            alert.showAndWait();
        }
    }
}