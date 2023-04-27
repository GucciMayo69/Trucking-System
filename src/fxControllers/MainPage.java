package fxControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.*;
import model.Driver;
import utils.CurrentUser;
import utils.DbUtils;
import utils.SavedLists;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class MainPage {
    @FXML
    public ComboBox<TyreType> tyreTypeField = new ComboBox<>();
    @FXML
    public ComboBox<CargoType> cargoTypeComboBox = new ComboBox<>();
    @FXML
    public ListView<Checkpoint> checkpointDetailListView = new ListView<>();
    @FXML
    public ListView<Manager> respManDetailListView = new ListView<>();
    @FXML
    public TableView<Destination> driverOrderTable = new TableView<>();
    @FXML
    public TableView<Destination> destinationTable = new TableView<>();
    @FXML
    public TableView<Manager> ManagerListField = new TableView<>();
    @FXML
    public TableView<Driver> driverListField = new TableView<>();
    @FXML
    public TableView<Truck> truckListField = new TableView<>();
    @FXML
    public TableView<Cargo> CargoTable = new TableView<>();
    @FXML
    public TableColumn<Destination, String> colDestId;
    @FXML
    public TableColumn<Destination, String> colDestStartCity;
    @FXML
    public TableColumn<Destination, String> colDestEndCity;
    @FXML
    public TableColumn<Destination, String> colDestTruck = new TableColumn<>();
    @FXML
    public TableColumn<Destination, String> colDestDriver = new TableColumn<>();
    @FXML
    public TableColumn<Destination, String> colDestCargo = new TableColumn<>();
    @FXML
    public TableColumn<Destination, String> colDestCreated;
    @FXML
    public TableColumn<Destination, String> colDestUpdated;
    @FXML
    public TableColumn<Destination, String> colDestCompleted;
    @FXML
    public TabPane mainPageTabs = new TabPane();
    @FXML
    public Tab destinationManTab;
    @FXML
    public Tab driverOrdersTab;
    @FXML
    public Tab managerManTab;
    @FXML
    public Tab driverManTab;
    @FXML
    public Tab cargoManTab;
    @FXML
    public Tab truckManTab;
    @FXML
    public DatePicker managerEmploymentDateField;
    @FXML
    public DatePicker managerBirthDateField;
    @FXML
    public DatePicker driverBirthDateField;
    @FXML
    public DatePicker driverMedDateField;
    @FXML
    public TextField modelField;
    @FXML
    public TextField yearField;
    @FXML
    public TextField odometerField;
    @FXML
    public TextField capacityTankField;
    @FXML
    public TextField markeField;
    @FXML
    public TextField driverLoginField;
    @FXML
    public TextField driverPasswordField;
    @FXML
    public TextField driverNameField;
    @FXML
    public TextField driverSurnameField;
    @FXML
    public TextField driverLicenseField;
    @FXML
    public TextField driverPhoneNumField;
    @FXML
    public TextField driverMedNumberField;
    @FXML
    public TextField managerLoginField;
    @FXML
    public TextField managerPasswordField;
    @FXML
    public TextField managerNameField;
    @FXML
    public TextField managerSurnameField;
    @FXML
    public TextField managerPhoneNumberField;
    @FXML
    public TextField managerEmailField;
    @FXML
    public TextField cargoTitleField;
    @FXML
    public TextField cargoWeightField;
    @FXML
    public TextField cargoCustomerField;
    @FXML
    public TextField filterDestField = new TextField();
    @FXML
    public CheckBox managerIsAdminCheck;
    @FXML
    public Button driverDeleteButton;
    @FXML
    public Button driverUpdateButton;
    @FXML
    public Button driverAddButton;
    @FXML
    public Button managerDeleteButton;
    @FXML
    public Button managerUpdateButton;
    @FXML
    public Button managerAddButton;
    @FXML
    public Button deleteDestinationButton;
    @FXML
    public Button toDestDetailsMenuButton;

    public void setInfo(User user) {
    }

    public void initialize() throws SQLException {
        colDestTruck.setCellValueFactory(new PropertyValueFactory<>("truckName"));
        colDestDriver.setCellValueFactory(new PropertyValueFactory<>("driverName"));
        colDestCargo.setCellValueFactory(new PropertyValueFactory<>("cargoName"));

        UpdateDestinationTable();

        SavedLists savedLists = SavedLists.getInstance();
        ObservableList<Destination> destList = FXCollections.observableArrayList(savedLists.getDestList());
        FilteredList<Destination> filteredData = new FilteredList<>(destList, b -> true);

        filterDestField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(destination -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (destination.getStartCityProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (destination.getEndCityProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (destination.getTruckName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (destination.getDriverName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (destination.getCargoName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (destination.getIsCompletedProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else return false;
        }));
        SortedList<Destination> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(destinationTable.comparatorProperty());
        destinationTable.setItems(sortedData);

        ObservableList<TyreType> tyreTypeList = FXCollections.observableArrayList(TyreType.values());
        tyreTypeField.setItems(tyreTypeList);
        ObservableList<CargoType> cargoTypeList = FXCollections.observableArrayList(CargoType.values());
        cargoTypeComboBox.setItems(cargoTypeList);
        UpdateTruckTable();
        UpdateDriverTable();
        UpdateManagerTable();
        UpdateCargoTable();
        UpdateOrdersTable();

        CurrentUser currentUser = CurrentUser.getInstance();
        User user = currentUser.getUser();
        if (!currentUser.isManager) {
            mainPageTabs.getTabs().remove(truckManTab);
            mainPageTabs.getTabs().remove(cargoManTab);
            mainPageTabs.getTabs().remove(destinationManTab);
            mainPageTabs.getTabs().remove(managerManTab);
            ;
        }
        if (!user.isAdmin()) {
            deleteDestinationButton.setVisible(false);
            driverLoginField.setDisable(true);
            driverPasswordField.setDisable(true);
            driverNameField.setDisable(true);
            driverSurnameField.setDisable(true);
            driverBirthDateField.setDisable(true);
            driverPhoneNumField.setDisable(true);
            driverMedDateField.setDisable(true);
            driverMedNumberField.setDisable(true);
            driverLicenseField.setDisable(true);
            driverUpdateButton.setDisable(true);
            driverAddButton.setDisable(true);
            driverDeleteButton.setDisable(true);
            managerLoginField.setDisable(true);
            managerPasswordField.setDisable(true);
            managerNameField.setDisable(true);
            managerSurnameField.setDisable(true);
            managerBirthDateField.setDisable(true);
            managerPhoneNumberField.setDisable(true);
            managerEmailField.setDisable(true);
            managerEmploymentDateField.setDisable(true);
            managerIsAdminCheck.setDisable(true);
            managerUpdateButton.setDisable(true);
            managerAddButton.setDisable(true);
            managerDeleteButton.setDisable(true);
        }
        if (currentUser.isManager) {
            mainPageTabs.getTabs().remove(driverOrdersTab);
        }
    }

    public void createTruck() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String insertTruck = "INSERT INTO trucks(`marke`, `model`, `year`, `odometer`, `fuel_tank_capacity`, `tyre_type`) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertTruck);
        Truck truck = new Truck(markeField.getText(), modelField.getText(), Integer.parseInt(yearField.getText()), Double.parseDouble(odometerField.getText()), Double.parseDouble(capacityTankField.getText()), tyreTypeField.getValue());
        preparedStatement.setString(1, truck.getMarke());
        preparedStatement.setString(2, truck.getModel());
        preparedStatement.setInt(3, truck.getYear());
        preparedStatement.setDouble(4, truck.getOdometer());
        preparedStatement.setDouble(5, truck.getFuelTankCapacity());
        preparedStatement.setInt(6, truck.getTyreType().ordinal());

        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateTruckTable();
    }

    public void UpdateTruckTable() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readTruck = "SELECT * FROM `trucks`";
        PreparedStatement preparedStatement = connection.prepareStatement(readTruck);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Truck> truckList = FXCollections.observableArrayList();
        while (rs.next()) {
            Truck truck = new Truck(
                    rs.getInt("id"),
                    rs.getString("marke"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getDouble("odometer"),
                    rs.getDouble("fuel_tank_capacity"),
                    TyreType.values()[rs.getInt("tyre_type")]
            );
            truckList.add(truck);
        }
        truckListField.setItems(truckList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void DeleteTruck() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedTruck = truckListField.getSelectionModel().getSelectedItem().getId();

        String deleteTruck = "DELETE FROM trucks WHERE id=" + selectedTruck;
        PreparedStatement preparedStatement = connection.prepareStatement(deleteTruck);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateTruckTable();
    }

    public void GetSelectedTruck() {
        Truck selectedTruck = truckListField.getSelectionModel().getSelectedItem();

        if (selectedTruck == null) {
            return;
        }
        markeField.setText(selectedTruck.getMarke());
        modelField.setText(selectedTruck.getModel());
        yearField.setText(Integer.toString(selectedTruck.getYear()));
        odometerField.setText(Double.toString(selectedTruck.getOdometer()));
        capacityTankField.setText(Double.toString(selectedTruck.getFuelTankCapacity()));
        tyreTypeField.getSelectionModel().select(selectedTruck.getTyreType());
    }

    public void UpdateTruck() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedTruck = truckListField.getSelectionModel().getSelectedItem().getId();

        String updateTruck = "UPDATE trucks SET" +
                " marke= '" + markeField.getText() +
                "', model= '" + modelField.getText() +
                "', year= " + Integer.parseInt(yearField.getText()) +
                ", odometer= " + Double.parseDouble(odometerField.getText()) +
                ", fuel_tank_capacity= " + Double.parseDouble(capacityTankField.getText()) +
                ", tyre_type= " + tyreTypeField.getValue().ordinal() +
                " WHERE id= " + selectedTruck;

        PreparedStatement preparedStatement = connection.prepareStatement(updateTruck);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateTruckTable();
    }

    public void createDriver() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String insertDriver = "INSERT INTO drivers(`login`, `password`, `name`, `surname`, `birth_date`, `phone_num`, `med_date`, `med_number`, `driver_license`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDriver);
        Driver driver = new Driver(driverLoginField.getText(), driverPasswordField.getText(), driverNameField.getText(), driverSurnameField.getText(), LocalDate.parse(driverBirthDateField.getValue().toString()),
                driverPhoneNumField.getText(), LocalDate.parse(driverMedDateField.getValue().toString()), driverMedNumberField.getText(), driverLicenseField.getText());
        preparedStatement.setString(1, driver.getLogin());
        preparedStatement.setString(2, driver.getPassword());
        preparedStatement.setString(3, driver.getName());
        preparedStatement.setString(4, driver.getSurname());
        preparedStatement.setDate(5, Date.valueOf(driver.getBirthDate()));
        preparedStatement.setString(6, driver.getPhoneNum());
        preparedStatement.setDate(7, Date.valueOf(driver.getMedCertificateDate()));
        preparedStatement.setString(8, driver.getMedCertificateNumber());
        preparedStatement.setString(9, driver.getDriverLicense());

        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateDriverTable();
    }

    public void UpdateDriverTable() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readDriver = "SELECT * FROM `drivers`";
        PreparedStatement preparedStatement = connection.prepareStatement(readDriver);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Driver> driverList = FXCollections.observableArrayList();
        while (rs.next()) {
            Driver driver = new Driver(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getDate("birth_date").toLocalDate(),
                    rs.getString("phone_num"),
                    rs.getDate("med_date").toLocalDate(),
                    rs.getString("med_number"),
                    rs.getString("driver_license")
            );
            driverList.add(driver);
        }
        driverListField.setItems(driverList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void GetSelectedDriver() {
        CurrentUser currentUser = CurrentUser.getInstance();
        User user = currentUser.getUser();
        Driver selectedDriver = driverListField.getSelectionModel().getSelectedItem();
        if (selectedDriver == null) {
            return;
        }
        if (user.isAdmin()) {
            driverLoginField.setText(selectedDriver.getLogin());
            driverPasswordField.setText(selectedDriver.getPassword());
            driverNameField.setText(selectedDriver.getName());
            driverSurnameField.setText(selectedDriver.getSurname());
            driverBirthDateField.setValue(selectedDriver.getBirthDate());
            driverPhoneNumField.setText(selectedDriver.getPhoneNum());
            driverMedDateField.setValue(selectedDriver.getMedCertificateDate());
            driverMedNumberField.setText(selectedDriver.getMedCertificateNumber());
            driverLicenseField.setText(selectedDriver.getDriverLicense());
        } else if (!currentUser.isManager) {
            if (Objects.equals(selectedDriver.getLogin(), user.getLogin())) {
                driverLoginField.setText(selectedDriver.getLogin());
                driverPasswordField.setText(selectedDriver.getPassword());
                driverNameField.setText(selectedDriver.getName());
                driverSurnameField.setText(selectedDriver.getSurname());
                driverBirthDateField.setValue(selectedDriver.getBirthDate());
                driverPhoneNumField.setText(selectedDriver.getPhoneNum());
                driverMedDateField.setValue(selectedDriver.getMedCertificateDate());
                driverMedNumberField.setText(selectedDriver.getMedCertificateNumber());
                driverLicenseField.setText(selectedDriver.getDriverLicense());
                driverLoginField.setDisable(false);
                driverPhoneNumField.setDisable(false);
                driverMedDateField.setDisable(false);
                driverMedNumberField.setDisable(false);
                driverLicenseField.setDisable(false);
                driverUpdateButton.setDisable(false);
            } else {
                driverLoginField.setDisable(true);
                driverLoginField.clear();
                driverPhoneNumField.setDisable(true);
                driverPhoneNumField.clear();
                driverMedDateField.setDisable(true);
                driverMedDateField.setValue(null);
                driverMedNumberField.setDisable(true);
                driverMedNumberField.clear();
                driverLicenseField.setDisable(true);
                driverLicenseField.clear();
                driverUpdateButton.setDisable(true);
            }
        }
    }

    public void DeleteDriver() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedDriver = driverListField.getSelectionModel().getSelectedItem().getId();

        String deleteDriver = "DELETE FROM drivers WHERE id=" + Integer.toString(selectedDriver);
        PreparedStatement preparedStatement = connection.prepareStatement(deleteDriver);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateDriverTable();
    }

    public void UpdateDriver() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedDriver = driverListField.getSelectionModel().getSelectedItem().getId();

        String updateDriver = "UPDATE drivers SET" +
                " login= '" + driverLoginField.getText() +
                "', password= '" + driverPasswordField.getText() +
                "', name= '" + driverNameField.getText() +
                "', surname= '" + driverSurnameField.getText() +
                "', birth_date= '" + driverBirthDateField.getValue().toString() +
                "', phone_num= '" + driverPhoneNumField.getText() +
                "', med_date= '" + driverMedDateField.getValue().toString() +
                "', med_number= '" + driverMedNumberField.getText() +
                "', driver_license= '" + driverLicenseField.getText() +
                "' WHERE id= " + selectedDriver;

        PreparedStatement preparedStatement = connection.prepareStatement(updateDriver);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateDriverTable();
    }

    public void createManager() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String insertManager = "INSERT INTO managers(`login`, `password`, `name`, `surname`, `birth_date`, `phone_num`, `email`, `employment_date`, `is_admin`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertManager);
        Manager manager = new Manager(managerLoginField.getText(), managerPasswordField.getText(), managerNameField.getText(), managerSurnameField.getText(), LocalDate.parse(managerBirthDateField.getValue().toString()),
                managerPhoneNumberField.getText(), managerEmailField.getText(), LocalDate.parse(managerEmploymentDateField.getValue().toString()), Boolean.parseBoolean(managerIsAdminCheck.getText()));
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
        DbUtils.disconnect(connection, preparedStatement);

        UpdateManagerTable();
    }

    public void UpdateManagerTable() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readManager = "SELECT * FROM `managers`";
        PreparedStatement preparedStatement = connection.prepareStatement(readManager);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Manager> managerList = FXCollections.observableArrayList();
        while (rs.next()) {
            Manager manager = new Manager(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getDate("birth_date").toLocalDate(),
                    rs.getString("phone_num"),
                    rs.getString("email"),
                    rs.getDate("employment_date").toLocalDate(),
                    rs.getBoolean("is_admin")
            );
            managerList.add(manager);
        }
        ManagerListField.setItems(managerList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void GetSelectedManager() {
        CurrentUser currentUser = CurrentUser.getInstance();
        User user = currentUser.getUser();
        Manager selectedManager = ManagerListField.getSelectionModel().getSelectedItem();
        if (user.isAdmin()) {
            managerLoginField.setText(selectedManager.getLogin());
            managerPasswordField.setText(selectedManager.getPassword());
            managerNameField.setText(selectedManager.getName());
            managerSurnameField.setText(selectedManager.getSurname());
            managerBirthDateField.setValue(selectedManager.getBirthDate());
            managerPhoneNumberField.setText(selectedManager.getPhoneNum());
            managerEmailField.setText(selectedManager.getEmail());
            managerEmploymentDateField.setValue(selectedManager.getEmploymentDate());
            managerIsAdminCheck.setSelected(selectedManager.isAdmin());
        } else if (currentUser.isManager && !user.isAdmin()) {
            if (Objects.equals(selectedManager.getLogin(), user.getLogin())) {
                managerLoginField.setText(selectedManager.getLogin());
                managerPasswordField.setText(selectedManager.getPassword());
                managerNameField.setText(selectedManager.getName());
                managerSurnameField.setText(selectedManager.getSurname());
                managerBirthDateField.setValue(selectedManager.getBirthDate());
                managerPhoneNumberField.setText(selectedManager.getPhoneNum());
                managerEmailField.setText(selectedManager.getEmail());
                managerEmploymentDateField.setValue(selectedManager.getEmploymentDate());
                managerIsAdminCheck.setSelected(selectedManager.isAdmin());
                managerLoginField.setDisable(false);
                managerPhoneNumberField.setDisable(false);
                managerEmailField.setDisable(false);
                managerUpdateButton.setDisable(false);
            } else {
                managerLoginField.setDisable(true);
                managerLoginField.clear();
                managerPasswordField.clear();
                managerNameField.clear();
                managerSurnameField.clear();
                managerBirthDateField.setValue(null);
                managerPhoneNumberField.setDisable(true);
                managerPhoneNumberField.clear();
                managerEmailField.setDisable(true);
                managerEmailField.clear();
                managerEmploymentDateField.setValue(null);
                managerUpdateButton.setDisable(true);
            }
        }
    }

    public void deleteManager() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedManager = ManagerListField.getSelectionModel().getSelectedItem().getId();

        String deleteManager = "DELETE FROM managers WHERE id=" + Integer.toString(selectedManager);
        PreparedStatement preparedStatement = connection.prepareStatement(deleteManager);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateDriverTable();
    }

    public void UpdateManager() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedManager = ManagerListField.getSelectionModel().getSelectedItem().getId();

        String updateManager = "UPDATE managers SET" +
                " login= '" + managerLoginField.getText() +
                "', password= '" + managerPasswordField.getText() +
                "', name= '" + managerNameField.getText() +
                "', surname= '" + managerSurnameField.getText() +
                "', birth_date= '" + managerBirthDateField.getValue().toString() +
                "', phone_num= '" + managerPhoneNumberField.getText() +
                "', email=  '" + managerEmailField.getText() +
                "', employment_date= '" + managerEmploymentDateField.getValue().toString() +
                "', is_admin= " + managerIsAdminCheck.isSelected() +
                " WHERE id= " + selectedManager;

        PreparedStatement preparedStatement = connection.prepareStatement(updateManager);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateManagerTable();
    }

    public void goToForum() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/forum-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("FreightSys");
        stage.setScene(scene);
        stage.show();
    }

    public void createCargo() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String insertCargo = "INSERT INTO cargo(`title`, `date_created`, `date_updated`, `weight`, `cargo_type`, `customer`) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertCargo);
        Cargo cargo = new Cargo(cargoTitleField.getText(), LocalDate.now(), LocalDate.now(), Double.parseDouble(cargoWeightField.getText()), cargoTypeComboBox.getValue(), cargoCustomerField.getText());
        preparedStatement.setString(1, cargo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(cargo.getDateCreated()));
        preparedStatement.setDate(3, Date.valueOf(cargo.getDateUpdated()));
        preparedStatement.setDouble(4, cargo.getWeight());
        preparedStatement.setInt(5, cargo.getCargoType().ordinal());
        preparedStatement.setString(6, cargo.getCustomer());

        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateCargoTable();
    }

    public void UpdateCargoTable() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readCargo = "SELECT * FROM `cargo`";
        PreparedStatement preparedStatement = connection.prepareStatement(readCargo);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Cargo> cargoList = FXCollections.observableArrayList();
        while (rs.next()) {
            Cargo cargo = new Cargo(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getDate("date_created").toLocalDate(),
                    rs.getDate("date_updated").toLocalDate(),
                    rs.getDouble("weight"),
                    CargoType.values()[rs.getInt("cargo_type")],
                    rs.getString("customer")
            );
            cargoList.add(cargo);
        }
        CargoTable.setItems(cargoList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void getSelectedCargo() {
        Cargo selectedCargo = CargoTable.getSelectionModel().getSelectedItem();

        if (selectedCargo == null) {
            return;
        }
        cargoTitleField.setText(selectedCargo.getTitle());
        cargoWeightField.setText(String.valueOf(selectedCargo.getWeight()));
        cargoTypeComboBox.getSelectionModel().select(selectedCargo.getCargoType());
        cargoCustomerField.setText(selectedCargo.getCustomer());
    }

    public void deleteCargo() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedCargo = CargoTable.getSelectionModel().getSelectedItem().getId();

        String deleteCargo = "DELETE FROM cargo WHERE id=" + selectedCargo;
        PreparedStatement preparedStatement = connection.prepareStatement(deleteCargo);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateCargoTable();
    }

    public void updateCargo() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedCargo = CargoTable.getSelectionModel().getSelectedItem().getId();

        String updateCargo = "UPDATE cargo SET" +
                " title= '" + cargoTitleField.getText() +
                "', date_updated= '" + LocalDate.now() +
                "', weight= '" + Double.parseDouble(cargoWeightField.getText()) +
                "', cargo_type= " + cargoTypeComboBox.getValue().ordinal() +
                ", customer= '" + cargoCustomerField.getText() +
                "' WHERE id= " + Integer.toString(selectedCargo);

        PreparedStatement preparedStatement = connection.prepareStatement(updateCargo);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateCargoTable();
    }

    public void CreateDestination() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPage.class.getResource("../view/city-selection-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) deleteDestinationButton.getScene().getWindow();
        stage.setTitle("City Selection");
        stage.setScene(scene);
        stage.show();
    }

    public void UpdateDestinationTable() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readDestination = "SELECT d.id, start_city, end_city, t.marke AS truck_name, drivers.surname" +
                " AS driver_name, c.title AS cargo_name, d.date_created, d.date_updated, is_completed FROM destinations d JOIN trucks t " +
                "ON d.truck_id = t.id JOIN drivers ON d.driver_id = drivers.id JOIN cargo c ON d.cargo_id = c.id";
        PreparedStatement preparedStatement = connection.prepareStatement(readDestination);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Destination> destinationList = FXCollections.observableArrayList();
        while (rs.next()) {
            Destination destination = new Destination(
                    rs.getInt("id"),
                    Cities.values()[rs.getInt("start_city")],
                    Cities.values()[rs.getInt("end_city")],
                    rs.getString("truck_name"),
                    rs.getString("driver_name"),
                    rs.getString("cargo_name"),
                    rs.getDate("date_created").toLocalDate(),
                    rs.getDate("date_updated").toLocalDate(),
                    rs.getBoolean("is_completed")
            );
            destinationList.add(destination);
        }
        SavedLists savedLists = SavedLists.getInstance();
        savedLists.setDestList(destinationList);

        destinationTable.setItems(destinationList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void DeleteDestination() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedDestination = destinationTable.getSelectionModel().getSelectedItem().getId();

        String deleteTruck = "DELETE FROM destinations WHERE id=" + selectedDestination;
        PreparedStatement preparedStatement = connection.prepareStatement(deleteTruck);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateDestinationTable();
    }

    public int GetSelectedDestination() {
        if (destinationTable.getSelectionModel().getSelectedItem() != null) {
            return destinationTable.getSelectionModel().getSelectedItem().getId();
        }
        return -1;
    }

    public void GoToDestDetailsMenu() throws IOException, SQLException {
        int selectedDest = GetSelectedDestination();

        FXMLLoader fxmlLoader = new FXMLLoader(MainPage.class.getResource("../view/destination-details-page.fxml"));
        Parent root = fxmlLoader.load();
        MainPage controller = fxmlLoader.getController();
        controller.UpdateCheckpointDetailListView(selectedDest);
        controller.UpdateRespManDetailListView(selectedDest);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Destination Details Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void UpdateCheckpointDetailListView(int selectedDest) throws SQLException {
        Connection connection = DbUtils.connectToDb();

        String readCheckpoints = "SELECT c.id, c.title FROM checkpoints c JOIN assigned_checkpoints ac on c.id = ac.checkpoint_id WHERE ac.destination_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(readCheckpoints);
        preparedStatement.setInt(1, selectedDest);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Checkpoint> checkpointList = FXCollections.observableArrayList();
        while (rs.next()) {
            Checkpoint checkpoint = new Checkpoint(
                    rs.getInt("id"),
                    rs.getString("title")
            );
            checkpointList.add(checkpoint);
        }

        checkpointDetailListView.setItems(checkpointList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void UpdateRespManDetailListView(int selectedDest) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readManagers = "SELECT m.id, m.surname FROM managers m JOIN responsible_managers rm on m.id = rm.manager_id WHERE rm.destination_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(readManagers);
        preparedStatement.setInt(1, selectedDest);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Manager> managerList = FXCollections.observableArrayList();
        while (rs.next()) {
            Manager manager = new Manager(
                    rs.getInt("id"),
                    rs.getString("surname")
            );
            managerList.add(manager);
        }
        respManDetailListView.setItems(managerList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void UpdateOrdersTable() throws SQLException {
        CurrentUser currentUser = CurrentUser.getInstance();
        User user = currentUser.getUser();
        Connection connection = DbUtils.connectToDb();
        String readDestination = "SELECT dest.*, dr.id, dr.login FROM destinations dest LEFT JOIN drivers dr on dest.driver_id = dr.id WHERE dr.id = ? AND dest.is_completed = 0";
        PreparedStatement preparedStatement = connection.prepareStatement(readDestination);
        preparedStatement.setInt(1, user.getId());
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Destination> destinationList = FXCollections.observableArrayList();
        while (rs.next()) {
            Destination destination = new Destination(
                    rs.getInt("id"),
                    Cities.values()[rs.getInt("start_city")],
                    Cities.values()[rs.getInt("end_city")],
                    rs.getInt("truck_id"),
                    rs.getInt("driver_id"),
                    rs.getInt("cargo_id"),
                    rs.getDate("date_created").toLocalDate(),
                    rs.getDate("date_updated").toLocalDate(),
                    rs.getBoolean("is_completed")
            );
            destinationList.add(destination);
        }
        driverOrderTable.setItems(destinationList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void completeOrder() throws SQLException {
        if (driverOrderTable.getSelectionModel().getSelectedItems() != null) {
            int selectedDestination = driverOrderTable.getSelectionModel().getSelectedItem().getId();
            Connection connection = DbUtils.connectToDb();

            String alterDestination = "UPDATE destinations SET is_completed = 1 WHERE id =" + selectedDestination;
            PreparedStatement preparedStatement = connection.prepareStatement(alterDestination);
            preparedStatement.execute();
            DbUtils.disconnect(connection, preparedStatement);
            UpdateOrdersTable();
        }
    }

    public void OpenStatistics() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readDestinations = "SELECT start_city, end_city FROM destinations";
        PreparedStatement preparedStatement = connection.prepareStatement(readDestinations);
        ResultSet rs = preparedStatement.executeQuery();

        int[] startCityOccurrences = new int[Cities.values().length];
        Arrays.fill(startCityOccurrences, 0);
        int[] endCityOccurrences = new int[Cities.values().length];
        Arrays.fill(endCityOccurrences, 0);

        while (rs.next()) {
            startCityOccurrences[rs.getInt("start_city")]++;
            endCityOccurrences[rs.getInt("end_city")]++;
        }

        DbUtils.disconnect(connection, preparedStatement);
        ObservableList<PieChart.Data> pieData1 = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieData2 = FXCollections.observableArrayList();
        for(int i = 0; i < Cities.values().length; i++) {
            if(startCityOccurrences[i] != 0) pieData1.add(new PieChart.Data(Cities.values()[i].name(), startCityOccurrences[i]));
            if(endCityOccurrences[i] != 0) pieData2.add(new PieChart.Data(Cities.values()[i].name(), endCityOccurrences[i]));
        }
        PieChart pChart1 = new PieChart(pieData1);
        PieChart pChart2 = new PieChart(pieData2);
        pChart1.setTitle("Starting Cities");
        pChart2.setTitle("Ending Cities");
        HBox hBox = new HBox(pChart1, pChart2);
        hBox.setSpacing(25);
        Scene scene = new Scene(hBox, 1000, 400);
        Stage stage = new Stage();
        stage.setTitle("Pie Charts");
        stage.setScene(scene);
        stage.show();
    }
}
