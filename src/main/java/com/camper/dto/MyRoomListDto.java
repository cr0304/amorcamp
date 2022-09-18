package com.camper.dto;

import com.camper.entity.RoomImg;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Getter
@Setter
public class MyRoomListDto {

    private Long id;

    private String roomName;

    private Integer roomPrice;

    private String roomShortInfo;

    private String roomDetailInfo;

    private String campType;

    private String imgUrl;

    private String campArea;

    private String surroundings;

    private String subImgUrl1;
    private String subImgUrl2;
    private String subImgUrl3;
    private String subImgUrl4;


    public MyRoomListDto()
    {

    }
    @QueryProjection //쿼리 dsl 결과 조회시 MainItemDto 객체로 바로 오도록 활용
    public MyRoomListDto(Long id, String roomName, Integer roomPrice , String roomShortInfo,
                         String roomDetailInfo, String campType, String imgUrl,
                         String surroundings,String campArea){
        this.id = id;
        this.roomName = roomName;
        this.roomPrice = roomPrice;
        this.roomShortInfo = roomShortInfo;
        this.roomDetailInfo = roomDetailInfo;
        this.campType = campType;
        this.imgUrl = imgUrl;
        this.surroundings = surroundings;
        this.campArea = campArea;
    }



}