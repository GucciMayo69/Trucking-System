package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.PrimitiveIterator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment{
    private int id;
    private String commentText;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private List<Comment> replies;
    private int parentForum;
    private int parentComment;
    private User user;

    public Comment(String commentText, LocalDate dateCreated, LocalDate dateModified, int parentForum, int parentComment) {
        this.commentText = commentText;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.parentForum = parentForum;
        this.parentComment = parentComment;
    }

    public Comment(int id, String commentText, LocalDate dateCreated, LocalDate dateModified, int parentForum, int parentComment) {
        this.id = id;
        this.commentText = commentText;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.parentForum = parentForum;
        this.parentComment = parentComment;
    }

    @Override
    public String toString() {
        return commentText;
    }
}
