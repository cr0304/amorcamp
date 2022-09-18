package com.camper.service;

import com.camper.dto.CampFormDto;
import com.camper.dto.RoomFormDto;
import com.camper.dto.RoomImgDto;
import com.camper.entity.*;
import com.camper.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomImgService roomImgService;
    private final RoomImgRepository roomImgRepository;

    private final RoomRepository2 roomRepository2;

    private final MemberRepository memberRepository;


    private final BookingRepository bookingRepository;


    public Long saveRoom(RoomFormDto roomFormDto, List<MultipartFile> roomImgFileList, String email) throws Exception{

        //객실 등록
        Room room = roomFormDto.createRoom();
        Member member = memberRepository.findByEmail(email);
        Camp camp = member.getCamp();
        room.setCamp(camp);
        roomRepository.save(room);

        for (int i = 0; i < roomImgFileList.size(); i++){
            RoomImg roomImg = new RoomImg();
            roomImg.setRoom(room);

            if (i == 0){
                roomImg.setRepImgYn("Y");
            }
            else{
                roomImg.setRepImgYn("N");
            }
            roomImgService.saveRoomImg(roomImg, roomImgFileList.get(i));
        }
        return room.getId();
    }

    public void deleteRoom(Long roomId) {

        Room room = roomRepository2.findByRoom(roomId); //roomId로 room 객체 찾아옴
        List<RoomImg> roomImgList = roomImgRepository.findByRoomImg(room.getId()); //이와 같은 roomId를 가진 roomImg를 싹 다 가져와라

        roomRepository.delete(room); //room 삭제
        for(int i=0;i<roomImgList.size();i++){
            roomImgRepository.delete(roomImgList.get(i)); //room_img들 삭제
        }
    }

    @Transactional(readOnly = true)
    public RoomFormDto getRoomDtl(Long roomId) {
        List<RoomImg> roomImgList = roomImgRepository.findByRoomIdOrderByIdAsc(roomId);

        List<RoomImgDto> roomImgDtoList = new ArrayList<>();

        for(RoomImg roomimg : roomImgList){ // ItemImg 엔티티를 ItemImgDto 객체를 만들어서 리스트에 추가
            RoomImgDto roomImgDto = RoomImgDto.of(roomimg);
            roomImgDtoList.add(roomImgDto);
        }

        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);
        RoomFormDto roomFormDto = RoomFormDto.of(room);
        roomFormDto.setRoomImgDtoList(roomImgDtoList);
        return roomFormDto;
    }

    public List<Room> getRoomList(Long campId) throws Exception{
        return roomRepository.findByRoom(campId);
    }

    public Page<Booking> findBookingByCamp(Pageable pageable, Long campId) {
        return bookingRepository.findAllByCampId(pageable, campId);
    }


}
