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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.tsypenkovalaba101112.db.DatabaseHelper;
import org.example.tsypenkovalaba101112.entity.Child;
import org.example.tsypenkovalaba101112.entity.ChildView;
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
    private TableView<ChildView> childrenTable;
    @FXML
    private TableColumn<ChildView, Long> idCol;
    @FXML
    private TableColumn<ChildView, String> firstNameCol;
    @FXML
    private TableColumn<ChildView, String> lastNameCol;
    @FXML
    private TableColumn<ChildView, String> birthDayCol;
    @FXML
    private TableColumn<ChildView, String> genderCol;
    @FXML
    private TableColumn<ChildView, String> photoCol;
    @FXML
    private TableColumn<ChildView, String> parentFatherNameCol;
    @FXML
    private TableColumn<ChildView, String> parentMotherNameCol;

    private final ObservableList<ChildView> childrenList = FXCollections.observableArrayList();

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
        parentFatherNameCol.setCellValueFactory(cellData -> cellData.getValue().parentFatherNameProperty());
        parentMotherNameCol.setCellValueFactory(cellData -> cellData.getValue().parentMotherNameProperty());
        reloadChildren();
    }

    private void reloadChildren() {
        try {
            childrenList.clear();
            ResultSet rs = DatabaseHelper.getChildren();
            if (rs != null) {

                while (rs.next()) {
                    ChildView child = new ChildView(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"),
                            rs.getTimestamp("birth_day").toLocalDateTime(), rs.getString("gender"), rs.getString("photo"),
                            rs.getString("father_first_name"), rs.getString("mother_first_name"));
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
            List<ChildView> foundChildren = DatabaseHelper.searchChildrenByName(name);
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

        ComboBox<Parent> parentComboBox = new ComboBox<>();
        parentComboBox.setPromptText("Выберите родителя");

        List<Parent> parents = DatabaseHelper.getParentsList();
        parentComboBox.getItems().addAll(parents);

        VBox vbox = new VBox(10, firstNameField, lastNameField, genderField, birthDayField, photoField, parentComboBox);
        dialog.getDialogPane().setContent(vbox);

        ButtonType addButtonType = new ButtonType("Добавить", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String gender = genderField.getText().trim();
                String birthDayText = birthDayField.getText().trim();
                LocalDateTime birthDay;

                // Проверка и парсинг даты
                if (!birthDayText.isEmpty()) {
                    try {
                        birthDay = LocalDateTime.parse(birthDayText);
                    } catch (Exception e) {
                        showAlert("Ошибка", "Неверный формат даты рождения.");
                        return null;
                    }
                } else {
                    birthDay = LocalDateTime.now();
                }

                String photo = photoField.getText().trim();
                Parent selectedParent = parentComboBox.getValue();
                Long parentId = selectedParent != null ? selectedParent.getId() : null;

                // Проверка обязательных полей
                if (firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || parentId == null) {
                    showAlert("Ошибка", "Пожалуйста, заполните все обязательные поля.");
                    return null;
                }

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getParentFatherName(Long parentId) {
        return DatabaseHelper.getParentFatherName(parentId);
    }

    private String getParentMotherName(Long parentId) {
        return DatabaseHelper.getParentMotherName(parentId);
    }


    @FXML
    protected void onEditChildButtonClick() {
        ChildView selectedChild = childrenTable.getSelectionModel().getSelectedItem();
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

        VBox vbox = new VBox(10, firstNameField, lastNameField, genderField, birthDayField, photoField);
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

                return new Child(
                        selectedChild.getId(),
                        firstName.isEmpty() ? selectedChild.getFirstName() : firstName,
                        lastName.isEmpty() ? selectedChild.getLastName() : lastName,
                        birthDay,
                        gender.isEmpty() ? selectedChild.getGender() : gender,
                        photo.isEmpty() ? selectedChild.getPhoto() : photo
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
        ChildView selectedChild = childrenTable.getSelectionModel().getSelectedItem();
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