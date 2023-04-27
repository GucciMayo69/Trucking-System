package utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.User;

@NoArgsConstructor
@Getter
@Setter
public class CurrentUser {
    private static CurrentUser instance = null;
    public User user = null;
    public boolean isManager = false;

    public static synchronized CurrentUser getInstance() {
        if (instance == null) instance = new CurrentUser();
        return instance;
    }

}
