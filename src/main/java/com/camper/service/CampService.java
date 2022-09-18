package com.camper.service;

import com.camper.dto.*;
import com.camper.entity.*;
import com.camper.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CampService {

    private final CampRepository campRepository;

    private final CampImgService campImgService;

    private final CampImgRepository campImgRepository;

    private final MemberRepository memberRepository;

    private final CampRoomRepository campRoomRepository;

    private final RoomRepository roomRepository;

    private final RoomImgService roomImgService;


    public Long saveCamp(CampFormDto campFormDto, MultipartFile itemImgFile, String email) throws Exception{
        //캠프 등록
        Camp camp = campFormDto.createCamp();
        Member member = memberRepository.findByEmail(email);
        member.setCamp(camp);
        campRepository.save(camp);
        //이미지 등록
        CampImg campImg = new CampImg();
        campImg.setCamp(camp);
        campImgService.saveCampImg(campImg, itemImgFile);
        return camp.getId();
    }


    public Long updateCamp(CampFormDto campFormDto, MultipartFile campImgFile)
            throws Exception{
        Camp camp = campRepository.findById(campFormDto.getId()).
                orElseThrow(EntityNotFoundException::new);
        camp.updateCamp(campFormDto);

        Long campImgId = campFormDto.getCampImgId();
        campImgService.updateCampImg(campImgId, campImgFile);

        return camp.getId();
    }

    public Long updateRoom(RoomFormDto roomFormDto, List<MultipartFile> roomImgFileList)
            throws Exception{
        Room room = roomRepository.findById(roomFormDto.getId()).
                orElseThrow(EntityNotFoundException::new);
        room.updateRoom(roomFormDto);

        List<Long> roomImgIds = roomFormDto.getRoomImgIds();

        for(int i =0; i<roomImgFileList.size();i++){
            roomImgService.updateItemImg(roomImgIds.get(i), roomImgFileList.get(i));
        }
        return room.getId();
    }



    /* Camp update */
    @Transactional(readOnly = true)
    public CampFormDto getCampDtl(Long campId) {
        CampImg campImg = campImgRepository.findByCampId(campId);

        CampImgDto campImgDto = CampImgDto.of(campImg);

        Camp camp = campRepository.findById(campId).orElseThrow(EntityNotFoundException::new);
        CampFormDto campFormDto = CampFormDto.of(camp);
        campFormDto.setCampImgDto(campImgDto);
        return campFormDto;
    }

    @Transactional
    public Page<CampSearchDto> getCampPage(SearchDto searchDto, Pageable pageable) throws Exception {
        return campRoomRepository.getCampPage(searchDto, pageable);
    }

    @Transactional(readOnly = true)
    public List<Camp> getAllCamp(String permission){
        return campRepository.findByCamp(permission);
        //permission 즉 NULL값을 들고 campRepository로 이동
        //List<Camp> 리턴
    }

    @Transactional(readOnly = true)
    public List<Long> getAllRoomByCamp(String campType){
        List<Room> roomList = campRoomRepository.findByRoom(campType); //"campType이 카라반인 RoomList를 뽑아옴"

        List<Long> campFilter = new ArrayList<>(); //새로운 campList를 만듬
        int count = 0;
        for(int i=0;i<roomList.size();i++){
            Long camp_id = roomList.get(i).getCamp().getId(); //해당 룸의 캠프 객체의 캠프아이디를 찾아옴.
            int j=0;
            for(;j<count;j++){
                if(campFilter.get(j) == camp_id){
                    break;
                }
            }
            if(count==j){
                campFilter.add(camp_id);
                count++;
            }


        }

        return campFilter;
    }

    public Long updatePermission(Long id,String result) throws Exception{
        Camp c = campRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        c.updatePermission(result);
        return c.getId();
    }

    public List<Long> getAllCampByCampType(String camping, String glamping, String  caravan){

        List<Room> roomList = roomRepository.findByRoom(camping, glamping, caravan);

        List<Long> campFilter = new ArrayList<>(); //새로운 campList를 만듬, 중복 제거
        int count = 0; //campiFilter 사이즈

        for(int i=0;i<roomList.size();i++){
            Long camp_id = roomList.get(i).getCamp().getId(); //해당 룸의 캠프 객체의 캠프아이디를 찾아옴.
            int j=0;
            for(;j<count;j++){
                if(campFilter.get(j).equals(camp_id)){
                    break;
                }
            }
            if(count==j){
                campFilter.add(camp_id);// 1
                count++;
            }

        }

        return campFilter;

    }

    public List<Camp> getAllCampByNatureArea(String mountain, String sea, String vally, String city, String area){
        List<Camp> campList = new ArrayList<>();
        //주변 환경과 지역을 모두 체크한 경우
        if((!mountain.equals("") || !sea.equals("") || !vally.equals("") || !city.equals("")) && !(area.equals(""))){
            campList = campRepository.findByNatureAreaAll(mountain, sea, vally, city, area);
            return campList;
        }
        //주변 환경만 체크한 경우
        if((!mountain.equals("") || !sea.equals("") || !vally.equals("") || !city.equals("")) && area.equals("")){
            campList = campRepository.findByNatureOrAreaOnly(mountain, sea, vally, city, area);
            return campList;
        }

        //지역만 체크한 경우
        if((mountain.equals("") && sea.equals("") && vally.equals("") && city.equals("")) && !(area.equals(""))){
            campList = campRepository.findByNatureOrAreaOnly(mountain, sea, vally, city, area);
            return campList;
        }
        return campList;
    }


    public List<Camp> getAllCampsForRankByDesc(){
        return campRepository.findCampsByCount();
    }

    public Camp getCampById(Long campId){
        return campRepository.findByCamp(campId);
    }
    public Integer getRatingAvg(Long campId){
        return campRepository.getRatingAvgByRating(campId);
    }



}