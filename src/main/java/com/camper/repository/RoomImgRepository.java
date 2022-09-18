package com.camper.repository;

import com.camper.entity.Room;
import com.camper.entity.RoomImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {

    List<RoomImg> findByRoomIdOrderByIdAsc(Long itemId);

    List<RoomImg> findByRoomId(Long roomId);

    @Query("select r from RoomImg r where r.room.id = :roomId")
    List<RoomImg> findByRoomImg(@Param("roomId") Long roomId);

}
