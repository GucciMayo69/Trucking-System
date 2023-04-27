package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Forum {
    private int id;
    private String title;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private List<Comment> commentList;
    private String user;

    public Forum(int id, String title, LocalDate dateCreated, LocalDate dateModified) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    public Forum(String title, LocalDate dateCreated, LocalDate dateModified, String user) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.user = user;
    }

    @Override
    public String toString() {
        return title;
    }

}

