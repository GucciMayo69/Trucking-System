package fxControllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.CurrentUser;
import utils.DbUtils;

import java.io.IOException;
import java.sql.SQLException;

public class LoginPage {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public CheckBox isManagerCheck;

    public void initialize() {
        Platform.runLater(() -> loginField.requestFocus());
        isManagerCheck.setSelected(false);
    }

    public void validate() throws IOException, SQLException {
        CurrentUser currentUser = CurrentUser.getInstance();
        User user = DbUtils.validateUser(loginField.getText(), passwordField.getText(), isManagerCheck.isSelected());
        if(isManagerCheck.isSelected()) currentUser.setManager(true);
        currentUser.setUser(user);
        if (user != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/main-page.fxml"));
            Parent parent = fxmlLoader.load();

            MainPage mainPage = fxmlLoader.getController();
            mainPage.setInfo(user);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("FreightSys");
            stage.setScene(scene);
            stage.show();
        } else {
            alertDialog("No such user", "User error");
        }
    }

    public void register() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/registration-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("FreightSys");
        stage.setScene(scene);
        stage.show();
    }

    public static void alertDialog(String message, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
