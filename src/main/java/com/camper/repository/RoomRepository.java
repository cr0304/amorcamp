package com.camper.repository;

import com.camper.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>  {
    @Query("select r from Room r where r.camp.id = :campId order by r.id desc")
    List<Room> findByRoom(@Param("campId") Long campId);

    @Query("select r from Room r where r.campType = :camping or r.campType = :glamping or r.campType = :caravan")
    List<Room> findByRoom(@Param("camping") String camping, @Param("glamping") String glamping, @Param("caravan") String caravan);

    List<Room> findByCampId(Long campId);
}
