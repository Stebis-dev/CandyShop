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
    private String comment;
    private Integer parentID;

    @OneToOne
    private User user;
    @OneToOne
    private Order order;
    @OneToOne
    private Product product;

    @Transient
    public List<Comment> childrenComment = new ArrayList<>();

    private LocalDateTime dateCreated;

    private int commentLevel;

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
        String commentB = user.getName() + "(" + dateCreated.getHour() + ":" + dateCreated.getMinute() + "): " + comment;
        String commentStart = "";
        for (int i = 0; i < commentLevel; i++) {
            commentStart += "\t";
        }
        commentStart += commentB;
        return commentStart;
    }
}
