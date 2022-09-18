package com.camper.service;

import com.camper.entity.Booking;
import com.camper.entity.Room;
import com.camper.repository.BookingRepository;
import com.camper.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    private final RoomRepository roomRepository;
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void updateBStatus(Booking booking,String bStatus){
        booking.updateBStatus(bStatus);
    }

    public List<Room> searchRoomsByDates(Long campId, LocalDate checkIn, LocalDate checkOut) {
        List<Room> rooms = roomRepository.findByCampId(campId);
        List<Booking> alreadyBooked = bookingRepository.findByDate("bComplete",checkIn, checkOut);

        for (int i = 0; i < rooms.size() ; i++){
            for (int j = 0; j < alreadyBooked.size(); j++){
                if(rooms.get(i).getId().equals(alreadyBooked.get(j).getRoom().getId())){
                    rooms.remove(i);
                }

            }
        }
        return rooms;
    }

    public void updateCount(Booking booking,int count){
        booking.updateCount(count);
    }
}
