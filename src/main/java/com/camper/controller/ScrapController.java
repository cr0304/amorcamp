
package com.camper.controller;

import com.camper.dto.ScrapCampDto;
import com.camper.dto.ScrapListDto;
import com.camper.entity.Member;
import com.camper.entity.Url;
import com.camper.repository.MemberRepository;
import com.camper.repository.UrlRepository;
import com.camper.service.ScrapService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    private final MemberRepository memberRepository;

    private final UrlRepository urlRepository;

    @GetMapping(value = "/scrapList/{campId}")
    public String scrapList(@PathVariable("campId")Long campId, Principal principal,Model model) {

        String email = principal.getName();
        Member member = memberRepository.findByEmail(principal.getName());
        Long memberId = member.getId();
        Url url = urlRepository.findByUrl(memberId);
        String urlname = url.getUrl();
        //Long scrapCampId;
        boolean chkScrap = scrapService.addScrap(campId, email);
        if (chkScrap == false){
            model.addAttribute("errorMessage", "이미 스크랩 저장이 되어있는 캠핑장입니다.");
            return "redirect:"+urlname;
        } //자동 alert 메시지 나옴.
        try{
            scrapService.addScrap2(campId,email);
        }catch (Exception e) {
            model.addAttribute("errorMessage", "스크랩 중에 오류가 발생하였습니다.");
            return "redirect:"+urlname;
        }
        return "redirect:"+urlname;
    }




}
