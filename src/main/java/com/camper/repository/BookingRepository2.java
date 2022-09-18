package com.camper.repository;

import com.camper.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository2 extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.member.id = :memberId and b.id = :bookingId")
    Booking findByBooking(@Param("memberId") Long memberId,@Param("bookingId") Long bookingId);

    @Query("select b from Booking b where b.id = :bookingId")
    Booking findByBooking(@Param("bookingId") Long bookingId);
}