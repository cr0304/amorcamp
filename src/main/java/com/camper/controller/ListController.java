package com.camper.controller;

import com.camper.dto.*;
import com.camper.entity.*;
import com.camper.repository.*;
import com.camper.service.CampService;
import com.camper.service.ReviewService;
import com.camper.service.RoomService;
import com.camper.service.ScrapService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class ListController {

    private final CampService campService;

    private final CampRepository campRepository;

    private final CampImgRepository campImgRepository;

    private final RoomService roomService;

    private final RoomImgRepository roomImgRepository;

    private final ScrapCampRepository scrapCampRepository;

    private final ScrapService scrapService;

    private final MemberRepository memberRepository;

    private final UrlRepository urlRepository;

    private final ReviewService reviewService;



    @GetMapping(value = "/campSearch")
    public String campForm() {

        return "list/campSearch";
    }


    @GetMapping(value = "/roomSearch/{campId}")
    public String roomForm(@PathVariable("campId") Long campId, Model model) {
        try {
            Camp camp = campRepository.findByCamp(campId);
            List<Room> roomList = roomService.getRoomList(campId); //캠프아이디에 해당하는 모든 객실 객체를 가져옴.

            List<MyRoomListDto> myRoomListDtoList = new ArrayList<>(roomList.size());//roomList크기만큼 DtoList를 만들어줌.
            List<RoomImg> roomImgList = new ArrayList<>();

            for (int i = 0; i < roomList.size(); i++) {//for문 시작
                roomImgList = roomImgRepository.findByRoomId(roomList.get(i).getId()); //이 아이디에 해당하는 모든 객실이미지 가져옴.
                //해당 객실의 대표 이미지만 추출
                RoomImg roomRepImg = null;
                for (int j = 0; j < roomImgList.size(); j++) {
                    if (roomImgList.get(j).getRepImgYn().equals("Y")) {
                        roomRepImg = roomImgList.get(j);
                    }
                }

                MyRoomListDto rl = new MyRoomListDto();

                //이미지 넣기
                rl.setSubImgUrl1(roomImgList.get(1).getImgUrl());
                rl.setSubImgUrl2(roomImgList.get(2).getImgUrl());
                rl.setSubImgUrl3(roomImgList.get(3).getImgUrl());
                rl.setSubImgUrl4(roomImgList.get(4).getImgUrl());

                // 끝
                rl.setId(roomList.get(i).getId());
                rl.setRoomName(roomList.get(i).getRoomName());
                rl.setRoomPrice(roomList.get(i).getRoomPrice());
                rl.setCampType(roomList.get(i).getCampType());
                rl.setRoomShortInfo(roomList.get(i).getRoomShortInfo());
                rl.setRoomDetailInfo(roomList.get(i).getRoomDetailInfo());
                rl.setImgUrl(roomRepImg.getImgUrl());
                rl.setSurroundings(camp.getSurroundings());
                rl.setCampArea(camp.getCampArea());
                myRoomListDtoList.add(rl);
            }

            CampSearchDto campSearchDto = new CampSearchDto();

            CampImg campImg = campImgRepository.findByCampId(camp.getId());
            campSearchDto.setId(camp.getId());
            campSearchDto.setCampName(camp.getCampName());
            campSearchDto.setCampArea(camp.getCampArea());
            campSearchDto.setSurroundings(camp.getSurroundings());
            campSearchDto.setAddress(camp.getAddress());
            campSearchDto.setInTime(camp.getInTime());
            campSearchDto.setOutTime(camp.getOutTime());
            campSearchDto.setCampInfo(camp.getCampInfo());
            campSearchDto.setImgUrl(campImg.getImgUrl());
            campSearchDto.setPermission(camp.getPermission());

            List<Review> reviewList = reviewService.getAllReviewByCampId(camp.getId());

            // 평균 별점 구하기
            int avg =0;
            if(reviewList.size() != 0) {
                // total / reviewList.size() = 0일때 = total/0 => "java.lang.ArithmeticException: / by zero" Error 발생
                int total = 0;

                for (Review r : reviewList) {
                    total += r.getRating();
                }
                avg = total / reviewList.size();
            }
            ////

            model.addAttribute("dateCheckDto", new DateCheckDto());
            model.addAttribute("reviewList",reviewList);
            model.addAttribute("camp", campSearchDto);
            model.addAttribute("roomList", myRoomListDtoList);
            model.addAttribute("avg",avg);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "객실을 불러오는 중에 발생하였습니다.");
            return "main";
        }
        return "list/roomSearch";
    }

    @GetMapping(value = "/categoryChk")
    public String categoryChk(CateCheckDto cateCheckDto, Model model, Principal principal) {
        String camping;
        String glamping;
        String caravan;
        String mountain;
        String sea;
        String vally;
        String city;
        String area;



        if (cateCheckDto.getCamping() != null) {
            camping = "캠핑";
        } else {
            camping = "";
        }

        if (cateCheckDto.getGlamping() != null) {
            glamping = "글램핑";
        } else {
            glamping = "";
        }

        if (cateCheckDto.getCaravan() != null) {
            caravan = "카라반";
        } else {
            caravan = "";
        }

        if (cateCheckDto.getMountain() != null) {
            mountain = "산";
        } else {
            mountain = "";
        }

        if (cateCheckDto.getSea() != null) {
            sea = "바다";
        } else {
            sea = "";
        }

        if (cateCheckDto.getVally() != null) {
            vally = "계곡";
        } else {
            vally = "";
        }

        if (cateCheckDto.getCity() != null) {
            city = "도심";
        } else {
            city = "";
        }

        if (cateCheckDto.getArea() != null) {
            area = cateCheckDto.getArea();
        } else {
            area = "";
        }

        try {
            List<Long> allCampTypeList = campService.getAllCampByCampType(camping, glamping, caravan);
            List<Camp> allCampList = new ArrayList<>();
            List<CampSearchDto> resultSearchDtolist = new ArrayList<>();

            for (int i = 0; i < allCampTypeList.size(); i++) {
                Camp camp = campRepository.findById(allCampTypeList.get(i)).orElseThrow(EntityNotFoundException::new);
                allCampList.add(camp);
            }
            List<Camp> allCampNatureAreaList = campService.getAllCampByNatureArea(mountain, sea, vally, city, area);

            List<Camp> resultAllCampList = new ArrayList<>();

            if (allCampList.size() == 0 && allCampNatureAreaList.size() == 0){
                model.addAttribute("campSearchDtoList", resultSearchDtolist); //todo
            } else if (allCampList.size()<allCampNatureAreaList.size()){
                if (allCampList.size() == 0 && allCampNatureAreaList.size()!= 0){
                    for (int i = 0; i < allCampNatureAreaList.size(); i++){
                        resultAllCampList.add(allCampNatureAreaList.get(i));
                    }
                }
                else {
                    for (int i = 0; i < allCampList.size(); i++){
                        for (int j = 0; j < allCampNatureAreaList.size(); j++){
                            if (allCampList.get(i).getCampName().equals(allCampNatureAreaList.get(j).getCampName())){
                                resultAllCampList.add(allCampList.get(i));
                            }
                        }
                    }
                }
            } else {
                if (allCampNatureAreaList.size() == 0 && allCampList.size()!= 0){
                    for (int i = 0; i < allCampList.size(); i++){
                        resultAllCampList.add(allCampList.get(i));
                    }
                }
                else{
                    for (int i = 0; i < allCampList.size(); i++){
                        for (int j = 0; j < allCampNatureAreaList.size(); j++){
                            if (allCampList.get(i).getCampName().equals(allCampNatureAreaList.get(j).getCampName())){
                                resultAllCampList.add(allCampList.get(i));
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < resultAllCampList.size(); i++){
                CampImg campImg = campImgRepository.findByCampId(resultAllCampList.get(i).getId());
                CampSearchDto campSearchDto = new CampSearchDto();
                campSearchDto.setId(resultAllCampList.get(i).getId());
                campSearchDto.setCampName(resultAllCampList.get(i).getCampName());
                campSearchDto.setCampArea(resultAllCampList.get(i).getCampArea());
                campSearchDto.setSurroundings(resultAllCampList.get(i).getSurroundings());
                campSearchDto.setCampInfo(resultAllCampList.get(i).getCampInfo());
                campSearchDto.setImgUrl(campImg.getImgUrl());
                campSearchDto.setPermission(resultAllCampList.get(i).getPermission());


                List<Room> roomList = roomService.getRoomList(resultAllCampList.get(i).getId()); //해당 캠프id에 관한 룸리스트 받기
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
                campSearchDto.setMin(min);
                if(campSearchDto.getPermission().equals("OK")){
                    resultSearchDtolist.add(campSearchDto);
                }
            }

            try {
                Member member = memberRepository.findByEmail(principal.getName());
            }catch (Exception e){ //email null값이 나올 경우 catch로 올것임
                List<ScrapListDto> scrapListDtoList2 = null;
                model.addAttribute("campSearchDtoList", resultSearchDtolist);
                model.addAttribute("scrapCamps", scrapListDtoList2);
                model.addAttribute("searchDto", new SearchDto());
                model.addAttribute("maxPage", 5);
                model.addAttribute("cateCheckDto", cateCheckDto);
                return "list/campSearch";
            }

            List<ScrapListDto> scrapListDtoList = scrapService.getScrapList(principal.getName());

            model.addAttribute("campSearchDtoList", resultSearchDtolist);
            model.addAttribute("scrapCamps", scrapListDtoList);
            model.addAttribute("searchDto", new SearchDto());
            model.addAttribute("maxPage", 5);
            model.addAttribute("cateCheckDto", cateCheckDto);
            return "list/campSearch";
        } catch (Exception e) {
            return "redirect:/main";
        }
    }


    @GetMapping(value = "/roomSearch/scrap/{scrapCampId}")
    public String ScrapRoomList(@PathVariable("scrapCampId") Long scrapCampId, Model model) {
        try {
            ScrapCamp scrapCamp = scrapCampRepository.findByScrapCampId(scrapCampId);
            Long campId = scrapCamp.getCamp().getId();
            List<Room> roomList = roomService.getRoomList(campId); //캠프아이디에 해당하는 모든 객실 객체를 가져옴.

            List<MyRoomListDto> myRoomListDtoList = new ArrayList<>(roomList.size());//roomList크기만큼 DtoList를 만들어줌.

            for (int i = 0; i < roomList.size(); i++) {//for문 시작
                List<RoomImg> roomImgList = roomImgRepository.findByRoomId(roomList.get(i).getId()); //이 아이디에 해당하는 모든 객실이미지 가져옴.
                //해당 객실의 대표 이미지만 추출
                RoomImg roomImg = null;
                for (int j = 0; j < roomImgList.size(); j++) {
                    if (roomImgList.get(j).getRepImgYn().equals("Y")) {
                        roomImg = roomImgList.get(j);
                    }
                }

                MyRoomListDto rl = new MyRoomListDto();
                rl.setId(roomList.get(i).getId());
                rl.setRoomName(roomList.get(i).getRoomName());
                rl.setRoomPrice(roomList.get(i).getRoomPrice());
                rl.setCampType(roomList.get(i).getCampType());
                rl.setRoomShortInfo(roomList.get(i).getRoomShortInfo());
                rl.setRoomDetailInfo(roomList.get(i).getRoomDetailInfo());
                rl.setImgUrl(roomImg.getImgUrl());
                myRoomListDtoList.add(rl);
            }
            Camp camp = campRepository.findByCamp(campId);

            CampSearchDto campSearchDto = new CampSearchDto();
            CampImg campImg = campImgRepository.findByCampId(camp.getId());
            campSearchDto.setId(camp.getId());
            campSearchDto.setCampName(camp.getCampName());
            campSearchDto.setCampArea(camp.getCampArea());
            campSearchDto.setSurroundings(camp.getSurroundings());
            campSearchDto.setAddress(camp.getAddress());
            campSearchDto.setInTime(camp.getInTime());
            campSearchDto.setOutTime(camp.getOutTime());
            campSearchDto.setCampInfo(camp.getCampInfo());
            campSearchDto.setImgUrl(campImg.getImgUrl());
            campSearchDto.setPermission(camp.getPermission());

            model.addAttribute("camp", campSearchDto);
            model.addAttribute("roomList", myRoomListDtoList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "객실을 불러오는 중에 발생하였습니다.");
            return "main";
        }
        return "list/roomSearch";
    }

    @GetMapping(value = "/roomSearch/delete/{scrapCampId}")
    public String DeleteScrap(@PathVariable("scrapCampId") Long scrapCampId, Model model, Principal principal) {
        try {
            //스크랩 삭제
            ScrapCamp scrapCamp = scrapCampRepository.findByScrapCampId(scrapCampId);
            scrapService.deleteScrap(scrapCamp);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "스크랩 삭제 중 에러가 발생하였습니다.");
            return "list/campSearch";
        }
        Member member = memberRepository.findByEmail(principal.getName());
        Long memberId = member.getId();
        Url url = urlRepository.findByUrl(memberId);

        return "redirect:" + url.getUrl();
    }


}