package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Truck {
    private int id;
    private String marke;
    private String model;
    private int year;
    private double odometer;
    private double fuelTankCapacity;
    private TyreType tyreType;

    private StringProperty idProperty;
    private StringProperty markeProperty;
    private StringProperty modelProperty;
    private StringProperty yearProperty;
    private StringProperty odometerProperty;
    private StringProperty fuelTankCapacityProperty;
    private StringProperty tyreTypeProperty;

    public Truck(String marke, String model, int year, double odometer, double fuelTankCapacity, TyreType tyreType) {
        this.marke = marke;
        this.model = model;
        this.year = year;
        this.odometer = odometer;
        this.fuelTankCapacity = fuelTankCapacity;
        this.tyreType = tyreType;
    }

    public Truck(int id, String marke, String model, int year, double odometer, double fuelTankCapacity, TyreType tyreType) {
        this.id = id;
        this.marke = marke;
        this.model = model;
        this.year = year;
        this.odometer = odometer;
        this.fuelTankCapacity = fuelTankCapacity;
        this.tyreType = tyreType;

        this.idProperty = new SimpleStringProperty(Integer.toString(id));
        this.markeProperty = new SimpleStringProperty(marke);
        this.modelProperty = new SimpleStringProperty(model);
        this.yearProperty = new SimpleStringProperty(Integer.toString(year));
        this.odometerProperty = new SimpleStringProperty(Double.toString(odometer));
        this.fuelTankCapacityProperty = new SimpleStringProperty(Double.toString(fuelTankCapacity));
        this.tyreTypeProperty = new SimpleStringProperty(tyreType.toString());
    }

    public Truck(int id, String marke) {
        this.id = id;
        this.marke = marke;
    }

    @Override
    public String toString() {
        return marke;
    }
}
