package com.camper.controller;

import com.camper.dto.CampFormDto;
import com.camper.dto.ReviewFormDto;
import com.camper.entity.Booking;
import com.camper.entity.Camp;
import com.camper.entity.Member;
import com.camper.entity.Review;
import com.camper.repository.BookingRepository2;
import com.camper.service.BookingService;
import com.camper.service.CampService;
import com.camper.service.MemberService;
import com.camper.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class ReviewController {

    private final MemberService memberService;

    private final ReviewService reviewService;

    private final CampService campService;

    private final BookingRepository2 bookingRepository2;

    private final BookingService bookingService;

    @GetMapping(value = "/popUpReview")
    public String showPopUp(Model model) {
        model.addAttribute("reviewFormDto", new ReviewFormDto());
        return "member/popUpReview";
    }

    @RequestMapping(value = "/saveReview", method = {RequestMethod.POST})
    @ResponseBody
    public boolean saveReview(@RequestBody ReviewFormDto reviewFormDto, Principal principal) {
        Booking booking = null;
        try {
            String email = principal.getName();
            Member member = memberService.findMember(email);

            Long campId = reviewFormDto.getCampId();
            Camp camp = campService.getCampById(campId);

            reviewFormDto.setUserName(member.getName());

            Long bookingId = reviewFormDto.getBookingId();
            booking = bookingRepository2.findByBooking(bookingId);

            int count = booking.getCount();

            //Review reviewCheck = reviewRepository.findByReview(camp.getId(),member.getId());
            if (count == 0) {
                Review review = Review.createReview(reviewFormDto, camp);
                reviewService.saveReview(review);
            } else {
                return false;
            }

            Integer ratingAvg = campService.getRatingAvg(campId);
        } catch (Exception e) {
            return false;
        }
        bookingService.updateCount(booking, 1);
        return true;
    }

}
