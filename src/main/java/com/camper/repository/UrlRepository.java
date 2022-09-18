package com.camper.repository;

import com.camper.entity.Camp;
import com.camper.entity.Room;
import com.camper.entity.Scrap;
import com.camper.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UrlRepository extends JpaRepository<Url, Long> {

    @Query("select u from Url u where u.member.id = :memberId")
    Url findByUrl(@Param("memberId") Long memberId);

}