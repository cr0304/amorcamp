package com.camper.dto;


import com.camper.entity.RoomImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;


@Getter
@Setter
public class RoomImgDto {
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName; // 원본 이미지 파일명

    private String imgUrl;// 이미지 조회 경로

    private String repImgYn; //대표이미지 여부

    private static ModelMapper modelMapper = new ModelMapper();

    public static RoomImgDto of(RoomImg roomImg){
        return modelMapper.map(roomImg,RoomImgDto.class);
    }


}
