package com.camper.repository;

import com.camper.dto.CampSearchDto;
import com.camper.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CampRoomRepositoryCustom{
    Page<CampSearchDto> getCampPage(SearchDto searchDto , Pageable pageable) throws Exception;

}
