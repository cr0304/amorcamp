package com.camper.repository;

import com.camper.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository2 extends JpaRepository<Room, Long> {
    @Query("select r from Room r where r.id = :roomId")
    Room findByRoom(Long roomId);
}