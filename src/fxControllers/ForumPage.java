package fxControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;
import utils.CurrentUser;
import utils.DbUtils;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ForumPage implements Initializable {
    @FXML public ListView<Forum> listForum;
    @FXML public TreeView<Comment> commentTree;
    @FXML public TextArea commentBody;
    @FXML public TextField newForumField;
    @FXML public Button deleteCommentButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            UpdateForumList();
            CurrentUser currentUser = CurrentUser.getInstance();
            User user = currentUser.getUser();
            if(!user.isAdmin()) deleteCommentButton.setVisible(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TreeItem<Comment> root = new TreeItem<>();
        commentTree.setShowRoot(false);
        commentTree.setRoot(root);
    }

    public void addComment() throws SQLException {
        if (commentTree.getSelectionModel().getSelectedItem() != null && listForum.getSelectionModel().getSelectedItem() != null) {
            Connection connection = DbUtils.connectToDb();
            String insertComment = "INSERT INTO comments(`comment_text`, `date_created`, `date_modified`, parent_forum_id, parent_comment_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertComment);
            Comment comment = new Comment(commentBody.getText(), LocalDate.now(), LocalDate.now(), listForum.getSelectionModel().getSelectedItem().getId(), commentTree.getSelectionModel().getSelectedItem().getValue().getId());
            preparedStatement.setString(1, comment.getCommentText());
            preparedStatement.setDate(2, Date.valueOf(comment.getDateCreated()));
            preparedStatement.setDate(3, Date.valueOf(comment.getDateModified()));
            preparedStatement.setInt(4, comment.getParentForum());
            preparedStatement.setInt(5, comment.getParentComment());

            preparedStatement.execute();
            DbUtils.disconnect(connection, preparedStatement);

            commentBody.clear();
            LoadForum();
        }
        else if(commentTree.getSelectionModel().getSelectedItem() == null && listForum.getSelectionModel().getSelectedItem() != null) {
            Connection connection = DbUtils.connectToDb();
            String insertComment = "INSERT INTO comments(`comment_text`, `date_created`, `date_modified`, parent_forum_id, parent_comment_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertComment);
            Comment comment = new Comment(commentBody.getText(), LocalDate.now(), LocalDate.now(), listForum.getSelectionModel().getSelectedItem().getId(), 0);
            preparedStatement.setString(1, comment.getCommentText());
            preparedStatement.setDate(2, Date.valueOf(comment.getDateCreated()));
            preparedStatement.setDate(3, Date.valueOf(comment.getDateModified()));
            preparedStatement.setInt(4, comment.getParentForum());
            preparedStatement.setInt(5, comment.getParentComment());

            preparedStatement.execute();
            DbUtils.disconnect(connection, preparedStatement);

            commentBody.clear();
            LoadForum();
        }
    }

    public void UpdateForumList() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String readForums = "SELECT * FROM `forums`";
        PreparedStatement preparedStatement = connection.prepareStatement(readForums);
        ResultSet rs = preparedStatement.executeQuery();
        ObservableList<Forum> forumList = FXCollections.observableArrayList();
        while (rs.next()) {
            Forum forum = new Forum(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getDate("date_created").toLocalDate(),
                    rs.getDate("date_modified").toLocalDate()
            );
            forumList.add(forum);
        }

        listForum.setItems(forumList);
        DbUtils.disconnect(connection, preparedStatement);
    }

    public void createNewForum() throws SQLException {
        CurrentUser currentUser = CurrentUser.getInstance();
        User user = currentUser.getUser();
        Connection connection = DbUtils.connectToDb();
        String insertForum = "INSERT INTO forums(`title`, `date_created`, `date_modified`, `current_user`) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertForum);
        Forum forum = new Forum(newForumField.getText(), LocalDate.now(), LocalDate.now(), user.getLogin());
        preparedStatement.setString(1, forum.getTitle());
        preparedStatement.setDate(2, Date.valueOf(forum.getDateCreated()));
        preparedStatement.setDate(3, Date.valueOf(forum.getDateModified()));
        preparedStatement.setString(4, user.getLogin());


        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        UpdateForumList();
    }

    public void LoadForum() throws SQLException {
        TreeItem<Comment> root = new TreeItem<>();
        commentTree.setRoot(root);
        root.getChildren().clear();

        Forum selectedForum = listForum.getSelectionModel().getSelectedItem();
        Connection connection = DbUtils.connectToDb();
        String readComments = "SELECT * FROM `comments` where parent_forum_id  = ? AND parent_comment_id = 0";
        PreparedStatement preparedStatement = connection.prepareStatement(readComments);
        preparedStatement.setInt(1, selectedForum.getId());
        ResultSet rs = preparedStatement.executeQuery();
        List<Comment> commentList = new ArrayList<>();
        while (rs.next()) {
            Comment comment = new Comment(
                    rs.getInt("id"),
                    rs.getString("comment_text"),
                    rs.getDate("date_created").toLocalDate(),
                    rs.getDate("date_modified").toLocalDate(),
                    rs.getInt("parent_forum_id"),
                    rs.getInt("parent_comment_id")
            );
            commentList.add(comment);
        }
        commentList.forEach(c -> {
            try {
                AddTreeItem(c, commentTree.getRoot());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        DbUtils.disconnect(connection, preparedStatement);
    }

    private List<Comment> getCommentByParentComment(Comment parentComment) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        String getChildComment = "SELECT * FROM `comments` where parent_comment_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(getChildComment);
        preparedStatement.setInt(1, parentComment.getId());
        ResultSet rs = preparedStatement.executeQuery();
        List<Comment> replies = new ArrayList<>();
        while (rs.next()) {
            Comment comment = new Comment(
                    rs.getInt("id"),
                    rs.getString("comment_text"),
                    rs.getDate("date_created").toLocalDate(),
                    rs.getDate("date_modified").toLocalDate(),
                    rs.getInt("parent_forum_id"),
                    rs.getInt("parent_comment_id")
            );
            replies.add(comment);
        }
        DbUtils.disconnect(connection, preparedStatement);
        return replies;

    }

    private void AddTreeItem(Comment comment, TreeItem<Comment> parent) throws SQLException {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parent.getChildren().add(treeItem);

        List<Comment> replies = getCommentByParentComment(comment);
        replies.forEach(r -> {
            try {
                AddTreeItem(r, treeItem);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void DeleteComment() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        int selectedComment = commentTree.getSelectionModel().getSelectedItem().getValue().getId();

        String deleteComment = "DELETE FROM comments WHERE id=" + selectedComment;
        PreparedStatement preparedStatement = connection.prepareStatement(deleteComment);
        preparedStatement.execute();
        DbUtils.disconnect(connection, preparedStatement);

        LoadForum();
    }
}
