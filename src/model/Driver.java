package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User implements Serializable {
    private LocalDate medCertificateDate;
    private String medCertificateNumber;
    private String driverLicense;

    private StringProperty idProperty;
    private StringProperty loginProperty;
    private StringProperty passwordProperty;
    private StringProperty nameProperty;
    private StringProperty surnameProperty;
    private StringProperty birthDateProperty;
    private StringProperty phoneNumProperty;
    private StringProperty medCertificateDateProperty;
    private StringProperty medCertificateNumberProperty;
    private StringProperty driverLicenseProperty;

    public Driver(String login, String password, String name, String surname, LocalDate birthDate, String phoneNum, LocalDate medCertificateDate, String medCertificateNumber, String driverLicense) {
        super(login, password, name, surname, birthDate, phoneNum);
        this.medCertificateDate = medCertificateDate;
        this.medCertificateNumber = medCertificateNumber;
        this.driverLicense = driverLicense;
    }

    public Driver(int id, String login, String password, String name, String surname, LocalDate birthDate, String phoneNum, LocalDate medCertificateDate, String medCertificateNumber, String driverLicense) {
        super(id, login, password, name, surname, birthDate, phoneNum);
        this.medCertificateDate = medCertificateDate;
        this.medCertificateNumber = medCertificateNumber;
        this.driverLicense = driverLicense;

        this.idProperty = new SimpleStringProperty(Integer.toString(id));
        this.loginProperty = new SimpleStringProperty(login);
        this.passwordProperty = new SimpleStringProperty(password);
        this.nameProperty = new SimpleStringProperty(name);
        this.surnameProperty = new SimpleStringProperty(surname);
        this.birthDateProperty = new SimpleStringProperty(birthDate.toString());
        this.phoneNumProperty = new SimpleStringProperty(phoneNum);
        this.medCertificateDateProperty = new SimpleStringProperty(medCertificateDate.toString());
        this.medCertificateNumberProperty = new SimpleStringProperty(medCertificateNumber);
        this.driverLicenseProperty = new SimpleStringProperty(driverLicense);
    }

    public Driver(int id, String login, String name, String surname, LocalDate birthDate, String phoneNum, LocalDate medCertificateDate, String medCertificateNumber, String driverLicense) {
        super(id, login, name, surname, birthDate, phoneNum);
        this.medCertificateDate = medCertificateDate;
        this.medCertificateNumber = medCertificateNumber;
        this.driverLicense = driverLicense;

        this.idProperty = new SimpleStringProperty(Integer.toString(id));
        this.loginProperty = new SimpleStringProperty(login);
        this.nameProperty = new SimpleStringProperty(name);
        this.surnameProperty = new SimpleStringProperty(surname);
        this.birthDateProperty = new SimpleStringProperty(birthDate.toString());
        this.phoneNumProperty = new SimpleStringProperty(phoneNum);
        this.medCertificateDateProperty = new SimpleStringProperty(medCertificateDate.toString());
        this.medCertificateNumberProperty = new SimpleStringProperty(medCertificateNumber);
        this.driverLicenseProperty = new SimpleStringProperty(driverLicense);
    }

    public Driver(int id, String login) {
        super(id, login);
    }

    @Override
    public String toString() {
        return super.getLogin();
    }
}
