package org.example.tsypenkovalaba101112;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static org.example.tsypenkovalaba101112.db.DatabaseConnector.PASSWORD;
import static org.example.tsypenkovalaba101112.db.DatabaseConnector.URL;
import static org.example.tsypenkovalaba101112.db.DatabaseConnector.USER;

public class HelloController {

    @FXML
    protected void onViewChildren() {
        openChildrenWindow();
    }

    @FXML
    protected void onViewParents() {
        openParentsWindow();
    }

    @FXML
    protected void onSettingsButtonClick() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Настройка подключения");
        alert.setHeaderText("Настройка завершена");
        alert.setContentText("Application connected to " + URL + " with username=" + USER + " and password=" + PASSWORD);
        alert.showAndWait();
    }

    private void openChildrenWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("children.fxml"));
            VBox childrenView = loader.load();
            Stage childrenStage = new Stage();
            String css = getClass().getResource("/css/styles.css").toExternalForm();
            childrenView.getStylesheets().add(css);
            childrenStage.setTitle("Таблица детей");
            childrenStage.initModality(Modality.APPLICATION_MODAL);
            childrenStage.setScene(new Scene(childrenView));
            childrenStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно таблицы детей.");
        }
    }

    private void openParentsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("parents.fxml"));
            VBox parentsView = loader.load();
            Stage parentsStage = new Stage();
            String css = getClass().getResource("/css/styles.css").toExternalForm();
            parentsView.getStylesheets().add(css);
            parentsStage.setTitle("Таблица родителей");
            parentsStage.initModality(Modality.APPLICATION_MODAL);
            parentsStage.setScene(new Scene(parentsView));
            parentsStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно таблицы родителей.");
        }
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}