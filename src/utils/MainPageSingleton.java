package utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.User;

@NoArgsConstructor
@Getter
@Setter
public class MainPageSingleton {
    private static MainPageSingleton instance = null;
    public User user = null;
    public boolean isManager = false;

    public static synchronized MainPageSingleton getInstance() {
        if (instance == null) instance = new MainPageSingleton();
        return instance;
    }

}