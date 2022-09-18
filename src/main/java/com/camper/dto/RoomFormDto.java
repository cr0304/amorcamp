package com.camper.dto;

import com.camper.constant.RoomStatus;
import com.camper.entity.Room;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoomFormDto {

    private Long id;

    @NotBlank(message = "객실 이름을 입력해주세요.")
    @Length(min = 2, max = 13, message = "객실 이름은 2자 이상 13자 이하로 입력해주세요.")
    private String roomName; //객실이름

    @NotNull(message = "객실 가격을 입력해주세요.")
    @Positive
    private Integer roomPrice; //객실가격

    @NotBlank(message = "객실의 간단한 소개를 입력해주세요.")
    @Length(min = 2, max = 25, message = "객실 소개는 2자 이상 25자 이하로 입력해주세요.")
    private String roomShortInfo; //객실 간단 소개

    @NotBlank(message = "객실 상세 정보를 입력해주세요.")
    @Length(min = 10, max = 300, message = "객실 정보는 10자 이상 300자 이하로 입력해주세요.")
    private String roomDetailInfo; //객실정보

    private RoomStatus roomStatus = RoomStatus.NO_BOOKING; //예약상태

    @NotBlank(message = "캠핑 유형을 선택해주세요.")
    private String campType;

    private List<RoomImgDto> roomImgDtoList = new ArrayList<>(); //객실 이미지 정보

    private List<Long> roomImgIds = new ArrayList<>(); //객실 이미지 아이디들

    private static ModelMapper modelMapper = new ModelMapper();
    public Room createRoom() {
        return modelMapper.map(this, Room.class);
    }

    public static RoomFormDto of(Room room){
        return modelMapper.map(room, RoomFormDto.class);
    }
}