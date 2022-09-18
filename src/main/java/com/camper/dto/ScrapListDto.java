package com.camper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrapListDto {
    private Long scrapCampId;
    private String campName;
    private String imgUrl;

    public ScrapListDto(Long scrapCampId, String campName, String imgUrl){
        this.scrapCampId = scrapCampId;
        this.campName = campName;
        this.imgUrl = imgUrl;
    }
}
