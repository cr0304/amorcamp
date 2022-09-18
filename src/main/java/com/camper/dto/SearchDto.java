package com.camper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {

    private String permission = "OK";

    private String searchBy;

    private String searchQuery = "";
}
