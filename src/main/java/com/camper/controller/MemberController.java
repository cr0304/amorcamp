package com.camper.controller;

import com.camper.dto.BookingListDto;
import com.camper.dto.MemberFormDto;
import com.camper.entity.*;
import com.camper.repository.*;
import com.camper.service.BookingService;
import com.camper.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final BookingRepository bookingRepository;

    private final RoomRepository2 roomRepository2;

    private final RoomImgRepository roomImgRepository;

    private final CampRepository campRepository;

    private final BookingRepository2 bookingRepository2;

    private final BookingService bookingService;

    /* 로그인 페이지 불러오기 컨트롤러 */
    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/login";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "/member/login";
    }

    /* Find PW Popup Page*/
    @GetMapping(value = "/findPwPopup")
    public String findPwPopup(){
        return "/member/findPwPopup";
    }


    /* 회원가입 페이지 불러오기 컨트롤러 */

    @GetMapping(value = "/join")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/join";
    }

    @PostMapping(value = "/join")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) { // 각 변수명 체크 문제가 생기면 호출 이름 X 이메일 형식 X
            return "member/join";
        }
        try {

            Member member = Member.createMember(memberFormDto, passwordEncoder);
            //EmailSenderService.sendEmail(member.getEmail(),"Camper 인증 번호", number); //회원가입시에 이메일 인증번호 전송
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }
        return "redirect:/login";
    }

    // 내 정보
    @GetMapping(value = "/myInfo")
    public String myInfo(Principal principal, Model model){

        try {
            MemberFormDto memberFormDto = memberService.getMemberInfo(principal.getName()); //현재 접속중인 이메일을 가지고 감.
            model.addAttribute("memberFormDto", memberFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 유저입니다.");
            return "redirect:/";
        }
        return "member/myInfo";
    }

    @PostMapping(value = "/myInfo")
    public String changeMyInfo(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,Principal principal,
                               Model model){
        if (bindingResult.hasErrors()) { // 각 변수명 체크 문제가 생기면 호출 이름 X 이메일 형식 X
            return "member/myInfo";
        }

        try {
            String name = principal.getName(); //현재 접속중인 유저의 이메일을 추출
            Member oldMember = memberRepository.findByEmail(name); //현재 접속중인 유저 member 객체를 가져옴
            memberService.updateMember(memberFormDto,passwordEncoder,oldMember);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", "회원정보 수정이 실패하였습니다.");
            return "member/myInfo";
        }
        return "redirect:/";

    }



    // 내 예약 확인
    @GetMapping(value = "/bookingList")
    public String bookingList(Model model,Principal principal){
        String email = principal.getName(); //이메일
        Member member = memberRepository.findByEmail(email); //현재 접속 멤버 객체 추출
        try{
            //멤버 id를 가지고 이 멤버 id가 예약한 모든 예약건수를 가져옴.
            List<Booking> booking = bookingRepository.findByBooking(member.getId());
            List<BookingListDto> bookingListDtoList = new ArrayList<>();
            for(int i=0; i<booking.size(); i++){

                Room room = roomRepository2.findByRoom(booking.get(i).getRoom().getId());
                Camp camp = campRepository.findByCamp(booking.get(i).getCamp().getId());
                List<RoomImg> roomImgList = roomImgRepository.findByRoomId(room.getId());
                RoomImg roomImg = null;

                //대표 객실이미지만 뽑아오기
                for(int j=0;j<roomImgList.size();j++){
                    if(roomImgList.get(j).getRepImgYn().equals("Y")){
                        roomImg = roomImgList.get(j);
                    }
                }
                BookingListDto bookingListDto = new BookingListDto();
                bookingListDto.setId(booking.get(i).getId());
                bookingListDto.setCampName(booking.get(i).getCampName());
                bookingListDto.setRoomName(booking.get(i).getRoomName());
                bookingListDto.setRoomPrice(room.getRoomPrice());
                bookingListDto.setCheckIn(booking.get(i).getCheckIn());
                bookingListDto.setCheckOut(booking.get(i).getCheckOut());
                bookingListDto.setRoomDetailInfo(room.getRoomDetailInfo());
                bookingListDto.setRoomDetailInfo(room.getRoomShortInfo());
                bookingListDto.setImgUrl(roomImg.getImgUrl());
                bookingListDto.setCampType(room.getCampType());
                bookingListDto.setCampId(camp.getId());
                bookingListDto.setRoomId(room.getId());
                bookingListDto.setCampArea(camp.getCampArea());
                bookingListDto.setSurroundings(camp.getSurroundings());

                //근데 여기서 오늘날짜를 지났다면?? booking의 BStatus를 update해줘야함.
                LocalDate today = LocalDate.now();

                for(int j=0;j<booking.size();j++){
                    LocalDate d1 = booking.get(j).getCheckOut();
                    int compare = today.compareTo(d1); //오늘날짜가 체크아웃날짜 비교
                    if(compare > 0){ //오늘날짜가 체크아웃날짜보다 클때
                        bookingService.updateBStatus(booking.get(j),"bUseEnd"); //업데이트 해줌.
                    }
                }
                bookingListDto.setBStatus(booking.get(i).getBStatus()); //bookingListDto에 BStatus를 set해줌.

                bookingListDtoList.add(bookingListDto);
            }
            model.addAttribute("bookingList",bookingListDtoList);
            return "member/bookingList";
        }catch (Exception e){
            return "main";
        }
    }

    ///changeBooking/
    @GetMapping(value = "/changeBooking/{bookingId}")
    public String roomForm(@PathVariable("bookingId") Long bookingId, Principal principal, Model model) {
        try {
            String email = principal.getName();
            Member member = memberRepository.findByEmail(email);
            //Room room = roomRepository2.findByRoom(roomId);
            Booking booking = bookingRepository2.findByBooking(member.getId(),bookingId);
            bookingService.updateBStatus(booking, "bCancel");
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/bookingList";
        }
        return "redirect:/bookingList";
    }

}
