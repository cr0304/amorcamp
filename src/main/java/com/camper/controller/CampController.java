package com.camper.controller;

import com.camper.dto.CampFormDto;
import com.camper.dto.MyRoomListDto;
import com.camper.dto.RoomFormDto;
import com.camper.entity.Member;
import com.camper.entity.Room;
import com.camper.entity.RoomImg;
import com.camper.repository.MemberRepository;
import com.camper.repository.RoomImgRepository;
import com.camper.service.CampService;
import com.camper.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor    // final이나 @NotNull이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class CampController {

    private final CampService campService;

    private final RoomService roomService;

    private final MemberRepository memberRepository;

    private final RoomImgRepository roomImgRepository;

    /* 캠핑장 등록 페이지 불러오기 컨트롤러 */
    @GetMapping(value = "/campForm")
    public String campForm(Model model){

        model.addAttribute("campFormDto", new CampFormDto());
        return "camp/campForm";
    }

    // 캠핑장 등록 완료 컨트롤러
    @PostMapping(value = "/campForm")
    public String campNew(@Valid CampFormDto campFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("campImgFile") MultipartFile campImgFile, Principal principal) {


        if(bindingResult.hasErrors()) {
            return "camp/campForm";
        }
        if(campImgFile.isEmpty() && campFormDto.getId() == null){
            model.addAttribute("errorMessage", "캠핑장 대표 이미지를 등록해주세요.");
            return "camp/campForm";
        }
        try{
            campFormDto.setPermission("WAIT");
            campFormDto.setRatingAvg(0);
            campService.saveCamp(campFormDto, campImgFile, principal.getName());
        }catch (Exception e){
            model.addAttribute("errorMessage", "캠핑장 등록중 에러가 발생하였습니다.");
            return "camp/campForm";
        }
        return "redirect:/";

    }

    /* 캠핑장 중복 등록 방지 컨트롤러 */
    @GetMapping(value = "/campFormChk")
    public @ResponseBody ResponseEntity campFormChk1(Principal principal){
        String email = principal.getName(); //이메일 뽑아오기
        Member m;
        try {
            m = memberRepository.findByEmail(email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if(m.getCamp() != null){ // camp가 있으면
            return new ResponseEntity<String>("NO", HttpStatus.OK);
        }
        return new ResponseEntity<String>("YES", HttpStatus.OK);
    }

    // 캠핑장 정보 수정 접근 제한
    @GetMapping(value = "/campFormChk2")
    public @ResponseBody ResponseEntity campFormChk2(Principal principal){
        String email = principal.getName(); //이메일 뽑아오기
        Member m;
        try {
            m = memberRepository.findByEmail(email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if(m.getCamp() != null){
            Long campId = m.getCamp().getId();
            return new ResponseEntity<Long>(campId, HttpStatus.OK);
        }

        return new ResponseEntity<String>("YES", HttpStatus.OK);
    }


    /* 캠핑장 정보 수정 페이지 접급 제한 */
   @GetMapping(value = "/campFormUpdate/{campId}")
    public String campDtl(@PathVariable("campId")Long campId, Model model){
        try {
            CampFormDto campFormDto = campService.getCampDtl(campId);
            model.addAttribute("campFormDto", campFormDto);
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 캠핑장입니다.");
            model.addAttribute("campFormDto", new CampFormDto());
            return "camp/campFormUpdate";
        }
        return  "camp/campFormUpdate";
    }
    // 캠핑장 정보 수정 완료
    @PostMapping(value = "/campFormUpdate/{campId}")
    public String campDtl2(@Valid CampFormDto campFormDto, BindingResult bindingResult,
                           Model model, @RequestParam("campImgFile") MultipartFile campImgFile){

        if(bindingResult.hasErrors()) {
            return "camp/campFormUpdate";
        }
        if(campImgFile.isEmpty() && campFormDto.getId() == null){
            model.addAttribute("errorMessage", "캠핑장 대표 이미지를 등록해주세요.");
            return "camp/campFormUpdate";
        }
        try{
            //업데이트캠프
            campService.updateCamp(campFormDto, campImgFile);
        }catch (Exception e){
            model.addAttribute("errorMessage", "캠핑장 수정중 에러가 발생하였습니다.");
            return "camp/campFormUpdate";
        }
        return "redirect:/";
    }


    @GetMapping(value = "/myRoomList")
    public String myRoomList(Model model,Principal principal){

        String email = principal.getName(); //현재 접속중인 이메일 추출
        Member m = memberRepository.findByEmail(email); // 이메일을 통해 현재 접속중인 멤버객체 추출
        Long campId = m.getCamp().getId(); // 멤버엔티티 안에 있는 캠프아이디를 가져옴.

        try {
            List<Room> roomList = roomService.getRoomList(campId); //캠프아이디에 해당하는 모든 객실 객체를 가져옴. 3개

            List<MyRoomListDto> myRoomListDtoList = new ArrayList<>(roomList.size());//roomList크기만큼 DtoList를 만들어줌.

            for(int i=0;i<roomList.size();i++){//for문 시작
                List<RoomImg> roomImgList = roomImgRepository.findByRoomId(roomList.get(i).getId()); //이 아이디에 해당하는 모든 객실이미지 가져옴.
                //해당 객실의 대표 이미지만 추출
                RoomImg roomImg = null;
                for(int j=0;j<roomImgList.size();j++){
                    if(roomImgList.get(j).getRepImgYn().equals("Y")){
                        roomImg = roomImgList.get(j);
                    }
                }

                MyRoomListDto rl = new MyRoomListDto();

                //이미지 넣기
                rl.setSubImgUrl1(roomImgList.get(1).getImgUrl());
                rl.setSubImgUrl2(roomImgList.get(2).getImgUrl());
                rl.setSubImgUrl3(roomImgList.get(3).getImgUrl());
                rl.setSubImgUrl4(roomImgList.get(4).getImgUrl());

                rl.setId(roomList.get(i).getId());
                rl.setRoomName(roomList.get(i).getRoomName());
                rl.setRoomPrice(roomList.get(i).getRoomPrice());
                rl.setCampType(roomList.get(i).getCampType());
                rl.setRoomShortInfo(roomList.get(i).getRoomShortInfo());
                rl.setRoomDetailInfo(roomList.get(i).getRoomDetailInfo());
                rl.setImgUrl(roomImg.getImgUrl());
                myRoomListDtoList.add(rl);
            }


            model.addAttribute("roomList", myRoomListDtoList);
            return "camp/myRoomList";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "객실 관리 접속에러가 발생하였습니다.");
            return "main";
        }
    }



    // 예약현황 페이지 불러오기




}