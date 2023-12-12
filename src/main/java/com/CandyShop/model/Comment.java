package com.CandyShop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    protected String comment;
    private Integer parentID;

    @OneToOne
    protected User user;
    @OneToOne
    private Order order;
    @OneToOne
    protected Product product;

    @Transient
    public List<Comment> childrenComment = new ArrayList<>();

    protected LocalDateTime dateCreated;

    protected int commentLevel;

    public Comment(String comment, Integer parentID, User user, Product product, LocalDateTime dateCreated, int commentLevel) {
        this.comment = comment;
        this.parentID = parentID;
        this.user = user;
        this.product = product;
        this.dateCreated = dateCreated;
        this.commentLevel = commentLevel;
    }

    public void addComment(Comment comment) {
        childrenComment.add(comment);
    }

    @Override
    public String toString() {
        String commentHeader = dateCreated.getHour() + ":" + dateCreated.getMinute() + " " + user.getName().toUpperCase();
        String commentBody = comment;
        String commentLevel = "";
        for (int i = 0; i < this.commentLevel; i++) {
            commentLevel += "\t";
        }
        return commentLevel + commentHeader + "\n" + commentLevel + commentBody;
    }
}
