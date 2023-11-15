package com.CandyShop.controllers.mainShop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.Comment;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CommentController {

    @FXML
    public TextField commentTitleField;
    @FXML
    public TextArea commentBodyField;
    @FXML
    public ListView<Comment> commentListField;
    @FXML
    public Label productCommentLabel;

    private CustomHib customHib;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);

    }

    private void loadCommentList() {
        commentListField.getItems().clear();
        commentListField.getItems().addAll(customHib.getAllRecords(Comment.class));
    }

    public void createComment() {
        try {
//
        } catch (NullPointerException ignored) {

        }
    }

    public void updateComment() {
//        try {
//            Comment selectedComment = commentListField.getSelectionModel().getSelectedItem();
//            Comment commentFromDb = customHib.getEntityById(Comment.class, selectedComment.getId());
//            commentFromDb.setCommentTitle(commentTitleField.getText());
//            commentFromDb.setCommentBody(commentBodyField.getText());
//            customHib.update(commentFromDb);
//            loadCommentList();
//        } catch (NullPointerException ignored) {
//
//        }
    }

    public void deleteComment() {
        try {
            Comment selectedComment = commentListField.getSelectionModel().getSelectedItem();
            //Comment commentFromDb = genericHib.getEntityById(Comment.class, selectedComment.getId());
            customHib.deleteComment(selectedComment.getId());
            loadCommentList();
        } catch (NullPointerException ignored) {
        }
    }

    public void loadCommentInfo() {
//        try {
//            Comment selectedComment = commentListField.getSelectionModel().getSelectedItem();
//            commentTitleField.setText(selectedComment.getCommentTitle());
//            commentBodyField.setText(selectedComment.getCommentBody());
//            productCommentLabel.setText(selectedComment.getProduct().getName());
//        } catch (NullPointerException ignored) {
//
//        }
    }

}
