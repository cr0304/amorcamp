package com.camper.service;

import com.camper.entity.Camp;
import com.camper.entity.Review;
import com.camper.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review saveReview(Review review){
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviewByCampId(Long campId){
        return reviewRepository.findReviewByCampId(campId);
    }


}
