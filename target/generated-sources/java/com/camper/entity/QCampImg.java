package com.camper.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCampImg is a Querydsl query type for CampImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCampImg extends EntityPathBase<CampImg> {

    private static final long serialVersionUID = -504776534L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCampImg campImg = new QCampImg("campImg");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final QCamp camp;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgName = createString("imgName");

    public final StringPath imgUrl = createString("imgUrl");

    public final StringPath oriImgName = createString("oriImgName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QCampImg(String variable) {
        this(CampImg.class, forVariable(variable), INITS);
    }

    public QCampImg(Path<? extends CampImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCampImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCampImg(PathMetadata metadata, PathInits inits) {
        this(CampImg.class, metadata, inits);
    }

    public QCampImg(Class<? extends CampImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.camp = inits.isInitialized("camp") ? new QCamp(forProperty("camp")) : null;
    }

}

