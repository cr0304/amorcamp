package com.camper.entity;


import com.camper.dto.BookingFormDto;
import com.camper.dto.ReviewFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.security.Principal;
import java.util.Optional;

@Entity
@Table(name = "review")
@Getter
@Setter
@ToString
public class Review {


    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int rating;

    private String reviewContent;

    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    public static Review createReview(ReviewFormDto reviewFormDto,Camp camp){
        Review review = new Review();
        review.setCamp(camp);
        review.setRating(reviewFormDto.getRating());
        review.setReviewContent(reviewFormDto.getReviewContent());
        review.setUserName(reviewFormDto.getUserName());
        return review;
    }

}
