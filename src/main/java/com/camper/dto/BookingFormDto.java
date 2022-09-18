package com.camper.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
public class BookingFormDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String customer;

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    private String phoneNumber;

    private String campName;

    private String roomName;

    private String checkIn;

    private String checkOut;

    private Integer price;

    private String bStatus;

    private Long roomId;

    private String email;

    private int count = 0;

}