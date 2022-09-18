package com.camper.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampSearchDto {
    private Long id;

    private String campName;

    private String campArea;

    private String address;

    private String inTime;

    private String outTime;

    private String surroundings;

    private String campInfo;


    private String imgUrl;

    private String Permission;

    private Integer max;

    private Integer min;

    public CampSearchDto()
    {

    }
    @QueryProjection //쿼리 dsl 결과 조회시 MainItemDto 객체로 바로 오도록 활용
    public CampSearchDto(Long id, String campName,String campArea , String surroundings, String campInfo, String imgUrl,String permission){
        this.id = id;
        this.campName = campName;
        this.campArea = campArea;
        this.surroundings = surroundings;
        this.campInfo = campInfo;
        this.imgUrl = imgUrl;
        this.Permission = permission;
    }

}