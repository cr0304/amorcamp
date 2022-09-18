package com.camper.service;

import com.camper.entity.RoomImg;
import com.camper.repository.RoomImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomImgService {

    @Value("${roomImgLocation}")
    private String roomImgLocation;

    private final RoomImgRepository roomImgRepository;

    private final FileService fileService;
    public void saveRoomImg(RoomImg roomImg, MultipartFile roomImgFile) throws Exception {
        String oriImgName = roomImgFile.getOriginalFilename(); //오리지날 이미지 경로
        String imgName = "";
        String imgUrl = "";
        System.out.println(oriImgName);

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(roomImgLocation, oriImgName, roomImgFile.getBytes());
            System.out.println(imgName);
            imgUrl = "/images/item2/"+imgName;
        }
        //객실 이미지 정보 저장
        //oriImgName : 객실 이미지 파일의 원래 이름
        //imgName : 실제 로컬에 저장된 객실 이미지 파일 이름
        //imgUrl: 로컬에 저장된 객실 이미지 파일을 불러오는 경로
        roomImg.updateRoomImg(oriImgName, imgName, imgUrl);
        roomImgRepository.save(roomImg);
    }

    public void updateItemImg(Long roomImgId, MultipartFile roomImgFile) throws Exception {
        if (!roomImgFile.isEmpty()) { // 상품의 이미지를 수정한 경우 상품 이미지 업데이트
            RoomImg savedItemImg = roomImgRepository.findById(roomImgId).
                    orElseThrow(EntityNotFoundException::new); // 기존 엔티티 조회
            // 기존에 등록된 상품 이미지 파일이 있는경우 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(roomImgLocation + "/" + savedItemImg.getImgName());
            }
            String oriImgName = roomImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(roomImgLocation, oriImgName,
                    roomImgFile.getBytes()); // 파일 업로드
            String imgUrl = "/images/item2/" + imgName;
            //변경된 상품 이미지 정보를 세팅
            //상품 등록을 하는 경우에는 ItemImgRepository.save()로직을 호출 하지만
            //호출을 하지 않았습니다.
            //savedItemImg 엔티티는 현재 영속성 상태이다.
            // 그래서 데이터를 변경하는 것만으로 변경을 감지기능이 동작
            // 트랜잭션이 끝날때 update 쿼리가 실행 된다.
            //※ 영속성 상태여야함 사용가능
            savedItemImg.updateRoomImg(oriImgName, imgName, imgUrl);
        }
    }
}
