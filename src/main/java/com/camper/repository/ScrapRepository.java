package com.camper.repository;

import com.camper.entity.Member;
import com.camper.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Scrap findByMemberId(Long memberId);

}
