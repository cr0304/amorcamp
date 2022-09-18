package com.camper.entity;

import com.camper.dto.BookingFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "booking")
@Getter
@Setter
@ToString
public class Booking extends BaseTimeEntity {

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String customer;

    private String phoneNumber;

    private String campName;

    private String roomName;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private Integer price;

    private int count;

    private String bStatus; // bComplete _ bCancel _ bUseEnd

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;


    public static Booking createBooking(BookingFormDto bookingFormDto, Member member, Room room) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate dateIn = LocalDate.parse(bookingFormDto.getCheckIn(), DateTimeFormatter.ISO_DATE);
        LocalDate dateOut = LocalDate.parse(bookingFormDto.getCheckOut(), DateTimeFormatter.ISO_DATE);
        Booking booking = new Booking();
        booking.setCustomer(bookingFormDto.getCustomer());
        booking.setPhoneNumber(bookingFormDto.getPhoneNumber());
        booking.setCampName(bookingFormDto.getCampName());
        booking.setRoomName(bookingFormDto.getRoomName());
        booking.setCheckIn(dateIn);
        booking.setCheckOut(dateOut);
        booking.setPrice(bookingFormDto.getPrice());
        booking.setBStatus(bookingFormDto.getBStatus());
        booking.setMember(member);
        booking.setRoom(room);
        booking.setEmail(member.getEmail());
        booking.setCamp(room.getCamp());
        booking.setCount(bookingFormDto.getCount());
        return booking;
    }

    public void updateBStatus(String bStatus){
        this.bStatus = bStatus;
    }

    public void updateCount(int count){
        this.count = count;
    }
}
