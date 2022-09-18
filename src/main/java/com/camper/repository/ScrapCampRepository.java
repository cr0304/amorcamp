package com.camper.repository;

import com.camper.dto.ScrapListDto;
import com.camper.entity.Room;
import com.camper.entity.ScrapCamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScrapCampRepository extends JpaRepository<ScrapCamp, Long> {

    ScrapCamp findByScrapIdAndCampId(Long scrapId, Long campId);


    @Query("select new com.camper.dto.ScrapListDto(sc.id, c.campName, im.imgUrl) " +
            "from ScrapCamp sc, CampImg im "+
            "join sc.camp c " +                     //scrapCamp sc와 sc의 camp가 조인.
            "where sc.scrap.id = :scrapId " +
            "and sc.camp.id = im.camp.id " +
            "order by sc.regTime desc")
    List<ScrapListDto> findScrapListDtoList(Long scrapId);

    @Query("select sc from ScrapCamp sc where sc.id = :scrapCampId")
    ScrapCamp findByScrapCampId(Long scrapCampId);

}