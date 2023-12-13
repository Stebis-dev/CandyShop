package com.CandyShop.controllers.mainShop;

import com.CandyShop.hibernateControllers.CustomHib;
import com.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ProductController {
    @FXML
    public ImageView productImage;
    @FXML
    public Label productName;
    @FXML
    public Label productPrice;
    @FXML
    public Label productDescription;
    @FXML
    public Label productIngredients;
    @FXML
    public Label productNutritionalValue;
    @FXML
    public Label productWeight;
    @FXML
    public Label productCountryOfOrigin;
    @FXML
    public Label productStorageConditions;
    @FXML
    public Label amountLabel;
    @FXML
    public ListView<Comment> reviewList;
    @FXML
    public TextArea reviewInputField;
    @FXML
    public Button reviewSubmit;
    @FXML
    public Slider reviewScore;
    private int amount;

    private Product product;
    private User currentUser;
    private CustomHib customHib;


    void setData(Product product) {
        amount = 1;
        this.product = product;
    }

    void loadData() {
        loadProductData();
        loadComments();
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        customHib = new CustomHib(entityManagerFactory);
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void loadProductData() {
        String measurement = "g.";
        if (product.getCategory() == ProductType.DRINK) {
            measurement = "ml.";
        }
        productImage.setImage(new Image(product.getImagePath()));
        productName.setText(product.getName() + ", " + product.getWeight() + " " + measurement);
        productPrice.setText(product.getPrice() + "€");
        amountLabel.setText(Integer.toString(amount));
        productDescription.setText(product.getDescription());
        productIngredients.setText(product.getIngredients());
        productNutritionalValue.setText(product.getNutritionalValue());
        productWeight.setText("Neto weight:\t\t\t" + product.getWeight() + " " + measurement);
        productCountryOfOrigin.setText("Country of origin:\t\t" + product.getCountryOfOrigin());
        productStorageConditions.setText("Storage conditions:\t\t" + product.getStorageConditions());
    }

    public void decreaseAmount() {
        if (amount > 1) {
            amount = amount - 1;
        }
        amountLabel.setText(Integer.toString(amount));
    }

    public void AddAmount() {
        if (amount < 500) {
            amount = amount + 1;
        }
        amountLabel.setText(Integer.toString(amount));
    }

    public void addToCart() {
        try {
            Cart customersCartWithProduct = customHib.getUserCartProduct(currentUser.getId(), product.getId());

            if (customersCartWithProduct != null) {
                customersCartWithProduct.addProduct(amount);
                customHib.update(customersCartWithProduct);
            }
        } catch (Exception ignored) {
            if (product != null) {
                Cart cart = new Cart(LocalDate.now(), ((Customer) currentUser), product);
                customHib.create(cart);
            }
        }
        amount = 1;
        amountLabel.setText(String.valueOf(amount));
    }

    private static void getCommentChildren(Comment comment, List<Comment> list) {
        list.add(comment);
        for (Comment child : comment.getChildrenComment()) {
            getCommentChildren(child, list);
        }
    }


    public void loadComments() {
        reviewList.getItems().clear();
        List<Comment> productComments = customHib.getProductComments(product.getId());

        Map<Integer, Comment> commentMap = new HashMap<>();
        for (Comment comment : productComments) {
            commentMap.put(comment.getId(), comment);
        }

        List<Comment> parents = new ArrayList<>();
        for (Comment comment : productComments) {
            if (comment.getParentID() == null) {
                parents.add(comment);
            } else {
                Comment parent = commentMap.get(comment.getParentID());
                if (parent != null) {
                    parent.addComment(comment);
                }
            }
        }

        List<Comment> root = new ArrayList<>();
        for (Comment comment : parents) {
            getCommentChildren(comment, root);
        }

        reviewList.getItems().addAll(root);
    }


    public void submitReview() {
        Comment selectedComment = reviewList.getSelectionModel().getSelectedItem();
        int commentLevel = 0;
        Integer parentComment = null;

        if (!reviewInputField.getText().isEmpty()) {
            if (selectedComment != null) {
                commentLevel = selectedComment.getCommentLevel() + 1;
                parentComment = selectedComment.getId();
                Comment comment = new Comment(reviewInputField.getText(), parentComment, currentUser,
                        product, LocalDateTime.now(), commentLevel);
                selectedComment.addComment(comment);
                customHib.create(comment);
            } else {
                Review review = new Review(reviewInputField.getText(), parentComment, currentUser,
                        product, LocalDateTime.now(), commentLevel, (int) reviewScore.getValue());
                customHib.create(review);
            }
        }

        reviewInputField.clear();
        loadComments();
    }

    public void selectedComment() {
        reviewScore.setDisable(reviewList.getSelectionModel().getSelectedItem() != null);
    }

    public void unSelect() {
        reviewList.getSelectionModel().clearSelection();
        reviewScore.setDisable(false);
    }

    public void deleteSelectedComment() {
        Comment selectedComment = reviewList.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            if (selectedComment.getUser().getId() == currentUser.getId() || (currentUser instanceof Manager && ((Manager) currentUser).isAdmin())) {

                List<Comment> productComments = customHib.getProductComments(product.getId());
                Map<Integer, Comment> commentMap = new HashMap<>();
                for (Comment comment : productComments) {
                    commentMap.put(comment.getId(), comment);
                }

                deleteCommentAndChildren(selectedComment, commentMap);

                loadComments();
            }
        }
    }

    private void deleteCommentAndChildren(Comment comment, Map<Integer, Comment> commentMap) {
        commentMap.remove(comment.getId()); // Remove the comment itself
        customHib.delete(Comment.class, comment.getId());
        for (Comment child : comment.getChildrenComment()) {
            deleteCommentAndChildren(child, commentMap); // Recursively remove children
        }
    }
}
