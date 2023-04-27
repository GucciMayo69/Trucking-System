package model;

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
public abstract class User implements Serializable {
    private int id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private LocalDate  birthDate;
    private String phoneNum;
    private boolean isAdmin = false;

    public User(String login, String password, String name, String surname, LocalDate birthDate, String phoneNum) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNum = phoneNum;
    }

    public User(int id, String login, String password, String name, String surname, LocalDate birthDate, String phoneNum) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNum = phoneNum;
    }

    public User(int id, String login, String name, String surname, LocalDate birthDate, String phoneNum) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNum = phoneNum;
    }

    public User(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public User(String login) {
    }
}
