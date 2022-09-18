package com.camper.controller;

import com.camper.dto.CampSearchDto;
import com.camper.dto.CateCheckDto;
import com.camper.dto.ScrapListDto;
import com.camper.dto.SearchDto;
import com.camper.entity.*;
import com.camper.repository.CampImgRepository;
import com.camper.repository.CampRepository;
import com.camper.repository.MemberRepository;
import com.camper.repository.UrlRepository;
import com.camper.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class MainController {
    PasswordEncoder passwordEncoder;
    private final CampService campService;

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final CampRepository campRepository;

    private final CampImgRepository campImgRepository;
    private final ScrapService scrapService;

    private final RoomService roomService;

    private final UrlRepository urlRepository;

    private final UrlService urlService;


    /* 메인문 컨트롤러 */
    @GetMapping("/")
    public String main(Model model) {

        List<Camp> campListForRank = campService.getAllCampsForRankByDesc();
        List<CampSearchDto> campSearchDtoList = new ArrayList<>();
        int size = 0;
        if (campListForRank.size() != 0) {
            if (campListForRank.get(0).getCount() != 0) {
                for (int i = 0; i < campListForRank.size(); i++) {
                    Camp camp = campListForRank.get(i);
                }
                for (int i = 0; i < campListForRank.size(); i++) {
                    CampImg campImg = campImgRepository.findByCampId(campListForRank.get(i).getId());
                    CampSearchDto c = new CampSearchDto();
                    c.setId(campListForRank.get(i).getId());
                    c.setCampName(campListForRank.get(i).getCampName());
                    c.setCampArea(campListForRank.get(i).getCampArea());
                    c.setSurroundings(campListForRank.get(i).getCampInfo());
                    c.setCampInfo(campListForRank.get(i).getCampInfo());
                    c.setImgUrl(campImg.getImgUrl());
                    c.setPermission(campListForRank.get(i).getPermission());
                    if (c.getPermission().equals("OK")) {
                        campSearchDtoList.add(c);
                        size++;
                        if (size == 5) {
                            break;
                        }
                    }
                }
            }
        }

        Member admin = Member.createAdmin(passwordEncoder); //어드민을 만들고 리턴
        Member whereAdmin = memberRepository.findByEmail(admin.getEmail());
        if (whereAdmin == null) { //어드민이 없을경우
            memberService.saveMember(admin);
        }//어드민이 이미 존재할 경우 어드민 아이디 저장 x

        model.addAttribute("campSearchDtoList", campSearchDtoList);
        return "/main";
    }

    @GetMapping("/campSearch/byKeyword")
    public String campSearchByBar(SearchDto searchDto, Optional<Integer> page, Model model, Principal principal) throws Exception{
        Pageable pageable = (Pageable) PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        if (searchDto.getSearchQuery() == null) {
            searchDto.setSearchQuery("");
        }
        Page<CampSearchDto> campList = campService.getCampPage(searchDto, pageable);

        try {
            String email = principal.getName(); //비로그인이란 가정하에 email => null
            Member member = memberRepository.findByEmail(email); // null => member객체 => 에러가 발생

        }catch (Exception e){ //email null값이 나올 경우 catch로 올것임
            List<ScrapListDto> scrapListDtoList2 = null;
            model.addAttribute("scrapCamps", scrapListDtoList2);
            model.addAttribute("campList", campList);
            model.addAttribute("searchDto", searchDto);
            model.addAttribute("maxPage", 5);
            model.addAttribute("cateCheckDto", new CateCheckDto());
            return "list/campSearch";
        }

        List<ScrapListDto> scrapListDtoList = scrapService.getScrapList(principal.getName());

        model.addAttribute("scrapCamps", scrapListDtoList);
        model.addAttribute("campList", campList);
        model.addAttribute("searchDto", searchDto);
        model.addAttribute("maxPage", 5);

        // category check TEST
        model.addAttribute("cateCheckDto", new CateCheckDto());
        return "list/campSearch";
    }

    /* 캠핑장 아이콘 클릭시 */
    @GetMapping("/campSearch/byKeyword/camping")
    public String campingSearch(SearchDto searchDto, Optional<Integer> page, Model model, Principal principal, HttpServletRequest request) {
        Pageable pageable = (Pageable) PageRequest.of(page.isPresent() ? page.get() : 0, 5);

//        // 본인 URL 저장
//        String email = principal.getName();
//        Member member = memberRepository.findByEmail(email);
//        Url checkUrl = urlRepository.findByUrl(member.getId()); //해당 멤버가 가지고 있는 그게 있나?
//
//        if (checkUrl == null) { //Url이 만들어져있지 않다면
//            Url url = Url.createUrl(request.getRequestURI(), member);
//            urlRepository.save(url);
//        } else if (checkUrl != null) { //Url이 이미 만들어져 있다면
//            urlService.updateUrl(request.getRequestURI(), member.getId());
//        }
//
//        ///////////////


        searchDto.setSearchQuery("캠핑");
        try {
            // room => camp_id 리스트들 가져오기
            List<Long> RoomCampIdList = campService.getAllRoomByCamp(searchDto.getSearchQuery()); //검색한 내용을 가지고 가서 관련 객실 싹 가져옴.

            //캠프 리스트 생성
            List<Camp> campListGo = new ArrayList<>();
            //for문을 돌려 camp_id로 camp 객체를 생성하여 campListGo에 넣어줌
            for (int i = 0; i < RoomCampIdList.size(); i++) {
                Camp c = campRepository.findById(RoomCampIdList.get(i)).orElseThrow(EntityNotFoundException::new);
                campListGo.add(c);
            }
            //새로운 camplist 완성
            //끝

            List<CampSearchDto> campSearchDtoList = new ArrayList<>(campListGo.size());
            for (int i = 0; i < campListGo.size(); i++) {
                CampImg campImg = campImgRepository.findByCampId(campListGo.get(i).getId());
                CampSearchDto c = new CampSearchDto();
                c.setId(campListGo.get(i).getId());
                c.setCampName(campListGo.get(i).getCampName());
                c.setCampArea(campListGo.get(i).getCampArea());
                c.setSurroundings(campListGo.get(i).getSurroundings());
                c.setCampInfo(campListGo.get(i).getCampInfo());
                c.setImgUrl(campImg.getImgUrl());

                // campSearch 객실 최대/최소가격 출력
                List<Room> roomList = roomService.getRoomList(campListGo.get(i).getId()); //해당 캠프id에 관한 룸리스트 받기
                int max = roomList.get(0).getRoomPrice();
                int min = roomList.get(0).getRoomPrice();
                for (int j = 0; j < roomList.size(); j++) {
                    if (max < roomList.get(j).getRoomPrice()) {
                        max = roomList.get(j).getRoomPrice();
                    }
                }
                for (int j = 0; j < roomList.size(); j++) {
                    if (min > roomList.get(j).getRoomPrice()) {
                        min = roomList.get(j).getRoomPrice();
                    }
                }
                c.setMax(max);
                c.setMin(min);
                //

                c.setPermission(campListGo.get(i).getPermission());
                if (c.getPermission().equals("OK")) {
                    campSearchDtoList.add(c);
                }
            }
            try {
                // 본인 URL 저장
                String email = principal.getName();
                Member member = memberRepository.findByEmail(email);
                Url checkUrl = urlRepository.findByUrl(member.getId()); //해당 멤버가 가지고 있는 그게 있나?

                if (checkUrl == null) { //Url이 만들어져있지 않다면
                    Url url = Url.createUrl(request.getRequestURI(), member);
                    urlRepository.save(url);
                } else if (checkUrl != null) { //Url이 이미 만들어져 있다면
                    urlService.updateUrl(request.getRequestURI(), member.getId());
                }
            }catch (Exception e){ //email null값이 나올 경우 catch로 올것임
                List<ScrapListDto> scrapListDtoList2 = null;
                model.addAttribute("scrapCamps", scrapListDtoList2);
                model.addAttribute("campList", campSearchDtoList);
                model.addAttribute("searchDto", searchDto);
                model.addAttribute("maxPage", 5);

                // category check TEST
                model.addAttribute("cateCheckDto", new CateCheckDto());
                return "list/campSearch";
            }
            List<ScrapListDto> scrapListDtoList = scrapService.getScrapList(principal.getName());


            model.addAttribute("scrapCamps", scrapListDtoList);
            model.addAttribute("campList", campSearchDtoList);
            model.addAttribute("searchDto", searchDto);
            model.addAttribute("maxPage", 5);

            // category check TEST
            model.addAttribute("cateCheckDto", new CateCheckDto());
            return "list/campSearch";
        } catch (Exception e) {
            return "main";
        }
    }

    /* 글램핑 아이콘 클릭시 */
    @GetMapping("/campSearch/byKeyword/glamping")
    public String glampingSearch(SearchDto searchDto, Model model, Principal principal, HttpServletRequest request) {

//        // 본인 URL 저장
//        String email = principal.getName();
//        Member member = memberRepository.findByEmail(email);
//        Url checkUrl = urlRepository.findByUrl(member.getId()); //해당 멤버가 가지고 있는 그게 있나?
//
//        if (checkUrl == null) { //Url이 만들어져있지 않다면
//            Url url = Url.createUrl(request.getRequestURI(), member);
//            urlRepository.save(url);
//        } else if (checkUrl != null) { //Url이 이미 만들어져 있다면
//            urlService.updateUrl(request.getRequestURI(), member.getId());
//        }
//
//        ///////////////


        searchDto.setSearchQuery("글램핑");
        try {
            // room => camp_id 리스트들 가져오기
            List<Long> RoomCampIdList = campService.getAllRoomByCamp(searchDto.getSearchQuery()); //검색한 내용을 가지고 가서 관련 객실 싹 가져옴.

            //캠프 리스트 생성
            List<Camp> campListGo = new ArrayList<>();
            //for문을 돌려 camp_id로 camp 객체를 생성하여 campListGo에 넣어줌
            for (int i = 0; i < RoomCampIdList.size(); i++) {
                Camp c = campRepository.findById(RoomCampIdList.get(i)).orElseThrow(EntityNotFoundException::new);
                campListGo.add(c);
            }
            //새로운 camplist 완성
            //끝

            List<CampSearchDto> campSearchDtoList = new ArrayList<>(campListGo.size());
            for (int i = 0; i < campListGo.size(); i++) {
                CampImg campImg = campImgRepository.findByCampId(campListGo.get(i).getId());
                CampSearchDto c = new CampSearchDto();
                c.setId(campListGo.get(i).getId());
                c.setCampName(campListGo.get(i).getCampName());
                c.setCampArea(campListGo.get(i).getCampArea());
                c.setSurroundings(campListGo.get(i).getSurroundings());
                c.setCampInfo(campListGo.get(i).getCampInfo());
                c.setImgUrl(campImg.getImgUrl());

                // campSearch 객실 최대/최소가격 출력
                List<Room> roomList = roomService.getRoomList(campListGo.get(i).getId()); //해당 캠프id에 관한 룸리스트 받기
                int max = roomList.get(0).getRoomPrice();
                int min = roomList.get(0).getRoomPrice();
                for (int j = 0; j < roomList.size(); j++) {
                    if (max < roomList.get(j).getRoomPrice()) {
                        max = roomList.get(j).getRoomPrice();
                    }
                }
                for (int j = 0; j < roomList.size(); j++) {
                    if (min > roomList.get(j).getRoomPrice()) {
                        min = roomList.get(j).getRoomPrice();
                    }
                }
                c.setMax(max);
                c.setMin(min);
                //


                c.setPermission(campListGo.get(i).getPermission());
                if (c.getPermission().equals("OK")) {
                    campSearchDtoList.add(c);
                }
            }
            try {
                // 본인 URL 저장
                String email = principal.getName();
                Member member = memberRepository.findByEmail(email);
                Url checkUrl = urlRepository.findByUrl(member.getId()); //해당 멤버가 가지고 있는 그게 있나?

                if (checkUrl == null) { //Url이 만들어져있지 않다면
                    Url url = Url.createUrl(request.getRequestURI(), member);
                    urlRepository.save(url);
                } else if (checkUrl != null) { //Url이 이미 만들어져 있다면
                    urlService.updateUrl(request.getRequestURI(), member.getId());
                }
            }catch (Exception e){ //email null값이 나올 경우 catch로 올것임
                List<ScrapListDto> scrapListDtoList2 = null;
                model.addAttribute("scrapCamps", scrapListDtoList2);
                model.addAttribute("campList", campSearchDtoList);
                model.addAttribute("searchDto", searchDto);
                model.addAttribute("maxPage", 5);

                // category check TEST
                model.addAttribute("cateCheckDto", new CateCheckDto());
                return "list/campSearch";
            }
            List<ScrapListDto> scrapListDtoList = scrapService.getScrapList(principal.getName());

            model.addAttribute("scrapCamps", scrapListDtoList);

            model.addAttribute("campList", campSearchDtoList);
            model.addAttribute("searchDto", searchDto);
            model.addAttribute("maxPage", 5);

            // category check TEST
            model.addAttribute("cateCheckDto", new CateCheckDto());
            return "list/campSearch";
        } catch (Exception e) {
            return "main";
        }
    }

    /* 카라반 아이콘 클릭시 */
    @GetMapping("/campSearch/byKeyword/caravan")
    public String caravanSearch(SearchDto searchDto, Optional<Integer> page, Model model, Principal principal, HttpServletRequest request) {
        Pageable pageable = (Pageable) PageRequest.of(page.isPresent() ? page.get() : 0, 5);

//        // 본인 URL 저장
//        String email = principal.getName();
//        Member member = memberRepository.findByEmail(email);
//        Url checkUrl = urlRepository.findByUrl(member.getId()); //해당 멤버가 가지고 있는 그게 있나?
//
//        if (checkUrl == null) { //Url이 만들어져있지 않다면
//            Url url = Url.createUrl(request.getRequestURI(), member);
//            urlRepository.save(url);
//        } else if (checkUrl != null) { //Url이 이미 만들어져 있다면
//            urlService.updateUrl(request.getRequestURI(), member.getId());
//        }
//
//        ///////////////


        searchDto.setSearchQuery("카라반");
        try {
            // room => camp_id 리스트들 가져오기
            List<Long> RoomCampIdList = campService.getAllRoomByCamp(searchDto.getSearchQuery()); //검색한 내용을 가지고 가서 관련 객실 싹 가져옴.

            //캠프 리스트 생성
            List<Camp> campListGo = new ArrayList<>();
            //for문을 돌려 camp_id로 camp 객체를 생성하여 campListGo에 넣어줌
            for (int i = 0; i < RoomCampIdList.size(); i++) {
                Camp c = campRepository.findById(RoomCampIdList.get(i)).orElseThrow(EntityNotFoundException::new);
                campListGo.add(c);
            }
            //새로운 camplist 완성
            //끝

            List<CampSearchDto> campSearchDtoList = new ArrayList<>(campListGo.size());
            for (int i = 0; i < campListGo.size(); i++) {
                CampImg campImg = campImgRepository.findByCampId(campListGo.get(i).getId());
                CampSearchDto c = new CampSearchDto();
                c.setId(campListGo.get(i).getId());
                c.setCampName(campListGo.get(i).getCampName());
                c.setCampArea(campListGo.get(i).getCampArea());
                c.setSurroundings(campListGo.get(i).getSurroundings());
                c.setCampInfo(campListGo.get(i).getCampInfo());
                c.setImgUrl(campImg.getImgUrl());

                // campSearch 객실 최대/최소가격 출력
                List<Room> roomList = roomService.getRoomList(campListGo.get(i).getId()); //해당 캠프id에 관한 룸리스트 받기
                int max = roomList.get(0).getRoomPrice();
                int min = roomList.get(0).getRoomPrice();
                for (int j = 0; j < roomList.size(); j++) {
                    if (max < roomList.get(j).getRoomPrice()) {
                        max = roomList.get(j).getRoomPrice();
                    }
                }
                for (int j = 0; j < roomList.size(); j++) {
                    if (min > roomList.get(j).getRoomPrice()) {
                        min = roomList.get(j).getRoomPrice();
                    }
                }
                c.setMax(max);
                c.setMin(min);
                //


                c.setPermission(campListGo.get(i).getPermission());
                if (c.getPermission().equals("OK")) {
                    campSearchDtoList.add(c);
                }
            }
            try {
                // 본인 URL 저장
                String email = principal.getName();
                Member member = memberRepository.findByEmail(email);
                Url checkUrl = urlRepository.findByUrl(member.getId()); //해당 멤버가 가지고 있는 그게 있나?

                if (checkUrl == null) { //Url이 만들어져있지 않다면
                    Url url = Url.createUrl(request.getRequestURI(), member);
                    urlRepository.save(url);
                } else if (checkUrl != null) { //Url이 이미 만들어져 있다면
                    urlService.updateUrl(request.getRequestURI(), member.getId());
                }
            }catch (Exception e){ //email null값이 나올 경우 catch로 올것임
                List<ScrapListDto> scrapListDtoList2 = null;
                model.addAttribute("scrapCamps", scrapListDtoList2);
                model.addAttribute("campList", campSearchDtoList);
                model.addAttribute("searchDto", searchDto);
                model.addAttribute("maxPage", 5);

                // category check TEST
                model.addAttribute("cateCheckDto", new CateCheckDto());
                return "list/campSearch";
            }
            List<ScrapListDto> scrapListDtoList = scrapService.getScrapList(principal.getName());

            model.addAttribute("scrapCamps", scrapListDtoList);
            model.addAttribute("campList", campSearchDtoList);
            model.addAttribute("searchDto", searchDto);
            model.addAttribute("maxPage", 5);

            // category check TEST
            model.addAttribute("cateCheckDto", new CateCheckDto());
            return "list/campSearch";
        } catch (Exception e) {
            return "main";
        }
    }


}