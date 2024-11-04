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

import static org.example.tsypenkovalaba101112.HelloController.showAlert;
import static org.example.tsypenkovalaba101112.db.DatabaseConnector.PASSWORD;
import static org.example.tsypenkovalaba101112.db.DatabaseConnector.URL;
import static org.example.tsypenkovalaba101112.db.DatabaseConnector.USER;

public class ParentsController {

    @FXML
    private TableView<Parent> parentsTable;
    @FXML
    private TableColumn<Parent, Long> parentIdColInParents;
    @FXML
    private TableColumn<Parent, String> parentMotherFirstNameCol;
    @FXML
    private TableColumn<Parent, String> parentFatherFirstNameCol;
    @FXML
    private TableColumn<Parent, String> parentLastNameCol;

    private final ObservableList<Parent> parentsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        parentIdColInParents.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
        parentMotherFirstNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMotherFirstName()));
        parentFatherFirstNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFatherFirstName()));
        parentLastNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));

        reloadParents();
    }

    private void reloadParents() {
        try {
            parentsList.clear();
            ResultSet rs = DatabaseHelper.getParents();
            if (rs != null) {
                while (rs.next()) {
                    Parent parent = new Parent(rs.getLong("id"),
                            rs.getString("mother_first_name"),
                            rs.getString("father_first_name"),
                            rs.getString("last_name"));
                    parentsList.add(parent);
                }
                parentsTable.setItems(parentsList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить данные о родителях.");
        }
    }

    @FXML
    protected void onAddParentButtonClick() {
        Dialog<Parent> dialog = new Dialog<>();
        dialog.setTitle("Добавить родителя");
        dialog.setHeaderText("Введите информацию о родителе:");

        TextField motherFirstNameField = new TextField();
        motherFirstNameField.setPromptText("Имя матери");
        TextField fatherFirstNameField = new TextField();
        fatherFirstNameField.setPromptText("Имя отца");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Фамилия");

        VBox vbox = new VBox(10, motherFirstNameField, fatherFirstNameField, lastNameField);
        dialog.getDialogPane().setContent(vbox);

        ButtonType addButtonType = new ButtonType("Добавить", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String motherFirstName = motherFirstNameField.getText();
                String fatherFirstName = fatherFirstNameField.getText();
                String lastName = lastNameField.getText();

                return new Parent(null, motherFirstName, fatherFirstName, lastName);
            }
            return null;
        });

        Optional<Parent> result = dialog.showAndWait();
        result.ifPresent(parent -> {
            DatabaseHelper.addParent(parent);
            reloadParents();
        });
    }

    @FXML
    protected void onEditParentButtonClick() {
        Parent selectedParent = parentsTable.getSelectionModel().getSelectedItem();
        if (selectedParent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Выбор не сделан");
            alert.setContentText("Пожалуйста, выберите родителя для редактирования.");
            alert.showAndWait();
            return;
        }

        Dialog<Parent> dialog = new Dialog<>();
        dialog.setTitle("Редактировать родителя");
        dialog.setHeaderText("Измените информацию о родителе:");

        TextField motherFirstNameField = new TextField(selectedParent.getMotherFirstName());
        motherFirstNameField.setPromptText("Имя матери");
        TextField fatherFirstNameField = new TextField(selectedParent.getFatherFirstName());
        fatherFirstNameField.setPromptText("Имя отца");
        TextField lastNameField = new TextField(selectedParent.getLastName());
        lastNameField.setPromptText("Фамилия");

        VBox vbox = new VBox(10, motherFirstNameField, fatherFirstNameField, lastNameField);
        dialog.getDialogPane().setContent(vbox);

        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String motherFirstName = motherFirstNameField.getText();
                String fatherFirstName = fatherFirstNameField.getText();
                String lastName = lastNameField.getText();

                return new Parent(selectedParent.getId(), motherFirstName, fatherFirstName, lastName);
            }
            return null;
        });

        Optional<Parent> result = dialog.showAndWait();
        result.ifPresent(parent -> {
            DatabaseHelper.updateParent(parent);
            reloadParents();
        });
    }

    @FXML
    protected void onDeleteParentButtonClick() {
        Parent selectedParent = parentsTable.getSelectionModel().getSelectedItem();
        if (selectedParent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить выбранного родителя?");
            alert.setContentText(selectedParent.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DatabaseHelper.deleteParent(selectedParent.getId());
                reloadParents();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Не выбран родитель для удаления");
            alert.setContentText("Пожалуйста, выберите запись из таблицы.");
            alert.showAndWait();
        }
    }

    @FXML
    protected void onSearchButtonClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Поиск родителей");
        dialog.setHeaderText("Введите фамилию или часть фамилии для поиска:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String name = result.get();
            List<Parent> foundParent = DatabaseHelper.searchParentsByLastName(name);
            parentsList.clear();
            parentsList.addAll(foundParent);
            parentsTable.setItems(parentsList);
        }
    }

    @FXML
    protected void onResetSearchButtonClick() {
        reloadParents();
    }
}