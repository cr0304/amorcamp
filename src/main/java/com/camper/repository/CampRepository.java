package com.camper.repository;

import com.camper.entity.Camp;
import com.camper.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CampRepository extends JpaRepository<Camp, Long>{
    @Query("select c from Camp c where c.permission = :permission order by c.id desc")
    List<Camp> findByCamp(@Param("permission") String permission);
    //Camp Entity 변수 중 permission이 비어있는 Camp만 싹 List 형태로 가져와라.
    //List의 값들은 camp_id을 기준으로 내림차순(desc)하여서...!
    //Camp findByCamp(Long campId);
    @Query("select c from Camp c where c.id = :campId")
    Camp findByCamp(Long campId);

    @Query("select c from Camp c ORDER BY c.count desc")
    List<Camp> findCampsByCount();

    @Query("select AVG(r.rating) FROM Review r where r.camp.id = :campId")
    Integer getRatingAvgByRating(@Param("campId") Long campId);

    @Query("select c from Camp c where (c.surroundings = :mountain or c.surroundings = :sea or c.surroundings = :vally or c.surroundings = :city) or c.campArea = :area" )
    List<Camp> findByNatureOrAreaOnly(@Param("mountain") String mountain, @Param("sea") String sea, @Param("vally") String vally, @Param("city") String city, @Param("area") String area);

    @Query("select c from Camp c where (c.surroundings = :mountain or c.surroundings = :sea or c.surroundings = :vally or c.surroundings = :city ) and c.campArea = :area ")
    List<Camp> findByNatureAreaAll(@Param("mountain") String mountain, @Param("sea") String sea, @Param("vally") String vally, @Param("city") String city, @Param("area") String area);


}