package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
public class Manager extends User implements Serializable {
    private String email;
    private LocalDate employmentDate;
    private boolean isAdmin;

    private StringProperty idProperty;
    private StringProperty loginProperty;
    private StringProperty passwordProperty;
    private StringProperty nameProperty;
    private StringProperty surnameProperty;
    private StringProperty birthDateProperty;
    private StringProperty phoneNumProperty;
    private StringProperty emailProperty;
    private StringProperty employmentDateProperty;
    private StringProperty isAdminProperty;

    public Manager(String login, String password, String name, String surname, LocalDate birthDate, String phoneNum, String email, LocalDate employmentDate) {
        super(login, password, name, surname, birthDate, phoneNum);
        this.email = email;
        this.employmentDate = employmentDate;
    }

    public Manager(String login, String password, String name, String surname, LocalDate birthDate, String phoneNum, String email, LocalDate employmentDate, boolean isAdmin) {
        super(login, password, name, surname, birthDate, phoneNum);
        this.email = email;
        this.employmentDate = employmentDate;
        setAdmin(isAdmin);
    }

    public Manager(int id, String login, String password, String name, String surname, LocalDate birthDate, String phoneNum, String email, LocalDate employmentDate, boolean isAdmin) {
        super(id, login, password, name, surname, birthDate, phoneNum);
        this.email = email;
        this.employmentDate = employmentDate;
        setAdmin(isAdmin);

        this.idProperty = new SimpleStringProperty(Integer.toString(id));
        this.loginProperty = new SimpleStringProperty(login);
        this.passwordProperty = new SimpleStringProperty(password);
        this.nameProperty = new SimpleStringProperty(name);
        this.surnameProperty = new SimpleStringProperty(surname);
        this.birthDateProperty = new SimpleStringProperty(birthDate.toString());
        this.phoneNumProperty = new SimpleStringProperty(phoneNum);
        this.emailProperty = new SimpleStringProperty(email);
        this.employmentDateProperty = new SimpleStringProperty(employmentDate.toString());
        this.isAdminProperty = new SimpleStringProperty(Boolean.toString(isAdmin));
    }

    public Manager(int id, String login, String name, String surname, LocalDate birthDate, String phoneNum, String email, LocalDate employmentDate, boolean isAdmin1) {
        super(id, login, name, surname, birthDate, phoneNum);
        this.email = email;
        this.employmentDate = employmentDate;
        this.isAdmin = isAdmin1;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        super.setAdmin(admin);
    }

    public Manager(int id, String login) {
        super(id, login);
    }

    @Override
    public String toString() {
        return super.getLogin();
    }

    public BooleanProperty isAdminProperty() {
        return new SimpleBooleanProperty(isAdmin);
    }
}
