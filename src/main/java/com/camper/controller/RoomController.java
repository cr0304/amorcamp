package com.camper.controller;

import com.camper.dto.*;
import com.camper.entity.*;
import com.camper.repository.*;
import com.camper.service.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final MemberService memberService;

    private final BookingRepository bookingRepository;

    private final BookingService bookingService;

    private final CampService campService;

    private final CampRepository campRepository;

    private final CampImgRepository campImgRepository;
    private final MemberRepository memberRepository;

    private final RoomImgRepository roomImgRepository;

    private final ReviewService reviewService;

    //  객실 등록 페이지 불러오기
    @GetMapping(value = "/roomForm")
    public String roomForm(Principal principal,Model model){

        // 유효성 검사
        String email = principal.getName(); //현재 접속중인 이메일 가져오기
        Member member = memberRepository.findByEmail(email); //접속중인 이메일을 통해 현재 접속중인 멤버 객체를 가져옴
        try {
            if (member.getCamp().getId() == null) { //만약 캠프 등록을 안한 상태라면 ??
                //getID가 null값이라면 오류가 나게 되있다. 그렇다면 catch로
                //이동하게 될 것이고 오류메세지를 들고 메인페이지로 가게 될 것
            }
        }catch (Exception e){
            model.addAttribute("errorMessage2", "캠핑장 등록을 먼저 해주세요.");
            return "redirect:/";
        }

        // 유효성 검사에서 걸리는게 없다면 정상작동
        model.addAttribute("roomFormDto", new RoomFormDto());
        return "camp/roomForm";
    }

    /* room create controller */
    @PostMapping(value = "/roomForm")
    public String roomNew(@Valid RoomFormDto roomFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("roomImgFile") List<MultipartFile> roomImgFileList, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "camp/roomForm";
        }

        if (roomImgFileList.get(0).isEmpty() && roomFormDto.getId() == null) {
            model.addAttribute("errorMessage",
                    "첫번째 객실 이미지는 필수 입력 값입니다.");
            return "camp/roomForm";
        }

        try {
            roomService.saveRoom(roomFormDto, roomImgFileList, principal.getName());
        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "객실 등록 중 에러가 발생하였습니다.");
            return "camp/roomForm";
        }
        return "redirect:/";
    }

    /* room 정보 수정 페이지 접급 제한 */
    @GetMapping(value = "/roomFormUpdate/{roomId}")
    public String roomUpdatePage(@PathVariable("roomId")Long roomId, Model model){
        try {
            RoomFormDto roomFormDto = roomService.getRoomDtl(roomId);
            model.addAttribute("roomFormDto", roomFormDto);
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 객실입니다.");
            model.addAttribute("roomFormDto", new RoomFormDto());
            return "camp/roomFormUpdate";
        }
        return  "camp/roomFormUpdate";
    }

    @PostMapping(value = "/roomFormUpdate/{roomId}")
    public String roomDtl2(@Valid RoomFormDto roomFormDto, BindingResult bindingResult,
                           @RequestParam("roomImgFile") List<MultipartFile> roomImgFileList,
                           Model model){

        if(bindingResult.hasErrors()) {
            return "camp/roomFormUpdate";
        }
        if(roomImgFileList.get(0).isEmpty() && roomFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 객실 이미지는 필수 입력 값입니다.");
            return "camp/roomFormUpdate";
        }
        try{
            //업데이트캠프
            campService.updateRoom(roomFormDto, roomImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage", "객실 수정중 에러가 발생하였습니다.");
            return "camp/roomFormUpdate";
        }
        return "redirect:/myRoomList";

    }
    @GetMapping(value = "/roomFormDelete/{roomId}")
    public String deleteRoom(@PathVariable("roomId")Long roomId,Model model){
        try{
            //업데이트캠프
            roomService.deleteRoom(roomId);
        }catch (Exception e){
            model.addAttribute("errorMessage", "캠핑장 삭제 중 에러가 발생하였습니다.");
            return "camp/roomForm";
        }
        return "redirect:/myRoomList";
    }


    //예약 현황 뿌리기
    @GetMapping(value = {"/reservation", "/reservation/{page}"})
    public String manageReservation(@PathVariable("page") Optional<Integer> page, Model model, Principal principal){
        String email = principal.getName();
        Member member = memberService.findMember(email);
        Camp camp = member.getCamp();
        Long campId = camp.getId();
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        List<Booking> bookingList = bookingRepository.findByBookingList(campId);
        LocalDate today = LocalDate.now();

        for(int i=0;i<bookingList.size();i++){
            LocalDate d1 = bookingList.get(i).getCheckOut();
            int compare = today.compareTo(d1); //오늘날짜가 체크아웃날짜 비교
            if(compare > 0){ //오늘날짜가 체크아웃날짜보다 클때
                bookingService.updateBStatus(bookingList.get(i),"bUseEnd"); //업데이트 해줌.
            }
        }


        Page<Booking> bookings = roomService.findBookingByCamp(pageable, campId);

        model.addAttribute("bookings", bookings);
        model.addAttribute("maxPage",5);
        return "/camp/reservation";
    }


    @PostMapping(value = "/roomDateSearch/{campId}")
    public String roomDateSearch(@PathVariable("campId") Long campId, Model model, DateCheckDto dateCheckDto, Principal principal) throws Exception {


        if (dateCheckDto.getCheckIn().equals("") || dateCheckDto.getCheckOut().equals("")){
            return "redirect:/roomSearch/"+campId;
        }
        LocalDate checkIn = LocalDate.parse(dateCheckDto.getCheckIn());
        LocalDate checkOut = LocalDate.parse(dateCheckDto.getCheckOut());
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

        List<Room> availableRooms = bookingService.searchRoomsByDates(campId, checkIn, checkOut);

        List<MyRoomListDto> myRoomListDtoList = new ArrayList<>(availableRooms.size());//roomList크기만큼 DtoList를 만들어줌.

        for (int i = 0; i < availableRooms.size(); i++) {//for문 시작
            List<RoomImg> roomImgList = roomImgRepository.findByRoomId(availableRooms.get(i).getId()); //이 아이디에 해당하는 모든 객실이미지 가져옴.
            //해당 객실의 대표 이미지만 추출
            RoomImg roomImg = null;
            for (int j = 0; j < roomImgList.size(); j++) {
                if (roomImgList.get(j).getRepImgYn().equals("Y")) {
                    roomImg = roomImgList.get(j);
                }
            }
            MyRoomListDto rl = new MyRoomListDto();
            //이미지 넣기
            rl.setSubImgUrl1(roomImgList.get(1).getImgUrl());
            rl.setSubImgUrl2(roomImgList.get(2).getImgUrl());
            rl.setSubImgUrl3(roomImgList.get(3).getImgUrl());
            rl.setSubImgUrl4(roomImgList.get(4).getImgUrl());

            rl.setId(availableRooms.get(i).getId());
            rl.setRoomName(availableRooms.get(i).getRoomName());
            rl.setRoomPrice(availableRooms.get(i).getRoomPrice());
            rl.setCampType(availableRooms.get(i).getCampType());
            rl.setRoomShortInfo(availableRooms.get(i).getRoomShortInfo());
            rl.setRoomDetailInfo(availableRooms.get(i).getRoomDetailInfo());
            rl.setImgUrl(roomImg.getImgUrl());
            rl.setSurroundings(camp.getSurroundings());
            rl.setCampArea(camp.getCampArea());
            myRoomListDtoList.add(rl);

        }
        List<Room> roomList = roomService.getRoomList(campId); //캠프아이디에 해당하는 모든 객실 객체를 가져옴.
        List<RoomImg> roomImgList = new ArrayList<>();

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
        model.addAttribute("dateCheckDto",dateCheckDto);
        model.addAttribute("reviewList",reviewList);
        model.addAttribute("avg",avg);
        model.addAttribute("roomList", myRoomListDtoList);
        model.addAttribute("camp", campSearchDto);
        return "list/roomSearch2";
    }

}