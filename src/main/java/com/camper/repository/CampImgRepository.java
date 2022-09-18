package com.camper.repository;

import com.camper.entity.Camp;
import com.camper.entity.CampImg;
import com.camper.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampImgRepository extends JpaRepository<CampImg, Long> {
    CampImg findByCampId(Long campId);

    @Query("select c from CampImg c where c.id = :campImgId")
    CampImg findByCampImg(@Param("campImgId") Long campImgId);
}