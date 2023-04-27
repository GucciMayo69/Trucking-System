package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Destination {


    private int id;
    private Cities startCity;
    private Cities endCity;
    private String truckName;
    private String driverName;
    private String cargoName;
    private List<Checkpoint> checkpointList;
    private List<Manager> responsibleManagers;
    private int truckId;
    private int driverId;
    private int cargoId;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;
    private boolean isCompleted;

    private StringProperty idProperty;
    private StringProperty startCityProperty;
    private StringProperty endCityProperty;
    private StringProperty truckProperty;
    private StringProperty driverProperty;
    private StringProperty cargoProperty;
    private StringProperty dateCreatedProperty;
    private StringProperty dateUpdatedProperty;
    private StringProperty isCompletedProperty;

    public Destination(int id, Cities startCity, Cities endCity, int truckId, int driverId, int cargoId, LocalDate dateCreated, LocalDate dateUpdated, boolean isCompleted) {
        this.id = id;
        this.startCity = startCity;
        this.endCity = endCity;
        this.truckId = truckId;
        this.driverId = driverId;
        this.cargoId = cargoId;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.isCompleted = isCompleted;

        this.idProperty = new SimpleStringProperty(Integer.toString(id));
        this.startCityProperty = new SimpleStringProperty(startCity.toString());
        this.endCityProperty = new SimpleStringProperty(endCity.toString());
        this.truckProperty = new SimpleStringProperty(Integer.toString(getTruckId()));
        this.driverProperty = new SimpleStringProperty(Integer.toString(getDriverId()));
        this.cargoProperty = new SimpleStringProperty(Integer.toString(getCargoId()));
        this.dateCreatedProperty = new SimpleStringProperty(getDateCreated().toString());
        this.dateUpdatedProperty = new SimpleStringProperty(getDateUpdated().toString());
        this.isCompletedProperty = new SimpleStringProperty(Boolean.toString(isCompleted));
    }

    public Destination(int id, Cities startCity, Cities endCity, String truckName, String driverName, String cargoName, LocalDate dateCreated, LocalDate dateUpdated, boolean isCompleted) {
        this.id = id;
        this.startCity = startCity;
        this.endCity = endCity;
        this.truckName = truckName;
        this.driverName = driverName;
        this.cargoName = cargoName;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.isCompleted = isCompleted;

        this.idProperty = new SimpleStringProperty(Integer.toString(id));
        this.startCityProperty = new SimpleStringProperty(startCity.toString());
        this.endCityProperty = new SimpleStringProperty(endCity.toString());
        this.truckProperty = new SimpleStringProperty(Integer.toString(getTruckId()));
        this.driverProperty = new SimpleStringProperty(Integer.toString(getDriverId()));
        this.cargoProperty = new SimpleStringProperty(Integer.toString(getCargoId()));
        this.dateCreatedProperty = new SimpleStringProperty(getDateCreated().toString());
        this.dateUpdatedProperty = new SimpleStringProperty(getDateUpdated().toString());
        this.isCompletedProperty = new SimpleStringProperty(Boolean.toString(isCompleted));
    }

    public BooleanProperty isCompletedProperty() {
        return new SimpleBooleanProperty(isCompleted);
    }

}
