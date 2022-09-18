package com.camper.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoomImg is a Querydsl query type for RoomImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoomImg extends EntityPathBase<RoomImg> {

    private static final long serialVersionUID = 325442576L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoomImg roomImg = new QRoomImg("roomImg");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgName = createString("imgName");

    public final StringPath imgUrl = createString("imgUrl");

    public final StringPath oriImgName = createString("oriImgName");

    public final StringPath repImgYn = createString("repImgYn");

    public final QRoom room;

    public QRoomImg(String variable) {
        this(RoomImg.class, forVariable(variable), INITS);
    }

    public QRoomImg(Path<? extends RoomImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoomImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoomImg(PathMetadata metadata, PathInits inits) {
        this(RoomImg.class, metadata, inits);
    }

    public QRoomImg(Class<? extends RoomImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new QRoom(forProperty("room"), inits.get("room")) : null;
    }

}

