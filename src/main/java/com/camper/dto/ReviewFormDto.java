package com.camper.dto;

import com.camper.entity.Camp;
import com.camper.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFormDto {

    private Long id;

    private int rating;

    private String reviewContent;

    private String userName;

    private Long campId;

    private Long bookingId;
}
