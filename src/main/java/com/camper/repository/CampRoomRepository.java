package com.camper.repository;

import com.camper.entity.Camp;
import com.camper.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampRoomRepository  extends JpaRepository<Camp, Long>, QuerydslPredicateExecutor<Camp>, CampRoomRepositoryCustom {
    @Query("select r from Room r where r.campType = :SearchQuery order by r.id desc")
    List<Room> findByRoom(@Param("SearchQuery") String SearchQuery);

    @Query("select r from Room r where r.campType = :camping or r.campType = :glamping or r.campType = :caravan")
    List<Room> findByRoom(@Param("camping") String camping, @Param("glamping") String glamping, @Param("caravan") String caravan);
}