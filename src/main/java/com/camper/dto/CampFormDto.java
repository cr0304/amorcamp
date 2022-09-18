package com.camper.dto;

import com.camper.entity.Camp;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CampFormDto {

    private Long id;

    @NotBlank(message = "캠핑장 이름을 입력해주세요.")
    @Length(min = 2, max = 13, message = "캠프장 이름은 2자 이상, 13자 이하로 입력해주세요.")
    private String campName;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "사업자 번호를 입력해주세요.")
    @Length(min = 10, max = 10, message = "사업자 번호는 '-' 를 제외한 10자입니다.")
    private String bNumber;

    @NotBlank(message = "지역을 선택해주세요.")
    private String campArea;

    @NotBlank(message = "주변환경을 선택해주세요.")
    private String surroundings;

    @NotBlank(message = "입실시간을 입력해주세요.")
    private String inTime;

    @NotBlank(message = "퇴실시간을 입력해주세요.")
    private String outTime;

    @NotBlank(message = "캠핑장 정보를 입력해주세요.")
    @Length(min = 20, max = 100, message = "캠핑장 정보는 20자 이상 100자 이하로 입력해주세요.")
    private String campInfo;

    private CampImgDto campImgDto;

    private Long campImgId;

    private String permission;

    private int ratingAvg;

    private static ModelMapper modelMapper = new ModelMapper();

    public Camp createCamp(){
        return modelMapper.map(this, Camp.class);
    }

    public static CampFormDto of(Camp camp){
        return modelMapper.map(camp, CampFormDto.class);
    }



}