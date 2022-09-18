package com.camper.controller;

import com.camper.dto.BookingFormDto;
import com.camper.dto.MemberFormDto;
import com.camper.entity.Booking;
import com.camper.entity.Camp;
import com.camper.entity.Member;
import com.camper.entity.Room;
import com.camper.repository.MemberRepository;
import com.camper.repository.RoomRepository2;
import com.camper.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@AllArgsConstructor
public class PaymentController {

    private final BookingService bookingService;

    private final MemberRepository memberRepository;

    private final RoomRepository2 roomRepository2;

    @GetMapping(value = "/pay")
    public String payForm(Model model){
        model.addAttribute("bookingFormDto", new BookingFormDto());
        return "/payment/pay";
    }

    @PostMapping(value = "/saveBooking")
    public String saveBooking(@Valid BookingFormDto bookingFormDto, BindingResult bindingResult, Principal principal, Model model){

        if(bindingResult.hasErrors()){
            return "redirect:/pay";
        }
        try{
            String email = principal.getName();
            Member member = memberRepository.findByEmail(email);
            Room room = roomRepository2.findByRoom(bookingFormDto.getRoomId());
            Booking booking = Booking.createBooking(bookingFormDto,member, room);
            Camp camp = booking.getCamp();
            camp.addCount();
            booking.setBStatus("bComplete");
            bookingService.saveBooking(booking);
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "payment/pay";
        }
        return "redirect:/";
    }
}
