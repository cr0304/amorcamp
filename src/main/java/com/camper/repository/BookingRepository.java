package com.camper.repository;

import com.camper.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.member.id = :memberId order by b.id desc")
    List<Booking> findByBooking(@Param("memberId") Long memberId);
    Page<Booking> findAllByCampId(Pageable pageable, Long campId);

    @Query("select b from Booking b where b.camp.id = :campId")
    List<Booking> findByBookingList(@Param("campId") Long campId);

    @Query("select b from Booking b where ((b.checkIn = :checkIn and b.checkOut = :checkOut) or ((b.checkIn <= :checkIn and b.checkOut >= :checkIn) or (b.checkIn <= :checkOut and " +
            "b.checkOut >= :checkOut)) or (b.checkIn > :checkIn and b.checkOut < :checkOut)) and (b.bStatus = :bComplete)")
    List<Booking> findByDate(@Param("bComplete")String bComplete, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);;
}
