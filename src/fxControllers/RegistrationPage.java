package fxControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Driver;
import model.Manager;
import utils.DbUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegistrationPage implements Initializable {
    @FXML public PasswordField passwordField;
    @FXML public PasswordField rePasswordField;
    @FXML public DatePicker employmentDateField;
    @FXML public DatePicker medCertDateField;
    @FXML public DatePicker birthDateField;
    @FXML public RadioButton radioDriver;
    @FXML public RadioButton radioManager;
    @FXML public ToggleGroup userType;
    @FXML public TextField loginField;
    @FXML public TextField nameField;
    @FXML public TextField surnameField;
    @FXML public TextField managerEmailField;
    @FXML public TextField phoneNumField;
    @FXML public TextField medCertNumField;
    @FXML public TextField driverLicenseField;
    @FXML public CheckBox isAdminCheck;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radioDriver.setSelected(true);
        disableFields();
    }

    public void createNewUser() throws IOException, SQLException {
        Connection connection = DbUtils.connectToDb();
        String insertDriver = "INSERT INTO drivers(`login`, `password`, `name`, `surname`, `birth_date`, `med_date`, `med_number`, `driver_license`, `phone_num`) VALUES (?,?,?,?,?,?,?,?,?)";
        String insertManager = "INSERT INTO managers(`login`, `password`, `name`, `surname`, `birth_date`, `phone_num`, `email`, `employment_date`, `is_admin`) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement;
        if (radioDriver.isSelected()) {
            preparedStatement = connection.prepareStatement(insertDriver);
            Driver driver = new Driver(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), LocalDate.parse(birthDateField.getValue().toString()), phoneNumField.getText(), LocalDate.parse(medCertDateField.getValue().toString()), medCertNumField.getText(), driverLicenseField.getText());
            preparedStatement.setString(1, driver.getLogin());
            preparedStatement.setString(2, driver.getPassword());
            preparedStatement.setString(3, driver.getName());
            preparedStatement.setString(4, driver.getSurname());
            preparedStatement.setDate(5, Date.valueOf(driver.getBirthDate()));
            preparedStatement.setDate(6, Date.valueOf(driver.getMedCertificateDate()));
            preparedStatement.setString(7, driver.getMedCertificateNumber());
            preparedStatement.setString(8, driver.getDriverLicense());
            preparedStatement.setString(9, driver.getPhoneNum());

            preparedStatement.execute();
        } else {
            preparedStatement = connection.prepareStatement(insertManager);
            Manager manager = new Manager(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(),
                    LocalDate.parse(birthDateField.getValue().toString()), phoneNumField.getText(), managerEmailField.getText(),
                    LocalDate.parse(employmentDateField.getValue().toString()), isAdminCheck.isSelected());
            preparedStatement.setString(1, manager.getLogin());
            preparedStatement.setString(2, manager.getPassword());
            preparedStatement.setString(3, manager.getName());
            preparedStatement.setString(4, manager.getSurname());
            preparedStatement.setDate(5, Date.valueOf(manager.getBirthDate()));
            preparedStatement.setString(6, manager.getPhoneNum());
            preparedStatement.setString(7, manager.getEmail());
            preparedStatement.setDate(8, Date.valueOf(manager.getEmploymentDate()));
            preparedStatement.setBoolean(9, manager.isAdmin());

            preparedStatement.execute();
        }
        DbUtils.disconnect(connection, preparedStatement);
        returnToPrev();
    }

    public void returnToPrev() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("FreightSys");
        stage.setScene(scene);
        stage.show();
    }

    public void disableFields() {
        if (radioDriver.isSelected()) {
            medCertDateField.setDisable(false);
            medCertNumField.setDisable(false);
            driverLicenseField.setDisable(false);
            managerEmailField.setDisable(true);
            employmentDateField.setDisable(true);
            isAdminCheck.setDisable(true);
        } else {
            medCertDateField.setDisable(true);
            medCertNumField.setDisable(true);
            driverLicenseField.setDisable(true);
            managerEmailField.setDisable(false);
            employmentDateField.setDisable(false);
            isAdminCheck.setDisable(false);
        }
    }
}
