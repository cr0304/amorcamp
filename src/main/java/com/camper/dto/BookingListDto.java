package com.camper.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingListDto {
    private Long id;
    private String campName;

    private String roomName;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private String imgUrl;

    private String roomShortInfo;

    private String roomDetailInfo;

    private String campType;

    private Integer roomPrice;

    private Long campId;

    private Long roomId;

    private String campArea;

    private String surroundings;

    private String bStatus; // bComplete _ bCancel _ bUseEnd

    public BookingListDto()
    {

    }
    @QueryProjection //쿼리 dsl 결과 조회시 MainItemDto 객체로 바로 오도록 활용
    public BookingListDto(Long id, String campName, String roomName , LocalDate checkIn,
                          LocalDate checkOut, String imgUrl, String roomShortInfo,
                          String roomDetailInfo,String campType,Integer roomPrice,
                          Long campId, String campArea, String surroundings, Long roomId,String bStatus){
        this.id = id;
        this.campName = campName;
        this.roomName = roomName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.imgUrl = imgUrl;
        this.roomShortInfo = roomShortInfo;
        this.roomDetailInfo = roomDetailInfo;
        this.campType = campType;
        this.roomPrice = roomPrice;
        this.campId = campId;
        this.campArea = campArea;
        this.surroundings = surroundings;
        this.roomId = roomId;
        this.bStatus = bStatus;
    }

}