package com.CandyShop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends Comment {
    private int score;

    public Review(String comment, Integer parentID, User user, Product product, LocalDateTime dateCreated, int commentLevel, int score) {
        super(comment, parentID, user, product, dateCreated, commentLevel);
        this.score = score;
    }

    @Override
    public String toString() {
        String commentHeader = dateCreated.getHour() + ":" + dateCreated.getMinute() + " " + user.getName().toUpperCase() + " " + score + "/5";
        String commentBody = comment;
        return commentHeader + "\n" + commentBody;
    }
}
