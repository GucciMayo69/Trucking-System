package fxControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Driver;
import model.*;
import utils.CurrentUser;
import utils.DbUtils;
import utils.SavedLists;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DestinationSelectionPage implements Initializable {

    @FXML public ListView<Checkpoint> checkpointListView = new ListView<>();
    @FXML public ListView<Manager> respManagersListView = new ListView<>();
    @FXML public ListView<Truck> truckListView = new ListView<>();
    @FXML public ListView<Driver> driverListView = new ListView<>();
    @FXML public ListView<Cargo> cargoListView = new ListView<>();
    @FXML public ComboBox<Cities> startCityComboBox = new ComboBox<>();
    @FXML public ComboBox<Cities> endCityComboBox = new ComboBox<>();
    @FXML public Button toCheckpointWindowButton;
    @FXML public Button toRespManagersWindowButton;
    @FXML public Button toTrucksWindowButton = new Button();
    @FXML public Button toDriversWindowButton = new Button();
    @FXML public Button toCargoWindowButton = new Button();
    @FXML public Button toMainPageButton = new Button();

    SavedLists savedLists = SavedLists.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Cities> citiesList = FXCollections.observableArrayList(Cities.values());
        startCityComboBox.setItems(citiesList);
        endCityComboBox.setItems(citiesList);
        try {
            UpdateCheckpointsListView();
            UpdateRespManagersListView();
            UpdateTruckListView();
            UpdateDriverListView();
            UpdateCargoListView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            toTrucksWindowButton.disableProperty().bind(checkpointListView.getSelectionModel().selectedItemProperty().isNull());
            toDriversWindowButton.disableProperty().bind(truckListView.getSelectionModel().selectedItemProperty().isNull());
            toCargoWindowButton.disableProperty().bind(driverListView.getSelectionModel().selectedItemProperty().isNull());
            toMainPageButton.disableProperty().bind(cargoListView.getSelectionModel().selectedItemProperty().isNull());
    }

    static Destination destination = new Destination();

    public void CreateDestination() throws SQLException, IOException {
        if (cargoListView.getSelectionModel().getSelectedItem() != null) {
            destination.setCargoId(cargoListView.getSelectionModel().getSelectedItem().getId());
        }
        destination.setDateCreated(LocalDate.now());
        destination.setDateUpdated(LocalDate.now());
        destination.setCompleted(false);

        Connection connection = DbUtils.connectToDb();
        String insertDestination = "INSERT INTO destinations(`start_city`, `end_city`, `truck_id`, `driver_id`, `cargo_id`, `date_created`, `date_updated`, `is_completed`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDestination, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, destination.getStartCity().ordinal());
        preparedStatement.setInt(2, destination.getEndCity().ordinal());
        preparedStatement.setInt(3, destination.getTruckId());
        preparedStatement.setInt(4, destination.getDriverId());
        preparedStatement.setInt(5, destination.getCargoId());
        preparedStatement.setDate(6, Date.valueOf(destination.getDateCreated()));
        preparedStatement.setDate(7, Date.valueOf(destination.getDateUpdated()));
        preparedStatement.setBoolean(8, destination.isCompleted());

        preparedStatement.execute();

        SavedLists savedLists = SavedLists.getInstance();
        int destId = 0;
        ResultSet rs = preparedStatement.getGeneratedKeys();
        while (rs.next()) {
            destId = rs.getInt(1);
        }
        savedLists.setDestId(destId);

        DbUtils.disconnect(connection, preparedStatement);

        CreateAssignedCheckpoints();
        CreateResponsibleManagers();

        FXMLLoader fxmlLoader = new FXMLLoader(DestinationSelectionPage.class.getResource("../view/main-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) toMainPageButton.getScene().getWindow();
        stage.setTitle("FreightSys");
        stage.setScene(scene);
        stage.show();
    }

    public void CreateAssignedCheckpoints() throws SQLException {
        List<Integer> cpList = savedLists.getCpList();
        int destId = savedLists.getDestId();
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement = null;
        for (Integer integer : cpList) {
            String insertCheckpoints = "INSERT INTO assigned_checkpoints(`destination_id`, `checkpoint_id`) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(insertCheckpoints);
            preparedStatement.setInt(1, destId);
            preparedStatement.setInt(2, integer);

            preparedStatement.execute();
        }
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void CreateResponsibleManagers() throws SQLException {
        List<Integer> respManList = savedLists.getRespManList();
        CurrentUser currentUser = CurrentUser.getInstance();
        User user = currentUser.getUser();
        int destId = savedLists.getDestId();
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement = null;
        if (respManList.isEmpty()) {
            String insertManagers = "INSERT INTO responsible_managers(`destination_id`, `manager_id`) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(insertManagers);
            preparedStatement.setInt(1, destId);
            preparedStatement.setInt(2, user.getId());

            preparedStatement.execute();
        } else {
            for (Integer integer : respManList) {
                String insertManagers = "INSERT INTO responsible_managers(`destination_id`, `manager_id`) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(insertManagers);
                preparedStatement.setInt(1, destId);
                preparedStatement.setInt(2, integer);

                preparedStatement.execute();
            }
        }
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void UpdateCheckpointsListView() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readCheckPoints = "SELECT id, title FROM `checkpoints`";
        PreparedStatement preparedStatement = connection.prepareStatement(readCheckPoints);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Checkpoint> checkpointList = FXCollections.observableArrayList();
        while (rs.next()) {
            Checkpoint checkpoint = new Checkpoint(
                    rs.getInt("id"),
                    rs.getString("title")
            );
            checkpointList.add(checkpoint);
        }
        checkpointListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        checkpointListView.setItems(checkpointList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void UpdateRespManagersListView() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readRespManagers = "SELECT id, surname FROM `managers`";
        PreparedStatement preparedStatement = connection.prepareStatement(readRespManagers);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Manager> managerList = FXCollections.observableArrayList();
        while (rs.next()) {
            Manager manager = new Manager(
                    rs.getInt("id"),
                    rs.getString("surname")
            );
            managerList.add(manager);
        }
        respManagersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        respManagersListView.setItems(managerList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void UpdateTruckListView() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readTrucks = "SELECT id, marke FROM `trucks`";
        PreparedStatement preparedStatement = connection.prepareStatement(readTrucks);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Truck> truckList = FXCollections.observableArrayList();
        while (rs.next()) {
            Truck truck = new Truck(
                    rs.getInt("id"),
                    rs.getString("marke")
            );
            truckList.add(truck);
        }

        truckListView.setItems(truckList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void UpdateDriverListView() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readDrivers = "SELECT id, surname FROM `drivers`";
        PreparedStatement preparedStatement = connection.prepareStatement(readDrivers);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Driver> driverList = FXCollections.observableArrayList();
        while (rs.next()) {
            Driver driver = new Driver(
                    rs.getInt("id"),
                    rs.getString("surname")
            );
            driverList.add(driver);
        }

        driverListView.setItems(driverList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void UpdateCargoListView() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readCargo = "SELECT id, title FROM `cargo`";
        PreparedStatement preparedStatement = connection.prepareStatement(readCargo);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Cargo> cargoList = FXCollections.observableArrayList();
        while (rs.next()) {
            Cargo cargo = new Cargo(
                    rs.getInt("id"),
                    rs.getString("title")
            );
            cargoList.add(cargo);
        }
        cargoListView.setItems(cargoList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void GoToRespManagersWindow() throws IOException {
        if (startCityComboBox.getValue() != null && endCityComboBox.getValue() != null &&
                startCityComboBox.getValue().equals(endCityComboBox.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid selection");
            alert.setContentText("Please select different cities");
            alert.showAndWait();
        } else if (startCityComboBox.getSelectionModel().isEmpty() || endCityComboBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No item selected");
            alert.setContentText("Please select an item from the list");
            alert.showAndWait();
        } else {
            destination.setStartCity(startCityComboBox.getValue());
            destination.setEndCity(endCityComboBox.getValue());

            FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/manager-selection-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) toRespManagersWindowButton.getScene().getWindow();
            stage.setTitle("Manager Selection Menu");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void GoToCheckpointWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/checkpoint-selection-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) toCheckpointWindowButton.getScene().getWindow();
        stage.setTitle("Checkpoint Selection Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void GoToTrucksWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/truck-selection-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) toTrucksWindowButton.getScene().getWindow();
        stage.setTitle("Truck Selection Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void GoToDriversWindow() throws IOException {
        if (truckListView.getSelectionModel().getSelectedItem() != null) {
            destination.setTruckId(truckListView.getSelectionModel().getSelectedItem().getId());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/driver-selection-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) toDriversWindowButton.getScene().getWindow();
        stage.setTitle("Driver Selection Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void GoToCargoWindow() throws IOException {
        if (driverListView.getSelectionModel().getSelectedItem() != null) {
            destination.setDriverId(driverListView.getSelectionModel().getSelectedItem().getId());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/cargo-selection-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) toCargoWindowButton.getScene().getWindow();
        stage.setTitle("Cargo Selection Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void SelectCheckpoints() {
        List<Integer> cpList = savedLists.getCpList();
        cpList.clear();
        ObservableList<Checkpoint> selectedItems = checkpointListView.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty()) {
            for (Checkpoint selectedItem : selectedItems) {
                cpList.add(selectedItem.getId());
            }
            savedLists.setCpList(cpList);
        }
    }

    public void SelectManagers() {
        List<Integer> respManList = savedLists.getRespManList();
        respManList.clear();
        ObservableList<Manager> selectedItems = respManagersListView.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty()) {
            for (Manager selectedItem : selectedItems) {
                respManList.add(selectedItem.getId());
            }
            savedLists.setRespManList(respManList);
        }
    }
}
