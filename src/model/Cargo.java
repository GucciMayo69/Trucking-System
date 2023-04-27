package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {
    private int id;
    private String title;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;
    private double weight;
    private CargoType cargoType;
    private String customer;

    private StringProperty idProperty;
    private StringProperty titleProperty;
    private StringProperty dateCreatedProperty;
    private StringProperty dateUpdatedProperty;
    private StringProperty weightProperty;
    private StringProperty cargoTypeProperty;
    private StringProperty customerProperty;

    public Cargo(int id, String title, LocalDate dateCreated, LocalDate dateUpdated, double weight, CargoType cargoType, String customer) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.weight = weight;
        this.cargoType = cargoType;
        this.customer = customer;

        this.idProperty = new SimpleStringProperty(Integer.toString(id));
        this.titleProperty = new SimpleStringProperty(title);
        this.dateCreatedProperty = new SimpleStringProperty(dateCreated.toString());
        this.dateUpdatedProperty = new SimpleStringProperty(dateUpdated.toString());
        this.weightProperty = new SimpleStringProperty(Double.toString(weight));
        this.cargoTypeProperty = new SimpleStringProperty(cargoType.toString());
        this.customerProperty = new SimpleStringProperty(customer);
    }

    public Cargo(String title, LocalDate dateCreated, LocalDate dateUpdated, double weight, CargoType cargoType, String customer) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.weight = weight;
        this.cargoType = cargoType;
        this.customer = customer;
    }

    public Cargo(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
