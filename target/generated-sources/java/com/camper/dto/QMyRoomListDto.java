package com.camper.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.camper.dto.QMyRoomListDto is a Querydsl Projection type for MyRoomListDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMyRoomListDto extends ConstructorExpression<MyRoomListDto> {

    private static final long serialVersionUID = 1609188504L;

    public QMyRoomListDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> roomName, com.querydsl.core.types.Expression<Integer> roomPrice, com.querydsl.core.types.Expression<String> roomShortInfo, com.querydsl.core.types.Expression<String> roomDetailInfo, com.querydsl.core.types.Expression<String> campType, com.querydsl.core.types.Expression<String> imgUrl) {
        super(MyRoomListDto.class, new Class<?>[]{long.class, String.class, int.class, String.class, String.class, String.class, String.class}, id, roomName, roomPrice, roomShortInfo, roomDetailInfo, campType, imgUrl);
    }

}

