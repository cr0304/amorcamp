
package com.camper.service;

import com.camper.dto.ScrapCampDto;
import com.camper.dto.ScrapListDto;
import com.camper.entity.*;
import com.camper.repository.CampRepository;
import com.camper.repository.MemberRepository;
import com.camper.repository.ScrapCampRepository;
import com.camper.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {

    private final ScrapRepository scrapRepository;

    private final CampRepository campRepository;

    private final MemberRepository memberRepository;

    private final ScrapCampRepository scrapCampRepository;


    public Long addScrap2(Long campId, String email){
        //camp와 member 추출
        Camp camp = campRepository.findById(campId)
                .orElseThrow(EntityExistsException::new);
        Member member = memberRepository.findByEmail(email);

        //멤버 아이디로 스크랩을 빼냄
        Scrap scrap = scrapRepository.findByMemberId(member.getId());

        //고객에게
        //scrap이 없으면 scrap을 만들어줌
        if(scrap == null){
            scrap = Scrap.createScrap(member);
            scrapRepository.save(scrap);
        }

        ScrapCamp savedScrapCamp = scrapCampRepository.findByScrapIdAndCampId(scrap.getId(),camp.getId());


        //스크랩 안에 캠프가 있으면 // 스크랩에 캠프 중복 저장 방지
        if(savedScrapCamp != null){
            return savedScrapCamp.getId();
        }
        else{   //스크랩 안에 같은 캠프가 없으면 캠프를 스크랩에 생성해준다.
            ScrapCamp scrapCamp =  ScrapCamp.createScrapCamp(scrap, camp);
            scrapCampRepository.save(scrapCamp);
            return scrapCamp.getId();
        }
    }



    public boolean addScrap(Long campId, String email){

        //camp와 member 추출
        Camp camp = campRepository.findById(campId)
                .orElseThrow(EntityExistsException::new);
        Member member = memberRepository.findByEmail(email);

        //멤버 아이디로 스크랩을 빼냄
        Scrap scrap = scrapRepository.findByMemberId(member.getId());


        //고객에게
        //scrap이 없으면 scrap을 만들어줌
        if(scrap == null){
            scrap = Scrap.createScrap(member);
            scrapRepository.save(scrap);
        }

        ScrapCamp savedScrapCamp = scrapCampRepository.findByScrapIdAndCampId(scrap.getId(),camp.getId());


        //스크랩 안에 캠프가 있으면 // 스크랩에 캠프 중복 저장 방지
        if(savedScrapCamp != null){
            return false;
        }
        else{   //스크랩 안에 같은 캠프가 없으면 캠프를 스크랩에 생성해준다.
            ScrapCamp scrapCamp =  ScrapCamp.createScrapCamp(scrap, camp);
            scrapCampRepository.save(scrapCamp);
            return true;
        }
    }


    @Transactional(readOnly = true)
    public List<ScrapListDto> getScrapList(String email){
        List<ScrapListDto> scrapListDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);

        Scrap scrap = scrapRepository.findByMemberId(member.getId());
        if(scrap == null){
            return scrapListDtoList;
        }
        scrapListDtoList = scrapCampRepository.findScrapListDtoList(scrap.getId());
        return scrapListDtoList;
    }

    public void deleteScrap(ScrapCamp scrapCamp){
        scrapCampRepository.delete(scrapCamp);
    }



}
