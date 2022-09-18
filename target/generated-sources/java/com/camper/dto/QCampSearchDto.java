package com.camper.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.camper.dto.QCampSearchDto is a Querydsl Projection type for CampSearchDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCampSearchDto extends ConstructorExpression<CampSearchDto> {

    private static final long serialVersionUID = -312553356L;

    public QCampSearchDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> campName, com.querydsl.core.types.Expression<String> campArea, com.querydsl.core.types.Expression<String> surroundings, com.querydsl.core.types.Expression<String> campInfo, com.querydsl.core.types.Expression<String> imgUrl, com.querydsl.core.types.Expression<String> permission) {
        super(CampSearchDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, String.class, String.class}, id, campName, campArea, surroundings, campInfo, imgUrl, permission);
    }

}

