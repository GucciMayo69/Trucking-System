package utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Destination;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SavedLists {
    private static SavedLists instance = null;
    List<Integer> cpList = new ArrayList<>();
    List<Integer> respManList = new ArrayList<>();
    List<Destination> destList = new ArrayList<>();
    int destId;

    public static synchronized SavedLists getInstance() {
        if (instance == null) instance = new SavedLists();
        return instance;
    }
}